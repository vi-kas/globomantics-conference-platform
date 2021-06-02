package com.globomantics.conference

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import com.globomantics.conference.dao.{Dao, UserDaoComponent, UserDaoInMemory, UserDaoPostgres}
import com.globomantics.conference.model.Model
import com.globomantics.conference.model.Model.{Location, User}
import com.globomantics.conference.util.ZIPCodeLocator.fetchLocation
import spray.json.{JsString, JsValue, JsonWriter, _}

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

package object services {

  /**
   * Method to create an entity for CRUD Service
   */
  def create[T](entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.create(entity)

  /**
   * Method to read an entity for CRUD Service
   */
  def read[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.read(id)

  /**
   * Method to update an entity for CRUD Service
   */
  def update[T](id: UUID, entity: T)(implicit sc: ServiceClient[T]): Future[ServiceResponse[T]] =
    sc.update(id, entity)

  /**
   * Method to delete an entity for CRUD Service
   */
  def delete[T](id: UUID)(implicit sc: ServiceClient[T]): Future[ServiceResponse[Boolean]] =
    sc.delete(id)

  /**
   * Response type alias for typical Service Response
   */
  type ServiceResponse[T] = Either[ErrorResponse, T]

  case class ErrorResponse(message: String, code: Int) extends Exception(message)

  case class ApiResponse(success: Boolean,
                         code: Option[Int] = None,
                         message: Option[String] = None,
                         data: JsValue = JsString(""))

  def respondWith[A](response: Future[ServiceResponse[A]], statusCode: StatusCode = StatusCodes.OK)
                    (implicit ee: JsonWriter[ErrorResponse], ar: JsonWriter[ApiResponse], rr: JsonWriter[A]): StandardRoute =
    complete {
      response map {
        case Left(error) =>
          HttpResponse(
            status = toStatusCode(error.code),
            entity =
              HttpEntity(
                ContentTypes.`application/json`,
                ApiResponse(success = false, Some(error.code), Some(error.message)).toJson.toString))

        case Right(value) =>
          HttpResponse(
            status = statusCode,
            entity =
              HttpEntity(
                ContentTypes.`application/json`,
                ApiResponse(success = true, data = value.toJson).toJson.toString))
      }
    }

  private def toStatusCode(i: Int): StatusCode =
    Try(StatusCode.int2StatusCode(i))
      .getOrElse(StatusCodes.InternalServerError)

  object Implicits {
    implicit val userClient: UserServiceClient = {
      new UserServiceClient
        with UserDaoComponent {

        override val userDao: Dao[Model.User] = new UserDaoInMemory
      }
    }
  }

  import com.globomantics.conference.util.ZIPCodeLocator.locateZIPCode

  implicit class UserOps(user: User) {

    import monocle.macros.syntax.lens._

    def ensureAddressDetails: Future[User] = {
      val location = user.address.location

      (location.city, location.country) match {

        case (None, None) => {
          val zipCodeFuture: Future[Location] = locateZIPCode(location.pin)

          zipCodeFuture
            .map(fetchLocation => {
              // update user's location here
              user.lens(_.address.location).set(fetchLocation)
            })
        }

        case _ => Future.successful(user)
      }
    }



    def ensureAddressDetailsWithoutLens: Future[User] = {

      val location = user.address.location

      (location.city, location.country) match {
        case (None, None) =>
          locateZIPCode(location.pin)
            .map {
              fetchedLocation =>
                user
                  .copy(
                    address =
                      user.address.copy(
                        location =
                          location.copy(
                            city = fetchedLocation.city,
                            country = fetchedLocation.country
                          )
                      )
                  )
            }

        case _ => Future.successful(user)
      }

    }
  }
}