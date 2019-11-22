package app;
import java.util.*;
import app.algorithms.GeneticAlgorithm;
import app.algorithms.NearestNeighbour;
import app.models.*;
import app.utils.*;

public class App {

    // parameter values
    private static int surfaceWidth = 800;
    private static int surfaceHeight = 800;
    private static int randomPointsQty = 200;
    private static int maxIterations = 2000;

    // global variables
    private static CityPoints cities;
    private static Surface surface;
    private static boolean isTest = true;
    private static TravelChromosome bestChromosone;
    private static long elapsedTime;
    
    public static void main(String[] args) throws Exception {

        // IMPORTANT!!
        // add timer
        // store distinct coordinates only

        String algorithm = args.length > 0 ? args[0] : "GA";
        String filename = args.length > 1 ? args[1] : "random-2019-11-22-10-42-39.txt";

        // load or generate cities
        App.cities = new CityPoints(surfaceWidth, surfaceHeight, randomPointsQty);
        App.cities.getCityPoints(filename);
        
        // visualise if in test mode
        if(isTest) {
            visualise();
        }

        
        if(algorithm == "GA") {
            // create population using a genetic algorithm
            GeneticAlgorithm population = new GeneticAlgorithm(App.cities, App.maxIterations);

            elapsedTime = System.nanoTime();
            bestChromosone = population.optimize(surface);
            elapsedTime = System.nanoTime() - elapsedTime;
        }
        else if(algorithm == "NN") {
            // specify that a heuristic approach was used on the nearest neighbour
            NearestNeighbour population = new NearestNeighbour();
            
            elapsedTime = System.nanoTime();
            bestChromosone = population.optimize(App.cities);
            elapsedTime = System.nanoTime() - elapsedTime;
        }
        
        //if(!isTest) {
            visualiseResult(bestChromosone);
        //}
        //System.out.println(bestChromosone);
    }

    ////////////////////////////////
    // create panel to visualise
    // city points & best ever path
    ////////////////////////////////
    public static void visualise() {
        App.surface = new Surface(App.cities, App.surfaceWidth, App.surfaceHeight);
    }

    ////////////////////////////////
    // visualise result points
    ////////////////////////////////
    public static void visualiseResult(TravelChromosome chromosome) {

        System.out.println("------------------------------");
        
        // print time
        double seconds = (double)elapsedTime / 1_000_000_000.0;
        System.out.println("Elapsed time: " + seconds + " seconds");

        // print distance
        System.out.println("Distance: " + chromosome.getDistance());

        // print path
        System.out.println(chromosome.toString());

        System.out.println("------------------------------");

        if(App.surface == null) {
            visualise();
        }

        // todo: fix update to allow only for one value
        ArrayList<City> bestEverPath = cities.getPathFromChromosome(chromosome);
        surface.update(bestEverPath, bestEverPath);
    }
}