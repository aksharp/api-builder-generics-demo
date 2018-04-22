package lib

import io.flow.common.v0.models.UserReference
import lib.db.JobInstanceStorage
import lib.db.JobInstanceStorage._
import lib.generated.models.{JobError, JobInstance}
import lib.serde.{Deserializer, Serializer}
import play.api.libs.json.{JsObject, JsValue}

class JobRecovery[J, I, O, E <: JobError] {
  def recover(job: J, key: String, jobInput: I)
    (implicit recoveryStrategy: RecoveryStrategy[J, I, O, E],
              dao: JobInstanceStorage[J, I, O, E],
              dsj: Deserializer[J, JsObject],
              dsi: Deserializer[Option[I], Option[JsObject]],
              dso: Deserializer[Option[O], Option[JsObject]],
              dse: Deserializer[Option[List[E]], Option[List[JsObject]]],
              sj: Serializer[J, JsValue],
              si: Serializer[Option[I], Option[JsValue]],
              so: Serializer[Option[O], Option[JsValue]],
              se: Serializer[Option[List[E]], Option[List[JsValue]]]
    ): Either[JobError, JobInstance[J, I, O, E]] =
    recoveryStrategy(job, key, jobInput)
}

trait RecoveryStrategy[J, I, O, E <: JobError] {
  def apply(job: J, key: String, input: I)(
    implicit dao: JobInstanceStorage[J, I, O, E],
    dsj: Deserializer[J, JsObject],
    dsi: Deserializer[Option[I], Option[JsObject]],
    dso: Deserializer[Option[O], Option[JsObject]],
    dse: Deserializer[Option[List[E]], Option[List[JsObject]]],
    sj: Serializer[J, JsValue],
    si: Serializer[Option[I], Option[JsValue]],
    so: Serializer[Option[O], Option[JsValue]],
    se: Serializer[Option[List[E]], Option[List[JsValue]]]
  ): Either[JobError, JobInstance[J, I, O, E]] = {
    for {
      lastIncompleteJobInstance <- dao findLastIncomplete(key)
      recoveredJobInstance <- recover(lastIncompleteJobInstance, input)
      savedRecoveredJobInstance <- dao.insert(UserReference("system"), recoveredJobInstance)
    } yield savedRecoveredJobInstance
  }

  def recover(jobInstance: JobInstance[J, I, O, E], input: I): Either[E, JobInstance[J, I, O, E]]
}