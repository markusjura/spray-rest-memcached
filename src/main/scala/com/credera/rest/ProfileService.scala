package com.credera.rest

import akka.actor.Actor
import com.credera.dto.{ProfileJsonProtocol, ProfileDTO}
import spray.httpx.SprayJsonSupport
import spray.routing.HttpService
import spray.json._
import ProfileJsonProtocol._


class ProfileActor extends Actor with ProfileService{

  def actorRefFactory = context

  def receive = runRoute(profileRoute)

}

trait ProfileService extends HttpService with SprayJsonSupport  {

  val profileRoute = {
    path("profile") {
      get {
        complete {
          ProfileDTO("Sam", "Bunting", "sam@g.com")
        }
      }

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
