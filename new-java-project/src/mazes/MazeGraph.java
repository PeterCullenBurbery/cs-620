package mazes;

import java.util.*;

class MazeGraph {
    private List<Node> nodes;  // List to store Node objects
    private Map<Node, List<Node>> adjacencyList; // Adjacency list to represent the graph

    // Constructor to initialize the graph
    public MazeGraph() {
        nodes = new ArrayList<>();
        adjacencyList = new HashMap<>();
    }

    // Method to add a node
    public void addNode(Node node) {
        nodes.add(node);  // Store the node in the list
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Method to add an edge between two nodes (bidirectional)
    public void addEdge(Node node1, Node node2) {
        adjacencyList.get(node1).add(node2);
        adjacencyList.get(node2).add(node1);
    }

    // Method to print all nodes with their coordinates
    public void printNodes() {
        for (Node node : nodes) {
            System.out.println("Node " + node.getNodeNumber() + " at " + node.getRow() + " " + node.getCol());
        }
    }

    // Method to print the adjacency list in the format `node X {connected nodes}`
    public void printConnections() {
        for (Node node : adjacencyList.keySet()) {
            List<Node> connections = adjacencyList.get(node);
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
}
