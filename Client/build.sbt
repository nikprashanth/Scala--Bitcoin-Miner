name:="scala_client"

version :="0.0.1-SNAPSHOT"

scalaVersion :="2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.13",
  "com.typesafe.akka" %% "akka-remote" % "2.3.13"
)