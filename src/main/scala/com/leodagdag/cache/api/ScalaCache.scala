package com.leodagdag.cache.api

/**
 * @tparam K type of key
 * @tparam V type of value
 */
trait ScalaCache[K, V] {

  protected val cacheKeyGenerator: CacheKeyGenerator[_] = NoOpCacheKeyGenerator

  /** Returns value corresponding to the given key */
  final def get(key: K): Option[V] = getCachedValue(cacheKey(key))

  /** Sets cache value */
  final def put(key: K, value: V) {
    putValue(cacheKey(key), value)
  }

  /** Returns cached value by using cache key */
  protected def getCachedValue(cacheKey: Any): Option[V]

  /** Sets cache value by using cache key */
  protected def putValue(cacheKey: Any, value: V): Unit

  /** Returns value if a corresponding value exists or returns the result of given f after saving it
    *
    * @param key a value which is used to generate/find return value by f.
    *            This is different from a cache key in almost cases.
    * @param fallback function to get new value
    * @return cached value or the result of given f
    */
  final def withFallback(key: K)(implicit fallback: K => Option[V]): Option[V] = {
    get(key) match {
      case None =>
        fallback(key) match {
          case Some(nv) => {
            put(key, nv)
            Some(nv)
          }
          case None => None
        }
      case Some(v) => {
        Some(v)
      }
    }
  }

  private def cacheKey(key: K): Any = cacheKeyGenerator.generate(key)
}
