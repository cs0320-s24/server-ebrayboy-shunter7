package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.census.Census;
import edu.brown.cs.student.main.census.CensusAPIUtilities;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {
  public Map<String, String> stateCodes;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    //     System.out.println(params);
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    //     System.out.println(participants);
    state = this.stateCodes.get(state);
    county = findCountyCode(state, county);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String censusJson = this.sendRequest(state, county);
      // Deserializes JSON into an Activity
      Census census = CensusAPIUtilities.deserializeCensus(censusJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("census", census);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?
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

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(sentCensusApiResponse);
    System.out.println(sentCensusApiResponse.body());

    return sentCensusApiResponse.body();
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
}
