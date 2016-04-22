/**
 * Created by ken12_000 on 1/29/2016.
 */
public class Node {
    //Coordinates for the node
    private double xCoord;
    private double yCoord;

    //Used for dijkstra
    private int dist;
    private Node prev;

    //Type for certain nodes
    private String type;

    public Node(double x, double y, String t){
        xCoord = x;
        yCoord = y;
        type = t;
    }

    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public String getType() {
        return type;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
}
