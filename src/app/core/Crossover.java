package app.core;
import app.utils.Helper;
import java.util.Arrays;

public class Crossover {

    // One point crossover between two parents
    public static Integer[][] onePointCross(Integer[] parent1Chromosome, Integer[] parent2Chromosome) {

        // copy parental genomes instead of modifying in case they were
        // chosen to participate in crossover multiple times
        Integer[] child1Chromosome = Helper.clone(parent1Chromosome);
        Integer[] child2Chromosome = Helper.clone(parent2Chromosome);

        // find midpoint which should always skip the first gene
        int parentLength = parent1Chromosome.length;
        int breakpoint = Helper.random().nextInt(parentLength - 1) + 1;

        doCrossover(child1Chromosome, parent2Chromosome, 0, breakpoint);
        doCrossover(child2Chromosome, parent1Chromosome, breakpoint, parentLength);

        Integer[][] children = new Integer[][] { child1Chromosome, child2Chromosome };
        return children;
    }

    // One point crossover between two parents
    public static Integer[][] twoPointCross(Integer[] parent1Chromosome, Integer[] parent2Chromosome) {

        // copy parental genomes instead of modifying in case they were
        // chosen to participate in crossover multiple times
        Integer[] child1Chromosome = Helper.clone(parent1Chromosome);
        Integer[] child2Chromosome = Helper.clone(parent2Chromosome);

        // find midpoint which should always skip the first gene
        int parentLength = parent1Chromosome.length - 1;
        int firstPoint = Helper.random().nextInt(parentLength);
        int secondPoint = Helper.random().nextInt(parentLength - firstPoint) + firstPoint;

        doCrossover(child1Chromosome, parent2Chromosome, 0, firstPoint);
        doCrossover(child1Chromosome, parent2Chromosome, firstPoint, secondPoint);
        doCrossover(child2Chromosome, parent1Chromosome, secondPoint, parentLength);

        // fix to close the circuit
        child1Chromosome[parent1Chromosome.length - 1] = child1Chromosome[0];
        child2Chromosome[parent1Chromosome.length - 1] = child2Chromosome[0];
        
        Integer[][] children = new Integer[][] { child1Chromosome, child2Chromosome };
        return children;
    }
    

    // // Create crossover child with one endpoint
    // public TravelChromosome createChildChromosome(int startPoint, int endPoint, Integer[] childChromosome, Integer[] parentChromosome) {

    //     for(int i = startPoint; i < endPoint; i++){
    //         int parentGene = parentChromosome[i];
    //         int childIndexOf = Helper.indexOf(childChromosome, parentGene);
    //         Helper.swap(childChromosome, childIndexOf, i);
    //     }

    //     return new TravelChromosome(this.cities.points, childChromosome);
    // }


    private static void doCrossover(Integer[] childChromosome, Integer[] parentChromosome, int startPoint, int endPoint) {

        for(int i = startPoint; i < endPoint; i++){
            int parentGene = parentChromosome[i];
            int childIndexOf = Helper.indexOf(childChromosome, parentGene);
            Helper.swap(childChromosome, childIndexOf, i);
        }
    }

    // Crossover class testing
    public static void main(String[] args) {

        Integer[] parent1Chromosome = new Integer[] { 1, 8, 2, 7, 3, 6, 4, 5, 0, 1};
        Integer[] parent2Chromosome = new Integer[] { 2, 4, 6, 8, 0, 1, 3, 5, 7, 2};

        Integer[][] children = twoPointCross(parent1Chromosome, parent2Chromosome);
        for(Integer[] child : children) {
            System.out.println(Arrays.toString(child));
        }
    }
}