package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.load.LoadCSV;
import java.util.List;
import kotlin.Pair;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadHandler implements Route {

  private Pair<List<List<String>>, List<String>> loadedCSV;

  public Pair<List<List<String>>, List<String>> getLoadedCSV() {
    return loadedCSV;
  }

  public Object handle(Request request, Response response) {
    String filePath = request.queryParams("filePath");
    String hasHeader = request.queryParams("hasHeader");

    if (filePath.isEmpty() || hasHeader.isEmpty()) {
      return new LoadFailureResponse("error_bad_request").serialize();
    }

    try {
      this.loadedCSV = this.sendRequest(filePath, Boolean.parseBoolean(hasHeader));

      if (this.loadedCSV.getSecond().isEmpty()) {
        return new LoadFailureResponse("error_datasource").serialize();
      }

      return new LoadSuccessResponse(filePath).serialize();
    } catch (Exception e) {
      e.printStackTrace();

      return new LoadFailureResponse("error_datasource").serialize();
    }
  }

  public Pair<List<List<String>>, List<String>> sendRequest(String filePath, boolean hasHeader) {
    LoadCSV loadcsv = new LoadCSV(filePath, hasHeader);

    return new Pair<>(loadcsv.parseCSV(), loadcsv.headerList);
  }

  public record LoadSuccessResponse(String result, String filepath) {
    public LoadSuccessResponse(String filepath) {
      this("success", filepath);
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
