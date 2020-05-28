package cezeri.search.meta_heuristic.simulated_annealing;

import cezeri.gui.FrameCanvas;
import cezeri.matrix.CPoint;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulatedAnnealing {

    private Travel travel = null;
    private int nCity = 0;
    private CPoint[] cityPos;
    private Random rnd = new Random(12);
    private int ncity = 50;

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    SimulatedAnnealing sa = new SimulatedAnnealing();
//                    double path_length = sa.simulateAnnealing(10000000, sa.ncity * 100000000, 0.99999999, sa.ncity, true, 12);
//                    System.out.println("path_length = " + path_length);
//                }
//            }).start();

            SimulatedAnnealing sa = new SimulatedAnnealing();
            double path_length = sa.simulateAnnealing(100, sa.ncity * 100000000, 0.99999999, sa.ncity, true, 12);
            System.out.println("path_length = " + path_length);

        }
    }

    public double simulateAnnealing(double startingTemperature, int numberOfIterations, double coolingRate, int _nCity, boolean isSeeded, int seed) {
        rnd = new Random(seed);
        nCity = _nCity;
        travel = new Travel(nCity, isSeeded, rnd);
        cityPos = getCityPosition(travel);

//        FrameCanvas canvas = new FrameCanvas();
//        canvas.setVisible(true);
//        updateCityMap(cityPos, true, null, false, canvas);
        FrameCanvas canvas2 = new FrameCanvas();
        canvas2.setVisible(true);
        updateCityMap(cityPos, true, null, false, canvas2);

//        System.out.println("Starting SA with temperature: " + startingTemperature + ", # of iterations: " + numberOfIterations + " and cooling rate: " + coolingRate);
        double t = startingTemperature;
        travel.generateInitialTravel(_nCity, true, rnd);
        double bestDistance = travel.getDistance();
//        System.out.println("Initial distance of travel: " + bestDistance);
        Travel bestSolution = travel;
        Travel finalSolution = null;
        Travel currentSolution = bestSolution;

        for (int i = 0; i < numberOfIterations; i++) {
//            updateCityMap(cityPos, true, getEdgePos(currentSolution), true,canvas2);
//            try {
//                Thread.sleep(3);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(SimulatedAnnealing.class.getName()).log(Level.SEVERE, null, ex);
//            }
            if (t > 0.1) {
                currentSolution.swapCities();
                double currentDistance = currentSolution.getDistance();
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                    finalSolution = currentSolution.copy();
                } else if (Math.exp((bestDistance - currentDistance) / t) < Math.random()) {
                    currentSolution.revertSwap();
                }
                t *= coolingRate;
            } else {
                continue;
            }
            if (i % 1000000 == 0) {
                System.out.println("Iteration #" + i + " temp:" + t + " best distance:" + bestDistance);
            }
        }
        updateCityMap(cityPos, true, getEdgePos(finalSolution), true, canvas2);
        return bestDistance;
    }

    private CPoint[] getCityPosition(Travel travel) {
        CPoint[] ret = new CPoint[nCity];
        for (int i = 0; i < nCity; i++) {
            ret[i] = new CPoint(travel.getCity(i).getY(), travel.getCity(i).getX());
        }
        return ret;
    }

    private static void updateCityMap(CPoint[] cp, boolean visNode, CPoint[] path, boolean visEdge, FrameCanvas canvas) {
        canvas.getCanvasPanel().updatePath(cp, visNode, path, visEdge);
    }

    private static CPoint[] getEdgePos(Travel travel) {
        int n = travel.getTravel().size();
        CPoint[] ret = new CPoint[n];
        for (int i = 0; i < n; i++) {
            ret[i] = new CPoint();
            ret[i].column = travel.getTravel().get(i).getX();
            ret[i].row = travel.getTravel().get(i).getY();
        }
        return ret;
    }

}
