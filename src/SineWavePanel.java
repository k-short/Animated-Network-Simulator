import javax.swing.*;
import java.awt.*;

/**
 * Created by ken12_000 on 2/14/2016.
 *
 * Class to draw sine and digital waves.
 */
public class SineWavePanel extends JPanel {
    private int width;
    private int height;
    private int graphWidth;
    double x;
    double y;
    int yScale = 100;
    boolean isSine = true;

    public SineWavePanel(int w, int h){
        width = w;
        height = h;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(0,height/2,width,height/2); // x-axis
        g.drawLine(10, 0, 10, height); // y-axis

        g.setColor(Color.red);

        int startX = 10;
        graphWidth = width - startX - 10;

        if(isSine) {
            //Draw 8 sin waves, one for each bit in 'A'
            for (int i = 0; i < 8; i++) {
                int[] frequencies = new int[]{8, 16, 8, 8, 8, 8, 8, 16};

                if (frequencies[i] == 8) {
                    for (double x = 0; x <= width - 40; x = x + 0.5) {
                        double y = 40 * Math.sin(x * (3.1415926 / 180));
                        int Y = (int) y;
                        int X = (int) x;

                        g.drawLine(startX + X / frequencies[i], (height / 2) - Y, startX + X / frequencies[i], (height / 2) - Y);
                    }
                } else {
                    for (double x = 0; x <= (width * 2) - 100; x = x + 0.5) {
                        double y = 40 * Math.sin(x * (3.1415926 / 180));
                        int Y = (int) y;
                        int X = (int) x;

                        g.drawLine(startX + X / frequencies[i], (height / 2) - Y, startX + X / frequencies[i], (height / 2) - Y);
                    }
                }
                startX += (width / 8) - 6;
            }
        }
        else{
            int[] digFrequencies = new int[]{0, 1, 0, 0, 0, 0, 0, 1};

            for(int k = 0; k < digFrequencies.length; k++){
                if(digFrequencies[k] == 0){
                    g.drawLine(startX, height/2, startX + (graphWidth/8), height/2);
                    startX += ((graphWidth) / 8);
                }
                else{
                    //g.draw3DRect(startX, height/4, width/8, height/4, false);
                    g.drawLine(startX, height/2, startX, height/4);
                    g.drawLine(startX, height/4, startX+(graphWidth/8), height/4);
                    g.drawLine(startX + (graphWidth/8), height/4, startX + (graphWidth/8), height/2);
                    startX += ((graphWidth) / 8);
                }
            }
        }
    }

    public void setIsSine(boolean value){
        isSine = value;
    }
}
