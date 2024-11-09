package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;
import java.util.*;
public class Knapsack {
    public List<Integer> solveKnapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        // Fill DP table
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(values[i - 1] + dp[i - 1][w - weights[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        // Backtrack to find selected items
        List<Integer> selectedItems = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selectedItems.add(i - 1);
                w -= weights[i - 1];
            }
        }
        
        return selectedItems;
    }

    public void printSolution(int[] weights, int[] values, int capacity, List<Integer> selectedItems) {
        int totalValue = 0;
        System.out.println("Selected Items:");
        for (int idx : selectedItems) {
            System.out.println("Item " + idx + ": Weight=" + weights[idx] + ", Value=" + values[idx]);
            totalValue += values[idx];
        }
        System.out.println("Total Value: " + totalValue);
        System.out.println("Number of Items Selected: " + selectedItems.size());
    }
}
