/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.evaluater;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryInstance;
import cezeri.factory.FactoryUtils;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author BAP1
 */
public class FactoryEvaluation {

    public static double[] simulated = new double[0];
    public static double[] observed = new double[0];

    public static Evaluation performCrossValidate(TFigureAttribute attr, Classifier model, Instances datax, int folds, boolean show_text, boolean show_plot) {
        return performCrossValidate(model, datax, folds, show_text, show_plot, attr);
    }

    public static Evaluation performCrossValidate(Classifier model, Instances datax, int folds, boolean show_text, boolean show_plot) {
        TFigureAttribute attr = new TFigureAttribute();
        return performCrossValidate(model, datax, folds, show_text, show_plot, attr);
    }

    public static Evaluation performCrossValidate(Classifier model, Instances datax, int folds, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        Random rand = new Random(1);
        Instances randData = new Instances(datax);
        randData.randomize(rand);
        if (randData.classAttribute().isNominal()) {
            randData.stratify(folds);
        }
        Evaluation eval = null;
        try {
            // perform cross-validation
            eval = new Evaluation(randData);
//            double[] simulated = new double[0];
//            double[] observed = new double[0];
//            double[] sim = new double[0];
//            double[] obs = new double[0];
            for (int n = 0; n < folds; n++) {
                Instances train = randData.trainCV(folds, n, rand);
                Instances validation = randData.testCV(folds, n);
                // build and evaluate classifier
                Classifier clsCopy = Classifier.makeCopy(model);
                clsCopy.buildClassifier(train);

//                sim = eval.evaluateModel(clsCopy, validation);
//                obs = validation.attributeToDoubleArray(validation.classIndex());
//                if (show_plot) {
//                    double[][] d = new double[2][sim.length];
//                    d[0] = obs;
//                    d[1] = sim;
//                    CMatrix f1 = CMatrix.getInstance(d);
//                    f1.transpose().plot(attr);
//                }
//                if (show_text) {
//                    // output evaluation
//                    System.out.println();
//                    System.out.println("=== Setup for each Cross Validation fold===");
//                    System.out.println("Classifier: " + model.getClass().getName() + " " + Utils.joinOptions(model.getOptions()));
//                    System.out.println("Dataset: " + randData.relationName());
//                    System.out.println("Folds: " + folds);
//                    System.out.println("Seed: " + 1);
//                    System.out.println();
//                    System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
//                }
                simulated = FactoryUtils.concatenate(simulated, eval.evaluateModel(clsCopy, validation));
                observed = FactoryUtils.concatenate(observed, validation.attributeToDoubleArray(validation.classIndex()));
//                simulated = FactoryUtils.mean(simulated,eval.evaluateModel(clsCopy, validation));
//                observed = FactoryUtils.mean(observed,validation.attributeToDoubleArray(validation.classIndex()));
            }

            if (show_plot) {
                double[][] d = new double[2][simulated.length];
                d[0] = observed;
                d[1] = simulated;
                CMatrix f1 = CMatrix.getInstance(d);
                attr.figureCaption = "overall performance";
                f1.transpose().plot(attr);
            }
            if (show_text) {
                // output evaluation
                System.out.println();
                System.out.println("=== Setup for Overall Cross Validation===");
                System.out.println("Classifier: " + model.getClass().getName() + " " + Utils.joinOptions(model.getOptions()));
                System.out.println("Dataset: " + randData.relationName());
                System.out.println("Folds: " + folds);
                System.out.println("Seed: " + 1);
                System.out.println();
                System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
            }
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;
    }

    public static Evaluation performCrossValidateTestAlso(Classifier model, Instances datax, Instances test, boolean show_text, boolean show_plot) {
        TFigureAttribute attr = new TFigureAttribute();
        Random rand = new Random(1);
        Instances randData = new Instances(datax);
        randData.randomize(rand);

        Evaluation eval = null;
        int folds = randData.numInstances();
        try {
            eval = new Evaluation(randData);
            for (int n = 0; n < folds; n++) {
//                randData.randomize(rand);
//                Instances train = randData;                
                Instances train = randData.trainCV(folds, n);
//                Instances train = randData.trainCV(folds, n, rand);
                Classifier clsCopy = Classifier.makeCopy(model);
                clsCopy.buildClassifier(train);
                Instances validation = randData.testCV(folds, n);
//                Instances validation = test.testCV(test.numInstances(), n%test.numInstances());
//                CMatrix.fromInstances(train).showDataGrid();
//                CMatrix.fromInstances(validation).showDataGrid();
                
                simulated = FactoryUtils.concatenate(simulated, eval.evaluateModel(clsCopy, validation));
                observed = FactoryUtils.concatenate(observed, validation.attributeToDoubleArray(validation.classIndex()));
            }

            if (show_plot) {
                double[][] d = new double[2][simulated.length];
                d[0] = observed;
                d[1] = simulated;
                CMatrix f1 = CMatrix.getInstance(d);
                attr.figureCaption = "overall performance";
                f1.transpose().plot(attr);
            }
            if (show_text) {
                // output evaluation
                System.out.println();
                System.out.println("=== Setup for Overall Cross Validation===");
                System.out.println("Classifier: " + model.getClass().getName() + " " + Utils.joinOptions(model.getOptions()));
                System.out.println("Dataset: " + randData.relationName());
                System.out.println("Folds: " + folds);
                System.out.println("Seed: " + 1);
                System.out.println();
                System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
            }
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;
    }

    private static Evaluation doTest(boolean isTrained, Classifier model, Instances train, Instances test, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        long t1=FactoryUtils.tic();
        Instances data = new Instances(train);
        Random rand = new Random(1);
        data.randomize(rand);
        Evaluation eval = null;
        try {
//            double[] simulated = null;
            eval = new Evaluation(train);
            if (isTrained) {
                simulated = eval.evaluateModel(model, test);
            } else {
                Classifier clsCopy = Classifier.makeCopy(model);
                clsCopy.buildClassifier(train);
                System.out.println("\n benchmarking  ******************************************");
                t1=FactoryUtils.toc("training:",t1);
                simulated = eval.evaluateModel(clsCopy, test);
                t1=FactoryUtils.toc("testing:",t1);
                System.out.println("************************************************************");
            }
            if (show_plot) {
                observed = test.attributeToDoubleArray(test.classIndex());
                double[][] d = new double[2][simulated.length];
                d[0] = observed;
                d[1] = simulated;
                CMatrix f1 = CMatrix.getInstance(d);
                String[] items = {"Observed", "Simulated"};
                attr.items = items;
                attr.figureCaption = model.getClass().getCanonicalName();
                f1.transpose().plot(attr);
//                if (attr.axis[0].isEmpty() && attr.axis[1].isEmpty()) {
//                    f1.transpose().plot(attr);
//                } else {
//                    f1.transpose().plot(model.getClass().getCanonicalName(), attr.items, attr.axis);
//                }
            }
            if (show_text) {
                System.out.println();
                System.out.println("=== Setup for Test ===");
                System.out.println("Classifier: " + model.getClass().getName() + " " + Utils.joinOptions(model.getOptions()));
                System.out.println("Dataset: " + test.relationName());
                System.out.println();
                System.out.println(eval.toSummaryString("=== Test Results ===", false));
                System.out.println(eval.toMatrixString());
            }
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eval;
    }

    public static Evaluation performTest(Classifier model, Instances train, Instances test, boolean show_text, boolean show_plot) {
        String[] s = {"index", "value"};
        TFigureAttribute attr = new TFigureAttribute();
        attr.axis = s;
        return performTest(model, train, test, show_text, show_plot, attr);
    }

    public static Evaluation performTest(Classifier model, Instances train, Instances test, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        return doTest(false, model, train, test, show_text, show_plot, attr);
    }

    public static Evaluation performTestWithTrainedClassifier(Classifier model, Instances test, boolean show_text, boolean show_plot) {
        return doTest(true, model, test, test, show_text, show_plot, new TFigureAttribute());
    }

    public static Evaluation performTestWithTrainedClassifier(Classifier model, Instances test, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        return doTest(true, model, test, test, show_text, show_plot, attr);
    }

    public static Evaluation performSubsetTestWithTrainedClassifier(String lstComb, Classifier model, Instances data, boolean show_text, boolean show_plot) {
        Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
        return doTest(true, model, subsetData, subsetData, show_text, show_plot, new TFigureAttribute());
    }

    public static Evaluation performSubsetTestWithTrainedClassifier(String lstComb, Classifier model, Instances data, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
        return doTest(true, model, subsetData, subsetData, show_text, show_plot, attr);
    }

    public static Evaluation performSubsetCrossValidate(String lstComb, Instances data, Classifier model, boolean show_text, boolean show_plot) {
        Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
        Evaluation eval = FactoryEvaluation.performCrossValidate(model, subsetData, 10, show_text, show_plot, new TFigureAttribute());
        return eval;
    }

    public static Evaluation performSubsetCrossValidate(String lstComb, Instances data, Classifier model, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
        Evaluation eval = FactoryEvaluation.performCrossValidate(model, subsetData, 10, show_text, show_plot, attr);
        return eval;
    }

    public static Evaluation performSubsetTest(String lstComb, Instances train, Instances test, Classifier model, boolean show_text, boolean show_plot, TFigureAttribute attr) {
        Instances subsetDataTrain = FactoryInstance.getSubsetData(train, lstComb.split(","));
        Instances subsetDataTest = FactoryInstance.getSubsetData(test, lstComb.split(","));
        Evaluation eval = FactoryEvaluation.performTest(model, subsetDataTrain, subsetDataTest, show_text, show_plot, attr);
        return eval;
    }

    public static Evaluation performSubsetTest(String lstComb, Instances train, Instances test, Classifier model, boolean show_text, boolean show_plot) {
        Instances subsetDataTrain = FactoryInstance.getSubsetData(train, lstComb.split(","));
        Instances subsetDataTest = FactoryInstance.getSubsetData(test, lstComb.split(","));
        Evaluation eval = FactoryEvaluation.performTest(model, subsetDataTrain, subsetDataTest, show_text, show_plot);
        return eval;
    }

    public static void saveSubsetClassifier(String lstComb, Classifier model, Instances data, String filePath) {
        try {
            Instances subsetData = FactoryInstance.getSubsetData(data, lstComb.split(","));
            Classifier clsCopy = Classifier.makeCopy(model);
            clsCopy.buildClassifier(subsetData);
            weka.core.SerializationHelper.write(filePath, clsCopy);
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveClassifier(Classifier model, Instances train, String filePath) {
        try {
            Classifier clsCopy = Classifier.makeCopy(model);
            clsCopy.buildClassifier(train);
            weka.core.SerializationHelper.write(filePath, clsCopy);
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Classifier loadClassifier(String filePath) {
        Classifier cls = null;
        try {
            cls = (Classifier) weka.core.SerializationHelper.read(filePath);
        } catch (Exception ex) {
            Logger.getLogger(FactoryEvaluation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cls;
    }

}
