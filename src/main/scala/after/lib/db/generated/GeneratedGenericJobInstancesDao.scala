package after.lib.db.generated.generic

import io.flow.common.v0.models.UserReference
import after.lib.db.generated.JobInstancesDao
import after.lib.generated.models._
import GeneratedGenericJobInstancesDao._
import com.typesafe.config.ConfigFactory
import after.lib.util._
import play.api.libs.json.{JsObject, JsValue}

import scala.language.implicitConversions
import scala.util.Try

//TODO: ---------------------------------------------------
//TODO: -----NEW TYPED DAO & TYPE CONVERSIONS--------------
//TODO: ---------------------------------------------------

class GeneratedGenericJobInstancesDao[J, I, O, E <: JobError](
  dao: JobInstancesDao
) {

  def insert(user: UserReference, form: JobInstanceForm[J, I, O, E])(
    implicit
    sj: Serializer[J, JsValue],
    si: Serializer[Option[I], Option[JsValue]],
    so: Serializer[Option[O], Option[JsValue]],
    se: Serializer[Option[List[E]], Option[List[JsValue]]],
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]]
  ): Either[JobError, JobInstance[J, I, O, E]] = {
    Try(dao.insert(
      updatedBy = user,
      form = form)
    ).fold(
      err => Left(JobDatabaseError(databaseName, s"insert job instance $form by user $user", err.getMessage)),
      good => findById(good)
    )
  }

  def findById(id: String)(
    implicit
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]]
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

  def findLastIncomplete(key: String)(
    implicit
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]]
  ): Either[JobError, JobInstance[J, I, O, E]] = {
    dao
      .findAll(limit = 1)(q =>
        q.equals("key", key).orderBy("created_at desc"))
      .headOption
      .map(fromDbJobInstance[J, I, O, E])
      .getOrElse(
        Left(
          JobDatabaseError(databaseName, "find last incomplete job instance", s"job instance with key $key was not found")
        )
      )
  }
}

object GeneratedGenericJobInstancesDao {
  private val databaseName = ConfigFactory.load().getString("db.name") // refactor to typed config

  implicit def toDbJobInstanceForm[J, I, O, E <: JobError](form: JobInstanceForm[J, I, O, E])(
    implicit
    sj: Serializer[J, JsValue],
    si: Serializer[Option[I], Option[JsValue]],
    so: Serializer[Option[O], Option[JsValue]],
    se: Serializer[Option[List[E]], Option[List[JsValue]]]
  ): after.lib.db.generated.JobInstanceForm = {
    after.lib.db.generated.JobInstanceForm(
      key = form.key,
      job = sj.serialize(form.job),
      input= si.serialize(form.input),
      output = so.serialize(form.output),
      errors = se.serialize(form.errors)
    )
  }

  implicit def toJobInstanceForm[J, I, O, E <: JobError](jobInstance: JobInstance[J, I, O, E]): JobInstanceForm[J, I, O, E] = {
    JobInstanceForm(
      key = jobInstance.key,
      job = jobInstance.job,
      input= jobInstance.input,
      output = jobInstance.output,
      errors = jobInstance.errors
    )
  }

  def newJobInstanceForm[J, I, O, E <: JobError](job: J, input: I): JobInstanceForm[J, I, O, E] = {
    JobInstanceForm[J, I, O, E](
      key = "generate key here",
      job = job,
      input= Option(input),
      output = None,
      errors = None
    )
  }

  /* Note: this can be tested by just calling it with expected types and if it compiles, it is correct
      specific type serialization/deserialization can be tested on value level with unit tests
   */
  implicit def fromDbJobInstance[J, I, O, E <: JobError](dbJobInstance: after.lib.db.generated.JobInstance)(
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