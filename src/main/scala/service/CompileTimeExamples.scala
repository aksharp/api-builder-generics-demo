package service

import io.flow.common.v0.models.UserReference
import lib.JobRunner
import lib.generated.models.{JobError, JobInstanceForm}
import org.joda.time.DateTime
import service.generated.json._
import lib.generated.json._
import service.generated.models.Job._
import service.generated.models._
import service.job.strategies.MyDailyEtlJobRunStrategyImpl._

object CompileTimeExamples extends AppStart {

  /* Define Job Runner with types
  *    will compile-time check correct run strategy
  */
  val myDailyEtlJobRunner = new JobRunner[
      MyDailyEtlJob.type ,
      Day,
      TotalDailyRevenueByOrganization,
      JobError
    ]

  /*
  * Compile-time Type Check
  *   compiles correctly because there is an implementation
  *   in scope (import Implementations._) for Job type: MyDailyEtlJob and JobInput type: Day
  */
  myDailyEtlJobRunner.run(MyDailyEtlJob, Day(DateTime.now))

  /*
  * Compile-time Type Check
  *   Does NOT compile because Job type is checked at compile time
  */
  // TODO: uncomment below to see compilation error
//  myDailyEtlJobRunner.run(MyLongRunningJob, Day(DateTime.now))

  /*
  * Compile-time Type Check
  *   Does NOT compile because Input type is checked at compile time
  */
  // TODO: uncomment below to see compilation error
//  myDailyEtlJobRunner.run(MyDailyEtlJob, "Some Input")


  /*
   * Compile-time Type Check
   *  value guaranteed to be of type: Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]]
   */
  private val value  = myDailyEtlJobDao.findById("123")


  /*
   * Compile-time Type Check / Validation
   *  form accepted guaranteed to be validated for type: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]
   */
  myDailyEtlJobDao.insert(
    user = UserReference("system"),
    form = JobInstanceForm(
      key = "key",
      job = MyDailyEtlJob,
      input = Option(Day(DateTime.now)),
      output = None,
      errors = None
    )
  )

  /*
   * Compile-time Type Check / Validation
   *  form accepted guaranteed to be validated for type: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]
   */
  // TODO: uncomment below to see compilation error
  //  myDailyEtlJobDao.insert(
  //    user = UserReference("system"),
  //    form = JobInstanceForm(
  //      key = "key",
  //      job = MyWeeklyEtlJob,
  //      input = Option(Day(DateTime.now)),
  //      output = None,
  //      errors = None
  //    )
  //  )


  println(
    s"""
       Have a great day!
              -- Your compiler
     """.stripMargin)

}
