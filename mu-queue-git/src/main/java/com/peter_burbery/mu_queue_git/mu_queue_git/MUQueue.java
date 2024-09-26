/**
 * @since 2024-W39-3 20.36.21.711 -0400
 * @author peter
 */
package com.peter_burbery.mu_queue_git.mu_queue_git;

/**
 * 
 */
public class MUQueue<E> {

	// Private inner class Node to store queue elements
	private class Node<E> {
		private E data;
		private Node<E> next;

		public Node(E data) {
			this.data = data;
			this.next = null;
		}
	}

	// Member variables for the front, rear, and size of the queue
	private Node<E> front, rear;
	private int size;

	// Constructor
	public MUQueue() {
		this.front = null;
		this.rear = null;
		this.size = 0;
	}

	// Method to add an element to the queue
	public void enqueue(E data) {
		Node<E> newNode = new Node<>(data);

		if (rear == null) {
			// If queue is empty, front and rear are the same
			front = rear = newNode;
		} else {
			// Add the new node at the end and change the rear pointer
			rear.next = newNode;
			rear = newNode;
		}
		size++;
	}

	// Method to remove and return the front element of the queue
	public E dequeue() {
		if (front == null) {
			return null; // Queue is empty
		}

		E dequeuedData = front.data;
		front = front.next;

		if (front == null) {
			// If queue becomes empty after dequeue
			rear = null;
		}

		size--;
		return dequeuedData;
	}

	// Method to return the front element without removing it
	public E peek() {
		if (front == null) {
			return null; // Queue is empty
		}
		return front.data;
	}

	// Method to return the size of the queue
	public int size() {
		return size;
	}

	// Method to check if the queue is empty
	public boolean isEmpty() {
		return size == 0;
	}
}
