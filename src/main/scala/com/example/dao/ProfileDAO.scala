package com.example.dao

import com.example.dto.Profile
import com.example.exceptions.ProfileNotFoundException
import com.example.database.ProfilesDatabase._
import scala.slick.driver.H2Driver.simple._
import grizzled.slf4j.Logger

class ProfileDAO

object ProfileDAO {
  val logger = Logger(classOf[ProfileDAO])

  def getProfiles: List[Profile] = {
    logger.debug("Getting all Profiles from the database")
    Profiles.list
  }
    
  def getProfile(id: Int): Option[Profile] = {
    logger.debug(s"Getting Profile with id [$id] from the database")
    Profiles.filter(_.id === id).firstOption
  }
    
  def insertProfile(p: Profile) = {
    logger.debug("Inserting Profile into the database")
    Profiles += Profile(None, p.firstName, p.lastName, p.email)
  }
    
  def updateProfile(id: Int, p: Profile) = {  
    val profilesToUpdate = Profiles.filter(_.id === id)
    val qtyUpdated = profilesToUpdate map(r => (r.firstName, r.lastName, r.email)) update (p.firstName, p.lastName, p.email)
    if(qtyUpdated == 0) throw ProfileNotFoundException(id)
    logger.debug(s"Updated Profile with id [$id] in the database")
  }
    
  def deleteProfile(id: Int) = {
    val qtyDeleted = Profiles.filter(_.id === id).delete
    if(qtyDeleted == 0) throw ProfileNotFoundException(id)
   logger.debug(s"Deleted Profile with id [$id] from the database")
  }
    
}

