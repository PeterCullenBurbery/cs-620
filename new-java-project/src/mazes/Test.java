package mazes;

import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		try {
			// Create a Maze object from a file
			MazeWithoutCompute mazeFromFile = new MazeWithoutCompute(new File("C:\\Users\\peter\\git\\cs-620\\mazes\\maze-001.txt"));
			mazeFromFile.printMaze();
			System.out.println("Width: " + mazeFromFile.getWidth());
			System.out.println("Height: " + mazeFromFile.getHeight());
			System.out.println("Start Coordinates: (" + mazeFromFile.getStartCoordinates()[0] + ", "
					+ mazeFromFile.getStartCoordinates()[1] + ")");
			System.out.println("Goal Coordinates: (" + mazeFromFile.getGoalCoordinates()[0] + ", "
					+ mazeFromFile.getGoalCoordinates()[1] + ")");
			System.out.println("Maze Input as String:\n" + mazeFromFile.getMazeInput());
			System.out.println("replace S with dot, G with dot--\n"+mazeFromFile.getReplacestartwithdotreplacegwithdot());

//			// You can also create a Maze object from a string
//			String mazeInput = "5 5\nS..##\n.####\n...##\n.#.##\n.#G##\n";
//			MazeWithoutCompute mazeFromString = new MazeWithoutCompute(mazeInput);
//			mazeFromString.printMaze();
//			System.out.println("Width: " + mazeFromString.getWidth());
//			System.out.println("Height: " + mazeFromString.getHeight());
//			System.out.println("Start Coordinates: (" + mazeFromString.getStartCoordinates()[0] + ", "
//					+ mazeFromString.getStartCoordinates()[1] + ")");
//			System.out.println("Goal Coordinates: (" + mazeFromString.getGoalCoordinates()[0] + ", "
//					+ mazeFromString.getGoalCoordinates()[1] + ")");
//			System.out.println("Maze Input as String:\n" + mazeFromString.getMazeInput());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}