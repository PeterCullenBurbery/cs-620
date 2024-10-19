package mazes;

public class MazeTest004 {
    private int width;
    private int height;
    private char[][] mazeLayout;

    // Constructor to initialize MazeTest004 with width, height, and maze layout
    public MazeTest004(int width, int height, char[][] mazeLayout) {
        this.width = width;
        this.height = height;
        this.mazeLayout = mazeLayout;
    }

    // Getters for width, height, and maze layout
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getMazeLayout() {
        return mazeLayout;
    }

    // Method to print the maze (for debugging or output)
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeLayout[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
