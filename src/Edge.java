/**
 * Created by ken12_000 on 1/29/2016.
 */
public class Edge {
    //Weight of the edge
    private int weight;

    //Start and end coordinates of the edges
    private double start;
    private double end;

    public Edge(double x, double y, int w){
        start = x;
        end = y;
        weight = w;
    }

    public double getStart() {
        return start;
    }

    public int getWeight() {
        return weight;
    }

    public double getEnd() {
        return end;
    }
}
