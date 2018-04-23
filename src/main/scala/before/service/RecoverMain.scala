package before.service

import before.service.jobs.MyDailyEtlJobRecoverer
import io.flow.my.service.before.generic.models.Job.MyDailyEtlJob
import io.flow.my.service.before.generic.models._
import org.joda.time.DateTime

object RecoverMain extends AppStart {

  val jobRecoverer = new MyDailyEtlJobRecoverer

  jobRecoverer.recoverJob(
    job = MyDailyEtlJob,
    key = "some key",
    input = Option(
      Day(DateTime.now)
    )
  )

}
