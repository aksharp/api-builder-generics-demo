package service.job.strategies

import lib.RunStrategy
import lib.generated.models._
import service.generated.models.Job._
import service.generated.models._

object MyDailyEtlJobRunStrategyImpl {

  implicit object MyDailyEtlJobRunStrategy extends RunStrategy[

    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError

    ] {

    override def run(
      jobInstance: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError],
      input: Day
    ): Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]] = {

      // TODO: implement here
      Right(JobInstance("id", "key", MyDailyEtlJob, Option(input), Option(TotalDailyRevenueByOrganization("flow", 1234567.89)), None))

    }
  }


}
