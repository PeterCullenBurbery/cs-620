/**
 * @since 2024-W38-6 18.40.05.439 -0400
 * @author peter
 */
package com.peter_burbery.postfix_stack.postfix_stack;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.ArithmeticUtils; // For GCD and LCM
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * 
 */

public class PostfixEvaluator {

	// Regex patterns for functions (case-insensitive)
	private static final Pattern ADDITION = Pattern.compile("\\Q+\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern SUBTRACTION = Pattern.compile("\\Q-\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern MULTIPLICATION = Pattern.compile("\\Q*\\E|\\Qx\\E"); // Handles '*' and 'x' for
																						// multiplication
	private static final Pattern DIVISION = Pattern.compile("\\Q/\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern FLOOR_DIVISION = Pattern.compile("\\Q//\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern POWER = Pattern.compile("\\Q^\\E|\\Q**\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern MODULO = Pattern.compile("\\Q%\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern GCD = Pattern.compile("\\Qgcd\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern LCM = Pattern.compile("\\Qlcm\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern FRESHMAN = Pattern.compile("\\Qfreshman\\E|\\Qfreshman_product\\E",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern SIN = Pattern.compile("\\Qsin\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern COS = Pattern.compile("\\Qcos\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern TAN = Pattern.compile("\\Qtan\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern FACTORIAL = Pattern.compile("\\Qfactorial\\E|\\Q!\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern GAMMA = Pattern.compile("\\Qgamma\\E|\\QΓ\\E", Pattern.CASE_INSENSITIVE);
	// Unimplemented functions
	private static final Pattern BINOMIAL = Pattern.compile("\\Qbinomial\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern LOG = Pattern.compile("\\Qlog\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern POCHHAMMER = Pattern.compile("\\Qpochhammer\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern RISING_FACTORIAL = Pattern.compile("\\Qrising_factorial\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern FALLING_FACTORIAL = Pattern.compile("\\Qfalling_factorial\\E",
			Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String userInput;

		while (true) {
			System.out.println("Enter a space-delimited postfix expression or type 'Quit' to exit:");
			userInput = scanner.nextLine().trim();

			if (userInput.equalsIgnoreCase("Quit")) {
				System.out.println("Exiting program...");
				break;
			}

			try {
				double result = evaluatePostfix(userInput);
				System.out.println("Calculated value: " + result);
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid input: " + e.getMessage());
			}
		}

		scanner.close();
	}

	/**
	 * Evaluates a postfix expression.
	 * 
	 * @param expression the postfix expression as a space-delimited string
	 * @return the result of the postfix evaluation
	 */
	public static double evaluatePostfix(String expression) {
		MUStack<Double> stack = new MUStack<>();
		String[] tokens = expression.split(" ");

		for (String token : tokens) {
			// Check if the token is a number
			if (isNumeric(token)) {
				stack.push(Double.parseDouble(token));
			} else if (isUnaryOperator(token)) {
				// Handle unary operators
				if (stack.size() < 1) {
					throw new IllegalArgumentException("Not enough values in the stack for operation.");
				}
				double operand = stack.pop();
				double result = performUnaryOperation(token, operand);
				stack.push(result);
			} else {
				// Handle binary operators
				if (stack.size() < 2) {
					throw new IllegalArgumentException("Not enough values in the stack for operation.");
				}

				double operand2 = stack.pop();
				double operand1 = stack.pop();
				double result = performBinaryOperation(token, operand1, operand2);
				stack.push(result);
			}
		}

		if (stack.size() != 1) {
			throw new IllegalArgumentException("Invalid postfix expression.");
		}

		return stack.pop();
	}

	/**
	 * Determines if a string is a numeric value.
	 * 
	 * @param str the string to check
	 * @return true if the string is numeric, false otherwise
	 */
	private static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Determines if a string is a valid unary operator.
	 * 
	 * @param token the string to check
	 * @return true if the token is a unary operator, false otherwise
	 */
	private static boolean isUnaryOperator(String token) {
		return matches(SIN, token) || matches(COS, token) || matches(TAN, token) || matches(FACTORIAL, token)
				|| matches(GAMMA, token);
	}

	/**
	 * Performs the specified unary operation on one operand.
	 * 
	 * @param operator the unary operator (sin, cos, tan, factorial, gamma, etc.)
	 * @param operand  the operand to apply the operator to
	 * @return the result of the unary operation
	 */
	private static double performUnaryOperation(String operator, double operand) {
		if (matches(SIN, operator)) {
			return Math.sin(operand);
		} else if (matches(COS, operator)) {
			return Math.cos(operand);
		} else if (matches(TAN, operator)) {
			// Handle tan undefined for odd multiples of PI/2
			if (Math.abs(operand % Math.PI) == Math.PI / 2) {
				throw new IllegalArgumentException("Tangent is undefined at odd multiples of PI/2.");
			}
			return Math.tan(operand);
		} else if (matches(FACTORIAL, operator)) {
			if (!isInteger(operand) || operand < 0) {
				throw new IllegalArgumentException("Factorial is only defined for non-negative integers.");
			}
			return CombinatoricsUtils.factorialDouble((int) operand); // Factorial from Apache Commons Math
		} else if (matches(GAMMA, operator)) {
			if (operand <= 0) {
				throw new IllegalArgumentException("Gamma function is not defined for non-positive integers.");
			}
			return Gamma.gamma(operand);
		} else {
			throw new IllegalArgumentException("Unknown unary operator: " + operator);
		}
	}

	/**
	 * Performs the specified binary operation on two operands.
	 * 
	 * @param operator the operator (+, -, *, /, ^, %, gcd, lcm, freshman, //)
	 * @param operand1 the first operand
	 * @param operand2 the second operand
	 * @return the result of the operation
	 */
	/**
	 * Performs the specified binary operation on two operands.
	 * 
	 * @param operator the operator (+, -, *, /, ^, %, gcd, lcm, freshman, //)
	 * @param operand1 the first operand
	 * @param operand2 the second operand
	 * @return the result of the operation
	 */
	private static double performBinaryOperation(String operator, double operand1, double operand2) {
		// Check case-insensitive operators using regex patterns
		if (matches(ADDITION, operator)) {
			return operand1 + operand2;
		} else if (matches(SUBTRACTION, operator)) {
			return operand1 - operand2;
		} else if (matches(MULTIPLICATION, operator)) {
			return operand1 * operand2;
		} else if (matches(DIVISION, operator)) {
			if (operand2 == 0) {
				throw new IllegalArgumentException("Division by zero is not allowed.");
			}
			return operand1 / operand2;
		} else if (matches(FLOOR_DIVISION, operator)) {
			if (operand2 == 0) {
				throw new IllegalArgumentException("Division by zero is not allowed.");
			}
			return Math.floorDiv((int) operand1, (int) operand2); // Using Math.floorDiv for floor division
		} else if (matches(POWER, operator)) {
			return Math.pow(operand1, operand2);
		} else if (matches(MODULO, operator)) {
			return operand1 % operand2;
		} else if (matches(GCD, operator)) {
			return validateAndComputeGCD(operand1, operand2);
		} else if (matches(LCM, operator)) {
			return validateAndComputeLCM(operand1, operand2);
		} else if (matches(FRESHMAN, operator)) {
			return freshmanProduct((int) operand1, (int) operand2);
		} else if (matches(BINOMIAL, operator)) {
			throw new IllegalArgumentException("The binomial function has not been implemented yet.");
		} else if (matches(LOG, operator)) {
			throw new IllegalArgumentException("The logarithm (log) function has not been implemented yet.");
		} else if (matches(POCHHAMMER, operator)) {
			throw new IllegalArgumentException(
					"The Pochhammer function (rising factorial) has not been implemented yet.");
		} else if (matches(RISING_FACTORIAL, operator)) {
			throw new IllegalArgumentException("The rising factorial function has not been implemented yet.");
		} else if (matches(FALLING_FACTORIAL, operator)) {
			throw new IllegalArgumentException("The falling factorial function has not been implemented yet.");
		} else {
			throw new IllegalArgumentException("Unknown operator: " + operator);
		}
	}

	/**
	 * Helper method to check if an operator matches a pattern.
	 * 
	 * @param pattern  the regex pattern for the operator
	 * @param operator the operator to check
	 * @return true if the operator matches the pattern, false otherwise
	 */
	private static boolean matches(Pattern pattern, String operator) {
		Matcher matcher = pattern.matcher(operator);
		return matcher.matches();
	}

	/**
	 * Checks if the value is an integer (not a floating point).
	 * 
	 * @param value the value to check
	 * @return true if the value is an integer, false otherwise
	 */
	private static boolean isInteger(double value) {
		return value == Math.floor(value) && !Double.isInfinite(value);
	}

	/**
	 * Validates inputs and computes the GCD using Apache Commons Math.
	 * 
	 * @param operand1 first operand
	 * @param operand2 second operand
	 * @return the GCD of operand1 and operand2
	 */
	private static double validateAndComputeGCD(double operand1, double operand2) {
		if (!isInteger(operand1) || !isInteger(operand2)) {
			throw new IllegalArgumentException("GCD requires integer inputs.");
		}
		if (operand1 == 0 && operand2 == 0) {
			throw new IllegalArgumentException("GCD is undefined when both numbers are zero.");
		}

		// GCD works with absolute values of the operands
		return ArithmeticUtils.gcd(Math.abs((int) operand1), Math.abs((int) operand2));
	}

	/**
	 * Validates inputs and computes the LCM using Apache Commons Math.
	 * 
	 * @param operand1 first operand
	 * @param operand2 second operand
	 * @return the LCM of operand1 and operand2
	 */
	private static double validateAndComputeLCM(double operand1, double operand2) {
		if (!isInteger(operand1) || !isInteger(operand2)) {
			throw new IllegalArgumentException("LCM requires integer inputs.");
		}
		if (operand1 == 0 || operand2 == 0) {
			throw new IllegalArgumentException("LCM is undefined for zero values.");
		}

		// LCM works with absolute values of the operands
		return ArithmeticUtils.lcm(Math.abs((int) operand1), Math.abs((int) operand2));
	}

	/**
	 * Computes the Freshman's product of two integers. The Freshman's product is
	 * the last digit of the product of each corresponding digit.
	 * 
	 * @param a first integer
	 * @param b second integer
	 * @return the Freshman's product of a and b
	 */
	private static int freshmanProduct(int a, int b) {
		int result = 0;
		int multiplier = 1;

		while (a > 0 || b > 0) {
			int digitA = a % 10;
			int digitB = b % 10;

			// Multiply the corresponding digits and get the last digit
			int product = (digitA * digitB) % 10;

			// Build the result
			result += product * multiplier;
			multiplier *= 10;

			// Move to the next digits
			a /= 10;
			b /= 10;
		}

		return result;
	}
}