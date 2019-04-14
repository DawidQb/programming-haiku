name := "programming-haiku"

version := "0.1.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-Ypartial-unification")

val http4sVersion = "0.19.0"


libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.11.1",
  "io.circe" %% "circe-parser" % "0.11.1",
  "com.beachape" %% "enumeratum" % "1.5.13",
  "com.beachape" %% "enumeratum-circe" % "1.5.21",
  "com.typesafe" % "config" % "1.3.2"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")


assembly / test := {}
assembly / assemblyJarName := "app.jar"
assembly / assemblyMergeStrategy := {
  case "application.conf" => MergeStrategy.first
  case x: Any => (assembly / assemblyMergeStrategy).value(x)
}
