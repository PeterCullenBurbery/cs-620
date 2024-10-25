package mazes_007;

import java.util.*;

public class GridGraphVisualizer {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';

	private int rows;
	private int cols;
	private List<Node> nodes;
	private int[][] adjacencyMatrix;
	private boolean[] visited;
	private Set<String> traversedEdges;
	private Random rand;

	private String gridGraphString; // String representation of the grid graph
	private String mazeString; // String representation of the maze after DFS

	// New properties
	private Node start;
	private Node goal;
	private List<Node> path;
	private List<String> pathEdges;
	private List<Node> tried;
	private List<String> triedEdges;

	// Constructor to initialize the grid graph, perform DFS, and solve the maze
	public GridGraphVisualizer(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new ArrayList<>();
		this.adjacencyMatrix = new int[rows * cols][rows * cols];
		this.visited = new boolean[rows * cols];
		this.traversedEdges = new HashSet<>();
		this.rand = new Random();
		this.path = new ArrayList<>();
		this.pathEdges = new ArrayList<>();
		this.tried = new ArrayList<>();
		this.triedEdges = new ArrayList<>();

		// Set the start (lower left) and goal (upper right)
		this.start = new Node(rows - 1, 0, nodeIndex(rows - 1, 0));
		this.goal = new Node(0, cols - 1, nodeIndex(0, cols - 1));

		// Stage 1: Build and store the initial grid graph
		createNodes();
		createConnections();
		this.gridGraphString = buildGridGraphString();

		// Stage 2: Perform random DFS to create the maze and remove untraversed edges
		randomDFS(0); // Randomized DFS starting from node 0
		removeUntraversedEdges();
		this.mazeString = buildGridGraphString(); // Build the final maze string after edge removal
		// Stage 3: Reset visited array before solving the maze
	    Arrays.fill(visited, false);
		// Stage 3: Solve the maze using ordered DFS (NESW)
		solveMaze(start.getNodeNumber()); // Solve the maze from the start to goal
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 1;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				nodes.add(new Node(row, col, nodeCount));
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

	// DFS function to traverse the graph with random neighbor selection
	public void randomDFS(int node) {
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
				randomDFS(neighbor);
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
	}

	// Function to solve the maze using DFS with NESW order
	private boolean solveMaze(int node) {
	    visited[node] = true;
	    path.add(nodes.get(node));

	    if (node == goal.getNodeNumber()) {
	        return true; // Reached the goal
	    }

	    // Get neighbors in NESW order based on node coordinates
	    List<Integer> neighbors = getOrderedNeighbors(node);

	    for (int neighbor : neighbors) {
	        // Check if the edge is valid in the modified maze (after DFS and edge removal)
	        if (!visited[neighbor] && adjacencyMatrix[node][neighbor] == 1) {  // Only traverse valid edges
	            pathEdges.add(node + "-" + neighbor);
	            traversedEdges.add(node + "-" + neighbor);
	            traversedEdges.add(neighbor + "-" + node);

	            if (solveMaze(neighbor)) {
	                return true;
	            } else {
	                // Backtrack: Move from path to tried
	                tried.add(nodes.get(neighbor));
	                path.remove(nodes.get(neighbor));

	                triedEdges.add(node + "-" + neighbor);
	                pathEdges.remove(node + "-" + neighbor);
	            }
	        }
	    }
	    return false; // No path found, backtracking
	}


	// Function to get neighbors in NESW order for solving the maze
	private List<Integer> getOrderedNeighbors(int node) {
		List<Integer> neighbors = new ArrayList<>();
		int row = nodes.get(node).getRow();
		int col = nodes.get(node).getCol();

		// North (row - 1, col)
		if (row > 0) {
			neighbors.add(nodeIndex(row - 1, col));
		}
		// East (row, col + 1)
		if (col < cols - 1) {
			neighbors.add(nodeIndex(row, col + 1));
		}
		// South (row + 1, col)
		if (row < rows - 1) {
			neighbors.add(nodeIndex(row + 1, col));
		}
		// West (row, col - 1)
		if (col > 0) {
			neighbors.add(nodeIndex(row, col - 1));
		}

		return neighbors;
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
	private void removeUntraversedEdges() {
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				String edgeKey = i + "-" + j;
				if (adjacencyMatrix[i][j] == 1 && !traversedEdges.contains(edgeKey)) {
					adjacencyMatrix[i][j] = 0;
				}
			}
		}
	}

	// Function to build a string representation of the grid graph or maze
	private String buildGridGraphString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < 2 * rows - 1; row++) {
			for (int col = 0; col < 2 * cols - 1; col++) {
				// Determine if the current position represents a node or edge
				if (row % 2 == 0 && col % 2 == 0) {
					int nodeIndex = nodeIndex(row / 2, col / 2);
					sb.append(NODE); // Append node
				} else if (row % 2 == 0) {
					// Horizontal edges between nodes
					int leftNode = nodeIndex(row / 2, (col - 1) / 2);
					int rightNode = nodeIndex(row / 2, (col + 1) / 2);
					if (adjacencyMatrix[leftNode][rightNode] == 1) {
						sb.append(EDGE); // Append edge
					} else {
						sb.append(WALL); // Append wall
					}
				} else if (col % 2 == 0) {
					// Vertical edges between nodes
					int topNode = nodeIndex((row - 1) / 2, col / 2);
					int bottomNode = nodeIndex((row + 1) / 2, col / 2);
					if (adjacencyMatrix[topNode][bottomNode] == 1) {
						sb.append(EDGE); // Append edge
					} else {
						sb.append(WALL); // Append wall
					}
				} else {
					// Walls between nodes and edges
					sb.append(WALL);
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	// Getter for the grid graph string (before DFS)
	public String getGridGraphString() {
		return gridGraphString;
	}

	// Getter for the maze string (after DFS and edge removal)
	public String getMazeString() {
		return mazeString;
	}

	// Getter for the path of nodes
	public List<Node> getPath() {
		return path;
	}

	// Getter for the edges in the path
	public List<String> getPathEdges() {
		return pathEdges;
	}

	// Getter for the tried nodes
	public List<Node> getTried() {
		return tried;
	}

	// Getter for the tried edges
	public List<String> getTriedEdges() {
		return triedEdges;
	}

	// Getter for the start node
	public Node getStart() {
		return start;
	}

	// Getter for the goal node
	public Node getGoal() {
		return goal;
	}

	public static void main(String[] args) {
		// Create a 3x3 grid graph and solve the maze
		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(50, 50);

		System.out.println("Initial Grid Graph:");
//        System.out.println(gridGraph3x3.getGridGraphString());

		System.out.println("Maze after DFS and edge removal:");
		System.out.println(gridGraph3x3.getMazeString());

//		System.out.println("Path from start to goal:");
//		System.out.println(gridGraph3x3.getPath());
//		System.out.println(gridGraph3x3.getPathEdges());
//		System.out.println("Tried nodes:");
//		System.out.println(gridGraph3x3.getTried());
//		System.out.println(gridGraph3x3.getTriedEdges());
	}
}
