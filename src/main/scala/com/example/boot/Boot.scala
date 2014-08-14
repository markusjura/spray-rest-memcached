package com.example.boot

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.example.cache.HazelcastProcess
import com.example.cache.MemcachedDaemon
import com.example.database.ProfilesDatabase
import com.example.rest.ProfileActor
import spray.can.Http

object Boot extends App {

  //HazelcastProcess
  MemcachedDaemon //Start up the jmemcached MemCacheDaemon
  ProfilesDatabase //Start and pre-populate the H2 database with a couple of Profiles

  implicit val system = ActorSystem("spray-can")
  val service = system.actorOf(Props[ProfileActor], "profile-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)

}
