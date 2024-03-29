package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic CSV Parser class that reads data from a given Reader, uses a provided CreatorFromRow to
 * convert each row into an arbitrary object of type T, and supports handling headers based on the
 * specified hasHeaders flag.
 *
 * @param <T> The type of object where the parsed data is stored.
 */
public class CSVParser<T> {

  Reader reader;
  CreatorFromRow<T> creator;
  Boolean hasHeaders;
  public List<String> headerList;

  /**
   * Constructs a CSVParser.
   *
   * @param reader A Reader containing the file information.
   * @param creator An object creator defining how data is stored in the target type T.
   * @param hasHeaders A boolean representing whether or not the inputted file has headers.
   */
  public CSVParser(Reader reader, CreatorFromRow<T> creator, Boolean hasHeaders) {
    this.reader = reader;
    this.creator = creator;
    this.hasHeaders = hasHeaders;
  }

  /**
   * Parses the CSV data, converting each row into objects of type T.
   *
   * @return A list of objects determined by the user that stores all of the data.
   * @throws IOException If there is an issue with I/O operations.
   * @throws FactoryFailureException If there is a failure during factory operation.
   */
  public List<T> parse() throws IOException, FactoryFailureException {

    // Parse line by line, passing into create method and then adding this to our returnList

    // creating list to store lines of the file
    List<T> returnList = new ArrayList<>();
    // creating reader to read file

    BufferedReader utilReader = new BufferedReader(this.reader);
    // initalizing the first row of file
    String row = utilReader.readLine();
    int rowLength = row.split(",").length;
    if (this.hasHeaders) {
      this.headerList = new ArrayList<>(Arrays.asList(row.split(",")));
      row = utilReader.readLine();
    }

    // looping through file
    while (row != null) {

      List<String> rowList =
          new ArrayList<>(
              Arrays.asList(row.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))")));

      if (rowList.size() == rowLength) {
        T toAdd = this.creator.create(rowList);
        returnList.add(toAdd);
      }

      // resetting the value of row to loop again and add
      row = utilReader.readLine();
    }

    return returnList;
  }
}
