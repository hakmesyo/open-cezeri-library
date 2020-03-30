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
public class TestFFT {
    
    public static void main(String[] args) {
        //for 1D signal
        CMatrix cm=CMatrix.getInstance().linspace(0,3600, 1024).sin().addNoise(2).plot().
                transformFFT().plot();
        //for 2d signal or image
        CMatrix cm2 = CMatrix.getInstance().imread("images//kaplan2.jpg").imshow().imresize(512,512).imshow()
                .transformFFT().plot();
    }
}
