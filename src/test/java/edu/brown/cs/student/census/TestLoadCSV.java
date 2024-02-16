package edu.brown.cs.student.census;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.LoadHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

public class TestLoadCSV {

  @Test
  void testLoad() {
    LoadHandler loadHandler = new LoadHandler();
    Request request = makeRequest("/Users/elybrayboy/Desktop/server-ebrayboy-shunter7/data/census/dol_ri_earnings_disparity.csv", "true");
    Response response = makeResponse();
    Object result = loadHandler.handle(request, response);

    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> jsonAdapter = moshi.adapter(type);

    try {
      Map<String, Object> map = jsonAdapter.fromJson(result.toString());
      assertEquals("success" ,map.get("result"));

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private Request makeRequest(String filePath, String hasHeader){
    return new Request(){
      @Override
      public String queryParams(String params){
        if (params.equals("filePath")) return filePath;
        else if (params.equals("hasHeader")) return hasHeader;
        return null;
      }
    };
  }

  private Response makeResponse(){
    return new Response(){
      @Override
      public void status(int statusCode){}
    };
  }
}
