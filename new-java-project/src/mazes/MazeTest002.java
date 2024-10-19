package mazes;

import java.util.*;

public class MazeTest002 {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private Node startNode;
    private Node goalNode;
    private List<Node> nodes;
    private Map<Node, List<Node>> edges;

    // Constructor to initialize the maze from a string input
    public MazeTest002(String mazeInput) {
        String[] lines = mazeInput.split("\n");
        this.height = lines.length;
        this.width = lines[0].length();
        this.mazeLayout = new char[height][width];
        this.nodes = new ArrayList<>();
        this.edges = new HashMap<>();

        // Populate the maze layout
        for (int i = 0; i < height; i++) {
            mazeLayout[i] = lines[i].toCharArray();
        }

        // Build the graph
        buildGraph();
    }

    // Method to build the graph based on the maze layout
    private void buildGraph() {
        Node[][] nodeGrid = new Node[height][width];
        int nodeNumber = 1;  // Start numbering from 1

        // Create nodes for each walkable ('.') cell and also for 'S' and 'G'
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char cell = mazeLayout[row][col];
                if (cell == '.' || cell == 'S' || cell == 'G') {
                    Node newNode = new Node(row, col, nodeNumber);
                    nodeGrid[row][col] = newNode;
                    nodes.add(newNode);  // Add node to the list
                    edges.put(newNode, new ArrayList<>());  // Initialize adjacency list

                    if (cell == 'S') {
                        startNode = newNode;  // Set start node
                    } else if (cell == 'G') {
                        goalNode = newNode;  // Set goal node
                    }

                    nodeNumber++;
                }
            }
        }

        // Connect nodes horizontally and vertically
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Node currentNode = nodeGrid[row][col];
                if (currentNode != null) {
                    // Connect to the right (horizontal connection)
                    if (col + 1 < width && nodeGrid[row][col + 1] != null) {
                        addEdge(currentNode, nodeGrid[row][col + 1]);
                    }
                    // Connect to the down (vertical connection)
                    if (row + 1 < height && nodeGrid[row + 1][col] != null) {
                        addEdge(currentNode, nodeGrid[row + 1][col]);
                    }
                }
            }
        }
    }

    // Method to add an edge between two nodes (bidirectional)
    private void addEdge(Node node1, Node node2) {
        edges.get(node1).add(node2);
        edges.get(node2).add(node1);
    }

    // Method to print the nodes with their coordinates
    public void printNodes() {
        for (Node node : nodes) {
            System.out.println("Node " + node.getNodeNumber() + " at " + node.getRow() + " " + node.getCol());
        }
    }

    // Method to print the adjacency list in the format `node X {connected nodes}`
    public void printConnections() {
        for (Node node : edges.keySet()) {
            List<Node> connections = edges.get(node);
            System.out.print("Node " + node.getNodeNumber() + " {");
            if (connections.isEmpty()) {
                System.out.print("}");
            } else {
                for (int i = 0; i < connections.size(); i++) {
                    System.out.print(connections.get(i).getNodeNumber());
                    if (i < connections.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("}");
            }
            System.out.println();
        }
    }

    // Getters for start and goal nodes
    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    public static void main(String[] args) {
        String mazeInput = "S..##\n.####\n...##\n.#.##\n.#G##";
        System.out.println(mazeInput);
        MazeTest002 maze = new MazeTest002(mazeInput);

        // Print nodes and their coordinates
        System.out.println("Maze Nodes:");
        maze.printNodes();

        // Print connections between nodes
        System.out.println("\nMaze Connections:");
        maze.printConnections();

        // Print start and goal nodes
        System.out.println("\nStart Node: " + maze.getStartNode());
        System.out.println("Goal Node: " + maze.getGoalNode());
    }
}
