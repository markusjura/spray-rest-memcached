package com.credera.dao

import com.credera.dto.Profile
import com.credera.jdbc.DataSource._
import scala.slick.driver.H2Driver.simple._
import grizzled.slf4j.Logger

trait ProfileDAOComponent {

  val profileDAO:ProfileDAO

  class ProfileDAO {
    val logger = Logger(classOf[ProfileDAO])

    def fetchProfiles:Option[List[Profile]] = {
      var result = List.empty[Profile]

      jdbc withSession {
        implicit session =>
          logger.debug("Fetching profiles from database")
          Profiles foreach {
            case (id, firstName, lastName, email) =>
              result = Profile(Some(id), firstName, lastName, email) :: result
          }
      }
      Some(result)
    }

    def fetchProfileByEmail(email:String):Option[Profile] = {
      profileExists(email)
    }

    def insertProfile(profile:Profile) = {
      implicit val session = jdbc.createSession()

      profileExists(profile.email) match {
        case None =>
          session.withTransaction {
            logger.debug("Inserting profile into database")
            Profiles.insert(0, profile.firstName, profile.lastName, profile.email)
          }
        case _ =>
      }
      session.close()
    }
    
    def updateProfile(profile:Profile) = {
      implicit val session = jdbc.createSession()

      session.withTransaction {
        logger.debug("Updating profile in database")
        val q = for { p <- Profiles if p.email === profile.email } yield (p.firstName, p.lastName, p.email)
        q.update(profile.firstName, profile.lastName, profile.email)
      }
      session.close()
    }

    private def profileExists(email:String):Option[Profile] = {
      jdbc withSession {
        implicit session =>
          logger.debug("Checking if profile exists in database")
          val q = Profiles.filter(_.email === email)
          val l = q.list()

          if(l.size == 0)
            None
          else {
            val p = l(0)
            Some(Profile(Some(p._1), p._2, p._3, p._4))
        }
      }

    }

  }


}

