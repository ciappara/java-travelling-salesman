package app;
import java.util.*;
import javax.swing.*;
import java.awt.*;

// creates a canvas to visualise all the cities and the connections between them
public class Surface {

	public static Map<Integer,Point> points;
	public static ArrayList<Integer> drawArray;
	public JFrame frame;

    public Surface() {
        // // create canvas to visualise all the points on your line
		// frame = new JFrame("Genetic Algorithm for TSP");
		// frame.setSize(800,600);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // SurfacePanel panel = new SurfacePanel(drawArray, points);
        // panel.setPrefferredSize(new Dimension(600,400));
        // frame.add(panel, BorderLayout.NORTH);

        // //refresh the jframe to update the plotted points and the shortest path distance.
        // SwingUtilities.updateComponentTreeUI(frame);
    }
}    