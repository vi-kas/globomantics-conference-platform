
lazy val AKKA_VERSION       = "2.6.14"
lazy val AKKA_HTTP_VERSION  = "10.2.4"

lazy val akkaDependencies: Seq[ModuleID] =
  Seq(
    "com.typesafe.akka"     %% "akka-stream"          % AKKA_VERSION,
    "com.typesafe.akka"     %% "akka-http"            % AKKA_HTTP_VERSION,
    "com.typesafe.akka"     %% "akka-http-core"       % AKKA_HTTP_VERSION,
    "com.typesafe.akka"     %% "akka-http-spray-json" % AKKA_HTTP_VERSION
  )

lazy val root = (project in file("."))
  .settings(
    name                    := "scala-design-patterns",
    version                 := "0.0.1-SNAPSHOT",
    description             := "Globomantics Platform",
    scalaVersion            := "2.13.4",
    organization            := "com.globomantics",
    libraryDependencies     ++= akkaDependencies
  )