package com.peter_burbery.dijkstra.com.peter_burbery.dijkstra;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		// Create a graph with 7 vertices (a=0, b=1, c=2, d=3, e=4, f=5, z=6)
		Graph graph = new Graph(7);

		// Add edges based on the provided graph
		graph.addEdge(0, 1, 4); // a -> b
		graph.addEdge(0, 2, 3); // a -> c
		graph.addEdge(1, 5, 5); // b -> f
		graph.addEdge(1, 4, 12); // b -> e
		graph.addEdge(2, 4, 10); // c -> e
		graph.addEdge(2, 3, 7); // c -> d
		graph.addEdge(3, 4, 2); // d -> e
		graph.addEdge(4, 6, 5); // e -> z
		graph.addEdge(5, 6, 16); // f -> z

		// Run Dijkstra from source vertex 0 (vertex 'a')
		DijkstraUtil.dijkstra(graph, 0);
	}
}
