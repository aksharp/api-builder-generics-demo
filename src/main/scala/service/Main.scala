package service

import lib.JobRunner
import service.generated.{MyDailyEtlJob, _}
import lib.generated._
import org.joda.time.DateTime
import Implementations._
import com.typesafe.config.{Config, ConfigFactory}
import lib.db.JobStorage
import lib.db.generated.JobInstancesDao
import play.api.Environment
import play.api.db._

object Main extends App {

  val jobRunner = new JobRunner[
      MyDailyEtlJob.type,
      Day,
      TotalDailyRevenueByOrganization,
      JobError
    ]

  val config = ConfigFactory.load()
  val env = Environment.simple()
  val pool = new HikariCPConnectionPool(env)
  val db = new PooledDatabase("jobs", config, env, pool)
  val dao = new JobInstancesDao(db)
  val storage = new JobStorage[MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError](dao)

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

  storage.findById("123")


  println(s"Have a great day!")

}
