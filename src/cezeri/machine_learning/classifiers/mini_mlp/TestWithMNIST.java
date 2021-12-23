/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.classifiers.mini_mlp;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cezerilab
 */
public class TestWithMNIST {

    public static void main(String[] args) {
        DataSet train_ds = getDS("images/mnist/train");
        DataSet test_ds = getDS("images/mnist/test");
        
        double[][] train_inp=train_ds.input;
        double[][] train_out=train_ds.output;
        NeuralNetwork nn = new NeuralNetwork(train_inp[0].length, 500, 1);
        nn.fit(train_inp, train_out, 10);
        
        double[][] test_inp=test_ds.input;
        double[][] test_out=test_ds.output;
        List<Double> output;
        for (double d[] : train_inp) {
            output = nn.predict(d);
            System.out.println(output.toString());
        }
    }

    private static DataSet getDS(String path) {
        File[] dirs = FactoryUtils.getDirectories(path);
        List<double[]> lst_input = new ArrayList();
        List<Double> lst_output = new ArrayList();
        for (File dir : dirs) {
            File[] files = FactoryUtils.getFileListInFolderForImages(dir.getAbsolutePath());
            for (File file : files) {
                CMatrix cm = CMatrix.getInstance()
                        .imread(file)
                        .rgb2gray();
                double[] d = cm.toDoubleArray1D();
                lst_input.add(d);
                lst_output.add(0.0+Double.parseDouble(dir.getName())/dirs.length);
            }

        }
        double[][] inp = new double[lst_input.size()][];
        lst_input.toArray(inp);
        double[][] out = new double[lst_output.size()][1];
        int k = 0;
        for (Double d : lst_output) {
            out[k++][0] = d;
        }
        return new DataSet(inp, out);
    }

}

class DataSet {

    double[][] input;
    double[][] output;

    public DataSet(double[][] inp, double[][] out) {
        this.input = inp;
        this.output = out;
    }
}
