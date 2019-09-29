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
public class TestCosineTheoremWithDotProduct {
    public static void main(String[] args) {
        double[] d1={-5,4};
        CMatrix cm1 = CMatrix.getInstance(d1);
        double[] d2={-13,2};
        CMatrix cm2 = CMatrix.getInstance(d2);
        CMatrix cm3 = cm1.getAngle(cm2).println();
//        CMatrix cm4 = cm1.dotProduct(cm2).println();
        
    }
}
