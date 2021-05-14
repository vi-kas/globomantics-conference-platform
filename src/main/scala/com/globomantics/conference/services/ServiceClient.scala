package com.globomantics.conference.services

import java.util.UUID
import scala.concurrent.Future

trait ServiceClient[T] {

  def create(entity: T): Future[ServiceResponse[T]]

  def read(id: UUID): Future[ServiceResponse[T]]

  def update(id: UUID, entity: T): Future[ServiceResponse[T]]

  def delete(id: UUID): Future[ServiceResponse[Boolean]]
}

object ServiceClient {

  implicit val userServiceClient = new UserServiceClient
}