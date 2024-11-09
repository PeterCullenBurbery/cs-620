package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;
import java.util.*;
public class KnapsackData {
    private int capacity;
    private List<KnapsackItem> items;

    // Getters and setters
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<KnapsackItem> getItems() {
        return items;
    }

    public void setItems(List<KnapsackItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "KnapsackData{" +
                "capacity=" + capacity +
                ", items=" + items +
                '}';
    }
}
