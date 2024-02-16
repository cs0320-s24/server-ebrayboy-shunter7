package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Implements CacheInterface for caching Census data.
 */
public class CachingCensusDatasource implements CacheInterface {

  private int size;
  private int minutes;

  /**
   * Default constructor initializes size and minutes.
   */
  public CachingCensusDatasource() {
    this.size = 100;
    this.minutes = 1;
  }

  /**
   * Builds and returns a cache configuration for Census data.
   *
   * @return CacheBuilder instance for Census data
   */
  @Override
  public CacheBuilder<Object, Object> buildCache() {
    return CacheBuilder.newBuilder()
        .maximumSize(this.size)
        .expireAfterWrite(this.minutes, TimeUnit.MINUTES)
        .recordStats();
  }
}
