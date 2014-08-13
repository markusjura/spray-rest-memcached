package com.example.config

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object AppConfig {

  private val config = ConfigFactory.load()

  object Memcached {
    private val memcachedConfig = config.getConfig("memcached")
    lazy val hosts = memcachedConfig.getStringList("hosts").asScala.toList
    lazy val enabled = memcachedConfig.getBoolean("enabled")
    lazy val timeToLive = memcachedConfig.getInt("timeToLive") 

  }

  object JDBC {
    private val jdbcConfig = config.getConfig("jdbc")
    lazy val host = jdbcConfig.getString("host")
    lazy val driver = jdbcConfig.getString("driver")
  }

}
