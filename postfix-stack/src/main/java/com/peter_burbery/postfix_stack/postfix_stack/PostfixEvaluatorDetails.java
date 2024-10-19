/**
 * @since 2024-W41-1 12.32.27.824 -0400
 * @author peter
 */
package com.peter_burbery.postfix_stack.postfix_stack;

/**
 * 
 */

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.ArithmeticUtils; // For GCD and LCM
import org.apache.commons.math3.util.CombinatoricsUtils;

public class PostfixEvaluatorDetails {

	// Regex patterns for functions (case-insensitive)
	private static final Pattern ADDITION = Pattern.compile("\\Q+\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern SUBTRACTION = Pattern.compile("\\Q-\\E", Pattern.CASE_INSENSITIVE);
	private static final Pattern MULTIPLICATION = Pattern.compile("\\Q*\\E|\\Qx\\E");
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

	public static double evaluatePostfix(String expression) {
		MUStack<Double> stack = new MUStack<>();
		String[] tokens = expression.split(" ");

		for (String token : tokens) {
			System.out.println("Processing token: " + token);

			if (isNumeric(token)) {
				stack.push(Double.parseDouble(token));
				System.out.println("Pushed " + token + " onto the stack.");
			} else if (isUnaryOperator(token)) {
				if (stack.size() < 1) {
					throw new IllegalArgumentException("Not enough values in the stack for operation.");
				}
				double operand = stack.pop();
				double result = performUnaryOperation(token, operand);
				stack.push(result);
				System.out
						.println("Performed unary operation " + token + " on " + operand + ", pushed result " + result);
			} else {
				if (stack.size() < 2) {
					throw new IllegalArgumentException("Not enough values in the stack for operation.");
				}
				double operand2 = stack.pop();
				double operand1 = stack.pop();
				double result = performBinaryOperation(token, operand1, operand2);
				stack.push(result);
				System.out.println("Performed binary operation " + token + " on " + operand1 + " and " + operand2
						+ ", pushed result " + result);
			}

			System.out.println("Current stack: " + stack.toStringHorizontal());
		}

		if (stack.size() != 1) {
			throw new IllegalArgumentException("Invalid postfix expression.");
		}

		return stack.pop();
	}

	private static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isUnaryOperator(String token) {
		return matches(SIN, token) || matches(COS, token) || matches(TAN, token) || matches(FACTORIAL, token)
				|| matches(GAMMA, token);
	}

	private static double performUnaryOperation(String operator, double operand) {
		if (matches(SIN, operator)) {
			return Math.sin(operand);
		} else if (matches(COS, operator)) {
			return Math.cos(operand);
		} else if (matches(TAN, operator)) {
			if (Math.abs(operand % Math.PI) == Math.PI / 2) {
				throw new IllegalArgumentException("Tangent is undefined at odd multiples of PI/2.");
			}
			return Math.tan(operand);
		} else if (matches(FACTORIAL, operator)) {
			if (!isInteger(operand) || operand < 0) {
				throw new IllegalArgumentException("Factorial is only defined for non-negative integers.");
			}
			return CombinatoricsUtils.factorialDouble((int) operand);
		} else if (matches(GAMMA, operator)) {
			if (operand <= 0) {
				throw new IllegalArgumentException("Gamma function is not defined for non-positive integers.");
			}
			return Gamma.gamma(operand);
		} else {
			throw new IllegalArgumentException("Unknown unary operator: " + operator);
		}
	}

	private static double performBinaryOperation(String operator, double operand1, double operand2) {
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
			return Math.floorDiv((int) operand1, (int) operand2);
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
		} else {
			throw new IllegalArgumentException("Unknown operator: " + operator);
		}
	}

	private static boolean matches(Pattern pattern, String operator) {
		Matcher matcher = pattern.matcher(operator);
		return matcher.matches();
	}

	private static boolean isInteger(double value) {
		return value == Math.floor(value) && !Double.isInfinite(value);
	}

	private static double validateAndComputeGCD(double operand1, double operand2) {
		if (!isInteger(operand1) || !isInteger(operand2)) {
			throw new IllegalArgumentException("GCD requires integer inputs.");
		}
		if (operand1 == 0 && operand2 == 0) {
			throw new IllegalArgumentException("GCD is undefined when both numbers are zero.");
		}
		return ArithmeticUtils.gcd(Math.abs((int) operand1), Math.abs((int) operand2));
	}

	private static double validateAndComputeLCM(double operand1, double operand2) {
		if (!isInteger(operand1) || !isInteger(operand2)) {
			throw new IllegalArgumentException("LCM requires integer inputs.");
		}
		if (operand1 == 0 || operand2 == 0) {
			throw new IllegalArgumentException("LCM is undefined for zero values.");
		}
		return ArithmeticUtils.lcm(Math.abs((int) operand1), Math.abs((int) operand2));
	}

	private static int freshmanProduct(int a, int b) {
		int result = 0;
		int multiplier = 1;
		while (a > 0 || b > 0) {
			int digitA = a % 10;
			int digitB = b % 10;
			int product = (digitA * digitB) % 10;
			result += product * multiplier;
			multiplier *= 10;
			a /= 10;
			b /= 10;
		}
		return result;
	}
}