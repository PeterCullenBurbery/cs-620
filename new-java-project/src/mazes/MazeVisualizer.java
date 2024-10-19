package mazes;

import java.util.*;

public class MazeVisualizer {

	private static final char EDGE = 'E';
	private static final char NODE = '.';
	private static final char WALL = '#';

	// Function to generate a visual maze structure with edges and walls, without
	// border walls
	public static String generateVisualMaze(int rows, int cols) {
		// The grid will be (2*rows - 1) by (2*cols - 1) to include nodes, edges, and
		// walls
		int gridHeight = 2 * rows - 1;
		int gridWidth = 2 * cols - 1;
		char[][] maze = new char[gridHeight][gridWidth];

		// Initialize the grid with walls everywhere
		for (int i = 0; i < gridHeight; i++) {
			Arrays.fill(maze[i], WALL);
		}

		// Place nodes and edges
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				// Nodes: Place a '.' at every (2*row, 2*col)
				maze[2 * row][2 * col] = NODE;

				// Horizontal edges: Place 'E' between horizontal neighbors, except for the last
				// column
				if (col < cols - 1) {
					maze[2 * row][2 * col + 1] = EDGE;
				}

				// Vertical edges: Place 'E' between vertical neighbors, except for the last row
				if (row < rows - 1) {
					maze[2 * row + 1][2 * col] = EDGE;
				}
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

	public static void main(String[] args) {
		// Generate and visualize a 2x2 maze
		System.out.println("2x2 Maze Visualization:");
		System.out.println(generateVisualMaze(2, 2));

		// Generate and visualize a 3x3 maze
		System.out.println("3x3 Maze Visualization:");
		System.out.println(generateVisualMaze(3, 3));

		// Generate and visualize a larger 5x5 maze
		System.out.println("5x5 Maze Visualization:");
		System.out.println(generateVisualMaze(5, 5));
	}
}
