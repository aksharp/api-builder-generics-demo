package lib.generated {

  sealed trait JobError

  case class JobDatabaseError(databaseName: String, databaseAction: String, errorMessage: String) extends JobError

  case class JobGenericError(errorMessage: String) extends JobError

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

  package object json {

    import play.api.libs.functional.syntax._
    import play.api.libs.json.JodaReads._
    import play.api.libs.json.Json._
    import play.api.libs.json.__
    import cats.implicits._
    import cats.{Functor}

    import scala.language.higherKinds

    implicit def aToJsValue[A](value: A)
      (implicit w: play.api.libs.json.Writes[A]): play.api.libs.json.JsValue = w.writes(value)

//    implicit def optionAToJsValue[A](value: Option[A])
//      (implicit w: play.api.libs.json.Writes[A]): Option[JsValue] = value.map(w.writes)

    implicit def ma[M[_] : Functor, A](value: M[A])
      (implicit w: play.api.libs.json.Writes[A]): M[play.api.libs.json.JsValue] = Functor[M].map(value)(w.writes)

    implicit def ma2[M[_] : Functor, G[_] : Functor, A](value: M[G[A]])
      (implicit w: play.api.libs.json.Writes[A]): M[G[play.api.libs.json.JsValue]] =
      Functor[M].map(value)(g => ma[G, A](g))

    implicit def jsonWritesExperimentEngineInternalJobInstance[J, I, O, E <: JobError](implicit
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

    def jsObjectJobInstance[J, I, O, E](obj: JobInstance[J, I, O, E])(
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

    def jsObjectJobInstanceForm[J, I, O, E](obj: JobInstanceForm[J, I, O, E])(
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

    implicit def jsonReadsExperimentEngineInternalJobInstance[J, I, O, E <: JobError](implicit
      readsI: play.api.libs.json.Reads[I],
      readsO: play.api.libs.json.Reads[O]
    ): play.api.libs.json.Reads[JobInstance[J, I, O, E]] = {
      (
        (__ \ "id").read[String] and
          (__ \ "name").read[String] and
          (__ \ "job_definition_id").read[String] and
          (__ \ "input").read[I] and
          (__ \ "output").readNullable[O] and
          (__ \ "error").readNullable[List[E]]
        ) (JobInstance[J, I, O, E] _)
    }

  }

}