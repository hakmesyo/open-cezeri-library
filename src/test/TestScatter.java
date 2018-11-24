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
public class TestScatter {
    public static void main(String[] args) {
        CMatrix cm1 = CMatrix.getInstance().rand(1,100).timesScalar(100).transpose();
        CMatrix cm2 = cm1.jitter(5);
        CMatrix cm3 = cm1.cat(1, cm2).plot();
    }
}
