/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.h2d_mlp;

import cezeri.factory.FactoryUtils;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cezerilab
 */
public class TrainDeep2DMLP {

    public static int EPOCHS = 50;
    public static int BATCH_SIZE = 50;
    public static double LEARNING_RATE = 0.01;
    public static int INPUT_NODES = 784;
    public static int OUTPUT_NODES = 10;
    public static int IMG_WIDTH = 28;
    public static int IMG_HEIGHT = 28;
    public static int NUM_CHANNEL = 1;

    public static void main(String[] args) {
//        prepareDataSet();
        String path_train = "C:\\ai\\djl\\mnist\\train";
        String path_test = "C:\\ai\\djl\\mnist\\test";

        
        LinkedHashMap<String, List<String>> tr = Datasets.loadData(path_train, IMG_WIDTH, IMG_HEIGHT, NUM_CHANNEL);
        List<String> X_train = tr.get("X");
        List<String> y_train = tr.get("Y");
        LinkedHashMap<String, List<String>> tst = Datasets.loadData(path_test, IMG_WIDTH, IMG_HEIGHT, NUM_CHANNEL);
        List<String> X_test = tst.get("X");
        List<String> y_test = tst.get("Y");

        MlpModel model = new MlpModel("Model_1", IMG_WIDTH, IMG_HEIGHT, NUM_CHANNEL, new Random(111));
        model = model.addLayer(new MlpLayer(model, LayerType.input, ActivationType.sigmoid));
        model = model.addLayer(new MlpLayer(model, LayerType.hidden, ActivationType.sigmoid));
        model = model.addLayer(new MlpLayer(model, LayerType.hidden, ActivationType.sigmoid));
        model = model.addLayer(new MlpLayer(model, LayerType.hidden, ActivationType.sigmoid));
        model = model.addLayer(new MlpLayer(model, LayerType.hidden, ActivationType.sigmoid));
        model = model.addLayer(new MlpLayer(model, LayerType.output, OUTPUT_NODES, ActivationType.softmax));
        model = model.compile();
        model.summary();
//        model.layers.get(0).channels.get(0).nodes[0][0].dump().nextNode.dump().nextNode.dump().nextNode.dump().nextNode.dump();
//        model.layers.get(0).channels.get(0).nodes[2][2].traceForward();

        model.fit(X_train, y_train, LEARNING_RATE, EPOCHS, BATCH_SIZE);
    }

    private static void prepareDataSet() {
        String path_train = "C:\\ai\\djl\\mnist\\train";
        String path_test = "C:\\ai\\djl\\mnist\\test";
        FactoryUtils.makeDirectory("C:\\ai\\djl\\mnist");
        FactoryUtils.makeDirectory("C:\\ai\\djl\\mnist\\train");
        FactoryUtils.makeDirectory("C:\\ai\\djl\\mnist\\test");

        String p1 = "C:\\ai\\djl\\mnist_train";
        File[] dirs = FactoryUtils.getDirectories(p1);
        Random rnd = new Random(111);
        for (File dir : dirs) {
            FactoryUtils.makeDirectory(path_train + "/" + dir.getName());
            File[] files = FactoryUtils.getFileListInFolderForImages(p1 + "/" + dir.getName());
            files = FactoryUtils.shuffle(files, rnd);
            for (int i = 0; i < 100; i++) {
                FactoryUtils.copyFile(files[i], new File(path_train + "/" + dir.getName() + "/" + files[i].getName()));
            }
        }

        String p2 = "C:\\ai\\djl\\mnist_test";
        dirs = FactoryUtils.getDirectories(p2);
        for (File dir : dirs) {
            FactoryUtils.makeDirectory(path_test + "/" + dir.getName());
            File[] files = FactoryUtils.getFileListInFolderForImages(p2 + "/" + dir.getName());
            files = FactoryUtils.shuffle(files, rnd);
            for (int i = 0; i < 100; i++) {
                FactoryUtils.copyFile(files[i], new File(path_test + "/" + dir.getName() + "/" + files[i].getName()));
            }
        }

    }
}
