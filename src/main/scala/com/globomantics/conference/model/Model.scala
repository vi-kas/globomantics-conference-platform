package com.globomantics.conference.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.globomantics.conference.services.{ApiResponse, ErrorResponse}
import spray.json._

import java.util.UUID

object Model
  extends SprayJsonSupport
    with DefaultJsonProtocol {

  /*  JsonFormatter for Models  */
  implicit val userRoleJsonFormat     = new EnumerationJSONFormat[UserRole.Value](UserRole)

  implicit val uuidJsonFormat         = new UUIDJsonFormat

  implicit val errorJsonFormat        = jsonFormat2(ErrorResponse)
  implicit val apiResponseJsonFormat  = jsonFormat4(ApiResponse)

  implicit val locationJsonFormat     = jsonFormat3(Location)
  implicit val addressJsonFormat      = jsonFormat2(Address)

  implicit val userJsonFormat         = jsonFormat6(User)

  /** Entities */
  trait Entity { def id: UUID }

  object UserRole extends Enumeration {
    type UserRole = Value

    val Admin     = Value("Admin")
    val Speaker   = Value("Speaker")
    val Attendee  = Value("Attendee")
    val Organizer = Value("Organizer")
  }

  import UserRole._

  case class User(id: UUID, name: String, email: String,
                  password: String, address: Address,
                  role: UserRole) extends Entity

  case class Address(addressLine: Option[String], location: Location)

  case class Location(pin: String, city: Option[String], country: Option[String])
}