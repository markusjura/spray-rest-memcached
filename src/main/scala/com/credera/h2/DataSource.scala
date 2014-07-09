package com.credera.h2

import com.mchange.v2.c3p0.ComboPooledDataSource

import scala.slick.driver.H2Driver.simple._

object DataSource {

  val Profiles = TableQuery[Profiles]

  val cpds = new ComboPooledDataSource

  val h2:Database = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass("org.h2.Driver")
    ds.setJdbcUrl("jdbc:h2:mem:test")
    Database.forDataSource(ds)
  }

  def apply() = {
    h2 withSession {
      implicit session =>
        Profiles.ddl.create

        Profiles += (1, "Sam", "Bunting", "s@gmail.com")
        Profiles += (2, "Andrew", "DeMaria", "j@gmail.com")
    }
  }

}
