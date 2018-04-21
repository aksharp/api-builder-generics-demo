package service

import lib.JobRunner
import service.generated._
import lib.generated._
import org.joda.time.DateTime
import Implementations._

object Main extends App {

  val jobRunner = new JobRunner[
      MyDailyEtlJob.type,
      Day,
      TotalDailyRevenueByOrganization,
      JobError
    ]

  /*
  * Compile-time Type Check
  *   compiles correctly because there is an implementation
  *   in scope (import Implementations._) for Job type: MyDailyEtlJob and JobInput type: Day
  */
  jobRunner.run(MyDailyEtlJob, Day(DateTime.now))

  /*
  * Compile-time Type Check
  *   Does NOT compile because there is NO implementation
  *   in scope for Job type: MyLongRunningJob and JobInput type: Day
  */
  // TODO: uncomment below to see compilation error
  // jobRunner.run(MyLongRunningJob, Day(DateTime.now))

  /*
  * Compile-time Type Check
  *   Does NOT compile because there is NO implementation
  *   in scope for Job type: MyDailyEtlJob and JobInput type: String
  */
  // TODO: uncomment below to see compilation error
  // jobRunner.run(MyDailyEtlJob, "Some Input")


  println(s"Have a great day!")

}
