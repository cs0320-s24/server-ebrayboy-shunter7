package edu.brown.cs.student.main.census;
import com.google.common.cache.CacheBuilder;

public interface CacheInterface {
  CacheBuilder<Object, Object> buildCache();

}
