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

    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError

    ] {

    override def recover(
      jobInstance: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError],
      input: Day
    ): Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]] = {

      // TODO: implement here (example: re-run the job to recover)
      val jobRunner = new JobRunner[
        MyDailyEtlJob.type,
        Day,
        TotalDailyRevenueByOrganization,
        JobError
        ]

      jobRunner.run(jobInstance.job, input)

    }
  }

}
