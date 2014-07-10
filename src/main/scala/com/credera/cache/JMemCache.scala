package com.credera.cache

import java.net.InetSocketAddress

import com.thimbleware.jmemcached.storage.CacheStorage
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap
import com.thimbleware.jmemcached.{CacheImpl, Key, LocalCacheElement, MemCacheDaemon}

object JMemCache {

  var daemon:MemCacheDaemon[LocalCacheElement] = new MemCacheDaemon[LocalCacheElement]()

  def apply(maxItems:Int, maxBytes:Int, binary:Boolean, addr:InetSocketAddress, idle:Int, verbose:Boolean) = {
    val storage:CacheStorage[Key, LocalCacheElement] = ConcurrentLinkedHashMap.create(ConcurrentLinkedHashMap.EvictionPolicy.FIFO, maxItems, maxBytes)
    daemon.setCache(new CacheImpl(storage))
    daemon.setBinary(binary)
    daemon.setAddr(addr)
    daemon.setIdleTime(idle)
    daemon.setVerbose(verbose)
    daemon.start()
  }

}
