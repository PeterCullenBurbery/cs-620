package new_best_002;

public class Main {
	public static void main(String[] args) {

		/* I will try to make the biggest maze where both the maze and the solved maze can fit on to the screen. */
		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(12, 114);
		System.out.println("Maze:");
		System.out.println(gridGraph3x3.getTransformedMazeString());
		System.out.println("Solved maze");
		System.out.println(gridGraph3x3.getTransformedSolveString());

	}
}
