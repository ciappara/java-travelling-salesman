package app;
import java.util.*;
import javax.swing.*;
import java.awt.geom.Point2D;

public class App {

    static SurfacePanel panel;
    static int citiesQty = 20, surfaceWidth = 800, surfaceHeight = 600;
    static ArrayList<City> cities;
    static ArrayList<City> bestEverPath;
    static double bestEverDistance = -1.0;
    static boolean isTest = true;
    
    public static void main(String[] args) throws Exception {

        cities = new ArrayList<City>();
        bestEverPath = new ArrayList<City>();

        // todo: load cities

        // randomise cities if nothing is loaded
        createRandomCityPoints(App.citiesQty);

        // initialise distance
        findBestEver();

        // visualise
        if(isTest) { 
            visualise();
        }
        
        // swap cities to find the best path
        for(int i = 0; i < 1000000; i++) {
            int a = (int) Math.floor(Helper.Rand().nextInt(cities.size()));
            int b = (int) Math.floor(Helper.Rand().nextInt(cities.size()));
            Helper.Swap(cities, a, b);
            findBestEver();
            
            if(isTest) {
                panel.update(cities, bestEverPath);
                SwingUtilities.updateComponentTreeUI(panel);
            }
        }
    }

    // creates random points for the amount of cities set
    public static void createRandomCityPoints(int citiesQty) {
        for(int i = 0; i < citiesQty; i++) {
            cities.add(new City(Helper.Rand().nextInt(surfaceWidth), Helper.Rand().nextInt(surfaceHeight)));
        }
    }

    // find the best ever distance and store the path
    public static void findBestEver() {
        double totalDistance = calculateDistance();

        if(bestEverDistance == -1 || totalDistance < bestEverDistance) {
            bestEverDistance = totalDistance;
            bestEverPath = new ArrayList<>(cities);

            if(isTest) {
                System.out.println("bestEver: " + bestEverDistance);
            }
        }
    }

    // calculate distance between two points
    public static double calculateDistance() {
        double totalDistance = 0;

        for(int i = 0; i < cities.size() - 1; i++) {
            City city = cities.get(i);
            City next = cities.get(i + 1);
            double dist = Point2D.distance(city.x, city.y, next.x, next.y);
            totalDistance += dist;
        }

        return totalDistance;
    }

    // create a new jpanel to visualise the city points and best ever path
    public static void visualise() {

        // create canvas to visualise all the points on your line
		JFrame frame = new JFrame("Genetic Algorithm for TSP");
        frame.setSize(surfaceWidth, surfaceHeight);
        // once frame is closed, stop the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setup panel for the graphics
        panel = new SurfacePanel(cities);
        panel.setSize(surfaceWidth, surfaceHeight);
        frame.add(panel);

        // refresh the jframe to update the plotted points and the shortest path distance.
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
    }
}