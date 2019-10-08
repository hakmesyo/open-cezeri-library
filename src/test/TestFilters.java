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
public class TestFilters {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan1.jpg").imresize(400, 400).rgb2gray().imshow("original");
        CMatrix cm1 = cm.filterMean(5).imshow("mean filter");
        CMatrix cm2 = cm.filterMedian(3).imshow("median filter");
        CMatrix cm3 = cm.filterGaussian(3).imshow("gaussian filter");
        
    }
}
