package new_best_002;

public class Main {
	public static void main(String[] args) {
//		// Create a 3x3 grid graph and solve the maze
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(3, 3);
//
//		System.out.println("Initial Grid Graph:");
////        System.out.println(gridGraph3x3.getGridGraphString());
//
//		System.out.println("Maze after DFS and edge removal:");
//		System.out.println(gridGraph3x3.getMazeString());
//
//		System.out.println("Path from start to goal:");
//		System.out.println(gridGraph3x3.getPath());
//		System.out.println(gridGraph3x3.getPathEdges());
//		System.out.println("Tried nodes:");
//		System.out.println(gridGraph3x3.getTried());
//		System.out.println(gridGraph3x3.getTriedEdges());
//		System.out.println("Path coordinates:");
//		for (int[] coord : gridGraph3x3.getPathCoordinates()) {
//			System.out.println(Arrays.toString(coord));
//		}
//
//		System.out.println("Tried coordinates:");
//		for (int[] coord : gridGraph3x3.getTriedCoordinates()) {
//			System.out.println(Arrays.toString(coord));
//		}
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(150,150/*3,3*/);
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(30,30/*3,3*/);

//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(30,30/*3,3*/);
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(20,20/*3,3*/);
//		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(6,6/*3,3*/);
//        System.out.println("Original Grid Graph (All Edges Present):");
//        System.out.println(gridGraph3x3.getGridGraphString());
		/* I will try to make the biggest maze that can fit onto the screen. */
		GridGraphVisualizer gridGraph3x3 = new GridGraphVisualizer(27, 110/* 3,3 */);
		System.out.println("Maze after DFS and Edge Removal:");

		System.out.println(gridGraph3x3.getTransformedSolveString());
/*		int count = 75;
		for (int i = 0; i < count; i++) {
			System.out.println(); // prints a new line
		}
		System.out.println("Correct forks--"+gridGraph3x3.getCorrectForks().size()+" Incorrect forks--"+gridGraph3x3.getIncorrectForks().size());
//		System.out.println(gridGraph3x3.getCorrectForks().size());
//		System.out.println("Incorrect forks--");
//		System.out.println(gridGraph3x3.getIncorrectForks().size());
		count = 1;
		for (int i = 0; i < count; i++) {
			System.out.println(); // prints a new line
		}
		System.out.println(gridGraph3x3.getTransformedMazeString());
		*/
//        System.out.println("Solved Maze:");

//        System.out.println("The number of correct moves was "+gridGraph3x3.getCorrectMovesCount());
//        System.out.println("The number of incorrect moves was "+gridGraph3x3.getIncorrectMovesCount());
//        System.out.println("The number of incorrect forks was "+gridGraph3x3.getBacktrackForksCount());
//        System.out.println("Nodes where backtracking occurred: " + gridGraph3x3.getBacktrackForkNodes());
//        System.out.println(gridGraph3x3.getTriedEdges());
//        System.out.println(gridGraph3x3.getForksString());
//        System.out.println("forks--"+gridGraph3x3.getForks());
//        System.out.println("path forks--"+gridGraph3x3.getPathForks());
//        System.out.println(gridGraph3x3.getTried().toString());
//        System.out.println("correct forks--"+gridGraph3x3.getCorrectForks());
//        System.out.println("incorrect forks--"+gridGraph3x3.getIncorrectForks());
//        
//        gridGraph3x3.printNonZeroValuesInAdjacencyMatrix();
//        System.out.println();
//        gridGraph3x3.printNonZeroValuesForPathForks();

	}
}
