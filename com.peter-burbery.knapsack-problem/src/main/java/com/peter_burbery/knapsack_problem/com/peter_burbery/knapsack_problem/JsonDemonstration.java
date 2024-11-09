package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
public class JsonDemonstration {
	public static void main(String[] args) {
        try {
            // Parse JSON file from resources folder
            ObjectMapper objectMapper = new ObjectMapper();

            // Load file from resources using the class loader
            InputStream inputStream = JsonDemonstration.class.getClassLoader().getResourceAsStream("knapsack.json");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! knapsack.json");
            }

            KnapsackData knapsackData = objectMapper.readValue(inputStream, KnapsackData.class);

            int capacity = knapsackData.getCapacity();
            List<KnapsackItem> items = knapsackData.getItems();

            int[] weights = items.stream().mapToInt(KnapsackItem::getWeight).toArray();
            int[] values = items.stream().mapToInt(KnapsackItem::getValue).toArray();

            // Solve Knapsack problem
            Knapsack knapsack = new Knapsack();
            List<Integer> selectedItems = knapsack.solveKnapsack(weights, values, capacity);
            knapsack.printSolution(weights, values, capacity, selectedItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
