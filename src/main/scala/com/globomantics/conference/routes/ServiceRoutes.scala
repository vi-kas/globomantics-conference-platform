package com.globomantics.conference.routes

import akka.http.scaladsl.server.Route

object ServiceRoutes
  extends UserRoute {

  import com.globomantics.conference.services.Implicits._

  val route: Route = user
}