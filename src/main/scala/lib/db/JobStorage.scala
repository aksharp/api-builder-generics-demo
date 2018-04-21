package lib.db

import io.flow.common.v0.models.UserReference
import lib.db.generated.JobInstancesDao
import lib.generated._
import JobStorage._
import lib.Serde
import play.api.libs.json.{JsObject, JsValue}

import scala.language.implicitConversions
import scala.util.Try

object JobStorage {
  private val databaseName = "jobsDb"

  implicit def toJobInstanceForm[J, I, O, E <: JobError](form: JobInstanceForm[J, I, O, E])(
    implicit
    serdeJ: Serde[J, JsValue],
    serdeI: Serde[Option[I], Option[JsValue]],
    serdeO: Serde[Option[O], Option[JsValue]],
    serdeE: Serde[Option[List[E]], Option[List[JsValue]]]
  ): lib.db.generated.JobInstanceForm = {
    lib.db.generated.JobInstanceForm(
      key = form.key,
      job = Serde[J, JsValue].serialize(form.job),
      input= Serde[Option[I], Option[JsValue]].serialize(form.input),
      output = Serde[Option[O], Option[JsValue]].serialize(form.output),
      errors = Serde[Option[List[E]], Option[List[JsValue]]].serialize(form.errors)
    )
  }

  /* Note: this can be tested by just calling it with expected types and if it compiles, it is correct
      specific type serialization/deserialization can be tested on value level with unit tests
   */
  implicit def fromDbJobInstance[J, I, O, E <: JobError](dbJobInstance: lib.db.generated.JobInstance)(
    implicit
    serdeJ: Serde[J, JsObject],
    serdeI: Serde[Option[I], Option[JsObject]],
    serdeO: Serde[Option[O], Option[JsObject]],
    serdeE: Serde[Option[List[E]], Option[List[JsObject]]]
    ): Either[JobError, JobInstance[J, I, O, E]] = {
    (
      Serde[J, JsObject].deserialize(dbJobInstance.job),
      Serde[Option[I], Option[JsObject]].deserialize(dbJobInstance.input),
      Serde[Option[O], Option[JsObject]].deserialize(dbJobInstance.output),
      Serde[Option[List[E]], Option[List[JsObject]]].deserialize(dbJobInstance.errors)
    ) match {
      case (Some(job), Some(maybeInput), Some(maybeOutput), Some(maybeErrors)) =>
        Right(JobInstance[J, I, O, E](
          id = dbJobInstance.id,
          key = dbJobInstance.key,
          job = job,
          input = maybeInput,
          output = maybeOutput,
          errors = maybeErrors
        ))
      case _ => Left(JobDatabaseError(databaseName,"deserialize job instance","could not deserialize"))
    }
  }
}

class JobStorage[J, I, O, E <: JobError](
  dao: JobInstancesDao
) {

  def insert(user: UserReference, form: JobInstanceForm[J, I, O, E])(
    implicit
    serdeJ: Serde[J, JsValue],
    serdeI: Serde[Option[I], Option[JsValue]],
    serdeO: Serde[Option[O], Option[JsValue]],
    serdeE: Serde[Option[List[E]], Option[List[JsValue]]]
  ): Either[JobError, String] = {
    Try(dao.insert(
      updatedBy = user,
      form = form)
    ).fold(
      err => Left(JobDatabaseError(databaseName, s"insert job instance $form by user $user", err.getMessage)),
      good => Right(good)
    )
  }

  def findById(id: String)(
    implicit
    serdeJ: Serde[J, JsObject],
    serdeI: Serde[Option[I], Option[JsObject]],
    serdeO: Serde[Option[O], Option[JsObject]],
    serdeE: Serde[Option[List[E]], Option[List[JsObject]]]
  ): Either[JobError, JobInstance[J, I, O, E]] = {
    dao
      .findById(id)
      .map(fromDbJobInstance[J, I, O, E])
      .getOrElse(
        Left(
          JobDatabaseError(databaseName, "find by id", s"job instance with id $id was not found")
        )
      )
  }
}