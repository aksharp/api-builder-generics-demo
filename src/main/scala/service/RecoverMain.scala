package service

import lib.JobRecovery
import lib.generated.models.JobError
import org.joda.time.DateTime
import service.generated.json._
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRecoveryStrategyImpl._

object RecoverMain extends AppStart {

  new JobRecovery[

    // types
    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError

    ].recover(

      // values
      job = MyDailyEtlJob,
      key ="key",
      jobInput = Day(DateTime.now)

    )


}
