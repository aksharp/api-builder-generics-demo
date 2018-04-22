package service

import com.typesafe.config.ConfigFactory
import io.flow.common.v0.models.UserReference
import io.flow.user.v0.Constants
import lib.JobRunner
import lib.db.JobStorage
import lib.db.generated.JobInstancesDao
import lib.generated.models.{JobError, JobInstance, JobInstanceForm}
import org.joda.time.DateTime
import play.api.Environment
import play.api.db._
import service.Implementations._
import service.generated.json._
import service.generated.models.Job._
import service.generated.models._

object Main extends App {

  val jobRunner = new JobRunner[
      MyDailyEtlJob.type ,
      Day,
      TotalDailyRevenueByOrganization,
      JobError
    ]

  val config = ConfigFactory.load()
  val env = Environment.simple()
  val pool = new HikariCPConnectionPool(env)
  val db = new PooledDatabase("jobs", config, env, pool)
  val dao = new JobInstancesDao(db)

  val myDailyEtlJobDao = new JobStorage[MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError](dao)

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


  /*
   * Compile-time Type Check
   *  value guaranteed to be of type: Either[JobError, JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]]
   */
  private val value  = myDailyEtlJobDao.findById("123")



  /*
   * Compile-time Type Check / Validation
   *  form accepted guaranteed to be validated for type: JobInstance[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError]
   */
//  myDailyEtlJobDao.insert(
//    user = UserReference("system"),
//    form = JobInstanceForm(
//      key = "key",
//      job = MyDailyEtlJob,
//      input = Option(Day(DateTime.now)),
//      output = None,
//      errors = None
//    )
//  )

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
