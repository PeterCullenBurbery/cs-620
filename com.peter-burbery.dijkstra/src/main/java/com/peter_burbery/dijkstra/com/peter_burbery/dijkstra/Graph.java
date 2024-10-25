package com.peter_burbery.dijkstra.com.peter_burbery.dijkstra;

import java.util.Arrays;

public class Graph {
    private int[][] adjacencyMatrix;
    private int vertices;

    // Constructor for the Graph
    public Graph(int vertices) {
        this.vertices = vertices;
        adjacencyMatrix = new int[vertices][vertices];

        // Initialize the adjacency matrix with zero or infinity
        for (int[] row : adjacencyMatrix) {
            Arrays.fill(row, Integer.MAX_VALUE); // Use Integer.MAX_VALUE to represent no connection
        }
    }

    // Add an edge to the graph
    public void addEdge(int source, int destination, int weight) {
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight; // Because it's an undirected graph
    }

    // Get the adjacency matrix
    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public int getVertices() {
        return vertices;
    }
}

