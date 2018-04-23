package before.service.jobs

import before.lib.JobRecoverer
import before.lib.db.generated.JobInstance
import before.service.AppStart
import io.flow.common.v0.models.UserReference
import io.flow.my.service.before.generic.models.Job.MyDailyEtlJob
import io.flow.my.service.before.generic.models.json._
import io.flow.my.service.before.generic.models.{Day, Job, TotalDailyRevenueByOrganization}
import play.api.libs.json.{JsObject, Json}

class MyDailyEtlJobRecoverer extends JobRecoverer[MyDailyEtlJob.type, Day] with AppStart {

  override def recover(jobInstance: JobInstance, input: Option[Day]): JobInstance = {
    jobInstance.copy(
      output = Option(
        // serialization boilerplate (not even sure this is the correct way to serialize)
        JsObject(Map("output" -> Json.toJson(TotalDailyRevenueByOrganization("flow", 1234567.89))))
      )
    )
  }

}
