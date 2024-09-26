/**
 * @since 2024-W39-3 20.53.17.888 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */
public class Graph {
    private int[][] adjacencyMatrix;
    private int numVertices;

    // Constructor to initialize the graph with an adjacency matrix
    public Graph(int[][] adjacencyMatrix) {
        this.numVertices = adjacencyMatrix.length;
        this.adjacencyMatrix = new int[numVertices][numVertices];

        // Copying the adjacency matrix
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                this.adjacencyMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
    }

    // Method to get the adjacency matrix
    public int[][] getAdjacencyMatrix() {
        return this.adjacencyMatrix;
    }

    // Method to print the adjacency matrix
    public void printAdjacencyMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Additional methods can be added for graph traversal (BFS, DFS), etc.
}

