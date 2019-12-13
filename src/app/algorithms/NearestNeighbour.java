package app.algorithms;
import app.models.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearestNeighbour {

    public NearestNeighbour() {
    }

    public TravelChromosome optimize(CityPoints cities) {
        TravelChromosome bestEverChromosome = null;

        for(int i = 0; i < cities.points.size(); i++) {
            TravelChromosome chromosome = optimize(cities, i);

            if(bestEverChromosome == null || chromosome.getFitness() < bestEverChromosome.getFitness()) {
                bestEverChromosome = chromosome;
                //System.out.println("." + Arrays.toString(bestEverChromosome.getOrderChromosome()));
            }
            else {
                //System.out.println(" " + Arrays.toString(chromosome.getOrderChromosome()));
            }
        }

        return bestEverChromosome;
    }
    
    ////////////////////////////////
    // optimise and find result
    ////////////////////////////////
    public TravelChromosome optimize(CityPoints cities, int startingCity) {

        int numberOfCities = cities.points.size();
        ArrayList<Integer> chromosome = new ArrayList<>();

        // create boolean to store if town has been visited
        boolean[] isVisited = new boolean[numberOfCities];
        Arrays.fill(isVisited, false);

        // todo: maybe sort by x and y?

        // start at town number 1 and get the next closest town and continue
        isVisited[startingCity] = true;
        City current = cities.points.get(startingCity);
        chromosome.add(0, startingCity);

        for (int j = 0; j < numberOfCities - 1; j++) {

            double shortest = Double.MAX_VALUE;
            int shortestCityPosition = -1;

            // find shortest route
            for (int i = 0; i < numberOfCities; i++) {

                City totest = cities.points.get(i);
                double distance = current.calculateDistance(totest);

                if (distance < shortest && isVisited[i] == false) {
                    shortest = distance;
                    shortestCityPosition = i;
                }
            }

            // add new chromosome with shortest city position
            chromosome.add(j + 1, shortestCityPosition);

            // store the next city
            isVisited[shortestCityPosition] = true;
            current = cities.points.get(shortestCityPosition);
        }
        
        // fix to close the circuit
        chromosome.add(startingCity);
        
        Integer[] shortestChromosome = new Integer[chromosome.size()];
        for(int i = 0; i < chromosome.size(); i++) {
            shortestChromosome[i] = chromosome.get(i);
        }

        TravelChromosome nearestChromosome = new TravelChromosome(cities.points, shortestChromosome);
        return nearestChromosome;
    }


    ////////////////////////////////
    // get new population
    ////////////////////////////////
    public List<TravelChromosome> getNNPopulation(CityPoints cities) {
        
        List<TravelChromosome> nnPopulaton = new ArrayList<>();
        for(int i = 0; i < cities.points.size(); i++) {
            TravelChromosome chromosome = optimize(cities, i);
            nnPopulaton.add(chromosome);
        }
        return nnPopulaton;
    }
}