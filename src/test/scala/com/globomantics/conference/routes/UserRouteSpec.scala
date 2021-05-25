package com.globomantics.conference.routes

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{MessageEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.globomantics.conference.dao.{Dao, UserDaoComponent, UserDaoInMemory}
import com.globomantics.conference.model.Model._
import com.globomantics.conference.services.UserServiceClient
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID

class UserRouteSpec
  extends AnyWordSpec
    with Matchers
    with ScalatestRouteTest {

  implicit val userClient: UserServiceClient =
    new UserServiceClient
      with UserDaoComponent {
      override val userDao: Dao[User] = new UserDaoInMemory
    }

  val userRoute: Route = ServiceRoutes.user
  val USER_API: String = "/user"
  val UUID_STRING: String = "123c78ec-1eb8-4410-b284-f75079b15709"

  private val user =
    User(
      UUID.fromString(UUID_STRING), "Bob", "bob@email.com",
      "plain_pwd", Address(None, Location("303030", None, None)), UserRole.Speaker
    )

  val userEntity: MessageEntity = Marshal(user).to[MessageEntity].futureValue

  "UserRoute" should {

    "return a Status 201 from the application" in {
      val userPostRequest = Post(USER_API).withEntity(userEntity)

      userPostRequest ~> userRoute ~> check {
        status shouldEqual StatusCodes.Created
      }
    }

    "return a Status 200 from the application" in {
      Get(s"$USER_API/$UUID_STRING") ~> userRoute ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }
}
