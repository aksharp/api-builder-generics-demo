package service

import lib.JobRunner
import lib.generated.models.JobError
import org.joda.time.DateTime
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRunStrategyImpl._
import service.generated.json._
import lib.generated.json._

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
