package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param <T> Parser Class, with T representing an Arbitrary Object where the user would like the
 *     data stored.
 */
public class CSVParser<T> {

  Reader reader;
  CreatorFromRow<T> creator;
  Boolean hasHeaders;
  public List<String> headerList;

  /**
   * @param reader A Reader that contains the file information that is inputted
   * @param creator An Object, in which the data is stored in, defined by whoever is using the
   *     method
   * @param hasHeaders A boolean representing weather or not the inputted file has headers
   */
  public CSVParser(Reader reader, CreatorFromRow<T> creator, Boolean hasHeaders) {
    this.reader = reader;
    this.creator = creator;
    this.hasHeaders = hasHeaders;
  }
  ;

  /**
   * @return A list of objects determined by the user that stores all of the data
   * @throws IOException
   * @throws FactoryFailureException
   */
  public List<T> Parse() throws IOException, FactoryFailureException {

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
  ;
}
