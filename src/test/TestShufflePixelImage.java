/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author cezerilab
 */
public class TestShufflePixelImage {
    public static void main(String[] args) {
        CMatrix cm=CMatrix.getInstance()
                .imread("images/kaplan.jpg")
//                .rgb2gray()
                .imshow("original")
//                .shufflePixelImage()
//                .updateImage()
//                .imshow("shuffled")
//                .updateImage()
//                .deShufllePixelImage()
//                .imshow("recovered image")
//                
                ;
    }
}
