package cezeri.search.meta_heuristic.genetic_algorithm;

//import simulated_annealing.*;
//import ant_colony_optimization.AntColonyOptimization;
//import binary.SimpleGeneticAlgorithm;
import java.util.Scanner;

public class RunAlgorithm {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        int nCity = 50;
        int seed = 12;
        SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm();
        ga.runAlgorithm(nCity, "1011000100000100010000100000100111001000000100000100000000001111");
    }

}
