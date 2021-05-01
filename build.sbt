
lazy val AKKA_VERSION       = "2.6.14"
lazy val AKKA_HTTP_VERSION  = "10.2.4"
lazy val SCALATEST_VERSION  = "3.2.7"

lazy val root = (project in file("."))
  .settings(
        name                    := "scala-design-patterns",
        version                 := "0.0.1-SNAPSHOT",
        description             := "Globomantics Platform",
        scalaVersion            := "2.13.4",
        organization            := "com.globomantics",
        libraryDependencies     ++=
          Seq(
                /* Dev Dependencies */
                "com.typesafe.akka"     %% "akka-stream"          % AKKA_VERSION,
                "com.typesafe.akka"     %% "akka-http"            % AKKA_HTTP_VERSION,
                "com.typesafe.akka"     %% "akka-http-spray-json" % AKKA_HTTP_VERSION,

                /* Test Dependencies */
                "org.scalactic"         %% "scalactic"            % SCALATEST_VERSION,
                "org.scalatest"         %% "scalatest"            % SCALATEST_VERSION     % Test,
                "com.typesafe.akka"     %% "akka-stream-testkit"  % AKKA_VERSION          % Test,
                "com.typesafe.akka"     %% "akka-http-testkit"    % AKKA_HTTP_VERSION     % Test
          )
  )