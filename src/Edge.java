import java.util.Random;

/**
 * Created by ken12_000 on 1/29/2016.
 */
public class Edge {
    //Weight of the edge
    private int redWeight;
    private int blueWeight;

    //Start and end coordinates of the edges
    private Node start;
    private Node end;

    //Coordinates of the nodes
    private int startX;
    private int startY;

    private int endX;
    private int endY;


    public Edge(Node x, Node y){
        start = x;
        end = y;

        redWeight = getRandomWeight();
        blueWeight = getRandomWeight();
    }

    public Node getStart() {
        return start;
    }

    public int getRedWeight() {
        return redWeight;
    }

    public int getBlueWeight() {
        return blueWeight;
    }

    public Node getEnd() {
        return end;
    }

    /**
     * Return x coordinate of the center of the edge
     */
    public int getCenterX(){
        int length = Math.abs(endX - startX);

        if(startX < endX)
            return startX + (length/2);
        else
            return startX - (length/2);
    }

    /**
     * Return the y coordinate of the center of the edge
     */
    public int getCenterY(){
        if(startY < endY)
            return startY + Math.abs(startY - endY)/2;
        else
            return startY - Math.abs(startY - endY)/2;
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

    /**
     * Set edge coordinates as appears on-screen
     */
    public void setCoordinates(int sX, int sY, int eX, int eY){
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
    }
}
