/**
 * @since 2024-W38-6 18.39.58.763 -0400
 * @author peter
 */
package com.peter_burbery.postfix_stack.postfix_stack;

/**
 * 
 */
public class MUStack<E> {

	/* Inner class for a node in the stack */
	private class Node<E> {
		private E data;
		Node<E> next;

		/* next node. is null if the next node is null. */
		public Node(E data) {
			this.data = data;
			this.next = null;
		}
	}

	private Node<E> top;

	private int count;

	/* The number of elements in the stack. */
	/*
	 * Constructor. The constructor takes no arguments because the stack starts
	 * empty.
	 */
	public MUStack() {
		this.top = null;
		this.count = 0;
	}

	/* Pushes an item onto the top of the stack */
	public void push(E item) {
		Node<E> newNode = new Node<>(item);
		newNode.next = top;
		top = newNode;
		count++;
	}

	/* Pops (removes) the top item off the stack and returns it */
	public E pop() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty. cannot pop.");
		}
		E data = top.data;
		top = top.next;
		count--;
		return data;
	}

	/* Returns (peeks) the top item of the stack without removing it */
	public E peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty. cannot peek.");
		}
		return top.data;
	}

	/* Checks if the stack is empty */
	public boolean isEmpty() {
		return top == null;
	}

	/* Returns the size (number of elements) in the stack */
	public int size() {
		return count;
	}

	/*
	 * toString method to print elements vertically, one per line.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node<E> current = top;
		while (current != null) {
			sb.append(current.data).append("\n"); // Each element on a new line
			current = current.next;
		}
		return sb.toString();
	}

	/*
	 * toStringHorizontal method to print elements horizontally, separated by
	 * commas.
	 */
	public String toStringHorizontal() {
		StringBuilder sb = new StringBuilder();
		Node<E> current = top;
		while (current != null) {
			sb.append(current.data);
			current = current.next;
			if (current != null) {
				sb.append(", "); // Add commas between elements
			}
		}
		return sb.toString();
	}
}
