package lib.util

trait Serializer[A, B] {
  def serialize(a: A): B
}