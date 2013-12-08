package com.leodagdag.cache.api.impl

import com.leodagdag.cache.api.{NoOpCacheKeyGenerator, CacheKeyGenerator, ScalaCache}
import net.sf.ehcache.{Cache, CacheManager, Element}


/** Cache implementation using Ehcache */
private class ScalaCacheImpl[K, V](underlying: Cache,
                                 override val cacheKeyGenerator: CacheKeyGenerator[_]) extends ScalaCache[K, V] {
  def getCachedValue(key: Any): Option[V] = {
    val e = underlying.get(key)
    if (e != null && e.getObjectValue != null) {
      println(s"Found $key in Cache")
      Some(e.getObjectValue.asInstanceOf[V])
    }
    else None
  }

  def putValue(key: Any, value: V) {
    underlying.put(new Element(key, value))
  }
}

/** Cache factory using Ehcache */
object ScalaCacheImpl {
  /** Returns Cache instance
    *
    * @tparam K type of key
    * @tparam V type of value
    * @param name cache name
    */
  def apply[K, V](name: String)(implicit cacheManager: CacheManager, cacheKeyGenerator: CacheKeyGenerator[_] = NoOpCacheKeyGenerator): ScalaCache[K, V] = {
    cacheManager.addCacheIfAbsent(name)
    val c: Cache = cacheManager.getCache(name)
    if (c == null) throw new IllegalArgumentException("no cache %s found".format(name))
    else new ScalaCacheImpl(c, cacheKeyGenerator)
  }
}