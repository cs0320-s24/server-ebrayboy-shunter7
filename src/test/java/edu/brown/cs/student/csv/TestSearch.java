package edu.brown.cs.student.csv;

import edu.brown.cs.student.main.csv.FactoryFailureException;
import edu.brown.cs.student.main.csv.Util;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSearch {

  @Test
  public void testBasic() throws IOException, FactoryFailureException {
    Util testBasic =
        new Util(
            "/Users/sanya/cs0320/server-ebrayboy-shunter7/data/census/income_by_race.csv",
            "asian",
            1,
            true);
    List<List<String>> getResults = testBasic.getResults();
    assertEquals(40, testBasic.getResults().size());
    for (List<String> ele : getResults) {
      assertEquals("Asian", ele.get(1));
    }
  }

  @Test
  public void testEarnings() throws IOException, FactoryFailureException {
    Util earningsTest =
        new Util(
            "/Users/sanya/cs0320/server-ebrayboy-shunter7/data/census/dol_ri_earnings_disparity.csv",
            "RI",
            null,
            true);
    List<List<String>> getResults = earningsTest.getResults();

    assertEquals(6, getResults.size());
  }

  @Test
  public void testEducation() throws IOException, FactoryFailureException {
    Util eduTest =
        new Util(
            "/Users/sanya/cs0320/server-ebrayboy-shunter7/data/census/postsecondary_education.csv",
            "Brown University",
            null,
            true);
    List<List<String>> getResults = eduTest.getResults();

    assertEquals(16, getResults.size());

    Util eduTestEmpty =
        new Util(
            "/Users/sanya/cs0320/server-ebrayboy-shunter7/data/census/postsecondary_education.csv",
            "hello",
            null,
            true);
    List<List<String>> getResultsEmpty = eduTestEmpty.getResults();

    assertEquals(0, getResultsEmpty.size());
  }
}
