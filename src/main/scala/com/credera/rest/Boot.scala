package com.credera.rest

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http

/**
 * Created by sbunting on 7/8/14.
 */
object Boot extends App{

  implicit val system = ActorSystem("on-spray-can")

  val service = system.actorOf(Props[ProfileActor], "profile-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}
