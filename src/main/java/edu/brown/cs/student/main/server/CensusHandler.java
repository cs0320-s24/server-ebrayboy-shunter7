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

/**
 * Handles Census-related requests and implements the Route interface.
 */
public class CensusHandler implements Route {
  public Map<String, String> stateCodes;

  /**
   * Handles the Census-related request.
   *
   * @param request  HTTP request
   * @param response HTTP response
   * @return Result of handling the request
   * @throws Exception if an error occurs during handling
   */
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

  /**
   * Sends a request to the Census API and retrieves data.
   *
   * @param state  State code
   * @param county County code
   * @return List of lists containing Census data
   * @throws URISyntaxException   if the URI is malformed
   * @throws IOException          if an I/O error occurs
   * @throws InterruptedException if the operation is interrupted
   */
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

  /**
   * Represents a successful response for Census data with a specific result and response map.
   */
  public record CensusSuccessResponse(String result, Map<String, Object> response) {
    /**
     * Constructs a CensusSuccessResponse with the provided response map, setting the result to
     * "success".
     *
     * @param response Map containing response data
     */
    public CensusSuccessResponse(Map<String, Object> response) {
      this("success", response);
    }

    /**
     * Serializes the CensusSuccessResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the CensusSuccessResponse
     */
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

  /**
   * Represents a failed response for Census data with a specific result.
   */
  public record CensusFailResponse(String result) {
    /**
     * Constructs a CensusFailResponse with the default result "error_datasource".
     */
    public CensusFailResponse() {
      this("error_datasource");
    }

    /**
     * Serializes the CensusFailResponse to a JSON string using Moshi.
     *
     * @return JSON representation of the CensusFailResponse
     */
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

  /**
   * Retrieves state codes from the Census API and populates the stateCodes map.
   *
   * @throws URISyntaxException   if the URI is malformed
   * @throws IOException          if an I/O error occurs
   * @throws InterruptedException if the operation is interrupted
   */
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

  /**
   * Finds the Census county code based on the state code and county name.
   *
   * @param stateCode  State code
   * @param countyName County name
   * @return Census county code
   * @throws URISyntaxException   if the URI is malformed
   * @throws IOException          if an I/O error occurs
   * @throws InterruptedException if the operation is interrupted
   */
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

  /**
   * Cleans the parsed CSV data by removing quotes from string elements.
   *
   * @param originalData Original data with quotes
   * @return Cleaned data without quotes
   */
  private List<List<String>> cleanParsedCSVData(List<List<String>> originalData) {
    return originalData.stream()
        .map(list -> list.stream().map(this::removeQuotesFromString).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  /**
   * Removes quotes from the beginning and end of a string.
   *
   * @param input Input string with quotes
   * @return String without quotes
   */
  private String removeQuotesFromString(String input) {
    if (input.startsWith("\"") && input.endsWith("\"")) {
      return input.substring(1, input.length() - 1);
    }
    return input;
  }
}
