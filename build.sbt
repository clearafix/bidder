name := "bidder"

version := "1.0"

scalaVersion := "2.12.2"

lazy val commonSettings = Seq(
  test in assembly := {}
)


libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test
)
        