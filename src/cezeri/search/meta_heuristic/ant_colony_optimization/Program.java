package cezeri.search.meta_heuristic.ant_colony_optimization;

import cezeri.factory.FactoryMatrix;
import cezeri.factory.FactoryUtils;
import java.util.Random;

/**
 * ACO - Ant Colony Optimization Meta-heuristic
 *
 * Reference book: Ant Colony Optimization. Authors: Marco Dorigo and Thomas
 * Stützle Links: -> https://mitpress.mit.edu/books/ant-colony-optimization ->
 * http://www.aco-metaheuristic.org/
 *
 * This algorithm present the implementation of ACO for TSP problems.
 */
public class Program {

    private int nCity = 100;
    private Random rnd = new Random(12);
    private int nAnts = 5000;

    public static void main(String[] args) throws Exception {

//        String tspPath = (new File(".")).getCanonicalPath();
//        tspPath = Paths.get(tspPath, "tsp").toAbsolutePath().toString();
//        String tspFiles[] = {"eil51.tsp", "lin318.tsp", "att532.tsp", "pcb1173.tsp", "pr2392.tsp"};
        for (int i = 0; i < 1; i++) {
            System.out.println("deneme:"+(i+1));
            Program app = new Program();
            // Test more simulations
//        for(String tspFile : tspFiles) {
//            System.out.println("\nProblem: " + tspFile);
//            app.startApplication(tspPath, tspFile);
//        }
//        app.startApplication(tspPath, "eil51.tsp");
            app.startApplicationNative();
        }
    }

    // Main part of the algorithm
    public void startApplication(String path, String file) {

        // Create a TSP instance from file with .tsp extension
        Environment environment = new Environment(TspReader.getDistances(path, file));
        Statistics statistics = new Statistics(file, environment, TspReader.getCoordinates(path, file));
        System.out.println(FactoryMatrix.toString(TspReader.getCoordinates(path, file)));

        // Startup part
        environment.generateNearestNeighborList();
        environment.generateAntPopulation();
        environment.generateEnvironment();

        // Repeat the ants behavior by n times
        int n = 0;
        while (n < Parameters.iterationsMax) {
            environment.constructSolutions();
            environment.updatePheromone();
            statistics.calculateStatistics(n);
            n++;
        }
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
        }
        statistics.close();
        System.out.println("Finished");
    }

    // Main part of the algorithm
    public void startApplicationNative() {

        // Create a TSP instance from file with .tsp extension
        double[][] cityLoc = populateGraph(nCity, rnd);
        double[][] graph = buildAdjacencyMatrix(cityLoc);
        Environment environment = new Environment(graph);
//        System.out.println(FactoryMatrix.toString(graph));

        Statistics statistics = new Statistics(environment, cityLoc);
//
//        // Startup part
        environment.generateNearestNeighborList();
        environment.generateAntPopulation();
        environment.generateEnvironment();

        // Repeat the ants behavior by n times
        int n = 0;
        while (n < Parameters.iterationsMax) {
            environment.constructSolutions();
            environment.updatePheromone();
            statistics.calculateStatistics(n);
            n++;
        }
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }
        System.out.println("best cost:" + statistics.bestSoFar);
        //statistics.close();
        System.out.println("Finished");
    }

    private double[][] populateGraph(int nCity, Random rnd) {
        double[][] ret = new double[nCity][2];
        for (int i = 0; i < nCity; i++) {
            ret[i][0] = 50 + rnd.nextInt(400);
            ret[i][1] = 50 + rnd.nextInt(400);
        }
        return ret;
    }

    private double[][] buildAdjacencyMatrix(double[][] cityLoc) {
        int n = cityLoc.length;
        double[][] ret = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = getDist(cityLoc, i, j);
            }
        }
        return ret;
    }

    private double getDist(double[][] d, int i, int j) {
        double ret = FactoryUtils.formatDouble(Math.sqrt(Math.pow((d[i][0] - d[j][0]), 2) + Math.pow((d[i][1] - d[j][1]), 2)), 1);
        return ret;
    }

}
