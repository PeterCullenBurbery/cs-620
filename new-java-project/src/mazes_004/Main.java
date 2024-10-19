package mazes_004;

import mazes_003.MazeFun;

public class Main {

	public static void main(String[] args) {
		// Create a 20x30 grid graph and visualize it before and after DFS
		MazeFun gridGraph = new MazeFun(10, 20);
		System.out.println("Pre-DFS Grid Graph:");
		System.out.println(gridGraph.generateGridGraphPreDfs());

		System.out.println("\nPost-DFS Grid Graph:");
		System.out.println(gridGraph.generateGridGraphPostDfs());

		System.out.println("\nProcessed Grid Graph after DFS:");
		System.out.println(gridGraph.getPostDfsProcessed());

	}

}
