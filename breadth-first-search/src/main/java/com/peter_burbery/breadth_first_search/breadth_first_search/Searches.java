/**
 * @since 2024-W39-3 20.53.23.918 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */

import com.peter_burbery.mu_queue.mu_queue.MUQueue;

public class Searches {

	public static void BFS(Vertex[] nodes, boolean[][] adjacency, int n, int start) {
		MUQueue<Vertex> myQueue = new MUQueue<>();
		myQueue.enqueue(nodes[start]);
		nodes[start].setVisited(true);

		System.out.println("Starting BFS traversal from node: " + start);

		// Track levels in BFS using a queue separator (null or custom marker)
		myQueue.enqueue(null); // Add level separator
		int level = 1; // Start with level 1

		while (!myQueue.isEmpty()) {
			Vertex current = myQueue.dequeue();

			// If we hit a level separator and the queue is not empty, we know we are moving
			// to the next level
			if (current == null) {
				if (!myQueue.isEmpty()) {
					myQueue.enqueue(null); // Add another level separator
					System.out.println("\n--- Level " + level + " ---");
					level++;
				}
				continue;
			}

			// Process the current node
			int toVisit = current.getIndex();
			System.out.println("Finished visiting node " + toVisit);

			// Add adjacent unvisited nodes to the queue
			for (int j = 0; j < n; j++) {
				if (adjacency[toVisit][j] && !nodes[j].getVisited()) {
					myQueue.enqueue(nodes[j]);
					nodes[j].setVisited(true);
				}
			}
		}
	}
}
