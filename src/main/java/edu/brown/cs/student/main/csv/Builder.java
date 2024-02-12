package edu.brown.cs.student.main.csv;

import java.util.List;

public class Builder implements CreatorFromRow<Astro> {
  @Override
  public Astro create(List<String> row) throws FactoryFailureException {
    return new Astro(row.get(1), row.get(0));
  }
}
