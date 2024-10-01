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
        int[][] adjacencyMatrix = new int[n][n];

        adjacencyMatrix[0][1] = 1;
        adjacencyMatrix[1][0] = 1;

        adjacencyMatrix[0][3] = 1;
        adjacencyMatrix[3][0] = 1;

        adjacencyMatrix[1][2] = 1;
        adjacencyMatrix[2][1] = 1;

        adjacencyMatrix[1][4] = 1;
        adjacencyMatrix[4][1] = 1;

        adjacencyMatrix[1][6] = 1;
        adjacencyMatrix[6][1] = 1;

        adjacencyMatrix[1][7] = 1;
        adjacencyMatrix[7][1] = 1;

        adjacencyMatrix[2][3] = 1;
        adjacencyMatrix[3][2] = 1;

        adjacencyMatrix[2][8] = 1;
        adjacencyMatrix[8][2] = 1;

        adjacencyMatrix[2][9] = 1;
        adjacencyMatrix[9][2] = 1;

        adjacencyMatrix[4][5] = 1;
        adjacencyMatrix[5][4] = 1;

        adjacencyMatrix[4][6] = 1;
        adjacencyMatrix[6][4] = 1;

        adjacencyMatrix[4][7] = 1;
        adjacencyMatrix[7][4] = 1;

        // Create a Graph object with the adjacency matrix
        Graph graph = new Graph(adjacencyMatrix);

        // Define the vertices of the graph
        Vertex[] nodes = new Vertex[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Vertex(i);
        }

        // Print the adjacency matrix to verify the graph
        graph.printAdjacencyMatrix();

        // Perform BFS starting from node 0
        System.out.println("\nPerforming BFS:");
        Searches.BFS(nodes, graph.getAdjacencyMatrix(), n, 0);

        // Reset visited status for all nodes for DFS
        for (int i = 0; i < n; i++) {
            nodes[i].setVisited(false);
        }

        // Perform DFS starting from node 0
        System.out.println("\nPerforming DFS:");
        Searches.DFS(nodes, graph.getAdjacencyMatrix(), n, 0);
    }
}