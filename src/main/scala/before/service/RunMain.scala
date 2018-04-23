package before.service

import before.service.jobs.MyDailyEtlJobRunner
import io.flow.my.service.before.generic.models.Job.MyDailyEtlJob
import io.flow.my.service.before.generic.models._
import org.joda.time.DateTime

object RunMain extends AppStart {

  val jobRunner = new MyDailyEtlJobRunner

  jobRunner.runJob(
    job = MyDailyEtlJob,
    input = Option(
      Day(DateTime.now)
    )
  )

}
