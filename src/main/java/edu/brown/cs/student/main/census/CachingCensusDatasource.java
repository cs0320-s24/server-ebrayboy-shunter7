package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class CachingCensusDatasource implements CacheInterface {

  private int size;
  private int minutes;

  public CachingCensusDatasource() {
    this.size = 100;
    this.minutes = 1;
  }

  @Override
  public CacheBuilder<Object, Object> buildCache() {
    return CacheBuilder.newBuilder()
        .maximumSize(this.size)
        .expireAfterWrite(this.minutes, TimeUnit.MINUTES)
        .recordStats();
  }
}
