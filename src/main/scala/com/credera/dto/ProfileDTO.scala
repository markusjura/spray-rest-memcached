package com.credera.dto

import spray.json._

case class ProfileDTO (id: Option[Int], firstName:String, lastName:String, email:String)

object ProfileJsonProtocol extends DefaultJsonProtocol {
  implicit def profileFormat = jsonFormat4(ProfileDTO)
}
