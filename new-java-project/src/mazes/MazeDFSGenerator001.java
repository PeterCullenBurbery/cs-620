package mazes;

import java.util.*;

public class MazeDFSGenerator001 {

    private static final char WALL = '#';
    private static final char PATH = '.';

    // Helper class to represent a position in the grid
    private static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Function to generate a random maze using DFS and backtracking
    public static String generateMaze(int rows, int cols) {
        // The output grid will be (2*rows - 1) by (2*cols - 1)
        int gridHeight = 2 * rows - 1;
        int gridWidth = 2 * cols - 1;
        char[][] maze = new char[gridHeight][gridWidth];

        // Fill the entire grid with open spaces and walls
        for (int i = 0; i < gridHeight; i++) {
            Arrays.fill(maze[i], PATH);
        }

        // Set for visited cells
        boolean[][] visited = new boolean[rows][cols];
        Stack<Position> stack = new Stack<>();
        Random rand = new Random();

        // Start at node 5 (which is row 1, col 1 in a 3x3 grid)
        Position start = new Position(1, 1);
        stack.push(start);
        visited[start.row][start.col] = true;
        carvePath(maze, start.row, start.col);

        // Directions for moving (up, right, down, left)
        int[][] directions = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };

        // Perform DFS with backtracking
        while (!stack.isEmpty()) {
            Position current = stack.peek();
            List<Position> neighbors = new ArrayList<>();

            // Check for unvisited neighbors
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (isValid(newRow, newCol, rows, cols, visited)) {
                    neighbors.add(new Position(newRow, newCol));
                }
            }

            if (!neighbors.isEmpty()) {
                // Pick a random neighbor
                Position next = neighbors.get(rand.nextInt(neighbors.size()));

                // Mark the neighbor as visited
                visited[next.row][next.col] = true;

                // Remove the wall between current and next
                removeWall(maze, current, next);

                // Push the neighbor to the stack
                stack.push(next);
            } else {
                // Backtrack if no unvisited neighbors are left
                stack.pop();
            }
        }

        // Convert the maze to a string for easy printing
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                result.append(maze[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    // Carve out the initial path in the maze (convert a wall to a path)
    private static void carvePath(char[][] maze, int row, int col) {
        maze[2 * row][2 * col] = PATH;
    }

    // Remove the wall between two cells
    private static void removeWall(char[][] maze, Position current, Position next) {
        int wallRow = (2 * current.row + 2 * next.row) / 2;
        int wallCol = (2 * current.col + 2 * next.col) / 2;
        maze[wallRow][wallCol] = PATH;
    }

    // Check if a neighbor is valid and unvisited
    private static boolean isValid(int row, int col, int rows, int cols, boolean[][] visited) {
        return row >= 0 && col >= 0 && row < rows && col < cols && !visited[row][col];
    }

    public static void main(String[] args) {
        // Generate a 3x3 random maze starting at node 5
        System.out.println(generateMaze(3, 3));
    }
}

