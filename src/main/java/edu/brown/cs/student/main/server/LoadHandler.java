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

public class LoadHandler implements Route {

  private Pair<List<List<String>>, List<String>> loadedCSV;

  public Pair<List<List<String>>, List<String>> getLoadedCSV() {
    return loadedCSV;
  }

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

  public Pair<List<List<String>>, List<String>> sendRequest(String filePath, boolean hasHeader) {
    LoadCSV loadcsv = new LoadCSV(filePath, hasHeader);

    return new Pair<>(loadcsv.parseCSV(), loadcsv.headerList);
  }

  public record LoadSuccessResponse(String result, Map<String, Object> response) {
    public LoadSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

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

  public record LoadFailureResponse(String result) {

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadFailureResponse.class).toJson(this);
    }
  }
}
