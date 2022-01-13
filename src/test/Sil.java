/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author cezerilab
 */
public class Sil {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .range(0,720)
                .perlinNoise(0.03)
//                .toRadians()
//                .sin()
                .plot()
                ;
        CMatrix cm1 = cm.addScalar(5).jitter(0.5).cat(1, cm).plot();
//        double n=-1.57E6;
//        System.out.println("n = " + n);
//        CMatrix cm = CMatrix.getInstance()
//                .randn(500,20)
//                .getHistogramData(30)
//                .plot()
//                //.bar()
//                ;
//        double a=n*6;
//        System.out.println("a = " + a);
    }
}
