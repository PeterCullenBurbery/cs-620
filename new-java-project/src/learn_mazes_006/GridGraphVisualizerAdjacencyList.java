package learn_mazes_006;

import java.util.*;

public class GridGraphVisualizerAdjacencyList {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';

	private int rows;
	private int cols;
	private List<Integer> nodes;
	private Map<Integer, List<Integer>> adjacencyList; // Adjacency list
	private Set<String> traversedEdges; // Store the traversed edges
	private boolean[] visited;
	private Random rand; // Randomizer for shuffling neighbors

	// Constructor to initialize the grid graph
	public GridGraphVisualizerAdjacencyList(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new ArrayList<>();
		this.adjacencyList = new HashMap<>();
		this.traversedEdges = new HashSet<>();
		this.visited = new boolean[rows * cols];
		this.rand = new Random(); // Initialize the random object

		// Create nodes and set up connections in the adjacency list
		createNodes();
		createConnections();
	}

	// Function to create the list of nodes
	private void createNodes() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int currentNode = nodeIndex(row, col);
				nodes.add(currentNode);
				adjacencyList.put(currentNode, new ArrayList<>()); // Initialize empty list for each node
			}
		}
	}

	// Function to create the adjacency list
	private void createConnections() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int currentNode = nodeIndex(row, col); // Get the current node's index

				// Connect to the right (if not at the last column)
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					adjacencyList.get(currentNode).add(rightNode);
					adjacencyList.get(rightNode).add(currentNode);
				}

				// Connect to the bottom (if not at the last row)
				if (row < rows - 1) {
					int bottomNode = nodeIndex(row + 1, col);
					adjacencyList.get(currentNode).add(bottomNode);
					adjacencyList.get(bottomNode).add(currentNode);
				}
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
	}

	// DFS function to traverse the graph with random neighbor selection
	public void dfs(int node) {
		visited[node] = true;

		// Get the neighbors of the current node
		List<Integer> neighbors = adjacencyList.get(node);

		// Shuffle the neighbors randomly
		Collections.shuffle(neighbors, rand);

		// Explore each neighbor randomly
		for (int neighbor : neighbors) {
			if (!visited[neighbor]) {
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
		for (int node : adjacencyList.keySet()) {
			List<Integer> neighbors = new ArrayList<>(adjacencyList.get(node));
			for (int neighbor : neighbors) {
				String edgeKey = node + "-" + neighbor;
				if (!traversedEdges.contains(edgeKey)) {
					adjacencyList.get(node).remove(Integer.valueOf(neighbor)); // Remove edge if not traversed
					adjacencyList.get(neighbor).remove(Integer.valueOf(node)); // Remove reverse direction
				}
			}
		}
	}

	// Function to print the grid graph (before or after)
	public void printGridGraph() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Print the node
				System.out.print(NODE);
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					int currentNode = nodeIndex(row, col);
					// Print edge or wall between nodes
					if (adjacencyList.get(currentNode).contains(rightNode)) {
						System.out.print(EDGE);
					} else {
						System.out.print(WALL);
					}
				}
			}
			System.out.println();
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					int bottomNode = nodeIndex(row + 1, col);
					int currentNode = nodeIndex(row, col);
					// Print edge or wall between nodes
					if (adjacencyList.get(currentNode).contains(bottomNode)) {
						System.out.print(EDGE);
					} else {
						System.out.print(WALL);
					}
					if (col < cols - 1) {
						System.out.print(WALL); // Wall between rows
					}
				}
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		// Create a 3x3 grid graph and visualize it before DFS
		GridGraphVisualizerAdjacencyList gridGraph3x3 = new GridGraphVisualizerAdjacencyList(50, 50);

		System.out.println("Grid Graph before DFS:");
		// gridGraph3x3.printGridGraph(); // Show grid graph before DFS

		// Start randomized DFS from node 0 (Node 1)
		gridGraph3x3.dfs(0);

		// Remove untraversed edges
		gridGraph3x3.removeUntraversedEdges();

		System.out.println("\nGrid Graph after DFS and edge removal:");
		gridGraph3x3.printGridGraph(); // Show grid graph after DFS
	}
}
