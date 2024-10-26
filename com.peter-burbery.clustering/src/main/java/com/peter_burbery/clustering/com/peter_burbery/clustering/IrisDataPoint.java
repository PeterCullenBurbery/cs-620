package com.peter_burbery.clustering.com.peter_burbery.clustering;

class IrisDataPoint {
    double[] features; // petal length, petal width, sepal length, sepal width
    String species;

    IrisDataPoint(double[] features, String species) {
        this.features = features;
        this.species = species;
    }
}
