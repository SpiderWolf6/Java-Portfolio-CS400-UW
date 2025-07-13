import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface {
  private BackendInterface backend;

  /**
   * Implementing classes should support the constructor below.
   * 
   * @param backend is used for shortest path computations
   */
  public Frontend(BackendInterface backend) {
    this.backend = backend;
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page. This HTML
   * output should include: - a text input field with the id="start", for the start location - a
   * text input field with the id="end", for the destination - a button labelled "Find Shortest
   * Path" to request this computation Ensure that these text fields are clearly labelled, so that
   * the user can understand how to use them.
   * 
   * @return an HTML string that contains input controls that the user can make use of to request a
   *         shortest path computation
   */
  @Override
  public String generateShortestPathPromptHTML() {
    return "<div>" + "<label for='start'>Start Location:</label>"
        + "<input type='text' id='start' name='start'><br>"
        + "<label for='end'>End Location:</label>" + "<input type='text' id='end' name='end'><br>"
        + "<button onclick='requestShortestPath()'>Find Shortest Path</button>" + "</div>";
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page. This HTML
   * output should include: - a paragraph (p) that describes the path's start and end locations - an
   * ordered list (ol) of locations along that shortest path - a paragraph (p) that includes the
   * total travel time along this path Or if there is no such path, the HTML returned should instead
   * indicate the kind of problem encountered.
   * 
   * @param start is the starting location to find a shortest path from
   * @param end   is the destination that this shortest path should end at
   * @return an HTML string that describes the shortest path between these two locations
   */
  @Override
  public String generateShortestPathResponseHTML(String start, String end) {
    if("".equals(start) || "".equals(end)) {
      return "<p>One or both of the start/end locations were left empty.</p>";
    }
    try {
      List<String> path = backend.findLocationsOnShortestPath(start, end);
      List<Double> times = backend.findTimesOnShortestPath(start, end);

      if (path.isEmpty()) {
        return "<p>No path exists between " + start + " and " + end + ".</p>";
      }

      // double totalTime = times.get(times.size() - 1); // Based on placeholder's incremental values
      double totalTime = 0.0;
      for(double t : times) {
        totalTime += t;
      }
      StringBuilder html = new StringBuilder();
      html.append("<p>Shortest path from ").append(start).append(" to ").append(end)
          .append(":</p>");
      html.append("<ol>");
      for (String location : path) {
        html.append("<li>").append(location).append("</li>");
      }
      html.append("</ol>");
      html.append("<p>Total travel time: ").append(totalTime).append(" seconds</p>");
      return html.toString();
    } catch (Exception e) {
      return "<p>Error: " + e.getMessage() + "</p>";
    }
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page. This HTML
   * output should include: - a text input field with the id="from", for the start location - a
   * button labelled "Longest Location List From" to submit this request Ensure that this text field
   * is clearly labelled, so that the user can understand how to use it.
   * 
   * @return an HTML string that contains input controls that the user can make use of to request a
   *         longest location list calculation
   */
  @Override
  public String generateLongestLocationListFromPromptHTML() {
    return "<div>" + "<label for='from'>Start Location:</label>"
        + "<input type='text' id='from' name='from'>"
        + "<button onclick='requestLongestList()'>Longest Location List From</button>" + "</div>";
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page. This HTML
   * output should include: - a paragraph (p) that describes the path's start and end locations - an
   * ordered list (ol) of locations along that shortest path - a paragraph (p) that includes the
   * total number of locations on path Or if no such path can be found, the HTML returned should
   * instead indicate the kind of problem encountered.
   * 
   * @param start is the starting location to find the longest list from
   * @return an HTML string that describes the longest list of locations along a shortest path
   *         starting from the specified location
   */
  @Override
  public String generateLongestLocationListFromResponseHTML(String start) {
    if("".equals(start)) {
      return "<p>No start location was provided.</p>";
    }
    try {
      List<String> path = backend.getLongestLocationListFrom(start);

      StringBuilder html = new StringBuilder();
      html.append("<p>Longest path starting from ").append(start).append(":</p>");
      html.append("<ol>");
      for (String location : path) {
        html.append("<li>").append(location).append("</li>");
      }
      html.append("</ol>");
      html.append("<p>Total locations: ").append(path.size()).append("</p>");
      return html.toString();
    } catch (NoSuchElementException e) {
      return "<p>Error: " + e.getMessage() + " or no reachable locations</p>";
    }
  }
}
