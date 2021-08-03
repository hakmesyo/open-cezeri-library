/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.basicmodelzoo.basic.Mlp;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.TrainingConfig;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;
import cezeri.types.TBlockType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab
 */
public class FactoryDJL {

    public static Model getModelForTraining(String MODEL_NAME, int NUM_CHANNEL, int IMAGE_WIDTH, int IMAGE_HEIGHT, int NUM_OUTPUT, int blockType) {
        Model model = Model.newInstance(MODEL_NAME);
        Block block = null;
        if (blockType == TBlockType.MLP) {
            block = new Mlp(
                    NUM_CHANNEL * IMAGE_HEIGHT * IMAGE_WIDTH,
                    NUM_OUTPUT,
                    new int[]{128, 64});
        } else if (blockType == TBlockType.ResNetV50) {
            block
                    = ResNetV1.builder() // construct the network
                            .setImageShape(new Shape(NUM_CHANNEL, IMAGE_HEIGHT, IMAGE_WIDTH))
                            .setNumLayers(50)
                            .setOutSize(NUM_OUTPUT)
                            .build();

        }
        model.setBlock(block);
        return model;
    }

    public static Model getModelForInference(String modelPath, int NUM_CHANNELS, int IMAGE_WIDTH, int IMAGE_HEIGHT, int blockType) {
        String modelName = extractModelName(modelPath);
        String dirPath = new File(modelPath).getParent();
        int NUM_OF_OUTPUT = FactoryUtils.readFromFileAsString1D(dirPath + "/synset.txt").length;
        Model model = Model.newInstance(modelName);
        Block block = null;
        if (blockType == TBlockType.MLP) {
            block = new Mlp(
                    NUM_CHANNELS * IMAGE_HEIGHT * IMAGE_WIDTH,
                    NUM_OF_OUTPUT,
                    new int[]{128, 64});
        } else if (blockType == TBlockType.ResNetV50) {
            block
                    = ResNetV1.builder() // construct the network
                            .setImageShape(new Shape(NUM_CHANNELS, IMAGE_HEIGHT, IMAGE_WIDTH))
                            .setNumLayers(50)
                            .setOutSize(NUM_OF_OUTPUT)
                            .build();

        }
        model.setBlock(block);
        Path modelDir = Paths.get(dirPath);
        try {
            model.load(modelDir, modelName);
        } catch (IOException ex) {
            Logger.getLogger(FactoryDJL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedModelException ex) {
            Logger.getLogger(FactoryDJL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }

    private static String extractModelName(String path) {
        String modelName = "";
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            char current;
            int k = 0;
            int val = 0;
            while (fis.available() > 0 && k < 30) {
                val = fis.read();//System.out.print(val);
                if (k++ < 10) {
                    continue;
                }
                if (val == 0) {
                    break;
                }
                current = (char) val;
                modelName += current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelName;
    }

    public static ImageFolder initDataset(String dsDir, int BATCH_SIZE) {
        ImageFolder dataset
                = ImageFolder.builder()
                        // retrieve the data
                        .setRepositoryPath(Paths.get(dsDir))
                        .optMaxDepth(10)
                        //.addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                        .addTransform(new ToTensor())
                        // random sampling; don't process the data in order
                        .setSampling(BATCH_SIZE, true)
                        .build();

        try {
            dataset.prepare();
        } catch (IOException ex) {
            Logger.getLogger(FactoryDJL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TranslateException ex) {
            Logger.getLogger(FactoryDJL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    public static TrainingConfig setupTrainingConfig(Loss loss) {
        return new DefaultTrainingConfig(loss)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
    }

}
