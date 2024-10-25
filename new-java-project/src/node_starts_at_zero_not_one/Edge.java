package node_starts_at_zero_not_one;

public class Edge {
    private Node startNode;
    private Node endNode;
    private int edgeRowCoordinate;    // Sum of start and end node rows
    private int edgeColumnCoordinate; // Sum of start and end node columns

    // Constructor to initialize the edge with its start and end nodes
    public Edge(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.edgeRowCoordinate = calculateEdgeRowCoordinate();
        this.edgeColumnCoordinate = calculateEdgeColumnCoordinate();
    }

    // Calculate the row coordinate by summing the rows of the start and end nodes
    private int calculateEdgeRowCoordinate() {
        return startNode.getRow() + endNode.getRow();
    }

    // Calculate the column coordinate by summing the columns of the start and end nodes
    private int calculateEdgeColumnCoordinate() {
        return startNode.getCol() + endNode.getCol();
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getEdgeRowCoordinate() {
        return edgeRowCoordinate;
    }

    public int getEdgeColumnCoordinate() {
        return edgeColumnCoordinate;
    }

    @Override
    public String toString() {
        return "Edge between Node " + startNode.getNodeNumber() + " and Node " + endNode.getNodeNumber() + " at (" + edgeRowCoordinate + ", " + edgeColumnCoordinate + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;

        // Check if the edge is equal regardless of direction
        return (startNode.equals(edge.startNode) && endNode.equals(edge.endNode)) ||
               (startNode.equals(edge.endNode) && endNode.equals(edge.startNode));
    }

    @Override
    public int hashCode() {
        // Ensure bidirectional equality by using the sum of the two nodes' hash codes
        return startNode.hashCode() + endNode.hashCode();
    }



}
