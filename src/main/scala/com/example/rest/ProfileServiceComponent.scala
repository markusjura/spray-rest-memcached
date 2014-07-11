package com.example.rest

import com.example.dao.ProfileDAOComponent
import com.example.dto.Profile
import com.example.dto.ProfileJsonProtocol._
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import spray.routing.directives.CachingDirectives._
import com.example.cache.MemcachedCache
import com.example.config.AppConfig
import scala.collection.JavaConversions._

trait ProfileServiceComponent { this: ProfileDAOComponent =>

  trait ProfileService extends HttpService with SprayJsonSupport  {

    val theCache = MemcachedCache.routeCache(AppConfig.Memcached.hosts,30,true)

    val profileRoute = {
      cache(theCache) {
        path("profile") {
          get {
            complete {
              val profiles = profileDAO.fetchProfiles.getOrElse(List.empty[Profile])
              profiles
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
        } ~
        get {
          complete {
            "Hi there!  Try /profile for the user rest api"
          }
        }
      }
    }
  }

}
