package app.core;
import app.models.TravelChromosome;
import app.utils.Helper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Selection {
    
    private Selection() {}

    public static List<TravelChromosome> select(List<TravelChromosome> population, int crossoverRate, int tournamentSize) {

        List<TravelChromosome> selected = new ArrayList<>();
        HashSet<TravelChromosome> hashSet = new HashSet<>();

        // add top row/s first
        TravelChromosome minimum = Collections.min(population);
        hashSet.add(minimum);
        selected.add(minimum);

        // use tournament or roulette crossover
        while(selected.size() < crossoverRate) {

            //TravelChromosome chromosome = rouletteSelection(population, tournamentSize);
            TravelChromosome chromosome = tournamentSelection(population, tournamentSize);
            if (!hashSet.contains(chromosome)) {
                hashSet.add(chromosome);
                selected.add(chromosome);
            }
        }

        return selected;
    }
    

    ////////////////////////////////
    // select min chromosome from
    // the random elements selected
    ////////////////////////////////
    public static TravelChromosome tournamentSelection(List<TravelChromosome> population, int tournamentSize) {
        List<TravelChromosome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }
    

    ////////////////////////////////
    // select random elements
    ////////////////////////////////
    public static TravelChromosome rouletteSelection(List<TravelChromosome> population, int populationSize){
        int totalFitness = population.stream().map(TravelChromosome::getFitness).mapToInt(Integer::intValue).sum();
        int selectedValue = Helper.random().nextInt(totalFitness);
        float recValue = (float) 1/selectedValue;
        float currentSum = 0;
        for(TravelChromosome genome : population){
            currentSum += (float) 1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;
            }
        }
        int selectRandom = Helper.random().nextInt(populationSize);
        return population.get(selectRandom);
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

}