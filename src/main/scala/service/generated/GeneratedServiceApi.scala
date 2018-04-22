package service.generated {

  import lib.generated.models.JobError
  import lib.serde.{Deserializer, Serializer}
  import play.api.libs.json._
  import service.generated.models.Job.MyDailyEtlJob

  import scala.language.higherKinds


  package models {
    sealed trait JobInstanceInput extends _root_.scala.Product with _root_.scala.Serializable

    /**
      * Defines the valid discriminator values for the type JobInstanceInput
      */
    sealed trait JobInstanceInputDiscriminator extends _root_.scala.Product with _root_.scala.Serializable

    object JobInstanceInputDiscriminator {

      case object Day extends JobInstanceInputDiscriminator { override def toString = "day" }

      final case class UNDEFINED(override val toString: String) extends JobInstanceInputDiscriminator

      val all: scala.List[JobInstanceInputDiscriminator] = scala.List(Day)

      private[this] val byName: Map[String, JobInstanceInputDiscriminator] = all.map(x => x.toString.toLowerCase -> x).toMap

      def apply(value: String): JobInstanceInputDiscriminator = fromString(value).getOrElse(UNDEFINED(value))

      def fromString(value: String): _root_.scala.Option[JobInstanceInputDiscriminator] = byName.get(value.toLowerCase)

    }

    sealed trait JobInstanceOutput extends _root_.scala.Product with _root_.scala.Serializable

    /**
      * Defines the valid discriminator values for the type JobInstanceOutput
      */
    sealed trait JobInstanceOutputDiscriminator extends _root_.scala.Product with _root_.scala.Serializable

    object JobInstanceOutputDiscriminator {

      case object TotalDailyRevenueByOrganization extends JobInstanceOutputDiscriminator { override def toString = "total_daily_revenue_by_organization" }

      final case class UNDEFINED(override val toString: String) extends JobInstanceOutputDiscriminator

      val all: scala.List[JobInstanceOutputDiscriminator] = scala.List(TotalDailyRevenueByOrganization)

      private[this] val byName: Map[String, JobInstanceOutputDiscriminator] = all.map(x => x.toString.toLowerCase -> x).toMap

      def apply(value: String): JobInstanceOutputDiscriminator = fromString(value).getOrElse(UNDEFINED(value))

      def fromString(value: String): _root_.scala.Option[JobInstanceOutputDiscriminator] = byName.get(value.toLowerCase)

    }

    final case class Day(
      day: _root_.org.joda.time.DateTime
    ) extends JobInstanceInput

    final case class TotalDailyRevenueByOrganization(
      organizationId: String,
      totalRevenue: Double
    ) extends JobInstanceOutput

    /**
      * Provides future compatibility in clients - in the future, when a type is added
      * to the union JobInstanceInput, it will need to be handled in the client code.
      * This implementation will deserialize these future types as an instance of this
      * class.
      *
      * @param description Information about the type that we received that is undefined in this version of
      *        the client.
      */
    final case class JobInstanceInputUndefinedType(
      description: String
    ) extends JobInstanceInput

    /**
      * Provides future compatibility in clients - in the future, when a type is added
      * to the union JobInstanceOutput, it will need to be handled in the client code.
      * This implementation will deserialize these future types as an instance of this
      * class.
      *
      * @param description Information about the type that we received that is undefined in this version of
      *        the client.
      */
    final case class JobInstanceOutputUndefinedType(
      description: String
    ) extends JobInstanceOutput

    sealed trait Job extends _root_.scala.Product with _root_.scala.Serializable

    object Job {

      case object MyDailyEtlJob extends Job { override def toString = "my_daily_etl_job" }
      case object MyWeeklyEtlJob extends Job { override def toString = "my_weekly_etl_job" }
      case object MyLongRunningJob extends Job { override def toString = "my_long_running_job" }

      /**
        * UNDEFINED captures values that are sent either in error or
        * that were added by the server after this library was
        * generated. We want to make it easy and obvious for users of
        * this library to handle this case gracefully.
        *
        * We use all CAPS for the variable name to avoid collisions
        * with the camel cased values above.
        */
      final case class UNDEFINED(override val toString: String) extends Job

      /**
        * all returns a list of all the valid, known values. We use
        * lower case to avoid collisions with the camel cased values
        * above.
        */
      val all: scala.List[Job] = scala.List(MyDailyEtlJob, MyWeeklyEtlJob, MyLongRunningJob)

      private[this]
      val byName: Map[String, Job] = all.map(x => x.toString.toLowerCase -> x).toMap

      def apply(value: String): Job = fromString(value).getOrElse(UNDEFINED(value))

      def fromString(value: String): _root_.scala.Option[Job] = byName.get(value.toLowerCase)

    }

  }

  package object json {
    import lib.generated.json._
    import models._
    import play.api.libs.functional.syntax._
    import play.api.libs.json.{JsString, Writes, __}

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

    implicit val jsonReadsJobInternalJob = new play.api.libs.json.Reads[Job] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[Job] = {
        js match {
          case v: play.api.libs.json.JsString => play.api.libs.json.JsSuccess(Job(v.value))
          case _ => {
            (js \ "value").validate[String] match {
              case play.api.libs.json.JsSuccess(v, _) => play.api.libs.json.JsSuccess(Job(v))
              case err: play.api.libs.json.JsError => err
            }
          }
        }
      }
    }

    def jsonWritesJobInternalJob(obj: Job) = {
      play.api.libs.json.JsString(obj.toString)
    }

    def jsObjectJob(obj: Job) = {
      play.api.libs.json.Json.obj("value" -> play.api.libs.json.JsString(obj.toString))
    }

    implicit def jsonWritesJobInternalJob: play.api.libs.json.Writes[Job] = {
      new play.api.libs.json.Writes[Job] {
        def writes(obj: Job) = {
          jsonWritesJobInternalJob(obj)
        }
      }
    }

    implicit def jsonReadsJobInternalDay: play.api.libs.json.Reads[Day] = {
      (__ \ "day").read[_root_.org.joda.time.DateTime].map { x => new Day(day = x) }
    }

    def jsObjectDay(obj: Day): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "day" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.day))
      )
    }

    implicit def jsonReadsJobInternalTotalDailyRevenueByOrganization: play.api.libs.json.Reads[TotalDailyRevenueByOrganization] = {
      (
        (__ \ "organization_id").read[String] and
          (__ \ "total_revenue").read[Double]
        )(TotalDailyRevenueByOrganization.apply _)
    }

    def jsObjectTotalDailyRevenueByOrganization(obj: TotalDailyRevenueByOrganization): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "organization_id" -> play.api.libs.json.JsString(obj.organizationId),
        "total_revenue" -> play.api.libs.json.JsNumber(obj.totalRevenue)
      )
    }

    implicit def jsonReadsJobInternalJobInstanceInput: play.api.libs.json.Reads[JobInstanceInput] = new play.api.libs.json.Reads[JobInstanceInput] {
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

    implicit def jsonWritesJobInternalJobInstanceInput: play.api.libs.json.Writes[JobInstanceInput] = {
      new play.api.libs.json.Writes[JobInstanceInput] {
        def writes(obj: JobInstanceInput) = {
          jsObjectJobInstanceInput(obj)
        }
      }
    }

    implicit def jsonReadsJobInternalJobInstanceOutput: play.api.libs.json.Reads[JobInstanceOutput] = new play.api.libs.json.Reads[JobInstanceOutput] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[JobInstanceOutput] = {
        (js \ "discriminator").asOpt[String].getOrElse { sys.error("Union[JobInstanceOutput] requires a discriminator named 'discriminator' - this field was not found in the Json Value") } match {
          case "total_daily_revenue_by_organization" => js.validate[TotalDailyRevenueByOrganization]
          case other => play.api.libs.json.JsSuccess(JobInstanceOutputUndefinedType(other))
        }
      }
    }

    def jsObjectJobInstanceOutput(obj: JobInstanceOutput): play.api.libs.json.JsObject = {
      obj match {
        case x: TotalDailyRevenueByOrganization => jsObjectTotalDailyRevenueByOrganization(x) ++ play.api.libs.json.Json.obj("discriminator" -> "total_daily_revenue_by_organization")
        case other => {
          sys.error(s"The type[${other.getClass.getName}] has no JSON writer")
        }
      }
    }

    implicit def jsonWritesJobInternalJobInstanceOutput: play.api.libs.json.Writes[JobInstanceOutput] = {
      new play.api.libs.json.Writes[JobInstanceOutput] {
        def writes(obj: JobInstanceOutput) = {
          jsObjectJobInstanceOutput(obj)
        }
      }
    }

    //TODO: ---------------------------------------------------
    //TODO: -----NEW SERIALIZATION / DESERIALIZATION-----------
    //TODO: ---------------------------------------------------


    // TODO: Add Serialization / Desearilzation for other enums / union types instances
    implicit val myDailyEtlJobReads = new Reads[MyDailyEtlJob.type] {
      override def reads(json: JsValue): JsResult[MyDailyEtlJob.type] = json match {
        case JsString(str) if str == "my_daily_etl_job" => play.api.libs.json.JsSuccess(MyDailyEtlJob)
        case other => play.api.libs.json.JsError(s"Undefined $other. Expected MyDailyEtlJob")
      }
    }

    import cats.implicits._
    import lib.serde.PlayJsonHelpers._

    // -------------------
    // Serialize Generics
    // -------------------

    // Option[Day]
    implicit val daySerializerM: Serializer[Option[Day], Option[JsValue]] = (a: Option[Day]) => serializeM(a)

    // MyDailyEtlJob
    implicit val myDailyEtlJobSerializer: Serializer[MyDailyEtlJob.type, JsValue] = (a: MyDailyEtlJob.type) => serialize(a)

    // Option[TotalDailyRevenueByOrganization]
    implicit val totalDailyRevenueByOrganizationSerializerM: Serializer[Option[TotalDailyRevenueByOrganization], Option[JsValue]] =
      (a: Option[TotalDailyRevenueByOrganization]) => serializeM(a)

    // Option[List[JobError]]
    implicit val jobErrorsSerializerK: Serializer[Option[List[JobError]], Option[List[JsValue]]] = (a: Option[List[JobError]]) => serializeK(a)


    // -------------------
    // Deserialize Generics
    // -------------------

    // Option[Day]
    implicit val dayDeserializerM: Deserializer[Option[Day], Option[JsObject]] = (b: Option[JsObject]) => deserializeM[Option, Day](b)

    // MyDailyEtlJob
    implicit val myDailyEtlJobDeserializer: Deserializer[MyDailyEtlJob.type, JsObject] = (b: JsObject) => deserialize[MyDailyEtlJob.type](b)

    // Option[TotalDailyRevenueByOrganization]
    implicit val totalDailyRevenueByOrganizationDeserializerM: Deserializer[Option[TotalDailyRevenueByOrganization], Option[JsObject]] =
      (b: Option[JsObject]) => deserializeM[Option, TotalDailyRevenueByOrganization](b)

    // Option[List[JobError]]
    implicit val jobErrorsDeserializerK: Deserializer[Option[List[JobError]], Option[List[JsObject]]] =
      (b: Option[List[JsObject]]) => deserializeK[Option, List, JobError](b)

  }

}
