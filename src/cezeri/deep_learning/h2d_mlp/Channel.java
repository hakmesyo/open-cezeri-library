/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.h2d_mlp;

import cezeri.matrix.CMatrix;
import java.util.Random;

/**
 *
 * @author DELL LAB
 */
public class Channel {
    Node[][] nodes;
    Bias bias;
    int nchannels;
    int nrows;
    int ncols;
    int size;
    ActivationType activationType;
    LayerType layerType;
    public Random rnd;
    MlpLayer layer;
    MlpLayer prevLayer;
    MlpLayer nextLayer;

    public Channel(MlpLayer layer,int nrows,int ncols,int nch) {
        this.layer=layer;
        this.activationType=layer.activationType;
        this.layerType=layer.layerType;
        this.nrows=nrows;
        this.ncols=ncols;
        this.nchannels=nch;
        this.nodes = prepareLayer();
        this.rnd=layer.rnd;
        this.bias=new Bias(Utils.getRandomWeight(this.rnd));
    }
    
    private Node[][] prepareLayer() {
        nodes = new Node[nrows][ncols];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                nodes[i][j] = new Node(this, i, j);
            }
        }
        return nodes;
    }

    public void feed(double[][] d) {
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                nodes[i][j].data = d[i][j] / 255.0;
            }
        }
    }

    public double applyActivation(double sum) {
        if (activationType == ActivationType.sigmoid) {
            return sigmoid(sum);
        } else if (activationType == ActivationType.relu) {
            return relu(sum);
        } else {
            return -1.0;
        }
    }

    public double applyActivation(double sum, ActivationType type) {
        if (type == ActivationType.sigmoid) {
            return sigmoid(sum);
        } else if (type == ActivationType.relu) {
            return relu(sum);
        } else {
            return -1.0;
        }
    }

    private double sigmoid(double x) {
        double ret = 1 / (1 + Math.pow(Math.E, -1 * x));
        return ret;
    }
    
    public double sigmoidDerivative(double x){
        double ret=sigmoid(x)*(1-sigmoid(x));
        return ret;
    }

    private double relu(double x) {
        double ret = Math.max(0, x);
        return ret;
    }
    
    private double reluDerivative(double x) {
        double ret = 0;
        if (x>0) {
            ret=1;
        }
        return ret;
    }

    public void softmax() {
        double exp_z_sum = 0.0;
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                exp_z_sum += Math.pow(Math.E, nodes[i][j].data);
            }
        }
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                nodes[i][j].data = Math.pow(Math.E, nodes[i][j].data) / exp_z_sum;
            }
        }
    }

    public void streamData() {
        System.out.println("");
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                System.out.println(nodes[i][j].data);
            }

        }
    }

    public double[][] toArray2D() {
        double[][] d = new double[nrows][ncols];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                d[i][j] = nodes[i][j].data;
            }
        }
        return d;
    }

    public double getLossFromAbsDifference(double[] y) {
        double ret = 0;
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                ret += Math.abs(nodes[i][j].data - y[i]);
            }
        }
        return ret;
    }

    public double getLoss() {
        double ret = CMatrix.getInstance(toArray2D()).sumTotal();
        return ret;
    }

    public double[][] getWeights() {
        /*
        double[][] ret;
        if (nextLayer.layerType == LayerType.output) {
            ret = new double[size][nextLayer.size];
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Node node = nodes[i][j];
                    ret[i*ncols+j] = node.weightsToOutputLayer;
                }
            }
        } else {
            ret = new double[1][size];
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    Node node = nodes[i][j];
                    ret[0][i*ncols+j] = node.weight;
                }
            }
        }
        return ret;
*/
        return null;
    }
    
}
