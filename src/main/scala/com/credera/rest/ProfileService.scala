package com.credera.rest

import akka.actor.Actor
import com.credera.dao.ProfileDAO
import com.credera.dto.{ProfileJsonProtocol, ProfileDTO}
import com.credera.h2.{Profile, DataSource}
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import ProfileJsonProtocol._


class ProfileActor extends Actor with ProfileService{

  def actorRefFactory = context

  def receive = runRoute(profileRoute)

}

trait ProfileService extends HttpService with SprayJsonSupport with H2Connect  {

  val dao = new ProfileDAO()

  val profileRoute = {
    path("profile") {
      get {
        complete {
          dao.fetchProfiles
        }
      } ~
      post {
        entity(as[ProfileDTO]){
          profile =>
            detach() {
              complete {
                ProfileDTO(profile.firstName, profile.lastName, "d@gmail.com")
              }
            }
          }
        }
    }
  }

}
