/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author hakmesyo
 */
public class TestHistogram {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan1.jpg").imshow("kaplan resmi").rgb2gray().imhist();
        CMatrix cm2 = CMatrix.getInstance().imread("images\\kaplan1.jpg").rgb2gray().matrix(":").hist();
        
//        CMatrix cm1 = CMatrix.getInstance().randn(200000,1).scale(1000);
//        CMatrix cm2 = cm1.scale(0.5).addScalar(2000);
//        CMatrix cm3 = cm2.scale(1.89).addScalar(1000);
//        CMatrix cm4 = cm3.addScalar(4000);
//        CMatrix cmx=cm1.cat(1, cm2).cat(1, cm3).cat(1, cm4).hist(100);
        
//        CMatrix cm = CMatrix.getInstance().randn(100,1).bar();
    }
    
}
