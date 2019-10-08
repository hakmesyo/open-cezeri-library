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
public class TestProbabilityDensityFunction {
    public static void main(String[] args) {
//        CMatrix cm=CMatrix.getInstance().imread("images\\kaplan2.jpg").imshow().imhist().rgb2gray().getHistogramData().plot();
        CMatrix cm2=CMatrix.getInstance().imread("images\\kaplan2.jpg").rgb2gray().imshow().getPDFData().plot();
    }
}
