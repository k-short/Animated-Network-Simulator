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

    //Types of nodes
    private final String ROUTER = "ROUTER";
    private final String HOST = "HOST";
    private final String LAYER = "LAYER";

    //Message bubble
    private MessageBubble bubble;

    //Message bubble starting coordinates
    private double bubbleX;
    private double bubbleY;

    //Message bubble size
    private final int BUBBLE_SIZE = 40;

    //Graph to hold nodes and edges
    private Graph graph;

    //Desktop and router images
    private BufferedImage hostImage1;
    private BufferedImage hostImage2;
    private BufferedImage routerImage1;
    private BufferedImage routerImage2;

    //7-layer images
    private BufferedImage layersImage1;
    private BufferedImage layersImage2;

    //Scaled images
    private Image scaledH1;
    private Image scaledH2;
    private Image scaledR1;
    private Image scaledR2;
    private Image scaledL1;
    private Image scaledL2;

    //Icon String labels
    private String host1Label = "H1";
    private String host2Label = "H2";
    private String router1Label = "R1";
    private String router2Label = "R2";

    //Image and image label coordinates.
    private int host1X;
    private int host1Y;
    private int host2X;
    private int host2Y;
    private int router1X;
    private int router1Y;
    private int router2X;
    private int router2Y;
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

    //Starting image coordinates (coords for host 1)
    private final int STARTING_IMAGE_X = 200;
    private final int STARTING_IMAGE_Y = 350;

    //Image sizes
    private final int IMAGE_WIDTH = 60;
    private final int IMAGE_HEIGHT = 60;
    private final int LAYER_WIDTH = 155;
    private final int LAYER_HEIGHT = 300;

    //JTextPanel to show bubble info
    private JTextPane bubbleDataPane;

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
    Bounds[] hostLayerBounds;
    Bounds[] destLayerBounds;

    private boolean isRunning = false;

    public GraphicPanel(MessageBubble b){
        //Set image coordinates
        setImageCoordinates(STARTING_IMAGE_X, STARTING_IMAGE_Y);

        //Set image bounds
        setImageBounds();

        //Create red message bubble
        //bubble = new MessageBubble(bubbleX, bubbleY, BUBBLE_SIZE, BUBBLE_SIZE, Color.red);
        bubble = b;
        bubble.setAttributes(bubbleX, bubbleY, BUBBLE_SIZE, BUBBLE_SIZE, Color.red);

        //Assign bounds to bubble
        bubble.setHostLayerBounds(hostLayerBounds);
        bubble.setDestLayerBounds(destLayerBounds);

        //Load host and router images, layer images
        try {
            hostImage1 = ImageIO.read(getClass().getResource("/resources/images/desktop_1_icon.png"));
            hostImage2 = ImageIO.read(getClass().getResource("/resources/images/desktop_2_icon.png"));
            routerImage1 = ImageIO.read(getClass().getResource("/resources/images/router_1_icon.png"));
            routerImage2 = ImageIO.read(getClass().getResource("/resources/images/router_2_icon.png"));

            scaledH1 = hostImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledH2 = hostImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR1 = routerImage1.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            scaledR2 = routerImage2.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);

            layersImage1 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_1.png"));
            layersImage2 = ImageIO.read(getClass().getResource("/resources/images/7_layer_network_2.png"));

            scaledL1 = layersImage1.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);
            scaledL2 = layersImage2.getScaledInstance(LAYER_WIDTH, LAYER_HEIGHT, Image.SCALE_SMOOTH);


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
        bubble.draw(g2);

        //Add desktop and router icons
        g2.drawImage(scaledH1,host1X, host1Y, null);
        g2.drawImage(scaledH2,host2X, host2Y, null);
        g2.drawImage(scaledR1,router1X, router1Y, null);
        g2.drawImage(scaledR2,router2X, router2Y, null);

        //Add 7 layer images
        g2.drawImage(scaledL1, layer1X, layer1Y, null);
        g2.drawImage(scaledL2, layer2X, layer2Y, null);

        //Add String labels to desktop and router icons
        g2.setColor(Color.black);
        g2.drawString(host1Label, host1LabelX, host1LabelY);
        g2.drawString(host2Label, host2LabelX, host2LabelY);
        g2.drawString(router1Label, router1LabelX, router1LabelY);
        g2.drawString(router2Label, router2LabelX, router2LabelY);

        //Draw lines in between images
        // i.e. H1 - R1 - R2 - H2
        BasicStroke bold = new BasicStroke(2.0f);
        g2.setStroke(bold);
        g2.draw(new Line2D.Double(host1X+IMAGE_WIDTH, host1Y+(IMAGE_HEIGHT/2), router1X, router1Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router1X+IMAGE_WIDTH, router1Y+(IMAGE_HEIGHT/2), router2X, router2Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(router2X+IMAGE_WIDTH, router2Y+(IMAGE_HEIGHT/2), host2X, host2Y+(IMAGE_HEIGHT/2)));
        g2.draw(new Line2D.Double(layer1X + (LAYER_WIDTH/2), layer1Y + LAYER_HEIGHT, layer1X + (LAYER_WIDTH/2), host1Y));
        g2.draw(new Line2D.Double(layer2X + (LAYER_WIDTH/2), layer2Y + LAYER_HEIGHT, layer2X + (LAYER_WIDTH/2), host2Y));

    }

    /**
     * Set image coordinates based on starting image coordinates.
     * Make each image equal distance apart.
     *
     * Set image label coordinates.
     *
     * @int x and int y -> starting coordinates for image all the way to the left.
     */
    private void setImageCoordinates(int x, int y){
        int xDifference = 250;
        int labelDifference = 50; //Center label over image
        int layerDifference = 45;

        //Initial image coords (host 1)
        host1X = x;
        host1Y = y;

        //Other image coords in relation to first image
        router1X = host1X + xDifference;
        router2X = router1X + xDifference;
        host2X = router2X + xDifference;
        layer1X = host1X - layerDifference;
        layer2X = host2X - layerDifference;

        router1Y = host1Y;
        router2Y = host1Y;
        host2Y = host1Y;
        layer1Y = host1Y - (LAYER_HEIGHT + 40);
        layer2Y = host2Y - (LAYER_HEIGHT + 40);

        //Image label coords
        host1LabelX = host1X + labelDifference;
        router1LabelX = host1LabelX + xDifference;
        router2LabelX = router1LabelX + xDifference;
        host2LabelX = router2LabelX + xDifference;

        host1LabelY = host1Y;
        router1LabelY = host1Y;
        router2LabelY = host2Y;
        host2LabelY = host1Y;

        //Set starting bubble coordinates
        bubbleX = layer1X + (LAYER_WIDTH / 2) - (BUBBLE_SIZE/2);
        bubbleY = layer1Y;
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
        double currentY1 = layer1Y;
        double currentY2 = layer2Y;
        double layerSize = LAYER_HEIGHT / 7;

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

        //DEST LAYERS
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

        //Add bounds in order of being visited.
        hostLayerBounds = new Bounds[]{app1, pres1, sess1, trans1, net1, dat1, phy1};
        destLayerBounds = new Bounds[]{phy2, dat2, net2, trans2, sess2, pres2, app2};
    }

    /**
     * Move the ball in a seperate thread.
     */
   // @Override
    public void run() {
        ArrayList<Node> path = graph.getPath();
        bubble.setTarget(path.get(0));
        Timer timer = new Timer(30, null);
        isRunning = true;

            ActionListener listener = new ActionListener() {
                //Current place in path
                int i = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(isRunning) {
                        if (bubble.move()) {
                            repaint();
                        } else {
                            if (i < path.size() - 1) {
                                i++;
                                bubble.setTarget(path.get(i));
                            } else {
                                timer.stop();
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
        double imageY = host1Y + IMAGE_HEIGHT/2 - BUBBLE_SIZE/2;

        Node node0 = new Node(host1X, imageY, HOST);
        Node node1 = new Node(router1X, imageY, ROUTER);
        Node node2 = new Node(router2X, imageY, ROUTER);
        Node node3 = new Node(host2X, imageY, HOST);
        Node node4 = new Node(host2X + IMAGE_WIDTH/2 - BUBBLE_SIZE/2, layer2Y, LAYER);

        graph.addNode(node0);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
    }

    /*
     * Stop animation.
     */
    public void stop(){
        isRunning = false;
        bubble.reset();
    }
}
