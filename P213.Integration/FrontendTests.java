import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FrontendTests {

  // Test 1: Verify all prompt HTML methods generate correct input elements
  @Test
  public void roleTest1() {
    Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);

    // Test shortest path prompt
    String shortestPrompt = frontend.generateShortestPathPromptHTML();
    Assertions.assertTrue(shortestPrompt.contains("id='start'"),
        "Shortest path prompt missing start input");
    Assertions.assertTrue(shortestPrompt.contains("id='end'"),
        "Shortest path prompt missing end input");
    Assertions.assertTrue(shortestPrompt.contains("Find Shortest Path"),
        "Shortest path prompt missing button");

    // Test longest path prompt (added to cover missing method)
    String longestPrompt = frontend.generateLongestLocationListFromPromptHTML();
    Assertions.assertTrue(longestPrompt.contains("id='from'"),
        "Longest path prompt missing 'from' input");
    Assertions.assertTrue(longestPrompt.contains("Longest Location List From"),
        "Longest path prompt missing button");
  }

  // Test shortest path response with placeholder backend behavior
  @Test
  public void roleTest2() {
    Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);

    // Graph_Placeholder's path: ["Union South", "Computer Sciences...", "Weeks Hall..."]
    String html = frontend.generateShortestPathResponseHTML("Union South",
        "Weeks Hall for Geological Sciences");

    Assertions.assertTrue(html.contains("Union South"), "Should list Union South");
    Assertions.assertTrue(html.contains("seconds"), "Should include total time");
    Assertions.assertTrue(html.contains("<ol>"), "Should have ordered list");
  }

  // Test longest path response handling exceptions
  @Test
  public void roleTest3() {
    Backend_Placeholder backend = new Backend_Placeholder(new Graph_Placeholder());
    Frontend frontend = new Frontend(backend);

    // Test invalid start location
    String html = frontend.generateLongestLocationListFromResponseHTML("Memorial Union");
    Assertions.assertTrue(html.contains("Memorial Union"), "Should contains Memorial Union");
  }
}
