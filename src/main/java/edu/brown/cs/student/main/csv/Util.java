package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.csv.search.SearchCSV;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import kotlin.Pair;

/**
 * Utility class that controls the overhead work of the program and is called directly from main.
 */
public class Util {
  public final String filename;
  public final String searchTarget;
  public final Object columnID;
  public final Boolean hasHeader;

  /**
   * Constructs a Util object with specified parameters.
   *
   * @param filename Name of the file to be parsed and searched.
   * @param searchTarget The desired string to be searched for in the data.
   * @param columnID An ID, either a String or an Integer, representing the column in which to look
   *     for the search target.
   * @param hasHeader A boolean representing whether or not the file inputted has a header.
   */
  public Util(String filename, String searchTarget, Object columnID, Boolean hasHeader) {
    this.filename = filename;
    this.searchTarget = searchTarget;
    this.columnID = columnID;
    this.hasHeader = hasHeader;
  }

  /**
   * Retrieves a list containing each row in which a searchTarget is found.
   *
   * @return A list containing each row in which a searchTarget is found.
   * @throws IOException If there is an issue with I/O operations.
   * @throws FactoryFailureException If there is a failure during factory operation.
   */
  public List<List<String>> getResults() throws IOException, FactoryFailureException {

    SearchCSV searcher;
    CSVParser<List<String>> parser =
        new CSVParser<>(new FileReader(this.filename), new DefaultRow(), hasHeader);
    if (this.columnID == null) {
      searcher =
          new SearchCSV(
              new Pair<>(parser.parse(), parser.headerList), this.searchTarget, this.hasHeader);
    } else if (this.columnID instanceof String) {
      searcher =
          new SearchCSV(
              new Pair<>(parser.parse(), parser.headerList),
              this.searchTarget,
              (String) this.columnID,
              this.hasHeader);
    } else {
      searcher =
          new SearchCSV(
              new Pair<>(parser.parse(), parser.headerList),
              this.searchTarget,
              (Integer) this.columnID,
              this.hasHeader);
    }
    return searcher.search();
  }
}
