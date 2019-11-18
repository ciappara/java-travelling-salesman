package app.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import app.*;
import app.models.*;
import app.utils.Helper;
import app.utils.Surface;

public class GeneticAlgorithm {

    int populationSize; // generation size
    int crossoverRate;  // reproduction size
    int maxIterations;
    float mutationRate;
    ArrayList<City> cities2;
    
    int tournamentSize;
    CityPoints cities;
    //int targetFitness;

    public GeneticAlgorithm(CityPoints cities, int maxIterations) { //int targetFitness
        this.cities = cities;
        this.populationSize = 1000;
        this.crossoverRate = 100;
        this.maxIterations = maxIterations; //1000;
        this.mutationRate = 0.5f;
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


    ////////////////////////////////
    // create initial generation
    ////////////////////////////////
    public List<TravelPathGenome> initialPopulation() {
        List<TravelPathGenome> population = new ArrayList<>();
        HashSet<TravelPathGenome> hashSet = new HashSet<>();

        while (population.size() < populationSize) {
            TravelPathGenome chromo = new TravelPathGenome(cities.points);
            if (!hashSet.contains(chromo)) {
                hashSet.add(chromo);
                population.add(chromo);
            }
        }

        return population;
    }


    ////////////////////////////////
    // create new population
    ////////////////////////////////
    public List<TravelPathGenome> createPopulation(List<TravelPathGenome> selectedPopulation){

        List<TravelPathGenome> newPopulation = new ArrayList<>();
        HashSet<TravelPathGenome> hashSet = new HashSet<>();

        while(newPopulation.size() < populationSize){

            // pick 2 random parents
            // List<TravelPathGenome> parents = new ArrayList<>();
            // int a = (int) Math.floor(Helper.random().nextInt(population.size()));
            // int b = (int) Math.floor(Helper.random().nextInt(population.size()));
            
            // parents.add(population.get(a));
            // parents.add(population.get(b));

            List<TravelPathGenome> parents = new ArrayList<>(); // = pickNRandomElements(population, 2);
            //List<TravelPathGenome> parents;
            while(parents.size() < 2) {
                int randNo =  Helper.random().nextInt(selectedPopulation.size());
                TravelPathGenome parent = selectedPopulation.get(randNo);
                parents.add(parent);
                
                //population.remove(randNo);
                //if (!hashSet.contains(parent)) {
                //    hashSet.add(parent);
                //}
            }
            // parents.add(population.get(Helper.random().nextInt(population.size())));
            // parents.add(population.get(Helper.random().nextInt(population.size())));

            // create children from parents
            List<TravelPathGenome> children = crossover(parents);

            // mutate children
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));

            // add children to new population
            //newPopulation.addAll(children);

            for(TravelPathGenome child : children) {
                if (!hashSet.contains(child)) {
                    hashSet.add(child);
                    newPopulation.add(child);
                }
            }
        }

        return newPopulation;
    }


    ////////////////////////////////
    // mutate selected genome
    ////////////////////////////////
    public TravelPathGenome mutate(TravelPathGenome travelpath) {

        float mutate = Helper.random().nextFloat();

        if(mutate < mutationRate) {
            List<Integer> genome = travelpath.getGenome();
            Collections.swap(genome, Helper.random().nextInt(genome.size()), Helper.random().nextInt(genome.size()));

            return new TravelPathGenome(cities.points, genome);
        }

        // return same travel path if not mutated
        return travelpath;
    }
    

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
    

    ////////////////////////////////
    // crossover between two parents
    ////////////////////////////////
    public List<TravelPathGenome> crossover(List<TravelPathGenome> parents){

        // the midpoint (should always skip the first genome which is the starting city)
        int breakpoint = Helper.random().nextInt(parents.get(0).genome.size() - 1) + 1;
        //System.out.println(breakpoint);

        // the new generation
        List<TravelPathGenome> children = new ArrayList<>();

        // copy parental genomes instead of modifying in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // creating child 1
        children.add(createChild(0, breakpoint, parent1Genome, parent2Genome));
        
        // reset edited parent1 genome for the next child
        parent1Genome = parents.get(0).getGenome();

        // creating child 2
        children.add(createChild(breakpoint, parents.size(), parent2Genome, parent1Genome));

        // for(int i = 0; i<breakpoint; i++){
        //     int newVal = parent2Genome.get(i);
        //     Collections.swap(parent1Genome,parent1Genome.indexOf(newVal),i);
        // }
        // children.add(new SalesmanGenome(parent1Genome,numberOfCities,travelPrices,startingCity));
        // parent1Genome = parents.get(0).getGenome(); // reseting the edited parent

        // // creating child 2
        // for(int i = breakpoint; i<genomeSize; i++){
        //     int newVal = parent1Genome.get(i);
        //     Collections.swap(parent2Genome,parent2Genome.indexOf(newVal),i);
        // }
        // children.add(new SalesmanGenome(parent2Genome,numberOfCities,travelPrices,startingCity));

        return children;
    }


    ////////////////////////////////
    // create crossover child
    ////////////////////////////////
    private TravelPathGenome createChild(int startPoint, int endPoint, List<Integer> parent1genome, List<Integer> parent2genome) {

        // create new child
        for(int i = startPoint; i < endPoint; i++){
            int newValue = parent2genome.get(i);
            Collections.swap(parent1genome, parent1genome.indexOf(newValue), i);
        }

        return new TravelPathGenome(this.cities.points, parent1genome);
    }


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