package edu.brown.cs.student.main.csv;

import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type.
 *
 * <p>Your parser class constructor should take a second parameter of this generic interface type.
 */
public interface CreatorFromRow<T> {
  /**
   * Creates an object of type T from a given row of data.
   *
   * @param row The row of data to create the object.
   * @return Object of type T created from the row.
   * @throws FactoryFailureException if there is a failure during factory operation.
   */
  T create(List<String> row) throws FactoryFailureException;
}
