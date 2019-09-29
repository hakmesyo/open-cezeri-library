/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import cezeri.matrix_processing.CMatrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author BAP1
 */
public class TestDilateErodeOperations {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan1.jpg")
                .imshow()
                .rgb2gray()
                .binarizeOtsu()
//                .edgeDetectionCanny()
                .imshow()                
                ;
        
        BufferedImage img=cm.getImage();
        BufferedImage img1=ImageProcess.dilate(img);
        CMatrix cm2 = CMatrix.getInstance(img1).imshow("after dilate operations");
        BufferedImage img2=ImageProcess.erode(img);
        CMatrix cm3 = CMatrix.getInstance(img2).imshow("after erode operations");
    }
}
