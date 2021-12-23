/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.classifiers.neural_net;

import cezeri.matrix.CMatrix;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 *
 * @author cezerilab
 */
public class TestMnist {

    public static int TRAINING_SAMPLES = 250;
    public static int EPOCHS = 50;
    public static double LEARNING_RATE = 0.01;
    public static int INPUT_NODES = 784;
    public static int OUTPUT_NODES = 10;

    public static void main(String[] args) {
//        CMatrix cm = CMatrix.getInstance()
//                .range(0, 3000)
//                .sigmoid()
//                .plot()
//                
//                ;
//        String path_train = "C:\\ai\\djl\\mnist_train";
//        String path_test = "C:\\ai\\djl\\mnist_test";
        String path_train = "C:\\ai\\djl\\mnist\\train";
        String path_test = "C:\\ai\\djl\\mnist\\test";

        LinkedHashMap<String, double[][][]> tr = Datasets.loadMnistData(path_train);
        double[][][] X_train = tr.get("X");
        double[][][] y_train = tr.get("Y");
        LinkedHashMap<String, double[][][]> tst = Datasets.loadMnistData(path_test);
        double[][][] X_test = tst.get("X");
        double[][][] y_test = tst.get("Y");

        Model model = new Model();
        model.addLayer(new Layer(INPUT_NODES));
        model.addLayer(new Layer(50));
        model.addLayer(new Layer(20));
        model.addLayer(new Layer(OUTPUT_NODES));
        model.summary();
//        System.exit(0);
        //training
        model.fit(X_train, y_train, LEARNING_RATE, EPOCHS);

        //testing
        double acc = 0;
        double err = 0;
        double perf = 0;
        for (int i = 0; i < X_test.length; i++) {
            Prediction pred = model.predict(X_test[i], y_test[i], i + 1);
            //System.out.println(pred.toString());
            if (pred.hit) {
                acc++;
                System.out.print(i + ". hit");
            } else {
                err++;
                System.out.print(i + ". err");
            }
            perf = acc / (acc + err) * 100;
            System.out.println(" acc=" + perf);
        }

        System.out.println("test accuracy = " + perf + " %");
    }

}
