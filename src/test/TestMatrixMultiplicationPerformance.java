/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;

/**
 *
 * @author elcezerilab
 */
public class TestMatrixMultiplicationPerformance {
    public static void main(String[] args) {
        int n=10000;
//        CMatrix.getInstance()
//                .tic()
//                .zeros(n,n)
////                .multiplyElement(CMatrix.getInstance().rand(n,n))
//                .toc("processing time=")
//                ;
//        CMatrix.getInstance()
//                .tic()
//                .rand(n,n)
////                .multiplyElement(CMatrix.getInstance().rand(n,n))
//                .toc("processing time=")
//                ;
        long t1=FactoryUtils.tic();
        double[][] d=new double[n][n];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                d[i][j]=Math.random();
//            }
//        }
        t1=FactoryUtils.toc(t1);

    }
}
