package com.credera.rest

import com.credera.dao.ProfileDAOComponent
import com.credera.dto.Profile
import com.credera.dto.ProfileJsonProtocol._
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import spray.caching._
import spray.routing.directives.CachingDirectives._

trait ProfileServiceComponent { this: ProfileDAOComponent =>

  trait ProfileService extends HttpService with SprayJsonSupport  {

    val profileRoute = {
      path("profile") {

        get {
          cache(routeCache()) {
            complete {
              val profiles = profileDAO.fetchProfiles.getOrElse(List.empty[Profile])
              profiles
            }
          }
        } ~
        post {
          entity(as[Profile]){
            profile =>
              detach() {
                complete {
                  profileDAO.insertProfile(profile)
                  profile
                }
              }
          }
        } ~
        put {
          entity(as[Profile]){
            profile =>
              detach() {
                complete {
                  profileDAO.updateProfile(profile)
                  profile
                }
              }
          }
        }

      }
    }
  }

}
