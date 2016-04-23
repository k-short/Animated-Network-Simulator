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
    private final String router4 = "ROUTER_4";
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

    /**
     * Pre-determined path for the red message bubble.
     * Passed int:  0 == red bubble path
     *              1 == blue bubble path
     */
    public ArrayList<Node> getPath(int color){
        ArrayList<Node> path = new ArrayList<>();
        ArrayList<Node> shortestPath;
        ArrayList<Node> routers = new ArrayList<>();

        Node sourceNode = null;
        Node targetNode = null;

        //Find the router1 node and make it source node
        for(Node node : nodeList){
            if(node.getType().equals(router1)){
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
            if(node.getType().equals(router4)) {
                targetNode = node;
                routers.add(node);
            }
        }

        //Use Dijkstra's algorithm to find shortest path between source and target node
        if(color == 0) {
            shortestPath = redDijkstra(routers, sourceNode, targetNode);
        }else
            shortestPath = blueDijkstra(routers, sourceNode, targetNode);

        //Find the host 1 node and add it first
        for(Node node : nodeList){
            if(node.getType().equals(host1))
                path.add(0, node);
        }

        //Add the nodes of the shortest path to the path list
        for(Node node : shortestPath){
            path.add(node);
        }

        //Find the host 2 node and add it second-to-last
        for(Node node : nodeList){
            if(node.getType().equals(host2))
                path.add(node);
        }

        //Find the layer 2 node and add it last
        for(Node node : nodeList){
            if(node.getType().equals(layer2))
                path.add(node);
        }

        return path;
    }

    /**
     * Pre-determined path for ACK.
     */
    public ArrayList<Node> getPathACK(){
        ArrayList<Node> ack = new ArrayList<>();

        //Find router1 and make it the path
        for(Node node : nodeList){
            if(node.getType().equals(router1)){
                ack.add(node);
            }
        }

        return ack;
    }

    /**
     * Return the shortest path betweent the source and target node
     * Uses red weights for edges
     */
    private ArrayList<Node> redDijkstra(ArrayList<Node> graph, Node source, Node target){
        ArrayList<Node> q = new ArrayList<>();
        ArrayList<Node> shortestPath = new ArrayList<>();
        boolean targetFound = false;

        //For each node in the graph
        for(Node node : graph){
            node.setDist(1000);  //infinite distance from source
            node.setPrev(null);  //prev node in optimal path
            q.add(node);        //all nodes start out in q
        }

        source.setDist(0);  //Distance from source to source

        Node u;
        while(q.size() != 0 && !targetFound){
            //Source node will be selected first
            u = getMinimumDistanceNode(q);

            //Once u = target node, then done
            //Otherwise, look for shorter paths
            if(u != target){
                q.remove(u);

                ArrayList<Node> neighbors = getNeighbors(u); //All neighbors of u
                ArrayList<Node> validNeighbors = new ArrayList<>(); //neighbors still in q

                //Find all neighbors still in q
                for(Node neigh : neighbors){
                    if(q.contains(neigh)){
                        validNeighbors.add(neigh);
                    }
                }

                //For each neighbor of u
                for(Node neighbor : validNeighbors){
                    int alt = u.getDist() + getEdge(u, neighbor).getRedWeight();

                    if(alt < neighbor.getDist()){
                        neighbor.setDist(alt);
                        neighbor.setPrev(u);
                    }
                }
            }
            else{
                targetFound = true;
            }
        }

        //Traverse previous nodes from target and add them to shortest path
        Node targ = target;
        while(targ.getPrev() != null){
            shortestPath.add(0, targ);
            targ = targ.getPrev();
        }
        shortestPath.add(0, targ);

        /*String str = "";
        for(Node node : shortestPath){
            str += node.getType();
        }
        System.out.println(str);*/

        return shortestPath;
    }

    /**
     * Return the shortest path betweent the source and target node
     * Uses blue weights for edges
     */
    private ArrayList<Node> blueDijkstra(ArrayList<Node> graph, Node source, Node target){
        ArrayList<Node> q = new ArrayList<>();
        ArrayList<Node> shortestPath = new ArrayList<>();
        boolean targetFound = false;

        //For each node in the graph
        for(Node node : graph){
            node.setDist(1000);  //infinite distance from source
            node.setPrev(null);  //prev node in optimal path
            q.add(node);        //all nodes start out in q
        }

        source.setDist(0);  //Distance from source to source

        String str1 = "";
        for(Node node : graph){
            str1 += node.getType();
        }
        System.out.println(str1);

        Node u;
        while(q.size() != 0 && !targetFound){
            //Source node will be selected first
            u = getMinimumDistanceNode(q);

            //Once u = target node, then done
            //Otherwise, look for shorter paths
            if(u != target){
                q.remove(u);

                ArrayList<Node> neighbors = getNeighbors(u); //All neighbors of u
                ArrayList<Node> validNeighbors = new ArrayList<>(); //neighbors still in q

                //Find all neighbors still in q
                for(Node neigh : neighbors){
                    if(q.contains(neigh)){
                        validNeighbors.add(neigh);
                    }
                }

                //For each neighbor of u
                for(Node neighbor : validNeighbors){
                    int alt = u.getDist() + getEdge(u, neighbor).getBlueWeight();

                    if(alt < neighbor.getDist()){
                        neighbor.setDist(alt);
                        neighbor.setPrev(u);
                    }
                }
            }
            else{
                targetFound = true;
            }
        }

        //Traverse previous nodes from target and add them to shortest path
        Node targ = target;
        while(targ.getPrev() != null){
            shortestPath.add(0, targ);
            targ = targ.getPrev();
        }
        shortestPath.add(0, targ);

        /*String str = "";
        for(Node node : shortestPath){
            str += node.getType();
        }
        System.out.println(str);*/

        return shortestPath;
    }

    /**
     * Get the node with the smallest distance.
     */
    private Node getMinimumDistanceNode(ArrayList<Node> list){
        Node minNode = null;
        int min = 10000;

        //Get the node with the smallest distance
        for(Node cur : list){
            if(cur.getDist() < min){
                minNode = cur;
                min = cur.getDist();
            }
        }

        return minNode;
    }

    /**
     * Get the neighbors of the given node.
     */
    private ArrayList<Node> getNeighbors(Node node){
        ArrayList<Node> neighbors = new ArrayList<>();

            for(Edge edge : edgeList){
                if(node == edge.getStart()){
                    neighbors.add(edge.getEnd());
                }
                else if(node == edge.getEnd()){
                    neighbors.add(edge.getStart());
                }
            }

        return neighbors;
    }

    /**
     * Return the edge that contains passed nodes
     */
    private Edge getEdge(Node node1, Node node2){
        Edge edge = null;

        for(Edge cur : edgeList){
            if(cur.getStart() == node1 && cur.getEnd() == node2){
                edge = cur;
            }
            else if(cur.getStart() == node2 && cur.getEnd() == node1){
                edge = cur;
            }
        }

        return edge;
    }

    public ArrayList<Edge> getEdgeList(){
        return edgeList;
    }
}
