package com.example.cache

import com.example.config.AppConfig
import com.hazelcast.config.Config
import com.hazelcast.config.NetworkConfig
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.config.InterfacesConfig
import java.util.Map
import java.util.Queue


object HazelcastProcess {
  
  //We're only starting a single HazelcastInstance
  
  private val instance: HazelcastInstance = {
    
    val hostInfo: String = AppConfig.Memcached.hosts(0)
    val ip = hostInfo.split(":")(0)
    val port = hostInfo.split(":")(1).toInt

    val config: Config = new Config()
    val networkConfig: NetworkConfig = config.getNetworkConfig()
    val interfaceConfig = new InterfacesConfig
  
    interfaceConfig.addInterface(ip)
    interfaceConfig.setEnabled(true)
    networkConfig.setInterfaces(interfaceConfig)
    networkConfig.setPort(port)
    
    Hazelcast.newHazelcastInstance(config);  
    
  }

}