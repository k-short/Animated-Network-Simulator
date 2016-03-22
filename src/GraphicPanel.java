import sun.plugin2.message.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ken12_000 on 1/26/2016.
 */
public class GraphicPanel extends JPanel{

    Graphics2D g2;

    private int speed =40;

    boolean timerReady = false;
    boolean resend = false;
    boolean makeCopy = false;
    boolean discarded = false;
    boolean ackDone = false;

    int counter = 0;

    //Types of nodes
    private final String ROUTER = "ROUTER";
    private final String HOST = "HOST";
    private final String LAYER = "LAYER";

    //Message bubble
    private MessageBubble bubbleRed;
    private MessageBubble bubbleBlue;
    private MessageBubble bubbleRedCopy;

    //Message bubble starting coordinates
    private double bubbleX;
    private double bubbleY;

    //Message bubble to act as ACK and its starting coordinates at R2
    private MessageBubble bubbleACK;
    private double bubbleACKX;
    private double bubbleACKY;

    //Message bubble size
    private final int BUBBLE_SIZE = 40;
    private final int BUBBLE_ACK_SIZE = 15;

    //Graph to hold nodes and edges
    private Graph graph;

    //Desktop and router images
    private BufferedImage hostImage1;
    private BufferedImage hostImage2;
    private BufferedImage hostImage3;
    private BufferedImage routerImage1;
    private BufferedImage routerImage2;
    private BufferedImage routerImage3;

    //7-layer images
    private BufferedImage layersImage1;
    private BufferedImage layersImage2;
    private BufferedImage layersImage3;

    //Timer image for ACK
    private BufferedImage timerImage;

    //Scaled images
    private Image scaledH1;
    private Image scaledH2;
    private Image scaledH3;
    private Image scaledR1;
    private Image scaledR2;
    private Image scaledR3;
    private Image scaledL1;
    private Image scaledL2;
    private Image scaledL3;
    private Image scaledTimer;

    //Icon String labels
    private String host1Label = "H1";
    private String host2Label = "H2";
    private String host3Label = "H3";
    private String router1Label = "R1";
    private String router2Label = "R2";
    private String router3Label = "R3";

    //Image and image label coordinates.
    private int host1X;
    private int host1Y;
    private int host2X;
    private int host2Y;
    private int host3X;
    private int host3Y;

    private int router1X;
    private int router1Y;
    private int router2X;
    private int router2Y;
    private int router3X;
    private int router3Y;

    private int layer1X;
    private int layer1Y;
    private int layer2X;
    private int layer2Y;
    private int layer3X;
    private int layer3Y;

    private int host1LabelX;
    private int host1LabelY;
    private int host2LabelX;
    private int host2LabelY;
    private int host3LabelX;
    private int host3LabelY;

    private int router1LabelX;
    private int router1LabelY;
    private int router2LabelX;
    private int router2LabelY;
    private int router3LabelX;
    private int router3LabelY;

    private int timerX;
    private int timerY;

    //Starting image coordinates (coords for host 1)
    private final int STARTING_IMAGE_X_1 = 200;
    private final int STARTING_IMAGE_Y_1 = 140;

    //Starting image coordinates for layer 2
    private final int STARTING_IMAGE_X_2 = 800;
    private final int STARTING_IMAGE_Y_2 = 140;

    //Starting image coordinates for layer 3 (layer in MIDDLE)
    private final int STARTING_IMAGE_X_3 = 550;
    private final int STARTING_IMAGE_Y_3 = 30;

    //Image sizes
    private final int IMAGE_WIDTH = 50;
    private final int IMAGE_HEIGHT = 50;
    private final int LAYER_WIDTH = 130;
    private final int LAYER_HEIGHT = 250;

    //Strings for layers host
    private final String PHY_HOST = "PHYSICAL_HOST";
    private final String DL_HOST = "DATALINK_HOST";
    private final String NET_HOST = "NETWORK_HOST";
    private final String TRANS_HOST = "TRANSPORT_HOST";
    private final String SESS_HOST = "SESSION_HOST";
    private final String PRES_HOST = "PRESENTATION_HOST";
    private final String APP_HOST = "APPLICATION_HOST";

    //Strings for layers destination
    private final String PHY_DEST = "PHYSICAL_DEST";
    private final String DL_DEST = "DATALINK_DEST";
    private final String NET_DEST = "NETWORK_DEST";
    private final String TRANS_DEST = "TRANSPORT_DEST";
    private final String SESS_DEST = "SESSION_DEST";
    private final String PRES_DEST = "PRESENTATION_DESTT";
    private final String APP_DEST = "APPLICATION_DEST";

    //Bounds for host and dest layers
    Bounds[] hostLayerBoundsRed;
    Bounds[] destLayerBoundsRed;

    Bounds[] hostLayerBoundsBlue;
    Bounds[] destLayerBoundsBlue;

    //Keep track of if animation is running.  False by default.
    private boolean isRunning = false;

    /**
     * Constructor.  Needs bubbles passed in because NetworkGUI has to create bubbles.
     * This is because NetworkGUI needs access to bubble data in order to dispaly in bubble data panel.
     */
    public GraphicPanel(MessageBubble bRed, MessageBubble bBlue){
        //Set image coordinates
        setImageCoordinates();

        //Set image label coordinates
        setImageLabelCoordinates();

        //Set image bounds
        setImageBounds();

        //Create red message bubble
        bubbleRed = bRed;
        bubbleRed.setAttributes(bubbleX, bubbleY, BUBBLE_SIZE, BUBBLE_SIZE, Color.red);

        //Create blue message bubble
        bubbleBlue = bBlue;
        bubbleBlue.setAttributes(bubbleX, bubbleY - BUBBLE_SIZE - 10, BUBBLE_SIZE, BUBBLE_SIZE, Color.blue);

        //Create ACK bubble
        bubbleACK = new MessageBubble(bubbleACKX, bubbleACKY, BUBBLE_ACK_SIZE, BUBBLE_ACK_SIZE, Color.green);
        bubbleACK.setBubbleType("ACK");

        //Create copy of red bubble for R1
        bubbleRedCopy = new MessageBubble(router1X, router1Y, BUBBLE_SIZE, BUBBLE_SIZE, Color.red);

        //Assign bounds to bubble
        bubbleRed.setHostLayerBounds(hostLayerBoundsRed);
        bubbleRed.setDestLayerBounds(destLayerBoundsRed);

        bubbleBlue.setHostLayerBounds(hostLayerBoundsBlue);
        bubbleBlue.setDestLayerBounds(destLayerBoundsBlue);

        //Load host and router images, layer images
        try {
            hostImage1 = ImageIO.read(getClass().getResource("/resources/images/desktop_1_icon.png"));
            hostImage2 = ImageIO.read(getClass().getResource("/resources/images/desktop_2_icon.png"));
            hostImage3 = ImageIO.read(getClass().getResource("/resources/images/desktop_3_icon.png"));

            routerImage1 = ImageIO.read(getClass().getResource("/resources/images/router_1_icon.png"));
            routerImage2 = ImageIO.read(getClass().getResource("/resources/images/router_2_icon.png"));
            routerImage3 = ImageIO.read(getClass().getResource("/resources/images/router_3_icon.png"));

            scaledH1 = hostImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledH2 = hostImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledH3 = hostImage3.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

            scaledR1 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR2 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR3 = routerImage3.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

            layersImage1 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_1.png"));
            layersImage2 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_2.png"));
            layersImage3 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_3.png"));

            scaledL1 = layersImage1.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);
            scaledL2 = layersImage2.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);
            scaledL3 = layersImage3.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);

            timerImage = ImageIO.read(getClass().getResource("/resources/images/timer_icon.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create graph
        graph = new Graph();

        //Create nodes for the graph and then add them to the graph.
        createNodes();

        //Create edges !! NOT NEEDED FOR CURRENT ASSIGNMENT
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Add message bubble
        if(makeCopy){
            bubbleRedCopy.draw(g2);
        }
        if(!discarded) {
            bubbleRed.draw(g2);
        }

        bubbleBlue.draw(g2);

        if(!ackDone) {
            bubbleACK.draw(g2);
        }

        //Add desktop and router icons
        g2.drawImage(scaledH1,host1X, host1Y, null);
        g2.drawImage(scaledH2,host2X, host2Y, null);
        g2.drawImage(scaledH3,host3X, host3Y, null);

        g2.drawImage(scaledR1,router1X, router1Y, null);
        g2.drawImage(scaledR2,router2X, router2Y, null);
        g2.drawImage(scaledR3,router3X, router3Y, null);

        //Add 7 layer images
        g2.drawImage(scaledL1, layer1X, layer1Y, null);
        g2.drawImage(scaledL2, layer2X, layer2Y, null);
        g2.drawImage(scaledL3, layer3X, layer3Y, null);

        if(timerReady) {
            g2.drawImage(timerImage, timerX, timerY, null);
        }

        //Add String labels to desktop and router icons
        g2.setColor(Color.black);
        g2.drawString(host1Label, host1LabelX, host1LabelY);
        g2.drawString(host2Label, host2LabelX, host2LabelY);
        g2.drawString(host3Label, host3LabelX, host3LabelY);
        g2.drawString(router1Label, router1LabelX, router1LabelY);
        g2.drawString(router2Label, router2LabelX, router2LabelY);
        g2.drawString(router3Label, router3LabelX, router3LabelY);

        //Draw lines in between images
        // i.e. H1 - R1 - R2 - H2
        BasicStroke bold = new BasicStroke(2.0f);
        g2.setStroke(bold);

        g2.draw(new Line2D.Double(host1X+IMAGE_WIDTH, host1Y+(IMAGE_HEIGHT/2), router1X, router1Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router1X+IMAGE_WIDTH, router1Y+(IMAGE_HEIGHT/2), router2X, router2Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router2X+IMAGE_WIDTH, router2Y+(IMAGE_HEIGHT/2), host2X, host2Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(layer1X + (LAYER_WIDTH/2), layer1Y + LAYER_HEIGHT, layer1X + (LAYER_WIDTH/2), host1Y));
        g2.draw(new Line2D.Double(layer2X + (LAYER_WIDTH/2), layer2Y + LAYER_HEIGHT, layer2X + (LAYER_WIDTH/2), host2Y));
        g2.draw(new Line2D.Double(router1X + (IMAGE_WIDTH/2), router1Y + (IMAGE_HEIGHT/2), router3X + (IMAGE_WIDTH/2), router3Y+IMAGE_HEIGHT));
        g2.draw(new Line2D.Double(router3X + IMAGE_WIDTH, router3Y + (IMAGE_HEIGHT/2), host3X, host3Y + (IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(host3X + (IMAGE_WIDTH/2), host3Y, layer3X + (LAYER_WIDTH/2), layer3Y + LAYER_HEIGHT));
    }

    /**
     * Sets image coordinates for assignment 2
     * This includes a 3rd 7-layers and a 3 host and router
     * All image coordinates are just going to be hard-coded unlike in first assignment
     */
    private void setImageCoordinates(){
        int layerCenter = LAYER_WIDTH/2 - IMAGE_WIDTH/2;
        int layerHostGap = 30;
        int routerXDiff = 150;

        //Set 7-layer coordinates
        layer1X = STARTING_IMAGE_X_1;
        layer1Y = STARTING_IMAGE_Y_1;

        layer2X = STARTING_IMAGE_X_2;
        layer2Y = STARTING_IMAGE_Y_2;

        layer3X = STARTING_IMAGE_X_3;
        layer3Y = STARTING_IMAGE_Y_3;

        //Set host machine coordinates
        //Each is centered below each OSI layer
        host1X = layer1X + layerCenter;
        host1Y = layer1Y + LAYER_HEIGHT + layerHostGap;

        host2X = layer2X + layerCenter;
        host2Y = layer2Y + LAYER_HEIGHT + layerHostGap;

        host3X = layer3X + layerCenter;
        host3Y = layer3Y + LAYER_HEIGHT + layerHostGap;

        //Set router machine coordinates
        router1X = host1X + routerXDiff;
        router1Y = host1Y;

        router2X = host2X - routerXDiff;
        router2Y = host2Y;

        router3X = host3X - routerXDiff - 40 ;
        router3Y = host3Y;

        //Set bubble starting coordinates
        bubbleX = layer1X + (LAYER_WIDTH / 2) - (BUBBLE_SIZE/2);
        bubbleY = layer1Y;

        //Set ACK bubble coordinates starting at R2
        bubbleACKX = router2X;
        bubbleACKY = router2Y + IMAGE_HEIGHT/2;

        //Set timer coordinates below R1
        timerX = router1X + IMAGE_WIDTH;
        timerY = router2Y + IMAGE_HEIGHT;
    }

    /**
     * Set the coordiantes for images labels.
     * Each label coordinate position will be based off of corresponding image location.
     */
    private void setImageLabelCoordinates(){
        //Set labels in top right corner of images
        host1LabelX = host1X + IMAGE_WIDTH;
        host1LabelY = host1Y;

        host2LabelX = host2X + IMAGE_WIDTH;
        host2LabelY = host2Y;

        host3LabelX = host3X + IMAGE_WIDTH;
        host3LabelY = host3Y;

        router1LabelX = router1X + IMAGE_WIDTH;
        router1LabelY = router1Y;

        router2LabelX = router2X + IMAGE_WIDTH;
        router2LabelY = router2Y;

        router3LabelX = router3X + IMAGE_WIDTH;
        router3LabelY = router3Y;
    }

    /**
     * Set the bounds for each image.
     * Set the bounds for each layer in 7 layer image.
     *
     * Fill an array with the bounds
     *
     * For assignment 1 just need bounds for two layers.
     */
    private void setImageBounds(){
        //Current positions within the layer
        //Starts off at layer starting coordinate
        double currentY1 = layer1Y;
        double currentY2 = layer2Y;
        double currentY3 = layer3Y;

        double layerSize = LAYER_HEIGHT / 7;

        //Host layers for Red and Blue bubbles
        Bounds app1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, APP_HOST);
        currentY1 += layerSize;

        Bounds pres1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, PRES_HOST);
        currentY1 += layerSize;

        Bounds sess1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, SESS_HOST);
        currentY1 += layerSize;

        Bounds trans1 = new Bounds(layer1X,layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, TRANS_HOST);
        currentY1 += layerSize;

        Bounds net1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, NET_HOST);
        currentY1 += layerSize;

        Bounds dat1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, DL_HOST);
        currentY1 += layerSize;

        Bounds phy1 = new Bounds(layer1X, layer1X + LAYER_WIDTH,currentY1, currentY1 + layerSize, PHY_HOST);

        //Dest layers for red
        Bounds app2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, APP_DEST);
        currentY2 += layerSize;

        Bounds pres2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, PRES_DEST);
        currentY2 += layerSize;

        Bounds sess2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, SESS_DEST);
        currentY2 += layerSize;

        Bounds trans2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, TRANS_DEST);
        currentY2 += layerSize;

        Bounds net2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, NET_DEST);
        currentY2 += layerSize;

        Bounds dat2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, DL_DEST);
        currentY2 += layerSize;

        Bounds phy2 = new Bounds(layer2X, layer2X + LAYER_WIDTH,currentY2, currentY2 + layerSize, PHY_DEST);

        //Dest layers for blue
        Bounds app3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, APP_DEST);
        currentY3 += layerSize;

        Bounds pres3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, PRES_DEST);
        currentY3 += layerSize;

        Bounds sess3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, SESS_DEST);
        currentY3 += layerSize;

        Bounds trans3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, TRANS_DEST);
        currentY3 += layerSize;

        Bounds net3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, NET_DEST);
        currentY3 += layerSize;

        Bounds dat3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, DL_DEST);
        currentY3 += layerSize;

        Bounds phy3 = new Bounds(layer3X, layer3X + LAYER_WIDTH,currentY3, currentY3 + layerSize, PHY_DEST);

        //Add bounds in order of being visited.
        hostLayerBoundsRed = new Bounds[]{app1, pres1, sess1, trans1, net1, dat1, phy1};
        destLayerBoundsRed = new Bounds[]{phy2, dat2, net2, trans2, sess2, pres2, app2};

        //Add second destination bounds for blue bubble.
        hostLayerBoundsBlue = hostLayerBoundsRed;
        destLayerBoundsBlue = new Bounds[]{phy3, dat3, net3, trans3, sess3, pres3, app3};
    }

    /**
     * Move the ball.
     */
   // @Override
    public void run() {
        //Get pre-determined paths for the red and blue message bubbles
        ArrayList<Node> pathRed = graph.getPathRed();
        ArrayList<Node> pathBlue = graph.getPathBlue();
        ArrayList<Node> pathACK = graph.getPathACK();

        //Set the first target nodes for the message bubbles
        bubbleRed.setTarget(pathRed.get(0));
        bubbleBlue.setTarget(pathBlue.get(0));
        bubbleACK.setTarget(pathACK.get(0));

        //Create a timer for animation
        Timer timer = new Timer(speed, null);

        //Animation is currently running
        isRunning = true;

        ActionListener listener = new ActionListener() {
            //Current place in path
            int redPos = 0;
            int bluePos = 0;

            //Booleans to keep track of if red and blue bubbles are done moving
            boolean redMoving = true;
            boolean blueMoving = true;
            boolean ackMoving = false;
            //To know when ack has finished moving
            boolean waitRed = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                //If red or bubble still moving then don't stop timer
                if(isRunning & (redMoving || blueMoving)) {
                    //Move the red bubble
                    if(redMoving && !waitRed) { //Not at next target yet
                        if (bubbleRed.move()) {
                            repaint();
                        }
                        else { //At target, update next target in path
                            if (redPos < pathRed.size() - 1 && !resend) {
                                redPos++;

                                //At R1, make a copy to leave there
                                if(redPos == 2 && !ackDone){
                                    makeCopy = true;
                                    timerReady = true;
                                    repaint();
                                }

                                //At R2 need to wait
                                if(redPos == 3 && !ackDone){
                                    //redMoving = false;
                                    waitRed = true;
                                    //repaint();
                                }

                                bubbleRed.setTarget(pathRed.get(redPos));
                            }
                            else if(redPos < pathRed.size() - 1){
                                redPos++;

                                //At R2, now ACK needs to be sent
                                if(redPos == 3 && !ackDone){
                                    ackMoving = true;
                                    waitRed = false;
                                }

                                bubbleRed.setTarget(pathRed.get(redPos));
                            }
                            else {
                                redMoving = false;
                            }
                        }
                    }

                    //Move the ack bubble if second time red bubble has been sent
                    if(ackMoving && resend){
                        if(bubbleACK.move()){
                            repaint();
                        }
                        else{ //ACK finished, let red continue moving
                            ackMoving = false;
                            ackDone = true;
                            redMoving = true;
                            timerReady = false;
                            repaint();
                        }
                    }else if(counter >=100 && !resend){
                        discarded = true;
                        JTextPane pane = bubbleRed.getBubblePane();
                        SineWavePanel panel = bubbleRed.getSinePanel();
                        bubbleRed = bubbleRedCopy;
                        bubbleRed.setHostLayerBounds(hostLayerBoundsRed);
                        bubbleRed.setDestLayerBounds(destLayerBoundsRed);
                        bubbleRed.setCurrentBounds(destLayerBoundsRed);
                        bubbleRed.setBubblePane(pane);
                        bubbleRed.setSineWavePanel(panel);
                        redPos = 1;
                        bubbleRedCopy.setTarget(pathRed.get(2));
                        waitRed = false;
                        counter = 0;
                        resend = true;
                    }
                    else if(waitRed && counter < 100){
                        counter ++;
                    }

                    //Move the blue bubble
                    if (bubbleBlue.move()) {
                        repaint();
                    } else {
                        if (bluePos < pathBlue.size() - 1) {
                            bluePos++;
                            bubbleBlue.setTarget(pathBlue.get(bluePos));
                        } else {
                            blueMoving = false;
                        }
                    }
                }else{
                    timer.stop();
                }
            }
        };
            timer.addActionListener(listener);
            timer.start();
    }

    /**
     * Create nodes for all image objects.
     * Add the nodes to the graph
     */
    private void createNodes(){
        //Y-coordinate that centers bubble on image for H1, H2, R1 and R2 (all on same horizontal line)
        double imageY = host1Y + IMAGE_HEIGHT/2 - BUBBLE_SIZE/2;
        double imageYACK = host1Y + IMAGE_HEIGHT/2 - BUBBLE_ACK_SIZE/2;

        //Y coordinate for H3 and R3
        double imageY3 = host3Y + IMAGE_HEIGHT/2 - BUBBLE_SIZE/2;

        //Nodes, Layer1 not including since it is the starting point for both bubbles
        Node nodeH1 = new Node(host1X, imageY, HOST);
        Node nodeH2 = new Node(host2X, imageY, HOST);
        Node nodeH3 = new Node(host3X, imageY3, HOST);

        Node nodeR1 = new Node(router1X, imageY, ROUTER);
        Node nodeR2 = new Node(router2X, imageY, ROUTER);
        Node nodeR3 = new Node(router3X, imageY3, ROUTER);

        Node nodeL2 = new Node(host2X + IMAGE_WIDTH/2 - BUBBLE_SIZE/2, layer2Y, LAYER);
        Node nodeL3 = new Node(host3X + IMAGE_WIDTH/2 - BUBBLE_SIZE/2, layer3Y, LAYER);

        Node nodeR1ACK = new Node(router1X, imageYACK, ROUTER);

        graph.addNode(nodeH1);
        graph.addNode(nodeH2);
        graph.addNode(nodeH3);
        graph.addNode(nodeR1);
        graph.addNode(nodeR2);
        graph.addNode(nodeR3);
        graph.addNode(nodeL2);
        graph.addNode(nodeL3);

        //Secondary node for R1 for ACK -- size is different
        graph.addNode(nodeR1ACK);
    }

    /*
     * Stop animation.
     * For reset button in NetworkGUI
     */
    public void stop(){
        isRunning = false;
        timerReady = false;
        resend = false;
        makeCopy = false;
        discarded = false;
        ackDone = false;

        counter = 0;

        bubbleRed.reset(bubbleX, bubbleY);
        bubbleBlue.reset();
    }
}
