/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;
import cezeri.matrix.FactoryMatrix;
import cezeri.utils.FactoryUtils;

/**
 *
 * @author BAP1
 */
public class TestCrossValidationSet {
    public static void main(String[] args) {
        long t1=FactoryUtils.tic();
        double[][] d=FactoryMatrix.matrixDoubleRandom(10000,10000, 0,255);
        t1=FactoryUtils.toc(t1);
//        FactoryMatrix.toString(d);
//        CMatrix cm = CMatrix.getInstance().rand(1000,320*240,0,100);
//        CMatrix[][] cv=cm.crossValidationSets(5);
//        for (int i = 0; i < cv.length; i++) {
//            cv[i][0].println(i+".fold train");
//            cv[i][1].println(i+".fold test");
//        }
    }
}
