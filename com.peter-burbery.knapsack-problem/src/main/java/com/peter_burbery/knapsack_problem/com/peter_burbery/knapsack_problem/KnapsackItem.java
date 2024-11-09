package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;

public class KnapsackItem {
    private int weight;
    private int value;

    // Getters and setters
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KnapsackItem{" +
                "weight=" + weight +
                ", value=" + value +
                '}';
    }
}

