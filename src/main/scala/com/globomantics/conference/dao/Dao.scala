package com.globomantics.conference.dao

import com.globomantics.conference.model.Model.Entity

import java.util.UUID
import scala.concurrent.Future

trait Dao[T <: Entity] {

  def insert(entity: T): Future[T]

  def byId(id: UUID): Future[Option[T]]

  def all: Future[Seq[T]]

  def update(id: UUID, t: T): Future[T]

  def remove(id: UUID): Future[Boolean]

}