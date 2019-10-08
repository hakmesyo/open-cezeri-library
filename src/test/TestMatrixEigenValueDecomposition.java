/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import weka.core.matrix.Matrix;

/**
 *
 * @author BAP1
 */
public class TestMatrixEigenValueDecomposition {
    public static void main(String[] args) {
        Matrix cm1 = CMatrix.getInstance().rand(3,3).toWekaMatrix();
        Matrix cm2=cm1.inverse();
        Matrix cm3=cm1.times(cm2);
        CMatrix cm4=CMatrix.getInstance().fromWekaMatrix(cm3).println();
    }
    
}
