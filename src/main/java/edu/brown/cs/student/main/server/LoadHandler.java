package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.csv.load.LoadCSV;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadHandler implements Route {

  public Object handle(Request request, Response response) {
    // ex. http://localhost:3232/activity?participants=num
    //    Set<String> params = request.queryParams();
    //     System.out.println(params);
    String filePath = request.queryParams("filePath");
    String hasHeader = request.queryParams("hasHeader");

    //     System.out.println(participants);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      List<ArrayList<String>> loadCSVJson =
          this.sendRequest(filePath, Boolean.parseBoolean(hasHeader));
      // Deserializes JSON into an Activity
      //      LoadCSV loadCSV = LoadAPIUtilities.deserializeLoadCSV(loadCSVJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("loadCSV", loadCSVJson);

      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private List<ArrayList<String>> sendRequest(String filePath, boolean hasHeader) {
    List<ArrayList<String>> csv = new LoadCSV(filePath, hasHeader).parseCSV();
    return csv;
  }

  //  private String sendRequest(String filePath, boolean hasHeader)
  //      throws URISyntaxException, IOException, InterruptedException {
  //
  //    HttpRequest buildLoadCSVApiRequest =
  //        HttpRequest.newBuilder()
  //            .uri(
  //                new URI(
  //                    "http://localhost:3232/loadcsv?filePath="
  //                    + filePath
  //                    + "&hasHeader="
  //                    + hasHeader))
  //            .GET()
  //            .build();
  //
  //    HttpResponse<String> sentLoadCSVApiResponse =
  //        HttpClient.newBuilder()
  //            .build()
  //            .send(buildLoadCSVApiRequest, HttpResponse.BodyHandlers.ofString());
  //
  //    return sentLoadCSVApiResponse.body();
  //  }
}
