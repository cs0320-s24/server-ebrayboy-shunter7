package edu.brown.cs.student.main.csv.load;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.DefaultRow;
import edu.brown.cs.student.main.csv.FactoryFailureException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading CSV files and parsing their content.
 */
public class LoadCSV {
  /**
   * File path of the CSV file.
   */
  private final String filePath;

  /**
   * Indicates whether the CSV file has a header.
   */
  private final boolean hadHeader;

  /**
   * List containing headers if present in the CSV file.
   */
  public List<String> headerList;

  /**
   * Constructs a LoadCSV instance with the specified file path and header information.
   *
   * @param filePath  The path to the CSV file.
   * @param hadHeader Indicates whether the CSV file has a header.
   */
  public LoadCSV(String filePath, boolean hadHeader) {
    this.filePath = filePath;
    this.hadHeader = hadHeader;
  }

  /**
   * Parses the CSV file and returns a list of lists representing the CSV content.
   *
   * @return List of lists containing CSV data.
   */
  public List<List<String>> parseCSV() {
    List<List<String>> parsedCSV = new ArrayList<>();
    try {
      FileReader reader = new FileReader(this.filePath);
      DefaultRow row = new DefaultRow();
      CSVParser<List<String>> parser = new CSVParser<>(reader, row, this.hadHeader);
      parsedCSV = parser.parse();
      this.headerList = parser.headerList;

      return parsedCSV;
    } catch (IOException e) {
      // figure out how to throw error messages to webpage rather than printing them
    } catch (FactoryFailureException e) {
      throw new RuntimeException(e);
    }

    return parsedCSV;
  }
}
