/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class TestGaussianMF {
    public static void main(String[] args) {
        CMatrix x=CMatrix.getInstance().vector(-30,30);
        CMatrix cm1=CMatrix.getInstance().vector(-30,30).gaussmf(10,0);
        CMatrix cm2=CMatrix.getInstance().vector(-30,30).gaussmf(5,-3);
        CMatrix cm3=CMatrix.getInstance().vector(-30,30).gaussmf(7,10).println();
        CMatrix cm=cm1.cat(1, cm2).cat(1, cm3).println();
        cm.plot(x.toDoubleArray1D());
    }
    
}
