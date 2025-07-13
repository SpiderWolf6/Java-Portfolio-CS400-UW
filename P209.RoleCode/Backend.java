import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {
    private GraphADT<String, Double> graph; // private variable for storing graph object for this class

    /**
     * Constructor for the Backend class
     * @param graph an instance of the GraphADT interface
     */
    public Backend(GraphADT<String,Double> graph) {
        this.graph = graph;
    }

    /**
     * Loads the data from a given filename into the graph object
     * @param filename a String representing the filepath of the file to be loaded from
     * @throws IOException if the given file path is not found
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
        List<String> nodes = graph.getAllNodes(); // get current nodes in the graph
        List<String> nodesToRemove = new ArrayList<>(nodes);
        if(!nodes.isEmpty()) {
            for(String node : nodesToRemove) {
                graph.removeNode(node); // clear the current graph
            }
        }
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String edgePattern = "\"(.*?)\" -> \"(.*?)\" \\[seconds=(\\d+\\.\\d+)]"; // regex edge pattern using groups in the graph (finds node -> node with time in seconds) 
        Pattern pattern = Pattern.compile(edgePattern);

        while ((line = reader.readLine()) != null) {
            // ignore lines that are empty or contain metadata like "digraph campus"
            if (line.trim().isEmpty() || line.contains("digraph") || line.contains("{") || line.contains("}")) {
                continue;
            }
            // match edge definitions using the regular expression
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String startNode = matcher.group(1).trim();
                String endNode = matcher.group(2).trim();
                double weight = Double.parseDouble(matcher.group(3));

                // insert the nodes if they are not already in the graph
                if (!graph.containsNode(startNode)) {
                    graph.insertNode(startNode);
                }
                if (!graph.containsNode(endNode)) {
                    graph.insertNode(endNode);
                }
                // insert the edge between the nodes with the given weight
                graph.insertEdge(startNode, endNode, weight);
            }
        }
        reader.close();
    }

    /**
     * Returns all present nodes in the graph object
     * @return a List<String> containing all nodes
     */
    @Override
    public List<String> getListOfAllLocations() {
        return graph.getAllNodes();
   }

    /**
     * Returns the nodes in the path from a start to an end node
     * @param startLocation,endLocation two Strings representing start/end nodes
     * @return a List<String> containing nodes on this shortest path
     */
    @Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
        try {
            return graph.shortestPathData(startLocation, endLocation);
        } catch (Exception e) {
            System.out.println(e); // if no path found, then handle error
        }
        return new ArrayList<>(); // return empty list in case of no path
    }

    /**
     * Returns the times between nodes on the shortest path from start to end node
     * @param startLocation,endLocation two Strings representing start/end nodes
     * @return a List<Double> containing the times on this shortest path
     */
    @Override
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
        List<Double> times = new ArrayList<>(); // store times on the shortest path
        List<String> path_nodes = this.findLocationsOnShortestPath(startLocation, endLocation); // nodes on the shortest path
        for(int i = 0; i<path_nodes.size()-1; i++) {
            try {
                times.add(graph.shortestPathCost(path_nodes.get(i), path_nodes.get(i+1))); // add each edge weight in loop
            } catch (Exception e) {
                System.out.println(e); // if no path found, then handle error
            }
        }
        return times; // return empty list in case of no path
    }

    /**
     * Returns the farthest path starting from any given node
     * @param startLocation a String representing the start node
     * @return a List<String> containing the nodes on this longest path
     * @throws NoSuchElementException if graph does not contain given start node
     */
    @Override
    public List<String> getLongestLocationListFrom(String startLocation) throws NoSuchElementException {
        if(graph.containsNode(startLocation)) {
            List<String> nodes = graph.getAllNodes(); // get all nodes in graph
            double maxCost = 0.0;
            String furthestNode = null;
            // loop through to find the farthest node reachable from this starting node
            for(String node : nodes) {
                double current_cost = graph.shortestPathCost(startLocation, node);
                if(current_cost > maxCost) {
                    maxCost = current_cost;
                    furthestNode = node; // continually store new max cost and furthest node
                }
            }
            return graph.shortestPathData(startLocation, furthestNode); // return nodes list on this longest path, from start to end
        }
        else {
            throw new NoSuchElementException("Starting node does not exist in graph.");
        }
    }
}