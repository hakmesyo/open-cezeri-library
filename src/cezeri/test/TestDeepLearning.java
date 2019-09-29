/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.machine_learning.classifiers.deeplearning_ocl.Matrix;
import cezeri.machine_learning.classifiers.deeplearning_ocl.MatrixUtils;
import cezeri.machine_learning.classifiers.deeplearning_ocl.DeepNeuralNetwork;
import cezeri.machine_learning.classifiers.deeplearning_ocl.NNParams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author BAP1
 */
public class TestDeepLearning {
    public static void main(String[] args) throws Exception {
//        double[][] da = {{1, 3, 5, 7, 9}, {2, 4, 6, 8, 1}, {0, 3, 5, 9, 1}, {3, 0, 2, 5, 9}};
//        double[][] db = new double[da.length][da[0].length];
//        System.arraycopy(da, 0, db, 0, da.length);
//        System.out.println(Arrays.toString(db));
//        
//        double[] d={1,0,-1,3,1,5,4,6,7};
//        double[] target=new double[d.length];
//        System.arraycopy(d, 0, target, 0, d.length);
//        System.out.println(Arrays.toString(target));
        runKaggleDigitsClassification();
    }
    /**
     * Performs classification of Handwritten digits,
     * using a subset (1000 rows) from the Kaggle Digits competition.
     */
    public static void runKaggleDigitsClassification() throws Exception {
        // Read data from CSV-file
        int headerRows = 1;
        char separator = ';';
        Matrix data = MatrixUtils.readCSV("data/Kaggle_Digits_1000.csv", separator, headerRows);

        // Split data into training set and crossvalidation set.
        float crossValidationPercent = 33;
        Matrix[] split = MatrixUtils.split(data, crossValidationPercent, 0);
        Matrix dataTrain = split[0];
        Matrix dataCV = split[1];

        // First column contains the classification label. The rest are the indata.
        Matrix xTrain = dataTrain.getColumns(1, -1);
        Matrix yTrain = dataTrain.getColumns(0, 0);
        Matrix xCV = dataCV.getColumns(1, -1);
        Matrix yCV = dataCV.getColumns(0, 0);

        NNParams params = new NNParams();        
        params.numClasses = 10; // 10 digits to classify
        params.hiddenLayerParams = new NNParams.NNLayerParams[] { 
            new NNParams.NNLayerParams(10, 5, 5, 2, 2) , 
            new NNParams.NNLayerParams(100, 5, 5, 2, 2) };
        params.learningRate = 1E-2;
        params.maxIterations = 10;
        params.numThreads=1;

        long startTime = System.currentTimeMillis();
        DeepNeuralNetwork nn = new DeepNeuralNetwork(params);
        nn.train(xTrain, yTrain);
        System.out.println("Training time: " + (System.currentTimeMillis() - startTime) / 1000.0 + "s");

        List<Matrix> batchesX = new ArrayList<>();
        List<Matrix> batchesY = new ArrayList<>();
        MatrixUtils.split(xTrain, yTrain, params.batchSize, batchesX, batchesY);
        int correct = 0;
        for (int batch = 0; batch < batchesX.size(); batch++) {
            int[] predictedClasses = nn.getPredictedClasses(batchesX.get(batch));
            for (int i = 0; i < predictedClasses.length; i++) {
                if (predictedClasses[i] == batchesY.get(batch).get(i, 0)) {
                    correct++;
                }
            }
        }
        System.out.println("Training set accuracy: " + (double) correct/xTrain.numRows()*100 + "%");

        batchesX = new ArrayList<>();
        batchesY = new ArrayList<>();
        MatrixUtils.split(xCV, yCV, params.batchSize, batchesX, batchesY);
        correct = 0;
        for (int batch = 0; batch < batchesX.size(); batch++) {
            int[] predictedClasses = nn.getPredictedClasses(batchesX.get(batch));
            for (int i = 0; i < predictedClasses.length; i++) {
                if (predictedClasses[i] == batchesY.get(batch).get(i, 0)) {
                    correct++;
                }
            }
        }
        System.out.println("Crossvalidation set accuracy: " + (double) correct/xCV.numRows()*100 + "%");

    }

}
