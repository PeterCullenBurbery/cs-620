package com.peter_burbery.mu_queue_git.mu_queue_git;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
import org.apache.commons.text.StringSubstitutor;

public class App {
	public static void main(String[] args) {
		// Create a new MUQueue instance
		MUQueue<Integer> queue = new MUQueue<>();

		// Create a Map and StringSubstitutor for dynamic string substitution
		Map<String, String> valuesMap = new HashMap<>();
		StringSubstitutor sub = new StringSubstitutor(valuesMap);

		// Enqueue some elements
		System.out.println("Enqueueing elements: 1, 2, 3");
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		// Check the size of the queue
		System.out.println("\nChecking the size of the queue:");
		valuesMap.put("queueSize", String.valueOf(queue.size()));
		String sizeTemplate = "Queue size: ${queueSize}";
		System.out.println(sub.replace(sizeTemplate));

		// Peek the front element
		valuesMap.put("frontElement", String.valueOf(queue.peek()));
		String peekTemplate = "Peeking front element: ${frontElement}";
		System.out.println(sub.replace(peekTemplate));

		// Dequeue an element
		valuesMap.put("dequeuedElement", String.valueOf(queue.dequeue()));
		String dequeueTemplate = "Dequeuing element: ${dequeuedElement}";
		System.out.println(sub.replace(dequeueTemplate));

		// Check the front again
		valuesMap.put("frontElement", String.valueOf(queue.peek()));
		String peekAgainTemplate = "Peeking front element after dequeue: ${frontElement}";
		System.out.println(sub.replace(peekAgainTemplate));

		// Dequeue all elements
		valuesMap.put("dequeuedElement", String.valueOf(queue.dequeue()));
		System.out.println(sub.replace(dequeueTemplate));

		valuesMap.put("dequeuedElement", String.valueOf(queue.dequeue()));
		System.out.println(sub.replace(dequeueTemplate));

		// Check if queue is empty
		valuesMap.put("isEmpty", String.valueOf(queue.isEmpty()));
		String emptyTemplate = "Is queue empty? ${isEmpty}";
		System.out.println(sub.replace(emptyTemplate));
	}
}
