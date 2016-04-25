import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ken12_000 on 1/26/2016.
 */
public class NetworkGUI {
    //Frame title
    final String TITLE = "Network Demo";
    //7 layer Strings

    //Frame to hold all panels
    private JFrame frame;

    //Main panel to fit inside of frame
    private JPanel mainPanel;

    //Graphics panel
    private GraphicPanel graphicPanel;

    //top panel
    private JPanel topPanel;

    //Start button
    private JButton startButton;

    //Pause button
    private JButton pauseButton;

    //Info panel
    private JPanel bottomPanel;

    //Text frame to show bubble info
    private JTextPane dataBoxRed;
    private JTextPane dataBoxBlue;

    //Button for reset
    private JButton resetButton;

    //Panel to hold the sine wave
    SineWavePanel sinePanelRed;
    SineWavePanel sinePanelBlue;

    //Boolean for if graphic panel is currently paused
    private boolean paused = false;

    /**
     * Default constructor for the GUI.
     */
    public NetworkGUI(){
        frame = new JFrame();

        //Get frame ready
        frame.setTitle(TITLE);

        //Set preferred size
        frame.setPreferredSize(new Dimension(1200, 700));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Build the panel and add it to the frame
        buildPanel();

        frame.getContentPane().add(mainPanel);

        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); // Center frame on screen
        frame.setVisible(true);
    }

    /**
     * Build the main panel for the JFrame
     */
    private void buildPanel(){
        //Create main panel containing all panels to be added to frame
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Create panel to hold start button and input box, added to top of main panel
        buildTopPanel();

        //Create graphics panel to be added to main panel
        buildGraphicPanel();

        //Create bottom panel to hold information in message bubble dispaly
        buildBottomPanel();

        //Add panels to main panel
        mainPanel.add(topPanel);
        mainPanel.add(graphicPanel);
        mainPanel.add(bottomPanel);

    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
    }

    /**
     * Build top panel of JFrame.
     * Will hold start button and message input box.
     */
    private void buildTopPanel(){
        topPanel = new JPanel();

        //Create start button and add listener
        startButton = new JButton("Send Message:  'A'");
        startButton.addActionListener(new StartButtonListener());

        //Create reset button and add listener
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetButtonListener());

        //Create pause button and add listener
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new PauseButtonListener());

        //Add button to panel
        topPanel.add(startButton);
        topPanel.add(pauseButton);
        topPanel.add(resetButton);
    }

    /**
     * Build graphic panel to go in middle of JFrame.
     * Will show all animations.
     */
    private void buildGraphicPanel(){
        dataBoxRed = new JTextPane();
        sinePanelRed = new SineWavePanel(410, 100);
        dataBoxBlue = new JTextPane();
        sinePanelBlue = new SineWavePanel(410, 100);

        MessageBubble bubbleR = new MessageBubble(dataBoxRed, sinePanelRed);
        MessageBubble bubbleB = new MessageBubble(dataBoxBlue, sinePanelBlue);


        graphicPanel = new GraphicPanel(bubbleR, bubbleB);
        graphicPanel.setPreferredSize(new Dimension(1200, 500));
        graphicPanel.setMinimumSize(new Dimension(1200, 500));
    }

    /**
     * Build panel for bottom of JFrame.
     * Displays message bubble information.
     */
    private void buildBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        //Dimension for data boxes
        Dimension dim = new Dimension(420, 100);

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);

        StyleConstants.setBold(left, true);
        StyleConstants.setFontSize(left, 8);

        //Set red data box with larger size and give it a frame
        dataBoxRed.setPreferredSize(dim);

        Border borderRed = BorderFactory.createLineBorder(Color.RED);
        dataBoxRed.setBorder(borderRed);
        dataBoxRed.setEditable(false);
        StyledDocument docRed = dataBoxRed.getStyledDocument();
        docRed.setParagraphAttributes(0, docRed.getLength(), left, false);

        //Set up and add sine panel red
        sinePanelRed.setPreferredSize(dim);
        sinePanelRed.setBorder(borderRed);
        sinePanelRed.setIsSine(false);
        sinePanelRed.setVisible(false);

        //Set blue data box with larger size and give it a frame
        dataBoxBlue.setPreferredSize(dim);

        Border borderBlue = BorderFactory.createLineBorder(Color.BLUE);
        dataBoxBlue.setBorder(borderBlue);
        dataBoxBlue.setEditable(false);
        StyledDocument docBlue = dataBoxBlue.getStyledDocument();
        docBlue.setParagraphAttributes(0, docBlue.getLength(), left, false);

        //Set up and add sine panel blue
        sinePanelBlue.setPreferredSize(dim);
        sinePanelBlue.setBorder(borderBlue);
        sinePanelBlue.setIsSine(false);
        sinePanelBlue.setVisible(false);

        //Add data box to panel
        bottomPanel.add(dataBoxRed);
        bottomPanel.add(sinePanelRed);
        bottomPanel.add(dataBoxBlue);
        bottomPanel.add(sinePanelBlue);
    }

    /**
     * Private class for start button listener.
     * When start button pressed, start animation
     */
    private class StartButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(false);
            //new Thread((Runnable) graphicPanel).start();
            graphicPanel.run();
        }
    }

    /**
     * Private class for reset button listener.
     */
    private class ResetButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(true);
            paused = false;
            pauseButton.setText("Pause");
            graphicPanel.stop();
            graphicPanel.resetWeights();
            graphicPanel.repaint();
        }
    }

    /**
     * Private class for pause button listener.
     * Pause the animation of the GraphicPanel object.
     */
    private class PauseButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(paused){
                graphicPanel.setPaused(false);
                pauseButton.setText("Pause");
                paused = false;
            }else {
                graphicPanel.setPaused(true);
                pauseButton.setText("Play");
                paused = true;
            }
        }
    }
}
