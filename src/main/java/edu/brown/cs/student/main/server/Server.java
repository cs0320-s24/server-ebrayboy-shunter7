package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.census.CacheProxy;
import edu.brown.cs.student.main.census.CachingCensusDatasource;
import spark.Spark;

import java.io.IOException;
import java.net.URISyntaxException;

import static spark.Spark.after;

/**
 * Represents the main class for the server, defining routes and initializing Spark.
 */
public class Server {
  /**
   * Main method to start the server, set up routes, and initialize Spark.
   *
   * @param args Command-line arguments (not used in this implementation).
   * @throws URISyntaxException   If there is an issue with URI syntax.
   * @throws IOException          If there is an I/O related issue.
   * @throws InterruptedException If the execution is interrupted.
   */
  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    LoadHandler loadHandler = new LoadHandler();
    Spark.get("loadcsv", loadHandler);
    CensusHandler censusHandler = new CensusHandler();
    censusHandler.getStateCodes();
    Spark.get("broadband", new CacheProxy(censusHandler, new CachingCensusDatasource()));
    Spark.get(
        "viewcsv",
        (req, res) -> new ViewHandler(loadHandler.getLoadedCSV().getFirst()).handle(req, res));
    Spark.get(
        "searchcsv", (req, res) -> new SearchHandler(loadHandler.getLoadedCSV()).handle(req, res));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
