package mazes_005;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class MazeFun {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';
	private static final char PATH = 'P';
	private static final char BACKTRACK = '?';

	private int rows;
	private int cols;
	private List<Node> nodes; // List of nodes in the maze
	private int[][] adjacencyMatrix;
	private Set<String> traversedEdges; // Store the traversed edges
	private boolean[] visited;
	private Random rand; // Randomizer for shuffling neighbors
	private String unmodifiedGrid; // Stores grid before DFS and edge removal
	private String modifiedGrid; // Stores grid after DFS and edge removal with S and G
	private String solve; // Stores the final solved grid with 'P' and '?'
	private Node startNode; // Start node
	private Node goalNode; // Goal node
	private Map<Node, List<Node>> edges; // Adjacency list for DFS

	// Constructor to initialize the grid graph
	public MazeFun(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new ArrayList<>();
		this.adjacencyMatrix = new int[rows * cols][rows * cols];
		this.traversedEdges = new HashSet<>();
		this.visited = new boolean[rows * cols];
		this.rand = new Random(); // Initialize the random object
		this.edges = new HashMap<>();

		// Create nodes and set up connections in the adjacency matrix
		createNodes();
		createConnections();

		// Generate the grid before DFS
		this.unmodifiedGrid = generateGridGraph(); // Grid before DFS

		// Start DFS from node 0
		dfs(startNode);

		// Remove untraversed edges
		removeUntraversedEdges();

		// Generate the modified grid after edge removal and add 'S' and 'G'
		this.modifiedGrid = generateGridGraph(); // Generate the modified grid
		this.modifiedGrid = addStartAndGoal(this.modifiedGrid); // Add S and G to the modified grid

		// Solve the maze and store the grid with 'P' and '?' markings
		this.solve = solveMaze(); // Solve the maze and mark the path
	}
	// DFS method to traverse the graph using nodes with randomized neighbor selection
	private void dfs(Node node) {
	    visited[node.getNodeNumber() - 1] = true;  // Mark the node as visited

	    // Get the neighbors of the current node
	    List<Node> neighbors = getSortedNeighbors(node, new HashSet<>(Arrays.asList(node)));

	    // Shuffle the neighbors to introduce randomness
	    Collections.shuffle(neighbors, rand);  // Use the Random object to shuffle the neighbors

	    for (Node neighbor : neighbors) {
	        if (!visited[neighbor.getNodeNumber() - 1]) {
	            // Store the traversed edge
	            traversedEdges.add(node.getNodeNumber() + "-" + neighbor.getNodeNumber());
	            traversedEdges.add(neighbor.getNodeNumber() + "-" + node.getNodeNumber());

	            // Recursive DFS call
	            dfs(neighbor);
	        }
	    }
	}

	// Function to create the list of nodes
	private void createNodes() {
		int nodeCount = 1;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (row == rows - 1 && col == 0) {
					startNode = new Node(row, col, nodeCount); // Start node
				} else if (row == 0 && col == cols - 1) {
					goalNode = new Node(row, col, nodeCount); // Goal node
				}
				Node node = new Node(row, col, nodeCount);
				nodes.add(node);
				edges.put(node, new ArrayList<>());
				nodeCount++;
			}
		}
	}

	// Function to create the adjacency matrix
	private void createConnections() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int currentNodeIndex = nodeIndex(row, col); // Get the current node's index
				Node currentNode = nodes.get(currentNodeIndex);

				// Connect to the right (if not at the last column)
				if (col < cols - 1) {
					int rightNodeIndex = nodeIndex(row, col + 1);
					Node rightNode = nodes.get(rightNodeIndex);
					adjacencyMatrix[currentNodeIndex][rightNodeIndex] = 1;
					edges.get(currentNode).add(rightNode);
					edges.get(rightNode).add(currentNode); // Add both directions
				}

				// Connect to the bottom (if not at the last row)
				if (row < rows - 1) {
					int bottomNodeIndex = nodeIndex(row + 1, col);
					Node bottomNode = nodes.get(bottomNodeIndex);
					adjacencyMatrix[currentNodeIndex][bottomNodeIndex] = 1;
					edges.get(currentNode).add(bottomNode);
					edges.get(bottomNode).add(currentNode); // Add both directions
				}
			}
		}
	}

	// Function to get the node's index based on its row and column
	private int nodeIndex(int row, int col) {
		return row * cols + col;
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

	// Method to replace the lower-left corner with 'S' and the top-right corner
	// with 'G' in the modified grid
	private String addStartAndGoal(String grid) {
		String[] lines = grid.split("\n");
		StringBuilder modifiedGrid = new StringBuilder();

		// Replace the bottom-left corner with 'S'
		lines[lines.length - 1] = 'S' + lines[lines.length - 1].substring(1);

		// Replace the top-right corner with 'G'
		int lastLineIndex = lines[0].length() - 1;
		lines[0] = lines[0].substring(0, lastLineIndex) + 'G';

		// Rebuild the grid string
		for (String line : lines) {
			modifiedGrid.append(line).append("\n");
		}

		return modifiedGrid.toString().trim(); // Remove the last newline character
	}

	// Function to generate the grid graph as a string representation
	private String generateGridGraph() {
		StringBuilder grid = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Add node
				grid.append(NODE);
				if (col < cols - 1) {
					int rightNode = nodeIndex(row, col + 1);
					int currentNode = nodeIndex(row, col);
					// Add edge or wall between nodes
					if (adjacencyMatrix[currentNode][rightNode] == 1) {
						grid.append(EDGE);
					} else {
						grid.append(WALL);
					}
				}
			}
			grid.append("\n");
			if (row < rows - 1) {
				for (int col = 0; col < cols; col++) {
					int bottomNode = nodeIndex(row + 1, col);
					int currentNode = nodeIndex(row, col);
					// Add edge or wall between nodes
					if (adjacencyMatrix[currentNode][bottomNode] == 1) {
						grid.append(EDGE);
					} else {
						grid.append(WALL);
					}
					if (col < cols - 1) {
						grid.append(WALL); // Wall between rows
					}
				}
				grid.append("\n");
			}
		}
		return grid.toString().trim();
	}

	// Function to solve the maze by marking the path and backtrack using DFS
	private String solveMaze() {
		Set<Node> visitedNodes = new HashSet<>();
		Stack<Node> dfsStack = new Stack<>();
		StringBuilder solvedGrid = new StringBuilder(modifiedGrid);
		dfsStack.push(startNode);

		visitedNodes.add(startNode); // Mark the start node as visited

		while (!dfsStack.isEmpty()) {
			Node currentNode = dfsStack.peek();

			// If we reach the goal node, stop searching
			if (currentNode.equals(goalNode)) {
				break; // Goal is reached, exit the loop
			}

			List<Node> neighbors = getSortedNeighbors(currentNode, visitedNodes);

			boolean foundUnvisited = false;
			for (Node neighbor : neighbors) {
				if (!visitedNodes.contains(neighbor)) {
					dfsStack.push(neighbor); // Traverse the neighbor
					visitedNodes.add(neighbor); // Mark neighbor as visited
					markPath(currentNode, neighbor, solvedGrid, PATH); // Mark path with 'P'
					foundUnvisited = true;
					break;
				}
			}

			if (!foundUnvisited) {
				Node backtrackNode = dfsStack.pop(); // Backtrack
				if (!backtrackNode.equals(startNode)) {
					markPath(currentNode, backtrackNode, solvedGrid, BACKTRACK); // Mark backtracked path with '?'
				}
			}
		}

		return solvedGrid.toString(); // Return the solved maze grid as a string
	}

	// Sort neighbors in the priority order (north, east, south, west)
	// Sort neighbors in the priority order: north, east, south, west
	private List<Node> getSortedNeighbors(Node node, Set<Node> visitedNodes) {
	    List<Node> neighbors = edges.get(node);
	    List<Node> sortedNeighbors = new ArrayList<>();

	    // Get the current node's row and column
	    int row = node.getRow();
	    int col = node.getCol();

	    // Iterate through the neighbors and add them in the order: north, east, south, west
	    for (Node neighbor : neighbors) {
	        if (visitedNodes.contains(neighbor)) {
	            continue; // Skip already visited nodes
	        }

	        int neighborRow = neighbor.getRow();
	        int neighborCol = neighbor.getCol();

	        // Check if the neighbor is north of the current node
	        if (neighborRow < row && neighborCol == col) {
	            sortedNeighbors.add(neighbor);
	        }
	    }

	    for (Node neighbor : neighbors) {
	        if (visitedNodes.contains(neighbor)) {
	            continue;
	        }

	        int neighborRow = neighbor.getRow();
	        int neighborCol = neighbor.getCol();

	        // Check if the neighbor is east of the current node
	        if (neighborRow == row && neighborCol > col) {
	            sortedNeighbors.add(neighbor);
	        }
	    }

	    for (Node neighbor : neighbors) {
	        if (visitedNodes.contains(neighbor)) {
	            continue;
	        }

	        int neighborRow = neighbor.getRow();
	        int neighborCol = neighbor.getCol();

	        // Check if the neighbor is south of the current node
	        if (neighborRow > row && neighborCol == col) {
	            sortedNeighbors.add(neighbor);
	        }
	    }

	    for (Node neighbor : neighbors) {
	        if (visitedNodes.contains(neighbor)) {
	            continue;
	        }

	        int neighborRow = neighbor.getRow();
	        int neighborCol = neighbor.getCol();

	        // Check if the neighbor is west of the current node
	        if (neighborRow == row && neighborCol < col) {
	            sortedNeighbors.add(neighbor);
	        }
	    }

	    return sortedNeighbors;
	}


	// Mark the path in the solved grid
	private void markPath(Node current, Node next, StringBuilder grid, char marker) {
		// Calculate the positions of current node, next node, and the edge between them
		int currentRow = current.getRow();
		int currentCol = current.getCol();
		int nextRow = next.getRow();
		int nextCol = next.getCol();

		// Mark the nodes with 'P' or '?' (current and next)
		int currentNodePos = getGridIndex(currentRow, currentCol);
		int nextNodePos = getGridIndex(nextRow, nextCol);
		grid.setCharAt(currentNodePos, marker);
		grid.setCharAt(nextNodePos, marker);

		// Mark the edge between them with 'P' or '?'
		int edgeRow = (currentRow + nextRow) / 2;
		int edgeCol = (currentCol + nextCol) / 2;
		int edgePos = getGridIndex(edgeRow, edgeCol);
		grid.setCharAt(edgePos, marker);
	}

	// Utility to get the index in the grid string based on row and column
	private int getGridIndex(int row, int col) {
		return row * (cols * 2 - 1) + col;
	}

	// Getter for unmodified grid
	public String getUnmodifiedGrid() {
		return this.unmodifiedGrid;
	}

	// Getter for modified grid
	public String getModifiedGrid() {
		return this.modifiedGrid;
	}

	// Getter for solved grid
	public String getSolvedGrid() {
		return this.solve;
	}

	public static void main(String[] args) {
		// Create a 15x15 grid graph and automatically perform DFS and edge removal
		MazeFun gridGraph15x15 = new MazeFun(7,7);
System.out.println(gridGraph15x15.getModifiedGrid());
//		// Print the solved grid after DFS and path marking
//		System.out.println("Solved Grid with P and ?:");
//		System.out.println(gridGraph15x15.getSolvedGrid());
	}
}