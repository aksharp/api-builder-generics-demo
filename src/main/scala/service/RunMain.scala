package service

import lib.JobRunner
import lib.generated.models.JobError
import org.joda.time.DateTime
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRunStrategyImpl._

object RunMain extends AppStart {

  val jobRunner = new JobRunner[
    MyDailyEtlJob.type ,
    Day,
    TotalDailyRevenueByOrganization,
    JobError
    ]

  jobRunner.run(MyDailyEtlJob, Day(DateTime.now))

}
