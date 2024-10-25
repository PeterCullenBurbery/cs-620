package learn_mazes_006;

import java.util.*;

public class GridGraphVisualizerAdjacencyListWithNode {

    private static final char EDGE = 'E';
    private static final char NODE = '.';
    private static final char WALL = '#';
    private static final char START = 'S';  // Character for start node
    private static final char GOAL = 'G';   // Character for goal node
    private int rows;
    private int cols;
    private List<Node> nodes;
    private Map<Node, List<Node>> adjacencyList; // Adjacency list with Node objects
    private Set<String> traversedEdges; // Store the traversed edges
    private Set<Node> visited; // Track visited nodes
    private Random rand; // Randomizer for shuffling neighbors
    private Node startNode;
    private Node goalNode;
    


    // Constructor to initialize the grid graph
    public GridGraphVisualizerAdjacencyListWithNode(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.nodes = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.traversedEdges = new HashSet<>();
        this.visited = new HashSet<>();
        this.rand = new Random(); // Initialize the random object

        // Create nodes and set up connections in the adjacency list
        createNodes();
        createConnections();
		      // Set the start and goal nodes
        this.startNode = getNode(rows - 1, 0); // Bottom-left corner
        this.goalNode = getNode(0, cols - 1);  // Top-right corner
    }

    // Function to create the list of nodes
    private void createNodes() {
        int nodeCount = 1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = new Node(row, col, nodeCount);
                nodes.add(node);
                adjacencyList.put(node, new ArrayList<>()); // Initialize empty list for each node
                nodeCount++;
            }
        }
    }

    // Function to create the adjacency list
    private void createConnections() {
        for (Node node : nodes) {
            int row = node.getRow();
            int col = node.getColumn();

            // Connect to the right (if not at the last column)
            if (col < cols - 1) {
                Node rightNode = getNode(row, col + 1);
                adjacencyList.get(node).add(rightNode);
                adjacencyList.get(rightNode).add(node);
            }

            // Connect to the bottom (if not at the last row)
            if (row < rows - 1) {
                Node bottomNode = getNode(row + 1, col);
                adjacencyList.get(node).add(bottomNode);
                adjacencyList.get(bottomNode).add(node);
            }
        }
    }

    // Helper function to retrieve a node based on its row and column
    private Node getNode(int row, int col) {
        for (Node node : nodes) {
            if (node.getRow() == row && node.getColumn() == col) {
                return node;
            }
        }
        return null;
    }

    // DFS function to traverse the graph with random neighbor selection
    public void dfs(Node node) {
        visited.add(node);

        // Get the neighbors of the current node
        List<Node> neighbors = adjacencyList.get(node);

        // Shuffle the neighbors randomly
        Collections.shuffle(neighbors, rand);

        // Explore each neighbor randomly
        for (Node neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                // Store the edge (both directions)
                traversedEdges.add(node + "-" + neighbor);
                traversedEdges.add(neighbor + "-" + node);

                // Traverse the neighbor
                dfs(neighbor);
            }
        }
    }

    // Function to remove untraversed edges from the adjacency list
    public void removeUntraversedEdges() {
        for (Node node : adjacencyList.keySet()) {
            List<Node> neighbors = new ArrayList<>(adjacencyList.get(node));
            for (Node neighbor : neighbors) {
                String edgeKey = node + "-" + neighbor;
                if (!traversedEdges.contains(edgeKey)) {
                    adjacencyList.get(node).remove(neighbor); // Remove edge if not traversed
                    adjacencyList.get(neighbor).remove(node); // Remove reverse direction
                }
            }
        }
    }

    // Function to print the grid graph (before or after)
    public void printGridGraph() {
        // We need to create a 2*rows-1 by 2*cols-1 grid for visualization
        int gridRows = 2 * rows - 1;
        int gridCols = 2 * cols - 1;
        char[][] grid = new char[gridRows][gridCols];

        // Initialize grid with walls (#) and nodes (.)
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    grid[i][j] = NODE;  // Place nodes
                } else {
                    grid[i][j] = WALL;  // Default walls between nodes
                }
            }
        }

        // Add edges where necessary
        for (Node node : adjacencyList.keySet()) {
            int row = 2 * node.getRow();
            int col = 2 * node.getColumn();

            for (Node neighbor : adjacencyList.get(node)) {
                int neighborRow = 2 * neighbor.getRow();
                int neighborCol = 2 * neighbor.getColumn();

                // Calculate the edge position between node and neighbor
                int edgeRow = (row + neighborRow) / 2;
                int edgeCol = (col + neighborCol) / 2;

                // Replace the wall with an edge ('E') for traversed connections
                grid[edgeRow][edgeCol] = EDGE;
            }
        }

        // Convert the grid to a string representation and print it
        for (char[] row : grid) {
            System.out.println(row);
        }
    }

    public static void main(String[] args) {
        // Create a grid graph using the Node-based adjacency list
        GridGraphVisualizerAdjacencyListWithNode gridGraph = new GridGraphVisualizerAdjacencyListWithNode(50, 50);

        System.out.println("Grid Graph before DFS:");
//        gridGraph.printGridGraph();  // Show grid graph before DFS

        // Start randomized DFS from node (0,0)
        gridGraph.dfs(gridGraph.getNode(0, 0));

        // Remove untraversed edges
        gridGraph.removeUntraversedEdges();

        System.out.println("\nGrid Graph after DFS and edge removal:");
        gridGraph.printGridGraph();  // Show grid graph after DFS
        System.out.println(gridGraph.getStartNode());
        System.out.println(gridGraph.getGoalNode());
    }

	/**
	 * @return the startNode
	 */
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * @return the goalNode
	 */
	public Node getGoalNode() {
		return goalNode;
	}
}

