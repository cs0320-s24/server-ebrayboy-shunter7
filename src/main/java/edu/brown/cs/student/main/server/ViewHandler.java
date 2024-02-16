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

public class ViewHandler implements Route {

  private final List<List<String>> parsedCSV;

  public ViewHandler(List<List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

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

  public record ViewSuccessResponse(String result, Map<String, Object> response) {
    public ViewSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

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

  public record ViewFailResponse(String result) {
    public ViewFailResponse() {
      this("error_datasource");
    }

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
