package com.credera.dao

import com.credera.dto.ProfileDTO
import com.credera.h2.DataSource
import com.credera.h2.DataSource._
import scala.slick.driver.H2Driver.simple._

class ProfileDAO {

  def fetchProfiles:List[ProfileDTO] = {
    var result = List[ProfileDTO]()
    DataSource.h2 withSession {
      implicit session =>
        profiles foreach {
          case (id, firstName, lastName, email) =>
            result = ProfileDTO(firstName, lastName, email) :: result
        }
    }
    result
  }

}
