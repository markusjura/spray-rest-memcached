package com.credera.rest

import com.credera.h2.DataSource
import com.mchange.v2.c3p0.ComboPooledDataSource

import scala.slick.driver.H2Driver.simple._

trait H2Connect {
  val cpds = new ComboPooledDataSource

//  val db = H2.db
}
