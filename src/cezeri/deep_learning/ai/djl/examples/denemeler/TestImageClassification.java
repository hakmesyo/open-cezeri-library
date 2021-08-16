/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler;

import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.repository.SimpleRepository;
import ai.djl.repository.SimpleUrlRepository;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * https://www.infoq.com/articles/djl-deep-learning-java/
 *
 * @author cezerilab
 */
public class TestImageClassification {

    // identify the location of the training data
    private static String trainingDatasetRoot = "C:\\Users\\cezerilab\\Downloads\\ut-zap50k-images-square";

    // identify the location of the validation data
    private static String validateDatasetRoot = "src/test/resources/imagefolder/validate";

    //create training data ImageFolder dataset
    private static ImageFolder trainingDataset = null;

    //create validation data ImageFolder dataset
    //private static ImageFolder validateDataset = initDataset(validateDatasetRoot);
    private static final int BATCH_SIZE = 32;

    private static final int NEW_WIDTH = 136;
    private static final int NEW_HEIGHT = 136;

    public static void main(String[] args) throws IOException, TranslateException {
        ImageFolder dataset = ImageFolder.builder()
                .setRepositoryPath(Paths.get(trainingDatasetRoot)) // set root of image folder
                .optMaxDepth(10) // set the max depth of the sub folders
                .addTransform(new Resize(136, 136))
                .addTransform(new ToTensor())
                .setSampling(BATCH_SIZE, true) // random sampling; don't process the data in order
                .build();
        dataset.prepare();
        System.out.println("dataset = " + dataset.size());
//        trainingDataset = initDataset(trainingDatasetRoot);
//        int a=4;
    }

    private static ImageFolder initDataset(String datasetRoot) throws IOException, TranslateException {
        ImageFolder dataset = ImageFolder.builder()
                .setRepositoryPath(Paths.get(trainingDatasetRoot))
                .optPipeline(
                    new Pipeline().add(new Resize(NEW_WIDTH, NEW_HEIGHT)).add(new ToTensor())
                )
                .setSampling(BATCH_SIZE, true)
                .build();
        dataset.prepare();
        return dataset;
    }
}
