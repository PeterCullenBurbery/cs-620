package learn_mazes_006;

public class Node {
    private int row;
    private int column;
    private int number;

    // Constructor to initialize row, column, and number
    public Node(int row, int column, int number) {
        this.row = row;
        this.column = column;
        this.number = number;
    }

    // Getter for row
    public int getRow() {
        return row;
    }

    // Getter for column
    public int getColumn() {
        return column;
    }

    // Getter for number
    public int getNumber() {
        return number;
    }

    // Override toString to display node information
    @Override
    public String toString() {
        return "Node{" +
                "row=" + row +
                ", column=" + column +
                ", number=" + number +
                '}';
    }

    // Override equals to compare nodes based on row, column, and number
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Node node = (Node) obj;

        if (row != node.row) return false;
        if (column != node.column) return false;
        return number == node.number;
    }


}
