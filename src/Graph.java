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
}
