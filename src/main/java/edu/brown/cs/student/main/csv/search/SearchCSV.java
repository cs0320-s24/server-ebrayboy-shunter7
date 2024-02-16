package edu.brown.cs.student.main.csv.search;

import edu.brown.cs.student.main.csv.CreatorFromRow;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;

/** This is a search class that is responsible for searching the parsed data of a CSV file */
public class SearchCSV implements CreatorFromRow<List<String>> {
  public final Pair<List<List<String>>, List<String>> parsedData;
  public final String searchTarget;
  public final Integer columnIDInteger;
  public final String columnIDString;
  public final Boolean hasHeader;

  /**
   * This is a constructor for SearchCSV that accepts a column ID in integer form
   *
   * @param searchTarget The "target" word the user is trying to search for
   * @param columnID The numerical "ID" of the column the user is looking for the searchTarget in
   * @param hasHeader A boolean representing weather or not the inputted file has a header
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
   * This is a constructor for SearchCSV that accepts a column ID in String form
   *
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

  public SearchCSV(
      Pair<List<List<String>>, List<String>> parsedData, String searchTarget, Boolean hasHeader) {
    this.parsedData = parsedData;
    this.searchTarget = searchTarget;
    this.hasHeader = hasHeader;
    this.columnIDInteger = null;
    this.columnIDString = null;
  }

  public List<List<String>> search() throws IOException, FactoryFailureException {

    // creating a Boolean to tell us weather we need to check a speific column number or row
    Boolean searchAll = true;
    // creating an index to look for if we are not searching all
    Integer columnIndex = 0;
    // checking to tell if there is a columnID, if yes, setting search all to false
    if (this.columnIDString != null || this.columnIDInteger != null) {
      searchAll = false;
      if (this.columnIDInteger != null) {
        columnIndex = this.columnIDInteger;
      }
    }

    // creating file reader to pass into parser
    //    FileReader reader = new FileReader(this.filename);
    //    // creating parser
    //    CSVParser<List<String>> parser = new CSVParser<>(reader, this, this.hasHeader);
    // getting parsed data
    // List<List<String>> parsedCSV = new LoadHandler().getLoadedCSV();
    //    List<List<String>> parsedData = parser.Parse();
    // checking for headers, if there is get list of headers.

    if (this.hasHeader) {
      List<String> headerList = this.parsedData.getSecond();
      // now that we have header, we can figure out which column we will need to be searching
      // check for if we are looking for an column integer or a column string

      if (this.columnIDString != null) {
        for (int index = 0; index < headerList.size(); index++) {
          if (headerList.get(index).equalsIgnoreCase(this.columnIDString)) {
            columnIndex = index;
          }
        }
      }
    }
    // now that we have column index, we want to loop through parsed data, if we do in fact have a
    // column ID,
    // we can look for the string in that position
    // initalize return list of pared rows
    List<List<String>> searchResults = new ArrayList<>();
    for (List<String> row : this.parsedData.getFirst()) {
      // this is the case in which we know which column to look at
      if (!searchAll) {
        // check columnindex slot, check if isEquals to searchTarget
        if (row.get(columnIndex).toLowerCase().contains(this.searchTarget.toLowerCase())) {
          searchResults.add(row);
        }
      } else {
        // case for in which we need to search every column
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
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
