package com.globomantics.conference.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HelloRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  val helloRoute: Route = ServiceRoutes.hello

  "HelloRoute" should {

    "return a greeting from the application" in {

      Get("/hello") ~> helloRoute ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Hello from Application!"
      }

    }
  }
}
