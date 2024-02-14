package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewHandler implements Route {

  private List<ArrayList<String>> parsedCSV;

  public ViewHandler(List<ArrayList<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    System.out.println(this.parsedCSV);
    if (this.parsedCSV == null || this.parsedCSV.isEmpty()) {
      return new ViewFailResponse().serialize();
    }

    Map<String, Object> responseMap = new HashMap<>();

    responseMap.put("csv", this.parsedCSV);

    return new ViewSuccessResponse(responseMap).serialize();
  }

  private record ViewSuccessResponse(String result, Map<String, Object> data) {
    public ViewSuccessResponse(Map<String, Object> data) {
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

  private record ViewFailResponse(String result) {
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
