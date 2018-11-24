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
public class TestColorSpace {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\pullar.png")
                .imshow("rgb")
                .imhist("rgb")
                .rgb2gray()
                .imshow("gray")
                .imhist("gray")
                .prev(3)
                .rgb2hsv()
                .imshow("hsv")
                .imhist("hsv")
                .rgb2gray()
                .imshow("gray of hsv")
                .imhist("gray of hsv")
//                .im2bw()
//                .imshow("binary")
                .threshold(90,240)
                .imshow("thresholded image")
                
                ;
    }
}
