package edu.brown.cs.student.main.census;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.CensusHandler;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Acts as a cache proxy for Census data, implementing the Route interface.
 */
public class CacheProxy implements Route {

  private final CensusHandler censusHandler;
  private final LoadingCache<String, String> cache;

  /**
   * Constructs a CacheProxy with a CensusHandler and a CacheInterface for caching.
   *
   * @param censusHandler CensusHandler instance
   * @param cache         CacheInterface instance
   */
  public CacheProxy(CensusHandler censusHandler, CacheInterface cache) {
    this.censusHandler = censusHandler;
    this.cache =
        cache
            .buildCache()
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(String key) throws Exception {
                    String[] params = key.split(",");
                    String state = params[0];
                    String county = params[1];
                    return censusHandler.sendRequest(state, county).toString();
                  }
                });
  }

  /**
   * Handles the request by delegating to the underlying CensusHandler.
   *
   * @param request  HTTP request
   * @param response HTTP response
   * @return Result of handling the request
   * @throws Exception if an error occurs during handling
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    return censusHandler.handle(request, response);
  }
}
