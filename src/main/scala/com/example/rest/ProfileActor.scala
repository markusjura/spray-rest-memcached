package com.example.rest

import akka.actor.Actor
import com.example.registry.ComponentRegistry.ProfileService

class ProfileActor extends Actor with ProfileService{
  def actorRefFactory = context

  def receive = runRoute(profileRoute)
}
