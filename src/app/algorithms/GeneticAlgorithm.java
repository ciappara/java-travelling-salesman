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
        this.maxIterations = maxIterations; //1000;
        this.mutationRate = 0.4f;
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


    // Create initial generation/population
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

    // Create new population
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

        
        // while(newPopulation.size() < populationSize) {
        //     // create new shuffled cities order
        //     Integer[] chromosome = TravelChromosome.shuffleCityOrder(cities.points.size());
        //     if (!hashSet.contains(chromosome)) {

        //         hashSet.add(chromosome);
        //         newPopulation.add(new TravelChromosome(cities.points, chromosome));
        //     }
        // }

        return newPopulation;
    }
    

    






    // /**
    //  * Performs a crossover on all the cities between two points.
    //  * @param p1    the first parent chromosome
    //  * @param p2    the second parent chromosome
    //  * @param r     the Random object for selecting a point
    //  * @return      the children
    //  */
    // public ArrayList<TravelChromosome> orderCrossover (TravelChromosome p1, TravelChromosome p2) {
    //     Integer[] parent1 = p1.getOrderChromosome();
    //     Integer[] parent2 = p2.getOrderChromosome();

    //     // City[] parent1 = p1.getArray();
    //     // City[] parent2 = p2.getArray();

    //     Integer[] child1 = new Integer[parent1.length];
    //     Integer[] child2 = new Integer[parent2.length];

    //     HashSet<Integer> citiesInChild1 = new HashSet<>();
    //     HashSet<Integer> citiesInChild2 = new HashSet<>();

    //     ArrayList<Integer> citiesNotInChild1 = new ArrayList<>();
    //     ArrayList<Integer> citiesNotInChild2 = new ArrayList<>();

    //     ArrayList<TravelChromosome> children = new ArrayList<>();
    //     int totalCities = parent1.length;

    //     // first and second point crossover
    //     int firstPoint = Helper.random().nextInt(totalCities);
    //     int secondPoint = Helper.random().nextInt(totalCities - firstPoint) + firstPoint;

    //     // Inherit the cities before and after the points selected.
    //     for (int i = 0; i < firstPoint; i++) {
    //         child1[i] = parent1[i];
    //         child2[i] = parent2[i];
    //         citiesInChild1.add(parent1[i]);
    //         citiesInChild2.add(parent2[i]);
    //     }
    //     for (int i = secondPoint; i < totalCities; i++) {
    //         child1[i] = parent1[i];
    //         child2[i] = parent2[i];
    //         citiesInChild1.add(parent1[i]);
    //         citiesInChild2.add(parent2[i]);
    //     }

    //     // Get the cities of the opposite parent if the child does not already contain them.
    //     for (int i = firstPoint; i < secondPoint; i++) {
    //         if (!citiesInChild1.contains(parent2.get(i))) {
    //             citiesInChild1.add(parent2.get(i));
    //             child1[i] = parent2.get(i);
    //         }
    //         if (!citiesInChild2.contains(parent1.get(i))) {
    //             citiesInChild2.add(parent1.get(i));
    //             child2[i] = parent1.get(i);
    //         }
    //     }

    //     // Find all the cities that are still missing from each child.
    //     for (int i = 0; i < totalCities; i++) {
    //         if (!citiesInChild1.contains(parent2.get(i))) {
    //             citiesNotInChild1.add(parent2.get(i));
    //         }
    //         if (!citiesInChild2.contains(parent1.get(i))) {
    //             citiesNotInChild2.add(parent1.get(i));
    //         }
    //     }

    //     // Find which spots are still empty in each child.
    //     ArrayList<Integer> emptySpotsC1 = new ArrayList<>();
    //     ArrayList<Integer> emptySpotsC2 = new ArrayList<>();
    //     for (int i = 0; i < totalCities; i++) {
    //         if (child1[i] == null) {
    //             emptySpotsC1.add(i);
    //         }
    //         if (child2[i] == null) {
    //             emptySpotsC2.add(i);
    //         }
    //     }

    //     // Fill in the empty spots.
    //     for (Integer city : citiesNotInChild1) {
    //         child1[emptySpotsC1.remove(0)] = city;
    //     }
    //     for (Integer city : citiesNotInChild2) {
    //         child2[emptySpotsC2.remove(0)] = city;
    //     }

    //     TravelChromosome childOne = new TravelChromosome(cities.points, child1);
    //     TravelChromosome childTwo = new TravelChromosome(cities.points, child2);
    //     children.add(childOne);
    //     children.add(childTwo);

    //     return children;
    // }







    //     /**
    //  * Performs a crossover on all the cities between two points.
    //  * @param p1    the first parent chromosome
    //  * @param p2    the second parent chromosome
    //  * @param r     the Random object for selecting a point
    //  * @return      the children
    //  */
    // public ArrayList<TravelChromosome> orderCrossover (TravelChromosome p1, TravelChromosome p2, Random r) {
    //     ArrayList<City> parent1 = cities.getPathFromChromosome(p1);
    //     ArrayList<City> parent2 = cities.getPathFromChromosome(p2);

    //     // City[] parent1 = p1.getArray();
    //     // City[] parent2 = p2.getArray();

    //     City[] child1 = new City[parent1.size()];
    //     City[] child2 = new City[parent2.size()];

    //     HashSet<City> citiesInChild1 = new HashSet<>();
    //     HashSet<City> citiesInChild2 = new HashSet<>();

    //     ArrayList<City> citiesNotInChild1 = new ArrayList<>();
    //     ArrayList<City> citiesNotInChild2 = new ArrayList<>();

    //     ArrayList<TravelChromosome> children = new ArrayList<>();
    //     int totalCities = parent1.size();

    //     int firstPoint = r.nextInt(totalCities);
    //     int secondPoint = r.nextInt(totalCities - firstPoint) + firstPoint;

    //     // Inherit the cities before and after the points selected.
    //     for (int i = 0; i < firstPoint; i++) {
    //         child1[i] = parent1.get(i);
    //         child2[i] = parent2.get(i);
    //         citiesInChild1.add(parent1.get(i));
    //         citiesInChild2.add(parent2.get(i));
    //     }
    //     for (int i = secondPoint; i < totalCities; i++) {
    //         child1[i] = parent1.get(i);
    //         child2[i] = parent2.get(i);
    //         citiesInChild1.add(parent1.get(i));
    //         citiesInChild2.add(parent2.get(i));
    //     }

    //     // Get the cities of the opposite parent if the child does not already contain them.
    //     for (int i = firstPoint; i < secondPoint; i++) {
    //         if (!citiesInChild1.contains(parent2.get(i))) {
    //             citiesInChild1.add(parent2.get(i));
    //             child1[i] = parent2.get(i);
    //         }
    //         if (!citiesInChild2.contains(parent1.get(i))) {
    //             citiesInChild2.add(parent1.get(i));
    //             child2[i] = parent1.get(i);
    //         }
    //     }

    //     // Find all the cities that are still missing from each child.
    //     for (int i = 0; i < totalCities; i++) {
    //         if (!citiesInChild1.contains(parent2.get(i))) {
    //             citiesNotInChild1.add(parent2.get(i));
    //         }
    //         if (!citiesInChild2.contains(parent1.get(i))) {
    //             citiesNotInChild2.add(parent1.get(i));
    //         }
    //     }

    //     // Find which spots are still empty in each child.
    //     ArrayList<Integer> emptySpotsC1 = new ArrayList<>();
    //     ArrayList<Integer> emptySpotsC2 = new ArrayList<>();
    //     for (int i = 0; i < totalCities; i++) {
    //         if (child1[i] == null) {
    //             emptySpotsC1.add(i);
    //         }
    //         if (child2[i] == null) {
    //             emptySpotsC2.add(i);
    //         }
    //     }

    //     // Fill in the empty spots.
    //     for (City city : citiesNotInChild1) {
    //         child1[emptySpotsC1.remove(0)] = city;
    //     }
    //     for (City city : citiesNotInChild2) {
    //         child2[emptySpotsC2.remove(0)] = city;
    //     }

    //     TravelChromosome childOne = new TravelChromosome(child1);
    //     TravelChromosome childTwo = new TravelChromosome(child2);
    //     children.add(childOne);
    //     children.add(childTwo);

    //     return children;
    // }












    // public void naturalSelection() {
    //     this.matingPool = new List<TravelChromosome>();

    //     // find the maximum fitness
    //     float maxFitness = 0;
    //     //int totalFitness = 0;
    //     for(int i=0; i< this.population.length; i++) {
    //         float popfitness = this.population[i].fitness;
    //         if(popfitness > maxFitness) {
    //             maxFitness = popfitness;
    //         }
    //         //totalFitness += popfitness;
    //     }

    //     // add the top populations in the matingPool bucket
    //     for(int i=0; i < this.population.length; i++) {
            
    //         // to find N, we are first checking what is the maxFitness value
    //         // then take the element fitness, and give it a number from 0 to maxFitness
    //         float fitness = Helper.Remap(this.population[i].fitness, 0, maxFitness, 0, 1);
    //         float n = (float) Math.floor(fitness * 100);

    //         for(int j = 0; j < n; j++) {
    //             this.matingPool.add(this.population[i]);
    //         }
    //     }
    // }


        /**
     * Picks k Chromosomes at at random and then return the best one.
     * There is a small chance that the best one will not be selected.
     * @param population    the population to selected from
     * @param k             the number of chromosomes to select
     * @param random        the Random object for randomly selecting
     * @return              usually the fittest Chromosome from k randomly selected chromosomes
     */
    // static TravelChromosome tournamentSelection (Population population, int k) {
    //     if (k < 1) {
    //         throw new IllegalArgumentException("K must be greater than 0.");
    //     }

    //     TravelChromosome[] populationAsArray = population.getChromosomes();
    //     ArrayList<TravelChromosome> kChromosomes = getKChromosomes(populationAsArray, k);
    //     return getChromosome(kChromosomes);
    // }

    /**
     * Returns k randomly selected Chromosomes.
     * @param pop       an array of Chromosomes (a population)
     * @param k         the number of Chromosomes to randomly select
     * @param random    the Random object used for picking a random chromosomes
     * @return          k randomly selected chromosomes
     */
    private static ArrayList<TravelChromosome> getKChromosomes (TravelChromosome[] pop, int k) {

        ArrayList<TravelChromosome> kChromosomes = new ArrayList<>();

        for (int j = 0; j < k; j++) {
            TravelChromosome chromosome = pop[Helper.random().nextInt(pop.length)];
            kChromosomes.add(chromosome);
        }

        return kChromosomes;
    }

    /**
     * Get the best Chromosome in a list of Chromosomes. There is a small chance
     * that a randomly selected Chromosome is picked instead of the best one.
     * @param arrayList     the list of Chromosomes
     * @param random        the Random object used for selecting a random Chromosome if needed
     * @return              usually the best Chromosome
     */
    private static TravelChromosome getChromosome (ArrayList<TravelChromosome> arrayList) {

        TravelChromosome bestChromosome = getBestChromosome(arrayList);
        int ODDS_OF_NOT_PICKING_FITTEST = 5;

        // 1 in 5 chance to return a chromosome that is not the best.
        if (Helper.random().nextInt(ODDS_OF_NOT_PICKING_FITTEST) == 0 && arrayList.size() != 1) {
            arrayList.remove(bestChromosome);
            return arrayList.get(Helper.random().nextInt(arrayList.size()));
        }

        return bestChromosome;
    }

    /**
     * Get the best Chromosome in a list of Chromosomes.
     * @param arrayList     the list to search
     * @return              the best chromosome
     */
    private static TravelChromosome getBestChromosome (ArrayList<TravelChromosome> arrayList) {

        TravelChromosome bestC = null;

        for (TravelChromosome c : arrayList) {
            if (bestC == null) {
                bestC = c;
            } else if (c.getFitness() < bestC.getFitness()) {
                bestC = c;
            }
        }

        return bestC;
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