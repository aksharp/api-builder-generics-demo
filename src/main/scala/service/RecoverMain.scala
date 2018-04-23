package service

import lib.JobRecovery
import lib.generated.models.JobError
import org.joda.time.DateTime
import service.generated.json._
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRecoveryStrategyImpl._

object RecoverMain extends AppStart {

  val jobRecovery = new JobRecovery[
    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError
    ]

  jobRecovery.recover(MyDailyEtlJob, "key", Day(DateTime.now))


}
