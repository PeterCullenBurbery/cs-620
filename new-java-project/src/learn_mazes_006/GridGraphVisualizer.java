package learn_mazes_006;

import java.util.*;

public class GridGraphVisualizer {
    private static final char EDGE = 'E';
    private static final char NODE = '.';
    private static final char WALL = '#';
    private static final char PATH_CHAR = 'P';  // For path nodes/edges
    private static final char TRIED_CHAR = '?'; // For tried nodes/edges

    private int rows;
    private int cols;
    private List<Node> nodes;
    private Map<Node, List<Node>> adjacencyList;
    private Set<String> traversedEdges;
    private Set<Node> visited;
    private List<Node> path; // Final solution path
    private List<Node> triedNodes; // Nodes that were tried and backtracked
    private List<String> pathEdges; // Edges in the final solution path
    private List<String> triedEdges; // Edges that were tried and backtracked
    private Random rand;

    private Node startNode; // The starting node (bottom-left)
    private Node goalNode;  // The goal node (top-right)

    public GridGraphVisualizer(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.nodes = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.traversedEdges = new HashSet<>();
        this.visited = new HashSet<>();
        this.path = new ArrayList<>();
        this.triedNodes = new ArrayList<>();
        this.pathEdges = new ArrayList<>();
        this.triedEdges = new ArrayList<>();
        this.rand = new Random();

        // Create nodes and set up adjacency list
        createNodes();
        createConnections();

        // Define the start and goal nodes
        this.startNode = getNode(rows - 1, 0);  // Bottom-left corner
        this.goalNode = getNode(0, cols - 1);   // Top-right corner
    }

    private void createNodes() {
        int nodeCount = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node node = new Node(row, col, nodeCount);
                nodes.add(node);
                adjacencyList.put(node, new ArrayList<>());
                nodeCount++;
            }
        }
    }

    private void createConnections() {
        for (Node node : nodes) {
            int row = node.getRow();
            int col = node.getColumn();

            // Add connections (right and bottom neighbors)
            Node rightNeighbor = getNode(row, col + 1);
            Node bottomNeighbor = getNode(row + 1, col);

            if (rightNeighbor != null) {
                addEdge(node, rightNeighbor);
            }
            if (bottomNeighbor != null) {
                addEdge(node, bottomNeighbor);
            }
        }
    }

    private void addEdge(Node node1, Node node2) {
        adjacencyList.get(node1).add(node2);
        adjacencyList.get(node2).add(node1);
    }

    private Node getNode(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return nodes.get(row * cols + col);
        }
        return null;
    }

    public boolean dfs(Node current) {
        visited.add(current);
        path.add(current);  // Add current node to path

        if (current.equals(goalNode)) {
            return true;
        }

        List<Node> neighbors = new ArrayList<>(adjacencyList.get(current));
        Collections.shuffle(neighbors, rand);

        for (Node neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                String edge = current + "-" + neighbor;
                traversedEdges.add(edge);
                traversedEdges.add(neighbor + "-" + current);

                boolean found = dfs(neighbor);
                if (found) {
                    pathEdges.add(edge);  // Add to path edges
                    return true;
                } else {
                    triedEdges.add(edge);  // Mark as backtracked edge
                    triedNodes.add(neighbor);  // Mark neighbor as tried
                }
            }
        }

        return false;
    }

    public void solveMaze() {
        dfs(startNode);
    }

    public void printPath() {
        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path to goal:");
            for (Node node : path) {
                System.out.print(node + " ");
            }
            System.out.println();
        }
    }

    public void printTriedNodes() {
        System.out.println("Tried (backtracked) nodes:");
        for (Node node : triedNodes) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    public void printTriedEdges() {
        System.out.println("Tried (backtracked) edges:");
        for (String edge : triedEdges) {
            System.out.println(edge);
        }
    }

    public void printOriginalMaze() {
        System.out.println("Original Maze:");
        printMaze(traversedEdges);
    }

    public void printSolvedMaze() {
        System.out.println("Solved Maze (with path and backtracked edges):");
        printMazeWithSolution();
    }

    private void printMaze(Set<String> edges) {
        for (int row = 0; row < rows; row++) {
            // Print nodes and horizontal edges
            for (int col = 0; col < cols; col++) {
                System.out.print(NODE);
                if (col < cols - 1) {
                    String edge = getNode(row, col) + "-" + getNode(row, col + 1);
                    if (edges.contains(edge)) {
                        System.out.print(EDGE);
                    } else {
                        System.out.print(WALL);
                    }
                }
            }
            System.out.println();
            // Print vertical edges
            if (row < rows - 1) {
                for (int col = 0; col < cols; col++) {
                    String edge = getNode(row, col) + "-" + getNode(row + 1, col);
                    if (edges.contains(edge)) {
                        System.out.print(EDGE);
                    } else {
                        System.out.print(WALL);
                    }
                    if (col < cols - 1) {
                        System.out.print(WALL);
                    }
                }
                System.out.println();
            }
        }
    }

    private void printMazeWithSolution() {
        for (int row = 0; row < rows; row++) {
            // Print nodes and horizontal edges
            for (int col = 0; col < cols; col++) {
                Node currentNode = getNode(row, col);
                if (path.contains(currentNode)) {
                    System.out.print(PATH_CHAR);
                } else if (triedNodes.contains(currentNode)) {
                    System.out.print(TRIED_CHAR);
                } else {
                    System.out.print(NODE);
                }

                if (col < cols - 1) {
                    String edge = currentNode + "-" + getNode(row, col + 1);
                    if (pathEdges.contains(edge)) {
                        System.out.print(PATH_CHAR);
                    } else if (triedEdges.contains(edge)) {
                        System.out.print(TRIED_CHAR);
                    } else {
                        System.out.print(WALL);
                    }
                }
            }
            System.out.println();
            // Print vertical edges
            if (row < rows - 1) {
                for (int col = 0; col < cols; col++) {
                    String edge = getNode(row, col) + "-" + getNode(row + 1, col);
                    if (pathEdges.contains(edge)) {
                        System.out.print(PATH_CHAR);
                    } else if (triedEdges.contains(edge)) {
                        System.out.print(TRIED_CHAR);
                    } else {
                        System.out.print(WALL);
                    }
                    if (col < cols - 1) {
                        System.out.print(WALL);
                    }
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        GridGraphVisualizer gridGraph = new GridGraphVisualizer(5, 5);  // Create a 5x5 maze

        // Print the original maze before solving
        gridGraph.printOriginalMaze();

        // Solve the maze
        gridGraph.solveMaze();

        // Print the path and the solved maze
        gridGraph.printPath();
        gridGraph.printTriedNodes();
        gridGraph.printSolvedMaze();
    }
}
