package com.example.registry

import com.example.dao.ProfileDAOComponent
import com.example.rest.ProfileServiceComponent

object ComponentRegistry
  extends ProfileServiceComponent
  with ProfileDAOComponent {

  val profileDAO = new ProfileDAO()

}
