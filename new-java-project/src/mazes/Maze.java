package mazes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private String mazeInput;  // Field to store the entire content of the maze input
    private String mazeCompute; // Field to store the maze with the computed path from 'S' to 'G'
    private int[] startCoordinates; // Stores the coordinates of 'S' (start)
    private int[] goalCoordinates;  // Stores the coordinates of 'G' (goal)

    // Constructor to initialize the maze from a string input
    public Maze(String mazeInput) {
        this.mazeInput = mazeInput;
        this.startCoordinates = new int[2];
        this.goalCoordinates = new int[2];
        parseMazeInput();
        this.mazeCompute = null; // mazeCompute is not set at initialization
    }

    // Constructor to initialize the maze from a file
    public Maze(java.io.File file) throws IOException {
        this.mazeInput = readFile(file); // Read file content and store in mazeInput
        this.startCoordinates = new int[2];
        this.goalCoordinates = new int[2];
        parseMazeInput();
        this.mazeCompute = null; // mazeCompute is not set at initialization
    }

    // Method to parse the mazeInput field and extract width, height, layout, and coordinates
    private void parseMazeInput() {
        String[] lines = mazeInput.split("\n");

        // The first line contains the width and height
        String[] dimensions = lines[0].split(" ");
        this.width = Integer.parseInt(dimensions[0]);
        this.height = Integer.parseInt(dimensions[1]);

        // Initialize the mazeLayout array based on the dimensions
        mazeLayout = new char[height][width];

        // Populate the mazeLayout and find the start and goal positions
        for (int i = 0; i < height; i++) {
            String line = lines[i + 1]; // Maze data starts from the second line
            for (int j = 0; j < width; j++) {
                mazeLayout[i][j] = line.charAt(j);
                if (mazeLayout[i][j] == 'S') {
                    startCoordinates[0] = i; // Row of 'S'
                    startCoordinates[1] = j; // Column of 'S'
                } else if (mazeLayout[i][j] == 'G') {
                    goalCoordinates[0] = i; // Row of 'G'
                    goalCoordinates[1] = j; // Column of 'G'
                }
            }
        }
    }

    // Method to compute the maze path using DFS and set the mazeCompute property
    public boolean setMazeCompute() {
        char[][] mazeCopy = copyMazeLayout(); // Create a copy of the original maze layout
        boolean pathFound = dfs(mazeCopy, startCoordinates[0], startCoordinates[1]);
        updateMazeCompute(mazeCopy); // Convert the result into a string and store in mazeCompute
        return pathFound;
    }

    // DFS algorithm with proper marking and backtracking from 'P' to '?'
    private boolean dfs(char[][] maze, int x, int y) {
        // If x or y is outside the maze, return false
        if (x < 0 || y < 0 || x >= height || y >= width) return false;

        // If the current position is the goal, return true
        if (maze[x][y] == 'G') return true;

        // If the current position is not empty ('.') and not the start ('S'), return false
        if (maze[x][y] != '.' && maze[x][y] != 'S') return false;

        // Mark the current cell as part of the path ('P')
        char original = maze[x][y];  // Store original state (either '.' or 'S')
        if (maze[x][y] != 'S') maze[x][y] = 'P';  // Temporarily mark as part of the path

        // Explore the four directions: North, East, South, West
        if (dfs(maze, x - 1, y)) return true; // Go North
        if (dfs(maze, x, y + 1)) return true; // Go East
        if (dfs(maze, x + 1, y)) return true; // Go South
        if (dfs(maze, x, y - 1)) return true; // Go West

        // Backtrack: if none of the directions worked, change 'P' to '?'
        if (maze[x][y] == 'P') maze[x][y] = '?';  // Mark it as backtracked

        return false;  // No valid path found from this cell
    }

    // Method to copy the maze layout
    private char[][] copyMazeLayout() {
        char[][] mazeCopy = new char[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(mazeLayout[i], 0, mazeCopy[i], 0, width);
        }
        return mazeCopy;
    }

    // Method to convert the maze layout into a string and set the mazeCompute property
    private void updateMazeCompute(char[][] maze) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(maze[i][j]);
            }
            sb.append("\n");
        }
        this.mazeCompute = sb.toString();
    }

    // Getter for the maze input string
    public String getMazeInput() {
        return mazeInput;
    }

    // Getter for the maze compute result
    public String getMazeCompute() {
        return mazeCompute;
    }

    // Getter for the start coordinates
    public int[] getStartCoordinates() {
        return startCoordinates;
    }

    // Getter for the goal coordinates
    public int[] getGoalCoordinates() {
        return goalCoordinates;
    }

    // Getter for width
    public int getWidth() {
        return width;
    }

    // Getter for height
    public int getHeight() {
        return height;
    }

    // Method to print the maze layout
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeLayout[i][j]);
            }
            System.out.println();
        }
    }

    // Utility method to read the content of a file
    private static String readFile(java.io.File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }
}






