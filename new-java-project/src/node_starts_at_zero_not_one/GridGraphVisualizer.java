package node_starts_at_zero_not_one;

import java.util.*;

public class GridGraphVisualizer {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';
	private static final char PATH = 'P';
	private static final char TRIED = '?';
	private static final char START = 'S';
	private static final char GOAL = 'G';
	private int rows;
	private int cols;
	private List<Node> nodes;
	private int[][] adjacencyMatrix;
	private boolean[] visited;
	private Set<Edge> traversedEdges; // Use Edge class for traversed edges
	private Random rand;
	// New properties to store the number of forks and path forks
	private int numForks;
	private int numPathForks;
	private int numCorrectPathForks;
	private int numIncorrectPathForks;

	// Arrays for different stages of the maze
	private char[][] gridGraphArray; // Original grid graph where everything is connected
	private char[][] mazeArray; // Maze where only traversed edges are kept
	private char[][] solveArray; // Solved maze with path and tried nodes
	private char[][] transformedMazeArray; // Transformed maze with 'S', 'G', and '.' replacing 'E'
	private char[][] transformedSolveArray; // Transformed solved maze
	private char[][] forksArray;
	// Strings for different stages of the maze
	private String gridGraphString;
	private String mazeString;
	private String solveString;
	private String transformedMazeString;
	private String transformedSolveString;
	private String forksString;
	// New properties
	private Node start;
	private Node goal;
	private List<Node> pathForks; // Intersection of forks and path nodes
	private List<Node> path;
	private List<Edge> pathEdges; // List of Edge objects for the path
	private List<Node> tried;
	private List<Edge> triedEdges; // List of Edge objects for tried edges
	// Lists for transformed coordinates
	private List<int[]> pathCoordinates; // List to store coordinates where 'P' will be placed
	private List<int[]> triedCoordinates; // List to store coordinates where '?' will be placed
	// New properties
	private List<Node> correctForks; // Forks where the correct decision was made
	private List<Node> incorrectForks; // Forks where an incorrect decision was made
	// Analytical properties
	private int backtrackForksCount; // Counter for backtracking forks
	private List<Node> backtrackForkNodes; // List of nodes where backtracking occurred
	private int correctMovesCount; // Counter for correct moves (P)
	private int incorrectMovesCount; // Counter for incorrect moves (?)
	// Property to store nodes that are part of the maze
	private Set<Node> mazeNodes; // Use a set to avoid duplicates
	private List<Node> forks; // Nodes that connect to more than 2 other nodes
	// Constructor to initialize the grid graph, perform DFS, and solve the maze

	public GridGraphVisualizer(int rows, int cols) {
		this.backtrackForkNodes = new ArrayList<>();
		this.backtrackForksCount = 0; // Initialize backtracking forks count
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
		this.mazeNodes = new HashSet<>(); // Initialize the maze nodes
		this.pathCoordinates = new ArrayList<>();
		this.triedCoordinates = new ArrayList<>();
		// Set the start (lower left) and goal (upper right)
		this.start = new Node(rows - 1, 0, nodeIndex(rows - 1, 0));
		this.goal = new Node(0, cols - 1, nodeIndex(0, cols - 1));
		// Initialize arrays
		this.gridGraphArray = new char[2 * rows - 1][2 * cols - 1];
		this.mazeArray = new char[2 * rows - 1][2 * cols - 1];
		this.solveArray = new char[2 * rows - 1][2 * cols - 1];
		this.transformedMazeArray = new char[2 * rows - 1][2 * cols - 1];
		this.transformedSolveArray = new char[2 * rows - 1][2 * cols - 1];
		this.forksArray = new char[2 * rows - 1][2 * cols - 1];
		this.correctForks = new ArrayList<>(); // Initialize correct forks
		this.incorrectForks = new ArrayList<>(); // Initialize incorrect forks
		// Stage 1: Build and store the initial grid graph
		createNodes();
		createConnections();
//		this.gridGraphString = buildGridGraphString();
		// Stage 1: Build and store the original grid graph (everything connected)
		buildGridGraphArray();
		this.gridGraphString = arrayToString(gridGraphArray);
		// Stage 2: Perform random DFS to create the maze and remove untraversed edges
		randomDFS(0); // Randomized DFS starting from node 0
		removeUntraversedEdges();

//		this.mazeString = buildGridGraphString(); // Build the final maze string after edge removal
		buildMazeArray(); // Build maze after edge removal
		this.mazeString = arrayToString(mazeArray);
		// Stage 3: Reset visited array before solving the maze
		Arrays.fill(visited, false);
		// Stage 3: Solve the maze using ordered DFS (NESW)
		solveMaze(start.getNodeNumber()); // Solve the maze from the start to goal
		computeCoordinates();
		// Stage 6: Construct solve string by modifying the maze array with 'P' and '?'
		constructSolveArray();
		this.solveString = arrayToString(solveArray);
		// Stage 7: Apply transformations for the two new versions
		this.transformedMazeArray = transformArray(mazeArray);
		this.transformedMazeString = arrayToString(transformedMazeArray);
		this.pathForks = new ArrayList<>(); // Initialize path forks
		this.transformedSolveArray = transformArray(solveArray);
		this.transformedSolveString = arrayToString(transformedSolveArray);
		countForksFromTriedEdges();
		this.forks = new ArrayList<>(); // Initialize forks
		computeForks(); // Find all fork nodes
		computePathForks(); // Find the intersection of forks and path nodes
		generateForksView();
		classifyForks();
		// Set the fork counts using size()
		this.numForks = forks.size();
		this.numPathForks = pathForks.size();
		this.numCorrectPathForks = correctForks.size();
		this.numIncorrectPathForks = incorrectForks.size();
	}

	public void printNonZeroValuesInAdjacencyMatrix() {
		for (int row = 0; row < adjacencyMatrix.length; row++) {
			int nonZeroCount = 0;
			for (int col = 0; col < adjacencyMatrix[row].length; col++) {
				if (adjacencyMatrix[row][col] != 0) {
					nonZeroCount++;
				}
			}
			// Print the row number (0-indexed) and the count of nonzero values
			System.out.println(row + " " + nonZeroCount);
		}
	}

	// Create the forks view character array based on the transformed maze array
	private void createForksArray() {
		// Initialize forksViewArray with the transformedMazeArray
		for (int row = 0; row < transformedMazeArray.length; row++) {
			for (int col = 0; col < transformedMazeArray[row].length; col++) {
				forksArray[row][col] = transformedMazeArray[row][col]; // Initialize with transformedMazeArray
			}
		}

		// Replace nodes in the forks with 'F'
		for (Node forkNode : forks) {
			int[] forkCoordinates = transformNodeCoordinates(forkNode);
			forksArray[forkCoordinates[0]][forkCoordinates[1]] = 'F'; // Mark forks with 'F'
		}
	}

	// Generate the forks view string by joining the forks view array
	public void generateForksView() {
		createForksArray(); // Create the array with forks marked as 'F'
		forksString = arrayToString(forksArray); // Set the forksString property by joining the array
	}

	// Getter for forksString
	public String getForksString() {
		return forksString; // Return the generated forksString
	}

	// Function to compute forks: nodes with more than two edges
	private void computeForks() {
		for (int nodeIndex = 0; nodeIndex < adjacencyMatrix.length; nodeIndex++) {
			int edgeCount = 0;

			// Count the number of non-zero entries (edges) in this node's row
			for (int neighbor = 0; neighbor < adjacencyMatrix[nodeIndex].length; neighbor++) {
				if (adjacencyMatrix[nodeIndex][neighbor] == 1) {
					edgeCount++;
				}
			}

			// If the node has more than 2 edges, it's a fork
			if (edgeCount > 2) {
				forks.add(nodes.get(nodeIndex)); // Add to forks set
			}
		}
	}

	// Transform an array: Replace start with 'S', goal with 'G', and 'E' with '.'
	private char[][] transformArray(char[][] array) {
		char[][] transformed = new char[array.length][array[0].length];

		// Copy the original array and apply transformations
		for (int row = 0; row < array.length; row++) {
			for (int col = 0; col < array[row].length; col++) {
				if (row == start.getRow() * 2 && col == start.getCol() * 2) {
					transformed[row][col] = START; // Replace start with 'S'
				} else if (row == goal.getRow() * 2 && col == goal.getCol() * 2) {
					transformed[row][col] = GOAL; // Replace goal with 'G'
				} else if (array[row][col] == EDGE) {
					transformed[row][col] = NODE; // Replace edges 'E' with '.'
				} else {
					transformed[row][col] = array[row][col]; // Copy everything else as is
				}
			}
		}

		return transformed;
	}

	// Helper function to join a character array into a string
	private String arrayToString(char[][] array) {
		StringBuilder sb = new StringBuilder();
		for (char[] row : array) {
			sb.append(new String(row)).append("\n");
		}
		return sb.toString();
	}

	// Build the original grid graph where all nodes are connected
	private void buildGridGraphArray() {
		for (int row = 0; row < 2 * rows - 1; row++) {
			for (int col = 0; col < 2 * cols - 1; col++) {
				if (row % 2 == 0 && col % 2 == 0) {
					gridGraphArray[row][col] = NODE;
				} else if (row % 2 == 0) {
					gridGraphArray[row][col] = EDGE; // All edges are present initially
				} else if (col % 2 == 0) {
					gridGraphArray[row][col] = EDGE; // All edges are present initially
				} else {
					gridGraphArray[row][col] = WALL;
				}
			}
		}
	}

	// Construct the solve array by placing 'P' and '?'
	private void constructSolveArray() {
		// Copy mazeArray to solveArray
		for (int i = 0; i < mazeArray.length; i++) {
			solveArray[i] = Arrays.copyOf(mazeArray[i], mazeArray[i].length);
		}

		// Place 'P' at path coordinates
		for (int[] coord : pathCoordinates) {
			solveArray[coord[0]][coord[1]] = PATH;
		}

		// Place '?' at tried coordinates
		for (int[] coord : triedCoordinates) {
			solveArray[coord[0]][coord[1]] = TRIED;
		}
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 0; // Start node count from 0 instead of 1
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
		// Add the current node to the maze nodes set
		mazeNodes.add(nodes.get(node));
		// Get the neighbors of the current node
		List<Integer> neighbors = getNeighbors(node);

		// Shuffle the neighbors randomly
		Collections.shuffle(neighbors, rand);

		// Explore each neighbor randomly
		for (int neighbor : neighbors) {
			if (!visited[neighbor]) {
				// Create an Edge object for the traversed edge
				Edge edge = new Edge(nodes.get(node), nodes.get(neighbor));
				traversedEdges.add(edge);

				// Traverse the neighbor
				randomDFS(neighbor);
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
	}

	// Modified DFS method to correctly detect forks on the correct path
	private boolean solveMaze(int node) {
		visited[node] = true;
		path.add(nodes.get(node));

		if (node == goal.getNodeNumber()) {
			return true; // Reached the goal
		}

		// Get neighbors in NESW order based on node coordinates
		List<Integer> neighbors = getOrderedNeighbors(node);
		boolean potentialFork = (neighbors.size() > 1); // Detect potential forks
		int wrongTurns = 0; // Count wrong turns at this node

		for (int neighbor : neighbors) {
			if (!visited[neighbor] && adjacencyMatrix[node][neighbor] == 1) {
				Edge edge = new Edge(nodes.get(node), nodes.get(neighbor));
				pathEdges.add(edge);

				if (solveMaze(neighbor)) {
					return true;
				} else {
					// Backtrack: Move from path to tried
					tried.add(nodes.get(neighbor));
					path.remove(nodes.get(neighbor));
					triedEdges.add(edge);
					pathEdges.remove(edge);
					wrongTurns++; // Increment wrong turn count
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
				Edge edge = new Edge(nodes.get(i), nodes.get(j));
				if (adjacencyMatrix[i][j] == 1 && !traversedEdges.contains(edge)) {
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

	// Build the maze character array based on adjacency matrix logic
	private void buildMazeArray() {
		for (int row = 0; row < 2 * rows - 1; row++) {
			for (int col = 0; col < 2 * cols - 1; col++) {
				if (row % 2 == 0 && col % 2 == 0) {
					// This is a node position
					mazeArray[row][col] = NODE;
				} else if (row % 2 == 0) {
					// Horizontal edge between nodes
					int leftNode = nodeIndex(row / 2, (col - 1) / 2);
					int rightNode = nodeIndex(row / 2, (col + 1) / 2);
					if (adjacencyMatrix[leftNode][rightNode] == 1) {
						mazeArray[row][col] = EDGE;
					} else {
						mazeArray[row][col] = WALL;
					}
				} else if (col % 2 == 0) {
					// Vertical edge between nodes
					int topNode = nodeIndex((row - 1) / 2, col / 2);
					int bottomNode = nodeIndex((row + 1) / 2, col / 2);
					if (adjacencyMatrix[topNode][bottomNode] == 1) {
						mazeArray[row][col] = EDGE;
					} else {
						mazeArray[row][col] = WALL;
					}
				} else {
					// Walls between nodes and edges
					mazeArray[row][col] = WALL;
				}
			}
		}
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

	// Getter for the tried nodes
	public List<Node> getTried() {
		return tried;
	}

	// Getter for the start node
	public Node getStart() {
		return start;
	}

	// Getter for the goal node
	public Node getGoal() {
		return goal;
	}

	// Function to compute path forks (intersection of forks and path nodes)
	private void computePathForks() {
		for (Node node : path) {
			if (forks.contains(node)) {
				pathForks.add(node); // Only add nodes that are both in the path and forks
			}
		}
	}

	public static void main(String[] args) {
//		// Create a 3x3 grid graph and solve the maze
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(3, 3);
//
//		System.out.println("Initial Grid Graph:");
////        System.out.println(gridGraph3x3.getGridGraphString());
//
//		System.out.println("Maze after DFS and edge removal:");
//		System.out.println(gridGraph3x3.getMazeString());
//
//		System.out.println("Path from start to goal:");
//		System.out.println(gridGraph3x3.getPath());
//		System.out.println(gridGraph3x3.getPathEdges());
//		System.out.println("Tried nodes:");
//		System.out.println(gridGraph3x3.getTried());
//		System.out.println(gridGraph3x3.getTriedEdges());
//		System.out.println("Path coordinates:");
//		for (int[] coord : gridGraph3x3.getPathCoordinates()) {
//			System.out.println(Arrays.toString(coord));
//		}
//
//		System.out.println("Tried coordinates:");
//		for (int[] coord : gridGraph3x3.getTriedCoordinates()) {
//			System.out.println(Arrays.toString(coord));
//		}
		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(100, 100/* 3,3 */);

//        System.out.println("Original Grid Graph (All Edges Present):");
//        System.out.println(gridGraph3x3.getGridGraphString());

		System.out.println("Maze after DFS and Edge Removal:");
//        System.out.println(gridGraph3x3.getTransformedMazeString());

//        System.out.println("Solved Maze:");
		System.out.println(gridGraph3x3.getTransformedSolveString());
        System.out.println("The number of correct moves was "+gridGraph3x3.getCorrectMovesCount());
        System.out.println("The number of incorrect moves was "+gridGraph3x3.getIncorrectMovesCount());
//        System.out.println("The number of incorrect forks was "+gridGraph3x3.getBacktrackForksCount());
//        System.out.println("Nodes where backtracking occurred: " + gridGraph3x3.getBacktrackForkNodes());
//        System.out.println(gridGraph3x3.getTriedEdges());
//		System.out.println(gridGraph3x3.getForksString());
//        System.out.println("forks--"+gridGraph3x3.getForks());
//		System.out.println("path forks--" + gridGraph3x3.getPathForks());
//        System.out.println(gridGraph3x3.getTried().toString());
//        System.out.println("correct forks--"+gridGraph3x3.getCorrectForks());
//        System.out.println("incorrect forks--"+gridGraph3x3.getIncorrectForks());
//        
//        gridGraph3x3.printNonZeroValuesInAdjacencyMatrix();
//        System.out.println();
//        gridGraph3x3.printNonZeroValuesForPathForks();
		System.out.println("Correct forks--" + gridGraph3x3.getNumCorrectPathForks());

		System.out.println("Incorrect forks--" + gridGraph3x3.getNumIncorrectPathForks());

	}

	public List<Node> getAdjacentNodes(Node node) {
		List<Node> adjacentNodes = new ArrayList<>();
		int nodeNumber = node.getNodeNumber(); // Get the index of the node from the Node object

		// Loop through the row corresponding to the node in the adjacency matrix
		for (int col = 0; col < adjacencyMatrix[nodeNumber].length; col++) {
			if (adjacencyMatrix[nodeNumber][col] == 1) {
				// If there's a connection (value of 1), add the corresponding node from the
				// List<Node>
				Node adjacentNode = nodes.get(col); // Retrieve node from the nodes list by index
				adjacentNodes.add(adjacentNode);
			}
		}

		return adjacentNodes;
	}

	public boolean isIncorrectFork(Node forkNode) {
		// Get the adjacent nodes for the given forkNode
		List<Node> adjacentNodes = getAdjacentNodes(forkNode);

		// Check if there is an intersection between the adjacent nodes and the tried
		// set
		for (Node adjacent : adjacentNodes) {
			if (tried.contains(adjacent)) {
				// If any adjacent node is in the tried set, return true
				return true;
			}
		}

		// If no intersection found, return false
		return false;
	}

	public void classifyForks() {
		// Clear the current correct and incorrect forks lists
		correctForks.clear();
		incorrectForks.clear();

		// Loop through each node in pathForks
		for (Node forkNode : pathForks) {
			// Check if the forkNode is incorrect
			if (isIncorrectFork(forkNode)) {
				// If it's an incorrect fork, add to incorrectForks
				incorrectForks.add(forkNode);
			} else {
				// If it's a correct fork, add to correctForks
				correctForks.add(forkNode);
			}
		}
	}

	public void printNonZeroValuesForPathForks() {
		// Loop through each node in the pathForks list
		for (Node fork : pathForks) {
			int nodeNumber = fork.getNodeNumber(); // No need to subtract 1 anymore
			int nonZeroCount = 0;

			// Check if nodeNumber is valid within matrix bounds
			if (nodeNumber >= 0 && nodeNumber < adjacencyMatrix.length) {
				// Count the nonzero values in the adjacency matrix row for this node
				for (int col = 0; col < adjacencyMatrix[nodeNumber].length; col++) {
					if (adjacencyMatrix[nodeNumber][col] != 0) {
						nonZeroCount++;
					}
				}
				// Print the node number (0-indexed) and the count of nonzero values
				System.out.println((nodeNumber) + " " + nonZeroCount);
			} else {
				// Handle cases where nodeNumber is out of bounds
				System.out.println("Invalid node number: " + (nodeNumber));
			}
		}
	}

	/*
	 * find tried edges get all the nodes from the edges by combining the start
	 * nodes and end nodes from tried edges into one big list. Remove duplicates
	 * from this list of nodes. Count how many nodes from path are in this list. Or
	 * to put it another way, take the intersection between this list and your path
	 * nodes, and find the length /cardinality/number of elements in this
	 * intersection.
	 * 
	 * 
	 * 
	 * 
	 */
	private void countForksFromTriedEdges() {
		Set<Node> uniqueTriedNodes = new HashSet<>();

		// Step 1: Collect all nodes from tried edges (start and end)
		for (Edge edge : triedEdges) {
			uniqueTriedNodes.add(edge.getStartNode());
			uniqueTriedNodes.add(edge.getEndNode());
		}

		// Step 2: Find intersection between path nodes and tried edge nodes
		Set<Node> forkNodes = new HashSet<>(path); // Create a copy of the path set
		forkNodes.retainAll(uniqueTriedNodes); // Intersection with unique tried nodes

		// Step 3: Store and count the forks
		backtrackForkNodes.clear(); // Clear previous forks
		backtrackForkNodes.addAll(forkNodes); // Store the new fork nodes
		backtrackForksCount = backtrackForkNodes.size(); // Update the fork count
	}

	// Compute the coordinates for path ('P') and tried ('?')
	private void computeCoordinates() {
		// For path coordinates: Transform node coordinates and union with edge
		// coordinates
		for (Node node : path) {
			pathCoordinates.add(transformNodeCoordinates(node)); // Add transformed node coordinates
		}
		for (Edge edge : pathEdges) {
			pathCoordinates.add(new int[] { edge.getEdgeRowCoordinate(), edge.getEdgeColumnCoordinate() }); // Add edge
																											// coordinates
		}

		// For tried coordinates: Transform node coordinates and union with edge
		// coordinates
		for (Node node : tried) {
			triedCoordinates.add(transformNodeCoordinates(node)); // Add transformed node coordinates
		}
		for (Edge edge : triedEdges) {
			triedCoordinates.add(new int[] { edge.getEdgeRowCoordinate(), edge.getEdgeColumnCoordinate() }); // Add edge
																												// coordinates
		}
		// Count correct and incorrect moves
		correctMovesCount = pathCoordinates.size(); // Each coordinate in path represents a correct move
		incorrectMovesCount = triedCoordinates.size(); // Each coordinate in tried represents an incorrect move

	}

	// Helper function to transform node coordinates by multiplying by 2
	private int[] transformNodeCoordinates(Node node) {
		return new int[] { node.getRow() * 2, node.getCol() * 2 };
	}

	/**
	 * @return the traversedEdges
	 */
	public Set<Edge> getTraversedEdges() {
		return traversedEdges;
	}

	/**
	 * @return the pathEdges
	 */
	public List<Edge> getPathEdges() {
		return pathEdges;
	}

	/**
	 * @return the triedEdges
	 */
	public List<Edge> getTriedEdges() {
		return triedEdges;
	}

	/**
	 * @return the pathCoordinates
	 */
	public List<int[]> getPathCoordinates() {
		return pathCoordinates;
	}

	/**
	 * @return the triedCoordinates
	 */
	public List<int[]> getTriedCoordinates() {
		return triedCoordinates;
	}

	/**
	 * @return the solveString
	 */
	public String getSolveString() {
		return solveString;
	}

	/**
	 * @return the transformedSolveArray
	 */
	public char[][] getTransformedSolveArray() {
		return transformedSolveArray;
	}

	/**
	 * @return the transformedMazeString
	 */
	public String getTransformedMazeString() {
		return transformedMazeString;
	}

	/**
	 * @return the transformedSolveString
	 */
	public String getTransformedSolveString() {
		return transformedSolveString;
	}

	/**
	 * @return the backtrackForksCount
	 */
	public int getBacktrackForksCount() {
		return backtrackForksCount;
	}

	/**
	 * @return the backtrackForkNodes
	 */
	public List<Node> getBacktrackForkNodes() {
		return backtrackForkNodes;
	}

	/**
	 * @return the correctMovesCount
	 */
	public int getCorrectMovesCount() {
		return correctMovesCount;
	}

	/**
	 * @return the incorrectMovesCount
	 */
	public int getIncorrectMovesCount() {
		return incorrectMovesCount;
	}

	/**
	 * @return the mazeNodes
	 */
	public Set<Node> getMazeNodes() {
		return mazeNodes;
	}

	/**
	 * @return the forks
	 */
	public List<Node> getForks() {
		return forks;
	}

	/**
	 * @return the pathForks
	 */
	public List<Node> getPathForks() {
		return pathForks;
	}

	/**
	 * @param tried the tried to set
	 */
	public void setTried(List<Node> tried) {
		this.tried = tried;
	}

	/**
	 * @return the correctForks
	 */
	public List<Node> getCorrectForks() {
		return correctForks;
	}

	/**
	 * @return the incorrectForks
	 */
	public List<Node> getIncorrectForks() {
		return incorrectForks;
	}

	/**
	 * @return the numForks
	 */
	public int getNumForks() {
		return numForks;
	}

	/**
	 * @return the numPathForks
	 */
	public int getNumPathForks() {
		return numPathForks;
	}

	/**
	 * @return the numCorrectPathForks
	 */
	public int getNumCorrectPathForks() {
		return numCorrectPathForks;
	}

	/**
	 * @return the numIncorrectPathForks
	 */
	public int getNumIncorrectPathForks() {
		return numIncorrectPathForks;
	}
}
