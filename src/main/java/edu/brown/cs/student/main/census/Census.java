package edu.brown.cs.student.main.census;

import spark.Request;
import spark.Response;
import spark.Route;

public class Census implements Route {
  String county;
  String state;

  public Census() {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return null;
  }
}
