package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;

import java.util.List;

public class KnapsackResult {
    private List<Integer> selectedItems;
    private int totalValue;
    private int numberOfItems;

    public KnapsackResult(List<Integer> selectedItems, int totalValue, int numberOfItems) {
        this.selectedItems = selectedItems;
        this.totalValue = totalValue;
        this.numberOfItems = numberOfItems;
    }

    // Getters and Setters
    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Integer> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }
}
