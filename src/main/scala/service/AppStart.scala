package service

import com.typesafe.config.ConfigFactory
import lib.db.generated.JobInstancesDao
import lib.db.generated.generic.GeneratedGenericJobInstancesDao
import lib.generated.models.JobError
import play.api.Environment
import play.api.db.{HikariCPConnectionPool, PooledDatabase}
import service.generated.models.Job.MyDailyEtlJob
import service.generated.models.{Day, TotalDailyRevenueByOrganization}

trait AppStart extends App {

  val config = ConfigFactory.load()
  val env = Environment.simple()
  val pool = new HikariCPConnectionPool(env)
  val db = new PooledDatabase(config.getString("db.name"), config, env, pool)

  // in central place and also required to be specified per
  implicit val myDailyEtlJobDao =
    new GeneratedGenericJobInstancesDao[

      // types
      MyDailyEtlJob.type,
      Day,
      TotalDailyRevenueByOrganization,
      JobError

      ](
        // values
        dao =  new JobInstancesDao(db)
      )

}
