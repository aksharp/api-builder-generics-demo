package after.lib.util

import cats.Functor
import play.api.libs.json._

import scala.language.higherKinds

object PlayJsonHelpers {

  // Serialize
  def serialize[A](value: A)(
    implicit w: Writes[A]): JsValue = w.writes(value)

  def serializeM[M[_] : Functor, A](a: M[A])(
    implicit w: Writes[A]): M[JsValue] =
    Functor[M].map(a)(w.writes)

  def serializeK[M[_] : Functor, G[_] : Functor, A](value: M[G[A]])(
    implicit w: Writes[A]): M[G[JsValue]] =
    Functor[M].map(value)(g => serializeM[G, A](g))

  // Deserialize
  def deserialize[A](value: JsObject)
    (implicit r: Reads[A]): A =
    read(value)

  def deserializeM[M[_] : Functor, A](value: M[JsObject])
    (implicit r: Reads[A]): M[A] =
    Functor[M].map(value)(read(_))

  def deserializeK[M[_] : Functor, G[_] : Functor, A](value: M[G[JsObject]])
    (implicit r: Reads[A]): M[G[A]] =
    Functor[M].map(value)(g => deserializeM[G, A](g))

  //TODO: ".get" is for simple demo only. It is not safe. Return Either[E, A] or Option[A], but do not use ".get" as it may fail
  private def read[A](a: JsObject)(implicit r: Reads[A]): A = r.reads(a).asOpt.get

}
