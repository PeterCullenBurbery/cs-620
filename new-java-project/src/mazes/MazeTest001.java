package mazes;

public class MazeTest001 {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private MazeGraph mazeGraph; // Property to store the MazeGraph

    // Constructor to initialize the maze from a string input
    public MazeTest001(String mazeInput) {
        String[] lines = mazeInput.split("\n");
        this.height = lines.length;
        this.width = lines[0].length();
        this.mazeLayout = new char[height][width];
        this.mazeGraph = new MazeGraph(); // Initialize the maze graph

        // Populate the maze layout
        for (int i = 0; i < height; i++) {
            mazeLayout[i] = lines[i].toCharArray();
        }

        // Build the maze graph
        buildMazeGraph();
    }

    // Method to build the graph based on the maze layout
    private void buildMazeGraph() {
        Node[][] nodes = new Node[height][width];
        int nodeNumber = 1;  // Start numbering from 1

        // Create nodes for each walkable ('.') cell and number them sequentially
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //System.out.println(row + "," + col + " " + mazeLayout[row][col]);  // Debugging output
                if (mazeLayout[row][col] == '.') {
                    nodes[row][col] = new Node(row, col, nodeNumber);
                    mazeGraph.addNode(nodes[row][col]);  // Add node to the graph by its object
                    nodeNumber++;
                }
            }
        }

        // Connect nodes horizontally and vertically
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (nodes[row][col] != null) {
                    // Connect to the right (horizontal connection)
                    if (col + 1 < width && nodes[row][col + 1] != null) {
                        mazeGraph.addEdge(nodes[row][col], nodes[row][col + 1]);
                    }
                    // Connect to the down (vertical connection)
                    if (row + 1 < height && nodes[row + 1][col] != null) {
                        mazeGraph.addEdge(nodes[row][col], nodes[row + 1][col]);
                    }
                }
            }
        }
    }

    // Method to print the nodes with their coordinates
    public void printNodes() {
        mazeGraph.printNodes();
    }

    // Method to print the adjacency list
    public void printConnections() {
        mazeGraph.printConnections();
    }

    public static void main(String[] args) {
        String mazeInput = "..###\n.####\n..###\n#####\n###.#";
        System.out.println(mazeInput);
        MazeTest001 maze = new MazeTest001(mazeInput);

        System.out.println("Maze Nodes:");
        maze.printNodes();

        System.out.println("\nMaze Connections:");
        maze.printConnections();
    }
}
