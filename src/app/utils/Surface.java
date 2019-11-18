package app.utils;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import app.models.*;

// creates a canvas to visualise all the cities and the connections between them
public class Surface {

    JFrame frame;
    SurfacePanel panel;
    CityPoints cities;

    public Surface(CityPoints cities, int width, int height) {

        this.cities = cities;

        // create canvas to visualise all the points on your line
		frame = new JFrame("TSP: Genetic Algorithm");
        frame.setSize(width + 70, height + 70);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stop app on closing frame
        frame.setBackground(Color.RED);

        // setup panel for the graphics
        panel = new SurfacePanel(this.cities, width, height);
        frame.add(panel);

        // refresh the jframe to update the plotted points and the shortest path distance.
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);


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

    public void update(ArrayList<City> minimumPath, ArrayList<City> bestEverPath) {
        panel.update(minimumPath, bestEverPath);
        SwingUtilities.updateComponentTreeUI(panel);
    }
}