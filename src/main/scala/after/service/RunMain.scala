package after.service

import after.lib.JobRunner
import after.lib.generated.models.JobError
import org.joda.time.DateTime
import after.service.generated.models.Job._
import after.service.generated.models._
import after.service.job.strategies.MyDailyEtlJobRunStrategyImpl._
import after.service.generated.json._
import after.lib.generated.json._

object RunMain extends AppStart {

  new JobRunner[

    // types
    MyDailyEtlJob.type,
    Day,
    TotalDailyRevenueByOrganization,
    JobError

    ].run(

      // values
      job = MyDailyEtlJob,
      jobInput = Day(DateTime.now)

    )

}
