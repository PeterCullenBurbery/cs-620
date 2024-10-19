package mazes;

import java.util.*;

public class GridGraph {

	private int rows;
	private int cols;
	private List<Integer> nodes;
	private int[][] adjacencyMatrix;
	private Set<String> traversedEdges; // Store edges traversed during DFS

	// Constructor to initialize the grid graph
	public GridGraph(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new ArrayList<>();
		this.adjacencyMatrix = new int[rows * cols][rows * cols];
		this.traversedEdges = new HashSet<>();

		// Create nodes and set up connections in the adjacency matrix
		createNodes();
		createConnections();
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 1;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				nodes.add(nodeCount);
				nodeCount++;
			}
		}
	}

	// Function to create the adjacency matrix (fully connected grid)
	private void createConnections() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int currentNode = nodeIndex(row, col); // Get the current node's index

				// Connect to the right (if not at the last column)
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					adjacencyMatrix[currentNode][rightNode] = 1;
					adjacencyMatrix[rightNode][currentNode] = 1;
				}

				// Connect to the bottom (if not at the last row)
				if (row < rows - 1) {
					int bottomNode = nodeIndex(row + 1, col);
					adjacencyMatrix[currentNode][bottomNode] = 1;
					adjacencyMatrix[bottomNode][currentNode] = 1;
				}
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
	}

	// DFS traversal to explore the graph and mark traversed edges
	public void dfsTraversal() {
		boolean[] visited = new boolean[nodes.size()];
		Stack<Integer> stack = new Stack<>();
		Random rand = new Random();

		// Start DFS from a random node
		int startNode = rand.nextInt(nodes.size());
		stack.push(startNode);
		visited[startNode] = true;

		while (!stack.isEmpty()) {
			int currentNode = stack.pop();

			// Get all neighbors (connected nodes)
			for (int neighbor = 0; neighbor < adjacencyMatrix[currentNode].length; neighbor++) {
				if (adjacencyMatrix[currentNode][neighbor] == 1 && !visited[neighbor]) {
					// Mark the edge as traversed
					traversedEdges.add(edgeKey(currentNode, neighbor));
					visited[neighbor] = true;
					stack.push(neighbor);
				}
			}
		}
	}

	// Create a unique key for an edge between two nodes
	private String edgeKey(int node1, int node2) {
		return Math.min(node1, node2) + "-" + Math.max(node1, node2);
	}

	// Remove the edges that were not traversed by DFS
	public void removeUntraversedEdges() {
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				if (adjacencyMatrix[i][j] == 1 && !traversedEdges.contains(edgeKey(i, j))) {
					// Remove the untraversed edge
					adjacencyMatrix[i][j] = 0;
				}
			}
		}
	}

	// Function to print the adjacency matrix
	public void printAdjacencyMatrix() {
		System.out.println("Adjacency Matrix:");
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				System.out.print(adjacencyMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// Create a 3x3 grid graph
		GridGraph gridGraph = new GridGraph(3, 3);

		// Print the initial fully connected adjacency matrix
		System.out.println("Initial Adjacency Matrix:");
		gridGraph.printAdjacencyMatrix();

		// Perform DFS traversal
		gridGraph.dfsTraversal();

		// Remove untraversed edges
		gridGraph.removeUntraversedEdges();

		// Print the final adjacency matrix after removing untraversed edges
		System.out.println("\nFinal Adjacency Matrix after DFS:");
		gridGraph.printAdjacencyMatrix();
	}
}
