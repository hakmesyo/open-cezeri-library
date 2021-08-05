/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.djl.examples.denemeler;

import ai.djl.nn.Block;
import cezeri.factory.FactoryDJL;
import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import cezeri.types.TBlockType;
import cezeri.types.TRoi;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class CMatrixPattern {

    private static String path = "C:\\ai\\djl\\dataset_pdr\\mixed\\";

    public static void main(String[] args) {
//        trainWithResNet();
//        predictTCNumbers();

//        trainGenderWithMLP();
//        predictGenderWithMLPBlock();
//        preparePistachioDataSet();
//        trainPistachioWithMLP();
//        predictPistachioWithMLPBlock();
//        trainPistachioWithResNet();
        predictPistachioWithResNet();
    }

    private static void extractNumbers(CMatrix[] numbers, CMatrix cm, int p) {
        int w = 303;
        int h = 1;
        for (int i = 0; i < 11; i++) {
            String str = (p + i * w) + ":" + (i * w + w + p);
            numbers[i] = cm.cmd("100:540", str).imresize(224, 224);
        }
    }

    private static void predictTCNumbers() {
        File[] files = FactoryUtils.getFileListInFolder(path);
        System.out.println("size:" + files.length);
        String[] tc = new String[files.length];
        String[] gender = new String[files.length];
        CMatrix cm_1 = CMatrix.getInstance();
        CMatrix cm_gender = CMatrix.getInstance().setModelForInference("models/gender/gender-0030.params");
        CMatrix cm_tc = CMatrix.getInstance().setModelForInference("models/tc_numbers/tc_numbers-0030.params");
        CMatrix[] numbers = new CMatrix[11];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = CMatrix.getInstance();
        }
        long t1 = FactoryUtils.tic();
        for (int i = 0; i < files.length; i++) {
            cm_1 = cm_1
                    .refresh()
                    //.tic()
                    .imread(files[i].getAbsolutePath())
                    //.toc()
                    .cmd("0:30", "130:160")
                    //.toc()
                    .imresize(20)
                    //.toc()
                    .imresize(224, 224) //.toc()
                    ;
            //t1=FactoryUtils.toc(t1);
            String str = cm_gender.setImage(cm_1.getImage()).predictWithLabel();
            System.out.println(str);
            //t1=FactoryUtils.toc(t1);

            gender[i] = str;
            if (str.equals("female")) {
                cm_tc = cm_tc
                        .imread(files[i].getAbsolutePath())
                        .cmd("0:30", "217:387")
                        .imresize(20) //.imshow()
                        ;

                extractNumbers(numbers, cm_tc, 0);
                String s = "";
                for (CMatrix number : numbers) {
                    s += number.predictWithLabel();
                }
                tc[i] = s;

            } else {
                cm_tc = cm_tc
                        .imread(files[i].getAbsolutePath())
                        .cmd("0:30", "220:390")
                        .imresize(20) //.imshow()
                        ;

                extractNumbers(numbers, cm_tc, 60);
                String s = "";
                for (CMatrix number : numbers) {
                    s += number.predictWithLabel();
                }
                tc[i] = s;
            }
            t1 = FactoryUtils.toc(t1);

            String fileName = FactoryUtils.getFileName(files[i].getName());
            if (!tc[i].equals(fileName)) {
                System.out.println(i + ".index error");
                System.out.println(tc[i]);
            } else {
                System.out.println(i + ".index hit");
            }

        }
        //Arrays.asList(tc).forEach(System.out::println);
        String[] rows = new String[tc.length];
        for (int i = 0; i < tc.length; i++) {
            rows[i] = gender[i] + ";" + tc[i];
        }
        FactoryUtils.writeToFile("models/tc_numbers/tcno.csv", rows);
    }

    private static void trainGenderWithMLP() {
        final int NUM_CHANNEL = 3;
        final int IMAGE_WIDTH = 224;
        final int IMAGE_HEIGHT = 224;
        final int NUM_OUTPUT = 2;
        final int EPOCH = 30;
        final int BATCH_SIZE = 32;
        final int TRAIN_RATIO = 10;
        final int VAL_RATIO = 0;
        final int[] HIDDEN = {128, 64};
        final String MODEL_NAME = "gender";
        final String DATA_SET = "C:\\ai\\djl\\gender";
//        final String MODEL_NAME = "tc_numbers";
//        final String DATA_SET = "C:\\ai\\djl\\dataset_pdr\\dataset";

        Block block = FactoryDJL.getMLPBlock(NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, HIDDEN);
        CMatrix cm = CMatrix.getInstance()
                .setModelForTrain(MODEL_NAME, NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, TBlockType.MLP, block)
                .trainDataSet(DATA_SET, BATCH_SIZE, TRAIN_RATIO, VAL_RATIO, EPOCH);
    }

    private static void trainWithResNet() {
        final int NUM_CHANNEL = 3;
        final int IMAGE_WIDTH = 224;
        final int IMAGE_HEIGHT = 224;
        final int NUM_OUTPUT = 10;
        final int EPOCH = 30;
        final int BATCH_SIZE = 32;
        final int TRAIN_RATIO = 10;
        final int VAL_RATIO = 0;
        final int NUM_LAYER = 50;
        final String MODEL_NAME = "tc_numbers";
        final String DATA_SET = "C:\\ai\\djl\\dataset_pdr\\dataset";

        Block block = FactoryDJL.getResNetBlock(NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, NUM_LAYER);
        CMatrix cm = CMatrix.getInstance()
                .setModelForTrain(MODEL_NAME, NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, TBlockType.ResNetV50, block)
                .trainDataSet(DATA_SET, BATCH_SIZE, TRAIN_RATIO, VAL_RATIO, EPOCH);
    }

    private static void predictGenderWithMLPBlock() {
        final String MODEL_PATH = "models/gender/gender-0030.params";
        final String IMAGE_PATH = "C:\\ai\\djl\\gender\\male\\1.jpg";

        CMatrix cm_gender = CMatrix.getInstance().setModelForInference(MODEL_PATH);
        for (int i = 0; i < 100; i++) {
            long t1 = FactoryUtils.tic();
            String str = cm_gender
                    .imread(IMAGE_PATH)
                    .predictWithLabel();
            t1 = FactoryUtils.toc(t1);
            System.out.println("predicted class:" + str);
        }
    }

    private static void trainPistachioWithMLP() {
        final int NUM_CHANNEL = 3;
        final int IMAGE_WIDTH = 224;
        final int IMAGE_HEIGHT = 224;
        final int NUM_OUTPUT = 3;
        final int EPOCH = 30;
        final int BATCH_SIZE = 32;
        final int TRAIN_RATIO = 10;
        final int VAL_RATIO = 0;
        final int[] HIDDEN = {128, 64};
        final String MODEL_NAME = "pistachio_mlp";
        final String DATA_SET = "C:\\ai\\djl\\resized_pistachio_train";

        Block block = FactoryDJL.getMLPBlock(NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, HIDDEN);
        CMatrix cm = CMatrix.getInstance()
                .setModelForTrain(MODEL_NAME, NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, TBlockType.MLP, block)
                .trainDataSet(DATA_SET, BATCH_SIZE, TRAIN_RATIO, VAL_RATIO, EPOCH);
    }

    private static void predictPistachioWithMLPBlock() {
        final String MODEL_PATH = "models/pistachio_mlp/pistachio_mlp-0030.params";
        final String IMAGE_PATH = "C:\\ai\\djl\\resized_pistachio_test\\open";

        File[] files = FactoryUtils.getFileListInFolderForImages(IMAGE_PATH);

        CMatrix cm_gender = CMatrix.getInstance().setModelForInference(MODEL_PATH);
        for (int i = 0; i < files.length; i++) {
            long t1 = FactoryUtils.tic();
            String str = cm_gender
                    .imread(files[i].getAbsolutePath())
                    .predictWithLabel();
            t1 = FactoryUtils.toc(t1);
            System.out.println("predicted class:" + str);
        }
    }

    private static void preparePistachioDataSet() {
        final String DATA_SET = "C:\\ai\\djl\\pistachio_test";
        File[] files = FactoryUtils.getFileListInFolderForImages(DATA_SET + "/open");
        for (int i = 0; i < files.length; i++) {
            TRoi roi = CMatrix.getInstance()
                    .imread(files[i].getAbsolutePath())
                    .rgb2gray()
                    //.imshow()
                    .getROIofWeightCenteredObject(40, 20, 1);
            System.out.println("roi = " + roi);
            CMatrix cm2 = CMatrix.getInstance()
                    .imread(files[i].getAbsolutePath())
                    .cmd(roi.pr + ":" + (roi.pr + roi.height), roi.pc + ":" + (roi.pc + roi.width))
                    //.imshow()
                    .imresize(224, 224)
                    //.imshow()
                    .imsave("C:\\ai\\djl\\resized_pistachio_test\\open\\"+files[i].getName())
                    ;
        }
        
        
//        CMatrix cm = CMatrix.getInstance().tic().imread(DATA_SET + "/open/282493955742600.jpg")
//                .rgb2gray()
//                //.cmd("20:460", "100:540")
//                .getWeightCenteredROI(40, 20, 1)
//                .imshow();
//        String cmd_str = roi.pr + ":" + roi.height + "," + roi.pc + ":" + roi.width;
//        System.out.println("cmd_str = " + cmd_str);
//        final String DATA_SET = "C:\\ai\\djl\\resized_pistachio_test";
//        File[] files=FactoryUtils.getFileListInFolderForImages(DATA_SET+"/open");
//        for (int i = 0; i < files.length; i++) {
//            CMatrix cm = CMatrix.getInstance()
//                    .imread(files[i].getAbsolutePath())
//                    .imresize(128,128)
//                    .imsave("C:\\ai\\djl\\resized_pistachio_test\\open\\"+files[i].getName())
//                    ;
//        }
    }

    private static void trainPistachioWithResNet() {
        final int NUM_CHANNEL = 3;
        final int IMAGE_WIDTH = 224;
        final int IMAGE_HEIGHT = 224;
        final int NUM_OUTPUT = 2;
        final int EPOCH = 30;
        final int BATCH_SIZE = 32;
        final int TRAIN_RATIO = 10;
        final int VAL_RATIO = 0;
        final String MODEL_NAME = "pistachio_resnet";
        final String DATA_SET = "C:\\ai\\djl\\resized_pistachio_train";

        Block block = FactoryDJL.getResNetBlock(NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, 50);
        CMatrix cm = CMatrix.getInstance()
                .setModelForTrain(MODEL_NAME, NUM_CHANNEL, IMAGE_WIDTH, IMAGE_HEIGHT, NUM_OUTPUT, TBlockType.ResNetV50, block)
                .trainDataSet(DATA_SET, BATCH_SIZE, TRAIN_RATIO, VAL_RATIO, EPOCH);
    }

    private static void predictPistachioWithResNet() {
        final String MODEL_PATH = "models/pistachio_resnet/pistachio_resnet-0030.params";
        final String IMAGE_PATH = "C:\\ai\\djl\\resized_pistachio_test\\close";

        File[] files = FactoryUtils.getFileListInFolderForImages(IMAGE_PATH);

        CMatrix cm_gender = CMatrix.getInstance().setModelForInference(MODEL_PATH);
        int k = 0;
        int t = files.length;
        for (int i = 0; i < files.length; i++) {
            long t1 = FactoryUtils.tic();
            String str = cm_gender
                    .imread(files[i].getAbsolutePath())
                    .predictWithLabel();
            t1 = FactoryUtils.toc(t1);
            
            System.out.println("predicted class:" + str);
            if (str.equals("close")) {
                k++;
            }
        }
        double accuracy = 1.0 * k / t * 100;
        System.out.println("accuracy = " + accuracy);
    }
}
