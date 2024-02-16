package edu.brown.cs.student.csv;

import edu.brown.cs.student.main.csv.FactoryFailureException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TestParse {

  @Test
  public void testBasic() throws IOException, FactoryFailureException {
    //    Reader testReader =
    //        new FileReader(
    //            "/Users/sanya/cs0320/server-ebrayboy-shunter7/data/malformed/malformed_signs.csv")
    // {};
    //    Builder builder = new Builder();
    //
    //    CSVParser<Astro> astroCSVParser = new CSVParser<>(testReader, builder, true);
    //    List<Astro> astroList = astroCSVParser.Parse();
    //    System.out.println(astroList);
    //    assertEquals("Annie", astroList.get(0).name);
    //    assertEquals("Aries", astroList.get(0).starsign);
    //
    //    assertEquals("Nicole", astroList.get(4).name);
    //    assertEquals("Scorpio", astroList.get(4).starsign);
    //
    //    assertEquals("Danny", astroList.get(7).name);
    //    assertEquals("Pisces", astroList.get(7).starsign);
  }

  @Test
  public void testErrorHandling() throws IOException, FactoryFailureException {
    //    Reader testReader =
    //        new FileReader(
    //
    // "/Users/elybrayboy/Desktop/cs32/sprints/csv-elybrayboy/data/malformed/malforhmed_signs.csv")
    // {};
    //    Builder builder = new Builder();
    //
    //    CSVParser<Astro> astroCSVParser = new CSVParser<>(testReader, builder, true);
    //    List<Astro> astroList = astroCSVParser.Parse();
  }
}
