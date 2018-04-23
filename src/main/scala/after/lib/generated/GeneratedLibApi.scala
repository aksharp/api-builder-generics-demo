package after.lib.generated {

  import play.api.libs.json.{JsObject, JsValue}

  package models {
    sealed trait JobError extends _root_.scala.Product with _root_.scala.Serializable

    /**
      * Defines the valid discriminator values for the type JobError
      */
    sealed trait JobErrorDiscriminator extends _root_.scala.Product with _root_.scala.Serializable

    object JobErrorDiscriminator {

      case object JobDatabaseError extends JobErrorDiscriminator { override def toString = "job_database_error" }
      case object JobGenericError extends JobErrorDiscriminator { override def toString = "job_generic_error" }

      final case class UNDEFINED(override val toString: String) extends JobErrorDiscriminator

      val all: scala.List[JobErrorDiscriminator] = scala.List(JobDatabaseError, JobGenericError)

      private[this] val byName: Map[String, JobErrorDiscriminator] = all.map(x => x.toString.toLowerCase -> x).toMap

      def apply(value: String): JobErrorDiscriminator = fromString(value).getOrElse(UNDEFINED(value))

      def fromString(value: String): _root_.scala.Option[JobErrorDiscriminator] = byName.get(value.toLowerCase)

    }

    final case class JobDatabaseError(
      databaseName: String,
      databaseAction: String,
      errorMessage: String
    ) extends JobError

    final case class JobGenericError(
      errorMessage: String
    ) extends JobError

    /**
      * Provides future compatibility in clients - in the future, when a type is added
      * to the union JobError, it will need to be handled in the client code. This
      * implementation will deserialize these future types as an instance of this class.
      *
      * @param description Information about the type that we received that is undefined in this version of
      *        the client.
      */
    final case class JobErrorUndefinedType(
      description: String
    ) extends JobError

    case class JobInstance[J, I, O, E <: JobError](
      id: String,
      key: String,
      job: J,
      input: Option[I] = None,
      output: Option[O] = None,
      errors: Option[List[E]] = None
    )

    case class JobInstanceForm[J, I, O, E <: JobError](
      key: String,
      job: J,
      input: Option[I] = None,
      output: Option[O] = None,
      errors: Option[List[E]] = None
    )
  }

  package object json {
    import cats.implicits._
    import after.lib.util.PlayJsonHelpers._
    import after.lib.util.Serializer
    import after.lib.util.Deserializer
    import models._
    import play.api.libs.functional.syntax._
    import play.api.libs.json.Json._
    import play.api.libs.json.{JsString, Writes, __}

    import scala.language.higherKinds

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

    implicit def jsonReadsJobInternalJobDatabaseError: play.api.libs.json.Reads[JobDatabaseError] = {
      (
        (__ \ "database_name").read[String] and
          (__ \ "database_action").read[String] and
          (__ \ "error_message").read[String]
        )(JobDatabaseError.apply _)
    }

    def jsObjectJobDatabaseError(obj: JobDatabaseError): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "database_name" -> play.api.libs.json.JsString(obj.databaseName),
        "database_action" -> play.api.libs.json.JsString(obj.databaseAction),
        "error_message" -> play.api.libs.json.JsString(obj.errorMessage)
      )
    }

    implicit def jsonReadsJobInternalJobGenericError: play.api.libs.json.Reads[JobGenericError] = {
      (__ \ "error_message").read[String].map { x => new JobGenericError(errorMessage = x) }
    }

    def jsObjectJobGenericError(obj: JobGenericError): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "error_message" -> play.api.libs.json.JsString(obj.errorMessage)
      )
    }

    implicit def jsonReadsJobInternalJobError: play.api.libs.json.Reads[JobError] = new play.api.libs.json.Reads[JobError] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[JobError] = {
        (js \ "discriminator").asOpt[String].getOrElse { sys.error("Union[JobError] requires a discriminator named 'discriminator' - this field was not found in the Json Value") } match {
          case "job_database_error" => js.validate[JobDatabaseError]
          case "job_generic_error" => js.validate[JobGenericError]
          case other => play.api.libs.json.JsSuccess(JobErrorUndefinedType(other))
        }
      }
    }

    def jsObjectJobError(obj: JobError): play.api.libs.json.JsObject = {
      obj match {
        case x: JobDatabaseError => jsObjectJobDatabaseError(x) ++ play.api.libs.json.Json.obj("discriminator" -> "job_database_error")
        case x: JobGenericError => jsObjectJobGenericError(x) ++ play.api.libs.json.Json.obj("discriminator" -> "job_generic_error")
        case other => {
          sys.error(s"The type[${other.getClass.getName}] has no JSON writer")
        }
      }
    }

    implicit def jsonWritesJobInternalJobError: play.api.libs.json.Writes[JobError] = {
      new play.api.libs.json.Writes[JobError] {
        def writes(obj: JobError) = {
          jsObjectJobError(obj)
        }
      }
    }


    //TODO: ---------------------------------------------------
    //TODO: -----NEW SERIALIZATION / DESERIALIZATION-----------
    //TODO: ---------------------------------------------------

    // -------------------
    // Serialize Generics
    // -------------------

    implicit def jsonWritesJobInstance[J, I, O, E <: JobError](
      implicit
        writesJ: play.api.libs.json.Writes[J],
        writesI: play.api.libs.json.Writes[I],
        writesO: play.api.libs.json.Writes[O],
        writesE: play.api.libs.json.Writes[E]
    ): play.api.libs.json.Writes[JobInstance[J, I, O, E]] = {
      new play.api.libs.json.Writes[JobInstance[J, I, O, E]] {
        def writes(obj: JobInstance[J, I, O, E]) = {
          jsObjectJobInstance(obj)
        }
      }
    }

    def jsObjectJobInstance[J, I, O, E <: JobError](obj: JobInstance[J, I, O, E])(
      implicit
        writesJ: play.api.libs.json.Writes[J],
        writesI: play.api.libs.json.Writes[I],
        writesO: play.api.libs.json.Writes[O],
        writesE: play.api.libs.json.Writes[E]
    ): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "id" -> play.api.libs.json.JsString(obj.id)
      ) ++ play.api.libs.json.Json.obj(
        "job" -> obj.job
      ) ++ play.api.libs.json.Json.obj(
        "input" -> obj.input
      ) ++
        (obj.output match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("output" -> x)
        }) ++
        (obj.errors match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("errors" -> x)
        })
    }

    def jsObjectJobInstanceForm[J, I, O, E <: JobError](obj: JobInstanceForm[J, I, O, E])(
      implicit
        writesJ: play.api.libs.json.Writes[J],
        writesI: play.api.libs.json.Writes[I],
        writesO: play.api.libs.json.Writes[O],
        writesE: play.api.libs.json.Writes[E]
    ): play.api.libs.json.JsObject = {
      play.api.libs.json.Json.obj(
        "job" -> obj.job
      ) ++ play.api.libs.json.Json.obj(
        "input" -> obj.input
      ) ++
        (obj.output match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("output" -> x)
        }) ++
        (obj.errors match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("errors" -> x)
        })
    }

    implicit def jsonReadsJobInstance[J, I, O, E <: JobError](
      implicit
        readsJ: play.api.libs.json.Reads[J],
        readsI: play.api.libs.json.Reads[I],
        readsO: play.api.libs.json.Reads[O],
        readsE: play.api.libs.json.Reads[E]
    ): play.api.libs.json.Reads[JobInstance[J, I, O, E]] = {
      (
        (__ \ "id").read[String] and
          (__ \ "key").read[String] and
          (__ \ "job").read[J] and
          (__ \ "input").readNullable[I] and
          (__ \ "output").readNullable[O] and
          (__ \ "error").readNullable[List[E]]
        ) (JobInstance[J, I, O, E] _)
    }

    // -------------------
    // Serialize Generics
    //  (serialize concrete types that will be used in place of generics)
    // -------------------

    // Option[List[E <: JobError]] = Error type
    // Option[List[JobError]]
    implicit val jobErrorsSerializerK: Serializer[Option[List[JobError]], Option[List[JsValue]]] =
      (a: Option[List[JobError]]) => serializeK(a)

    // -------------------
    // Deserialize Generics
    //  (deserialize concrete types that will be used in place of generics)
    // -------------------

    // Option[List[E <: JobError]] = Error type
    // Option[List[JobError]]
    implicit val jobErrorsDeserializerK: Deserializer[Option[List[JobError]], Option[List[JsObject]]] =
      (b: Option[List[JsObject]]) => deserializeK[Option, List, JobError](b)

  }

}