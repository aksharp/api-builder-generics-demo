package service.job.strategies

import lib.generated.models._
import lib.{JobRunner, RecoveryStrategy}
import service.AppStart
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRunStrategyImpl._
import service.generated.json._

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
