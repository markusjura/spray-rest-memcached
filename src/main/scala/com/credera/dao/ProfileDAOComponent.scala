package com.credera.dao

import com.credera.dto.ProfileDTO
import com.credera.h2.DataSource
import com.credera.h2.DataSource._
import scala.slick.driver.H2Driver.simple._

trait ProfileDAOComponent {

  val profileDAO:ProfileDAO

  class ProfileDAO {

    def fetchProfiles:Option[List[ProfileDTO]] = {
      var result = List.empty[ProfileDTO]
      DataSource.h2 withSession {
        implicit session =>
          Profiles foreach {
            case (id, firstName, lastName, email) =>
              result = ProfileDTO(Some(id), firstName, lastName, email) :: result
          }
      }
      Some(result)
    }

    def addProfile(profile:ProfileDTO) = {
      implicit val session = DataSource.h2.createSession()
      session.withTransaction {
        ProfileDTO
      }
    }

  }


}

