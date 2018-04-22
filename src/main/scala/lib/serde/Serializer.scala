package lib.serde

import scala.language.higherKinds

trait Serializer[A, B] {
  def serialize(a: A): B
}
abstract class SerializerFunctions[M[A, B] <: Serializer[A, B]] {
  def serialize[A, B](a: A)(implicit ev: M[A, B]): B = ev.serialize(a)
}
object Serializer extends SerializerFunctions[Serializer] {
  @inline final def apply[A, B](implicit ev: Serializer[A,B]): Serializer[A, B] = ev
}
