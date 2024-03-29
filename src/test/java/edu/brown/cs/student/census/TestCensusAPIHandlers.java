package edu.brown.cs.student.census;

// public class TestCensusAPIHandlers {
//
//  private CensusHandler censusHandler;
//
//  @BeforeEach
//  public void setup() throws URISyntaxException, IOException, InterruptedException {
//    censusHandler = new CensusHandler();
//  }
//
//  @Test
//  void testApiHandle() throws Exception {
//
//    Request request = makeRequest("Arizona", "Maricopa County");
//    Response response = makeResponse();
//    Object result = censusHandler.handle(request, response);
//
//    Moshi moshi = new Moshi.Builder().build();
//    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
//    JsonAdapter<Map<String, Object>> jsonAdapter = moshi.adapter(type);
//
//    try {
//      Map<String, Object> map = jsonAdapter.fromJson(result.toString());
//      assertEquals("success", map.get("result"));
//      assertEquals(
//          "{data=[[NAME, S2802_C03_022E, state, county], [Maricopa County, Arizona, 91.2, 04,
// 013]]}"
//              .toString(),
//          map.get("response").toString());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  void TestGetStateCodes() throws URISyntaxException, IOException, InterruptedException {
//    CensusHandler test = new CensusHandler();
//    test.getStateCodes();
//    assertEquals(test.findCountyCode("06", "Kings County"), "031");
//  }
//
//  private Request makeRequest(String state, String county) {
//    return new Request() {
//      @Override
//      public String queryParams(String params) {
//        if (params.equals("state")) return state;
//        else if (params.equals("county")) return county;
//        return null;
//      }
//    };
//  }
//
//  private Response makeResponse() {
//    return new Response() {
//      @Override
//      public void status(int statusCode) {}
//    };
//  }
//  // private Response makeResponse(){}
// }
