package grid_graph_to_Graph;

import java.util.Objects;



class Node {
	private int row;
	private int col;
	private int nodeNumber; // Unique number for each node

	// Constructor for a node
	public Node(int row, int col, int nodeNumber) {
		this.row = row;
		this.col = col;
		this.nodeNumber = nodeNumber;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getNodeNumber() {
		return nodeNumber;
	}

	@Override
	public String toString() {
		return "Node " + nodeNumber + " at (" + row + ", " + col + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Node node = (Node) o;
		return row == node.row && col == node.col && nodeNumber == node.nodeNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col, nodeNumber);
	}
}

