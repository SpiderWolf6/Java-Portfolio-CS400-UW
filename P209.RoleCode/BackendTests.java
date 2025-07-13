import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.NoSuchElementException;

public class BackendTests {
    
    /**
     * Tests the functionality of the getListOfAllLocations and findLocationsOnShortestPath methods (including an edge case for the second method)
     */
    @Test
    public void roleTest1() {
        // instantiate graph object and new backend for testing
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);
        List<String> node_check = new ArrayList<>(); // create checker List and fill it with expected values
        node_check.add("Union South");
        node_check.add("Computer Sciences and Statistics");
        node_check.add("Weeks Hall for Geological Sciences");
        Assertions.assertEquals(node_check, backend.getListOfAllLocations(), "Nodes are incorrect in the graph.");
        List<String> shortest_path_check = new ArrayList<>(); // create checker List and fill it with expected values
        shortest_path_check.add("Union South");
        shortest_path_check.add("Computer Sciences and Statistics");
        shortest_path_check.add("Weeks Hall for Geological Sciences");
        Assertions.assertEquals(shortest_path_check, backend.findLocationsOnShortestPath("Union South", "Weeks Hall for Geological Sciences"), "Shortest path from Union South to Weeks Hall is incorrect.");
        List<String> shortest_path_check2 = new ArrayList<>(); // create checker List and fill it with expected values
        shortest_path_check2.add("Union South");
        Assertions.assertEquals(shortest_path_check2, backend.findLocationsOnShortestPath("Union South", "Union South"), "Shortest path from Union South to Union South is itself.");
    }

    /**
     * Tests the functionality of the loadGraphData and findTimesOnShortestPath (including edge cases for both)
     */
    @Test
    public void roleTest2() {
        // instantiate graph object and new backend for testing
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            Assertions.assertTrue(false, "Exception not expected here."); // file should be read correctly
        }
        List<Double> shortest_path_times_check = new ArrayList<>(); // create checker List and fill it with expected values
        shortest_path_times_check.add(1.0);
        shortest_path_times_check.add(2.0);
        shortest_path_times_check.add(3.0);
        Assertions.assertEquals(shortest_path_times_check, backend.findTimesOnShortestPath("Union South", "Luther Memorial Church"), "Shortest path times from Union South to Luther Church is incorrect.");
        List<Double> shortest_path_times_check2 = new ArrayList<>(); // create checker List and fill it with expected values
        Assertions.assertEquals(shortest_path_times_check2, backend.findTimesOnShortestPath("Luther Memorial Church", "Luther Memorial Church"), "Shortest path time from Luther Church to Luther Church is an empty list.");
        try {
            backend.loadGraphData("wrongfile.dot"); // file does not exist so error expected
            Assertions.assertTrue(false, "Exception should be thrown here.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Tests the functionality of the getLongestLocationListFrom and (including edge cases)
     */
    @Test
    public void roleTest3() {
        // instantiate graph object and new backend for testing
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            Assertions.assertTrue(false, "Exception not expected here.");
        }
        List<String> longest_path_check = new ArrayList<>(); // create checker List and fill it with expected values
        longest_path_check.add("Computer Sciences and Statistics");
        longest_path_check.add("Weeks Hall for Geological Sciences");
        longest_path_check.add("Memorial Union");
        Assertions.assertEquals(longest_path_check, backend.getLongestLocationListFrom("Computer Sciences and Statistics"), "Longest path from CS is incorrect.");
        try {
            backend.getLongestLocationListFrom("The Crossing"); // node doesn't exist so error expected
            Assertions.assertTrue(false, "Starting node should not exist in the graph.");
        } catch (NoSuchElementException e) {
            System.out.print(e);
        }
    }
}