package com.globomantics.conference.util

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.globomantics.conference.Backend._
import com.globomantics.conference.model.Model._
import spray.json.JsValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ZIPCodeLocator {

  lazy val apiKey: String = configuration.getString("maps.apiKey")

  /**
   * Given a `zipCode: String`, provides a Location instance.
   * - Calls google's geocode api for location information.
   */
  def locateZIPCode(zipCode: String): Future[Location] = {

    val locationFuture: Future[Location] = Http().singleRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = s"https://maps.googleapis.com/maps/api/geocode/json?address=$zipCode&key=$apiKey"
      )
    ).flatMap {
      case successResponse @ HttpResponse(StatusCodes.OK, headers, entity, _) => {

        val responseJson: Future[JsValue] = Unmarshal(successResponse).to[JsValue]

        responseJson
          .map {
            case json => {
              fetchLocation(zipCode, json) match {
                case Some(l) => l
                case None => Location(zipCode, Some(""), Some(""))
              }
            }
          }

      }
      case _ => Future.failed(new Exception(s"Could not fetch Location based on ZIPCode: $zipCode"))
    }

    locationFuture
  }


  import spray.json._
  import DefaultJsonProtocol._

  case class AddressComponent(name: String, componentType: List[String])
  case class Result(addressComponents: List[AddressComponent])

  implicit object AddressComponent extends RootJsonFormat[AddressComponent] {

    override def write(obj: AddressComponent): JsValue = {
      JsObject(
        "long_name" -> JsString(obj.name), "type" -> JsArray(obj.componentType.map(JsString(_)))
      )
    }

    def read(json: JsValue): AddressComponent = {
      val fields = json.asJsObject.fields

      AddressComponent(
        fields("long_name").convertTo[String],
        fields("types").convertTo[List[String]]
      )
    }

  }

  implicit object ResultFormat extends JsonReader[Result] {

    def read(json: JsValue): Result = {
      val fields = json.asJsObject.fields

      Result(
        fields("address_components").convertTo[List[AddressComponent]]
      )
    }
  }

  def fetchLocation(zipCode: String, json: JsValue): Option[Location] = {
    val fields = json.asJsObject.fields

    val maybeAddressComponents: Option[List[AddressComponent]] = fields("results")
      .convertTo[List[JsValue]]
      .headOption
      .map(_.convertTo[Result])
      .map(_.addressComponents)

    maybeAddressComponents
      .map {
        case addressComponents =>
          val (city, country) =
            (addressComponents
              .filter(_.componentType.contains("locality")).headOption.map(_.name),
              addressComponents
                .filter(_.componentType.contains("country")).headOption.map(_.name))

          Location(zipCode, city, country)
      }
  }

}