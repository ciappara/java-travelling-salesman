package app;
import app.algorithms.GeneticAlgorithm;
import app.algorithms.GeneticNearest;
import app.algorithms.NearestNeighbour;
import app.models.*;
import app.utils.*;
import java.util.*;

public class App {

    // parameter values
    private static int surfaceWidth = 1000;
    private static int surfaceHeight = 1000;
    private static int randomPointsQty = 700;
    private static int maxIterations = 7000;

    // global variables
    private static CityPoints cities;
    private static Surface surface;
    private static boolean isTest = false;
    private static TravelChromosome bestChromosone;
    private static long elapsedTime;
    private static String surfaceTitle;
    
    // settings
    private static boolean enableVisualisation = false;
    
    public static void main(String[] args) throws Exception {

        // get arguments and create title
        String algorithm = args.length > 0 ? args[0] : "GA";
        String filename = args.length > 1 ? args[1] : "test1tsp.txt"; // "finaltest2018.txt"; //"random-2019-11-22-10-42-39.txt";
        setSurfaceTitle(algorithm, filename);

        // load or generate cities
        App.cities = new CityPoints(surfaceWidth, surfaceHeight, randomPointsQty);
        App.cities.getCityPoints(filename);
        
        // visualise if in test mode
        if(isTest) {
            visualise();
        }

        // run chosen algorithm
        if(algorithm.equals("GA")) {
            // create population using a genetic algorithm
            GeneticAlgorithm population = new GeneticAlgorithm(App.cities, App.maxIterations);

            elapsedTime = System.nanoTime();
            bestChromosone = population.optimize(surface);
            elapsedTime = (System.nanoTime() - elapsedTime);
        }
        else if(algorithm.equals("GN")) {
            // create population using a genetic algorithm
            GeneticNearest population = new GeneticNearest(App.cities, App.maxIterations);

            elapsedTime = System.nanoTime();
            bestChromosone = population.optimize(surface);
            elapsedTime = (System.nanoTime() - elapsedTime);
        }
        else if(algorithm.equals("NN")) {
            // specify that a heuristic approach was used on the nearest neighbour
            NearestNeighbour population = new NearestNeighbour();
            
            elapsedTime = System.nanoTime();
            bestChromosone = population.optimize(App.cities);
            elapsedTime = (System.nanoTime() - elapsedTime);
        }
        
        // create surface and visualise result if not in test mode
        visualiseResult(bestChromosone);
    }

    ////////////////////////////////
    // create panel to visualise
    // city points & best ever path
    ////////////////////////////////
    private static void visualise() {
        App.surface = new Surface(App.cities, App.surfaceWidth, App.surfaceHeight, surfaceTitle);
    }

    ////////////////////////////////
    // visualise result points
    ////////////////////////////////
    public static void visualiseResult(TravelChromosome chromosome) {

        System.out.println("\n" + surfaceTitle);
        System.out.println("--------------------------------------");
        
        // print time
        double seconds = (double)elapsedTime / 1_000_000_000.0;
        System.out.println("Elapsed time: " + seconds + " seconds");

        // print distance
        System.out.println("Distance: " + chromosome.getDistance());

        // print path
        System.out.println(chromosome.toString());
        System.out.println("--------------------------------------\n");

        if(!enableVisualisation) { return; }

        // create surface if not available
        if(App.surface == null) {
            visualise();
        }

        // get path from chromosome and update surface
        ArrayList<City> bestEverPath = cities.getPathFromChromosome(chromosome);
        App.surface.update(bestEverPath);
    }

    ////////////////////////////////
    // specify visualization title
    ////////////////////////////////
    public static void setSurfaceTitle(String algorithm, String filename) {
        surfaceTitle = "";

        switch(algorithm) {
            case "GA":
                surfaceTitle = "Genetic Algorithm";
                break;
            case "NN":
                surfaceTitle = "Nearest Neighbour";
                break;
            case "GN":
                surfaceTitle = "Genetic Neighbour";
                break;
        }

        surfaceTitle += " - " + filename;
    }
}