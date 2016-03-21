import java.util.ArrayList;

/**
 * Created by ken12_000 on 2/1/2016.
 */
public class Graph {
    private ArrayList<Node> nodeList;
    private ArrayList<Edge> edgeList;

    public Graph(){
        nodeList = new ArrayList<>();
        edgeList = new ArrayList<>();
    }

    public void addNode(Node n){
        nodeList.add(n);
    }

    public void addEdge(Edge e){
        edgeList.add(e);
    }

    public ArrayList<Node> getPath(){
        return nodeList;
    }

    /**
     * Pre-determined path for the red message bubble.
     */
    public ArrayList<Node> getPathRed(){
        ArrayList<Node> red = new ArrayList<>();
        red.add(nodeList.get(0));
        red.add(nodeList.get(3));
        red.add(nodeList.get(4));
        red.add(nodeList.get(1));
        red.add(nodeList.get(6));

        return red;
    }

    /**
     * Pre-determined path for the blue message bubble
     */
    public ArrayList<Node> getPathBlue(){
        ArrayList<Node> blue = new ArrayList<>();

        blue.add(nodeList.get(0));
        blue.add(nodeList.get(3));
        blue.add(nodeList.get(5));
        blue.add(nodeList.get(2));
        blue.add(nodeList.get(7));

        return blue;
    }

    /**
     * Pre-determined path for ACK.
     */
    public ArrayList<Node> getPathACK(){
        ArrayList<Node> ack = new ArrayList<>();

        ack.add(nodeList.get(8));

        return ack;
    }
}
