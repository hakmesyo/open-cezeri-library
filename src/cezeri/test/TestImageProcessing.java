/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.gui.FrameImage;
import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author BAP1
 */
public class TestImageProcessing {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .tic()
                .imread("images\\pullar.png")  
                .imshow("rgb")
                .rgb2gray()
                .imshow("gray")
//                .imhist("rice")
//                .binarizeOtsu()
//                .imshow("otsu")
//                .edgeDetectionCanny().imshow("canny")
//                .drawRect(30, 30, 100, 100,4, Color.yellow)
//                .fillRect(30, 30, 100, 100, Color.yellow)
//                .drawLine(30, 30, 100, 100,20,Color.yellow)
                .drawOval(30, 30, 300, 100,5,Color.yellow)
                .imshow("drawed rect")
                .rgb2gray()
                .imshow("gray")
//                .prev().prev().prev().imcomplement().imshow("reverted image")                
                .toc()
                ;
    }
}
