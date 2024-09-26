/**
 * @since 2024-W39-3 20.53.33.324 -0400
 * @author peter
 */
package com.peter_burbery.breadth_first_search.breadth_first_search;

/**
 * 
 */
public class Vertex {
    private boolean visited;
    private int index;

    // Constructor
    public Vertex(int index) {
        visited = false;
        this.index = index;
    }

    // Getter for visited
    public boolean getVisited() {
        return visited;
    }

    // Getter for index
    public int getIndex() {
        return index;
    }

    // Setter for visited
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
