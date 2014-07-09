package com.credera.dao

import com.credera.dto.Profile
import com.credera.h2.DataSource._
import scala.slick.driver.H2Driver.simple._

trait ProfileDAOComponent {

  val profileDAO:ProfileDAO

  class ProfileDAO {

    def fetchProfiles:Option[List[Profile]] = {
      var result = List.empty[Profile]

      h2 withSession {
        implicit session =>
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
      implicit val session = h2.createSession()

      profileExists(profile.email) match {
        case None =>
          session.withTransaction {
            Profiles.insert(0, profile.firstName, profile.lastName, profile.email)
          }
        case _ =>
      }
      session.close()
    }
    
    def updateProfile(profile:Profile) = {
      implicit val session = h2.createSession()

      session.withTransaction {
        val q = for { p <- Profiles if p.email === profile.email } yield (p.firstName, p.lastName, p.email)
        q.update(profile.firstName, profile.lastName, profile.email)
      }
      session.close()
    }

    private def profileExists(email:String):Option[Profile] = {
      h2 withSession {
        implicit session =>

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

