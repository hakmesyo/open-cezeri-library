/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author cezerilab
 */
public class TestAdaptiveThreshold {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images/pistachio/fst_36_3.jpg")
                //.drawLine(0, 100, 300, 100, 3, Color.yellow)
                .imshow("original")
                .rgb2gray()
                //.threshold(45)
                //.threshold(45,110)
                //.thresholdOtsu()
                .binarizeOtsu()
                .imshow("after threshold")
                //.drawLine(0, 0, 300, 0, 3, Color.white)
                
                .imshow()
                ;
    }
}
