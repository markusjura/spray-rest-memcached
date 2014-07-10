package com.credera.config

import com.typesafe.config.ConfigFactory

object AppConfig {

  private val config = ConfigFactory.load()

  object Memcached {

    private val memCachedConfig = config.getConfig("memcached")

    lazy val host = memCachedConfig.getString("host")

  }

}
