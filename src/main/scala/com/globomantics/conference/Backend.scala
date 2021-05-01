package com.globomantics.conference

import akka.actor.ActorSystem
import akka.event._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.typesafe.config._

import scala.util.{Failure, Success}

class Backend private(config: Config, serviceRoutes: Route)
                     (implicit system: ActorSystem, matzr: Materializer) {

  implicit def executor = system.dispatcher
  private val logger    = Logging(system, getClass)

  private val PORT = config.getInt("http.port")
  private val HOST = config.getString("http.interface")

  Http()
    .newServerAt(HOST, PORT)
    .bind(serviceRoutes)
    .onComplete {
      case Success(bound) =>
        logger.info(s"Server Started: ${bound.localAddress.getHostString}")
      case Failure(e) =>
        logger.error(s"Server could not start: ${e.getMessage}")
        system.terminate()
    }
}

object Backend extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val matzr: Materializer = Materializer(system)

  final val configuration: Config  = ConfigFactory.load()
  final val HELLO_API: String      = "hello"

  val serviceRoutes =
    pathPrefix(HELLO_API) {
      get {
        complete {
          "Hello"
        }
      }
    }

  new Backend(configuration, serviceRoutes)
}