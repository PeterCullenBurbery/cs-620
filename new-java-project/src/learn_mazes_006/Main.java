package learn_mazes_006;



public class Main {
    public static void main(String[] args) {
//        // Create an object of MazeFun with 2 rows and 3 columns
//        MazeFun mazeFun = new MazeFun(2, 3);
//
//        // Print the original grid graph
//        mazeFun.printOriginalGraph();
//mazeFun.printModifiedGraph();
//        // Print the path and tried nodes after DFS
//        mazeFun.printPathAndTried();
    	   MazeFun mazeFun = new MazeFun(5, 5);  // Create a 5x5 maze

           // Print the visual strings for the original, traversed, and path/tried graphs
           System.out.println("Original Graph Visualizer:");
           System.out.println(mazeFun.getOriginalGraphVisual());

           System.out.println("Traversed Graph Visualizer:");
           System.out.println(mazeFun.getTraversedGraphVisual());
    }
}
