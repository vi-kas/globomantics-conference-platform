package com.globomantics.conference.dao

import com.globomantics.conference.model.Model.User

trait UserDaoComponent {

  val userDao: Dao[User]
}