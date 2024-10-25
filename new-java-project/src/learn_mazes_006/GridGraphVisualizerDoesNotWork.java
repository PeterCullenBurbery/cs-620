package learn_mazes_006;

import java.util.*;

public class GridGraphVisualizerDoesNotWork {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';
	private static final char PATH = 'P';
	private static final char TRIED = '?';

	private int rows;
	private int cols;
	private List<Integer> nodes;
	private int[][] adjacencyMatrix;
	private Map<Integer, List<Integer>> adjacencyList; // Store adjacency list for second graph
	private Set<String> traversedEdges; // Store the traversed edges
	private boolean[] visited;
	private Random rand; // Randomizer for shuffling neighbors

	// Lists for path and tried tracking
	private List<Integer> path;
	private List<String> pathEdges;
	private List<Integer> tried;
	private List<String> triedEdges;

	// Constructor to initialize the grid graph
	public GridGraphVisualizerDoesNotWork(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new ArrayList<>();
		this.adjacencyMatrix = new int[rows * cols][rows * cols];
		this.adjacencyList = new HashMap<>();
		this.traversedEdges = new HashSet<>();
		this.visited = new boolean[rows * cols];
		this.rand = new Random(); // Initialize the random object
		this.path = new ArrayList<>();
		this.pathEdges = new ArrayList<>();
		this.tried = new ArrayList<>();
		this.triedEdges = new ArrayList<>();

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
		// Convert to adjacency list after removing untraversed edges
		createAdjacencyList();
	}

	// Create adjacency list from the modified adjacency matrix
	private void createAdjacencyList() {
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			adjacencyList.put(i, new ArrayList<>());
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				if (adjacencyMatrix[i][j] == 1) {
					adjacencyList.get(i).add(j);
				}
			}
		}
	}

	// Solve the maze using NESW priority (DFS) and track path, tried nodes, and
	// edges
	public boolean solveMaze(int currentNode, int goalNode) {
		path.add(currentNode);

		if (currentNode == goalNode) {
			return true; // Goal node reached
		}

		visited[currentNode] = true;

		// Get neighbors from the adjacency list (NESW order)
		List<Integer> neighbors = adjacencyList.get(currentNode);
		for (int neighbor : neighbors) {
			if (!visited[neighbor]) {
				pathEdges.add(currentNode + "-" + neighbor);

				boolean found = solveMaze(neighbor, goalNode);
				if (found) {
					return true; // Goal reached
				}

				// Backtrack
				path.remove((Integer) neighbor);
				tried.add(neighbor);
				String backtrackedEdge = currentNode + "-" + neighbor;
				pathEdges.remove(backtrackedEdge);
				triedEdges.add(backtrackedEdge);
			}
		}

		return false;
	}

	// Visualization with 'P' for path and '?' for tried
	public String generateSolutionVisualization() {
		StringBuilder visual = new StringBuilder();

		for (int row = 0; row < rows; row++) {
			// First print nodes
			for (int col = 0; col < cols; col++) {
				int currentNode = nodeIndex(row, col);

				// Check if the node is in the path or tried
				if (path.contains(currentNode)) {
					visual.append(PATH); // Path node
				} else if (tried.contains(currentNode)) {
					visual.append(TRIED); // Tried node
				} else {
					visual.append(NODE); // Regular node
				}

				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					if (pathEdges.contains(currentNode + "-" + rightNode)) {
						visual.append(PATH);
					} else if (triedEdges.contains(currentNode + "-" + rightNode)) {
						visual.append(TRIED);
					} else {
						visual.append(WALL);
					}
				}
			}
			visual.append("\n");

			// Then print the vertical edges between rows
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					int currentNode = nodeIndex(row, col);
					int bottomNode = nodeIndex(row + 1, col);
					if (pathEdges.contains(currentNode + "-" + bottomNode)) {
						visual.append(PATH);
					} else if (triedEdges.contains(currentNode + "-" + bottomNode)) {
						visual.append(TRIED);
					} else {
						visual.append(WALL);
					}

					if (col < cols - 1) {
						visual.append(WALL); // Wall between rows
					}
				}
				visual.append("\n");
			}
		}

		return visual.toString();
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
					if (adjacencyMatrix[currentNode][rightNode] == 1) {
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
					if (adjacencyMatrix[currentNode][bottomNode] == 1) {
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
		// Create a 5x5 grid graph and visualize it
		GridGraphVisualizerDoesNotWork gridGraph = new GridGraphVisualizerDoesNotWork(5, 5);

		// Perform DFS to traverse the graph and remove untraversed edges
		gridGraph.dfs(0);
		gridGraph.removeUntraversedEdges();
gridGraph.printGridGraph();
		// Solve the maze using DFS (start node 0, goal node 24)
		gridGraph.solveMaze(0, 24);

		// Print the solution visualization
		System.out.println("\nMaze Solution Visualization:");
//		System.out.println(gridGraph.generateSolutionVisualization());
		System.out.println(gridGraph.getPath());
	}

	/**
	 * @return the path
	 */
	public List<Integer> getPath() {
		return path;
	}
}
