package service.generated {

  import org.joda.time.DateTime

  sealed trait Job

  case object MyDailyEtlJob extends Job {
    override def toString = "my_daily_etl_job"
  }

  case object MyWeeklyEtlJob extends Job {
    override def toString = "my_weekly_etl_job"
  }

  case object MyLongRunningJob extends Job {
    override def toString = "my_long_running_job"
  }

  sealed trait JobInstanceInput

  case class Day(day: DateTime) extends JobInstanceInput

  case class SomeValue(value: String) extends JobInstanceInput

  sealed trait JobInstanceOutput

  case class TotalDailyRevenueByOrganization(organizationId: String, totalRevenue: Double)

  package json {

    import lib.Serde
    import play.api.libs.json.{JsObject, JsValue, Json}

    package object json {

      implicit def jobSerde[Day]: Serde[Day, JsValue] = new Serde[Day, JsValue] {
        override def serialize: Day => JsValue = a => Json.toJson[Day](a)
        override def deserialize: JsValue => Option[Day] = value => Json.fromJson[Day](value).asOpt
      }
    }

  }

}
