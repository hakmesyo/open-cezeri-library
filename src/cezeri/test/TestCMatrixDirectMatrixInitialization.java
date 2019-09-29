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
public class TestCMatrixDirectMatrixInitialization {
    public static void main(String[] args) {
        CMatrix A = CMatrix.getInstance(new int[][]{{-4,2,1},{1,6,2},{1,-2,5}}).dump();
        CMatrix b = CMatrix.getInstance(new int[]{-4,23,19}).dump();
        CMatrix x = A.inv().times(b).dump();
        CMatrix xJac = CMatrix.getInstance().jacobianApproximation(A,b,5E-5).dump();
        CMatrix xJac2 = A.jacobianApproximation(b,5E-5).dump();
//        System.out.printf("n:%e\n",3E-2);
    }
}
