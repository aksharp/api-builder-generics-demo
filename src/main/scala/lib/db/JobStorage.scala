package lib.db

import io.flow.common.v0.models.UserReference
import lib.db.generated.JobInstancesDao
import lib.generated.models._
import JobStorage._
import com.typesafe.config.ConfigFactory
import lib.serde._
import play.api.libs.json.{JsObject, JsValue}

import scala.language.implicitConversions
import scala.util.Try

object JobStorage {
  private val databaseName = ConfigFactory.load().getString("db.name") // TODO: refactor

  implicit def toJobInstanceForm[J, I, O, E <: JobError](form: JobInstanceForm[J, I, O, E])(
    implicit
    sj: Serializer[J, JsValue],
    si: Serializer[Option[I], Option[JsValue]],
    so: Serializer[Option[O], Option[JsValue]],
    se: Serializer[Option[List[E]], Option[List[JsValue]]]
  ): lib.db.generated.JobInstanceForm = {
    lib.db.generated.JobInstanceForm(
      key = form.key,
      job = sj.serialize(form.job),
      input= si.serialize(form.input),
      output = so.serialize(form.output),
      errors = se.serialize(form.errors)
    )
  }

  /* Note: this can be tested by just calling it with expected types and if it compiles, it is correct
      specific type serialization/deserialization can be tested on value level with unit tests
   */
  implicit def fromDbJobInstance[J, I, O, E <: JobError](dbJobInstance: lib.db.generated.JobInstance)(
    implicit
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]]
    ): Either[JobError, JobInstance[J, I, O, E]] = {
    (
      dsj.deserialize(dbJobInstance.job),
      dsi.deserialize(dbJobInstance.input),
      dso.deserialize(dbJobInstance.output),
      dse.deserialize(dbJobInstance.errors)
    ) match {
      case (job, maybeInput, maybeOutput, maybeErrors) =>
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
    serdeJ: Serializer[J, JsValue],
    serdeI: Serializer[Option[I], Option[JsValue]],
    serdeO: Serializer[Option[O], Option[JsValue]],
    serdeE: Serializer[Option[List[E]], Option[List[JsValue]]]
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
    serdeJ: Deserializer[J, JsObject],
    serdeI: Deserializer[Option[I], Option[JsObject]],
    serdeO: Deserializer[Option[O], Option[JsObject]],
    serdeE: Deserializer[Option[List[E]], Option[List[JsObject]]]
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