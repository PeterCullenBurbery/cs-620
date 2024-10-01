/**
 * @since 2024-W39-3 20.53.23.918 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */

import com.peter_burbery.mu_queue.mu_queue.MUQueue;
import com.peter_burbery.postfix_stack.postfix_stack.MUStack;


public class Searches {

    public static void BFS(Vertex[] nodes, int[][] adjacency, int n, int start) {
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
                if (adjacency[toVisit][j] == 1 && !nodes[j].getVisited()) {
                    myQueue.enqueue(nodes[j]);
                    nodes[j].setVisited(true);
                }
            }
        }
    }
    // Depth-First Search (DFS) method with backtracking
 // Depth-First Search (DFS) method with backtracking
    public static void DFS(Vertex[] nodes, int[][] adjacency, int n, int start) {
        MUStack<Vertex> stack = new MUStack<>();
        stack.push(nodes[start]);
        nodes[start].setVisited(true);

        System.out.println("Starting DFS traversal from node: " + start);

        while (!stack.isEmpty()) {
            Vertex current = stack.peek(); // Look at the top of the stack without removing it yet
            int toVisit = current.getIndex();

            boolean foundUnvisitedNeighbor = false;

            // Explore neighbors of the current node
            for (int j = 0; j < n; j++) {
                if (adjacency[toVisit][j] == 1 && !nodes[j].getVisited()) {
                    stack.push(nodes[j]); // Push the unvisited neighbor onto the stack
                    nodes[j].setVisited(true); // Mark the neighbor as visited
                    System.out.println("Visiting node " + j + " from node " + toVisit);
                    foundUnvisitedNeighbor = true; // Found a new node to visit
                    break; // Break the loop and explore deeper
                }
            }

            // If no unvisited neighbors were found, we backtrack by popping the stack
            if (!foundUnvisitedNeighbor) {
                stack.pop(); // Backtrack by removing the current node
                if (!stack.isEmpty()) {
                    // Peek the new top of the stack to know where we're backtracking to
                    System.out.println("Backtracking to node " + stack.peek().getIndex() + " from node " + toVisit);
                } else {
                    // If the stack is empty, we're back at the start
                    System.out.println("Backtracking to start from node " + toVisit);
                }
            }
        }
    }
}
