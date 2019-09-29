/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;

/**
 * This test deals with imnoise or addNoise methods
 * @author BAP1
 */
public class TestImageNoise {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images\\kaplan1.jpg")
                .rgb2gray()
                .imshow()
                .imnoise(0,0.2)
                .imshow();
//        CMatrix cmNoise=CMatrix.getInstance().randn(cm.getRowNumber(),cm.getColumnNumber(),0,255).abs().round();
//        cm.add(cmNoise).imshow(true);
    }
}
