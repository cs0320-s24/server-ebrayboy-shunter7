package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

public class DefaultRow implements CreatorFromRow<List<String>> {
  public String row;

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return new ArrayList<>(row);
  }
  ;
}
