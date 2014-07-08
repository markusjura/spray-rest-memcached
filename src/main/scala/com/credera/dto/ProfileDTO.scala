package com.credera.dto

import spray.json._

case class ProfileDTO (firstName:String, lastName:String, email:String)

object ProfileJsonProtocol extends DefaultJsonProtocol {
  implicit def profileFormat = jsonFormat3(ProfileDTO)
}