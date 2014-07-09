package com.credera.rest

import com.credera.dao.ProfileDAOComponent
import com.credera.dto.ProfileDTO
import com.credera.dto.ProfileJsonProtocol._
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService

trait ProfileServiceComponent { this: ProfileDAOComponent =>

  trait ProfileService extends HttpService with SprayJsonSupport  {

    val profileRoute = {
      path("profile") {

        get {
          complete {
            val profiles = profileDAO.fetchProfiles.getOrElse(List.empty[ProfileDTO])
            profiles
          }
        } ~
          post {
            entity(as[ProfileDTO]){
              profile =>
                complete {
                  ProfileDTO(profile.id, profile.firstName, profile.lastName, "d@gmail.com")
                }
            }
          }

      }
    }
  }

}
