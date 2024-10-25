package mazes_004;



public class Main {

	public static void main(String[] args) {
		// Create a 20x30 grid graph and visualize it before and after DFS
//		MazeFun gridGraph = new MazeFun(10, 20);
		MazeFun gridGraph = new MazeFun(7,7);
//		System.out.println("Pre-DFS Grid Graph:");
//		System.out.println(gridGraph.generateGridGraphPreDfs());
//
//		System.out.println("\nPost-DFS Grid Graph:");
//		System.out.println(gridGraph.generateGridGraphPostDfs());

		System.out.println("\nProcessed Grid Graph after DFS:");
		System.out.println(gridGraph.getPostDfsProcessed());
		
		System.out.println("solved");
		System.out.println(gridGraph.getSolve());

	}

}
