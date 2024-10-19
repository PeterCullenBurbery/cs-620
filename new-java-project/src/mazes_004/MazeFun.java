package mazes_004;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MazeFun {
	private static final char NODE = '.';
	private static final char WALL = '#';
	private static final char START = 'S';
	private static final char GOAL = 'G';
	private static final char EDGE = 'E'; // Constant to represent edges
	private int[][] adjacencyMatrix; // This will store the connections between nodes
	private int rows;
	private int cols;
	private String preDfsGraph;
	private String postDfsGraph;
	private String postDfsProcessed;
	private char[][] mazeGrid;
	private boolean[] visited; // Boolean array to track visited nodes
	private Node startNode;
	private Set<String> traversedEdges;
	private Node goalNode;
	private List<Node> nodes; // List to store the nodes in the maze
	private Random rand; // Randomizer for shuffling neighbors

	// Constructor to initialize the grid graph and store pre/post DFS graphs
	public MazeFun(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		// Initialize various fields
		this.adjacencyMatrix = new int[rows * cols][rows * cols]; // Initialize the adjacency matrix
		this.visited = new boolean[rows * cols]; // Initialize visited array
		this.traversedEdges = new HashSet<>(); // Initialize traversedEdges
		this.nodes = new ArrayList<>(); // Initialize nodes list
		this.rand = new Random(); // Initialize random object

		// Create nodes and connections for the adjacency matrix
		createNodes();
		createConnections();

		// Generate and store the pre-DFS graph (before edges are removed)
		this.preDfsGraph = generateGridGraphPreDfs();

		// Perform DFS and remove untraversed edges
		dfs(0); // Start DFS from the first node
		removeUntraversedEdges();

		// Generate and store the post-DFS graph (after edges are removed)
		this.postDfsGraph = generateGridGraphPostDfs();

		// Process the post-DFS graph and store the result
		this.postDfsProcessed = processPostDfsGraph();

		// Initialize the maze grid based on the processed post-DFS graph
		this.mazeGrid = convertToGrid(this.postDfsProcessed);

		// Now find the start and goal nodes in the processed grid
		this.startNode = findNode(START);
		this.goalNode = findNode(GOAL);

		// Check if startNode and goalNode are found
		if (startNode == null || goalNode == null) {
			throw new IllegalStateException("Start node or goal node could not be found in the grid.");
		}

		// Solve the maze and store the result
		// this.solve = solveMaze();
	}

	// Function to convert the string grid to a 2D char array for easier traversal
	private char[][] convertToGrid(String gridString) {
		String[] lines = gridString.split("\n");
		char[][] grid = new char[lines.length][];
		for (int i = 0; i < lines.length; i++) {
			grid[i] = lines[i].toCharArray();
		}
		return grid;
	}

	// Function to generate and return the pre-DFS grid graph
	public String generateGridGraphPreDfs() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Append the node
				sb.append(NODE);
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					int currentNode = nodeIndex(row, col);
					// Append edge or wall between nodes
					if (adjacencyMatrix[currentNode][rightNode] == 1) {
						sb.append(EDGE);
					} else {
						sb.append(WALL);
					}
				}
			}
			sb.append("\n");
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					int bottomNode = nodeIndex(row + 1, col);
					int currentNode = nodeIndex(row, col);
					// Append edge or wall between nodes
					if (adjacencyMatrix[currentNode][bottomNode] == 1) {
						sb.append(EDGE);
					} else {
						sb.append(WALL);
					}
					if (col < cols - 1) {
						sb.append(WALL); // Wall between rows
					}
				}
				sb.append("\n");
			}
		}
		return sb.toString(); // Return pre-DFS graph
	}

	// Function to generate and return the post-DFS grid graph
	public String generateGridGraphPostDfs() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Append the node
				sb.append(NODE);
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					int currentNode = nodeIndex(row, col);
					// Append edge or wall between nodes
					if (adjacencyMatrix[currentNode][rightNode] == 1) {
						sb.append(EDGE);
					} else {
						sb.append(WALL);
					}
				}
			}
			sb.append("\n");
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					int bottomNode = nodeIndex(row + 1, col);
					int currentNode = nodeIndex(row, col);
					// Append edge or wall between nodes
					if (adjacencyMatrix[currentNode][bottomNode] == 1) {
						sb.append(EDGE);
					} else {
						sb.append(WALL);
					}
					if (col < cols - 1) {
						sb.append(WALL); // Wall between rows
					}
				}
				sb.append("\n");
			}
		}
		return sb.toString(); // Return post-DFS graph
	}

	// Function to process the post-DFS graph (replace 'E' with '.', set 'S' and
	// 'G')
	// Function to process the post-DFS graph (replace 'E' with '.', set 'S' and
	// 'G')
	// Function to process the post-DFS graph (replace 'E' with '.', set 'S' and
	// 'G')
	public String processPostDfsGraph() {
		// Split the postDfsGraph into lines (each representing a row of the grid)
		String[] lines = postDfsGraph.split("\n");

		// Use StringBuilder to construct the new graph
		StringBuilder sb = new StringBuilder();

		// Replace 'E' with '.' in each line
		for (int row = 0; row < lines.length; row++) {
			String line = lines[row].replace(EDGE, NODE); // Replace 'E' with '.'
			sb.append(line).append("\n");
		}

		int lastColIndex = cols - 1; // The index of the last column (rightmost column)

		// Extract last and first lines for modification
		String lastLine = sb.substring(sb.lastIndexOf("\n", sb.length() - 2) + 1, sb.length() - 1); // Extract last line
		String firstLine = sb.substring(0, sb.indexOf("\n")); // Extract first line

		// 'S' in the bottom-left corner (first character of the last line)
		lastLine = 'S' + lastLine.substring(1); // Replace the first character of the last line with 'S'

		// 'G' in the top-right corner (last character of the first line)
		firstLine = firstLine.substring(0, lastColIndex) + 'G'; // Replace the last character of the first line with 'G'

		// Rebuild the final string with modified first and last lines
		sb.replace(0, sb.indexOf("\n"), firstLine); // Replace the first line
		sb.replace(sb.lastIndexOf("\n", sb.length() - 2) + 1, sb.length() - 1, lastLine); // Replace the last line

		return sb.toString();
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 1;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Create a new Node with the row, col, and nodeCount and add it to the list
				nodes.add(new Node(row, col, nodeCount));
				nodeCount++; // Increment the node count
			}
		}
	}

	// Function to create the adjacency matrix
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

	// DFS function to traverse the graph with random neighbor selection
	public void dfs(int node) {
		visited[node] = true;

		// Get the neighbors of the current node
		List<Integer> neighbors = getNeighbors(node);

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

	// Function to get the neighbors of a node
	private List<Integer> getNeighbors(int node) {
		List<Integer> neighbors = new ArrayList<>();
		for (int neighbor = 0; neighbor < adjacencyMatrix[node].length; neighbor++) {
			if (adjacencyMatrix[node][neighbor] == 1) {
				neighbors.add(neighbor);
			}
		}
		return neighbors;
	}

	// Function to remove untraversed edges from the adjacency matrix
	public void removeUntraversedEdges() {
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				String edgeKey = i + "-" + j;
				if (adjacencyMatrix[i][j] == 1 && !traversedEdges.contains(edgeKey)) {
					// Remove the edge
					adjacencyMatrix[i][j] = 0;
				}
			}
		}
	}

	// Function to find a node in the grid (start or goal)
	private Node findNode(char target) {
		for (int row = 0; row < mazeGrid.length; row++) {
			for (int col = 0; col < mazeGrid[row].length; col++) {
				if (mazeGrid[row][col] == target) {
					return new Node(row, col, nodeIndex(row, col)); // Node constructor now includes nodeNumber
				}
			}
		}
		return null; // Not found
	}

	/**
	 * @return the postDfsProcessed
	 */
	public String getPostDfsProcessed() {
		return postDfsProcessed;
	}

}
