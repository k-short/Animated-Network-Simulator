/**
 * Created by ken12_000 on 1/29/2016.
 */
public class Node {
    //Coordinates for the node
    private double xCoord;
    private double yCoord;

    //Node type
    private String type;

    //Types of nodes
    private final String ROUTER = "ROUTER";
    private final String HOST = "HOST";
    private final String LAYER = "LAYER";

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

    public String getType(){
        return type;
    }
}
