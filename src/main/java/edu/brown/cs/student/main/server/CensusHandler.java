package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CensusHandler implements Route {
  public Map<String, String> stateCodes;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    if (!params.contains("state")
        || !params.contains("county")
        || state.isEmpty()
        || county.isEmpty()) {
      return new CensusFailResponse("error_bad_request").serialize();
    }

    String stateCode = this.stateCodes.get(state);
    String countyCode = findCountyCode(stateCode, county);

    Map<String, Object> responseMap = new HashMap<>();
    try {
      List<List<String>> censusJson = this.sendRequest(stateCode, countyCode);

      LocalDateTime myDateObj = LocalDateTime.now();
      DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      String formattedDate = myDateObj.format(myFormatObj);

      responseMap.put("data", censusJson);
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("date-time-retrieved", formattedDate);

      return new CensusSuccessResponse(responseMap).serialize();
    } catch (Exception e) {
      e.printStackTrace();

      return new CensusFailResponse().serialize();
    }
  }

  public List<List<String>> sendRequest(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {

    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                    + county
                    + "&in=state:"
                    + state))
            .GET()
            .build();

    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    Type listType =
        Types.newParameterizedType(
            List.class, Types.newParameterizedType(List.class, String.class));
    JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(listType);

    List<List<String>> data = jsonAdapter.fromJson(sentCensusApiResponse.body());

    return cleanParsedCSVData(data);
  }

  public record CensusSuccessResponse(String result, Map<String, Object> response) {
    public CensusSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CensusSuccessResponse> adapter = moshi.adapter(CensusSuccessResponse.class);

        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  public record CensusFailResponse(String result) {
    public CensusFailResponse() {
      this("error_datasource");
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CensusFailResponse> adapter = moshi.adapter(CensusFailResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {

        e.printStackTrace();
        throw e;
      }
    }
  }

  public void getStateCodes() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
            .GET()
            .build();
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(listType);
    List<List<String>> responseList = jsonAdapter.fromJson(sentCensusApiResponse.body());
    Map<String, String> codes = new HashMap<>();
    for (int i = 1; i < responseList.size(); i++) {
      List<String> stateInfo = responseList.get(i);

      codes.put(stateInfo.get(0), stateInfo.get(1));
    }
    this.stateCodes = codes;
  }

  public static String findCountyCode(String stateCode, String countyName)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                    + stateCode))
            .GET()
            .build();
    HttpResponse<String> sentResponse =
        HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
    Moshi moshi = new Moshi.Builder().build();
    Type listType = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(listType);

    List<List<String>> counties = jsonAdapter.fromJson(sentResponse.body());
    for (List<String> county : counties) {
      String currCountyName = county.get(0).split(",")[0];
      if (currCountyName.equals(countyName)) {
        return county.get(2);
      }
    }
    return "";
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
