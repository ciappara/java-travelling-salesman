package app;
import java.util.*;
import javax.swing.*;
import java.awt.*;

//MyPanel is used to draw the points and edges on the GUI.java main interface.
//The path displyed is the shortest path found from the Genetic.java program
public class SurfacePanel extends JPanel{

    ArrayList<Integer> drawArray;
	Map<Integer,Point> points;
    private static final long serialVersionUID = 1L;

    ArrayList<City> cities;
    ArrayList<City> bestPath;

    public SurfacePanel(ArrayList<City> cities) {
        this.cities = cities;
    }

    // updates the arrays for the cities and best path
    public void update(ArrayList<City> cities, ArrayList<City> bestEverPath) {
        this.cities = cities;
        this.bestPath = bestEverPath;
        this.repaint();
        this.revalidate();
    }
    

    public void paint(Graphics g) {
        // clean surface
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        
        // paint current path
        paintCities(g2, cities, 1, Color.BLACK, true);
        paintCities(g2, bestPath, 2, Color.RED, false);

        // g2.setStroke(new BasicStroke(1));
        // g2.setColor(Color.BLACK);
        // for(int i = 0; i < cities.size(); i++){
        //     g2.drawOval(cities.get(i).x-3, cities.get(i).y-3, 6, 6);
        //     if(i > 0) {
        //         g2.drawLine(cities.get(i).x, cities.get(i).y, cities.get(i - 1).x, cities.get(i - 1).y);
        //     }
        // }

        // // paint best path
        // if(bestPath != null) {
        //     g2.setStroke(new BasicStroke(2));
        //     g2.setColor(Color.RED);
        //     for(int i = 1; i < cities.size(); i++){
        //         g2.drawLine(bestPath.get(i).x, bestPath.get(i).y, bestPath.get(i - 1).x, bestPath.get(i - 1).y);
        //     }
        // }
    }


    public void paintCities(Graphics2D g2, ArrayList<City> cities, int stroke, Color color, boolean drawCityPoint) {
        if(cities == null) {
            return;
        }

        // paint current path
        g2.setStroke(new BasicStroke(stroke));
        g2.setColor(color);
        int cityPointSize = 8;
        int cityPointLocation = cityPointSize / 2;

        for(int i = 0; i < cities.size(); i++){
            
            City current = cities.get(i);
            if(drawCityPoint) {
                g2.fillOval(current.x - cityPointLocation, current.y - cityPointLocation, cityPointSize, cityPointSize);
            }
            if(i > 0) {
                City prev = cities.get(i - 1);
                g2.drawLine(current.x, current.y, prev.x, prev.y);
            }
        }
    }

    public void setSize(int width, int height){
        super.setPreferredSize(new Dimension(width, height));
    }
}