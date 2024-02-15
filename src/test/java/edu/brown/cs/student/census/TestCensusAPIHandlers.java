package edu.brown.cs.student.census;

import edu.brown.cs.student.main.server.CensusHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestCensusAPIHandlers {
  @Test
  void testApiHandle() {
    try {
      CensusHandler test = new CensusHandler();
      test.getStateCodes();

      System.out.println(test.findCountyCode("06", "Kings County"));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
