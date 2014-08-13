package com.example.jdbc

import com.example.dto.Profile

import scala.slick.driver.H2Driver.simple._

class Profiles(tag: Tag) extends Table[Profile](tag, "PROFILES") {
  def id = column[Option[Int]]("User_ID", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("First_Name__ID")
  def lastName = column[String]("Last_Name_ID")
  def email = column[String]("Email_ID")

  def * = (id, firstName, lastName, email).<>(Profile.tupled, Profile.unapply _)
}