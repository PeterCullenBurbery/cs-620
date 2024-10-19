package mazes_002;

import java.util.*;

public class MazeFun {
    private int rows;
    private int cols;
    private char[][] mazeLayout;  // First maze property (with walls, S, and G)
    private char[][] dfsMazeLayout;  // Second maze property (with DFS paths marked)
    private Node startNode;
    private Node goalNode;
    private Set<Node> visited;
    private Stack<Node> dfsStack;
    private Map<Node, List<Node>> edges;

    // String properties for easy printing
    private String initialMazeString;  // First property in string format
    private String dfsMazeString;      // Second property in string format

    // Directions for moving: {row, col} for N, S, E, W
    private static final int[][] DIRECTIONS = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 } };

    // Constructor to initialize the maze and perform DFS
    public MazeFun(int rows, int cols) {
        this.rows = rows * 2 - 1;  // Account for the visualization
        this.cols = cols * 2 - 1;
        this.mazeLayout = new char[this.rows][this.cols];
        this.dfsMazeLayout = new char[this.rows][this.cols];
        this.visited = new HashSet<>();
        this.dfsStack = new Stack<>();
        this.edges = new HashMap<>();

        // Generate the random maze with 'S' and 'G'
        generateRandomMaze();
        this.initialMazeString = mazeToString(mazeLayout);  // Store the initial maze as a string
        System.out.println("Initial Maze:\n" + initialMazeString);

        // Set up the edges and initialize nodes before DFS
        initializeDFS();

        // Perform DFS and update the maze
        performDFS();
        this.dfsMazeString = mazeToString(dfsMazeLayout);  // Store the DFS-traversed maze as a string
        System.out.println("Maze after DFS Traversal:\n" + dfsMazeString);
    }

    // Method to generate a random maze
    private void generateRandomMaze() {
        boolean[][] visited = new boolean[rows][cols];
        Stack<int[]> stack = new Stack<>();
        Random random = new Random();

        // Initialize the maze with walls ('#')
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeLayout[i][j] = '#';
            }
        }

        // Start DFS from the bottom-left corner (S)
        int startRow = rows - 1;  // Bottom-left
        int startCol = 0;
        stack.push(new int[] { startRow, startCol });
        mazeLayout[startRow][startCol] = '.'; // Open the start cell
        visited[startRow][startCol] = true;

        // DFS with backtracking to generate the maze
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int row = current[0];
            int col = current[1];

            // Find unvisited neighbors
            List<int[]> neighbors = new ArrayList<>();
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if the neighbor is within bounds and unvisited
                if (isInBounds(newRow, newCol) && !visited[newRow][newCol]) {
                    neighbors.add(new int[] { newRow, newCol });
                }
            }

            if (!neighbors.isEmpty()) {
                // Randomly choose one of the unvisited neighbors
                int[] next = neighbors.get(random.nextInt(neighbors.size()));
                int nextRow = next[0];
                int nextCol = next[1];

                // Carve a path between current and next
                mazeLayout[(row + nextRow) / 2][(col + nextCol) / 2] = '.';  // Remove the wall
                mazeLayout[nextRow][nextCol] = '.';  // Mark the next cell as open
                visited[nextRow][nextCol] = true;

                // Push the next cell onto the stack
                stack.push(next);

            } else {
                // No unvisited neighbors, backtrack
                stack.pop();
            }
        }

        // Set 'S' at the start position
        mazeLayout[rows - 1][0] = 'S';
        startNode = new Node(rows - 1, 0, 1);

        // Set 'G' at the top-right corner
        mazeLayout[0][cols - 1] = 'G';
        goalNode = new Node(0, cols - 1, 2);

        // Replace 'E' with '.' (as per requirement)
        replaceEdgesWithDots();

        // Create a copy of the maze for DFS traversal
        for (int i = 0; i < rows; i++) {
            dfsMazeLayout[i] = Arrays.copyOf(mazeLayout[i], cols);
        }
    }

    // Check if the cell is within bounds
    private boolean isInBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    // Initialize DFS by setting up the nodes and edges
    private void initializeDFS() {
        Node[][] nodeGrid = new Node[rows][cols];
        int nodeNumber = 1;

        // Create nodes for each walkable ('.', 'S', 'G') cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
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
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node currentNode = nodeGrid[row][col];
                if (currentNode != null) {
                    // Connect to the right
                    if (col + 1 < cols && nodeGrid[row][col + 1] != null) {
                        addEdge(currentNode, nodeGrid[row][col + 1]);
                    }
                    // Connect to the down
                    if (row + 1 < rows && nodeGrid[row + 1][col] != null) {
                        addEdge(currentNode, nodeGrid[row + 1][col]);
                    }
                }
            }
        }
    }

    // Add an edge between two nodes (bidirectional)
    private void addEdge(Node node1, Node node2) {
        edges.get(node1).add(node2);
        edges.get(node2).add(node1);
    }

    // Perform DFS and mark the paths in the DFS maze
    private void performDFS() {
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
        if (neighbors != null) {
            neighbors.sort((n1, n2) -> {
                // Priority sorting: North (-1), East (+1), South (+1), West (-1)
                // Compare rows for north and south
                if (n1.getRow() < node.getRow()) return -1; // North comes first
                if (n2.getRow() < node.getRow()) return 1;

                if (n1.getRow() > node.getRow()) return 1; // South comes later
                if (n2.getRow() > node.getRow()) return -1;

                // Compare columns for east and west
                if (n1.getCol() > node.getCol()) return -1; // East comes second
                if (n2.getCol() > node.getCol()) return 1;

                if (n1.getCol() < node.getCol()) return 1; // West comes last
                if (n2.getCol() < node.getCol()) return -1;

                return 0;
            });
        }
        return neighbors;
    }

    // Mark the node in the maze layout
    private void markVisited(Node node, char marker) {
        if (dfsMazeLayout[node.getRow()][node.getCol()] != 'S' && dfsMazeLayout[node.getRow()][node.getCol()] != 'G') {
            dfsMazeLayout[node.getRow()][node.getCol()] = marker;
        }
    }

    // Replace 'E' with '.' in the initial maze layout
    private void replaceEdgesWithDots() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mazeLayout[i][j] == 'E') {
                    mazeLayout[i][j] = '.';
                }
            }
        }
    }

    // Convert the maze to a string format
    private String mazeToString(char[][] maze) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maze.length; i++) {
            result.append(maze[i]);
            if (i < maze.length - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    // Get the initial maze as a string
    public String getInitialMazeString() {
        return initialMazeString;
    }

    // Get the DFS-traversed maze as a string
    public String getDfsMazeString() {
        return dfsMazeString;
    }

    public static void main(String[] args) {
        MazeFun mazeFun = new MazeFun(2,2);
        MazeFun mazeFun3 = new MazeFun(3,3);
        MazeFun mazeFun5 = new MazeFun(5,5);
        MazeFun mazeFun10 = new MazeFun(10,10);
        MazeFun mazeFun20 = new MazeFun(20,20);
        MazeFun mazeFun30 = new MazeFun(30,30);
    }
}
