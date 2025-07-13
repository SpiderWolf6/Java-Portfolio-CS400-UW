import java.util.List;

/**
 * This class implements the frontendInterface
 */
public class Frontend implements FrontendInterface {

  BackendInterface backend;

  /**
   * Contructor for this class
   * @param backend is used for shortest path computations
   */
  public Frontend(BackendInterface backend) {
    this.backend = backend;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a text input field with the id="start", for the start location
   * - a text input field with the id="end", for the destination
   * - a button labelled "Find Shortest Path" to request this computation
   * Ensure that these text fields are clearly labelled, so that the user
   * can understand how to use them.
   * @return an HTML string that contains input controls that the user can
   *         make use of to request a shortest path computation
   */
  @Override
  public String generateShortestPathPromptHTML() {
    return """
            <input type="text" id="start" placeholder="enter start location"/>\r
            <input type="text" id="end" placeholder="enter end location"/>\r
            <input type="button" value="Find Shortest Path"/>""";
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a paragraph (p) that describes the path's start and end locations
   * - an ordered list (ol) of locations along that shortest path
   * - a paragraph (p) that includes the total travel time along this path
   * Or if there is no such path, the HTML returned should instead indicate
   * the kind of problem encountered.
   * @param start is the starting location to find a shortest path from
   * @param end is the destination that this shortest path should end at
   * @return an HTML string that describes the shortest path between these
   *         two locations
   */
  @Override
  public String generateShortestPathResponseHTML(String start, String end) {
    //find all the locations and times along the path
    List<String> locationsAlongPath = backend.findLocationsOnShortestPath(start, end);
    List<Double> timesAlongPath = backend.findTimesOnShortestPath(start, end);

    //check for error
    if (locationsAlongPath.isEmpty() || timesAlongPath.isEmpty()) {
      return "<p>some problem encountered, no such path exists.</p>";
    }

    // build the string that describes the path's start and end locations
    StringBuilder DisplayStartEnd = new StringBuilder();
    DisplayStartEnd.append("<p>Shortest path from <strong>start location: ").append(start)
    .append("</strong> to <strong>end location: ").append(end).append(".</strong></p>\r");

    // build the string that lists locations along the path
    StringBuilder DisplayListOfLocations = new StringBuilder();
    DisplayListOfLocations = listBuilder(locationsAlongPath);

    // count the total travel time for path, and build the string to include this total time
    double totalTime = 0;
    for (double time : timesAlongPath) {
      totalTime += time;
    }
    StringBuilder DisplayTotalTime = new StringBuilder();
    DisplayTotalTime.append("<p>total travel time: ").append(totalTime).append(" seconds.</p>");

    // return the HTML string that describes this shorest path
    return DisplayStartEnd.toString() + DisplayListOfLocations.toString() + DisplayTotalTime.toString();
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a text input field with the id="from", for the start location
   * - a button labelled "Longest Location List From" to submit this request
   * Ensure that this text field is clearly labelled, so that the user
   * can understand how to use it.
   * @return an HTML string that contains input controls that the user can
   *         make use of to request a longest location list calculation
   */
  @Override
  public String generateLongestLocationListFromPromptHTML() {
    return """
            <input type="text" id="from" placeholder="start location"/>\r
            <input type="button" value="Longest Location List From"/>"""
        ;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a
   * larger html page.  This HTML output should include:
   * - a paragraph (p) that describes the path's start and end locations
   * - an ordered list (ol) of locations along that shortest path
   * - a paragraph (p) that includes the total number of locations on path
   * Or if no such path can be found, the HTML returned should instead
   * indicate the kind of problem encountered.
   * @param start is the starting location to find the longest list from
   * @return an HTML string that describes the longest list of locations
   *        along a shortest path starting from the specified location
   */
  @Override
  public String generateLongestLocationListFromResponseHTML(String start) {

    try {
      // get the longest location list
      List<String> locationsAlongPath = backend.getLongestLocationListFrom(start);

      // find the end location
      String end = locationsAlongPath.get(locationsAlongPath.size() - 1);

      // build the string to display path start and end locations
      StringBuilder DisplayStartEnd = new StringBuilder();
      DisplayStartEnd.append("<p>Shortest path from <strong>start location: ").append(start)
      .append("</strong> to <strong>end location: ").append(end).append(".</strong></p>\r");

      // build the string to display the list of locations along path
      StringBuilder DisplayListOfLocations = new StringBuilder();
      DisplayListOfLocations = listBuilder(locationsAlongPath);

      // build string to display the total number of locations along path
      int totalNumLocations = locationsAlongPath.size();
      StringBuilder DisplayTotalLocations = new StringBuilder();
      DisplayTotalLocations.append("<p>total locations on path: ").append(totalNumLocations).append(".</p>\r");

      // return the HTML output
      return DisplayStartEnd.toString() + DisplayListOfLocations.toString() + DisplayTotalLocations.toString();
    } catch (Exception e) {
      //return error message if there's trouble getting the path
      return "<p>some problem encountered, no such path exists.</p>";
    }
  }

  /**
   * Private helper method to get the list of locatios from a given path and
   * organize them into an ordered list (ol) in the HTML fragment
   * @param locationsAlongPath - the list of locations along the path
   * @return HTML fragment that represents the locations along the path
   */
  private StringBuilder listBuilder(List<String> locationsAlongPath) {
    // Initialize a StringBuilder to construct the ordered list
    StringBuilder DisplayListOfLocations = new StringBuilder();
    DisplayListOfLocations.append("<ol>\r");
    // Iterate over the locations and add them as list items
    for (String location: locationsAlongPath) {
      DisplayListOfLocations.append("   <li>").append(location).append("</li>\r");
    }
    DisplayListOfLocations.append("</ol>\r");

    return DisplayListOfLocations;
  }

  public static void main(String[] args) {
    Graph_Placeholder graph = new Graph_Placeholder();
    Backend_Placeholder backend = new Backend_Placeholder(graph);
    Frontend frontend = new Frontend(backend);
    System.out.println(frontend.generateLongestLocationListFromResponseHTML("abc"));
  }
}