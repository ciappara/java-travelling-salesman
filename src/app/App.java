package app;
import java.util.*;
import javax.swing.*;
//import java.awt.geom.Point2D;
import app.models.*;
import app.utils.*;

public class App {

    private static CityPoints cities;

    // parameter values
    private static int surfaceWidth = 800;
    private static int surfaceHeight = 600;
    private static int randomPointsQty = 20;
    private static int maxIterations = 1000;



    // static ArrayList<City> cities;
    // static ArrayList<City> bestEverPath;
    static double bestEverDistance = -1.0;


    private static boolean isTest = true;
    private static SurfacePanel panel;
    
    public static void main(String[] args) throws Exception {

        // load or generate cities
        App.cities = new CityPoints(surfaceWidth, surfaceHeight, randomPointsQty);
        App.cities.getCityPoints("test2tsp.txt");
        
        // visualise if in test mode
        if(isTest) { 
            visualise();
        }
        
        Population geneticAlgorithm = new Population(App.cities.points, 1, App.maxIterations);

        TravelPathGenome bestChromosone;
        
        if(isTest) {
            bestChromosone = optimizeWithPanel(geneticAlgorithm, maxIterations);
        }
        else {
            bestChromosone = geneticAlgorithm.optimize();
            printResultOnPanel(bestChromosone);
        }

        System.out.println(bestChromosone);
    }


    ////////////////////////////////
    // optimise and find result
    ////////////////////////////////
    public static TravelPathGenome optimizeWithPanel(Population geneticAlgorithm, int maxIterations) {

        List<TravelPathGenome> newPopulation = geneticAlgorithm.initialPopulation();
        TravelPathGenome bestEverGenome = newPopulation.get(0);
        TravelPathGenome bestGenome = newPopulation.get(0);
        ArrayList<City> bestEverGenomePath = GetGenomeAsPath(bestEverGenome);

        for(int i=0; i < maxIterations; i++) {

            List<TravelPathGenome> selectedPopulation = geneticAlgorithm.selection(newPopulation);
            newPopulation = geneticAlgorithm.createPopulation(selectedPopulation);
            bestGenome = Collections.min(newPopulation);

            if(bestGenome.getFitness() < bestEverGenome.getFitness()) {
                bestEverGenome = bestGenome;
                bestEverGenomePath = GetGenomeAsPath(bestEverGenome);
                //bestEverGenomePath = GetGenomeAsPath(bestEverGenome);
            }

            // visualise global best chromosome (genome)
            // ArrayList<City> bestGenomePath = new ArrayList<City>();
            // for(int gene : bestGenome.genome) {
            //     City current = cities.get(gene);
            //     bestGenomePath.add(new City(current.id, current.x, current.y));
            // }
            ArrayList<City> bestGenomePath = GetGenomeAsPath(bestGenome);
            //bestEverGenomePath = GetGenomeAsPath(bestEverGenome);

            panel.update(bestGenomePath, bestEverGenomePath);
            SwingUtilities.updateComponentTreeUI(panel);
            System.out.println(bestGenome.toString() + " // ever: " + bestEverGenome.fitness);
        }

        panel.update(bestEverGenomePath, bestEverGenomePath);
        SwingUtilities.updateComponentTreeUI(panel);
        return bestGenome;
    }

    public static ArrayList<City> GetGenomeAsPath(TravelPathGenome genome) {
        ArrayList<City> genomeAsPath = new ArrayList<City>();

        for(int gene : genome.genome) {
            City city = cities.points.get(gene);
            genomeAsPath.add(new City(city.x, city.y, city.id));
        }

        return genomeAsPath;
    }

    public static void printResultOnPanel(TravelPathGenome result) {
        visualise();
        ArrayList<City> bestEverGenomePath = GetGenomeAsPath(result);
        System.out.println(bestEverGenomePath.toString());
        panel.update(bestEverGenomePath, bestEverGenomePath);
        SwingUtilities.updateComponentTreeUI(panel);
    }



    // public static void main2(String[] args) throws Exception {

    //     cities = new ArrayList<City>();
    //     bestEverPath = new ArrayList<City>();

    //     // todo: load cities

    //     // randomise cities if nothing is loaded
    //     createRandomCityPoints(App.citiesQty);

    //     // initialise distance
    //     findBestEver();

    //     // visualise
    //     if(isTest) { 
    //         visualise();
    //     }
        
    //     //randomSelection();

    //     int maxIterations = 1000;
    //     Population geneticAlgorithm = new Population(cities, 1, maxIterations);
    //     TravelPathGenome result;
    //     if(isTest) {
    //         result = optimizeWithPanel(geneticAlgorithm, maxIterations);
    //     }
    //     else {
    //         result = geneticAlgorithm.optimize();
    //     }
    //     System.out.println(result);
    // }

    // public static void randomSelection() {

    //     // swap cities to find the best path
    //     for(int i = 0; i < 1000000; i++) {
    //         int a = (int) Math.floor(Helper.random().nextInt(cities.size() - 1)) + 1;
    //         int b = (int) Math.floor(Helper.random().nextInt(cities.size() - 1)) + 1;
    //         Helper.swap(cities, a, b);
    //         findBestEver();
            
    //         if(isTest) {
    //             panel.update(cities, bestEverPath);
    //             SwingUtilities.updateComponentTreeUI(panel);
    //         }
    //     }
    // }

    // // find the best ever distance and store the path
    // public static void findBestEver() {
    //     double totalDistance = calculateDistance();

    //     if(bestEverDistance == -1 || totalDistance < bestEverDistance) {
    //         bestEverDistance = totalDistance;
    //         bestEverPath = new ArrayList<>(cities);

    //         if(isTest) {
    //             System.out.println("bestEver: " + bestEverDistance);
    //         }
    //     }
    // }

    // // calculate distance between two points
    // public static double calculateDistance() {
    //     double totalDistance = 0;

    //     for(int i = 0; i < cities.size() - 1; i++) {
    //         City city = cities.get(i);
    //         City next = cities.get(i + 1);
    //         double dist = Point2D.distance(city.x, city.y, next.x, next.y);
    //         totalDistance += dist;
    //     }

    //     return totalDistance;
    // }

    // create a new jpanel to visualise the city points and best ever path
    public static void visualise() {

        // create canvas to visualise all the points on your line
		JFrame frame = new JFrame("Genetic Algorithm for TSP");
        frame.setSize(surfaceWidth, surfaceHeight);
        // once frame is closed, stop the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setup panel for the graphics
        panel = new SurfacePanel(App.cities.points);
        panel.setSize(surfaceWidth, surfaceHeight);
        frame.add(panel);

        // refresh the jframe to update the plotted points and the shortest path distance.
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
    }
}