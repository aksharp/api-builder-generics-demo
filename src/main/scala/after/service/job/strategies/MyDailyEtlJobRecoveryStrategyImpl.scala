package after.service.job.strategies

import after.lib.generated.models._
import after.lib.{JobRunner, RecoveryStrategy}
import after.service.AppStart
import after.service.generated.models.Job._
import after.service.generated.models._
import after.service.job.strategies.MyDailyEtlJobRunStrategyImpl._
import after.service.generated.json._
import after.lib.generated.json._

object MyDailyEtlJobRecoveryStrategyImpl extends AppStart {

  implicit object MyDailyEtlJobRecoveryStrategy extends RecoveryStrategy[

    // types
    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError

    ] {

    override def recover(
      jobInstance: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError],
      input: Day
    ): Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]] = {

      // implementation (example: re-run the job to recover)
      new JobRunner[
        MyDailyEtlJob.type,
        Day,
        TotalDailyRevenueByOrganization,
        JobError
        ].run(
          job = jobInstance.job,
          jobInput = input
        )

    }
  }

}
