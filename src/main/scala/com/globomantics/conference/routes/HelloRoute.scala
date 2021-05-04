package com.globomantics.conference.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

trait HelloRoute {

  private val HELLO_API = "hello"

  /**
   * GET - /hello should return a success response with string
   */
  val hello: Route =
    path(HELLO_API) {
      get {
        complete(StatusCodes.OK, "Hello from Application!")
      }
    }
}