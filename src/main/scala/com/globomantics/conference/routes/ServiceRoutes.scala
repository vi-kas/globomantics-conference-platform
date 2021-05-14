package com.globomantics.conference.routes

import akka.http.scaladsl.server.Route

object ServiceRoutes extends UserRoute {

  val route: Route = user
}