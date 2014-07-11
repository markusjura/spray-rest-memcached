package com.credera.jdbc


import scala.slick.driver.H2Driver.simple._

class Profiles(tag: Tag) extends Table[(Int, String, String, String)](tag, "PROFILES") {
  def id = column[Int]("User_ID", O.PrimaryKey)
  def firstName = column[String]("First_Name__ID")
  def lastName = column[String]("Last_Name_ID")
  def email = column[String]("Email_ID")

  def * = (id, firstName, lastName, email)
}