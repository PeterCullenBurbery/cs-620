package mazes_004;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

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
	private Map<Node, List<Node>> edges;
	private String solve; // Property to store the final solved maze with 'P' and '?'
	// Constructor to initialize the grid graph and store pre/post DFS graphs

	// Constructor to initialize the grid graph and store pre/post DFS graphs
	public MazeFun(int rows, int cols) {
	    this.rows = rows;
	    this.cols = cols;

	    // Initialize mazeGrid first
	    this.mazeGrid = generateInitialGrid();  // Create an initial grid (default structure)

	    // Initialize nodes list and edges map before creating nodes and connections
	    this.nodes = new ArrayList<>();  // Initialize nodes list here
	    this.edges = new HashMap<>();    // Initialize edges map

	    // Create nodes and connections
	    createNodes();  // Now nodes is initialized, so this will work correctly
	    createConnections();  // Create connections between nodes

	    // Initialize various fields
	    this.adjacencyMatrix = new int[rows * cols][rows * cols]; // Initialize the adjacency matrix
	    this.visited = new boolean[rows * cols]; // Initialize visited array
	    this.traversedEdges = new HashSet<>(); // Initialize traversedEdges
	    this.rand = new Random(); // Initialize random object

	    // Generate and store the pre-DFS graph (before edges are removed)
	    this.preDfsGraph = generateGridGraphPreDfs();

	    // Perform DFS and remove untraversed edges
	    dfs(startNode); // Start DFS from the first node
	    removeUntraversedEdges();

	    // Generate and store the post-DFS graph (after edges are removed)
	    this.postDfsGraph = generateGridGraphPostDfs();

	    // Process the post-DFS graph and store the result
	    this.postDfsProcessed = processPostDfsGraph();

	    // Reinitialize the mazeGrid based on the processed post-DFS graph
	    this.mazeGrid = convertToGrid(this.postDfsProcessed);

	    // Check if startNode and goalNode are found
	    if (startNode == null || goalNode == null) {
	        throw new IllegalStateException("Start node or goal node could not be found in the grid.");
	    }

	    // Solve the maze and store the result
	    this.solve = solveMaze();
	}


	// Function to generate an initial grid structure
	private char[][] generateInitialGrid() {
	    char[][] grid = new char[rows][cols];

	    // Example: Fill the grid with walls by default
	    for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < cols; j++) {
	            grid[i][j] = WALL;  // Fill with walls or any other default structure
	        }
	    }

	    // Place start and goal in default positions (optional)
	    grid[rows - 1][0] = START;  // Bottom-left corner for start
	    grid[0][cols - 1] = GOAL;   // Top-right corner for goal

	    return grid;  // Return the initialized grid
	}

	private int directionPriority(Node currentNode, Node neighbor) {
		// Assume north is row decreasing, east is col increasing, south is row
		// increasing, west is col decreasing
		if (neighbor.getRow() < currentNode.getRow()) {
			return 1; // North
		} else if (neighbor.getCol() > currentNode.getCol()) {
			return 2; // East
		} else if (neighbor.getRow() > currentNode.getRow()) {
			return 3; // South
		} else {
			return 4; // West
		}
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

	// Function to generate and return the pre-DFS grid graph using adjacency list
	public String generateGridGraphPreDfs() {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Node currentNode = getNodeAt(row, col); // Get the current node

				// Append the node (assuming it's not a wall)
				sb.append(currentNode != null ? NODE : WALL);

				if (col < cols - 1) {
					Node rightNode = getNodeAt(row, col + 1);
					// Append edge or wall between current node and the node to its right
					if (rightNode != null && edges.get(currentNode).contains(rightNode)) {
						sb.append(EDGE); // There's an edge between current and right node
					} else {
						sb.append(WALL); // No edge, so it's a wall
					}
				}
			}
			sb.append("\n");

			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					Node currentNode = getNodeAt(row, col);
					Node bottomNode = getNodeAt(row + 1, col);
					// Append edge or wall between current node and the node below
					if (currentNode != null && bottomNode != null && edges.get(currentNode).contains(bottomNode)) {
						sb.append(EDGE); // There's an edge between current and bottom node
					} else {
						sb.append(WALL); // No edge, so it's a wall
					}

					if (col < cols - 1) {
						sb.append(WALL); // Wall between rows
					}
				}
				sb.append("\n");
			}
		}

		return sb.toString(); // Return the pre-DFS graph
	}

	// Function to generate and return the post-DFS grid graph using adjacency list
	public String generateGridGraphPostDfs() {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Node currentNode = getNodeAt(row, col); // Get the current node

				// Append the node (assuming it's not a wall)
				sb.append(currentNode != null ? NODE : WALL);

				if (col < cols - 1) {
					Node rightNode = getNodeAt(row, col + 1);
					// Append edge or wall between current node and the node to its right
					if (rightNode != null && edges.get(currentNode).contains(rightNode)) {
						sb.append(EDGE); // There's an edge between current and right node
					} else {
						sb.append(WALL); // No edge, so it's a wall
					}
				}
			}
			sb.append("\n");

			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					Node currentNode = getNodeAt(row, col);
					Node bottomNode = getNodeAt(row + 1, col);
					// Append edge or wall between current node and the node below
					if (currentNode != null && bottomNode != null && edges.get(currentNode).contains(bottomNode)) {
						sb.append(EDGE); // There's an edge between current and bottom node
					} else {
						sb.append(WALL); // No edge, so it's a wall
					}

					if (col < cols - 1) {
						sb.append(WALL); // Wall between rows
					}
				}
				sb.append("\n");
			}
		}

		return sb.toString(); // Return the post-DFS graph
	}

	// Helper function to get a node based on row and column
	private Node getNodeAt(int row, int col) {
		// Assuming you have a method that retrieves the Node object based on row and
		// column.
		// If the node doesn't exist at the given row/col, return null.
		int index = nodeIndex(row, col);
		if (index < nodes.size()) {
			return nodes.get(index);
		}
		return null; // No node at this position
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
	            // Check if the current cell is a walkable cell ('.', 'S', 'G')
	            char cell = mazeGrid[row][col];
	            if (cell == NODE || cell == START || cell == GOAL) {
	                // Create a new Node for this cell and add it to the list
	                Node newNode = new Node(row, col, nodeCount);
	                nodes.add(newNode);

	                // Check if this node is the start or goal node
	                if (cell == START) {
	                    startNode = newNode;  // Assign the start node directly
	                } else if (cell == GOAL) {
	                    goalNode = newNode;  // Assign the goal node directly
	                }

	                nodeCount++;
	            }
	        }
	    }
	}


	private void createConnections() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Node currentNode = getNodeAt(row, col); // Modify to fetch nodes
				if (currentNode != null) {
					// Add neighboring nodes to the adjacency list
					addEdge(currentNode, getNodeAt(row, col + 1)); // Right neighbor
					addEdge(currentNode, getNodeAt(row + 1, col)); // Down neighbor
				}
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
	}

	// Helper function to add an edge between two nodes
	private void addEdge(Node node1, Node node2) {
	    if (node1 != null && node2 != null) {  // Ensure both nodes are not null
	        edges.computeIfAbsent(node1, k -> new ArrayList<>()).add(node2);  // Add edge from node1 to node2
	        edges.computeIfAbsent(node2, k -> new ArrayList<>()).add(node1);  // Add edge from node2 to node1 (bidirectional)
	    }
	}


	// DFS function to traverse the graph with random neighbor selection
	public void dfs(Node currentNode) {
		visited[currentNode.getNodeNumber()] = true;
		markVisited(currentNode, 'P'); // Mark path

		// If we reached the goal node, stop searching
		if (currentNode.equals(goalNode)) {
			return;
		}

		// Get sorted neighbors in priority order (north, east, south, west)
		List<Node> neighbors = getSortedNeighbors(currentNode);

		boolean foundUnvisited = false;
		for (Node neighbor : neighbors) {
			if (!visited[neighbor.getNodeNumber()]) {
				foundUnvisited = true;
				dfs(neighbor); // Recursively perform DFS
				break;
			}
		}

		// If no unvisited neighbors, backtrack
		if (!foundUnvisited && currentNode != startNode) {
			markVisited(currentNode, '?'); // Mark backtrack
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

	// Function to remove untraversed edges from the adjacency list
	public void removeUntraversedEdges() {
		for (Node node : edges.keySet()) {
			List<Node> neighbors = new ArrayList<>(edges.get(node)); // Copy of neighbors to avoid concurrent
																		// modification

			for (Node neighbor : neighbors) {
				String edgeKey = node.getNodeNumber() + "-" + neighbor.getNodeNumber();
				if (!traversedEdges.contains(edgeKey)) {
					// Remove the edge if it wasn't traversed
					edges.get(node).remove(neighbor); // Remove from current node's list
					edges.get(neighbor).remove(node); // Remove from neighbor's list (bidirectional)
				}
			}
		}
	}

	// Function to get sorted neighbors, filtering out null values
	private List<Node> getSortedNeighbors(Node node) {
	    List<Node> neighbors = edges.get(node);
	    if (neighbors != null) {
	        // Filter out any null neighbors before sorting
	        neighbors.removeIf(n -> n == null);
	        
	        // Sort neighbors by direction: north, east, south, west
	        neighbors.sort((n1, n2) -> directionPriority(node, n1) - directionPriority(node, n2));
	    }
	    return neighbors;
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

	private void markVisited(Node node, char marker) {
		if (mazeGrid[node.getRow()][node.getCol()] != START && mazeGrid[node.getRow()][node.getCol()] != GOAL) {
			mazeGrid[node.getRow()][node.getCol()] = marker;
		}
	}

	/**
	 * @return the postDfsProcessed
	 */
	public String getPostDfsProcessed() {
		return postDfsProcessed;
	}

	// Function to solve the maze using DFS, returning the maze string with 'P' and
	// '?' markings
	public String solveMaze() {
		// Initialize visited array and stack for DFS traversal
		this.visited = new boolean[rows * cols];
		Stack<Node> dfsStack = new Stack<>();
		Set<Node> visitedNodes = new HashSet<>();

		// Start DFS from the start node
		dfsStack.push(startNode);
		visitedNodes.add(startNode);
		markVisited(startNode, 'P'); // Mark start node as part of the solution path

		while (!dfsStack.isEmpty()) {
			Node currentNode = dfsStack.peek();

			// If we reached the goal node, stop searching
			if (currentNode.equals(goalNode)) {
				break; // Terminate DFS when the goal is reached
			}

			// Get sorted neighbors in the priority order (north, east, south, west)
			List<Node> neighbors = getSortedNeighbors(currentNode);

			boolean foundUnvisitedNeighbor = false;
			for (Node neighbor : neighbors) {
				if (!visitedNodes.contains(neighbor)) {
					dfsStack.push(neighbor); // Push the neighbor to the stack
					visitedNodes.add(neighbor); // Mark the neighbor as visited
					markVisited(neighbor, 'P'); // Mark this neighbor as part of the solution path
					foundUnvisitedNeighbor = true;
					break; // Move to the next neighbor
				}
			}

			// If no unvisited neighbor found, backtrack
			if (!foundUnvisitedNeighbor) {
				Node backtrackingNode = dfsStack.pop(); // Backtrack
				if (backtrackingNode != startNode && backtrackingNode != goalNode) {
					markVisited(backtrackingNode, '?'); // Mark backtracked nodes with '?'
				}
			}
		}

		// Convert the final maze with 'P' and '?' into a string and return it
		return convertMazeToString();
	}

	// Helper method to convert the current state of the maze (with 'P' and '?')
	// into a string
	private String convertMazeToString() {
		StringBuilder result = new StringBuilder();
		for (int row = 0; row < mazeGrid.length; row++) {
			for (int col = 0; col < mazeGrid[row].length; col++) {
				result.append(mazeGrid[row][col]);
			}
			result.append("\n"); // Add a newline after each row
		}
		return result.toString();
	}

	/**
	 * @return the solve
	 */
	public String getSolve() {
		return solve;
	}

}
