/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author elcezerilab
 */
public class TestShuffleImage {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
//                .range("-360:360:5")
//                .toRadians()
//                .sin()
//                .map(0, 255)
//                .replicateColumn(720/5)
//                .imshow()
//                .shape()
                .imread("images/kaplan1.jpg")
//                .imread("images/rice.png")
//                .rgb2gray()
                .imshow()
                .shufflePixelImage()
                .imshow(true)
                .deShufllePixelImage()
                .imshow(true)
                ;
    }
}
