package com.credera.boot

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.credera.h2.DataSource
import com.credera.rest.ProfileActor
import spray.can.Http

object Boot extends App {

  DataSource()

  val ACTOR_SYSTEM = "on-spray-can"
  implicit val system = ActorSystem(Boot.ACTOR_SYSTEM)

  val service = system.actorOf(Props[ProfileActor], "profile-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}
