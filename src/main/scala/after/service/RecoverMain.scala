package after.service

import after.lib.JobRecovery
import after.lib.generated.models.JobError
import org.joda.time.DateTime
import after.lib.generated.json._
import after.service.generated.json._
import after.service.generated.models.Job._
import after.service.generated.models._
import after.service.job.strategies.MyDailyEtlJobRecoveryStrategyImpl._

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
