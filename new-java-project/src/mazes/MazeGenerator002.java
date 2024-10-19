package mazes;

import java.util.*;

public class MazeGenerator002 {
    // Directions for moving: {row, col} for N, S, E, W
    private static final int[][] DIRECTIONS = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 } };
    private int rows;
    private int cols;
    private char[][] maze;
    private boolean[][] visited;
    
    // Constructor to initialize the maze with walls ('#') and unvisited cells
    public MazeGenerator002(int rows, int cols) {
        this.rows = rows * 2 - 1; // Adjust size to allow walls between cells
        this.cols = cols * 2 - 1;
        this.maze = new char[this.rows][this.cols];
        this.visited = new boolean[this.rows][this.cols];

        // Initialize the maze with walls ('#')
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                maze[i][j] = '#';  // Wall
            }
        }
    }

    // Method to generate the maze
    public void generateMaze() {
        // Start at a random cell
        int startRow = 0;
        int startCol = 0;
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { startRow, startCol });
        maze[startRow][startCol] = '.'; // Mark the start as an open cell
        visited[startRow][startCol] = true;

        Random random = new Random();

        // DFS with backtracking
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
                maze[(row + nextRow) / 2][(col + nextCol) / 2] = '.';  // Remove the wall
                maze[nextRow][nextCol] = '.';  // Mark the next cell as open
                visited[nextRow][nextCol] = true;

                // Push the next cell onto the stack
                stack.push(next);

            } else {
                // No unvisited neighbors, backtrack
                stack.pop();
            }
        }
    }

    // Check if the cell is within bounds
    private boolean isInBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    // Method to print the maze
    public void printMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Create a maze of size 10x10
    	MazeGenerator002 mazeGenerator = new MazeGenerator002(10, 10);
        mazeGenerator.generateMaze();
        mazeGenerator.printMaze();
    }
}

