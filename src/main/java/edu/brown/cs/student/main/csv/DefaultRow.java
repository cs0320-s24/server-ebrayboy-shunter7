package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a DefaultRow class that implements the CreatorFromRow interface to create a List of
 * Strings.
 */
public class DefaultRow implements CreatorFromRow<List<String>> {
  public String row;

  /**
   * Creates a List of Strings from the given row of data.
   *
   * @param row The row of data to create the List of Strings.
   * @return List of Strings created from the row.
   * @throws FactoryFailureException if there is a failure during factory operation.
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return new ArrayList<>(row);
  }
}
