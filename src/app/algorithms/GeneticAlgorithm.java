package app.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import app.*;
import app.core.Crossover;
import app.core.Mutation;
import app.models.*;
import app.utils.Helper;
import app.utils.Surface;

public class GeneticAlgorithm {

    int populationSize; // generation size
    int crossoverRate;  // reproduction size
    int maxIterations;
    float mutationRate;
    
    int tournamentSize;
    CityPoints cities;

    public GeneticAlgorithm(CityPoints cities, int maxIterations) { //int targetFitness
        this.cities = cities;
        this.populationSize = 800;
        this.crossoverRate = 90;
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
    public TravelPathGenome optimize(Surface surface) {

        List<TravelPathGenome> population = this.initialPopulation();
        TravelPathGenome minimumChromosome = population.get(0);
        TravelPathGenome bestEverChromosome = population.get(0);

        for(int i=0; i < this.maxIterations; i++) {

            List<TravelPathGenome> selectedPopulation = this.selection(population);
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
    public List<TravelPathGenome> initialPopulation() {
        List<TravelPathGenome> population = new ArrayList<>();
        HashSet<TravelPathGenome> hashSet = new HashSet<>();

        while (population.size() < populationSize) {

            // create new shuffled cities order
            TravelPathGenome chromo = new TravelPathGenome(cities.points);
            if (!hashSet.contains(chromo)) {

                hashSet.add(chromo);
                population.add(chromo);
            }
        }

        return population;
    }

    // Create new population
    public List<TravelPathGenome> createPopulation(List<TravelPathGenome> selectedPopulation){

        HashSet<Integer[]> hashSet = new HashSet<>();
        List<TravelPathGenome> newPopulation = new ArrayList<>();

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
                    newPopulation.add(new TravelPathGenome(cities.points, child));
                }
            }
        }

        return newPopulation;
    }


    // ////////////////////////////////
    // // mutate selected genome
    // ////////////////////////////////
    // public TravelPathGenome mutate(TravelPathGenome travelpath) {

    //     float mutate = Helper.random().nextFloat();

    //     if(mutate < mutationRate) {
    //         List<Integer> genome = travelpath.getGenome();
    //         Collections.swap(genome, Helper.random().nextInt(genome.size()), Helper.random().nextInt(genome.size()));

    //         return new TravelPathGenome(cities.points, genome);
    //     }

    //     // return same travel path if not mutated
    //     return travelpath;
    // }
    

    ////////////////////////////////
    // generic pick randome elements
    ////////////////////////////////
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        int length = list.size();
        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, Helper.random().nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }
    






    // /**
    //  * Performs a crossover on all the cities between two points.
    //  * @param p1    the first parent chromosome
    //  * @param p2    the second parent chromosome
    //  * @param r     the Random object for selecting a point
    //  * @return      the children
    //  */
    // public ArrayList<TravelPathGenome> orderCrossover (TravelPathGenome p1, TravelPathGenome p2) {
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

    //     ArrayList<TravelPathGenome> children = new ArrayList<>();
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

    //     TravelPathGenome childOne = new TravelPathGenome(cities.points, child1);
    //     TravelPathGenome childTwo = new TravelPathGenome(cities.points, child2);
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
    // public ArrayList<TravelPathGenome> orderCrossover (TravelPathGenome p1, TravelPathGenome p2, Random r) {
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

    //     ArrayList<TravelPathGenome> children = new ArrayList<>();
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

    //     TravelPathGenome childOne = new TravelPathGenome(child1);
    //     TravelPathGenome childTwo = new TravelPathGenome(child2);
    //     children.add(childOne);
    //     children.add(childTwo);

    //     return children;
    // }












    // public void naturalSelection() {
    //     this.matingPool = new List<TravelPathGenome>();

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
    // static TravelPathGenome tournamentSelection (Population population, int k) {
    //     if (k < 1) {
    //         throw new IllegalArgumentException("K must be greater than 0.");
    //     }

    //     TravelPathGenome[] populationAsArray = population.getChromosomes();
    //     ArrayList<TravelPathGenome> kChromosomes = getKChromosomes(populationAsArray, k);
    //     return getChromosome(kChromosomes);
    // }

    /**
     * Returns k randomly selected Chromosomes.
     * @param pop       an array of Chromosomes (a population)
     * @param k         the number of Chromosomes to randomly select
     * @param random    the Random object used for picking a random chromosomes
     * @return          k randomly selected chromosomes
     */
    private static ArrayList<TravelPathGenome> getKChromosomes (TravelPathGenome[] pop, int k) {

        ArrayList<TravelPathGenome> kChromosomes = new ArrayList<>();

        for (int j = 0; j < k; j++) {
            TravelPathGenome chromosome = pop[Helper.random().nextInt(pop.length)];
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
    private static TravelPathGenome getChromosome (ArrayList<TravelPathGenome> arrayList) {

        TravelPathGenome bestChromosome = getBestChromosome(arrayList);
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
    private static TravelPathGenome getBestChromosome (ArrayList<TravelPathGenome> arrayList) {

        TravelPathGenome bestC = null;

        for (TravelPathGenome c : arrayList) {
            if (bestC == null) {
                bestC = c;
            } else if (c.getFitness() < bestC.getFitness()) {
                bestC = c;
            }
        }

        return bestC;
    }




    public List<TravelPathGenome> selection(List<TravelPathGenome> population) {

        List<TravelPathGenome> selected = new ArrayList<>();
        HashSet<TravelPathGenome> hashSet = new HashSet<>();

        //for(int i=0; i < crossoverRate; i++){
        while(selected.size() < crossoverRate) {
            // if(selectionType == SelectionType.ROULETTE){
            //     selected.add(rouletteSelection(population));
            // }
            // else if(selectionType == SelectionType.TOURNAMENT){
            //     selected.add(tournamentSelection(population));
            // }
            
            // add children to new population
            //newPopulation.addAll(children);

            TravelPathGenome chromosome = tournamentSelection(population);
            if (!hashSet.contains(chromosome)) {
                hashSet.add(chromosome);
                selected.add(chromosome);
            }

            //selected.add(rouletteSelection(population));
        }

        return selected;
    }
    

    // select the min chromosome from the random elements selected
    public TravelPathGenome tournamentSelection(List<TravelPathGenome> population) {
        List<TravelPathGenome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }


    public TravelPathGenome rouletteSelection(List<TravelPathGenome> population){
        int totalFitness = population.stream().map(TravelPathGenome::getFitness).mapToInt(Integer::intValue).sum();
        int selectedValue = Helper.random().nextInt(totalFitness);
        float recValue = (float) 1/selectedValue;
        float currentSum = 0;
        for(TravelPathGenome genome : population){
            currentSum += (float) 1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;
            }
        }
        int selectRandom = Helper.random().nextInt(populationSize);
        return population.get(selectRandom);
    }


    ////////////////////////////////
    // print selected population
    ////////////////////////////////
    public void printPopulation(List<TravelPathGenome> population) {

        for(TravelPathGenome genome : population) {
            System.out.println(genome);
        }
    }
}