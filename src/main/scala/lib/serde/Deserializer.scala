package lib.serde

import scala.language.higherKinds

trait Deserializer[A, B] {
  def deserialize(b: B): A
}
abstract class DeserializerFunctions[M[A, B] <: Deserializer[A, B]] {
  def deserialize[A, B](b: B)(implicit ev: M[A, B]): A = ev.deserialize(b)
}
object Deserializer extends DeserializerFunctions[Deserializer] {
  @inline final def apply[A, B](implicit ev: Deserializer[A,B]): Deserializer[A, B] = ev
}