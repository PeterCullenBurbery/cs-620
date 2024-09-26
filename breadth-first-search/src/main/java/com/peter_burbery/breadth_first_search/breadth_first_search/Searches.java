/**
 * @since 2024-W39-3 20.53.23.918 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */
import java.util.LinkedList;
import java.util.Queue;

public class Searches {

	public static void BFS(Vertex[] nodes, boolean[][] adjacency, int n, int start) {
		Queue<Vertex> myQueue = new LinkedList<>();
		myQueue.add(nodes[start]);
		nodes[start].setVisited(true);

		System.out.println("Starting BFS traversal from node: " + start);

		// Track levels in BFS using a queue separator (null or custom marker)
		myQueue.add(null); // Add level separator
		while (!myQueue.isEmpty()) {
			Vertex current = myQueue.poll();

			// If we hit a level separator and the queue is not empty, we know we are moving
			// to the next level
			if (current == null) {
				if (!myQueue.isEmpty()) {
					myQueue.add(null); // Add another level separator
					System.out.println("\n--- Moving to next level ---");
				}
				continue;
			}

			// Process the current node
			int toVisit = current.getIndex();
			System.out.println("Finished visiting node " + toVisit);

			// Add adjacent unvisited nodes to the queue
			for (int j = 0; j < n; j++) {
				if (adjacency[toVisit][j] && !nodes[j].getVisited()) {
					myQueue.add(nodes[j]);
					nodes[j].setVisited(true);
				}
			}
		}
	}
}

