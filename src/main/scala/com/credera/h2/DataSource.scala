package com.credera.h2

import com.mchange.v2.c3p0.ComboPooledDataSource

import scala.slick.driver.H2Driver.simple._



object DataSource {

  val profiles = TableQuery[Profile]

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
        profiles.ddl.create

        profiles += (1, "Sam", "Bunting", "s@gmail.com")
        profiles += (2, "Andrew", "DeMaria", "j@gmail.com")
    }
  }

}
