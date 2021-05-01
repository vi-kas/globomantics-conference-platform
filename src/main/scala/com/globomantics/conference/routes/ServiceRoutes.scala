package com.globomantics.conference.routes

import akka.http.scaladsl.server.Route

object ServiceRoutes extends HelloRoute {

  val route: Route = hello
}