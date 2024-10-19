package mazes;



import java.util.*;

public class MazeTest003 {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private Node startNode;
    private Node goalNode;
    private List<Node> nodes;
    private Map<Node, List<Node>> edges;
    private Set<Node> visited;
    private Stack<Node> dfsStack;

    // Constructor to initialize the maze from a string input
    public MazeTest003(String mazeInput) {
        String[] lines = mazeInput.split("\n");
        this.height = lines.length;
        this.width = lines[0].length();
        this.mazeLayout = new char[height][width];
        this.nodes = new ArrayList<>();
        this.edges = new HashMap<>();
        this.visited = new HashSet<>();
        this.dfsStack = new Stack<>();

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

    // Perform DFS to find the path from startNode to goalNode
    public void dfs() {
        dfsStack.push(startNode);  // Start at the startNode
        visited.add(startNode);
        markVisited(startNode, 'P');  // Mark the start node as part of the path

        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.peek();

            // Sort neighbors by the prioritized order: north, east, south, west
            List<Node> neighbors = getSortedNeighbors(currentNode);

            boolean foundUnvisited = false;

            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsStack.push(neighbor);
                    visited.add(neighbor);
                    markVisited(neighbor, 'P');  // Mark the node as part of the path
                    foundUnvisited = true;
                    break;  // Continue to the next node
                }
            }

            // If no unvisited neighbors, backtrack
            if (!foundUnvisited) {
                Node backtrackNode = dfsStack.pop();
                if (backtrackNode != startNode) {
                    markVisited(backtrackNode, '?');  // Mark backtracked node with '?'
                }
            }

            // Stop when we reach the goal
            if (currentNode == goalNode) {
                break;
            }
        }
    }

    // Sort neighbors in the order: north, east, south, west
    private List<Node> getSortedNeighbors(Node node) {
        List<Node> neighbors = edges.get(node);
        neighbors.sort((n1, n2) -> {
            // North (row-1, same col)
            if (n1.getRow() < node.getRow()) return -1;
            if (n2.getRow() < node.getRow()) return 1;

            // East (same row, col+1)
            if (n1.getCol() > node.getCol()) return -1;
            if (n2.getCol() > node.getCol()) return 1;

            // South (row+1, same col)
            if (n1.getRow() > node.getRow()) return 1;
            if (n2.getRow() > node.getRow()) return -1;

            // West (same row, col-1)
            if (n1.getCol() < node.getCol()) return -1;
            if (n2.getCol() < node.getCol()) return 1;

            return 0;
        });
        return neighbors;
    }

    // Mark the node on the maze layout with a given character ('P' for path, '?' for backtrack)
    private void markVisited(Node node, char marker) {
        if (mazeLayout[node.getRow()][node.getCol()] == 'S' || mazeLayout[node.getRow()][node.getCol()] == 'G') {
            return;  // Don't overwrite 'S' or 'G'
        }
        mazeLayout[node.getRow()][node.getCol()] = marker;
        printMaze();
    }

    // Print the current state of the maze
    private void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeLayout[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String mazeInput = "S..##\n.####\n...##\n.#.##\n.#G##";
        MazeTest003 maze = new MazeTest003(mazeInput);

        // Perform DFS to explore the maze
        maze.dfs();
    }
}
