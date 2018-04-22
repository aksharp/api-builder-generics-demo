package lib.serde

trait Deserializer[A, B] {
  def deserialize(b: B): A
}