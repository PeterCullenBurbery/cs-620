package com.peter_burbery.dijkstra.com.peter_burbery.dijkstra;

import java.util.Arrays;

public class DijkstraUtil {

    public static void dijkstra(Graph graph, int source) {
        int vertices = graph.getVertices();
        int[] dist = new int[vertices];
        int[] prev = new int[vertices];
        boolean[] visited = new boolean[vertices];

        Arrays.fill(dist, Integer.MAX_VALUE); // Initialize distances to infinity
        Arrays.fill(prev, -1); // No previous node initially
        dist[source] = 0; // Distance to the source is 0

        for (int i = 0; i < vertices; i++) {
            // Get the vertex with the minimum distance
            int u = getMinDistanceVertex(dist, visited);

            // Mark the vertex as visited
            visited[u] = true;

            // Explore neighbors of u
            for (int v = 0; v < vertices; v++) {
                if (!visited[v] && graph.getAdjacencyMatrix()[u][v] != Integer.MAX_VALUE) {
                    int newDist = dist[u] + graph.getAdjacencyMatrix()[u][v];
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                    }
                }
            }
        }

        // Output the result
        System.out.println("Vertex\tDistance from Source\tPrevious Vertex");
        for (int i = 0; i < vertices; i++) {
            System.out.println(i + "\t\t" + dist[i] + "\t\t\t" + prev[i]);
        }
    }

    // Helper method to get the vertex with the minimum distance
    private static int getMinDistanceVertex(int[] dist, boolean[] visited) {
        int minDist = Integer.MAX_VALUE;
        int minVertex = -1;

        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < minDist) {
                minDist = dist[i];
                minVertex = i;
            }
        }

        return minVertex;
    }
}

