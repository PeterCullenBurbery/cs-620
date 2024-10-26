package com.peter_burbery.clustering.com.peter_burbery.clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Hello world!
 */
public class App {
	private static final int K = 3;
    private static final int MAX_ITERATIONS = 100;
    private static final int NUM_RUNS = 10;

    public static void main(String[] args) {
        try {
            List<IrisDataPoint> data = loadIrisData("C:/Users/peter/git/cs-620/iris.txt");

            double bestInertia = Double.MAX_VALUE;
            List<Cluster> bestClusters = null;

            for (int run = 0; run < NUM_RUNS; run++) {
                List<Cluster> clusters = initializeClusters(data);

                for (int i = 0; i < MAX_ITERATIONS; i++) {
                    for (Cluster cluster : clusters) {
                        cluster.clearPoints();
                    }
                    assignPointsToClusters(data, clusters);
                    boolean centroidsChanged = updateCentroids(clusters);
                    if (!centroidsChanged) {
                        break;
                    }
                }

                double inertia = calculateInertia(clusters);
                if (inertia < bestInertia) {
                    bestInertia = inertia;
                    bestClusters = clusters;
                }
            }

            printClusterResults(bestClusters);

        } catch (IOException e) {
            System.out.println("Error reading data file: " + e.getMessage());
        }
    }
    // Loads Iris dataset from file
    private static List<IrisDataPoint> loadIrisData(String filePath) throws IOException {
        List<IrisDataPoint> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            double[] features = new double[4];
            for (int i = 0; i < 4; i++) {
                features[i] = Double.parseDouble(parts[i]);
            }
            String species = parts[4];
            data.add(new IrisDataPoint(features, species));
        }
        br.close();
        return data;
    }

    // Initializes clusters with random centroids from the data
    private static List<Cluster> initializeClusters(List<IrisDataPoint> data) {
        Random random = new Random();
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            double[] centroid = data.get(random.nextInt(data.size())).features.clone();
            clusters.add(new Cluster(centroid));
        }
        return clusters;
    }

    // Assigns each point to the closest cluster
    private static void assignPointsToClusters(List<IrisDataPoint> data, List<Cluster> clusters) {
        for (IrisDataPoint point : data) {
            Cluster closestCluster = clusters.get(0);
            double minDistance = calculateDistance(point.features, closestCluster.centroid);
            for (Cluster cluster : clusters) {
                double distance = calculateDistance(point.features, cluster.centroid);
                if (distance < minDistance) {
                    closestCluster = cluster;
                    minDistance = distance;
                }
            }
            closestCluster.addPoint(point);
        }
    }

    // Updates the centroid of each cluster
    private static boolean updateCentroids(List<Cluster> clusters) {
        boolean centroidsChanged = false;
        for (Cluster cluster : clusters) {
            double[] oldCentroid = cluster.centroid.clone();
            cluster.updateCentroid();
            if (!Arrays.equals(oldCentroid, cluster.centroid)) {
                centroidsChanged = true;
            }
        }
        return centroidsChanged;
    }

    // Calculates Euclidean distance between two points
    private static double calculateDistance(double[] point, double[] centroid) {
        double sum = 0;
        for (int i = 0; i < point.length; i++) {
            sum += Math.pow(point[i] - centroid[i], 2);
        }
        return Math.sqrt(sum);
    }

    private static void printClusterResults(List<Cluster> clusters) {
        // Initialize species counts for each cluster and totals
        int[][] counts = new int[clusters.size()][3]; // Rows are clusters, columns are species
        int[] totalCounts = new int[3]; // Total counts for each species across all clusters

        String[] speciesLabels = {"Setosa", "Versicolor", "Virginica"};

        // Map each species name to a column index
        Map<String, Integer> speciesIndex = new HashMap<>();
        speciesIndex.put("Iris-setosa", 0);
        speciesIndex.put("Iris-versicolor", 1);
        speciesIndex.put("Iris-virginica", 2);

        // Count species in each cluster
        for (int i = 0; i < clusters.size(); i++) {
            for (IrisDataPoint point : clusters.get(i).points) {
                int index = speciesIndex.get(point.species);
                counts[i][index]++;
                totalCounts[index]++;
            }
        }

        // Print the header
        System.out.printf("%-10s %-10s %-10s %-10s %-10s%n", "CLUSTER", "Setosa", "Versicolor", "Virginica", "Total");

        // Print each cluster's row
        for (int i = 0; i < clusters.size(); i++) {
            int clusterTotal = counts[i][0] + counts[i][1] + counts[i][2];
            System.out.printf("%-10d %-10d %-10d %-10d %-10d%n", i + 1, counts[i][0], counts[i][1], counts[i][2], clusterTotal);
        }

        // Print the totals row
        int grandTotal = totalCounts[0] + totalCounts[1] + totalCounts[2];
        System.out.printf("%-10s %-10d %-10d %-10d %-10d%n", "Total", totalCounts[0], totalCounts[1], totalCounts[2], grandTotal);
    }


    private static double calculateInertia(List<Cluster> clusters) {
        double inertia = 0;
        for (Cluster cluster : clusters) {
            for (IrisDataPoint point : cluster.points) {
                inertia += calculateDistance(point.features, cluster.centroid);
            }
        }
        return inertia;
    }
}
