/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.feature.selection;

import cezeri.matrix.CMatrix;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.REPTree;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author HP-pc
 */
public class FeatureSelectionInfluence {


    public static String yaz(String msg){
        System.out.println(msg);
        return msg;
    }
    
    public static class Influence {
        double infVal;
        String attributeName;
    }

    public static Evaluation getEvaluation(Instances randData, Classifier model, int folds) {
        Evaluation eval = null;
        try {
            eval = new Evaluation(randData);
            for (int n = 0; n < folds; n++) {
                Instances train = randData.trainCV(folds, n);
                Instances test = randData.testCV(folds, n);
                // build and evaluate classifier
                Classifier clsCopy = Classifier.makeCopy(model);
                clsCopy.buildClassifier(train);
                eval.evaluateModel(clsCopy, test);
//                double[] prediction = eval.evaluateModel(clsCopy, test);
//                double[] original = getAttributeValues(test);
//                double[][] d = new double[2][prediction.length];
//                d[0] = prediction;
//                d[1] = original;
//                CMatrix f1 = new CMatrix(d);
            }

            // output evaluation
            System.out.println();
            System.out.println("=== Setup ===");
            System.out.println("Classifier: " + model.getClass().getName() + " " + Utils.joinOptions(model.getOptions()));
            System.out.println("Dataset: " + randData.relationName());
            System.out.println("Folds: " + folds);
            System.out.println();
            System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
            System.out.println(eval.toClassDetailsString("=== Detailed Accuracy By Class ==="));
            System.out.println(eval.toMatrixString("Confusion Matrix"));

            double acc = eval.correct() / eval.numInstances() * 100;
            System.out.println("correct:" + eval.correct() + "  " + acc + "%");
        } catch (Exception ex) {

            Logger.getLogger(FeatureSelectionInfluence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;
    }

    public static Influence[] getMostDiscriminativeFeature(String filePath, Classifier model) {
        Influence[] ret = null;
        try {
            Instances data = DataSource.read(filePath);
            ret = new Influence[data.numAttributes() - 1];
            data.setClassIndex(data.numAttributes() - 1);
            // other options
            int seed = 1;
            int folds = 10;
            // randomize data
            Instances randData = new Instances(data);
            Random rand = new Random(seed);
            randData.randomize(rand);
            Evaluation evalBase = getEvaluation(randData, model, folds);
            double accBase = evalBase.correct() / evalBase.numInstances() * 100;
            double nf = randData.numAttributes();

            for (int j = 0; j < nf - 1; j++) {
                ret[j] = new Influence();
                String str = randData.attribute(j).name();
                Attribute att = randData.attribute(j);
                randData.deleteAttributeAt(j);
                Evaluation evalTemp = getEvaluation(randData, model, folds);
                double accTemp = evalTemp.correct() / evalTemp.numInstances() * 100;
                double tempInfluence = accBase - accTemp;
                ret[j].attributeName = str;
                ret[j].infVal = tempInfluence;
                randData.insertAttributeAt(att, j);
            }
            sortInfluenceArray(ret);
        } catch (Exception ex) {
            Logger.getLogger(FeatureSelectionInfluence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    private static void sortInfluenceArray(Influence[] ret) {
        Influence temp;
        for (int i = 0; i < ret.length; i++) {
            for (int j = i; j < ret.length; j++) {
                if (ret[i].infVal<ret[j].infVal) {
                    temp=ret[i];
                    ret[i]=ret[j];
                    ret[j]=temp;
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\BAP1\\Google Drive\\DataSet\\Weka_Files\\dental_florisis\\kayac_dental_2.arff";
        Classifier[] models = {new MultilayerPerceptron(), new Bagging(), new REPTree()};
        Influence[] dFeature = getMostDiscriminativeFeature(filePath, models[2]);
        System.out.println("Most Disciriminative Features are");
        for (int i = 0; i < dFeature.length; i++) {
            System.out.println(dFeature[i].attributeName+"="+dFeature[i].infVal);
        }
        
    }

    private static double[] getAttributeValues(Instances test) {
        int n = test.numInstances();
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            Instance ins = test.instance(i);
            ret[i] = ins.classValue();
        }
        return ret;
    }

}
