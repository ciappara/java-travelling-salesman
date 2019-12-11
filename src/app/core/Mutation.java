package app.core;
import app.models.City;
import app.models.TravelChromosome;
import app.utils.Helper;
import java.util.Arrays;
import java.util.ArrayList;

public class Mutation {

    private Mutation() {}

    // Mutate selected travel chromosome
    public static TravelChromosome mutate(ArrayList<City> cityPoints, TravelChromosome travelpath, float mutationRate) {

        return new TravelChromosome(cityPoints, mutate(travelpath.getOrderChromosome(), mutationRate));
    }

    // Mutate selected chromosome
    public static Integer[] mutate(Integer[] orderChromosome, float mutationRate) {

        int mutations = orderChromosome.length / 9;

        for(int i = 0; i < mutations; i++) {

            float mutate = Helper.random().nextFloat();
            if(mutate < mutationRate) {
    
                int from = Helper.random().nextInt(orderChromosome.length);
                int to   = Helper.random().nextInt(orderChromosome.length);
                Helper.swap(orderChromosome, from, to);
            }
        }

        // return same travel path if not mutated
        return orderChromosome;
    }

    // Mutation class testing
    public static void main(String[] args) {

        Integer[] parent1Chromosome = new Integer[] { 1, 8, 2, 7, 3, 6, 4, 5, 0};
        Integer[] parent2Chromosome = new Integer[] { 2, 4, 6, 8, 0, 1, 3, 5, 7};

        Integer[] child1 = mutate(parent1Chromosome, 0.4f);
        Integer[] child2 = mutate(parent2Chromosome, 0.4f);
        
        System.out.println(Arrays.toString(child1));
        System.out.println(Arrays.toString(child2));
        System.out.println(20 / 9);
        System.out.println(19 / 10);
    }
}