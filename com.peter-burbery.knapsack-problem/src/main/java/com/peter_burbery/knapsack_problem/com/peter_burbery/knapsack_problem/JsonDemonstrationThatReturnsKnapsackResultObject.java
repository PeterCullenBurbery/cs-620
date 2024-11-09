package com.peter_burbery.knapsack_problem.com.peter_burbery.knapsack_problem;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;

public class JsonDemonstrationThatReturnsKnapsackResultObject {
    public static void main(String[] args) {
        try {
            // Parse JSON file from resources folder
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream inputStream = JsonDemonstrationThatReturnsKnapsackResultObject.class.getClassLoader().getResourceAsStream("knapsack.json");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! knapsack.json");
            }

            KnapsackData knapsackData = objectMapper.readValue(inputStream, KnapsackData.class);

            int capacity = knapsackData.getCapacity();
            List<KnapsackItem> items = knapsackData.getItems();

            int[] weights = items.stream().mapToInt(KnapsackItem::getWeight).toArray();
            int[] values = items.stream().mapToInt(KnapsackItem::getValue).toArray();

            // Solve Knapsack problem using the new class
            KnapsackThatReturnsKnapsackResult knapsack = new KnapsackThatReturnsKnapsackResult();
            KnapsackResult result = knapsack.solveKnapsack(weights, values, capacity);

            // Serialize result to JSON and print
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
