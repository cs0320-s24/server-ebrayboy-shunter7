package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

public class DefaultRow implements CreatorFromRow<ArrayList<String>> {
  public String row;

  @Override
  public ArrayList<String> create(List<String> row) throws FactoryFailureException {
    return new ArrayList<>(row);
  }
  ;
}
