package app.utils;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import app.TravelPathGenome;
import app.models.*;
import app.utils.Helper;

// SurfacePanel is used to draw the points and shortest path on the GUI
public class SurfacePanel extends JPanel{

    private static final long serialVersionUID = 1L;

    CityPoints cities;
    
    TravelPathGenome minimumChromosome;
    TravelPathGenome bestEverChromosome;

    ArrayList<City> minimumPath;
    ArrayList<City> bestEverPath;

    int width, height;

    public SurfacePanel(CityPoints cities, int width, int height) {
        this.cities = cities;
        this.width = width;
        this.height = height;
        super.setPreferredSize(new Dimension(this.width, this.height));
    }

    ////////////////////////////////
    // update arrays for cities 
    // and best path
    ////////////////////////////////
    public void update(ArrayList<City> minimumPath, ArrayList<City> bestEverPath) {
        this.minimumPath = minimumPath;
        this.bestEverPath = bestEverPath;
        
        this.repaint();
        this.revalidate();
    }

    ////////////////////////////////
    // paint and repaint cities
    // on panel
    ////////////////////////////////
    public void paint(Graphics g) {
        // clean surface
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        // paint current path
        paintCities(g2, this.minimumPath, 1, Color.BLACK, true);
        paintCities(g2, this.bestEverPath, 2, Color.RED, false);
    }

    ////////////////////////////////
    // paint city points on panel
    ////////////////////////////////
    private void paintCities(Graphics2D g2, ArrayList<City> path, int stroke, Color color, boolean drawCityPoint) {
        
        if(cities == null || path == null) {
            return;
        }

        // set color options
        g2.setStroke(new BasicStroke(stroke));
        g2.setColor(color);
        
        int cityPointSize = 8;
        int cityPointLocation = cityPointSize / 2;

        // draw path
        for(int i = 0; i < path.size(); i++){
            
            City current = path.get(i);

            // remap current x and y from min/max to 800/600
            int currentX = Helper.remap(current.x, this.cities.minX, this.cities.maxX, 10, this.width - 10);
            int currentY = Helper.remap(current.y, this.cities.minY, this.cities.maxY, 10, this.height - 10);
            //System.out.println(current.x + "/" + current.y + " = " + currentX + "/" + currentY);

            // draw city point
            if(drawCityPoint) {
                g2.fillOval(currentX - cityPointLocation, currentY - cityPointLocation, cityPointSize, cityPointSize);
            }
            else {
                if(i == 0) {
                    g2.setColor(Color.GREEN);
                    g2.fillOval(currentX - cityPointLocation, currentY - cityPointLocation, cityPointSize, cityPointSize);   
                }
                else {                    
                    g2.setColor(color);
                }
            }

            // draw line
            if(i > 0) {
                City prev = path.get(i - 1);
                int prevX = Helper.remap(prev.x, this.cities.minX, this.cities.maxX, 10, this.width - 10);
                int prevY = Helper.remap(prev.y, this.cities.minY, this.cities.maxY, 10, this.height - 10);
                g2.drawLine(currentX, currentY, prevX, prevY);
            }
        }
    }
}