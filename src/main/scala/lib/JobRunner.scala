package lib

import io.flow.common.v0.models.UserReference
import lib.db.generated.generic.GeneratedGenericJobInstancesDao
import lib.db.generated.generic.GeneratedGenericJobInstancesDao._
import lib.generated.models.{JobError, JobInstance}
import lib.util.{Deserializer, Serializer}
import play.api.libs.json.{JsObject, JsValue}

class JobRunner[J, I, O, E <: JobError] {
  def run(job: J, jobInput: I)
    (implicit runStrategy: RunStrategy[J, I, O, E],
      dao: GeneratedGenericJobInstancesDao[J, I, O, E],
      dsj: Deserializer[J, JsObject],
      dsi: Deserializer[Option[I], Option[JsObject]],
      dso: Deserializer[Option[O], Option[JsObject]],
      dse: Deserializer[Option[List[E]], Option[List[JsObject]]],
      sj: Serializer[J, JsValue],
      si: Serializer[Option[I], Option[JsValue]],
      so: Serializer[Option[O], Option[JsValue]],
      se: Serializer[Option[List[E]], Option[List[JsValue]]]
    ): Either[JobError, JobInstance[J, I, O, E]] =

    // reusable implementation to pick up correct job run strategy
    runStrategy(job, jobInput)
}

trait RunStrategy[J, I, O, E <: JobError] {
  def apply(job: J, input: I)(
    implicit dao: GeneratedGenericJobInstancesDao[J, I, O, E],
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]],
    sj: Serializer[J, JsValue],
    si: Serializer[Option[I], Option[JsValue]],
    so: Serializer[Option[O], Option[JsValue]],
    se: Serializer[Option[List[E]], Option[List[JsValue]]]
  ): Either[JobError, JobInstance[J, I, O, E]] = {

    // reusable implementation for job instance database persistence
    for {
      newJobInstance <- dao.insert(UserReference("system"), newJobInstanceForm(job, input))
      ranJobInstance <- run(newJobInstance, input)
      savedRecoveredJobInstance <- dao.insert(UserReference("system"), ranJobInstance)
    } yield savedRecoveredJobInstance
  }

  // override to specify what it means to run a job
  def run(jobInstance: JobInstance[J, I, O, E], input: I): Either[JobError, JobInstance[J, I, O, E]]
}