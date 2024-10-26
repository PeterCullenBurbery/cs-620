package first_try;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class Main {
    private static final int K = 3;
    private static final int MAX_ITERATIONS = 100;

    public static void main(String[] args) throws IOException {
        List<IrisDataPoint> data = loadIrisData("C:/Users/peter/git/cs-620/iris.txt");

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

        printClusterResults(clusters);
    }

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

    private static List<Cluster> initializeClusters(List<IrisDataPoint> data) {
        Random random = new Random();
        List<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            double[] centroid = data.get(random.nextInt(data.size())).features.clone();
            clusters.add(new Cluster(centroid));
        }
        return clusters;
    }

    private static void assignPointsToClusters(List<IrisDataPoint> data, List<Cluster> clusters) {
        for (IrisDataPoint point : data) {
            Cluster closestCluster = clusters.get(0);
            double minDistance = calculateDistance(point.features, closestCluster.getCentroid());
            for (Cluster cluster : clusters) {
                double distance = calculateDistance(point.features, cluster.getCentroid());
                if (distance < minDistance) {
                    closestCluster = cluster;
                    minDistance = distance;
                }
            }
            closestCluster.addPoint(point);
        }
    }

    private static boolean updateCentroids(List<Cluster> clusters) {
        boolean centroidsChanged = false;
        for (Cluster cluster : clusters) {
            double[] oldCentroid = cluster.getCentroid().clone();
            cluster.updateCentroid();
            if (!Arrays.equals(oldCentroid, cluster.getCentroid())) {
                centroidsChanged = true;
            }
        }
        return centroidsChanged;
    }

    private static double calculateDistance(double[] point, double[] centroid) {
        double sum = 0;
        for (int i = 0; i < point.length; i++) {
            sum += Math.pow(point[i] - centroid[i], 2);
        }
        return Math.sqrt(sum);
    }

    private static void printClusterResults(List<Cluster> clusters) {
        System.out.println("Cluster Composition by Species:");
        for (int i = 0; i < clusters.size(); i++) {
            Map<String, Integer> speciesCount = new HashMap<>();
            for (IrisDataPoint point : clusters.get(i).points) {
                speciesCount.put(point.species, speciesCount.getOrDefault(point.species, 0) + 1);
            }
            System.out.printf("Cluster %d: %s%n", i + 1, speciesCount);
        }
    }
}
