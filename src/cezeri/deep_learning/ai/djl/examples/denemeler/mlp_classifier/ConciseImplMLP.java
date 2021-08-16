/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.mlp_classifier;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.metric.*;
import ai.djl.ndarray.types.*;
import ai.djl.nn.*;
import ai.djl.nn.core.*;
import ai.djl.training.*;
import ai.djl.training.initializer.*;
import ai.djl.training.loss.*;
import ai.djl.training.listener.*;
import ai.djl.training.evaluator.*;
import ai.djl.training.optimizer.*;
import ai.djl.training.tracker.*;
import ai.djl.basicdataset.cv.classification.FashionMnist;
import ai.djl.training.dataset.Dataset;
import ai.djl.translate.TranslateException;
import cezeri.matrix.CMatrix;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author cezerilab
 */
public class ConciseImplMLP {

    public static void main(String[] args) throws IOException, TranslateException {
        SequentialBlock net = new SequentialBlock();
        net.add(Blocks.batchFlattenBlock(784));
        net.add(Linear.builder().setUnits(256).build());
        net.add(Activation::relu);
        net.add(Linear.builder().setUnits(10).build());
        net.setInitializer(new NormalInitializer(), Parameter.Type.OTHER);

        int batchSize = 256;
        int numEpochs = 10;
        double[] trainLoss;
        double[] testAccuracy;
        double[] epochCount;
        double[] trainAccuracy;

        trainLoss = new double[numEpochs];
        trainAccuracy = new double[numEpochs];
        testAccuracy = new double[numEpochs];
        epochCount = new double[numEpochs];

        FashionMnist trainIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TRAIN)
                .setSampling(batchSize, true)
                .build();

        FashionMnist testIter = FashionMnist.builder()
                .optUsage(Dataset.Usage.TEST)
                .setSampling(batchSize, true)
                .build();

        trainIter.prepare();
        testIter.prepare();

        for (int i = 0; i < epochCount.length; i++) {
            epochCount[i] = (i + 1);
        }

        Map<String, double[]> evaluatorMetrics = new HashMap<>();

        Tracker lrt = Tracker.fixed(0.5f);
        Optimizer sgd = Optimizer.sgd().setLearningRateTracker(lrt).build();

        Loss loss = Loss.softmaxCrossEntropyLoss();

        DefaultTrainingConfig config = new DefaultTrainingConfig(loss)
                .optOptimizer(sgd) // Optimizer (loss function)
                .optDevices(Device.getDevices(1)) // single GPU
                .addEvaluator(new Accuracy()) // Model Accuracy
                .addTrainingListeners(TrainingListener.Defaults.logging()); // Logging

        try (Model model = Model.newInstance("mlp")) {
            model.setBlock(net);

            try (Trainer trainer = model.newTrainer(config)) {

                trainer.initialize(new Shape(1, 784));
                trainer.setMetrics(new Metrics());

                EasyTrain.fit(trainer, numEpochs, trainIter, testIter);
                // collect results from evaluators
                Metrics metrics = trainer.getMetrics();

                trainer.getEvaluators().stream()
                        .forEach(evaluator -> {
                            evaluatorMetrics.put("train_epoch_" + evaluator.getName(), metrics.getMetric("train_epoch_" + evaluator.getName()).stream()
                                    .mapToDouble(x -> x.getValue().doubleValue()).toArray());
                            evaluatorMetrics.put("validate_epoch_" + evaluator.getName(), metrics.getMetric("validate_epoch_" + evaluator.getName()).stream()
                                    .mapToDouble(x -> x.getValue().doubleValue()).toArray());
                        });
            }
        }

        trainLoss = evaluatorMetrics.get("train_epoch_SoftmaxCrossEntropyLoss");
        trainAccuracy = evaluatorMetrics.get("train_epoch_Accuracy");
        testAccuracy = evaluatorMetrics.get("validate_epoch_Accuracy");

        String[] lossLabel = new String[trainLoss.length + testAccuracy.length + trainAccuracy.length];

        Arrays.fill(lossLabel, 0, trainLoss.length, "test acc");
        Arrays.fill(lossLabel, trainAccuracy.length, trainLoss.length + trainAccuracy.length, "train acc");
        Arrays.fill(lossLabel, trainLoss.length + trainAccuracy.length,
                trainLoss.length + testAccuracy.length + trainAccuracy.length, "train loss");
        CMatrix cm = CMatrix.getInstance(trainLoss);
        CMatrix.getInstance(trainLoss).cat(1, CMatrix.getInstance(trainAccuracy)).cat(1, CMatrix.getInstance(testAccuracy)).plot();

    }
}
