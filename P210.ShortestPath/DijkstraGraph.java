// === CS400 File Header Information ===
// Name: Soham Mukherjee
// Email: smukherjee39@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // if graph does not contain either node, then throw an exception
        if(!this.containsNode(start) || !this.containsNode(end)) {
            throw new NoSuchElementException("one or both nodes do not exist in graph or no path exists between them");
        }
        PriorityQueue<SearchNode> pq = new PriorityQueue<>(); // useful for tracking edges
        PlaceholderMap<NodeType, Double> map = new PlaceholderMap<>(); // useful for tracking visited nodes
        pq.add(new SearchNode(this.nodes.get(start), 0.0, null)); // initialize queue with original start node      
        while(!pq.isEmpty()) {
            SearchNode currentNode = pq.poll(); // remove minimum weighted edge
            if(!map.containsKey(currentNode.node.data)) {
                map.put(currentNode.node.data, currentNode.cost); // if destination node hasn't been visited, mark as visited
                if(currentNode.node.data.equals(end)) {
                    return currentNode; // if we have reached end node, return
                }
                for(Edge edge : currentNode.node.edgesLeaving) {
                    pq.add(new SearchNode(edge.successor, edge.data.doubleValue() + currentNode.cost, currentNode)); // extend queue with all unvisited neighbors of current node
                }
            }
        }
        throw new NoSuchElementException("no nodes found between start and end"); // if the loop reaches the end, then no existing edge was found
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        SearchNode returnedNode = computeShortestPath(start, end); // get the final SearchNode
        List<NodeType> path = new LinkedList<>(); // create a LinkedList to store the traversal path
        
        SearchNode currentNode = returnedNode;
        // trace the path backwards, while adding nodes encountered along the traversal
        while (currentNode != null) {
            path.add(0, currentNode.node.data);
            currentNode = currentNode.predecessor;
        }
        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        SearchNode returnedNode = computeShortestPath(start, end);
        return returnedNode.cost; // return shortest path from start to end (already accounted for in Searchnode.cost)
    }

    // /**
    //  * Tests the functionality of the DijkstraGraph using the graph and an example from lecture (checks two different paths).
    //  */
    // @Test
    // public void test1() {
    //     DijkstraGraph graph = new DijkstraGraph<>();
    //     List<String> l = new ArrayList<>();
    //     l.add("A E 15");
    //     l.add("A B 4");
    //     l.add("A C 2");
    //     l.add("B E 10");
    //     l.add("B D 1");
    //     l.add("C D 5");
    //     l.add("D E 3");
    //     l.add("D F 0");
    //     l.add("F D 2");
    //     l.add("F H 4");
    //     l.add("G H 4");
    //     for (String s : l) {
    //         String[] currentEdge = s.split(" ");
    //         String startNode = currentEdge[0];
    //         String endNode = currentEdge[1];
    //         int weight = Integer.parseInt(currentEdge[2]);
    //         if(!graph.containsNode(startNode)) {
    //             graph.insertNode(startNode);
    //         }
    //         if(!graph.containsNode(endNode)) {
    //             graph.insertNode(endNode);
    //         }
    //         if(!graph.containsEdge(startNode, endNode)) {
    //             graph.insertEdge(startNode, endNode, weight);
    //         }
    //     }
    //     List<String> shortest_path_check = new ArrayList<>();
    //     shortest_path_check.add("A");
    //     shortest_path_check.add("B");
    //     shortest_path_check.add("D");
    //     shortest_path_check.add("E");
    //     Assertions.assertEquals(shortest_path_check, graph.shortestPathData("A", "E"), "Shortest path from A to E is incorrect.");
    //     Assertions.assertEquals(8.0, graph.shortestPathCost("A", "E"), "Shortest path cost from A to E should be 8.0.");
    //     List<String> shortest_path_check2 = new ArrayList<>();
    //     shortest_path_check2.add("A");
    //     shortest_path_check2.add("B");
    //     shortest_path_check2.add("D");
    //     shortest_path_check2.add("F");
    //     Assertions.assertEquals(shortest_path_check2, graph.shortestPathData("A", "F"), "Shortest path from A to F is incorrect.");
    //     Assertions.assertEquals(5.0, graph.shortestPathCost("A", "F"), "Shortest path cost from A to F should be 5.0.");
    // }

    // /**
    //  * Tests the functionality of the DijkstraGraph using the same graph but with an example not from lecture (checks two different paths).
    //  */
    // @Test
    // public void test2() {
    //     DijkstraGraph graph = new DijkstraGraph<>();
    //     List<String> l = new ArrayList<>();
    //     l.add("A E 15");
    //     l.add("A B 4");
    //     l.add("A C 2");
    //     l.add("B E 10");
    //     l.add("B D 1");
    //     l.add("C D 5");
    //     l.add("D E 3");
    //     l.add("D F 0");
    //     l.add("F D 2");
    //     l.add("F H 4");
    //     l.add("G H 4");
    //     for (String s : l) {
    //         String[] currentEdge = s.split(" ");
    //         String startNode = currentEdge[0];
    //         String endNode = currentEdge[1];
    //         int weight = Integer.parseInt(currentEdge[2]);
    //         if(!graph.containsNode(startNode)) {
    //             graph.insertNode(startNode);
    //         }
    //         if(!graph.containsNode(endNode)) {
    //             graph.insertNode(endNode);
    //         }
    //         if(!graph.containsEdge(startNode, endNode)) {
    //             graph.insertEdge(startNode, endNode, weight);
    //         }
    //     }
    //     List<String> shortest_path_check = new ArrayList<>();
    //     shortest_path_check.add("B");
    //     shortest_path_check.add("D");
    //     shortest_path_check.add("F");
    //     Assertions.assertEquals(shortest_path_check, graph.shortestPathData("B", "F"), "Shortest path from B to F is incorrect.");
    //     Assertions.assertEquals(1.0, graph.shortestPathCost("B", "F"), "Shortest path cost from B to F should be 1.0.");
    //     List<String> shortest_path_check2 = new ArrayList<>();
    //     shortest_path_check2.add("G");
    //     shortest_path_check2.add("H");
    //     Assertions.assertEquals(shortest_path_check2, graph.shortestPathData("G", "H"), "Shortest path from G to H is incorrect.");
    //     Assertions.assertEquals(4.0, graph.shortestPathCost("G", "H"), "Shortest path cost from G to H should be 4.0.");
    // }

    // /**
    //  * Tests the exceptions of the DijkstraGraph using the same graph but with nonexistent nodes and paths (checks two different cases).
    //  */
    // @Test
    // public void test3() {
    //     DijkstraGraph graph = new DijkstraGraph<>();
    //     List<String> l = new ArrayList<>();
    //     l.add("A E 15");
    //     l.add("A B 4");
    //     l.add("A C 2");
    //     l.add("B E 10");
    //     l.add("B D 1");
    //     l.add("C D 5");
    //     l.add("D E 3");
    //     l.add("D F 0");
    //     l.add("F D 2");
    //     l.add("F H 4");
    //     l.add("G H 4");
    //     for (String s : l) {
    //         String[] currentEdge = s.split(" ");
    //         String startNode = currentEdge[0];
    //         String endNode = currentEdge[1];
    //         int weight = Integer.parseInt(currentEdge[2]);
    //         if(!graph.containsNode(startNode)) {
    //             graph.insertNode(startNode);
    //         }
    //         if(!graph.containsNode(endNode)) {
    //             graph.insertNode(endNode);
    //         }
    //         if(!graph.containsEdge(startNode, endNode)) {
    //             graph.insertEdge(startNode, endNode, weight);
    //         }
    //     }
    //     try {
    //         graph.shortestPathData("B", "I");
    //         Assertions.assertTrue(false, "Exception expected here since I is not a node.");
    //     } catch (NoSuchElementException e) {
    //         System.out.println(e);
    //     }
    //     try {
    //         graph.shortestPathCost("B", "I");
    //         Assertions.assertTrue(false, "Exception expected here since I is not a node.");
    //     } catch (NoSuchElementException e) {
    //         System.out.println(e);
    //     }
    //     try {
    //         graph.shortestPathData("D", "G");
    //         Assertions.assertTrue(false, "Exception expected here since there is no edge from D to G.");
    //     } catch (NoSuchElementException e) {
    //         System.out.println(e);
    //     }
    //     try {
    //         graph.shortestPathCost("D", "G");
    //         Assertions.assertTrue(false, "Exception expected here since there is no edge from D to G.");
    //     } catch (NoSuchElementException e) {
    //         System.out.println(e);
    //     }
    // }

    public static void main(String[] args) {
        DijkstraGraph graph = new DijkstraGraph<>();
        List<String> l = new ArrayList<>();
        l.add("A E 15");
        l.add("A B 4");
        l.add("A C 2");
        l.add("B E 10");
        l.add("B D 1");
        l.add("C D 5");
        l.add("D E 3");
        l.add("D F 0");
        l.add("F D 2");
        l.add("F H 4");
        l.add("G H 4");
        for (String s : l) {
            String[] currentEdge = s.split(" ");
            String startNode = currentEdge[0];
            String endNode = currentEdge[1];
            int weight = Integer.parseInt(currentEdge[2]);
            if(!graph.containsNode(startNode)) {
                graph.insertNode(startNode);
            }
            if(!graph.containsNode(endNode)) {
                graph.insertNode(endNode);
            }
            if(!graph.containsEdge(startNode, endNode)) {
                System.out.println("inserting " + startNode + " to " + endNode + " with cost " + weight);
                graph.insertEdge(startNode, endNode, weight);
            }
        }
        List<String> path = graph.shortestPathData("A", "H");
        for(String s : path) {
            System.out.println(s);
        }
        System.out.println(graph.shortestPathCost("A", "H"));
    }
}