package edu.brown.cs.student.main.csv.load;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.DefaultRow;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadCSV {
  private String filePath;
  private boolean hadHeader;
  private boolean hasLoaded;

  public LoadCSV(String filePath, boolean hadHeader) {
    this.filePath = filePath;
    this.hadHeader = hadHeader;
    this.hasLoaded = false;
  }
  ;

  public List<ArrayList<String>> parseCSV() {
    List<ArrayList<String>> parsedCSV = new ArrayList<>();
    try {
      FileReader reader = new FileReader(this.filePath);
      DefaultRow row = new DefaultRow();
      parsedCSV = new CSVParser<>(reader, row, this.hadHeader).Parse();
      this.hasLoaded = true;
      return parsedCSV;
    } catch (IOException e) {
      // figure out how to throw error messages to webpage rather than printing them
    } catch (FactoryFailureException e) {
      throw new RuntimeException(e);
    }

    return parsedCSV;
  }
}
