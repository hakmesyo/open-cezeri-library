/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author BAP1
 */
public class TestOverlayImages {

    public static void main(String[] args) {
        CMatrix cm1 = CMatrix.getInstance().imread("images\\horoz.jpg").imresize(550,400);
        CMatrix cm2 = CMatrix.getInstance().imread("images\\pullar.png");
        CMatrix cm3 = CMatrix.getInstance().imread("images\\kaplan1.jpg");
        CMatrix cm4 = cm1.overlay(cm2,0.3f).overlay(cm3, 0.7f).imshow();
        
//        BufferedImage img1 = cm1.getImage();
//        BufferedImage img2 = cm2.getImage();
//        BufferedImage img3 = ImageProcess.overlayImage(img1,img2,0.33f);
//        CMatrix cm = CMatrix.getInstance(img3).imshow();
        
    }
}
