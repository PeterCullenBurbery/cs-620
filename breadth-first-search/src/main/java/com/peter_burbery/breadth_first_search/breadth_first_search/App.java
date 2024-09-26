package com.peter_burbery.breadth_first_search.breadth_first_search;

import com.peter_burbery.mu_queue.mu_queue.MUQueue;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		// Create a new MUQueue of Integers
		MUQueue<Integer> queue = new MUQueue<>();

		// Add elements to the queue
		queue.enqueue(10);
		queue.enqueue(20);
		queue.enqueue(30);

		// Dequeue and print each element
		System.out.println("Dequeue: " + queue.dequeue()); // Expected output: 10
		System.out.println("Dequeue: " + queue.dequeue()); // Expected output: 20
		System.out.println("Dequeue: " + queue.dequeue()); // Expected output: 30

		// Check if the queue is empty after all dequeues
		System.out.println("Is the queue empty? " + queue.isEmpty()); // Expected output: true
	}
}
