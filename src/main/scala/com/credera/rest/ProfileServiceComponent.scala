package com.credera.rest

import com.credera.dao.ProfileDAOComponent
import com.credera.dto.Profile
import com.credera.dto.ProfileJsonProtocol._
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import spray.routing.directives.CachingDirectives._
import com.credera.cache.MemcachedCache
import com.credera.config.AppConfig
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
