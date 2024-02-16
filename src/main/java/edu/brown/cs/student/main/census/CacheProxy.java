package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.CensusHandler;
import spark.Request;
import spark.Response;
import spark.Route;

public class CacheProxy implements Route {

  private final CensusHandler censusHandler;
  private final LoadingCache<String, String> cache;

  public CacheProxy(CensusHandler censusHandler, CacheInterface cache) {
    this.censusHandler = censusHandler;
    this.cache =
        cache
            .buildCache()
            .build(
                new CacheLoader<String, String>() {
                  @Override
                  public String load(String key) throws Exception {
                    String[] params = key.split(",");
                    String state = params[0];
                    String county = params[1];
                    return censusHandler.sendRequest(state, county).toString();
                  }
                });
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return censusHandler.handle(request, response);
  }
}
