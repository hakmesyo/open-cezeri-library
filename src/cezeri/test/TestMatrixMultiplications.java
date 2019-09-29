/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestMatrixMultiplications {
    public static void main(String[] args) {
//        double[][] m1={{1,2},{1,1},{3,1}};
//        double[][] m2={{1,0,3},{2,1,2}};
//        CMatrix cm1 = CMatrix.getInstance(m1);
//        CMatrix cm2=CMatrix.getInstance(m2);
//        CMatrix cm3 = cm1.times(cm2).println();
        
//        double[][] m1={{1,2,1},{-1,3,9},{4,5,12}};
//        double[] m2={11,9,-3};
//        CMatrix cm1 = CMatrix.getInstance(m1);
//        CMatrix cm2=CMatrix.getInstance(m2).transpose();
//        CMatrix cm3 = cm1.times(cm2).println();
        
        //test inverse and pseudoinverse matrix
        double[][] m1={{1,2,1},{-1,3,9},{4,5,12},{2,0,3}};
        double[] m2={-11,9,-3};
        CMatrix cm1 = CMatrix.getInstance(m1).println();
        CMatrix inv = CMatrix.getInstance(m1).inv().println();
        CMatrix cmm = cm1.times(inv).println();
        CMatrix cm2=CMatrix.getInstance(m2).transpose();
        CMatrix cm3 = inv.times(cm2).println();
    }
}
