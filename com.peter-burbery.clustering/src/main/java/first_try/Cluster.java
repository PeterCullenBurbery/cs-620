package first_try;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cluster {
    List<IrisDataPoint> points;
    double[] centroid;

    Cluster(double[] centroid) {
        this.centroid = centroid;
        this.points = new ArrayList<>();
    }

    void addPoint(IrisDataPoint point) {
        points.add(point);
    }

    void clearPoints() {
        points.clear();
    }

    double[] getCentroid() {
        return centroid;
    }

    void updateCentroid() {
        int numFeatures = centroid.length;
        Arrays.fill(centroid, 0);
        for (IrisDataPoint point : points) {
            for (int i = 0; i < numFeatures; i++) {
                centroid[i] += point.features[i];
            }
        }
        for (int i = 0; i < numFeatures; i++) {
            centroid[i] /= points.size();
        }
    }
}
