package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import edu.brown.cs.student.main.csv.search.SearchCSV;
import kotlin.Pair;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchHandler implements Route {
  private final Pair<List<List<String>>, List<String>> parsedCSV;

  public SearchHandler(Pair<List<List<String>>, List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    if (this.parsedCSV.getFirst() == null || this.parsedCSV.getFirst().isEmpty()) {
      return new SearchFailResponse().serialize();
    }

    Map<String, Object> responseMap = new HashMap<>();

    String searchTarget = request.queryParams("searchTarget");
    String columnID = request.queryParams("columnID");
    String hasHeader = request.queryParams("hasHeader");

    if (searchTarget.isEmpty() || hasHeader.isEmpty()) {
      return new SearchFailResponse("error_bad_request").serialize();
    }

    Integer columnIDInt = null;
    String columnIDStr = null;

    if (!columnID.isEmpty()) {
      try {
        columnIDInt = Integer.valueOf(columnID);
      } catch (NumberFormatException e) {
        columnIDStr = columnID;
      }
    }

    try {
      List<List<String>> searchResultsJson;

      if (columnIDStr != null) {
        searchResultsJson =
            this.sendRequest(searchTarget, columnIDStr, Boolean.parseBoolean(hasHeader));
      } else if (columnIDInt != null) {

        searchResultsJson =
            this.sendRequest(searchTarget, columnIDInt, Boolean.parseBoolean(hasHeader));
      } else {

        searchResultsJson = this.sendRequest(searchTarget, Boolean.parseBoolean(hasHeader));
      }

      responseMap.put("data", searchResultsJson);

      return new SearchSuccessResponse(responseMap).serialize();
    } catch (Exception e) {
      e.printStackTrace();

      return new SearchFailResponse().serialize();
    }
  }

  public List<List<String>> sendRequest(String searchTarget, String columnID, boolean hasHeader)
      throws IOException, FactoryFailureException {

    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
            new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()),
            searchTarget,
            columnID,
            hasHeader)
        .search();
  }

  public List<List<String>> sendRequest(String searchTarget, Integer columnID, boolean hasHeader)
      throws IOException, FactoryFailureException {

    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
            new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()),
            searchTarget,
            columnID,
            hasHeader)
        .search();
  }

  public List<List<String>> sendRequest(String searchTarget, boolean hasHeader)
      throws IOException, FactoryFailureException {

    List<List<String>> cleanedParsedCSV = cleanParsedCSVData(this.parsedCSV.getFirst());

    return new SearchCSV(
            new Pair<>(cleanedParsedCSV, this.parsedCSV.getSecond()), searchTarget, hasHeader)
        .search();
  }

  public record SearchSuccessResponse(String result, Map<String, Object> response) {
    public SearchSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

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

  public record SearchFailResponse(String result) {
    public SearchFailResponse() {
      this("error_datasource");
    }

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

  private List<List<String>> cleanParsedCSVData(List<List<String>> originalData) {
    return originalData.stream()
        .map(list -> list.stream().map(this::removeQuotesFromString).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private String removeQuotesFromString(String input) {
    if (input.startsWith("\"") && input.endsWith("\"")) {
      return input.substring(1, input.length() - 1);
    }
    return input;
  }
}
