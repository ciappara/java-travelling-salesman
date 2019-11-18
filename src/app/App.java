package app;
import java.awt.Color;
import java.util.*;
import javax.swing.*;

import app.algorithms.GeneticAlgorithm;
import app.models.*;
import app.utils.*;

public class App {

    // parameter values
    private static int surfaceWidth = 800;
    private static int surfaceHeight = 800;
    private static int randomPointsQty = 20;
    private static int maxIterations = 1000;

    // global variables
    private static CityPoints cities;
    private static Surface surface;
    private static SurfacePanel panel;
    private static boolean isTest = true;
    
    public static void main(String[] args) throws Exception {

        // load or generate cities
        App.cities = new CityPoints(surfaceWidth, surfaceHeight, randomPointsQty);
        App.cities.getCityPoints("test2tsp.txt");
        
        // visualise if in test mode
        if(isTest) {
            visualise();
        }
        
        // create population using a genetic algorithm
        GeneticAlgorithm population = new GeneticAlgorithm(App.cities.points, App.maxIterations); //1
        
        // find best chromosome by optimising
        TravelPathGenome bestChromosone = isTest
            ? optimizeWithPanel(population, maxIterations)
            : population.optimize();

        if(!isTest) { visualiseResult(bestChromosone); }
        System.out.println(bestChromosone);
    }


    ////////////////////////////////
    // optimise and find result
    ////////////////////////////////
    public static TravelPathGenome optimizeWithPanel(GeneticAlgorithm geneticAlgorithm, int maxIterations) {

        List<TravelPathGenome> newPopulation = geneticAlgorithm.initialPopulation();
        TravelPathGenome minimumChromosome = newPopulation.get(0);
        TravelPathGenome bestEverChromosome = newPopulation.get(0);
        ArrayList<City> bestEverPath = cities.getPathFromChromosome(bestEverChromosome);

        for(int i=0; i < maxIterations; i++) {

            List<TravelPathGenome> selectedPopulation = geneticAlgorithm.selection(newPopulation);
            newPopulation = geneticAlgorithm.createPopulation(selectedPopulation);

            // gets chromosome with minimum distance path of new population
            minimumChromosome = Collections.min(newPopulation);

            // test if min-dist chromosome has best ever path
            if(minimumChromosome.getFitness() < bestEverChromosome.getFitness()) {
                bestEverChromosome = minimumChromosome;
                bestEverPath = cities.getPathFromChromosome(bestEverChromosome);
            }

            // get current best chromosome path
            ArrayList<City> minimumPath = cities.getPathFromChromosome(minimumChromosome);

            // update panel with current-best and best-ever paths
            surface.update(minimumPath, bestEverPath);
            //panel.update(minimumPath, bestEverPath);

            // print current-best and best-ever path
            System.out.println(minimumChromosome.toString() + " // ever: " + bestEverChromosome.fitness);
        }

        // todo: not sure it's requied
        // // final update panel with current-best and best-ever paths
        // panel.update(bestEverGenomePath, bestEverGenomePath);
        // SwingUtilities.updateComponentTreeUI(panel);

        return bestEverChromosome;
    }

    ////////////////////////////////
    // print result and points
    ////////////////////////////////
    public static void visualiseResult(TravelPathGenome chromosome) {
        visualise();

        ArrayList<City> bestEverPath = cities.getPathFromChromosome(chromosome);
        System.out.println(bestEverPath.toString());

        // todo: fix update to allow only for one value
        surface.update(bestEverPath, bestEverPath);
    }


    // todo: move this to the Surface.java class

    ////////////////////////////////
    // create jpanel to visualise
    // city points & best ever path
    ////////////////////////////////
    public static void visualise() {
        
        App.surface = new Surface(App.cities, App.surfaceWidth, App.surfaceHeight);

        // // create canvas to visualise all the points on your line
		// JFrame frame = new JFrame("Genetic Algorithm for TSP");
        // frame.setSize(surfaceWidth + 70, surfaceHeight + 70);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // stop app on closing frame
        // frame.setBackground(Color.RED);

        // // setup panel for the graphics
        // panel = new SurfacePanel(App.cities, surfaceWidth, surfaceHeight);
        // frame.add(panel);

        // // refresh the jframe to update the plotted points and the shortest path distance.
        // SwingUtilities.updateComponentTreeUI(frame);
        // frame.setVisible(true);
    }
}