package after.lib

import io.flow.common.v0.models.UserReference
import after.lib.db.generated.generic.GeneratedGenericJobInstancesDao
import after.lib.db.generated.generic.GeneratedGenericJobInstancesDao._
import after.lib.generated.models.{JobError, JobInstance}
import after.lib.util.{Deserializer, Serializer}
import play.api.libs.json.{JsObject, JsValue}

class JobRecovery[J, I, O, E <: JobError] {
  def recover(job: J, key: String, jobInput: I)
    (implicit recoveryStrategy: RecoveryStrategy[J, I, O, E],
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

    // reusable implementation to pick up correct recovery strategy
    recoveryStrategy(job, key, jobInput)
}

trait RecoveryStrategy[J, I, O, E <: JobError] {
  def apply(job: J, key: String, input: I)(
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
      lastIncompleteJobInstance <- dao.findLastIncomplete(key)
      recoveredJobInstance <- recover(lastIncompleteJobInstance, input)
      savedRecoveredJobInstance <- dao.insert(UserReference("system"), recoveredJobInstance)
    } yield savedRecoveredJobInstance

  }

  // override to specify what it means to recover a failed job
  def recover(jobInstance: JobInstance[J, I, O, E], input: I): Either[E, JobInstance[J, I, O, E]]
}