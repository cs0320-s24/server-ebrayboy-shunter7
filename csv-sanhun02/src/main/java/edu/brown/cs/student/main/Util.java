package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.List;

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

    Search searcher;
    if (this.columnID == null) {
      searcher = new Search(this.filename, this.searchTarget, this.hasHeader);
    } else if (this.columnID instanceof String) {
      searcher =
          new Search(this.filename, this.searchTarget, (String) this.columnID, this.hasHeader);
    } else {
      searcher =
          new Search(this.filename, this.searchTarget, (Integer) this.columnID, this.hasHeader);
    }
    return searcher.search();
  }
}
