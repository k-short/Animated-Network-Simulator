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
    boolean isPaused = false;

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
    private BufferedImage routerImage1;
    private BufferedImage routerImage2;
    private BufferedImage routerImage3;
    private BufferedImage routerImage4;
    private BufferedImage routerImage5;
    private BufferedImage routerImage6;
    private BufferedImage routerImage7;
    private BufferedImage routerImage8;



    //7-layer images
    private BufferedImage layersImage1;
    private BufferedImage layersImage2;

    //Timer image for ACK
    private BufferedImage timerImage;

    //Scaled images
    private Image scaledH1;
    private Image scaledH2;

    private Image scaledR1;
    private Image scaledR2;
    private Image scaledR3;
    private Image scaledR4;
    private Image scaledR5;
    private Image scaledR6;
    private Image scaledR7;
    private Image scaledR8;

    private Image scaledL1;
    private Image scaledL2;

    //Icon String labels
    private String host1Label = "H1";
    private String host2Label = "H2";
    private String host3Label = "H3";
    private String router1Label = "R1";
    private String router2Label = "R2";
    private String router3Label = "R3";
    private String router4Label = "R4";
    private String router5Label = "R5";
    private String router6Label = "R6";
    private String router7Label = "R7";
    private String router8Label = "R8";


    //Image and image label coordinates.
    private int host1X;
    private int host1Y;
    private int host2X;
    private int host2Y;

    private int router1X;
    private int router1Y;
    private int router2X;
    private int router2Y;
    private int router3X;
    private int router3Y;
    private int router4X;
    private int router4Y;
    private int router5X;
    private int router5Y;
    private int router6X;
    private int router6Y;
    private int router7X;
    private int router7Y;
    private int router8X;
    private int router8Y;

    private int layer1X;
    private int layer1Y;
    private int layer2X;
    private int layer2Y;

    private int host1LabelX;
    private int host1LabelY;
    private int host2LabelX;
    private int host2LabelY;

    private int router1LabelX;
    private int router1LabelY;
    private int router2LabelX;
    private int router2LabelY;
    private int router3LabelX;
    private int router3LabelY;
    private int router4LabelX;
    private int router4LabelY;
    private int router5LabelX;
    private int router5LabelY;
    private int router6LabelX;
    private int router6LabelY;
    private int router7LabelX;
    private int router7LabelY;
    private int router8LabelX;
    private int router8LabelY;

    private int timerX;
    private int timerY;

    //Starting image coordinates (coords for host 1)
    private final int STARTING_IMAGE_X_1 = 140;
    private final int STARTING_IMAGE_Y_1 = 60;

    //Starting image coordinates for layer 2
    private final int STARTING_IMAGE_X_2 = 900;
    private final int STARTING_IMAGE_Y_2 = 60;

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

    //Bounds for all routers
    Bounds[] routerBounds;

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
        bubbleRed.setRouterBounds(routerBounds);

        bubbleBlue.setHostLayerBounds(hostLayerBoundsBlue);
        bubbleBlue.setDestLayerBounds(destLayerBoundsBlue);
        bubbleBlue.setRouterBounds(routerBounds);

        //Load images
        loadImages();

        //Create graph
        graph = new Graph();

        //Create nodes for the graph and then add them to the graph.
        createNodes();
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

        g2.drawImage(scaledR1,router1X, router1Y, null);
        g2.drawImage(scaledR2,router2X, router2Y, null);
        g2.drawImage(scaledR3,router3X, router3Y, null);
        g2.drawImage(scaledR4,router4X, router4Y, null);
        g2.drawImage(scaledR5,router5X, router5Y, null);
        g2.drawImage(scaledR6,router6X, router6Y, null);
        g2.drawImage(scaledR7,router7X, router7Y, null);
        g2.drawImage(scaledR8,router8X, router8Y, null);

        //Add 7 layer images
        g2.drawImage(scaledL1, layer1X, layer1Y, null);
        g2.drawImage(scaledL2, layer2X, layer2Y, null);

        if(timerReady) {
            g2.drawImage(timerImage, timerX, timerY, null);
        }

        //Add String labels to desktop and router icons
        g2.setColor(Color.black);
        g2.drawString(host1Label, host1LabelX, host1LabelY);
        g2.drawString(host2Label, host2LabelX, host2LabelY);
        g2.drawString(router1Label, router1LabelX, router1LabelY);
        g2.drawString(router2Label, router2LabelX, router2LabelY);
        g2.drawString(router3Label, router3LabelX, router3LabelY);
        g2.drawString(router4Label, router4LabelX, router4LabelY);
        g2.drawString(router5Label, router5LabelX, router5LabelY);
        g2.drawString(router6Label, router6LabelX, router6LabelY);
        g2.drawString(router7Label, router7LabelX, router7LabelY);
        g2.drawString(router8Label, router8LabelX, router8LabelY);


        //Draw lines in between images
        // i.e. H1 - R1 - R2 - H2
        BasicStroke bold = new BasicStroke(2.0f);
        g2.setStroke(bold);

        g2.draw(new Line2D.Double(host1X+IMAGE_WIDTH, host1Y+(IMAGE_HEIGHT/2), router1X, router1Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router1X+(IMAGE_WIDTH/2), router1Y, router2X + (IMAGE_WIDTH/2), router2Y+IMAGE_HEIGHT));
        g2.draw(new Line2D.Double(router2X + IMAGE_WIDTH, router2Y + (IMAGE_HEIGHT/2), router3X, router3Y + (IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router1X + (IMAGE_WIDTH/2), router1Y + IMAGE_HEIGHT, router7X + (IMAGE_WIDTH/2), router7Y));
        g2.draw(new Line2D.Double(router7X + IMAGE_WIDTH, router7Y + (IMAGE_HEIGHT/2), router8X, router8Y + (IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router4X + (IMAGE_WIDTH/2), router1Y, router3X + (IMAGE_WIDTH/2), router3Y + IMAGE_HEIGHT));
        g2.draw(new Line2D.Double(router4X + (IMAGE_WIDTH/2), router1Y + IMAGE_HEIGHT, router8X + (IMAGE_WIDTH/2), router8Y));
        g2.draw(new Line2D.Double(router5X + IMAGE_WIDTH, router5Y + (IMAGE_HEIGHT/2), router6X, router6Y + (IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router2X + IMAGE_WIDTH, router2Y + IMAGE_HEIGHT, router5X + (IMAGE_WIDTH/2), router5Y));
        g2.draw(new Line2D.Double(router5X + (IMAGE_WIDTH/2), router5Y + IMAGE_HEIGHT, router7X + IMAGE_WIDTH, router7Y));
        g2.draw(new Line2D.Double(router3X, router3Y + IMAGE_HEIGHT, router6X + IMAGE_WIDTH, router6Y));
        g2.draw(new Line2D.Double(router6X + (IMAGE_WIDTH/2), router6Y + IMAGE_HEIGHT, router8X, router8Y+ (IMAGE_HEIGHT/4)));
        g2.draw(new Line2D.Double(router4X + IMAGE_WIDTH, router4Y + (IMAGE_HEIGHT/2), host2X, host2Y + (IMAGE_HEIGHT/2)));

        g2.draw(new Line2D.Double(layer1X + (LAYER_WIDTH/2), layer1Y + LAYER_HEIGHT, layer1X + (LAYER_WIDTH/2), host1Y));
        g2.draw(new Line2D.Double(layer2X + (LAYER_WIDTH/2), layer2Y + LAYER_HEIGHT, layer2X + (LAYER_WIDTH/2), host2Y));
    }

    /**
     * Sets image coordinates for assignment 2
     * All image coordinates are just going to be hard-coded unlike in first assignment
     */
    private void setImageCoordinates(){
        int layerCenter = LAYER_WIDTH/2 - IMAGE_WIDTH/2;
        int layerHostGap = 30;
        int routerXDiff = 150;
        int routerXDiffAngled = 60;
        int routerYDiff = 130;
        int hostYDiff = 50;

        //Set 7-layer coordinates
        layer1X = STARTING_IMAGE_X_1;
        layer1Y = STARTING_IMAGE_Y_1;

        layer2X = STARTING_IMAGE_X_2;
        layer2Y = STARTING_IMAGE_Y_2;

        //Set host machine coordinates
        //Each is centered below each OSI layer
        host1X = layer1X + layerCenter;
        host1Y = layer1Y + LAYER_HEIGHT + layerHostGap;

        host2X = layer2X + layerCenter;
        host2Y = layer2Y + LAYER_HEIGHT + layerHostGap;

        //Set router machine coordinates
        router1X = host1X + routerXDiff;
        router1Y = host1Y - hostYDiff;

        router5X = router1X + routerXDiff;
        router5Y = router1Y;

        router6X = router5X + routerXDiff;
        router6Y = router1Y;

        router4X = host2X - routerXDiff;
        router4Y = host2Y - hostYDiff;

        router2X = router1X + routerXDiffAngled;
        router2Y = router1Y - routerYDiff;

        router3X = router4X - routerXDiffAngled;
        router3Y = router4Y - routerYDiff;

        router7X = router1X + routerXDiffAngled;
        router7Y = router1Y + routerYDiff;

        router8X = router4X - routerXDiffAngled;
        router8Y = router4Y + routerYDiff;

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

        router1LabelX = router1X + IMAGE_WIDTH;
        router1LabelY = router1Y + IMAGE_HEIGHT/2;

        router2LabelX = router2X + IMAGE_WIDTH;
        router2LabelY = router2Y;

        router3LabelX = router3X + IMAGE_WIDTH;
        router3LabelY = router3Y;

        router4LabelX = router4X + IMAGE_WIDTH;
        router4LabelY = router4Y;

        router5LabelX = router5X + IMAGE_WIDTH;
        router5LabelY = router5Y;

        router6LabelX = router6X + IMAGE_WIDTH;
        router6LabelY = router6Y + IMAGE_HEIGHT;

        router7LabelX = router7X - 15;
        router7LabelY = router7Y + IMAGE_HEIGHT;

        router8LabelX = router8X + IMAGE_WIDTH;
        router8LabelY = router8Y + IMAGE_HEIGHT;
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

        //Dest layers for red and blue
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

        //Create bounds for each router
        Bounds r1 = new Bounds(router1X, router1X + IMAGE_WIDTH, router1Y, router1Y + IMAGE_HEIGHT, ROUTER);
        Bounds r2 = new Bounds(router2X, router2X + IMAGE_WIDTH, router2Y, router2Y + IMAGE_HEIGHT, ROUTER);
        Bounds r3 = new Bounds(router3X, router3X + IMAGE_WIDTH, router3Y, router3Y + IMAGE_HEIGHT, ROUTER);
        Bounds r4 = new Bounds(router4X, router4X + IMAGE_WIDTH, router4Y, router4Y + IMAGE_HEIGHT, ROUTER);
        Bounds r5 = new Bounds(router5X, router5X + IMAGE_WIDTH, router5Y, router5Y + IMAGE_HEIGHT, ROUTER);
        Bounds r6 = new Bounds(router6X, router6X + IMAGE_WIDTH, router6Y, router6Y + IMAGE_HEIGHT, ROUTER);
        Bounds r7 = new Bounds(router7X, router7X + IMAGE_WIDTH, router7Y, router7Y + IMAGE_HEIGHT, ROUTER);
        Bounds r8 = new Bounds(router8X, router8X + IMAGE_WIDTH, router8Y, router8Y + IMAGE_HEIGHT, ROUTER);

        //Add bounds in order of being visited.
        hostLayerBoundsRed = new Bounds[]{app1, pres1, sess1, trans1, net1, dat1, phy1};
        destLayerBoundsRed = new Bounds[]{phy2, dat2, net2, trans2, sess2, pres2, app2};

        //Add second destination bounds for blue bubble.
        hostLayerBoundsBlue = hostLayerBoundsRed;
        destLayerBoundsBlue = destLayerBoundsRed;

        //Add router bounds to bounds array
        routerBounds = new Bounds[]{r1, r2, r3, r4, r5, r6, r7, r8};
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
                if(isRunning && (redMoving || blueMoving) && !isPaused) {
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
                        bubbleRed.setRouterBounds(routerBounds);
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
                }else if(!isPaused){
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

        //Nodes, Layer1 not including since it is the starting point for both bubbles
        Node nodeH1 = new Node(host1X, imageY);
        Node nodeH2 = new Node(host2X, imageY);

        Node nodeR1 = new Node(router1X, imageY);
        Node nodeR2 = new Node(router2X, imageY);
        Node nodeR3 = new Node(router3X, imageY);
        Node nodeR4 = new Node(router4X, imageY);
        Node nodeR5 = new Node(router5X, imageY);
        Node nodeR6 = new Node(router6X, imageY);
        Node nodeR7 = new Node(router7X, imageY);
        Node nodeR8 = new Node(router8X, imageY);

        Node nodeL2 = new Node(host2X + IMAGE_WIDTH/2 - BUBBLE_SIZE/2, layer2Y);

        Node nodeR1ACK = new Node(router1X, imageYACK);

        graph.addNode(nodeH1);
        graph.addNode(nodeH2);
        graph.addNode(nodeR1);
        graph.addNode(nodeR2);
        graph.addNode(nodeR3);
        graph.addNode(nodeR4);
        graph.addNode(nodeR5);
        graph.addNode(nodeR6);
        graph.addNode(nodeR7);
        graph.addNode(nodeR8);
        graph.addNode(nodeL2);

        //Secondary node for R1 for ACK -- size is different
        graph.addNode(nodeR1ACK);

        Edge edge1 = new Edge(nodeR1, nodeR2);
        Edge edge2 = new Edge(nodeR1, nodeR7);
        Edge edge3 = new Edge(nodeR2, nodeR5);
        Edge edge4 = new Edge(nodeR5, nodeR7);
        Edge edge5 = new Edge(nodeR2, nodeR3);
        Edge edge6 = new Edge(nodeR5, nodeR6);
        Edge edge7 = new Edge(nodeR7, nodeR8);
        Edge edge8 = new Edge(nodeR3, nodeR6);
        Edge edge9 = new Edge(nodeR6, nodeR8);
        Edge edge10 = new Edge(nodeR3, nodeR4);
        Edge edge11 = new Edge(nodeR4, nodeR8);

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
        isPaused = false;

        counter = 0;

        bubbleRed.reset(bubbleX, bubbleY);
        bubbleBlue.reset();
        bubbleACK.resetACK(bubbleACKX, bubbleACKY);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Load all images to be used.
     */
    private void loadImages(){
        //Load host and router images, layer images
        try {
            layersImage1 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_1.png"));
            layersImage2 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_2.png"));

            hostImage1 = ImageIO.read(getClass().getResource("/resources/images/desktop_1_icon.png"));
            hostImage2 = ImageIO.read(getClass().getResource("/resources/images/desktop_2_icon.png"));

            routerImage1 = ImageIO.read(getClass().getResource("/resources/images/router_1_icon.png"));
            routerImage2 = ImageIO.read(getClass().getResource("/resources/images/router_2_icon.png"));
            routerImage3 = ImageIO.read(getClass().getResource("/resources/images/router_3_icon.png"));
            routerImage4 = ImageIO.read(getClass().getResource("/resources/images/router_4_icon.png"));
            routerImage5 = ImageIO.read(getClass().getResource("/resources/images/router_5_icon.png"));
            routerImage6 = ImageIO.read(getClass().getResource("/resources/images/router_6_icon.png"));
            routerImage7 = ImageIO.read(getClass().getResource("/resources/images/router_7_icon.png"));
            routerImage8 = ImageIO.read(getClass().getResource("/resources/images/router_8_icon.png"));

            scaledL1 = layersImage1.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);
            scaledL2 = layersImage2.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);

            scaledH1 = hostImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledH2 = hostImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

            scaledR1 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR2 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR3 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR4 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR5 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR6 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR7 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR8 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

            timerImage = ImageIO.read(getClass().getResource("/resources/images/timer_icon.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
