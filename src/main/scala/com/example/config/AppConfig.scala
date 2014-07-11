package com.example.config

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object AppConfig {

  private val config = ConfigFactory.load()

  object Memcached {

    private val memCachedConfig = config.getConfig("memcached")

    lazy val hosts = memCachedConfig.getStringList("hosts").asScala.toList

  }

  object JDBC {
    private val jdbcConfig = config.getConfig("jdbc")

    lazy val host = jdbcConfig.getString("host")
    lazy val driver = jdbcConfig.getString("driver")
  }

}
