/**
 * @since 2024-W39-3 20.53.28.951 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */
public class SearchTest {
	public static void main(String[] args) {
		// Create the graph using the adjacency matrix defined inside the Graph class
		int n = 10; // Number of nodes (0 to 9)

		// Define the adjacency matrix for the graph
		boolean[][] adjacencyMatrix = new boolean[n][n];

		adjacencyMatrix[0][1] = true;
		adjacencyMatrix[1][0] = true;

		adjacencyMatrix[0][3] = true;
		adjacencyMatrix[3][0] = true;

		adjacencyMatrix[1][2] = true;
		adjacencyMatrix[2][1] = true;

		adjacencyMatrix[1][4] = true;
		adjacencyMatrix[4][1] = true;

		adjacencyMatrix[1][6] = true;
		adjacencyMatrix[6][1] = true;

		adjacencyMatrix[1][7] = true;
		adjacencyMatrix[7][1] = true;

		adjacencyMatrix[2][3] = true;
		adjacencyMatrix[3][2] = true;

		adjacencyMatrix[2][8] = true;
		adjacencyMatrix[8][2] = true;

		adjacencyMatrix[2][9] = true;
		adjacencyMatrix[9][2] = true;

		adjacencyMatrix[4][5] = true;
		adjacencyMatrix[5][4] = true;

		adjacencyMatrix[4][6] = true;
		adjacencyMatrix[6][4] = true;

		adjacencyMatrix[4][7] = true;
		adjacencyMatrix[7][4] = true;

		// Create a Graph object with the adjacency matrix
		Graph graph = new Graph(adjacencyMatrix);

		// Define the vertices of the graph
		Vertex[] nodes = new Vertex[10];
		for (int i = 0; i < 10; i++) {
			nodes[i] = new Vertex(i);
		}

		// Print the adjacency matrix to verify the graph
		graph.printAdjacencyMatrix();

		// Perform BFS starting from node 0 using the adjacency matrix from the Graph
		// object
		Searches.BFS(nodes, graph.getAdjacencyMatrix(), 10, 0);
	}
}
