package lib.util

trait Deserializer[A, B] {
  def deserialize(b: B): A
}