package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ViewHandler implements Route {

  private final List<List<String>> parsedCSV;

  public ViewHandler(List<List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  public List<List<String>> getParsedCSV() {
    return parsedCSV;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    if (this.parsedCSV == null || this.parsedCSV.isEmpty()) {
      return new ViewFailResponse().serialize();
    }

    return new ViewSuccessResponse(this.getParsedCSV()).serialize();
  }

  public record ViewSuccessResponse(String result, List<List<String>> data) {
    public ViewSuccessResponse(List<List<String>> data) {
      this("success", data);
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
}
