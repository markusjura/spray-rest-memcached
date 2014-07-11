package com.credera.jdbc

import com.credera.config.AppConfig
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

  def apply() = {
    jdbc withSession {
      implicit session =>
        Profiles.ddl.create

        Profiles += (1, "Jon", "Doe", "jj@gmail.com")
        Profiles += (2, "Jane", "Doe", "jd@gmail.com")
    }
  }

}
