package mazes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MazeWithoutCompute {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private String mazeInput;  // Field to store the entire content of the maze input
    private String replacestartwithdotreplacegwithdot;  // Transformed version of the maze
    
    private int[] startCoordinates; // Stores the coordinates of 'S' (start)
    private int[] goalCoordinates;  // Stores the coordinates of 'G' (goal)

    // Constructor to initialize the maze from a string input
    public MazeWithoutCompute(String mazeInput) {
        this.mazeInput = mazeInput;
        this.startCoordinates = new int[2];
        this.goalCoordinates = new int[2];
        parseAndGenerateTransformedMaze();  // Combine parsing and transforming into one method
    }

    // Constructor to initialize the maze from a file
    public MazeWithoutCompute(java.io.File file) throws IOException {
        this.mazeInput = readFile(file); // Read file content and store in mazeInput
        this.startCoordinates = new int[2];
        this.goalCoordinates = new int[2];
        parseAndGenerateTransformedMaze();  // Combine parsing and transforming into one method
    }

    // Method to parse the mazeInput field, extract width, height, layout, coordinates,
    // and generate the transformed maze in one go
    private void parseAndGenerateTransformedMaze() {
        String[] lines = mazeInput.split("\n");

        // The first line contains the width and height
        String[] dimensions = lines[0].split(" ");
        this.width = Integer.parseInt(dimensions[0]);
        this.height = Integer.parseInt(dimensions[1]);

        // Initialize the mazeLayout array based on the dimensions
        mazeLayout = new char[height][width];

        StringBuilder transformedMaze = new StringBuilder();

        // Populate the mazeLayout, find the start and goal positions,
        // and generate the transformed version of the maze
        for (int i = 0; i < height; i++) {
            String line = lines[i + 1]; // Maze data starts from the second line
            for (int j = 0; j < width; j++) {
                mazeLayout[i][j] = line.charAt(j);
                if (mazeLayout[i][j] == 'S') {
                    startCoordinates[0] = i; // Row of 'S'
                    startCoordinates[1] = j; // Column of 'S'
                    transformedMaze.append('.'); // Replace 'S' with '.'
                } else if (mazeLayout[i][j] == 'G') {
                    goalCoordinates[0] = i; // Row of 'G'
                    goalCoordinates[1] = j; // Column of 'G'
                    transformedMaze.append('.'); // Replace 'G' with '.'
                } else {
                    transformedMaze.append(mazeLayout[i][j]); // Keep other characters as is
                }
            }
            transformedMaze.append("\n");
        }

        // Store the transformed maze as a string
        replacestartwithdotreplacegwithdot = transformedMaze.toString();
    }

    // Getter for the maze input string
    public String getMazeInput() {
        return mazeInput;
    }

    // Getter for the start coordinates
    public int[] getStartCoordinates() {
        return startCoordinates;
    }

    // Getter for the goal coordinates
    public int[] getGoalCoordinates() {
        return goalCoordinates;
    }

    // Getter for width
    public int getWidth() {
        return width;
    }

    // Getter for height
    public int getHeight() {
        return height;
    }

    // Getter for the transformed maze
    public String getReplacestartwithdotreplacegwithdot() {
        return replacestartwithdotreplacegwithdot;
    }

    // Method to print the maze layout
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeLayout[i][j]);
            }
            System.out.println();
        }
    }

    // Utility method to read the content of a file
    private static String readFile(java.io.File file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }
}
