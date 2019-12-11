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

        // the crossover rate will be the new population size!!!
        // so this 


        // SELECT TOP ROWS FIRST
        TravelChromosome minimum = Collections.min(population);
        hashSet.add(minimum);
        selected.add(minimum);


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

            TravelChromosome chromosome = tournamentSelection(population, tournamentSize);
            if (!hashSet.contains(chromosome)) {
                hashSet.add(chromosome);
                selected.add(chromosome);
            }

            //selected.add(rouletteSelection(population));
        }

        return selected;
    }
    

    // select the min chromosome from the random elements selected
    public static TravelChromosome tournamentSelection(List<TravelChromosome> population, int tournamentSize) {
        List<TravelChromosome> selected = pickNRandomElements(population, tournamentSize);
        return Collections.min(selected);
    }


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