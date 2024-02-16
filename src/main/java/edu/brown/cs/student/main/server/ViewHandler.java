package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a Spark route handler for viewing parsed CSV data.
 */
public class ViewHandler implements Route {

  private final List<List<String>> parsedCSV;

  /**
   * Constructs a ViewHandler with the provided parsed CSV data.
   *
   * @param parsedCSV Parsed CSV data to be viewed.
   */
  public ViewHandler(List<List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  /**
   * Handles the request to view parsed CSV data.
   *
   * @param request  The Spark HTTP request.
   * @param response The Spark HTTP response.
   * @return Object representing the response to the request.
   */
  @Override
  public Object handle(Request request, Response response) {

    if (this.parsedCSV == null || this.parsedCSV.isEmpty()) {
      return new ViewFailResponse().serialize();
    }

    List<List<String>> cleanedData = cleanParsedCSVData(this.parsedCSV);

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("data", cleanedData);

    return new ViewSuccessResponse(responseMap).serialize();
  }

  /**
   * Represents a success response for viewing parsed CSV data.
   */
  public record ViewSuccessResponse(String result, Map<String, Object> response) {
    /**
     * Constructs a ViewSuccessResponse record with the given response map.
     *
     * @param response The response map.
     */
    public ViewSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

    /**
     * Serializes the ViewSuccessResponse to JSON.
     *
     * @return JSON representation of the ViewSuccessResponse.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ViewSuccessResponse> adapter = moshi.adapter(ViewSuccessResponse.class);

        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Represents a failure response for viewing parsed CSV data.
   */
  public record ViewFailResponse(String result) {

    /**
     * Constructs a ViewFailResponse record with the given result.
     */
    public ViewFailResponse() {
      this("error_datasource");
    }

    /**
     * Serializes the ViewFailResponse to JSON.
     *
     * @return JSON representation of the ViewFailResponse.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ViewFailResponse> adapter = moshi.adapter(ViewFailResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Cleans the parsed CSV data by removing quotes from each element in each row.
   *
   * @param originalData List of lists containing the original parsed CSV data.
   * @return List of lists containing the cleaned parsed CSV data.
   */
  private List<List<String>> cleanParsedCSVData(List<List<String>> originalData) {
    return originalData.stream()
        .map(list -> list.stream().map(this::removeQuotesFromString).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  /**
   * Removes quotes from a given input string if it starts and ends with quotes.
   *
   * @param input The input string to be processed.
   * @return The input string without starting and ending quotes (if present).
   */
  private String removeQuotesFromString(String input) {
    if (input.startsWith("\"") && input.endsWith("\"")) {
      return input.substring(1, input.length() - 1);
    }
    return input;
  }
}
