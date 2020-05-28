package cezeri.search.meta_heuristic.ant_colony_optimization;

/**
 * Global parameters used to adjust the ACO
 */
public class Parameters {

    /**
     * Pheromone evaporation rate
     */
    public static double rho = 0.5;

    /**
     * Pheromone importance
     */
    public static double alpha = 1.0;

    /**
     * Heuristic importance
     */
    public static double beta = 2.0;

    /**
     * Size of ant population
     */
    public static int antPopSize = 100;

    /**
     * Size of nearest neighbor list for each vertex
     */
    public static int NNSize = 20;

    /**
     * Number of iterations to find a good solution
     */
    public static int iterationsMax = 50000;

}
