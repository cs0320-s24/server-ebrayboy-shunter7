package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheBuilder;

/**
 * Defines the Cache interface with a method to build a cache.
 */
public interface CacheInterface {

  /**
   * Builds and returns a cache using CacheBuilder.
   *
   * @return CacheBuilder instance
   */
  CacheBuilder<Object, Object> buildCache();
}
