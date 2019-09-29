/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestDCT {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images//eagle.jpg")
                .imshow()
                .rgb2gray()
                .imshow()
                .resize(512,512)
                .imshow()
                .transformDCT()
                .imshow();
    }
}
