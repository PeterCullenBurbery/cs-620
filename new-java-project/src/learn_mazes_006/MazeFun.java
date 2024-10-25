package learn_mazes_006;

import java.util.*;

public class MazeFun {
	private int rows;
	private int columns;
	private List<Node> nodes;
	private Map<Node, List<Node>> originalGraph; // Original grid graph with all edges
	private List<Edge> traversedEdges; // List of traversed edges during DFS
	private Map<Node, List<Node>> mazeGraph; // Second graph created from traversed edges
	private Node startNode;
	private Node goalNode;
	private String originalGraphVisual;
	private String traversedGraphVisual;
	// Lists to track nodes and edges during maze-solving
	private List<Node> path; // Nodes in the path
	private List<Edge> pathEdges; // Edges in the path
	private List<Node> tried; // Nodes that were backtracked
	private List<Edge> triedEdges; // Edges that were backtracked
//    private String pathAndTriedGraphVisual;  // Third visual string for path and tried nodes/edges
	private String visualization; // Visualization property
	// Constructor that takes rows and columns, generates nodes and the original
	// graph
	private String pathVisualization;  // Visualization with 'P' added
	private Set<Node> visited; // Track visited nodes during DFS
    private Random rand; // Randomizer for DFS neighbor selection
	public MazeFun(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.nodes = generateNodes();
		this.originalGraph = generateGridGraph(); // Full grid graph with all edges
		this.traversedEdges = new ArrayList<>(); // List of edges traversed during DFS
		this.visited = new HashSet<>();
        this.rand = new Random();
		this.startNode = getNode(rows - 1, 0); // Starting at lower-left corner
		this.goalNode = getNode(0, columns - 1); // Goal node at upper-right corner
		performDFS(startNode); // Perform DFS to traverse all nodes and track traversed edges
		this.mazeGraph = createTraversedGraph(); // Create second graph from traversed edges
		initializeSolvingLists();
		// Set the string visualizations for both the original and traversed graphs
		this.originalGraphVisual = generateGraphString(originalGraph);
		this.traversedGraphVisual = generateGraphString(mazeGraph);
		// Compute the third visualizer with 'P' and '?'
//        this.pathAndTriedGraphVisual = generatePathAndTriedGraph();
		// Compute and set the visualization
		this.solveMaze();
		this.visualization = computeVisualization();
		this.pathVisualization = generatePathVisualization();
	}
	// String properties for the visual representation of both graphs

	// Static method to transform a coordinate (row or column)
	public static int transformCoordinate(int input) {
		return 2 * input;
	}
//    // Method to generate the path visualization by copying the traversed graph and adding 'P'
//    private String generatePathVisualization() {
//        // Make a copy of the traversedVisualization
//        String[] traversedLines = traversedGraphVisual.split("\n");
//        char[][] gridCopy = new char[traversedLines.length][];
//        
//        for (int i = 0; i < traversedLines.length; i++) {
//            gridCopy[i] = traversedLines[i].toCharArray();
//        }
//
//        // Replace the first node from the path with 'P' in the copied grid
//        if (!path.isEmpty()) {
//            for (Node node : path) {
//                int row = node.getRow();
//                int col = node.getColumn();
//
//                // Transform the row and column for grid placement
//                int transformedRow = transformCoordinate(row);
//                int transformedCol = transformCoordinate(col);
//
//                // Replace '.' with 'P' at the transformed position
//                if (transformedRow < gridCopy.length && transformedCol < gridCopy[0].length) {
//                    gridCopy[transformedRow][transformedCol] = 'P';
//                }
//            }
//        }
//
//        // Convert the grid copy to a string representation
//        StringBuilder visual = new StringBuilder();
//        for (char[] row : gridCopy) {
//            visual.append(row).append("\n");
//        }
//
//        return visual.toString();
//    }
    // Method to generate the path visualization by copying the traversed graph and adding 'P'
//    private String generatePathVisualization() {
//        // Make a copy of the traversedGraphVisual
//        String[] traversedLines = traversedGraphVisual.split("\n");
//        char[][] gridCopy = new char[traversedLines.length][];
//        
//        for (int i = 0; i < traversedLines.length; i++) {
//            gridCopy[i] = traversedLines[i].toCharArray();
//        }
//
//        // Replace the nodes and edges in the path with 'P'
//        if (!path.isEmpty()) {
//            for (int i = 0; i < path.size(); i++) {
//                Node currentNode = path.get(i);
//                int row = currentNode.getRow();
//                int col = currentNode.getColumn();
//
//                // Transform the row and column for grid placement
//                int transformedRow = transformCoordinate(row);
//                int transformedCol = transformCoordinate(col);
//
//                // Replace '.' with 'P' at the transformed position for nodes
//                if (transformedRow < gridCopy.length && transformedCol < gridCopy[0].length) {
//                    gridCopy[transformedRow][transformedCol] = 'P';
//                }
//
//                // Now handle edges between consecutive nodes in the path
//                if (i < path.size() - 1) {
//                    Node nextNode = path.get(i + 1);
//                    int nextRow = nextNode.getRow();
//                    int nextCol = nextNode.getColumn();
//
//                    // Transform next node's coordinates
//                    int transformedNextRow = transformCoordinate(nextRow);
//                    int transformedNextCol = transformCoordinate(nextCol);
//
//                    // Calculate edge position between the two nodes
//                    int edgeRow = (transformedRow + transformedNextRow) / 2;
//                    int edgeCol = (transformedCol + transformedNextCol) / 2;
//
//                    // Replace 'E' with 'P' only if both nodes are part of the path
//                    if (gridCopy[edgeRow][edgeCol] == 'E') {
//                        gridCopy[edgeRow][edgeCol] = 'P';
//                    }
//                }
//            }
//        }
//
//        // Convert the grid copy to a string representation
//        StringBuilder visual = new StringBuilder();
//        for (char[] row : gridCopy) {
//            visual.append(row).append("\n");
//        }
//
//        return visual.toString();
//    }
    // Method to generate the path and tried visualization by copying the traversed graph and adding 'P' and '?'
    private String generatePathVisualization() {
        // Make a copy of the traversedGraphVisual
        String[] traversedLines = traversedGraphVisual.split("\n");
        char[][] gridCopy = new char[traversedLines.length][];
        
        for (int i = 0; i < traversedLines.length; i++) {
            gridCopy[i] = traversedLines[i].toCharArray();
        }

        // Replace the nodes and edges in the path with 'P' and tried nodes/edges with '?'
        if (!path.isEmpty()) {
            for (int i = 0; i < path.size(); i++) {
                Node currentNode = path.get(i);
                int row = currentNode.getRow();
                int col = currentNode.getColumn();

                // Transform the row and column for grid placement
                int transformedRow = transformCoordinate(row);
                int transformedCol = transformCoordinate(col);

                // Replace '.' with 'P' at the transformed position for nodes
                if (transformedRow < gridCopy.length && transformedCol < gridCopy[0].length) {
                    gridCopy[transformedRow][transformedCol] = 'P';
                }

                // Handle edges between consecutive nodes in the path
                if (i < path.size() - 1) {
                    Node nextNode = path.get(i + 1);
                    int nextRow = nextNode.getRow();
                    int nextCol = nextNode.getColumn();

                    // Transform next node's coordinates
                    int transformedNextRow = transformCoordinate(nextRow);
                    int transformedNextCol = transformCoordinate(nextCol);

                    // Calculate edge position between the two nodes
                    int edgeRow = (transformedRow + transformedNextRow) / 2;
                    int edgeCol = (transformedCol + transformedNextCol) / 2;

                    // Replace 'E' with 'P' if both nodes are part of the path
                    gridCopy[edgeRow][edgeCol] = 'P';
                }
            }
        }

        // Now handle the tried nodes and edges
        for (int i = 0; i < tried.size(); i++) {
            Node currentNode = tried.get(i);
            int row = currentNode.getRow();
            int col = currentNode.getColumn();

            // Transform the row and column for grid placement
            int transformedRow = transformCoordinate(row);
            int transformedCol = transformCoordinate(col);

            // Replace '.' with '?' at the transformed position for tried nodes
            if (transformedRow < gridCopy.length && transformedCol < gridCopy[0].length) {
                gridCopy[transformedRow][transformedCol] = '?';
            }

            // Handle edges between consecutive nodes in the tried list
            if (i < tried.size() - 1) {
                Node nextNode = tried.get(i + 1);
                int nextRow = nextNode.getRow();
                int nextCol = nextNode.getColumn();

                // Transform next node's coordinates
                int transformedNextRow = transformCoordinate(nextRow);
                int transformedNextCol = transformCoordinate(nextCol);

                // Calculate edge position between the two nodes
                int edgeRow = (transformedRow + transformedNextRow) / 2;
                int edgeCol = (transformedCol + transformedNextCol) / 2;

                // Replace 'E' with '?' if both nodes are part of the tried list
                gridCopy[edgeRow][edgeCol] = '?';
            }
        }

        // Convert the grid copy to a string representation
        StringBuilder visual = new StringBuilder();
        for (char[] row : gridCopy) {
            visual.append(row).append("\n");
        }

        return visual.toString();
    }
    // Method to update the path visualization based on the current path
    public void updatePathVisualization() {
        this.pathVisualization = generatePathVisualization();
    }
	// Method to compute the visualization of the maze
	private String computeVisualization() {
		int gridRows = 2 * rows - 1; // Grid rows: 2*rows-1
		int gridCols = 2 * columns - 1; // Grid cols: 2*columns-1
		char[][] grid = new char[gridRows][gridCols];

		// Initialize grid with walls (#) and empty spaces (.)
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridCols; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					grid[i][j] = '.'; // Place nodes
				} else {
					grid[i][j] = '#'; // Wall by default
				}
			}
		}

		// Replace the first node from the path with 'P'
		if (!path.isEmpty()) {
			Node firstNode = path.get(0); // Get the first node in the path
			int row = firstNode.getRow();
			int col = firstNode.getColumn();

			// Transform the row and column for grid placement
			int transformedRow = transformCoordinate(row);
			int transformedCol = transformCoordinate(col);

			// Replace '.' with 'P' at the transformed position
//			System.out.println(transformedRow + "," + transformedCol);
			grid[transformedRow][transformedCol] = 'P';
		}

		// Convert the grid to a string representation
		StringBuilder visual = new StringBuilder();
		for (char[] row : grid) {
			visual.append(row).append("\n");
		}

		return visual.toString();
	}

	// Method to initialize path and tried lists for solving the maze
	private void initializeSolvingLists() {
		this.path = new ArrayList<>();
		this.pathEdges = new ArrayList<>();
		this.tried = new ArrayList<>();
		this.triedEdges = new ArrayList<>();
	}

	// Method to compute the visualization of the maze
	public void updateVisualization() {
		int gridRows = 2 * rows - 1; // Grid rows: 2*rows-1
		int gridCols = 2 * columns - 1; // Grid cols: 2*columns-1
		char[][] grid = new char[gridRows][gridCols];

		// Initialize grid with walls (#) and empty spaces (.)
		for (int i = 0; i < gridRows; i++) {
			for (int j = 0; j < gridCols; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					grid[i][j] = '.'; // Place nodes
				} else {
					grid[i][j] = '#'; // Wall by default
				}
			}
		}

		// Replace the first node from the path with 'P'
		if (!path.isEmpty()) {
			Node firstNode = path.get(0); // Get the first node in the path
			int row = firstNode.getRow();
			int col = firstNode.getColumn();

			// Transform the row and column for grid placement
			int transformedRow = transformCoordinate(row);
			int transformedCol = transformCoordinate(col);

			// Replace '.' with 'P' at the transformed position
//			System.out.println("Placing P at: " + transformedRow + "," + transformedCol);
			grid[transformedRow][transformedCol] = 'P';
		}

		// Convert the grid to a string representation
		StringBuilder visual = new StringBuilder();
		for (char[] row : grid) {
			visual.append(row).append("\n");
		}

		this.visualization = visual.toString();
	}

    // Method to generate nodes based on the rows and columns
    private List<Node> generateNodes() {
        List<Node> nodeList = new ArrayList<>();
        int nodeNumber = 1;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                nodeList.add(new Node(row, column, nodeNumber));
                nodeNumber++;
            }
        }
        return nodeList;
    }

    // Helper method to get a node based on its row and column
    private Node getNode(int row, int column) {
        for (Node node : nodes) {
            if (node.getRow() == row && node.getColumn() == column) {
                return node;
            }
        }
        return null;
    }

	// Method to generate a grid graph where each node is connected to its neighbors
	private Map<Node, List<Node>> generateGridGraph() {
		Map<Node, List<Node>> gridGraph = new HashMap<>();

		for (Node node : nodes) {
			List<Node> neighbors = new ArrayList<>();

			// Check for possible neighbors (north, east, south, west)
			Node up = getNode(node.getRow() - 1, node.getColumn());
			Node down = getNode(node.getRow() + 1, node.getColumn());
			Node left = getNode(node.getRow(), node.getColumn() - 1);
			Node right = getNode(node.getRow(), node.getColumn() + 1);

			// Add valid neighbors to the list
			if (up != null)
				neighbors.add(up);
			if (down != null)
				neighbors.add(down);
			if (left != null)
				neighbors.add(left);
			if (right != null)
				neighbors.add(right);

			// Store the node and its connections
			gridGraph.put(node, neighbors);
		}

		return gridGraph;
	}

//	// Perform DFS to traverse the entire graph and keep track of traversed edges
//	private void performDFS() {
//		Set<Node> visited = new HashSet<>();
//		Deque<Node> stack = new ArrayDeque<>();
//		stack.push(startNode);
//		visited.add(startNode);
//
//		while (!stack.isEmpty()) {
//			Node current = stack.peek();
//			List<Node> neighbors = originalGraph.get(current);
//			List<Node> unvisitedNeighbors = new ArrayList<>();
//
//			// Find unvisited neighbors
//			for (Node neighbor : neighbors) {
//				if (!visited.contains(neighbor)) {
//					unvisitedNeighbors.add(neighbor);
//				}
//			}
//
//			if (unvisitedNeighbors.isEmpty()) {
//				stack.pop(); // Backtrack if no unvisited neighbors
//			} else {
//				// Select a random unvisited neighbor
//				Node chosenNeighbor = unvisitedNeighbors.get(new Random().nextInt(unvisitedNeighbors.size()));
//				visited.add(chosenNeighbor);
//				stack.push(chosenNeighbor);
//
//				// Add this edge (current -> chosenNeighbor) to the list of traversed edges
//				traversedEdges.add(new Edge(current, chosenNeighbor));
//			}
//		}
//	}
//	// Perform DFS to traverse the entire graph and keep track of traversed edges
//	private void performDFS() {
//	    Set<Node> visited = new HashSet<>();
//	    Deque<Node> stack = new ArrayDeque<>();
//	    stack.push(startNode);
//	    visited.add(startNode);
//
//	    while (!stack.isEmpty()) {
//	        Node current = stack.peek();
//	        List<Node> neighbors = originalGraph.get(current);
//	        List<Node> unvisitedNeighbors = new ArrayList<>();
//
//	        // Find unvisited neighbors
//	        for (Node neighbor : neighbors) {
//	            if (!visited.contains(neighbor)) {
//	                unvisitedNeighbors.add(neighbor);
//	            }
//	        }
//
//	        if (unvisitedNeighbors.isEmpty()) {
//	            stack.pop(); // Backtrack if no unvisited neighbors
//	        } else {
//	            // Select a random unvisited neighbor
//	            Node chosenNeighbor = unvisitedNeighbors.get(new Random().nextInt(unvisitedNeighbors.size()));
//	            visited.add(chosenNeighbor);
//	            stack.push(chosenNeighbor);
//
//	            // Add this edge (current -> chosenNeighbor) to the list of traversed edges
//	            traversedEdges.add(new Edge(current, chosenNeighbor));
//	        }
//	    }
//	}
	// Perform DFS using an iterative approach with a stack to improve randomness
	private void performDFS(Node startNode) {
	    Deque<Node> stack = new ArrayDeque<>();
	    Set<Node> visited = new HashSet<>();
	    
	    stack.push(startNode);
	    visited.add(startNode);
	    
	    while (!stack.isEmpty()) {
	        Node current = stack.peek();
	        List<Node> neighbors = originalGraph.get(current);
	        
	        // Shuffle neighbors to introduce randomness
	        Collections.shuffle(neighbors, rand);
	        
	        boolean hasUnvisitedNeighbor = false;
	        for (Node neighbor : neighbors) {
	            if (!visited.contains(neighbor)) {
	                visited.add(neighbor);
	                stack.push(neighbor);
	                
	                // Store the traversed edge
	                traversedEdges.add(new Edge(current, neighbor));
	                hasUnvisitedNeighbor = true;
	                break;
	            }
	        }
	        
	        // If no unvisited neighbors, backtrack
	        if (!hasUnvisitedNeighbor) {
	            stack.pop();
	        }
	    }
	}

    // Method to create the second graph (maze) based on traversed edges
    private Map<Node, List<Node>> createTraversedGraph() {
        Map<Node, List<Node>> mazeGraph = new HashMap<>();

        // Add the nodes and their traversed edges to the maze graph
        for (Edge edge : traversedEdges) {
            Node from = edge.getFrom();
            Node to = edge.getTo();

            mazeGraph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            mazeGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from); // Bidirectional edge
        }

        return mazeGraph;
    }

	// Solve the maze using NESW priority order
	public void solveMaze() {
		Set<Node> visited = new HashSet<>();
		dfsNESW(startNode, visited);
	}

	// DFS with NESW priority
	private boolean dfsNESW(Node current, Set<Node> visited) {
		path.add(current);
		visited.add(current);

		if (current.equals(goalNode)) {
			return true; // Goal node reached
		}

		// Get neighbors in NESW order
		List<Node> neighbors = getNESWNeighbors(current);

		for (Node neighbor : neighbors) {
			if (!visited.contains(neighbor)) {
				// Add the edge to the path
				pathEdges.add(new Edge(current, neighbor));

				boolean found = dfsNESW(neighbor, visited);
				if (found) {
					return true; // Goal reached, stop DFS
				}

				// Backtrack: move the node and edge to the tried lists
				path.remove(neighbor);
				tried.add(neighbor);
				Edge backtrackedEdge = new Edge(current, neighbor);
				pathEdges.remove(backtrackedEdge);
				triedEdges.add(backtrackedEdge);
			}
		}

		return false;
	}

	// Get neighbors in NESW priority order
	private List<Node> getNESWNeighbors(Node node) {
		List<Node> neighbors = new ArrayList<>();
		int row = node.getRow();
		int col = node.getColumn();

		// North, East, South, West (in this order)
		Node north = getNode(row - 1, col);
		Node east = getNode(row, col + 1);
		Node south = getNode(row + 1, col);
		Node west = getNode(row, col - 1);

		if (north != null && mazeGraph.getOrDefault(node, Collections.emptyList()).contains(north)) {
			neighbors.add(north);
		}
		if (east != null && mazeGraph.getOrDefault(node, Collections.emptyList()).contains(east)) {
			neighbors.add(east);
		}
		if (south != null && mazeGraph.getOrDefault(node, Collections.emptyList()).contains(south)) {
			neighbors.add(south);
		}
		if (west != null && mazeGraph.getOrDefault(node, Collections.emptyList()).contains(west)) {
			neighbors.add(west);
		}

		return neighbors;
	}

	// Print the solution path
	public void printPath() {
		System.out.println("Path to goal:");
		for (Node node : path) {
			System.out.print(node + " ");
		}
		System.out.println();

		System.out.println("Path edges:");
		for (Edge edge : pathEdges) {
			System.out.println(edge);
		}
	}

	// Print the tried nodes and edges (backtracked)
	public void printTried() {
		System.out.println("Tried nodes:");
		for (Node node : tried) {
			System.out.print(node + " ");
		}
		System.out.println();

		System.out.println("Tried edges:");
		for (Edge edge : triedEdges) {
			System.out.println(edge);
		}
	}

	// Edge class to store traversed edges
	private static class Edge {
		private final Node from;
		private final Node to;

		public Edge(Node from, Node to) {
			this.from = from;
			this.to = to;
		}

		public Node getFrom() {
			return from;
		}

		public Node getTo() {
			return to;
		}

		@Override
		public String toString() {
			return from + " -> " + to;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Edge edge = (Edge) o;
			return from.equals(edge.from) && to.equals(edge.to);
		}

		@Override
		public int hashCode() {
			return Objects.hash(from, to);
		}
	}

	// Print the original grid graph (before any traversal)
	public void printOriginalGraph() {
		System.out.println("Original Graph:");
		printGraph(originalGraph);
	}

	// Print the traversed graph (after DFS traversal)
	public void printTraversedGraph() {
		System.out.println("Traversed Graph (from DFS):");
		printGraph(mazeGraph); // mazeGraph contains the graph built from traversed edges
	}

	// Helper method to print a graph
	private void printGraph(Map<Node, List<Node>> graph) {
		for (Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
			Node node = entry.getKey();
			List<Node> neighbors = entry.getValue();
			System.out.print(node + " is connected to: ");
			for (Node neighbor : neighbors) {
				System.out.print(neighbor + " ");
			}
			System.out.println();
		}
	}

	// String visualizer for the graph
	public String generateGraphString(Map<Node, List<Node>> graph) {
		StringBuilder visual = new StringBuilder();

		// Iterate through rows
		for (int row = 0; row < rows; row++) {
			// First print node row
			for (int col = 0; col < columns; col++) {
				visual.append("."); // Node representation
				if (col < columns - 1) {
					Node currentNode = getNode(row, col);
					Node rightNode = getNode(row, col + 1);
					if (graph.getOrDefault(currentNode, Collections.emptyList()).contains(rightNode)) {
						visual.append("E"); // Edge exists horizontally
					} else {
						visual.append("#"); // No edge horizontally
					}
				}
			}
			visual.append("\n");

			// Then print the edges between rows if this isn't the last row
			if (row < rows - 1) {
				for (int col = 0; col < columns; col++) {
					Node currentNode = getNode(row, col);
					Node downNode = getNode(row + 1, col);
					if (graph.getOrDefault(currentNode, Collections.emptyList()).contains(downNode)) {
						visual.append("E"); // Edge exists vertically
					} else {
						visual.append("#"); // No edge vertically
					}

					if (col < columns - 1) {
						visual.append("#"); // Filler space between nodes
					}
				}
				visual.append("\n");
			}
		}

		return visual.toString();
	}

//    public static void main(String[] args) {
//        MazeFun mazeFun = new MazeFun(10,10);  // Create a 3x3 maze
////        mazeFun.printOriginalGraph();  // Print the original grid graph
////        System.out.println(mazeFun.getOriginalGraphVisual());
//        mazeFun.printTraversedGraph();  // Print the traversed graph with fewer edges
//        System.out.println(mazeFun.getTraversedGraphVisual());
//        mazeFun.solveMaze();  // Solve the maze using NESW priority order
//        mazeFun.printPath();   // Print the solution path
//        mazeFun.printTried();  // Print the backtracked nodes and edges
//    }
	// Method to generate the third graph with 'P' for path and '?' for tried
	// nodes/edges
	private String generatePathAndTriedGraph() {
		StringBuilder visual = new StringBuilder();

		// Iterate through rows
		for (int row = 0; row < rows; row++) {
			// First print node row
			for (int col = 0; col < columns; col++) {
				Node currentNode = getNode(row, col);
				if (isInPath(currentNode)) {
					visual.append("P"); // Path node
				} else if (isInTried(currentNode)) {
					visual.append("?"); // Tried node
				} else {
					visual.append("."); // Regular node
				}

				if (col < columns - 1) {
					// Compute the edge coordinate between current node and right node
					Node rightNode = getNode(row, col + 1);
					if (isInPathEdge(currentNode, rightNode)) {
						visual.append("P"); // Path edge
					} else if (isInTriedEdge(currentNode, rightNode)) {
						visual.append("?"); // Tried edge
					} else {
						visual.append("#"); // No edge
					}
				}
			}
			visual.append("\n");

			// Then print the edges between rows if this isn't the last row
			if (row < rows - 1) {
				for (int col = 0; col < columns; col++) {
					Node currentNode = getNode(row, col);
					Node downNode = getNode(row + 1, col);
					if (isInPathEdge(currentNode, downNode)) {
						visual.append("P"); // Path edge
					} else if (isInTriedEdge(currentNode, downNode)) {
						visual.append("?"); // Tried edge
					} else {
						visual.append("#"); // No edge
					}

					if (col < columns - 1) {
						visual.append("#"); // Filler between cells
					}
				}
				visual.append("\n");
			}
		}

		return visual.toString();
	}

	// Helper method to check if a node is part of the path
	private boolean isInPath(Node node) {
		return path.contains(node);
	}

	// Helper method to check if a node is part of the tried list (backtracked)
	private boolean isInTried(Node node) {
		return tried.contains(node);
	}

	// Helper method to check if an edge is part of the path
	private boolean isInPathEdge(Node node1, Node node2) {
		Edge edge = new Edge(node1, node2);
		return pathEdges.contains(edge);
	}

	// Helper method to check if an edge is part of the tried list (backtracked)
	private boolean isInTriedEdge(Node node1, Node node2) {
		Edge edge = new Edge(node1, node2);
		return triedEdges.contains(edge);
	}

//    public String getPathAndTriedGraphVisual() {
//        return pathAndTriedGraphVisual;
//    }

//    public static void main(String[] args) {
//        MazeFun mazeFun = new MazeFun(5, 5);  // Create a 5x5 maze
//
//        // Print the visual strings for the original, traversed, and path/tried graphs
//        System.out.println("Original Graph Visualizer:");
//        System.out.println(mazeFun.getOriginalGraphVisual());
//
//        System.out.println("Traversed Graph Visualizer:");
//        System.out.println(mazeFun.getTraversedGraphVisual());
//System.out.println("Visualization--");
//System.out.println(mazeFun.getVisualization());
//// Print the first three nodes from the path
//List<Node> path = mazeFun.getPath();
//
//if (path.size() >= 1) {
//    System.out.println("First node in path: " + path.get(0));
//    System.out.println("Second node in path: " + path.get(1));
//    System.out.println("Third node in path: " + path.get(2));
//} else {
//    System.out.println("The path contains fewer than 3 nodes.");
//}
////        System.out.println("Path and Tried Graph Visualizer:");
////        System.out.println(mazeFun.getPathAndTriedGraphVisual());
//System.out.println(path.size());
//    }
//    public static void main(String[] args) {
//        MazeFun mazeFun = new MazeFun(5, 5);  // Create a 5x5 maze
//
//        // Call solveMaze to populate the path
//        mazeFun.solveMaze();
//
//        // Print the visual strings for the original, traversed, and path/tried graphs
//        System.out.println("Original Graph Visualizer:");
//        System.out.println(mazeFun.getOriginalGraphVisual());
//
//        System.out.println("Traversed Graph Visualizer:");
//        System.out.println(mazeFun.getTraversedGraphVisual());
//
//        System.out.println("Visualization--");
//        System.out.println(mazeFun.getVisualization());
//
//        // Print the first three nodes from the path
//        List<Node> path = mazeFun.getPath();
//
//        if (path.size() >= 3) {
//            System.out.println("First node in path: " + path.get(0));
//            System.out.println("Second node in path: " + path.get(1));
//            System.out.println("Third node in path: " + path.get(2));
//        } else {
//            System.out.println("The path contains fewer than 3 nodes.");
//        }
//
//        // Print the size of the path
//        System.out.println("Path size: " + path.size());
//    }
//	public static void main(String[] args) {
//		MazeFun mazeFun = new MazeFun(5, 5); // Create a 5x5 maze
//		mazeFun.solveMaze(); // Solve the maze using NESW priority order
//
//		// Print the visualization
//		System.out.println("Maze Visualization:");
//		System.out.println(mazeFun.getVisualization());
//
//		// Print the first three nodes from the path
//		List<Node> path = mazeFun.getPath();
//		if (path.size() >= 3) {
//			System.out.println("First node in path: " + path.get(0));
//			System.out.println("Second node in path: " + path.get(1));
//			System.out.println("Third node in path: " + path.get(2));
//		} else {
//			System.out.println("The path contains fewer than 3 nodes.");
//		}
//
//		// Print the size of the path
//		System.out.println("Path size: " + path.size());
//	}
    public static void main(String[] args) {
        MazeFun mazeFun = new MazeFun(50, 50);  // Create a 5x5 maze
        
//		MazeFun mazeFun = new MazeFun(5, 5); // Create a 5x5 maze
//		mazeFun.solveMaze(); // Solve the maze using NESW priority order

		// Print the visualization
//		System.out.println("Maze Visualization:");
//		System.out.println(mazeFun.getTraversedGraphVisual());
//		mazeFun.solveMaze(); // Solve the maze using NESW priority order

//        // Now update the visualization after the path is populated
//        mazeFun.updateVisualization();
//
//        // Print the visualization
//        System.out.println("Maze Visualization:");
//        System.out.println(mazeFun.getVisualization());
        System.out.println(mazeFun.getPath());
//        System.out.println(mazeFun.getTraversedGraphVisual());
        System.out.println(mazeFun.getPathVisualization());
    }
	/**
	 * @return the originalGraphVisual
	 */
	public String getOriginalGraphVisual() {
		return originalGraphVisual;
	}

	/**
	 * @return the traversedGraphVisual
	 */
	public String getTraversedGraphVisual() {
		return traversedGraphVisual;
	}

	/**
	 * @return the visualization
	 */
	public String getVisualization() {
		return visualization;
	}

	/**
	 * @return the path
	 */
	public List<Node> getPath() {
		return path;
	}

	/**
	 * @return the pathVisualization
	 */
	public String getPathVisualization() {
		return pathVisualization;
	}

}
