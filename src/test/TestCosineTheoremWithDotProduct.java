/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestCosineTheoremWithDotProduct {
    public static void main(String[] args) {
        //buraya bak: https://www.youtube.com/watch?v=Dd16LVt5ct4
        CMatrix cm1 = CMatrix.getInstance(new double[]{3,2,0,5,0,0,0,2,0,0});
        CMatrix cm2 = CMatrix.getInstance(new double[]{3,2,0,5,0,0,0,1,0,2});
        CMatrix cm3 = cm1.cosineSimilarity(cm2).println();
        
        double[][] d1={{1,2,3},{4,5,6},{7,8,9}};
        CMatrix cm_1 = CMatrix.getInstance(d1);
        double[][] d2={{9,8,7},{6,5,4},{3,2,1}};
        CMatrix cm_2 = CMatrix.getInstance(d2);
//        CMatrix cm3 = cm1.getAngle(cm2).println();
        //cm1.dotProduct(cm2).println();
        //cm_1.dot(cm_2).println();
//        cm_1.times(cm_2).println();
        CMatrix cm = CMatrix.getInstance(new double[]{1,2,3}).dotProduct(CMatrix.getInstance(new double[]{1,2,3})).println();
    }
}
