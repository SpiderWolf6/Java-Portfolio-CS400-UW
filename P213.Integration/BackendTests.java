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

    /**
     * Tests the functionality of the generateShortestPathResponseHTML using DijkstraGraph, Backend, and Frontend
     * Tests two different valid cases and checks if the expected list of locations and total travel time is returned
     */
    @Test
    public void integrationTest1() {
        // without using any placeholders, instantiante a graph instance, backend instance (using the graph), and frontend instance (using the backend)
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);
        try {
            backend.loadGraphData("campus.dot"); // read data from file
        } catch (IOException e) {
            System.out.println(e); // in case of an exception being thrown, which is not expected
        }
        // check if the actual response matches the expected response
        String response = frontend.generateShortestPathResponseHTML("Memorial Union", "Science Hall");
        Assertions.assertEquals("<p>Shortest path from Memorial Union to Science Hall:</p><ol><li>Memorial Union</li><li>Science Hall</li></ol><p>Total travel time: 105.8 seconds</p>", 
                                response, 
                                "Shortest path response HTML does not match expected result from Memorial Union to Science Hall."
                                );
        
        // check if the actual response matches the expected response
        response = frontend.generateShortestPathResponseHTML("Union South", "X01");
        Assertions.assertEquals("<p>Shortest path from Union South to X01:</p><ol><li>Union South</li><li>Computer Sciences and Statistics</li><li>Meiklejohn House</li><li>Noland Hall</li><li>Grand Central</li><li>X01</li></ol><p>Total travel time: 721.1 seconds</p>", 
                                response, 
                                "Shortest path response HTML does not match expected result from Union South to X01."
                                );
    }

    /**
     * Tests the functionality of the generateLongestLocationListFromResponseHTML using DijkstraGraph, Backend, and Frontend
     * Tests two different valid cases and checks if the expected list of locations and total number of locations is returned
     */
    @Test
    public void integrationTest2() {
        // without using any placeholders, instantiante a graph instance, backend instance (using the graph), and frontend instance (using the backend)
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);
        try {
            backend.loadGraphData("campus.dot"); // read data from file
        } catch (IOException e) {
            System.out.println(e); // in case of an exception being thrown, which is not expected
        }
        // check if the actual response matches the expected response
        String response = frontend.generateLongestLocationListFromResponseHTML("Memorial Union");
        Assertions.assertEquals("<p>Longest path starting from Memorial Union:</p><ol><li>Memorial Union</li><li>Radio Hall</li><li>Education Building</li><li>South Hall</li><li>Law Building</li><li>X01</li><li>Luther Memorial Church</li><li>Noland Hall</li><li>Meiklejohn House</li><li>Computer Sciences and Statistics</li><li>Rust-Schreiner Hall</li><li>Humbucker Apartments</li><li>Harlow Primate Laboratory</li><li>Budget Bicycle Center - New Bicycles</li><li>Jenson Auto</li><li>Phi Kappa Theta</li><li>Hong Kong Cafe</li><li>Davis Duehr Dean Eye Care</li><li>Meriter Laboratories</li><li>UW Health 20 South Park Street Clinic North Building</li><li>Park Regent Apartments</li><li>21 N. Park St. and Lot 29 Ramp</li><li>Smith Residence Hall</li></ol><p>Total locations: 23</p>", 
                                response, 
                                "Longest path response HTML does not match expected result from Memorial Union."
                                );
        
        // check if the actual response matches the expected response
        response = frontend.generateLongestLocationListFromResponseHTML("Union South");
        Assertions.assertEquals("<p>Longest path starting from Union South:</p><ol><li>Union South</li><li>Atmospheric, Oceanic and Space Sciences</li><li>Rust-Schreiner Hall</li><li>Humbucker Apartments</li><li>Harlow Primate Laboratory</li><li>Budget Bicycle Center - New Bicycles</li><li>Wingstop</li><li>Jenson Auto</li><li>Phi Kappa Theta</li><li>Hong Kong Cafe</li><li>Davis Duehr Dean Eye Care</li><li>Meriter Laboratories</li><li>UW Health 20 South Park Street Clinic North Building</li><li>Park Regent Apartments</li><li>21 N. Park St. and Lot 29 Ramp</li><li>Environmental Health and Safety Building</li><li>Smith Residence Hall</li></ol><p>Total locations: 17</p>", 
                                response, 
                                "Longest path response HTML does not match expected result from Union South."
                                );
    }

    /**
     * Tests the functionality of the generateShortestPathResponseHTML using DijkstraGraph, Backend, and Frontend
     * Tests two different invalid cases and checks if the proper error is thrown and dealt with
     */
    @Test
    public void integrationTest3() {
        // without using any placeholders, instantiante a graph instance, backend instance (using the graph), and frontend instance (using the backend)
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);
        try {
            backend.loadGraphData("campus.dot"); // read data from file
        } catch (IOException e) {
            System.out.println(e); // in case of an exception being thrown, which is not expected
        }
        // check if the actual response matches the expected response
        String response = frontend.generateShortestPathResponseHTML("abc", "Science Hall");
        Assertions.assertEquals("<p>Error: one or both nodes do not exist in graph or no path exists between them</p>", response, "Expected to return an error statement listing illegal arguments were passed.");
        
        // check if the actual response matches the expected response
        response = frontend.generateShortestPathResponseHTML("abc", "def");
        Assertions.assertEquals("<p>Error: one or both nodes do not exist in graph or no path exists between them</p>", response, "Expected to return an error statement listing illegal arguments were passed.");
    }

    /**
     * Tests the functionality of the generateLongestLocationListFromResponseHTML using DijkstraGraph, Backend, and Frontend
     * Tests two different invalid cases and checks if the proper error is thrown and dealt with
     */
    @Test
    public void integrationTest4() {
        // without using any placeholders, instantiante a graph instance, backend instance (using the graph), and frontend instance (using the backend)
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);
        try {
            backend.loadGraphData("campus.dot"); // read data from file
        } catch (IOException e) {
            System.out.println(e); // in case of an exception being thrown, which is not expected
        }
        // check if the actual response matches the expected response
        String response = frontend.generateLongestLocationListFromResponseHTML("abc");
        Assertions.assertEquals("<p>Error: Starting node does not exist in graph. or no reachable locations</p>", response, "Expected to return an error statement listing illegal arguments were passed.");
        
        // check if the actual response matches the expected response
        response = frontend.generateLongestLocationListFromResponseHTML(" ");
        Assertions.assertEquals("<p>Error: Starting node does not exist in graph. or no reachable locations</p>", response, "Expected to return an error statement listing illegal arguments were passed.");
    }
}
