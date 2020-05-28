package cezeri.search.meta_heuristic.genetic_algorithm;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Population {

    private List<Individual> individuals;

    public Population(int size, boolean createNew) {
        individuals = new ArrayList<>();
        if (createNew) {
            createNewPopulation(size);
        }
    }

    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual getFittest() {
        Individual fittest = individuals.get(0);
        for (int i = 0; i < individuals.size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    private void createNewPopulation(int size) {
        for (int i = 0; i < size; i++) {
            Individual newIndividual = new Individual();
            individuals.add(i, newIndividual);
        }
    }
}
