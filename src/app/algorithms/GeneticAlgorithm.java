package app.algorithms;
import app.core.Crossover;
import app.core.Mutation;
import app.core.Selection;
import app.models.*;
import app.utils.Helper;
import app.utils.Surface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class GeneticAlgorithm {

    int populationSize; // generation size
    int crossoverRate;  // reproduction size
    int maxIterations;
    float mutationRate;
    
    int tournamentSize;
    CityPoints cities;

    public GeneticAlgorithm(CityPoints cities, int maxIterations) { //int targetFitness
        this.cities = cities;
        this.populationSize = 1000;
        this.crossoverRate = 120;
        this.maxIterations = maxIterations;
        this.mutationRate = 0.5f;
        this.tournamentSize = 40;

        if(populationSize < tournamentSize) {
            System.out.println("populationSize must be greater than the tournamentSize");
        }
    }

    ////////////////////////////////
    // optimise and find result
    ////////////////////////////////
    public TravelChromosome optimize(Surface surface) {

        List<TravelChromosome> population = this.initialPopulation();
        TravelChromosome minimumChromosome = population.get(0);
        TravelChromosome bestEverChromosome = population.get(0);

        for(int i = 0; i < this.maxIterations; i++) {

            List<TravelChromosome> selectedPopulation = Selection.select(population, crossoverRate, tournamentSize);
            population = this.createPopulation(selectedPopulation);

            // gets chromosome with minimum distance path of new population
            minimumChromosome = Collections.min(population);

            // if min-dist chromosome has better fitness, replace best ever
            if(minimumChromosome.getFitness() < bestEverChromosome.getFitness()) {
                bestEverChromosome = minimumChromosome;
            }

            // if visualise option is called
            if(surface != null) {
                // get current best and best ever chromosome paths
                ArrayList<City> bestEverPath = cities.getPathFromChromosome(bestEverChromosome);
                ArrayList<City> minimumPath = cities.getPathFromChromosome(minimumChromosome);
    
                // update panel with current-best and best-ever paths
                surface.update(minimumPath, bestEverPath);
                
                // print current-best and best-ever path
                System.out.println(minimumChromosome.toString() + " // ever: " + bestEverChromosome.getFitness());
            }
        }

        return bestEverChromosome;
    }


    ////////////////////////////////
    // Create initial generation/population
    ////////////////////////////////
    public List<TravelChromosome> initialPopulation() {

        int numberOfCities = cities.points.size();
        HashSet<Integer[]> hashSet = new HashSet<>(); // stores all chromosomes created and makes sure they are not the same
        List<TravelChromosome> population = new ArrayList<>();
        
        while (population.size() < populationSize) {

            // create new shuffled cities order
            Integer[] chromosome = TravelChromosome.shuffleCityOrder(numberOfCities);
            if (!hashSet.contains(chromosome)) {

                hashSet.add(chromosome);
                population.add(new TravelChromosome(cities.points, chromosome));
            }
        }

        return population;
    }

    ////////////////////////////////
    // Create new population
    ////////////////////////////////
    public List<TravelChromosome> createPopulation(List<TravelChromosome> selectedPopulation){

        HashSet<Integer[]> hashSet = new HashSet<>();
        List<TravelChromosome> newPopulation = new ArrayList<>();
        //int randPercentage = (int) (populationSize * 0.2);

        while(newPopulation.size() < populationSize){

            // get random parents
            Integer[] parent1Chromosome = selectedPopulation.get(Helper.random().nextInt(selectedPopulation.size())).getOrderChromosome();
            Integer[] parent2Chromosome = selectedPopulation.get(Helper.random().nextInt(selectedPopulation.size())).getOrderChromosome();
            
            // cross and mutate children
            Integer[][] children = Crossover.twoPointCross(parent1Chromosome, parent2Chromosome);
            children[0] = Mutation.mutate(children[0], mutationRate);
            children[1] = Mutation.mutate(children[1], mutationRate);

            // add non-existent children to new population
            for(Integer[] child : children) {
                if (!hashSet.contains(child)) {

                    hashSet.add(child);
                    newPopulation.add(new TravelChromosome(cities.points, child));
                }
            }
        }

        return newPopulation;
    }


    ////////////////////////////////
    // print selected population
    ////////////////////////////////
    public void printPopulation(List<TravelChromosome> population) {

        for(TravelChromosome genome : population) {
            System.out.println(genome);
        }
    }
}