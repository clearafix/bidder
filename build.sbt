name := "bidder"

version := "1.0"

scalaVersion := "2.12.2"

lazy val commonSettings = Seq(
  test in assembly := {}
)


libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "org.json4s" %% "json4s-jackson" % "3.5.2",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)
        