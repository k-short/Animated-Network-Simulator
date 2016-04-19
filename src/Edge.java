import java.util.Random;

/**
 * Created by ken12_000 on 1/29/2016.
 */
public class Edge {
    //Weight of the edge
    private int weight;

    //Start and end coordinates of the edges
    private Node start;
    private Node end;

    public Edge(Node x, Node y){
        start = x;
        end = y;
        weight = getRandomWeight();
    }

    public Node getStart() {
        return start;
    }

    public int getWeight() {
        return weight;
    }

    public Node getEnd() {
        return end;
    }

    /**
     * Get a random number between 1 and 7 (inclusive)
     */
    private int getRandomWeight(){
        int min = 1;
        int max = 7;

        Random rm = new Random();
        return rm.nextInt((max - min) + 1) + min;
    }
}
