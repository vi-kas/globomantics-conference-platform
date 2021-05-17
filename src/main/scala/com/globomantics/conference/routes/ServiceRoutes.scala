package com.globomantics.conference.routes

import akka.http.scaladsl.server.Route
import com.globomantics.conference.dao.{Dao, UserDaoComponent, UserDaoInMemory}
import com.globomantics.conference.model.Model
import com.globomantics.conference.services.{ServiceClient, UserServiceComponent}

object ServiceRoutes
  extends UserRoute
    with UserServiceComponent
    with UserDaoComponent {

  override val userDao: Dao[Model.User] = new UserDaoInMemory
  //  override val userDao: Dao[Model.User] = new UserDaoPostgres
  override implicit val userClient: ServiceClient[Model.User] = new UserServiceImpl

  val route: Route = user
}