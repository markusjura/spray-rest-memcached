package com.credera.registry

import com.credera.dao.ProfileDAOComponent
import com.credera.rest.ProfileServiceComponent

object ComponentRegistry
  extends ProfileServiceComponent
  with ProfileDAOComponent {

  val profileDAO = new ProfileDAO()

}
