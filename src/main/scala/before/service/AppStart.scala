package before.service

import before.lib.db.generated.JobInstancesDao
import com.typesafe.config.ConfigFactory
import play.api.Environment
import play.api.db.{HikariCPConnectionPool, PooledDatabase}

trait AppStart extends App {

  // app setup
  val config = ConfigFactory.load()
  val env = Environment.simple()
  val pool = new HikariCPConnectionPool(env)
  val db = new PooledDatabase(config.getString("db.name"), config, env, pool)
  val dao = new JobInstancesDao(db)


}
