package app.models;
import app.utils.Helper;
import java.util.Arrays;
import java.util.ArrayList;

// notes:
// starting city will always be the first one
// While the starting city doesn't change the solution of the problem,
// it's handy to just pick one so you could rely on it being the same
// across genomes

public class TravelChromosome implements Comparable<TravelChromosome> {
    
    private Integer[] orderChromosome; // cities' list in order in which they should be visited
    private double totalDistance;
    private int fitness;


    // Generate random travel path
    public TravelChromosome(int numberOfCities) {
        this.orderChromosome = shuffleCityOrder(numberOfCities);
    }

    // Generate random travel path
    public TravelChromosome(ArrayList<City> cityPoints) {
        this.orderChromosome = shuffleCityOrder(cityPoints.size());
        //this.resetStartingCity();
        this.calculateFitness(cityPoints);
    }

    // Generate travel path with a user-defined chromosome
    public TravelChromosome(ArrayList<City> cityPoints, Integer[] chromosome) {
        this.orderChromosome = chromosome;
        //this.resetStartingCity();
        this.calculateFitness(cityPoints);
    }

    ////////////////
    // PRIVATE
    ////////////////

    // PRV: Generate random chromosome using number of cities and shuffle
    public static Integer[] shuffleCityOrder(int numberOfCities) {
        Integer[] chromosome = new Integer[numberOfCities];
        return shuffleCityOrder(chromosome);
    }

    // PRV: Shuffle chromosom
    public static Integer[] shuffleCityOrder(Integer[] chromosome) {
        
        if(chromosome.length != 0 && chromosome[0] == null) {
    
            for (int i = 0; i < chromosome.length; i++) {
                chromosome[i] = i;
            }
        }

        for(int i = 0; i < chromosome.length; i++) {
            int j = Helper.random().nextInt(chromosome.length);
            if(i != j) {
                Helper.swap(chromosome, i, j);
            }
        }

        return chromosome;
    }

    // PRV: Reset starting city to initial city at element 0
    private void resetStartingCity() {

        if(orderChromosome[0] != 0) {
            Helper.swap(orderChromosome, Arrays.asList(orderChromosome).indexOf(0), 0);
        }
    }


    // PRV: Calculate distance between two points
    private void calculateFitness(ArrayList<City> cities) {
        int numberOfCities = cities.size();
        double totalDistance = 0.0;

        for(int i = 0; i < numberOfCities - 1; i++) {

            // get the cities according to the genome sorting
            City thisCity = cities.get(orderChromosome[i]);
            City nextCity = cities.get(orderChromosome[i + 1]);
            totalDistance += thisCity.calculateDistance(nextCity);
        }

        // returns total distance for whole genome
        this.totalDistance = totalDistance;
        this.fitness = (int) this.totalDistance;
    }

    ////////////////
    // PROPERTIES
    ////////////////

    public Integer[] getOrderChromosome() {
        return orderChromosome;
    }

    public int getStartingCity() {
        return orderChromosome[0];
    }

    public int getFitness() {
        return this.fitness;
    }

    public double getDistance() {
        return this.totalDistance;
    }

    ////////////////
    // OVERRIDES
    ////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");

        // todo: is gene int the same as the city id?
        for (int gene : orderChromosome) {
            sb.append(" ");
            sb.append(gene);
        }

        // sb.append(" // Distance: ");
        // sb.append(this.totalDistance);
        return sb.toString();
    }

    @Override
    public int compareTo(TravelChromosome chromosome) {

        if(this.fitness > chromosome.getFitness()) {
            return 1;
        }
        else if(this.fitness < chromosome.getFitness()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}