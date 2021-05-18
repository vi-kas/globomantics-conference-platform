package com.globomantics.conference.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.globomantics.conference.model.Model._
import com.globomantics.conference.services._

import scala.concurrent.Future

trait UserRoute {

  private val USER_API = "user"

  def user(implicit userClient: ServiceClient[User]): Route =
    pathPrefix(USER_API){

      pathEndOrSingleSlash {

        /** POST request to CREATE User */
        (post & entity(as[User])) { user =>

          val createUserResult: Future[ServiceResponse[User]] = create(user)
          respondWith(createUserResult, StatusCodes.Created)
        }
      } ~
        path(JavaUUID){id =>
          get {
            val readResult = read(id)
            respondWith(readResult)
          }
        }
    }

}