package mazes;

import java.io.FileNotFoundException;

public class Main1 {
    public static void main(String[] args) throws FileNotFoundException {
        MazeUtil1 mazeUtil1 = new MazeUtil1();

        // Directly print the result of getMazeResult
        System.out.println(mazeUtil1.getMazeResult("C:\\Users\\peter\\git\\cs-620\\mazes\\mazes-without-dimensions\\maze-001.txt"));
        System.out.println();
        System.out.println(mazeUtil1.getMazeResult("C:\\Users\\peter\\git\\cs-620\\mazes\\mazes-without-dimensions\\maze-002.txt"));
        System.out.println();
        System.out.println(mazeUtil1.getMazeResult("C:\\Users\\peter\\git\\cs-620\\mazes\\mazes-without-dimensions\\maze-003.txt"));
        System.out.println();
        System.out.println(mazeUtil1.getMazeResult("C:\\Users\\peter\\git\\cs-620\\mazes\\mazes-without-dimensions\\maze-004.txt"));
    }
}

