/**
 * @since 2024-W42-5 11.05.54.607 -0400
 * @author peter
 */
package maze_package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */
public class Maze {
    private int width;
    private int height;
    private char[][] mazeLayout;
    private String mazeInput; // Field to store the entire content of the maze input

    // Constructor to initialize the maze object from the mazeInput string
    public Maze(String mazeInput) {
        this.mazeInput = mazeInput; // Store the input string
        parseMazeInput(); // Parse the input to extract width, height, and maze layout
    }

    // Method to parse the mazeInput field and extract width, height, and layout
    private void parseMazeInput() {
        String[] lines = mazeInput.split("\n");
        
        // The first line contains the width and height
        String[] dimensions = lines[0].split(" ");
        this.width = Integer.parseInt(dimensions[0]);
        this.height = Integer.parseInt(dimensions[1]);

        // Initialize the mazeLayout array based on the dimensions
        mazeLayout = new char[height][width];

        // Populate the mazeLayout from the rest of the input
        for (int i = 0; i < height; i++) {
            String line = lines[i + 1]; // Maze data starts from the second line
            for (int j = 0; j < width; j++) {
                mazeLayout[i][j] = line.charAt(j);
            }
        }
    }

    // Method to print the maze
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mazeLayout[i][j]);
            }
            System.out.println();
        }
    }

    // Getter for the maze input string
    public String getMazeInput() {
        return mazeInput;
    }

    public static void main(String[] args) {
        // Read the file content as a string (this could be handled externally)
        String fileContent = readFile("C:\\Users\\peter\\git\\cs-620\\mazes\\maze-001.txt");

        // Create a Maze object using the file content
        Maze maze = new Maze(fileContent);

        // Print the maze layout
        maze.printMaze();
    }

    // Utility method to read the content of the file
    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}

