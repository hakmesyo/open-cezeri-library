/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.mnist;

import ai.djl.Model;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Block;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
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
    public static int NUM_OF_OUTPUT = 10;

    // the height and width for pre-processing of the image
    public static int IMAGE_HEIGHT = 28;
    public static int IMAGE_WIDTH = 28;

    // the name of the model
    public static final String MODEL_NAME = "mnist";

    private Models() {
    }

    public static Model getModel(int p_NUM_OF_OUTPUT, int p_IMAGE_HEIGHT, int p_IMAGE_WIDTH) {
        NUM_OF_OUTPUT = p_NUM_OF_OUTPUT;
        IMAGE_WIDTH = p_IMAGE_WIDTH;
        IMAGE_HEIGHT = p_IMAGE_HEIGHT;

        // create new instance of an empty model
        Model model = Model.newInstance(MODEL_NAME);

        // Block is a composable unit that forms a neural network; combine them like Lego blocks
        // to form a complex network
//        Block block =
//                ResNetV1.builder() // construct the network
//                        .setImageShape(new Shape(3, IMAGE_HEIGHT, IMAGE_WIDTH))
//                        .setNumLayers(50)
//                        .setOutSize(NUM_OF_OUTPUT)
//                        .build();
        Block block =
                new Mlp(
                        3*IMAGE_HEIGHT * IMAGE_WIDTH,
                        NUM_OF_OUTPUT,
                        new int[] {128, 64});
//        SequentialBlock block = new SequentialBlock()
//                .add(Blocks.batchFlattenBlock(3 * IMAGE_WIDTH * IMAGE_HEIGHT))
//                .add(Linear.builder().setUnits(128).build())
//                .add(Activation.reluBlock())
//                .add(Linear.builder().setUnits(64).build())
//                .add(Activation.reluBlock())
//                .add(Linear.builder().setUnits(NUM_OF_OUTPUT).build());

        // set the neural network to the model
        model.setBlock(block);
        return model;
    }

    public static void saveSynset(Path modelDir, List<String> synset) throws IOException {
        Path synsetFile = modelDir.resolve("synset.txt");
        try (Writer writer = Files.newBufferedWriter(synsetFile)) {
            writer.write(String.join("\n", synset));
        }
    }
}
