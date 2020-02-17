/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.classifiers.dl4c;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class Test_dl4c {

    public static void main(String[] args) {
        int n=10000;
        for (int i = 0; i < 5; i++) {
            CMatrix cm = CMatrix.getInstance()
                    .tic()
                    .rand(n,n)
                    .toc()
                    .pow(3)
                    .toc();
            System.out.println("");
        }
    }
}
