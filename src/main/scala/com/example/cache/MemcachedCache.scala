package com.example.cache

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Promise
import org.slf4j.LoggerFactory
import net.spy.memcached.AddrUtil
import net.spy.memcached.MemcachedClient
import spray.caching.Cache
import spray.routing.directives.CachingDirectives.RouteResponse
import spray.caching.ExpiringLruCache
import scala.concurrent.duration.Duration

object MemcachedCache {

  def apply[V](memcachedHosts: List[String], timeToLiveSeconds: Int): Cache[V] = routeCache(memcachedHosts,timeToLiveSeconds)

  def routeCache(memcachedHosts: List[String], timeToLiveSeconds: Int): Cache[RouteResponse] = {
      MemcachedCache(memcachedHosts, timeToLiveSeconds)
  }

}


final class MemcachedCache[V](memcachedHosts: List[String], timeToLiveSeconds: Int) extends Cache[V] {

  val log = LoggerFactory.getLogger(classOf[MemcachedCache[V]])

  import scala.collection.JavaConversions._
  val memcachedClient = new MemcachedClient(AddrUtil.getAddresses(memcachedHosts))

  def get(key: Any) = {
    memcachedClient.get(key.toString) match {
      case null => {
        log.debug("cache miss - key: {} ", key.toString)
        None
      }
      case o: Object =>{
        log.debug("cache hit - key: {}", key.toString)
        log.trace("cache hit - value: {}", o.asInstanceOf[V])
        Some(Promise.successful(o.asInstanceOf[V]).future)
      }
    }
  }

  def apply(key: Any, genValue: () => Future[V])(implicit ec: ExecutionContext): Future[V] = {

    val optObj = Option(memcachedClient.get(key.toString))
    optObj match {
      case None =>
        log.debug("cache miss - key: {} ", key.toString)
        val future = genValue()
        future.onComplete { value =>
          log.debug("add cache entry - key: {} " + key.toString)
          log.trace("add cache entry - value: {}" + value.get)
          memcachedClient.add(key.toString, timeToLiveSeconds, value.get)
        }
        future
      case Some(obj) => {
        log.debug("cache hit - key: {}", key.toString)
        log.trace("cache hit - value: {}", obj.asInstanceOf[V])
        Future(obj.asInstanceOf[V])
      }
    }

  }

  def remove(key: Any) = {
    log.debug("remove cache entry - key: {}" , key.toString)
    val obj = memcachedClient.get(key.toString)
    obj match {
      case null => None
      case o: Object => {
        memcachedClient.delete(key.toString)
        Some(Promise.successful(o.asInstanceOf[V]).future)
      }
    }
  }

  def clear(): Unit = { } // do nothing.

  def size = 0 // We don't care.  We just need to fulfill the contract of the Cache trait
}

