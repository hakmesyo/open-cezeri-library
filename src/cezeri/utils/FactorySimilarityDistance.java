/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

/**
 *
 * @author musa-atas
 */
public class FactorySimilarityDistance {

    /**
     * Musa ATAŞ Various similarity/dissimilarity measures based on manhattan
     * euclidean chebyshev canberra sorensen angular_seperation
     * correlation_coefficient
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double getEuclideanDistance(double[] d1, double[] d2) {
        double dist = 0;
        for (int i = 0; i < d1.length; i++) {
            dist += (d1[i] - d2[i]) * (d1[i] - d2[i]);
        }
        return Math.sqrt(dist);
    }

    public static double getManhattanDistance(double[] d1, double[] d2) {
        double dist = 0;
        for (int i = 0; i < d1.length; i++) {
            dist += Math.abs(d1[i] - d2[i]);
        }
        return dist;
    }

    public static double getChebyshevDistance(double[] d1, double[] d2) {
        double dist = 0;
        for (int i = 0; i < d1.length; i++) {
            if (Math.abs(d1[i] - d2[i]) > dist) {
                dist = Math.abs(d1[i] - d2[i]);
            }
        }
        return dist;
    }

    public static double getCanberraDistance(double[] d1, double[] d2) {
        double dist = 0;
        for (int i = 0; i < d1.length; i++) {
            if (Math.abs(d1[i] - d2[i]) == 0 || (d1[i] + d2[i]) == 0) {
                continue;
            }
            dist += Math.abs(d1[i] - d2[i]) / (d1[i] + d2[i]);
        }
        return dist;
    }

    public static double getSorensenDistance(double[] d1, double[] d2) {
        double dist = 0;
        double absdif = 0;
        double total = 0;
        for (int i = 0; i < d1.length; i++) {
            absdif += Math.abs(d1[i] - d2[i]);
            total += d1[i] + d2[i];
        }
        dist = absdif / total;
        return dist;
    }

    public static double getAngularDistance(double[] d1, double[] d2) {
        double dist = 0;
        double pay = 0;
        double payda1 = 0;
        double payda2 = 0;
        for (int i = 0; i < d1.length; i++) {
            pay += d1[i] * d2[i];
            payda1 += d1[i] * d1[i];
            payda2 += d2[i] * d2[i];
        }
        dist = pay / Math.sqrt(payda1 * payda2);
        return Math.abs(dist);
    }

    public static double getCorrelationCoefficientDistance(double[] d1, double[] d2) {
        double dist = 0;
        double pay = 0;
        double payda1 = 0;
        double m1 = getMean(d1);
        double payda2 = 0;
        double m2 = getMean(d2);
        for (int i = 0; i < d1.length; i++) {
            pay += (d1[i] - m1) * (d2[i] - m2);
            payda1 += (d1[i] - m1) * (d1[i] - m1);
            payda2 += (d2[i] - m2) * (d2[i] - m2);
        }
        dist = pay / Math.sqrt(payda1 * payda2);
        return Math.abs(dist);
    }

    public static double getMean(double[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i];
        }
        return t / d.length;
    }
}
