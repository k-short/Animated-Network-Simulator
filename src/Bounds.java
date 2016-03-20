/**
 * Created by ken12_000 on 2/11/2016.
 *
 * Bounds added to objects to determine if message ball is at same location in coordinates.
 */
public class Bounds {
    private double x1;
    private double x2;
    private double y1;
    private double y2;

    private String type;

    public Bounds(double initX, double endX, double initY, double endY, String t){
        x1 = initX;
        x2 = endX;
        y1 = initY;
        y2 = endY;
        type = t;
    }

    /**
     * Calculate if given coordinates are within these bounds.
     * @return true if in bounds
     */
    public boolean isInBounds(double xPrim, double yPrim, double size) {
        //Make x and y in CENTER of bubble
        double x = xPrim + (size/2);
        double y = yPrim + (size/2);

        if ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2)) {
            return true;
        }else
            return false;
    }

    public String getType(){
        return type;
    }
}
