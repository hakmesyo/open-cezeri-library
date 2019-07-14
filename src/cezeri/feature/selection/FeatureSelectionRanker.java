/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.feature.selection;

import cezeri.evaluater.FactoryEvaluation;
import cezeri.matrix.CMatrix;
import cezeri.utils.CustomComparatorForFeatureRank;
import cezeri.utils.FactoryCombination;
import cezeri.utils.FactoryInstance;
import cezeri.utils.FactoryStatistic;
import cezeri.utils.FactoryUtils;
import cezeri.types.TCorelation;
import cezeri.types.TFeatureRank;
import cezeri.types.TLearningType;
//import cezeri.types.TMachineLearning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 *
 * @author BAP1
 */
public class FeatureSelectionRanker {


    public static String yaz(String msg) {
        System.out.println(msg);
        return msg;
    }

    /**
     * You should use this method only for the Classification problems Fisher is
     * not suitable for Regression. For Regression problems you can use CRCF
     * method.
     *
     * @param data
     * @param type
     * @return
     */
    public static TFeatureRank[] fisherDistance(Instances data, int type) {
        if (type == TLearningType.REGRESSION) {
            return null;
        }
        TFeatureRank[] ret = new TFeatureRank[data.numAttributes() - 1];
        String[] attributeNames = FactoryInstance.getAttributeList(data);
//        FactoryInstance.getMatrix(data).plot();
        Instances[] ins = FactoryInstance.getSpecificInstancesBasedOnClassValue(data, FactoryInstance.getDefaultClasses(data));
        if (ins.length < 2) {
            return null;
        }
        double[][] cl_1 = CMatrix.getInstance(FactoryInstance.getData(ins[0])).transpose().toDoubleArray2D();
        double[][] cl_2 = CMatrix.getInstance(FactoryInstance.getData(ins[1])).transpose().toDoubleArray2D();
//        FactoryMatrix.transpose(FactoryInstance.getData(ins[1]));
        double[] fisher = new double[cl_1.length];
        for (int i = 0; i < cl_1.length - 1; i++) {
            double mean_1 = FactoryUtils.getMean(cl_1[i]);
            double std_1 = FactoryStatistic.getStandardDeviation(cl_1[i]);
            double mean_2 = FactoryUtils.getMean(cl_2[i]);
            double std_2 = FactoryStatistic.getStandardDeviation(cl_2[i]);
            if (Math.pow(std_1, 2) + Math.pow(std_2, 2) == 0.0) {
                fisher[i] = 0.0;
            } else {
//                double f = Math.abs(mean_1 - mean_2) / (Math.pow(std_1, 2) + Math.pow(std_2, 2));
                double f = Math.pow((mean_1 - mean_2), 2) / (Math.pow(std_1, 2) + Math.pow(std_2, 2));
                fisher[i] = FactoryUtils.formatDouble(f);
            }
            TFeatureRank obj = new TFeatureRank();
            obj.featureName = attributeNames[i];
            obj.index = "" + i;
            obj.value = fisher[i];
            ret[i] = obj;
            //println(i + ".fisher distance:" + fisher[i]);
        }
        ArrayList<TFeatureRank> lst = toArrayList(ret);
        Collections.sort(lst, new CustomComparatorForFeatureRank());
        ret = toArray(lst);
//        int[] fisherIndex = FactoryUtils.sortArrayAndReturnIndex(fisher, "desc");
        return ret;
    }

    /**
     * You should use this method only for the Classification problems Fisher is
     * not suitable for Regression. For Regression problems you can use CRCF
     * method.
     *
     * @param data
     * @param type : like TCorelation.ARE
     * @return
     */
    public static TFeatureRank[] rankFeatureRegression(Instances data, int type) {
        TFeatureRank[] cr = correlation(data, type);
        for (int i = 0; i < cr.length; i++) {
            for (int j = i; j < cr.length; j++) {
                if (cr[i].value<cr[j].value) {
                    TFeatureRank tmp=new TFeatureRank();
                    tmp.featureName=cr[i].featureName;
                    tmp.index=cr[i].index;
                    tmp.value=cr[i].value;
                    cr[i].featureName=cr[j].featureName;
                    cr[i].index=cr[j].index;
                    cr[i].value=cr[j].value;                    
                    cr[j].featureName=tmp.featureName;
                    cr[j].index=tmp.index;
                    cr[j].value=tmp.value;
                }
            }
        }
        return cr;
    }

    /**
     * if full exhaustive search is not feasible due to computational cost,
     * shrink search space by reducing the number of features that you want to
     * explore
     *
     * @param nSubset desired subset number i.e.: if you have 15 features you
     * may want to 9 feature subset result
     * @param data train or test data
     * @param model classifier you used
     * @param nFolds during learning what will be the cross validation folds
     * @param show_text print the output
     * @param show_plot plot the output
     * @return
     */
    public static TFeatureRank[] wrapperExhaustiveSearchLimited(int nSubset, Instances data, Classifier model, int nFolds, boolean show_text, boolean show_plot) {
        if (nSubset > data.numAttributes() - 1) {
            System.out.println("subset should be less than attribute number");
            return null;
        }

        String[] attributeNames = FactoryInstance.getAttributeListExceptClassAttribute(data);
        String[] lstComb = FactoryCombination.getCombination(attributeNames, nSubset);
        FactoryCombination.toString(lstComb);
        TFeatureRank[] ret = computeCombinationPairs(lstComb, data, model, nFolds, show_text, show_plot);
        return ret;
    }

    /**
     * if number of features is less than 15 only you can make exhaustive global
     * search on the feature space
     *
     * @param data :dataset
     * @param model :classifier
     * @param nFolds :number of cross validation folds
     * @param show_text :print the output
     * @param show_plot :plot the output
     * @return
     */
    public static TFeatureRank[] wrapperExhaustiveSearch(Instances data, Classifier model, int nFolds, boolean show_text, boolean show_plot) {
        if (data.numAttributes() > 15) {
            System.out.println("for exhaustive search num of attributes greater than 13 is not feasible comp cost is too high to compute");
            return null;
        }
        String[] attributeNames = FactoryInstance.getAttributeListExceptClassAttribute(data);
        String[] lstComb = FactoryCombination.getAllCombinations(attributeNames);
        TFeatureRank[] ret = computeCombinationPairs(lstComb, data, model, nFolds, show_text, show_plot);
        return ret;
    }

    /**
     * Previous version of Simulated Annealing
     *
     *
     * @param subset :works only subsets
     * @param data :instances
     * @param model :classifier
     * @param show_text :print the output
     * @param show_plot :plot the output
     * @return
     */
    private static TFeatureRank[] wrapperSimulatedAnnealingV1(int subset, Instances data, Classifier model, int folds, boolean show_text, boolean show_plot) {
        String[] attList = FactoryInstance.getAttributeListExceptClassAttribute(data);
        Random rnd = new Random();
        double globalError = 1.0;
        double ret = 0;
        double probability;
        double alpha = 0.9999;
        double temperature = 400000.0;
        double epsilon = 0.001;
        double delta;
        ArrayList<String> combinationList = new ArrayList<String>();
        ArrayList<TFeatureRank> rankList = new ArrayList<TFeatureRank>();
        String[] lstComb = FactoryCombination.getCombination(attList, subset);
        String currCombination = FactoryUtils.getRandomSubset(lstComb);
        String bestCombination = currCombination;
        int q = 0;
        boolean isNeighbor = false;
        while (temperature > epsilon) {
            q++;
            if (isNeighbor) {
                currCombination = FactoryUtils.getNeighborSubset(currCombination, lstComb);
            } else {
                currCombination = FactoryUtils.getRandomSubset(lstComb);
            }
            if (combinationList.contains(currCombination)) {
//                    System.out.println("temperature:" + temperature + " during sim annealing redundant subset was found and ignored " + currCombination);
                temperature *= alpha;
                continue;
            } else {
                combinationList.add(currCombination);
            }
            ret = computeCombinationFeature(currCombination, data, folds, model, show_text, show_plot);
            delta = ret - globalError;
            if (delta < 0) {
                globalError = ret;
                bestCombination = currCombination;
            } else {
                probability = rnd.nextDouble();
                if (probability < Math.exp(-delta / temperature)) {
                    globalError = ret;
                    bestCombination = currCombination;
                }
            }
            temperature *= alpha;
            System.out.println(q + ".new subset:" + currCombination + " temperature:" + temperature + " accuracy:" + ret);
            TFeatureRank obj = new TFeatureRank();
            obj.featureName = currCombination;
            obj.index = q + "";
            obj.value = ret;
            rankList.add(obj);

        }
        Collections.sort(rankList, new CustomComparatorForFeatureRank());
        TFeatureRank[] fr = toArray(rankList);
        return fr;
    }

    public static TFeatureRank[] wrapperSimulatedAnnealing(int subset, Instances data,  Classifier model,int folds, boolean show_text, boolean show_plot) {
        String[] attList = FactoryInstance.getAttributeListExceptClassAttribute(data);
        ArrayList<String> combinationList = new ArrayList<>();
        ArrayList<TFeatureRank> rankList = new ArrayList<>();
        String[] lstComb = FactoryCombination.getCombination(attList, subset);
        System.out.println("Size of subsets:"+lstComb.length);
        int size=lstComb.length;
        String currentCombination = FactoryUtils.getRandomSubset(lstComb);
        String newCombination = "";
        String bestCombination = currentCombination;

        // Set initial temp
        double temp = 1000000;

        // Cooling rate
        double coolingRate = 0.009;

        // Set as current and best
        double bestEnergy = computeCombinationFeature(currentCombination, data,folds, model, show_text, show_plot);
        double currentEnergy = bestEnergy;
        // Initialize intial solution
        System.out.println("Initial solution accuracy: " + bestEnergy);

        // Loop until system has cooled
        int k = 0;
        int m = 0;
        while (temp > 1) {
            m++;
            if (m>size) {
                temp=0;
            }
            // get new random neighbour subset
            newCombination = FactoryUtils.getRandomSubset(lstComb);
            if (combinationList.contains(newCombination)) {
//                temp *= 1 - coolingRate;
                continue;
            } else {
                combinationList.add(newCombination);
            }

            // Get energy of solutions
            double neighbourEnergy = computeCombinationFeature(newCombination, data, folds, model, show_text, show_plot);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentCombination = newCombination;
                currentEnergy = neighbourEnergy;
            }

            // Keep track of the best solution found
            if (currentEnergy > bestEnergy) {
                bestEnergy = currentEnergy;
                bestCombination = currentCombination;
                TFeatureRank obj = new TFeatureRank();
                obj.featureName = bestCombination;
                obj.index = "" + (k++);
                obj.value = bestEnergy;
                rankList.add(obj);
                obj.toString();
            }
            System.out.println("dor:" + m + " T=" + temp+" result:"+bestEnergy);
            // Cool system
            temp *= 1 - coolingRate;
        }
        System.out.println("dor:" + m + " T=" + temp);
        System.out.println("Final solution accuracy: " + bestEnergy);
        System.out.println("Subset: " + bestCombination);
        Collections.sort(rankList, new CustomComparatorForFeatureRank());
        TFeatureRank[] fr = toArray(rankList);
        return fr;
    }

    public static ArrayList<TFeatureRank> toArrayList(TFeatureRank[] d) {
        ArrayList<TFeatureRank> ret = new ArrayList<TFeatureRank>();
        for (int i = 0; i < d.length; i++) {
            ret.add(d[i]);
        }
        return ret;
    }

    public static TFeatureRank[] toArray(ArrayList<TFeatureRank> d) {
        TFeatureRank[] ret = new TFeatureRank[d.size()];
        for (int i = 0; i < d.size(); i++) {
            ret[i] = d.get(i);
        }
        return ret;
    }

    public static String toString(String str, TFeatureRank[] d) {
        String ret = "";
        System.out.println("");
//        System.out.println(str);
        if (d == null) {
            System.out.println("NULL VALUE OR REGRESSION PROBLEM DOESNT HOLD FISHER DISCRIMINATION POWER METRIC! ");
            return "";
        }
        for (int i = 0; i < d.length; i++) {
            ret += d[i].toString() + "\n";
        }
//        System.out.println("------------------------------------------------------");
//        System.out.println("List Size:" + d.length);
        return ret;
    }

    private static TFeatureRank[] correlation(Instances data, int type) {
        TFeatureRank[] ret = new TFeatureRank[data.numAttributes() - 1];
        String[] attributeNames = FactoryInstance.getAttributeList(data);
        double[] out = data.attributeToDoubleArray(data.classIndex());
        for (int i = 0; i < data.numAttributes() - 1; i++) {
            TFeatureRank obj = new TFeatureRank();
            obj.featureName = attributeNames[i];
            obj.index = i + "";
            if (type == TCorelation.ARE) {
                obj.value = Math.abs(FactoryStatistic.ARE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.CRCF) {
                obj.value = Math.abs(FactoryStatistic.CRCF(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.IOA) {
                obj.value = Math.abs(FactoryStatistic.IOA(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.KENDALL) {
                obj.value = Math.abs(FactoryStatistic.KENDALL(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.MAE) {
                obj.value = Math.abs(FactoryStatistic.MAE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.MPE) {
                obj.value = Math.abs(FactoryStatistic.MPE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.MSE) {
                obj.value = Math.abs(FactoryStatistic.MSE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.NSEC) {
                obj.value = Math.abs(FactoryStatistic.NSEC(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.PEARSON) {
                obj.value = Math.abs(FactoryStatistic.PEARSON(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.R) {
                obj.value = Math.abs(FactoryStatistic.R(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.R2) {
                obj.value = Math.abs(FactoryStatistic.R2(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.RAE) {
                obj.value = Math.abs(FactoryStatistic.RAE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.RELATIVE_NSEC) {
                obj.value = Math.abs(FactoryStatistic.RELATIVE_NSEC(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.RMSE) {
                obj.value = Math.abs(FactoryStatistic.RMSE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.RRSE) {
                obj.value = Math.abs(FactoryStatistic.RRSE(data.attributeToDoubleArray(i), out));
            }
            if (type == TCorelation.SPEARMAN) {
                obj.value = Math.abs(FactoryStatistic.SPEARMAN(data.attributeToDoubleArray(i), out));
            }
//            if (type==FactoryCorrelation.KENDALL) {
//                obj.value=Math.abs(FactoryCorrelation.rankKendallTauBeta(data.attributeToDoubleArray(i), out));
//            }
//            if (type==FactoryCorrelation.PEARSON) {
//                obj.value=Math.abs(FactoryCorrelation.pearson(data.attributeToDoubleArray(i), out));
//            }
//            if (type==FactoryCorrelation.SPEARMAN) {
//                obj.value=Math.abs(FactoryCorrelation.spearman(data.attributeToDoubleArray(i), out));
//            }            
            ret[i] = obj;
        }
        ArrayList<TFeatureRank> lst = toArrayList(ret);
        Collections.sort(lst, new CustomComparatorForFeatureRank());
        ret = toArray(lst);
        return ret;
    }

    private static TFeatureRank[] computeCombinationPairs(String[] lstComb, Instances data, Classifier model, int nFolds, boolean show_text, boolean show_plot) {
        TFeatureRank[] ret = new TFeatureRank[lstComb.length];
        int m = lstComb.length;
        double q = m * 1.0 / 100;
        int n = 0;
        for (int i = 0; i < m; i++) {
            if (n != (int) Math.round(i / q)) {
                n = (int) Math.round(i / q);
                System.out.println("progress:" + n + "%");
            }
            TFeatureRank obj = new TFeatureRank();
            obj.featureName = lstComb[i];
            obj.index = i + "";
            Instances subsetData = FactoryInstance.getSubsetData(data, lstComb[i].split(","));
            Evaluation eval = FactoryEvaluation.performCrossValidate(model, subsetData, nFolds, show_text, show_plot);
            try {
                if (data.classAttribute().isNominal()) {
                    obj.value = eval.pctCorrect();
                } else {
                    obj.value = eval.correlationCoefficient();
                }
            } catch (Exception ex) {
                Logger.getLogger(FeatureSelectionRanker.class.getName()).log(Level.SEVERE, null, ex);
            }
            ret[i] = obj;
        }
        ArrayList<TFeatureRank> lst = toArrayList(ret);
        Collections.sort(lst, new CustomComparatorForFeatureRank());
        ret = toArray(lst);
        return ret;
    }

    private static double computeCombinationFeature(String lstComb, Instances data,int folds, Classifier model, boolean show_text, boolean show_plot) {
        TFeatureRank obj = new TFeatureRank();
        obj.featureName = lstComb;
        obj.index = "";
        Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
        Evaluation eval = FactoryEvaluation.performCrossValidate(model, subsetData, folds, show_text, show_plot);
        try {
            if (data.classAttribute().isNominal()) {
                obj.value = eval.pctCorrect();
            } else {
                obj.value = eval.correlationCoefficient();
            }
        } catch (Exception ex) {
            Logger.getLogger(FeatureSelectionRanker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.value;
    }

    // Calculate the acceptance probability
    private static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

}
