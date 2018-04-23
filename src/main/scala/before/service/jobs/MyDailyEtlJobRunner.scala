package before.service.jobs

import before.lib.{JobRecoverer, JobRunner}
import before.lib.db.generated.JobInstance
import before.service.AppStart
import io.flow.common.v0.models.UserReference
import io.flow.my.service.before.generic.models.Job.MyDailyEtlJob
import io.flow.my.service.before.generic.models.{Day, Job, TotalDailyRevenueByOrganization}
import play.api.libs.json.{JsObject, Json}
import io.flow.my.service.before.generic.models.json._

class MyDailyEtlJobRunner extends JobRunner[MyDailyEtlJob.type, Day] with AppStart {

  override def run(jobInstance: JobInstance, input: Option[Day]): JobInstance = {
    jobInstance.copy(
      output = Option(
        // serialization boilerplate (not even sure this is the correct way to serialize)
        JsObject(Map("output" -> Json.toJson(TotalDailyRevenueByOrganization("flow", 1234567.89))))
      )
    )
  }

  // additional boilerplate code to store job instance
  override def newJobInstance(job: Job.MyDailyEtlJob.type, input: Option[Day]): Option[JobInstance] = {
    val id: String = dao.insert(
      updatedBy = UserReference("system"),
      form = newJobInstanceForm(
        // additional boilerplate code to serialize concrete Job type
        job = Json.toJson[Job.MyDailyEtlJob.type](job),
        // additional boilerplate code to serialize concrete Input type
        input = input.map(Json.toJson[Day])))
    val maybeJobInstance: Option[JobInstance] = dao.findById(id)
    maybeJobInstance
  }
}
