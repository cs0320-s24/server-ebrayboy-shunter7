package edu.brown.cs.student.main.csv.search;

import edu.brown.cs.student.main.csv.CreatorFromRow;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;

/** This is a search class that is responsible for searching the parsed data of a CSV file. */
public class SearchCSV implements CreatorFromRow<List<String>> {
  public final Pair<List<List<String>>, List<String>> parsedData;
  public final String searchTarget;
  public final Integer columnIDInteger;
  public final String columnIDString;
  public final Boolean hasHeader;

  /**
   * Constructs a SearchCSV instance with the specified parameters.
   *
   * @param parsedData Pair of parsed content and headers.
   * @param searchTarget The word the user is trying to search for.
   * @param columnID The numerical "ID" of the column in integer form.
   * @param hasHeader A boolean representing whether or not the inputted file has a header.
   */
  public SearchCSV(
      Pair<List<List<String>>, List<String>> parsedData,
      String searchTarget,
      Integer columnID,
      Boolean hasHeader) {
    this.parsedData = parsedData;
    this.searchTarget = searchTarget;
    this.columnIDInteger = columnID;
    this.columnIDString = null;
    this.hasHeader = hasHeader;
  }

  /**
   * This is a constructor for SearchCSV that accepts a column ID in String form.
   *
   * @param parsedData Pair of parsed content and headers.
   * @param searchTarget The "target" word the user is trying to search for
   * @param columnID The string "ID" of the column the user is looking for the searchTarget in
   * @param hasHeader A boolean representing weather or not the inputted file has a header
   */
  public SearchCSV(
      Pair<List<List<String>>, List<String>> parsedData,
      String searchTarget,
      String columnID,
      Boolean hasHeader) {
    this.parsedData = parsedData;
    this.searchTarget = searchTarget;
    this.columnIDInteger = null;
    this.columnIDString = columnID;
    this.hasHeader = hasHeader;
  }

  /**
   * This is a constructor for SearchCSV that accepts a column ID in String form.
   *
   * @param parsedData Pair of parsed content and headers.
   * @param searchTarget The "target" word the user is trying to search for
   * @param hasHeader A boolean representing weather or not the inputted file has a header
   */
  public SearchCSV(
      Pair<List<List<String>>, List<String>> parsedData, String searchTarget, Boolean hasHeader) {
    this.parsedData = parsedData;
    this.searchTarget = searchTarget;
    this.hasHeader = hasHeader;
    this.columnIDInteger = null;
    this.columnIDString = null;
  }

  /**
   * Searches for the specified target in the CSV data.
   *
   * @return List of lists containing search results.
   */
  public List<List<String>> search() {
    Boolean searchAll = true;
    Integer columnIndex = 0;

    if (this.columnIDString != null || this.columnIDInteger != null) {
      searchAll = false;
      if (this.columnIDInteger != null) {
        columnIndex = this.columnIDInteger;
      }
    }

    if (this.hasHeader) {
      List<String> headerList = this.parsedData.getSecond();

      if (this.columnIDString != null) {
        for (int index = 0; index < headerList.size(); index++) {
          if (headerList.get(index).equalsIgnoreCase(this.columnIDString)) {
            columnIndex = index;
          }
        }
      }
    }

    List<List<String>> searchResults = new ArrayList<>();
    for (List<String> row : this.parsedData.getFirst()) {
      if (!searchAll) {
        if (row.get(columnIndex).toLowerCase().contains(this.searchTarget.toLowerCase())) {
          searchResults.add(row);
        }
      } else {
        for (String string : row) {
          if (string.toLowerCase().contains(this.searchTarget.toLowerCase())) {
            searchResults.add(row);
            break;
          }
        }
      }
    }
    return searchResults;
  }

  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
