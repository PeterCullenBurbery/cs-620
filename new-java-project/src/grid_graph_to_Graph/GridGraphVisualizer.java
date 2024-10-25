package grid_graph_to_Graph;

import java.util.*;

public class GridGraphVisualizer {

	private static final char EDGE = 'E';
	private static final char NODE_CHAR = '.';
	private static final char WALL = '#';
	private static final char START_CHAR = 'S';
	private static final char GOAL_CHAR = 'G';

	private int rows;
	private int cols;
	private List<Node> allNodes; // List to store all nodes in the graph
	private List<Node> visitedNodes; // List to store visited nodes during DFS
	private Map<Node, List<Node>> adjacencyList;
	private Set<String> traversedEdges; // Store the traversed edges
	private Random rand; // Randomizer for shuffling neighbors
//	private String SInCorner; // Property to store the graph string with 'S' and 'G'
	private Node startNode; // Store the start node
	private Node goalNode; // Store the goal node
	private String gridGraph;
	private String mazeGraph;
	// Constructor to initialize the grid graph
	public GridGraphVisualizer(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.allNodes = new ArrayList<>();
		this.visitedNodes = new ArrayList<>();
		this.adjacencyList = new HashMap<>();
		this.traversedEdges = new HashSet<>();
		this.rand = new Random(); // Initialize the random object
//		this.SInCorner = ""; // Initialize SInCorner string

		// Create nodes and set up connections in the adjacency list
		createNodes();
		createConnections();

		// Set the start and goal nodes
		setStartAndGoalNodes();
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 1;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Node newNode = new Node(row, col, nodeCount);
				allNodes.add(newNode); // Store the node in the list of all nodes
				nodeCount++;
			}
		}
	}

	// Function to create the adjacency list
	private void createConnections() {
		for (Node node : allNodes) {
			adjacencyList.putIfAbsent(node, new ArrayList<>());

			// Get the node's row and column
			int row = node.getRow();
			int col = node.getCol();

			// Connect to the right (if not at the last column)
			if (col < cols - 1) {
				Node rightNode = getNodeAt(row, col + 1);
				adjacencyList.get(node).add(rightNode);
				adjacencyList.putIfAbsent(rightNode, new ArrayList<>());
				adjacencyList.get(rightNode).add(node);
			}

			// Connect to the bottom (if not at the last row)
			if (row < rows - 1) {
				Node bottomNode = getNodeAt(row + 1, col);
				adjacencyList.get(node).add(bottomNode);
				adjacencyList.putIfAbsent(bottomNode, new ArrayList<>());
				adjacencyList.get(bottomNode).add(node);
			}
		}
	}

	// Helper method to find a node by row and column
	private Node getNodeAt(int row, int col) {
		return allNodes.stream().filter(node -> node.getRow() == row && node.getCol() == col).findFirst().orElse(null);
	}

	// Function to set the start node at the bottom-left and goal node at the
	// top-right
	private void setStartAndGoalNodes() {
		startNode = getNodeAt(rows - 1, 0); // Bottom-left corner
		goalNode = getNodeAt(0, cols - 1); // Top-right corner
	}

	// Function to update the graph string with 'S' at the start node and 'G' at the
	// goal node
	public void updateSInCorner() {
		StringBuilder gridWithStartAndGoal = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Node currentNode = getNodeAt(row, col);
				if (currentNode.equals(startNode)) {
					gridWithStartAndGoal.append(START_CHAR);
				} else if (currentNode.equals(goalNode)) {
					gridWithStartAndGoal.append(GOAL_CHAR);
				} else {
					gridWithStartAndGoal.append(NODE_CHAR);
				}

				if (col < cols - 1) {
					Node rightNode = getNodeAt(row, col + 1);
					// Print edge or wall between nodes
					if (adjacencyList.get(currentNode).contains(rightNode)) {
						gridWithStartAndGoal.append(EDGE);
					} else {
						gridWithStartAndGoal.append(WALL);
					}
				}
			}
			gridWithStartAndGoal.append("\n");
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					Node currentNode = getNodeAt(row, col);
					Node bottomNode = getNodeAt(row + 1, col);
					// Print edge or wall between nodes
					if (adjacencyList.get(currentNode).contains(bottomNode)) {
						gridWithStartAndGoal.append(EDGE);
					} else {
						gridWithStartAndGoal.append(WALL);
					}
					if (col < cols - 1) {
						gridWithStartAndGoal.append(WALL); // Wall between rows
					}
				}
				gridWithStartAndGoal.append("\n");
			}
		}

		this.gridGraph = gridWithStartAndGoal.toString();
	}

	// DFS function to traverse the graph with random neighbor selection
	public void dfs(Node node) {
		visitedNodes.add(node); // Mark the node as visited

		// Get the neighbors of the current node
		List<Node> neighbors = new ArrayList<>(adjacencyList.get(node));

		// Shuffle the neighbors randomly
		Collections.shuffle(neighbors, rand);

		// Explore each neighbor randomly
		for (Node neighbor : neighbors) {
			if (!visitedNodes.contains(neighbor)) {
				// Store the edge (both directions)
				traversedEdges.add(node.getNodeNumber() + "-" + neighbor.getNodeNumber());
				traversedEdges.add(neighbor.getNodeNumber() + "-" + node.getNodeNumber());

				// Traverse the neighbor
				dfs(neighbor);
			}
		}
	}

	// Function to remove untraversed edges from the adjacency list
	public void removeUntraversedEdges() {
		for (Node node : adjacencyList.keySet()) {
			adjacencyList.get(node).removeIf(neighbor -> {
				String edgeKey = node.getNodeNumber() + "-" + neighbor.getNodeNumber();
				return !traversedEdges.contains(edgeKey);
			});
		}
	}

	// Function to print the grid graph (before or after)
	public void printGridGraph() {
		System.out.print(gridGraph); // Print the grid with 'S' and 'G'
	}

	public static void main(String[] args) {
		// Create a 4x5 grid graph and visualize it before DFS
		GridGraphVisualizer gridGraph4x5 = new GridGraphVisualizer(7,7);

		// Set 'S' in the bottom-left and 'G' in the top-right
		//gridGraph4x5.updateSInCorner();

		System.out.println("Grid Graph with 'S' at the bottom-left and 'G' at the top-right:");
		gridGraph4x5.printGridGraph(); // Show grid graph with 'S' and 'G'
//		gridGraph4x5.updateSInCorner();
//		// Start randomized DFS from the start node
//		gridGraph4x5.dfs(gridGraph4x5.startNode);
//
//		// Remove untraversed edges
//		gridGraph4x5.removeUntraversedEdges();
//
//		System.out.println("\nGrid Graph after DFS and edge removal:");
//		gridGraph4x5.updateSInCorner(); // Update the grid with 'S' and 'G' again
//		gridGraph4x5.printGridGraph(); // Show grid graph after DFS
	}
}
