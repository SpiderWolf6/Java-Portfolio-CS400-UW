import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TeamTests {
    
    // JUnit tester for checking correct functionality of Frontend HTML prompt generation methods
    @Test
    public void testFrontendPrompts() {
        // use graph placeholder instance to create backend instance, then use that backend instance to create frontend instance 
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);

        // check if generateShortestPathPromptHTML returns correctly formatted HTML user prompt
        String shortestPathPrompt = frontend.generateShortestPathPromptHTML();
        Assertions.assertTrue(shortestPathPrompt.contains("input type='text'") || shortestPathPrompt.contains("input type=\"text\""), "HTML does not contain text box for user to enter information.");
        Assertions.assertTrue(shortestPathPrompt.contains("id='start'") || shortestPathPrompt.contains("id=\"start\""), "HTML does not contain text box with id='start' for start location input.");
        Assertions.assertTrue(shortestPathPrompt.contains("id='end'") || shortestPathPrompt.contains("id=\"end\""), "HTML does not contain text box with id='end' for start location input.");
        Assertions.assertTrue(shortestPathPrompt.contains("Find Shortest Path"), "HTML does not contain a button with the expected label Find Shortest Path.");
    
        // check if generateLongestLocationListFromPromptHTML returns correctly formatted HTML user prompt
        String longestPathPrompt = frontend.generateLongestLocationListFromPromptHTML();
        Assertions.assertTrue(longestPathPrompt.contains("input type='text'") || longestPathPrompt.contains("input type=\"text\""), "HTML does not contain text box for user to enter information.");
        Assertions.assertTrue(longestPathPrompt.contains("id='from'") || longestPathPrompt.contains("id=\"from\""), "HTML does not contain text box with id='from' for start location input.");
        Assertions.assertTrue(longestPathPrompt.contains("Longest Location List From"), "HTML does not contain a button with the expected label Longest Location List From.");
    }

    // JUnit tester for checking correct functionality of Frontend HTML response generation methods
    @Test
    public void testFrontendResponses() {
        // use graph placeholder instance to create backend instance, then use that backend instance to create frontend instance 
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);

        // check if generateShortestPathResponseHTML returns correctly formatted HTML strings and contains correct information
        String shortestPathResponse = frontend.generateShortestPathResponseHTML("Union South", "Weeks Hall for Geological Sciences");
        Assertions.assertTrue(shortestPathResponse.contains("Union South"), "HTML response for shortest path from Union South to Weeks is expected to contain Union South.");
        Assertions.assertTrue(shortestPathResponse.contains("Computer Sciences and Statistics"), "HTML response for shortest path from Union South to Weeks is expected to contain Computer Sciences and Statistics.");
        Assertions.assertTrue(shortestPathResponse.contains("Weeks Hall for Geological Sciences"), "HTML response for shortest path from Union South to Weeks is expected to contain Weeks Hall for Geological Sciences.");
        Assertions.assertTrue(shortestPathResponse.contains("<p>") && shortestPathResponse.contains("</p>"), "HTML response for shortest path is expected to contain paragraph element.");
        Assertions.assertTrue(shortestPathResponse.contains("<ol>") && shortestPathResponse.contains("</ol>"), "HTML response for shortest path is expected to contain ordered list element.");
        Assertions.assertTrue(shortestPathResponse.contains("<li>") && shortestPathResponse.contains("</li>"), "HTML response for shortest path is expected to contain list items.");
        Assertions.assertTrue(shortestPathResponse.contains("3.0 seconds"), "HTML response for shortest path from Union South to Weeks should contain the time 3.0 seconds.");

        // check if generateLongestLocationListFromResponseHTML returns correctly formatted HTML strings and contains correct information
        String longestPathResponse = frontend.generateLongestLocationListFromResponseHTML("Computer Sciences and Statistics");
        Assertions.assertTrue(longestPathResponse.contains("Computer Sciences and Statistics"), "HTML response for longest path from Computer Sciences and Statistics is expected to contain Computer Sciences and Statistics.");
        Assertions.assertTrue(longestPathResponse.contains("Weeks Hall for Geological Sciences"), "HTML response for longest path from Computer Sciences and Statistics is expected to contain Weeks Hall for Geological Sciences.");
        Assertions.assertTrue(longestPathResponse.contains("<p>") && longestPathResponse.contains("</p>"), "HTML response for longest path is expected to contain paragraph element.");
        Assertions.assertTrue(longestPathResponse.contains("<ol>") && longestPathResponse.contains("</ol>"), "HTML response for longest path is expected to contain ordered list element.");
        Assertions.assertTrue(longestPathResponse.contains("<li>") && longestPathResponse.contains("</li>"), "HTML response for longest path is expected to contain list items.");
        Assertions.assertTrue(longestPathResponse.contains("2"), "HTML response for longest path from Computer Sciences and Statistics should contain the total number of 2 locations.");
    }

    // JUnit tester for checking incorrect functionality of Frontend HTML response generation methods (error handling implementation)
    @Test
    public void testFrontendErrorHandling() {
        // use graph placeholder instance to create backend instance, then use that backend instance to create frontend instance 
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);

        // check if generateShortestPathResponseHTML returns correctly formatted HTML strings when there is an error
        String shortestPathResponse = frontend.generateShortestPathResponseHTML("abc", "de");
        Assertions.assertTrue(shortestPathResponse.contains("<p>") && shortestPathResponse.contains("</p>"), "HTML response is expected to contain a paragraph element stating the error.");
        Assertions.assertTrue(!shortestPathResponse.contains("<ol>") && !shortestPathResponse.contains("</ol>"), "HTML response should not contain ordered list element.");
        Assertions.assertTrue(!shortestPathResponse.contains("<li>") && !shortestPathResponse.contains("</li>"), "HTML response should not contain list items.");

        // check if generateLongestLocationListFromResponseHTML returns correctly formatted HTML strings when there is an error
        String longestPathResponse = frontend.generateLongestLocationListFromResponseHTML("abcde");
        Assertions.assertTrue(longestPathResponse.contains("<p>") && longestPathResponse.contains("</p>"), "HTML response is expected to contain a paragraph element stating the error.");
        Assertions.assertTrue(!longestPathResponse.contains("<ol>") && !longestPathResponse.contains("</ol>"), "HTML response should not contain ordered list element.");
        Assertions.assertTrue(!longestPathResponse.contains("<li>") && !longestPathResponse.contains("</li>"), "HTML response should not contain list items.");
    }

}
