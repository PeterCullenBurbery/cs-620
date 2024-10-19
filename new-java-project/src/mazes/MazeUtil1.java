package mazes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MazeUtil1 {
    private MazeTest004 mazeTest004;
    private Set<Node> visited;
    private Stack<Node> dfsStack;
    private Node startNode;
    private Node goalNode;
    private Map<Node, List<Node>> edges;

    // Method to read the maze from a file and initialize MazeTest004
    private void readMazeFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        scanner.close();

        int height = lines.size();
        int width = lines.get(0).length();
        char[][] mazeLayout = new char[height][width];

        for (int i = 0; i < height; i++) {
            mazeLayout[i] = lines.get(i).toCharArray();
        }

        // Initialize MazeTest004 object
        mazeTest004 = new MazeTest004(width, height, mazeLayout);

        // Now set up DFS
        initializeDFS();
    }

    // Initialize the DFS process
    private void initializeDFS() {
        visited = new HashSet<>();
        dfsStack = new Stack<>();
        edges = new HashMap<>();

        char[][] mazeLayout = mazeTest004.getMazeLayout();
        int height = mazeTest004.getHeight();
        int width = mazeTest004.getWidth();
        Node[][] nodeGrid = new Node[height][width];
        int nodeNumber = 1;

        // Create nodes for each walkable ('.', 'S', 'G') cell
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char cell = mazeLayout[row][col];
                if (cell == '.' || cell == 'S' || cell == 'G') {
                    Node newNode = new Node(row, col, nodeNumber);
                    nodeGrid[row][col] = newNode;
                    edges.put(newNode, new ArrayList<>());

                    if (cell == 'S') {
                        startNode = newNode;
                    } else if (cell == 'G') {
                        goalNode = newNode;
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
                    // Connect to the right
                    if (col + 1 < width && nodeGrid[row][col + 1] != null) {
                        addEdge(currentNode, nodeGrid[row][col + 1]);
                    }
                    // Connect to the down
                    if (row + 1 < height && nodeGrid[row + 1][col] != null) {
                        addEdge(currentNode, nodeGrid[row + 1][col]);
                    }
                }
            }
        }

        // Run DFS starting from the start node
        runDFS();
    }

    // Add an edge between two nodes (bidirectional)
    private void addEdge(Node node1, Node node2) {
        edges.get(node1).add(node2);
        edges.get(node2).add(node1);
    }

 // Run DFS and update the maze with 'P' and '?' markers
    private void runDFS() {
        dfsStack.push(startNode);
        visited.add(startNode);
        markVisited(startNode, 'P');

        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.peek();

            // If we reach the goal node, stop searching
            if (currentNode.equals(goalNode)) {
                break; // Terminate DFS when goal is reached
            }

            List<Node> neighbors = getSortedNeighbors(currentNode);

            boolean foundUnvisited = false;
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsStack.push(neighbor);
                    visited.add(neighbor);
                    markVisited(neighbor, 'P');
                    foundUnvisited = true;
                    break;
                }
            }

            if (!foundUnvisited) {
                Node backtrackNode = dfsStack.pop();
                if (backtrackNode != startNode) {
                    markVisited(backtrackNode, '?');
                }
            }
        }
    }


    // Sort neighbors in the priority order (north, east, south, west)
    private List<Node> getSortedNeighbors(Node node) {
        List<Node> neighbors = edges.get(node);
        neighbors.sort((n1, n2) -> {
            if (n1.getRow() < node.getRow()) return -1;
            if (n2.getRow() < node.getRow()) return 1;

            if (n1.getCol() > node.getCol()) return -1;
            if (n2.getCol() > node.getCol()) return 1;

            if (n1.getRow() > node.getRow()) return 1;
            if (n2.getRow() > node.getRow()) return -1;

            if (n1.getCol() < node.getCol()) return -1;
            if (n2.getCol() < node.getCol()) return 1;

            return 0;
        });
        return neighbors;
    }

    // Mark the node in the maze layout
    private void markVisited(Node node, char marker) {
        char[][] mazeLayout = mazeTest004.getMazeLayout();
        if (mazeLayout[node.getRow()][node.getCol()] == 'S' || mazeLayout[node.getRow()][node.getCol()] == 'G') {
            return; // Don't overwrite 'S' or 'G'
        }
        mazeLayout[node.getRow()][node.getCol()] = marker;
    }

    // Method to return the maze layout after reading the file and computing DFS as a string
    public String getMazeResult(String filePath) throws FileNotFoundException {
        // Step 1: Read the maze from the file
        readMazeFromFile(filePath);

        // Step 2: Convert the updated maze to a string and return it
        StringBuilder result = new StringBuilder();
        char[][] mazeLayout = mazeTest004.getMazeLayout();

        for (int i = 0; i < mazeTest004.getHeight(); i++) {
            result.append(mazeLayout[i]);
            if (i < mazeTest004.getHeight() - 1) {
                result.append("\n");  // Add new line except after the last row
            }
        }

        return result.toString();
    }
}
