/**
 * @since 2024-W38-6 18.40.12.869 -0400
 * @author peter
 */
package com.peter_burbery.postfix_stack.postfix_stack;

/**
 * 
 */
import java.util.Random;

public class RandomIntegerObject {
	private int lowerBound;
	private int upperBound;
	private Random random;

	/* Constructor for a random object with specified upper bound */
	public RandomIntegerObject(int upperBound) {
		this.lowerBound = 1; // default lower bound
		this.upperBound = upperBound;
		this.random = new Random();
	}

	/* Constructor for a random object with both lower and upper bounds */
	public RandomIntegerObject(int lowerBound, int upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.random = new Random();
	}

	/* Generates a random number between lowerBound and upperBound (inclusive) */
	public int generate() {
		return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
	}

	/* Getters for lower and upper bounds */
	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}
}
