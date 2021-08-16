/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.mlp_classifier;

import ai.djl.Device;
import ai.djl.basicdataset.cv.classification.FashionMnist;
import ai.djl.engine.Engine;
import cezeri.deep_learning.ai.djl.examples.denemeler.utils.Training;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.GradientCollector;
import ai.djl.training.dataset.Batch;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;
import cezeri.matrix.CMatrix;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

/**
 *
 * @author cezerilab
 */
public class TestMLP {

    private static int numInputs = 784;
    private static int numOutputs = 10;
    private static int numHiddens = 256;

    private static NDManager manager = NDManager.newBaseManager();

    private static NDArray W1 = manager.randomNormal(
            0, 0.01f, new Shape(numInputs, numHiddens), DataType.FLOAT32, Device.defaultDevice());
    private static NDArray b1 = manager.zeros(new Shape(numHiddens));
    private static NDArray W2 = manager.randomNormal(
            0, 0.01f, new Shape(numHiddens, numOutputs), DataType.FLOAT32, Device.defaultDevice());
    private static NDArray b2 = manager.zeros(new Shape(numOutputs));

    private static NDList params = new NDList(W1, b1, W2, b2);
    private static Loss loss = Loss.softmaxCrossEntropyLoss();

    public static void main(String[] args) throws IOException, TranslateException {
        int batchSize = 256;

        FashionMnist trainIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TRAIN)
                .setSampling(batchSize, true)
                .optLimit(Long.getLong("DATASET_LIMIT", Long.MAX_VALUE))
                .build();

        FashionMnist testIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TEST)
                .setSampling(batchSize, true)
                .optLimit(Long.getLong("DATASET_LIMIT", Long.MAX_VALUE))
                .build();

        trainIter.prepare();
        testIter.prepare();

        for (NDArray param : params) {
            param.setRequiresGradient(true);
        }
//        int numEpochs = Integer.getInteger("MAX_EPOCH", 10);
        int numEpochs = Integer.getInteger("MAX_EPOCH", 5);
        float lr = 0.5f;

        double[] trainLoss;
        double[] testAccuracy;
        double[] epochCount;
        double[] trainAccuracy;

        trainLoss = new double[numEpochs];
        trainAccuracy = new double[numEpochs];
        testAccuracy = new double[numEpochs];
        epochCount = new double[numEpochs];

        float epochLoss = 0f;
        float accuracyVal = 0f;

        for (int epoch = 1; epoch <= numEpochs; epoch++) {

            System.out.print("Running epoch " + epoch + "...... ");
            // Iterate over dataset
            for (Batch batch : trainIter.getData(manager)) {

                NDArray X = batch.getData().head();
                NDArray y = batch.getLabels().head();

                try (GradientCollector gc = Engine.getInstance().newGradientCollector()) {
                    NDArray yHat = net(X); // net function call

                    NDArray lossValue = loss.evaluate(new NDList(y), new NDList(yHat));
                    NDArray l = lossValue.mul(batchSize);
                    accuracyVal += Training.accuracy(yHat, y);
                    epochLoss += l.sum().getFloat();

                    gc.backward(l); // gradient calculation
                }

                batch.close();
                Training.sgd(params, lr, batchSize); // updater
            }

            trainLoss[epoch - 1] = epochLoss / trainIter.size();
            trainAccuracy[epoch - 1] = accuracyVal / trainIter.size();

            epochLoss = 0f;
            accuracyVal = 0f;
            // testing now
            for (Batch batch : testIter.getData(manager)) {

                NDArray X = batch.getData().head();
                NDArray y = batch.getLabels().head();

                NDArray yHat = net(X); // net function call
                accuracyVal += Training.accuracy(yHat, y);
            }

            testAccuracy[epoch - 1] = accuracyVal / testIter.size();
            epochCount[epoch - 1] = epoch;
            accuracyVal = 0f;
            System.out.println("Finished epoch " + epoch);
        }

        System.out.println("Finished training!");

        String[] lossLabel = new String[trainLoss.length + testAccuracy.length + trainAccuracy.length];

        Arrays.fill(lossLabel, 0, trainLoss.length, "train loss");
        Arrays.fill(lossLabel, trainAccuracy.length, trainLoss.length + trainAccuracy.length, "train acc");
        Arrays.fill(lossLabel, trainLoss.length + trainAccuracy.length,
                trainLoss.length + testAccuracy.length + trainAccuracy.length, "test acc");
        
        int a=1;
        CMatrix cm = CMatrix.getInstance(trainLoss);
        cm.cat(1, CMatrix.getInstance(trainAccuracy)).cat(1, CMatrix.getInstance(testAccuracy)).plot();


    }

    public static NDArray relu(NDArray X) {
        return X.maximum(0f);
    }

    public static NDArray net(NDArray X) {
        X = X.reshape(new Shape(-1, numInputs));
        NDArray H = relu(X.dot(W1).add(b1));
        return H.dot(W2).add(b2);
    }
}
