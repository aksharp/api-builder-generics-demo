package service

import com.typesafe.config.ConfigFactory
import lib.db.generated.generic.GeneratedGenericJobInstancesDao
import lib.db.generated.JobInstancesDao
import lib.generated.models.JobError
import play.api.Environment
import play.api.db.{HikariCPConnectionPool, PooledDatabase}
import service.generated.models.Job.MyDailyEtlJob
import service.generated.models.{Day, Job, TotalDailyRevenueByOrganization}

trait AppStart extends App {

  val config = ConfigFactory.load()
  val env = Environment.simple()
  val pool = new HikariCPConnectionPool(env)
  val db = new PooledDatabase(config.getString("db.name"), config, env, pool)
  val dao = new JobInstancesDao(db)
  implicit val myDailyEtlJobDao: GeneratedGenericJobInstancesDao[Job.MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError] = new GeneratedGenericJobInstancesDao[MyDailyEtlJob.type, Day, TotalDailyRevenueByOrganization, JobError](dao)

}
