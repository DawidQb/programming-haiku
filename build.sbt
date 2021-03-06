name := "programming-haiku"

version := "0.1.2"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-Ypartial-unification")

val http4sVersion = "0.19.0"
val doobieVersion = "0.6.0"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.11.1",
  "io.circe" %% "circe-parser" % "0.11.1",
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
"com.beachape" %% "enumeratum" % "1.5.13",
  "com.beachape" %% "enumeratum-circe" % "1.5.21",
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.1.1" % "test,it",
  "io.gatling" % "gatling-test-framework" % "3.1.1" % "test,it"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

enablePlugins(GatlingPlugin)

assembly / test := {}
assembly / assemblyJarName := "app.jar"
assembly / assemblyMergeStrategy := {
  case "application.conf" => MergeStrategy.first
  case x: Any => (assembly / assemblyMergeStrategy).value(x)
}
