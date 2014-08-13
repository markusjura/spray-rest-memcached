package com.example.rest

import com.example.registry.ComponentRegistry.ProfileService

import akka.actor.Actor

class ProfileActor extends Actor with ProfileService {
  
  def actorRefFactory = context

  def receive = runRoute(profileRoute)
}
