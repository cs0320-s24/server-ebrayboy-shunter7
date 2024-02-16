package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.search.SearchCSV;
import kotlin.Pair;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles HTTP requests related to searching within a parsed CSV file and implements the Route
 * interface.
 */
public class SearchHandler implements Route {
  private final Pair<List<List<String>>, List<String>> parsedCSV;

  /**
   * Constructs a SearchHandler instance with the specified parsed CSV data.
   *
   * @param parsedCSV Pair of parsed content and headers.
   */
  public SearchHandler(Pair<List<List<String>>, List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  /**
   * Handles HTTP requests for searching within a parsed CSV file.
   *
   * @param request  HTTP request.
   * @param response HTTP response.
   * @return Result of handling the request.
   * @throws Exception if an error occurs during handling.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    if (this.parsedCSV.getFirst() == null || this.parsedCSV.getFirst().isEmpty()) {
      return new SearchFailResponse().serialize();
    }

    Set<String> params = request.queryParams();
    String searchTarget = request.queryParams("searchTarget");
    String columnID = request.queryParams("columnID");
    String hasHeader = request.queryParams("hasHeader");

    if (!params.contains("searchTarget")
        || !params.contains("hasHeader")
        || searchTarget.isEmpty()
        || hasHeader.isEmpty()) {
      return new SearchFailResponse("error_bad_request").serialize();
    }

    List<List<String>> searchResultsJson;

    // Check if columnID is provided and parse it if present
    if (columnID != null && !columnID.isEmpty()) {
      Integer columnIDInt = null;
      String columnIDStr = null;
      try {
        columnIDInt = Integer.valueOf(columnID);
      } catch (NumberFormatException e) {
        columnIDStr = columnID;
      }

      // Use the appropriate sendRequest method based on the type of columnID provided
      if (columnIDStr != null) {
        searchResultsJson =
            this.sendRequest(searchTarget, columnIDStr, Boolean.parseBoolean(hasHeader));
      } else {
        searchResultsJson =
            this.sendRequest(searchTarget, columnIDInt, Boolean.parseBoolean(hasHeader));
      }
    } else {
      // If no columnID is provided, search across all columns
      searchResultsJson = this.sendRequest(searchTarget, Boolean.parseBoolean(hasHeader));
    }

    Map<String, Object> responseMap = new HashMap<>();

    responseMap.put("hasHeader", hasHeader);
    responseMap.put("searchTarget", searchTarget);

    if (columnID != null && !columnID.isEmpty()) {
      responseMap.put("columnID", columnID);
    }

    responseMap.put("data", searchResultsJson);

    return new SearchSuccessResponse(responseMap).serialize();
  }

  /**
   * Sends a search request based on the provided parameters.
   *
   * @param searchTarget The word the user is trying to search for.
   * @param columnID     The ID of the column (can be in integer or string form).
   * @param hasHeader    A boolean representing whether or not the inputted file has a header.
   * @return List of lists containing search results.
   */
  public List<List<String>> sendRequest(String searchTarget, String columnID, boolean hasHeader) {
    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
        new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()),
        searchTarget,
        columnID,
        hasHeader)
        .search();
  }

  /**
   * Sends a search request based on the provided parameters.
   *
   * @param searchTarget The word the user is trying to search for.
   * @param columnID     The ID of the column (in integer form).
   * @param hasHeader    A boolean representing whether or not the inputted file has a header.
   * @return List of lists containing search results.
   */
  public List<List<String>> sendRequest(String searchTarget, Integer columnID, boolean hasHeader) {
    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
        new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()),
        searchTarget,
        columnID,
        hasHeader)
        .search();
  }

  /**
   * Sends a search request based on the provided parameters.
   *
   * @param searchTarget The word the user is trying to search for.
   * @param hasHeader    A boolean representing whether or not the inputted file has a header.
   * @return List of lists containing search results.
   */
  public List<List<String>> sendRequest(String searchTarget, boolean hasHeader) {
    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
        new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()), searchTarget, hasHeader)
        .search();
  }

  /**
   * Represents a successful response for a search operation within a CSV file.
   */
  public record SearchSuccessResponse(String result, Map<String, Object> response) {

    /**
     * Constructs a SearchSuccessResponse with the provided response map, setting the result to
     * "success".
     *
     * @param response Map containing response data.
     */
    public SearchSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

    /**
     * Serializes the SearchSuccessResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the SearchSuccessResponse.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SearchSuccessResponse> adapter = moshi.adapter(SearchSuccessResponse.class);

        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Represents a failure response for a search operation within a CSV file.
   */
  public record SearchFailResponse(String result) {
    /**
     * Constructs a SearchFailResponse with the default result "error_datasource".
     */
    public SearchFailResponse() {
      this("error_datasource");
    }

    /**
     * Serializes the SearchFailResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the SearchFailResponse.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SearchFailResponse> adapter = moshi.adapter(SearchFailResponse.class);
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
