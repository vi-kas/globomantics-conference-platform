package com.globomantics.conference.services

import com.globomantics.conference.model.Model.User

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceClient extends ServiceClient[User] {

  override def create(entity: User): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Creating a User with ID: ${entity.id}")

      Right(entity)
    }

  override def read(id: UUID): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Reading a User with ID: $id")

      Left(ErrorResponse(s"No user with id: $id", 0))
    }

  override def update(id: UUID, entity: User): Future[ServiceResponse[User]] =
    Future {
      println(s"ServiceClient: Updating User with ID: $id")

      Left(ErrorResponse(s"No user with id: $id", 0))
    }

  override def delete(id: UUID): Future[ServiceResponse[Boolean]] =
    Future {
      println(s"ServiceClient: Deleting User with ID: $id")

      Left(ErrorResponse(s"No user with id: $id", 0))
    }
}