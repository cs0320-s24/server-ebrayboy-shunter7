package edu.brown.cs.student.census;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.server.CensusHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

public class TestCensusAPIHandlers {
  @Test
  void testApiHandle() {
    try {
      CensusHandler test = new CensusHandler();
      test.getStateCodes();
      assertEquals(test.findCountyCode("06", "Kings County"), "031");

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
