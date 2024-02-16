package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.csv.search.SearchCSV;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import kotlin.Pair;

/** Utility class that controls the overhead work of the program and is called directly from main */
public class Util {
  public final String filename;
  public final String searchTarget;
  public final Object columnID;
  public final Boolean hasHeader;

  /**
   * @param filename name of the file to be parsed and searched
   * @param searchTarget the desired string to be searched for in the data
   * @param columnID an ID, either a String or an Integer that represents the column in which to
   *     look for the search target
   * @param hasHeader A boolean representing weather or not the file inputted has a header
   */
  public Util(String filename, String searchTarget, Object columnID, Boolean hasHeader) {
    this.filename = filename;
    this.searchTarget = searchTarget;
    this.columnID = columnID;
    this.hasHeader = hasHeader;
  }

  /**
   * @return Returns a list contains each row in which a searchTarget is found
   * @throws IOException
   * @throws FactoryFailureException
   */
  public List<List<String>> getResults() throws IOException, FactoryFailureException {

    SearchCSV searcher;
    CSVParser<List<String>> parser =
        new CSVParser<>(new FileReader(this.filename), new DefaultRow(), hasHeader);
    if (this.columnID == null) {
      searcher =
          new SearchCSV(
              new Pair<>(parser.Parse(), parser.headerList), this.searchTarget, this.hasHeader);
    } else if (this.columnID instanceof String) {
      searcher =
          new SearchCSV(
              new Pair<>(parser.Parse(), parser.headerList),
              this.searchTarget,
              (String) this.columnID,
              this.hasHeader);
    } else {
      searcher =
          new SearchCSV(
              new Pair<>(parser.Parse(), parser.headerList),
              this.searchTarget,
              (Integer) this.columnID,
              this.hasHeader);
    }
    return searcher.search();
  }
}
