package com.example.jdbc

import com.example.config.AppConfig
import com.example.dto.Profile
import com.mchange.v2.c3p0.ComboPooledDataSource

import scala.slick.driver.H2Driver.simple._

object DataSource {

  val Profiles = TableQuery[Profiles]
  
  val cpds = new ComboPooledDataSource

  val jdbc:Database = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass(AppConfig.JDBC.host)
    ds.setJdbcUrl(AppConfig.JDBC.driver)
    Database.forDataSource(ds)
  }
  
  implicit val session = jdbc.createSession();

  def apply() = {
    Profiles.ddl.create
    Profiles += (Profile(Some(1), "Jon", "Doe", "jj@gmail.com"))
    Profiles += (Profile(Some(2), "Jane", "Doe", "jd@gmail.com"))    
  }

}
