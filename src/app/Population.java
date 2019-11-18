package app;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import app.models.*;

public class Population {

    int populationSize; // generation size
    int crossoverRate;  // reproduction size
    int maxIterations;
    float mutationRate;
    int targetFitness;
    ArrayList<City> cities;
    
    int tournamentSize;

    public Population(ArrayList<City> cities, int targetFitness, int maxIterations) {
        this.cities = cities;
        this.populationSize = 5000;
        this.crossoverRate = 100;
        this.maxIterations = maxIterations; //1000;
        this.mutationRate = 0.1f;
        this.tournamentSize = 40;

        if(populationSize < tournamentSize) {
            System.out.println("populationSize must be greater than the tournamentSize");
        }
    }


    ////////////////////////////////
    // create initial generation
    ////////////////////////////////
    public List<TravelPathGenome> initialPopulation() {
        List<TravelPathGenome> population = new ArrayList<>();

        for(int i = 0; i < populationSize; i++){
            population.add(new TravelPathGenome(cities));
        }

        return population;
    }


    ////////////////////////////////
    // create new population
    ////////////////////////////////
    public List<TravelPathGenome> createPopulation(List<TravelPathGenome> population){

        List<TravelPathGenome> newPopulation = new ArrayList<>();
        int newPopulationSize = 0;

        while(newPopulationSize < populationSize){

            // pick 2 random parents
            // List<TravelPathGenome> parents = new ArrayList<>();
            // int a = (int) Math.floor(Helper.random().nextInt(population.size()));
            // int b = (int) Math.floor(Helper.random().nextInt(population.size()));
            
            // parents.add(population.get(a));
            // parents.add(population.get(b));

            List<TravelPathGenome> parents = pickNRandomElements(population, 2);

            // create children from parents
            List<TravelPathGenome> children = crossover(parents);

            // mutate children
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));

            // add children to new population
            newPopulation.addAll(children);
            newPopulationSize += 2;
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

            return new TravelPathGenome(cities, genome);
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
        int breakpoint = Helper.random().nextInt(parents.size() - 1) + 1;

        // the new generation
        List<TravelPathGenome> children = new ArrayList<>();

        // copy parental genomes instead of modifying in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // creating child 1
        children.add(createChild(0, breakpoint, parent1Genome, parent2Genome, cities));
        
        // reset edited parent1 genome for the next child
        parent1Genome = parents.get(0).getGenome();

        // creating child 2
        children.add(createChild(breakpoint, parents.size(), parent2Genome, parent1Genome, cities));

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
    private TravelPathGenome createChild(int startPoint, int endPoint, List<Integer> parent1genome, List<Integer> parent2genome, ArrayList<City> cities) {

        // create new child
        for(int i = startPoint; i < endPoint; i++){
            int newValue = parent2genome.get(i);
            Collections.swap(parent1genome, parent1genome.indexOf(newValue), i);
        }

        return new TravelPathGenome(cities, parent1genome);
    }


    ////////////////////////////////
    // optimise and find result
    ////////////////////////////////
    public TravelPathGenome optimize() {

        List<TravelPathGenome> newPopulation = initialPopulation();
        TravelPathGenome globalBestGenome = newPopulation.get(0);

        for(int i=0; i < maxIterations; i++) {

            List<TravelPathGenome> selectedPopulation = selection(newPopulation);
            newPopulation = createPopulation(selectedPopulation);
            globalBestGenome = Collections.min(newPopulation);

            // breaks loop if target fitness is achieved
            // if(globalBestGenome.getFitness() < targetFitness) {
            //     break;
            // }
        }

        return globalBestGenome;
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

        for(int i=0; i < crossoverRate; i++){
            // if(selectionType == SelectionType.ROULETTE){
            //     selected.add(rouletteSelection(population));
            // }
            // else if(selectionType == SelectionType.TOURNAMENT){
            //     selected.add(tournamentSelection(population));
            // }
            //selected.add(tournamentSelection(population));
            selected.add(rouletteSelection(population));
        }

        return selected;
    }

    public TravelPathGenome rouletteSelection(List<TravelPathGenome> population){
        int totalFitness = population.stream().map(TravelPathGenome::getFitness).mapToInt(Integer::intValue).sum();
        int selectedValue = Helper.random().nextInt(totalFitness);
        float recValue = (float) 1/selectedValue;
        float currentSum = 0;
        for(TravelPathGenome genome : population){
            currentSum += (float) 1/genome.getFitness();
            if(currentSum>=recValue){
                return genome;
            }
        }
        int selectRandom = Helper.random().nextInt(populationSize);
        return population.get(selectRandom);
    }
    

    public TravelPathGenome tournamentSelection(List<TravelPathGenome> population) {
        List<TravelPathGenome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
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