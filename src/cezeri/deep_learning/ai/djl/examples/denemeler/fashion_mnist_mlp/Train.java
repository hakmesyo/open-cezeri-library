/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.fashion_mnist_mlp;

import ai.djl.basicdataset.cv.classification.FashionMnist;
import ai.djl.engine.Engine;
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
import cezeri.deep_learning.ai.djl.examples.denemeler.utils.Training;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.api.LinePlot;

/**
 *
 * @author cezerilab
 */
public class Train {

    static int numInputs = 784;
    static int numOutputs = 10;
    static int numHiddens = 256;

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

        try {
            trainIter.prepare();
            testIter.prepare();
        } catch (IOException ex) {
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TranslateException ex) {
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
        }

        NDManager manager = NDManager.newBaseManager();

        NDArray W1 = manager.randomNormal(
                0, 0.01f, new Shape(numInputs, numHiddens), DataType.FLOAT32);
        NDArray b1 = manager.zeros(new Shape(numHiddens));
        NDArray W2 = manager.randomNormal(
                0, 0.01f, new Shape(numHiddens, numOutputs), DataType.FLOAT32);
        NDArray b2 = manager.zeros(new Shape(numOutputs));

        NDList params = new NDList(W1, b1, W2, b2);

        for (NDArray param : params) {
            param.setRequiresGradient(true);
        }

        Loss loss = Loss.softmaxCrossEntropyLoss();

        int numEpochs = Integer.getInteger("MAX_EPOCH", 1);
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
                    NDArray yHat = net(X, W1, b1, W2, b2); // net function call

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

                NDArray yHat = net(X, W1, b1, W2, b2); // net function call
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

        Table data = Table.create("Data").addColumns(
                DoubleColumn.create("epochCount", ArrayUtils.addAll(epochCount, ArrayUtils.addAll(epochCount, epochCount))),
                DoubleColumn.create("loss", ArrayUtils.addAll(trainLoss, ArrayUtils.addAll(trainAccuracy, testAccuracy))),
                StringColumn.create("lossLabel", lossLabel)
        );

        //render(LinePlot.create("", data, "epochCount", "loss", "lossLabel"), "text/html");
    }

    public static NDArray relu(NDArray X) {
        return X.maximum(0f);
    }

    public static NDArray net(NDArray X, NDArray W1, NDArray b1, NDArray W2, NDArray b2) {
        X = X.reshape(new Shape(-1, numInputs));
        NDArray H = relu(X.dot(W1).add(b1));
        return H.dot(W2).add(b2);
    }
}
