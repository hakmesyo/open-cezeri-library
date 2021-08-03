/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.djl.examples.denemeler.pistachio_classification;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.engine.Engine;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Block;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.convolutional.Conv2d;
import ai.djl.nn.core.Linear;
import ai.djl.nn.pooling.Pool;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingConfig;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A helper class loads and saves model.
 */
public final class Models {

    // the number of classification labels: boots, sandals, shoes, slippers
    public static int NUM_OF_OUTPUT = 3;

    // the height and width for pre-processing of the image
    public static int IMAGE_HEIGHT = 224;
    public static int IMAGE_WIDTH = 224;

    // the name of the model
    public static final String MODEL_NAME = "pistachio_open_closed";
    public static TrainingConfig config=null;
    public static Model model=null;

    public static Trainer getTrainer() {
        return model.newTrainer(Models.config);
    }

    private Models() {
    }

    public static Model getModel(int p_NUM_OF_OUTPUT, int p_IMAGE_HEIGHT, int p_IMAGE_WIDTH) {
        NUM_OF_OUTPUT = p_NUM_OF_OUTPUT;
        IMAGE_WIDTH = p_IMAGE_WIDTH;
        IMAGE_HEIGHT = p_IMAGE_HEIGHT;


        // Block is a composable unit that forms a neural network; combine them like Lego blocks
        // to form a complex network
//        Block block =
//                ResNetV1.builder() // construct the network
//                        .setImageShape(new Shape(3, IMAGE_HEIGHT, IMAGE_WIDTH))
//                        .setNumLayers(50)
//                        .setOutSize(NUM_OF_OUTPUT)
//                        .build();
//        Block block =
//                new Mlp(
//                        3 * IMAGE_HEIGHT * IMAGE_WIDTH,
//                        NUM_OF_OUTPUT,
//                        new int[] {128, 64});
        // create new instance of an empty model
        model = Model.newInstance(MODEL_NAME);
        Block block = buildLeNetCNN();
        model.setBlock(block);
        config=setupTrainingConfig(model,block);
        // set the neural network to the model
        return model;
    }

    public static Block buildLeNetCNN() {
        Engine.getInstance().setRandomSeed(1111);

        SequentialBlock block = new SequentialBlock()
                .add(Conv2d.builder()
                        .setKernelShape(new Shape(5, 5))
                        .optPadding(new Shape(2, 2))
                        .optBias(false)
                        .setFilters(6)
                        .build())
                .add(Activation::sigmoid)
                .add(Pool.avgPool2dBlock(new Shape(5, 5), new Shape(2, 2), new Shape(2, 2)))
                .add(Conv2d.builder()
                        .setKernelShape(new Shape(5, 5))
                        .setFilters(16).build())
                .add(Activation::sigmoid)
                .add(Pool.avgPool2dBlock(new Shape(5, 5), new Shape(2, 2), new Shape(2, 2)))
                // Blocks.batchFlattenBlock() will transform the input of the shape (batch size, channel,
                // height, width) into the input of the shape (batch size,
                // channel * height * width)
                .add(Blocks.batchFlattenBlock())
                .add(Linear
                        .builder()
                        .setUnits(120)
                        .build())
                .add(Activation::sigmoid)
                .add(Linear
                        .builder()
                        .setUnits(84)
                        .build())
                .add(Activation::sigmoid)
                .add(Linear
                        .builder()
                        .setUnits(10)
                        .build());
        return block;
    }

    public static TrainingConfig setupTrainingConfig(Model model,Block block) {
        float lr = 0.9f;
        //Model model = Model.newInstance("cnn");
        //model.setBlock(block);

        Loss loss = Loss.softmaxCrossEntropyLoss();

        Tracker lrt = Tracker.fixed(lr);
        Optimizer sgd = Optimizer.sgd().setLearningRateTracker(lrt).build();

        DefaultTrainingConfig config = new DefaultTrainingConfig(loss).optOptimizer(sgd) // Optimizer (loss function)
                .optDevices(Device.getDevices(1)) // Single GPU
                .addEvaluator(new Accuracy()) // Model Accuracy
                .addTrainingListeners(TrainingListener.Defaults.logging()); // Logging

        Trainer trainer = model.newTrainer(config);

        NDManager manager = NDManager.newBaseManager();
        NDArray X = manager.randomUniform(0f, 1.0f, new Shape(1, 3, IMAGE_WIDTH, IMAGE_HEIGHT));
        trainer.initialize(X.getShape());

        Shape currentShape = X.getShape();

        for (int i = 0; i < block.getChildren().size(); i++) {

            Shape[] newShape = block.getChildren().get(i).getValue().getOutputShapes(new Shape[]{currentShape});
            currentShape = newShape[0];
            System.out.println(block.getChildren().get(i).getKey() + " layer output : " + currentShape);
        }
        return config;
    }
    
    public static void saveSynset(Path modelDir, List<String> synset) throws IOException {
        Path synsetFile = modelDir.resolve("synset.txt");
        try (Writer writer = Files.newBufferedWriter(synsetFile)) {
            writer.write(String.join("\n", synset));
        }
    }
}
