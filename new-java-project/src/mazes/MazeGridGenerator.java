package mazes;
public class MazeGridGenerator {

    // Function to generate a grid for a maze with walls between internal cells
    public static String generateGrid(int rows, int cols) {
        // The output grid will be (2*rows - 1) by (2*cols - 1)
        int gridHeight = 2 * rows - 1;
        int gridWidth = 2 * cols - 1;
        char[][] grid = new char[gridHeight][gridWidth];

        // Fill the grid with open spaces (.) and walls (#)
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    // Place a wall (#) between internal cells
                    grid[i][j] = '#';
                } else {
                    // Place an open space (.) everywhere else
                    grid[i][j] = '.';
                }
            }
        }

        // Convert the grid to a string representation
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                result.append(grid[i][j]);
            }
            result.append("\n");
        }

        return result.toString();
    }

    public static void main(String[] args) {
        // Test for 2x2 Grid
        System.out.println("2x2 Grid:");
        System.out.println(generateGrid(2, 2));

        // Test for 3x3 Grid
        System.out.println("3x3 Grid:");
        System.out.println(generateGrid(3, 3));
        System.out.println(generateGrid(7, 7));
    }
}

