package app;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import app.models.*;

// notes:
// starting city will always be the first one
// While the starting city doesn't change the solution of the problem,
// it's handy to just pick one so you could rely on it being the same
// across genomes

public class TravelPathGenome implements Comparable<TravelPathGenome> {
    
    public List<Integer> genome;   // cities' list in order in which they should be visited
    double totalDistance;
    int fitness;

    // Generates a random travel path
    public TravelPathGenome(ArrayList<City> cities) {
        this.genome = randomSalesman(cities.size());
        this.calculateFitness(cities);
    }

    // Generates a travel path with a user-defined genome
    public TravelPathGenome(ArrayList<City> cities, List<Integer> permutationOfCities) {

        // reset starting city
        if(permutationOfCities.indexOf(0) != 0) {
            Collections.swap(permutationOfCities, permutationOfCities.indexOf(0), 0);
        }

        this.genome = permutationOfCities;
        this.calculateFitness(cities);
    }

    // Generates a random genome
    // Genomes are permutations of the list of cities, except the starting city
    // so we add them all to a list and shuffle
    private List<Integer> randomSalesman(int numberOfCities) {
        
        List<Integer> result = new ArrayList<Integer>();

        for (int i = 0; i < numberOfCities; i++) {
            result.add(i);
        }

        Collections.shuffle(result);

        // reset starting city
        if(result.indexOf(0) != 0) {
            Collections.swap(result, result.indexOf(0), 0);
        }
        
        return result;
    }


    // calculates distance between two points
    public void calculateFitness(ArrayList<City> cities) {
        int numberOfCities = cities.size();
        double totalDistance = 0.0;

        for(int i = 0; i < numberOfCities - 1; i++) {

            // get the cities according to the genome sorting
            City thisCity = cities.get(genome.get(i));
            City nextCity = cities.get(genome.get(i + 1));
            totalDistance += thisCity.calculateDistance(nextCity);

        }

        // returns total distance for whole genome
        this.totalDistance = totalDistance;
        this.fitness = (int) this.totalDistance;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getStartingCity() {
        return genome.get(0);
    }

    public int getFitness() {
        return this.fitness;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");

        // todo: is gene int the same as the city id?
        for (int gene : genome) {
            sb.append(" ");
            sb.append(gene);
        }

        sb.append(" // Distance: ");
        sb.append(this.totalDistance);
        return sb.toString();
    }

    @Override
    public int compareTo(TravelPathGenome genome) {

        if(this.fitness > genome.getFitness()) {
            return 1;
        }
        else if(this.fitness < genome.getFitness()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}