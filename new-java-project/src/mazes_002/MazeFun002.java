package mazes_002;



import java.util.*;

public class MazeFun002 {
    private static final char EDGE = 'E';
    private static final char NODE = '.';
    private static final char WALL = '#';

    private int rows;
    private int cols;
    private char[][] mazeLayout;  // First maze property (with walls, S, and G)
    private char[][] dfsMazeLayout;  // Second maze property (with DFS paths marked)
    private Node startNode;
    private Node goalNode;
    private Set<Node> visited;
    private Stack<Node> dfsStack;
    private Map<Node, List<Node>> edges;
    private String initialMazeString;
    private String dfsMazeString;

    // Constructor for the maze
    public MazeFun002(int rows, int cols) {
        this.rows = rows * 2 - 1;
        this.cols = cols * 2 - 1;
        this.mazeLayout = new char[this.rows][this.cols];
        this.dfsMazeLayout = new char[this.rows][this.cols];
        this.edges = new HashMap<>();
        this.visited = new HashSet<>();
        this.dfsStack = new Stack<>();

        // Generate the grid graph and create the initial maze layout
        GridGraphVisualizer graph = new GridGraphVisualizer(rows, cols);
        initializeMazeLayout();
        performDFS(0);  // Start DFS from the first node (bottom-left)

        // Save the string representation
        this.initialMazeString = mazeToString(mazeLayout);
        this.dfsMazeString = mazeToString(dfsMazeLayout);
    }

    // Initialize the maze layout with walls and edges
    private void initializeMazeLayout() {
        // Initialize all cells as walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeLayout[i][j] = WALL;
                dfsMazeLayout[i][j] = WALL;
            }
        }

        // Set start ('S') at bottom-left and goal ('G') at top-right
        mazeLayout[rows - 1][0] = 'S';
        mazeLayout[0][cols - 1] = 'G';
        dfsMazeLayout[rows - 1][0] = 'S';
        dfsMazeLayout[0][cols - 1] = 'G';

        // Create the grid of nodes with edges ('E')
        for (int row = 0; row < rows; row += 2) {
            for (int col = 0; col < cols; col += 2) {
                mazeLayout[row][col] = NODE;
                dfsMazeLayout[row][col] = NODE;

                // Add edges for nodes to the right and below them
                if (col + 2 < cols) {
                    mazeLayout[row][col + 1] = EDGE;
                    dfsMazeLayout[row][col + 1] = EDGE;
                }
                if (row + 2 < rows) {
                    mazeLayout[row + 1][col] = EDGE;
                    dfsMazeLayout[row + 1][col] = EDGE;
                }
            }
        }

        // Store the start and goal nodes
        startNode = new Node(rows - 1, 0, 1); // Start at bottom-left
        goalNode = new Node(0, cols - 1, (rows / 2) * (cols / 2));  // Goal at top-right
    }

    // Perform DFS traversal and mark visited paths
    private void performDFS(int startNodeIndex) {
        dfsStack.push(startNode);
        visited.add(startNode);
        markVisited(startNode, 'P');

        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.peek();

            // If we reach the goal node, stop the DFS
            if (currentNode.equals(goalNode)) {
                break;
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

    // Sort neighbors based on priority: north, east, south, west
    private List<Node> getSortedNeighbors(Node node) {
        List<Node> neighbors = edges.get(node);
        if (neighbors != null) {
            neighbors.sort((n1, n2) -> {
                // Compare rows first (north-south)
                if (n1.getRow() < node.getRow()) return -1;  // North comes first
                if (n2.getRow() < node.getRow()) return 1;

                if (n1.getRow() > node.getRow()) return 1;   // South comes later
                if (n2.getRow() > node.getRow()) return -1;

                // Compare columns (east-west)
                if (n1.getCol() > node.getCol()) return -1;  // East before west
                if (n2.getCol() > node.getCol()) return 1;

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

    // Convert the maze layout to a string
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
    	MazeFun002 mazeFun = new MazeFun002(20, 30);
        System.out.println("Initial Maze:");
        System.out.println(mazeFun.getInitialMazeString());

        System.out.println("\nDFS-traversed Maze:");
        System.out.println(mazeFun.getDfsMazeString());
    }
}

