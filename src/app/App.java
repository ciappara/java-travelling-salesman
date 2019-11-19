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
    private static int randomPointsQty = 50;
    private static int maxIterations = 2000;

    // global variables
    private static CityPoints cities;
    private static Surface surface;
    private static boolean isTest = true;
    private static String algorithm = "NN";
    private static TravelChromosome bestChromosone;
    
    public static void main(String[] args) throws Exception {

        // load or generate cities
        App.cities = new CityPoints(surfaceWidth, surfaceHeight, randomPointsQty);
        App.cities.getCityPoints("random-2019-11-19-12-37-33.txt"); //"test1tsp.txt");
        
        // visualise if in test mode
        if(isTest) {
            visualise();
        }

        
        if(algorithm == "GA") {
            // create population using a genetic algorithm
            GeneticAlgorithm population = new GeneticAlgorithm(App.cities, App.maxIterations);
            bestChromosone = population.optimize(surface);
        }
        else if(algorithm == "NN") {
            NearestNeighbour population = new NearestNeighbour();
            bestChromosone = population.optimize(App.cities);
            visualiseResult(bestChromosone);
        }


        if(!isTest) {
            visualiseResult(bestChromosone);
        }
        System.out.println(bestChromosone);
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
        visualise();

        ArrayList<City> bestEverPath = cities.getPathFromChromosome(chromosome);
        System.out.println(cities.toString());

        // todo: fix update to allow only for one value
        surface.update(bestEverPath, bestEverPath);
    }
}