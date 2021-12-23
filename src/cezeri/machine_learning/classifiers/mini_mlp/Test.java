/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * see form this link
 * https://towardsdatascience.com/understanding-and-implementing-neural-networks-in-java-from-scratch-61421bb6352c
 */

package cezeri.machine_learning.classifiers.mini_mlp;

import java.util.List;

public class Test {

    static double[][] X = {
        {0, 0},
        {1, 0},
        {0, 1},
        {1, 1}
    };
    static double[][] Y = {
        {0}, {1}, {1}, {0}
    };

    public static void main(String[] args) {

        NeuralNetwork nn = new NeuralNetwork(2, 7, 1);

        List<Double> output;

        nn.fit(X, Y, 100000);
        double[][] input = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1},{0, 0}
        };
        for (double d[] : input) {
            output = nn.predict(d);
            System.out.println(output.toString());
        }

    }

}
