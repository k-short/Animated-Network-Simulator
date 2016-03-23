import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.CRC32;

/**
 * Created by ken12_000 on 1/26/2016.
 *
 * Create message bubble and handle bubble animations.
 */
public class MessageBubble extends Ellipse2D{
    // current layer
    int currentLayer = 1;

    //Target node in path and coords
    private Node target;
    private double targetX;
    private double targetY;

    //Values for if target node is left/right/above/below current coordinates
    private boolean toRight;
    private boolean above;

    //Current path for bubble
    private ArrayList<Node> path;

    //Values for moving message bubble
    double deltaX;
    double deltaY;
    double direction;

    //Bubble graphic
    private Ellipse2D bubble;

    //Bubble parameters
    private double height;
    private double width;
    private double startX; //The X coordinate of the upper-left corner of the framing rectangle of this Ellipse2D.
    private double startY; //The Y coordinate of the upper-left corner of the framing rectangle of this Ellipse2D.

    //Current bubble coords
    private double currX;
    private double currY;

    //Bubble color
    private Color color;

    //Speed for the bubble to move
    private int speed = 1;

    //TYPE used for special ACK message bubble
    private String type = "";

    //Current node in the array
    private int currentIndex = 0;

    //String to hold data in Bubble
    private String data = "INIT";

    //Bounds for the host and dest layers
    Bounds[] hostLayerBounds;
    Bounds[] destLayerBounds;

    //Bounds for the routers
    Bounds[] routerBounds;

    //Bounds currently looking for
    Bounds[] currentBounds;

    //text pane to show bubble data
    JTextPane bubblePane;

    //sine panel
    SineWavePanel sinePanel;

    //Message being set in the bubble
    private String message;

    //Data-link frame
    String frame;

    //Current packet, changes in each layer
    private String currentPacket;

    //Get CRC-32 value from data stream
    private CRC32 crc;

    public MessageBubble(double x, double y, double w, double h, Color c){
        startX = x;
        startY = y;
        currX = x;
        currY = y;
        width = w;
        height = h;
        color = c;

        bubble = new Ellipse2D.Double(startX, startY, width, height);

        message = "A";
        frame = getDataLinkFrame(message);
    }

    public MessageBubble(JTextPane pane, SineWavePanel sine){
        bubblePane = pane;
        sinePanel = sine;

        message = "A";
        frame = getDataLinkFrame(message);
    }

    /*
     * Set bubble attributes.
     * Starting coords, size, and color.
     */
    public void setAttributes(double x, double y, double w, double h, Color c){
        startX = x;
        startY = y;
        currX = x;
        currY = y;
        width = w;
        height = h;
        color = c;

        bubble = new Ellipse2D.Double(startX, startY, width, height);
    }

    public void draw(Graphics2D g2){
        g2.setColor(color);
        //g2.setStroke(stroke);
        g2.fill(bubble);

    }

    /*
     * Determine the next coordinates for the bubble to move to.
     */
    public boolean move(){
        //Get coordinates of target node
        targetX = target.getXCoord();
        targetY = target.getYCoord();

        //If at target node, return false
        if(atTargetNode(targetX, targetY)){
            return false;
        }
        else {  //Get new coords
            //Get moving values
            deltaX = targetX - currX;
            deltaY = targetY - currY;
            direction = Math.atan2(deltaY, deltaX); // Math.atan2(deltaY, deltaX) does the same thing but checks for deltaX being zero to prevent divide-by-zero exceptions

            currX = currX + (speed * Math.cos(direction));
            currY = currY + (speed * Math.sin(direction));

            //Set new values to the bubble
            setFrame(currX, currY, width, height);
        }
        //Update message bubble data based on coordinates IF not an ACK bubble
        if(type != "ACK") {
            updateData();
        }

        return true;
    }

    /*
     * Determine where the bubble goes next.
     */
    public void setTarget(Node t){
        target = t;

        //Determine where target is compared to current node
        if(target.getXCoord() >= currX){
            toRight = true;
        }else{
            toRight = false;
        }

        if(target.getYCoord() <= currY){
            above = true;
        }else{
            above = false;
        }
    }

    private boolean atTargetNode(double x, double y){
        double targetX = x;
        double targetY = y;
        int buffer = 0;

        //Using position boolean variable (toRight and above) determine if bubble is close to the target
        if(toRight && above){
            if((currX >= targetX - buffer) && (currY <= targetY + buffer))
                return true;
        }else if(toRight && !above){
            if((currX >= targetX - buffer) && (currY >= targetY - buffer))
                return true;
        }else if(!toRight && above){
            if((currX <= targetX + buffer) && (currY <= targetY + buffer))
                return true;
        }else{
            if((currX <= targetX + buffer) && (currY >= targetY - buffer))
                return true;
        }
        return false;
    }

    @Override
    public double getX() {
        return currX;
    }

    @Override
    public double getY() {
        return currY;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        bubble.setFrame(x, y, w, h);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return bubble.getBounds2D();
    }


    /**
     * Update the bubble's data based on its coordinates.
     * Display data in bubble text pane in NetworkGUI.
     */
    private void updateData(){
        if(currentBounds == hostLayerBounds) {
            if (currentLayer == 1) {
                if (currentBounds[0].isInBounds(currX, currY, width)) {
                    data = "  A";
                    bubblePane.setText(data);
                    currentLayer = 4;
                }
            } else if (currentLayer == 4) {
                if (currentBounds[3].isInBounds(currX, currY, width)) { //transport layer
                    data = "  H1" + "  A";
                    bubblePane.setText(data);
                    currentLayer = 5;
                }
            } else if (currentLayer == 5) {
                if (currentBounds[4].isInBounds(currX, currY, width)) { //network layer
                    data = "  H2" + "  H1" + "  A";
                    bubblePane.setText(data);
                    currentLayer = 6;
                }
            }else if(currentLayer == 6){ //Data-link layer
                if (currentBounds[5].isInBounds(currX, currY, width)) {
                    bubblePane.setText(frame);
                    currentLayer = 7;
                }
            }else if(currentLayer == 7){
                if (currentBounds[6].isInBounds(currX, currY, width)) {
                    sinePanel.setVisible(true);
                    bubblePane.setVisible(false);
                    currentLayer = 8;
                }
            }else if(currentLayer == 8){
                if(!currentBounds[6].isInBounds(currX, currY, width)){
                    sinePanel.setIsSine(true);
                    sinePanel.repaint();
                    currentBounds = destLayerBounds;
                    currentLayer = 1;
                }
            }
        }

        else if(currentBounds == destLayerBounds){
            if (currentLayer == 1){
                if(currentBounds[0].isInBounds(currX, currY, width)) {
                    sinePanel.setIsSine(false);
                    sinePanel.repaint();
                    currentLayer = 2;
                }else {
                    for (int i = 0; i < routerBounds.length; i++) {
                        if (routerBounds[i].isInBounds(currX, currY, width)) {
                            bubblePane.setText(frame);
                            bubblePane.setVisible(true);
                            sinePanel.setVisible(false);
                            break;
                        } else {
                            bubblePane.setVisible(false);
                            sinePanel.setVisible(true);
                        }
                    }
                }
            } else if (currentLayer == 2) {
                if (currentBounds[1].isInBounds(currX, currY, width)) {
                    sinePanel.setVisible(false);
                    bubblePane.setVisible(true);
                    bubblePane.setText(frame);
                    currentLayer = 3;
                }
            } else if (currentLayer == 3) {
                if (currentBounds[2].isInBounds(currX, currY, width)) { //transport layer
                    data = "  H2" + "  H1" + "  A";
                    bubblePane.setText(data);
                    currentLayer = 4;
                }
            } else if (currentLayer == 4) {
                if (currentBounds[3].isInBounds(currX, currY, width)) { //network layer
                    data = "  H1" + "  A";
                    bubblePane.setText(data);
                    currentLayer = 5;
                }
            }else if(currentLayer == 5) {
                if (currentBounds[4].isInBounds(currX, currY, width)) {
                    data = "  A";
                    bubblePane.setText(data);
                }
            }
        }
    }

    /*
     * Set hostLayerBounds
     */
    public void setHostLayerBounds(Bounds[] host){
        hostLayerBounds = host;

        //First boundst to look for are host layer bounds
        currentBounds = hostLayerBounds;
    }

    /*
     * Set destLayerBounds
     */
    public void setDestLayerBounds(Bounds[] dest){
        destLayerBounds = dest;
    }

    /**
     * Set bounds of routers in panel.
     */
    public void setRouterBounds(Bounds[] router){
        routerBounds = router;
    }

    /**
     * Reset message bubble's position.
     */
    public void reset(){
        //bubble.setFrame(startX, startY, width, height);
        bubble = new Ellipse2D.Double(startX, startY, width, height);
        currX = startX;
        currY = startY;
        currentBounds = hostLayerBounds;
        currentLayer = 1;
        bubblePane.setVisible(true);
        bubblePane.setText("");
        sinePanel.setVisible(false);
        sinePanel.setIsSine(false);
    }

    /**
     * Reset message bubble's position, given starting coordinates.
     */
    public void reset(double x, double y){
        startX = x;
        startY = y;

        bubble = new Ellipse2D.Double(startX, startY, width, height);
        currX = startX;
        currY = startY;
        currentBounds = hostLayerBounds;
        currentLayer = 1;
        bubblePane.setVisible(true);
        bubblePane.setText("");
        sinePanel.setVisible(false);
        sinePanel.setIsSine(false);
    }

    /**
     * Set message to be transmitted in this message bubble.
     */
    public void setMessage(String msg){
        message = msg;
        currentPacket = message;
    }

    /**
     * Get data-link frame from current packet.
     * Add appropriate fields for a frame.
     */
    private String getDataLinkFrame(String packet){
        crc = new CRC32();

        String frame = "";
        String preliminary = "AAAAAAAAAAAAAA";
        String SFD = "AB";
        String dest = "4A301021101A";
        String src = "47201B2E08EE";
        String type = "DBA0";
        String data = "41000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

        //Get CRC
        byte[] bytes = hexStringToByteArray(dest + src + type + data);
        crc.update(bytes, 0, 60);
        long CRC = crc.getValue();
        Long CRC_LONG = new Long(0);
        String crc32 = CRC_LONG.toHexString(CRC);
        crc32 = crc32.toUpperCase();

        frame = preliminary + "\n" + SFD + "\n" + dest + "\n" + src + "\n" + type + "\n" + data + "\n" + crc32;

        return frame;
    }

    /**
     * Set type.  ACK message bubble will use this method to set type "ACK".
     */
    public void setBubbleType(String t){
        type = t;
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void setBubblePane(JTextPane pane){
        bubblePane = pane;
    }

    public void setSineWavePanel(SineWavePanel panel){
        sinePanel = panel;
    }

    public SineWavePanel getSinePanel() {
        return sinePanel;
    }

    public JTextPane getBubblePane() {
        return bubblePane;
    }

    public void setCurrentBounds(Bounds[] bounds){
        currentBounds = bounds;
    }
}
