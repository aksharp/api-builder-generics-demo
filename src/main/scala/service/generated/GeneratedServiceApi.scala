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

  case class JobInstanceInputUndefinedType(description: String) extends JobInstanceInput

  sealed trait JobInstanceOutput

  case class TotalDailyRevenueByOrganization(organizationId: String, totalRevenue: Double)

  package json {

    import lib.Serde
    import play.api.libs.json.{JsObject, JsValue, Json}
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._

    package object json {

      private[this] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

      private[this] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
        def writes(x: java.util.UUID) = JsString(x.toString)
      }

      private[this] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
        import org.joda.time.format.ISODateTimeFormat.dateTimeParser
        dateTimeParser.parseDateTime(str)
      }

      private[this] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
        def writes(x: org.joda.time.DateTime) = {
          import org.joda.time.format.ISODateTimeFormat.dateTime
          val str = dateTime.print(x)
          JsString(str)
        }
      }

      private[this] implicit val jsonReadsJodaLocalDate = __.read[String].map { str =>
        import org.joda.time.format.ISODateTimeFormat.dateParser
        dateParser.parseLocalDate(str)
      }

      private[this] implicit val jsonWritesJodaLocalDate = new Writes[org.joda.time.LocalDate] {
        def writes(x: org.joda.time.LocalDate) = {
          import org.joda.time.format.ISODateTimeFormat.date
          val str = date.print(x)
          JsString(str)
        }
      }

      implicit def jsonReadsExperimentEngineJobsInternalDay: play.api.libs.json.Reads[Day] = {
        (__ \ "day").read[_root_.org.joda.time.DateTime].map { x => new Day(day = x) }
      }

      def jsObjectDay(obj: Day): play.api.libs.json.JsObject = {
        play.api.libs.json.Json.obj(
          "day" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.day))
        )
      }


      implicit def jsonReadsExperimentEngineJobsInternalJobInstanceInput: play.api.libs.json.Reads[JobInstanceInput] = new play.api.libs.json.Reads[JobInstanceInput] {
        def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[JobInstanceInput] = {
          (js \ "discriminator").asOpt[String].getOrElse { sys.error("Union[JobInstanceInput] requires a discriminator named 'discriminator' - this field was not found in the Json Value") } match {
            case "day" => js.validate[Day]
            case other => play.api.libs.json.JsSuccess(JobInstanceInputUndefinedType(other))
          }
        }
      }

      def jsObjectJobInstanceInput(obj: JobInstanceInput): play.api.libs.json.JsObject = {
        obj match {
          case x: Day => jsObjectDay(x) ++ play.api.libs.json.Json.obj("discriminator" -> "day")
          case other => {
            sys.error(s"The type[${other.getClass.getName}] has no JSON writer")
          }
        }
      }

      implicit def jsonWritesExperimentEngineJobsInternalJobInstanceInput: play.api.libs.json.Writes[JobInstanceInput] = {
        new play.api.libs.json.Writes[JobInstanceInput] {
          def writes(obj: JobInstanceInput) = {
            jsObjectJobInstanceInput(obj)
          }
        }
      }


      implicit val jobSerde: Serde[Day, JsValue] = new Serde[Day, JsValue] {
        override def serialize: Day => JsValue = a => Json.toJson[Day](a)
        override def deserialize: JsValue => Option[Day] = value => Json.fromJson[Day](value).asOpt
      }



    }

  }

}
