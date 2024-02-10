package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.List;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws IOException, FactoryFailureException {
    new Main(args).run();
  }

  private Main(String[] args) throws IOException, FactoryFailureException {
    try {
      Boolean hasHeaders;
      if (args.length > 4) {
        System.out.println("Too many arugments. This Program takes between 3 and 4 arguments.");
      } else if (args.length < 3) {
        System.out.println("Too few arguments. Excepts two or three");
      } else {
        if (args.length == 3) {
          hasHeaders = args[2].equals("yes");

          Util utility = new Util(args[0], args[1], null, hasHeaders);
          if (utility.getResults().size() == 0) {
            System.out.println("No Results Found");
          } else {
            for (List<String> ele : utility.getResults()) {
              System.out.println(ele + "\n");
            }
            // System.out.println(utility.getResults());

          }
          // System.out.println(utility.getResults());
        } else {
          hasHeaders = args[3].equals("yes");

          try {
            int colId = Integer.parseInt(args[2]);
            Util utility = new Util(args[0], args[1], colId, hasHeaders);
            if (utility.getResults().size() == 0) {
              System.out.println("No Results Found");
            } else {
              for (List<String> ele : utility.getResults()) {
                System.out.println(ele + "\n");
              }
            }
          } catch (NumberFormatException e) {
            Util utility = new Util(args[0], args[1], args[2], hasHeaders);
            if (utility.getResults().size() == 0) {
              System.out.println("No Results Found");
            } else {
              for (List<String> ele : utility.getResults()) {
                System.out.println(ele + "\n");
              }
            }
          }
        }
      }
    } catch (IOException e) {
      System.out.println("File not found. Please double check file name and try again.");
    }
  }

  private void run() throws IOException, FactoryFailureException {
    // dear student: you can remove this. you can remove anything. you're in cs32. you're free!

    //    System.out.println(
    //        "Your horoscope for this project:\n"
    //            + "Entrust in the Strategy pattern, and it shall give thee the sovereignty to "
    //            + "decide and the dexterity to change direction in the realm of thy code.");
  }
}
