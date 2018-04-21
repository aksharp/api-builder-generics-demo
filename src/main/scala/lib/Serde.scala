package lib

import scala.language.higherKinds

trait Serde[A, B] {
  def serialize: A => B
  def deserialize: B => Option[A]
}
abstract class SerdeFunctions[M[A, B] <: Serde[A, B]] {
  def serialize[A, B](a: A)(implicit ev: M[A, B]): B = ev.serialize(a)
  def deserialize[A, B](b: B)(implicit ev: M[A, B]): Option[A] = ev.deserialize(b)
}
object Serde extends SerdeFunctions[Serde] {
  @inline final def apply[A, B](implicit ev: Serde[A,B]): Serde[A, B] = ev
}