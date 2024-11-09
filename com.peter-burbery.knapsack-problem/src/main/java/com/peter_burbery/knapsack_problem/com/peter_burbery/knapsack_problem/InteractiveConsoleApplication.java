package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;

import java.util.List;
import java.util.Scanner;

public class InteractiveConsoleApplication {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter knapsack capacity: ");
        int capacity = scanner.nextInt();
        
        System.out.print("Enter number of items: ");
        int n = scanner.nextInt();
        
        int[] weights = new int[n];
        int[] values = new int[n];
        
        System.out.println("Enter weights:");
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }
        
        System.out.println("Enter values:");
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }
        
        Knapsack knapsack = new Knapsack();
        List<Integer> selectedItems = knapsack.solveKnapsack(weights, values, capacity);
        knapsack.printSolution(weights, values, capacity, selectedItems);
        
        scanner.close();
    }
}
