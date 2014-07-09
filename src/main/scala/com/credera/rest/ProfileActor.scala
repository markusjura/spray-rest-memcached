package com.credera.rest

import akka.actor.Actor
import com.credera.registry.ComponentRegistry.ProfileService

class ProfileActor extends Actor with ProfileService{
  def actorRefFactory = context

  def receive = runRoute(profileRoute)
}
