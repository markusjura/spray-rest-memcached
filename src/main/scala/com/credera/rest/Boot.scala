package com.credera.rest

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.credera.dto.ProfileDTO
import com.credera.h2.{Profile, DataSource}
import com.credera.h2.DataSource._
import spray.can.Http
import scala.slick.driver.H2Driver.simple._

object Boot extends App{

  DataSource()

  implicit val system = ActorSystem("on-spray-can")

  val service = system.actorOf(Props[ProfileActor], "profile-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}
