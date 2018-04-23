package lib.serde

trait Serializer[A, B] {
  def serialize(a: A): B
}