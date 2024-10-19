package mazes_002;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;



public class GridGraphVisualizer {

    private static final char EDGE = 'E';
    private static final char NODE = '.';
    private static final char WALL = '#';

    private int rows;
    private int cols;
    private List<Integer> nodes;
    private int[][] adjacencyMatrix;
    private Set<String> traversedEdges; // Store the traversed edges
    private boolean[] visited;
    private Random rand; // Randomizer for shuffling neighbors

    // Constructor to initialize the grid graph
    public GridGraphVisualizer(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.nodes = new ArrayList<>();
        this.adjacencyMatrix = new int[rows * cols][rows * cols];
        this.traversedEdges = new HashSet<>();
        this.visited = new boolean[rows * cols];
        this.rand = new Random(); // Initialize the random object

        // Create nodes and set up connections in the adjacency matrix
        createNodes();
        createConnections();
    }

    // Function to create the list of nodes
    private void createNodes() {
        int nodeCount = 1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                nodes.add(nodeCount);
                nodeCount++;
            }
        }
    }

    // Function to create the adjacency matrix
    private void createConnections() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int currentNode = nodeIndex(row, col);  // Get the current node's index

                // Connect to the right (if not at the last column)
                if (col < cols - 1) {
                    int rightNode = nodeIndex(row, col + 1);
                    adjacencyMatrix[currentNode][rightNode] = 1;
                    adjacencyMatrix[rightNode][currentNode] = 1;
                }

                // Connect to the bottom (if not at the last row)
                if (row < rows - 1) {
                    int bottomNode = nodeIndex(row + 1, col);
                    adjacencyMatrix[currentNode][bottomNode] = 1;
                    adjacencyMatrix[bottomNode][currentNode] = 1;
                }
            }
        }
    }

    // Function to get the node's index based on its row and column
    private int nodeIndex(int row, int col) {
        return row * cols + col;
    }

    // DFS function to traverse the graph with random neighbor selection
    public void dfs(int node) {
        visited[node] = true;

        // Get the neighbors of the current node
        List<Integer> neighbors = getNeighbors(node);

        // Shuffle the neighbors randomly
        Collections.shuffle(neighbors, rand);

        // Explore each neighbor randomly
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                // Store the edge (both directions)
                traversedEdges.add(node + "-" + neighbor);
                traversedEdges.add(neighbor + "-" + node);

                // Traverse the neighbor
                dfs(neighbor);
            }
        }
    }

    // Function to get the neighbors of a node
    private List<Integer> getNeighbors(int node) {
        List<Integer> neighbors = new ArrayList<>();
        for (int neighbor = 0; neighbor < adjacencyMatrix[node].length; neighbor++) {
            if (adjacencyMatrix[node][neighbor] == 1) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // Function to remove untraversed edges from the adjacency matrix
    public void removeUntraversedEdges() {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                String edgeKey = i + "-" + j;
                if (adjacencyMatrix[i][j] == 1 && !traversedEdges.contains(edgeKey)) {
                    // Remove the edge
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }
    }

    // Function to print the grid graph (before or after)
    public void printGridGraph() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Print the node
                System.out.print(NODE);
                if (col < cols - 1) {
                    int rightNode = nodeIndex(row, col + 1);
                    int currentNode = nodeIndex(row, col);
                    // Print edge or wall between nodes
                    if (adjacencyMatrix[currentNode][rightNode] == 1) {
                        System.out.print(EDGE);
                    } else {
                        System.out.print(WALL);
                    }
                }
            }
            System.out.println();
            if (row < rows - 1) {
                for (int col = 0; col < cols; col++) {
                    int bottomNode = nodeIndex(row + 1, col);
                    int currentNode = nodeIndex(row, col);
                    // Print edge or wall between nodes
                    if (adjacencyMatrix[currentNode][bottomNode] == 1) {
                        System.out.print(EDGE);
                    } else {
                        System.out.print(WALL);
                    }
                    if (col < cols - 1) {
                        System.out.print(WALL); // Wall between rows
                    }
                }
                System.out.println();
            }
        }
    }


}
