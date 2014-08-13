package com.example.rest

import com.example.cache.MemcachedCache
import com.example.config.AppConfig._
import com.example.dao.ProfileDAOComponent
import com.example.dto.Profile
import com.example.dto.ProfileJsonProtocol._

import scala.collection.JavaConversions._
import spray.http.{StatusCodes, Uri}
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import spray.routing.directives.CachingDirectives._

trait ProfileServiceComponent { this: ProfileDAOComponent =>

  trait ProfileService extends HttpService with SprayJsonSupport with RouteExceptionHandlers  {

    val theCache = MemcachedCache.routeCache(Memcached.hosts, Memcached.timeToLive , Memcached.enabled)

    val profileRoute = {
      alwaysCache(theCache) {
          (get & path("profiles")) { //Get all Profiles
            complete {
              profileDAO.getProfiles match { 
                case head :: tail => head :: tail
                case Nil => StatusCodes.NoContent
              }
            }
          } ~
          (post & path("profiles")) { // Create a single Profile
            entity(as[Profile]) { profile => ctx =>
              profileDAO.insertProfile(profile)
              evictCache(ctx.request.uri, false)              
              ctx.complete(StatusCodes.Created, profile)
            }
          } ~
          (get & path("profiles" / IntNumber)) { id => //Get a single Profile
            val profile = profileDAO.getProfile(id)
            complete(profile)
          } ~
          (delete & path("profiles" / IntNumber)) { id => ctx => //Delete a single Profile            
            profileDAO.deleteProfile(id)
            evictCache(ctx.request.uri, true)
            ctx.complete(StatusCodes.NoContent)
          } ~
	      (put & path("profiles" / IntNumber)) { id => //Update a single Profile
	        entity(as[Profile]) { profile => ctx =>
	          profileDAO.updateProfile(id, profile)
	          evictCache(ctx.request.uri, true)
	          ctx.complete(profile)
	        }
	      }
      }
    }
    
    private def evictCache(uri: Uri, evictParentPath: Boolean) = {
      theCache.remove(uri)
      if(evictParentPath) {
        val uriString = uri.toString
        theCache.remove(uriString.substring(0, uriString.lastIndexOf("/")))        
      }
    }
    
  }

}
