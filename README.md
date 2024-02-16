> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details

For this project, we started with a CSVParser and searcher from the last sprint. The biggest noticable difference is that previously, we were parsing **within** the search method, and now we have moved the parsing to the Utils class and pass in the parsed data along with the headerlist in a pair.
# Design Choices

we have four different handlers for each command: view, search, load, and census(endpoint is "broadband" when making census api requests). Each of these work properly. 

# Errors/Bugs

There are no known errors or bugs currently. 

# Tests

We have tested the load, search and census handlers, as well as unit testing the methods we made within these handlers. We have extensively tested the view handler/command through making numerous direct api calls. 

# How to

In order to view or search a CSV, you must first call load by using the endpoint "loadcsv," and provide a "filePath" argument with the direct path to the file, and a "hasHeader" argument with true or false depending on if the file has a header. To view, just call "viewcsv" as an endpoint with no arguments, and to search, call "searchcsv" as the endpoint with a "searchTarget" argument representing what you'd like to search for, a "columnID" (optional, if not provided will search every column)argument for which column number or header name you'd like to search, and a "hasHeader" argument with true or false depending on if the file has a header. To search a state and county for their broadband data, simply use the "broadband" endpoint with arguments for the state("state") and county("county") you'd like to get data on. 
