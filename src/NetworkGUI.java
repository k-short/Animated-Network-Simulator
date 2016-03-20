import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
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

    //Info panel
    private JPanel bottomPanel;

    //Text frame to show bubble info
    private JTextPane dataBox;

    //Button for reset
    private JButton resetButton;


    //String to hold data box info
    private String bubbleData;

    //Message bubble starting coordinates
    private double bubbleX;
    private double bubbleY;

    //Message bubble size
    private final int BUBBLE_SIZE = 40;

    //Message bubble
    MessageBubble bubble;

    //File to write bubble data to
    private final String FILE_NAME = "bubble_data.txt";
    private Path filePath = Paths.get(FILE_NAME);

    //Panel to hold the sine wave
    SineWavePanel sinePanel;

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

        //Add button to panel
        topPanel.add(startButton);
        topPanel.add(resetButton);
    }

    /**
     * Build graphic panel to go in middle of JFrame.
     * Will show all animations.
     */
    private void buildGraphicPanel(){
        dataBox = new JTextPane();
        sinePanel = new SineWavePanel(410, 100);
        bubble = new MessageBubble(dataBox, sinePanel);

        graphicPanel = new GraphicPanel(bubble);
        graphicPanel.setPreferredSize(new Dimension(1200, 600));
    }

    /**
     * Build panel for bottom of JFrame.
     * Displays message bubble information.
     */
    private void buildBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 50, 10));

        //Set data box with larger size and give it a frame
        dataBox.setPreferredSize(new Dimension(400, 100));

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        dataBox.setBorder(border);
        dataBox.setEditable(false);
        StyledDocument doc = dataBox.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setBold(center, true);
        StyleConstants.setFontSize(center, 18);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        //Set up and add sine panel
        sinePanel.setPreferredSize(new Dimension(410, 100));
        sinePanel.setBorder(border);
        sinePanel.setIsSine(false);
        sinePanel.setVisible(false);

        //Add data box to panel
        bottomPanel.add(dataBox);
        bottomPanel.add(sinePanel);
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
            graphicPanel.stop();
            graphicPanel.repaint();
        }
    }
}
