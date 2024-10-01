package com.peter_burbery.postfix_stack.postfix_stack;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		StackDemonstration stackDemonstration = new StackDemonstration();

		System.out.println("Demonstration in Vertical Format:");
		stackDemonstration.demonstrate("vertical", 5, new RandomIntegerObject(1000));

		System.out.println("\nDemonstration in Horizontal Format:");
		stackDemonstration.demonstrate("horizontal", 5, new RandomIntegerObject(-1000, 1000));

	}
}
