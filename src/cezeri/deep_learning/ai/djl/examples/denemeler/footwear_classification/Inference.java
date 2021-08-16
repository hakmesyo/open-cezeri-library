/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.footwear_classification;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import cezeri.factory.FactoryUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Uses the model to generate a prediction called an inference
 */
public class Inference {
//    private static final int NUM_OF_OUTPUTS = 10;
//    private static final int IMAGE_WIDTH = 224;
//    private static final int IMAGE_HEIGHT = 224;
//    private static final int IMAGE_WIDTH = 28;
//    private static final int IMAGE_HEIGHT = 28;

    public static void main(String[] args) throws ModelException, TranslateException, IOException {
        // the location where the model is saved
        Path modelDir = Paths.get("models");
        System.setProperty("DJL_CACHE_DIR", "c:/ai/djl");
        // the path of image to classify
        String imageFilePath;
        if (args.length == 0) {
            //imageFilePath = "c:/ai/djl/ut-zap50k-images-square/Sandals/Heel/Annie/7350693.3.jpg";
            imageFilePath = "c:/ai/djl/tc_numbers/9/1.jpg";
//            imageFilePath = "c:/ai/djl/mnist_test/0/img_1.jpg";
        } else {
            imageFilePath = args[0];
        }

        // Load the image file from the path
        Image img = ImageFactory.getInstance().fromFile(Paths.get(imageFilePath));

        try (Model model = Models.getModel(Models.NUM_OF_OUTPUT, Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT)) { // empty model instance
            // load the model
            model.load(modelDir, Models.MODEL_NAME);

            // define a translator for pre and post processing
            // out of the box this translator converts images to ResNet friendly ResNet 18 shape
            Translator<Image, Classifications> translator
                    = ImageClassificationTranslator.builder()
                            .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                            .addTransform(new ToTensor())
                            .optApplySoftmax(true)
                            .build();

            // run the inference using a Predictor
            int totImg = 0;
            int nErr = 0;
            try (Predictor<Image, Classifications> predictor = model.newPredictor(translator)) {
                // holds the probability score per label
                String basePath = "c:/ai/djl/tc_numbers/";
                for (int i = 0; i < 10; i++) {
                    String imgPath = basePath + i;
                    File[] imgFiles = FactoryUtils.getFileListInFolderForImages(imgPath);
                    for (File imgFile : imgFiles) {
                        totImg++;
                        img = ImageFactory.getInstance().fromFile(Paths.get(imgFile.getAbsolutePath()));
                        Classifications predictResult = predictor.predict(img);
                        int predictedNum = Integer.parseInt(predictResult.topK(1).toString().split(",")[0].split(":")[1].replace('"', ' ').trim());
                        if (i != predictedNum) {
                            nErr++;
                            System.out.println("classificiation error at:" + i + " -> " + predictedNum + " img:" + imgFile.getName());
                        }else{
                            System.out.println("hit at:" + i + " -> " + predictedNum + " img:" + imgFile.getAbsolutePath());
                        }
                        //System.out.println(i+".class:"+predictedNum);
//                        System.out.println(predictResult);
                    }
                }
            }
            System.out.println("total test case:"+totImg+" accuracy ratio:" + (totImg-nErr)*1.0/totImg*100);
        }
    }
}
