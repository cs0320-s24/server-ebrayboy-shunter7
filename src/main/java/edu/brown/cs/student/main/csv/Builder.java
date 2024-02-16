package edu.brown.cs.student.main.csv;

import java.util.List;

/**
 * Represents a Builder class that implements the CreatorFromRow interface to create Astro objects.
 */
public class Builder implements CreatorFromRow<Astro> {

  /**
   * Creates an Astro object from a given row of data.
   *
   * @param row The row of data to create the Astro object.
   * @return Astro object created from the row.
   * @throws FactoryFailureException if there is a failure during factory operation.
   */
  @Override
  public Astro create(List<String> row) throws FactoryFailureException {
    return new Astro(row.get(1), row.get(0));
  }
}
