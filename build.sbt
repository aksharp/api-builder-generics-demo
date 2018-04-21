name := "api-builder-generics-demo"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.9",
  "com.typesafe.play" %% "play-json-joda" % "2.6.9",
  "io.flow" %% "lib-postgresql-play-play26" % "0.1.76",
  "org.typelevel" %% "cats-core" % "1.1.0"
)