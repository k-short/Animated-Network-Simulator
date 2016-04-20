import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ken12_000 on 2/1/2016.
 */
public class Graph {
    //Lists
    private ArrayList<Node> nodeList;
    private ArrayList<Edge> edgeList;

    //Types for certain nodes
    private final String host1 = "HOST_1";
    private final String host2 = "HOST_2";
    private final String layer2 = "LAYER_2";
    private final String router1 = "ROUTER_1";
    private final String router = "ROUTER";

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
        Node[] shortestPath;
        ArrayList<Node> routers;

        Node sourceNode;
        Node targetNode;

        //Find the router1 node and make it source node
        for(Node node : nodeList){
            if(node.getType().equals(host2)){
                sourceNode = node;
                routers.add(node);
            }
        }

        //Find all router nodes and add them to router array
        for(Node node : nodeList){
            if(node.getType().equals(router))
                routers.add(node);
        }

        //Find the router4 node and make it the target node
        for(Node node : nodeList){
            if(node.getType().equals(host2)) {
                targetNode = node;
                routers.add(node);
            }
        }

        //Use Dijkstra's algorithm to find shortest path between source and target node
        shortestPath = Dijkstra(ArrayList<Node> routers, sourceNode, targetNode);

        //Find the host 1 node and add it first
        for(Node node : nodeList){
            if(node.getType().equals(host1))
                red.add(0, node);
        }

        //Find the host 2 node and add it second-to-last
        for(Node node : nodeList){
            if(node.getType().equals(host2))
                red.add(node);
        }

        //Find the layer 2 node and add it last
        for(Node node : nodeList){
            if(node.getType().equals(layer2))
                red.add(node);
        }

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

    /**
     * Return the shortest path betweent the source and target node
     */
    private Node[] Dijkstra(ArrayList<Node> graph, Node source, Node target){
        ArrayList<Node> q = new ArrayList<>();
        ArrayList<Integer> dist = new ArrayList<>();
        ArrayList<Node> prev = new ArrayList<>();

        //For each node in the graph
        for(Node node : graph){
            node.setDist(1000);  //infinite distance from source
            node.setPrev(null);  //prev node in optimal path
            q.add(node);        //all nodes start out in q
        }

        source.setDist(0);  //Distance from source to source

        Node u;
        while(q.size() != 0){
            //Source node will be selected first

        }

        
    }

    /**
     * Get the distance between two nodes
     */
    private int getDistance(Node node1, Node node2){
        return 0;
    }
}
