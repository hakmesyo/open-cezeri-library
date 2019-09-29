/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestLBP {
    public static void main(String[] args) {
        CMatrix cm_1 = CMatrix.getInstance().vector(-360, 1, 360).multiplyScalar(0.5).addNoise(2).toRadians().cos();
        CMatrix cm_2 = CMatrix.getInstance().vector(-360, 1, 360).multiplyScalar(3).toRadians().sin().divideScalar(10);
        CMatrix cm_3 = cm_1.add(cm_2).plot();
        CMatrix cm_4 = cm_3.getLBP1D(8,true).plot();
//        CMatrix cm_5 = cm_3.getLBP1D(8,false).plot();
    }
}
