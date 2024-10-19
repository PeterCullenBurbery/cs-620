package mazes;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			// Create a Maze object from a file
			Maze mazeFromFile = new Maze(new File("C:\\Users\\peter\\git\\cs-620\\mazes\\maze-001.txt"));
			mazeFromFile.printMaze();
			System.out.println("Width: " + mazeFromFile.getWidth());
			System.out.println("Height: " + mazeFromFile.getHeight());
			System.out.println("Start Coordinates: (" + mazeFromFile.getStartCoordinates()[0] + ", "
					+ mazeFromFile.getStartCoordinates()[1] + ")");
			System.out.println("Goal Coordinates: (" + mazeFromFile.getGoalCoordinates()[0] + ", "
					+ mazeFromFile.getGoalCoordinates()[1] + ")");
			System.out.println("Maze Input as String:\n" + mazeFromFile.getMazeInput());

			// You can also create a Maze object from a string
			String mazeInput = "5 5\nS..##\n.####\n...##\n.#.##\n.#G##\n";
			Maze mazeFromString = new Maze(mazeInput);
			mazeFromString.printMaze();
			System.out.println("Width: " + mazeFromString.getWidth());
			System.out.println("Height: " + mazeFromString.getHeight());
			System.out.println("Start Coordinates: (" + mazeFromString.getStartCoordinates()[0] + ", "
					+ mazeFromString.getStartCoordinates()[1] + ")");
			System.out.println("Goal Coordinates: (" + mazeFromString.getGoalCoordinates()[0] + ", "
					+ mazeFromString.getGoalCoordinates()[1] + ")");
			System.out.println("Maze Input as String:\n" + mazeFromString.getMazeInput());

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Create a Maze object from a file
			Maze maze = new Maze(new File("C:\\Users\\peter\\git\\cs-620\\mazes\\maze-001.txt"));

			// Compute the maze path and store it in the mazeCompute property
			boolean pathFound = maze.setMazeCompute();

			if (pathFound) {
				System.out.println("Path found from 'S' to 'G':");
			} else {
				System.out.println("No path found from 'S' to 'G'.");
			}

			// Print the original maze input
			System.out.println("Original Maze:\n" + maze.getMazeInput());

			// Print the computed maze (with 'P' and '?' markings)
			System.out.println("Computed Maze:\n" + maze.getMazeCompute());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
