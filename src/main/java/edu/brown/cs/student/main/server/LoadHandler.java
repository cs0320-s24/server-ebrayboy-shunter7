package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.load.LoadCSV;
import kotlin.Pair;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles requests related to loading CSV files and implements the Route interface.
 */
public class LoadHandler implements Route {
  /**
   * Represents the loaded CSV data as a Pair of content and headers.
   */
  private Pair<List<List<String>>, List<String>> loadedCSV;

  /**
   * Gets the loaded CSV data.
   *
   * @return Pair of CSV content and headers.
   */
  public Pair<List<List<String>>, List<String>> getLoadedCSV() {
    return loadedCSV;
  }

  /**
   * Handles HTTP requests for loading CSV files.
   *
   * @param request  HTTP request.
   * @param response HTTP response.
   * @return Result of handling the request.
   */
  public Object handle(Request request, Response response) {
    Set<String> params = request.queryParams();
    String filePath = request.queryParams("filePath");
    String hasHeader = request.queryParams("hasHeader");

    if (!params.contains("filePath")
        || !params.contains("hasHeader")
        || filePath.isEmpty()
        || hasHeader.isEmpty()) {
      return new LoadFailureResponse("error_bad_request").serialize();
    }

    Map<String, Object> responseMap = new HashMap<>();
    try {
      this.loadedCSV = this.sendRequest(filePath, Boolean.parseBoolean(hasHeader));

      if (this.loadedCSV.getSecond().isEmpty()) {
        return new LoadFailureResponse("error_datasource").serialize();
      }

      responseMap.put("filepath", filePath);

      return new LoadSuccessResponse(responseMap).serialize();
    } catch (Exception e) {
      e.printStackTrace();

      return new LoadFailureResponse("error_datasource").serialize();
    }
  }

  /**
   * Sends a request to load and parse a CSV file.
   *
   * @param filePath  File path of the CSV file.
   * @param hasHeader Indicates whether the CSV file has a header.
   * @return Pair of loaded CSV content and headers.
   */
  public Pair<List<List<String>>, List<String>> sendRequest(String filePath, boolean hasHeader) {
    LoadCSV loadcsv = new LoadCSV(filePath, hasHeader);

    return new Pair<>(loadcsv.parseCSV(), loadcsv.headerList);
  }

  /**
   * Represents a successful response for loading CSV data.
   */
  public record LoadSuccessResponse(String result, Map<String, Object> response) {
    /**
     * Constructs a LoadSuccessResponse with the provided response map, setting the result to
     * "success".
     *
     * @param response Map containing response data.
     */
    public LoadSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

    /**
     * Serializes the LoadSuccessResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the LoadSuccessResponse.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadSuccessResponse> adapter = moshi.adapter(LoadSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Represents a failure response for loading CSV data.
   */
  public record LoadFailureResponse(String result) {

    /**
     * Serializes the LoadFailureResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the LoadFailureResponse.
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadFailureResponse.class).toJson(this);
    }
  }
}
