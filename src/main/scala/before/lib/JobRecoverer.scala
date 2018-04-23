package before.lib

import before.lib.db.generated.{JobInstance, JobInstancesDao}
import io.flow.common.v0.models.UserReference

trait JobRecoverer[J, I] {

  val dao: JobInstancesDao

  def recover(jobInstance: JobInstance, input: Option[I]): JobInstance

  def recoverJob(job: J, key: String, input: Option[I]): Unit = {

    val maybeLastIncompleteJobInstance = dao
      .findAll(limit = 1)(q =>
        q.equals("key", key).orderBy("created_at desc"))
      .headOption

    maybeLastIncompleteJobInstance.foreach { jobInstance =>

      val recoveredJobInstance: JobInstance = recover(jobInstance, input)

      dao.insert(
        updatedBy = UserReference("system"),
        form = recoveredJobInstance.form)
    }

  }


}
