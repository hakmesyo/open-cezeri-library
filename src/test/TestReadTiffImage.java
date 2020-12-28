/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.gui.FrameImage;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author cezerilab
 */
public class TestReadTiffImage {

    public static void main(String[] args) {
        BufferedImage img = ImageProcess.imread("C:/deneme.tif");
        int w = img.getWidth();
        int h = img.getHeight();
        System.out.println("w = " + w);
        System.out.println("h = " + h);

//        FrameImage frm=new FrameImage();
//        frm.setImage(ImageProcess.readImage("C:\\deneme.tif"));
//        frm.setVisible(true);
//        CMatrix cm = CMatrix.getInstance()
//                .imread("C:/deneme.tif")
//                .imshow()
//                ;
    }
}
