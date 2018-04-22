package service

import lib.{JobRunner, RecoveryStrategy, RunStrategy}
import lib.generated.models._
import service.generated.models._
import service.generated.models.Job._

object Implementations {

  implicit object MyDailyEtlJobRunStrategy extends RunStrategy[
    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError
    ] {
    override def apply(job: Job.MyDailyEtlJob.type, input: Day): Either[
      JobError,
      JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]] =
      Right(JobInstance("id","key",MyDailyEtlJob,Option(input),Option(TotalDailyRevenueByOrganization("flow", 1234567.89)),None))
  }

  // ---------------------
  // Recovery Strategies
  // ---------------------
  implicit object MyDailyEtlJobRecoveryStrategy extends
    RecoveryStrategy[
      MyDailyEtlJob.type,
      Day,
      TotalDailyRevenueByOrganization,
      JobError
      ] {
    override def recover(

      jobInstance: JobInstance
        [Job.MyDailyEtlJob.type,
          Day,
          TotalDailyRevenueByOrganization,
          JobError
          ],

      input: Day

    ): Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]] = {
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
