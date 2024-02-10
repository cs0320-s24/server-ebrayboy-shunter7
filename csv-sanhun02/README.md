# Project Details
This program contains parse functionality that can be used to parse CSV files into an object of the user's choice. It also contains search functionality for searching a desired CSV file and search target. The implementation is described below.
# Design Choices
For this project, I decided to have an overhead Util class to call directly from the main method using command line arguments. This only has one method to operate searching, which is called getResults(). This method makes a call to our search class, which returns the rows in which our search target is found. The search class also makes a call to our CSVParser class. The CSV parser class can operate without search, and can also be used by other programmers who wish to parse their data into objects of their choice. All they need to do is define a create method that tells it how it would like the objects stored. The CSV parser skips over any rows that are unfilled, only parsing through rows that have all columns filled. The other classes, Astro and Builder were used for testing as described below 
# Errors/Bugs
Errors are handled in the Main class, which has a try catch for when files are not found. It also prints a message when the user gives too few or too many arguments. I have not found any other errors or bugs in my code, and have tried to test every error. When a file cannot be found, this is printed: "File not found. Please double check file name and try again."
# Tests
I wrote JUnit tests for the parse method, as well as JUnit tests for the search method. When looking at the coverage, the missed instructions coverage is at 60% and the missed branches coverage is 52%. I tested all the CSV files given, and they all return elements as expected. 
# How to...
To run the code, here are the steps. 
1. Open up the terminal
2. cd into the main class that is **Within** our edu.brown.cs.student.main package
3. Compile all the files. This can be done by typing: "javac Main.java FactoryFailureException.java CreatorFromRow.java CSVParser.java Search.java Util.java" all in one line
4. Navigate back to the java directory that is overhead of our edu.brown.cs.student.main package
5. Type in "java" followed by the path from the directory to the inner-most main class, followed by the arguments to run the program as follows: 1. a string that represents the absolute                                                          filepath to the file you'd like to search, 2. the target word you'd like to search for,
                                                         3. (optional argument) an identifier for the column you'd like to search (this can either be a string for the header column,
                                                         or an integer that represents the column number you'd like to search), 4. a string "yes" if the file has headers (if anything                                                             else, will assume there are not headers)
6. Enjoy your results! 
