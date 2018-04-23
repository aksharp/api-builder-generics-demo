package before.lib

import before.lib.db.generated.{JobInstance, JobInstanceForm, JobInstancesDao}
import io.flow.common.v0.models.UserReference
import play.api.libs.json.{JsValue, Json, Writes}

trait JobRunner[J, I] {

  val dao: JobInstancesDao

  def run(jobInstance: JobInstance, input: Option[I]): JobInstance

  def newJobInstance(job: J, input: Option[I]): Option[JobInstance]


  def runJob(job: J, input: Option[I]): Unit = {

    val maybeJobInstance = newJobInstance(job, input)

    maybeJobInstance.foreach { jobInstance =>

      dao.insert(
        updatedBy = UserReference("system"),
        form = jobInstance.form)

      val ranJobInstance: JobInstance = run(jobInstance, input)

      dao.insert(
        updatedBy = UserReference("system"),
        form = ranJobInstance.form)
    }

  }

  def newJobInstanceForm(job: JsValue, input: Option[JsValue]) =
    JobInstanceForm(
      key = "generate key",
      job = job,
      input = input,
      output = None,
      errors = None
    )


}
