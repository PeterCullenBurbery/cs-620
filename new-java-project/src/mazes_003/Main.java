package mazes_003;

public class Main {
    public static void main(String[] args) {
        // Create a 20x30 grid graph and visualize it before and after DFS
    	MazeFun gridGraph = new MazeFun(7,7);
//        MazeFun gridGraph = new MazeFun(18,70);

//        // Show the graph before DFS
//        System.out.println("Grid Graph before DFS:");
//        System.out.println(gridGraph.getPreDfsGraph());
//
//        // Show the graph after DFS and edge removal
//        System.out.println("\nGrid Graph after DFS and edge removal:");
//        System.out.println(gridGraph.getPostDfsGraph());

        // Show the processed post-DFS graph
        System.out.println("\nProcessed Grid Graph after DFS:");
        System.out.println(gridGraph.getPostDfsProcessed());
        System.out.println(gridGraph.getSolve());
    }
}
