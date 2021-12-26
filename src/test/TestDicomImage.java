/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import java.awt.image.BufferedImage;

public class TestDicomImage {
    public static void main(String[] args) {
        BufferedImage img=ImageProcess.convertDicomToBufferedImage("images/MASS_056_L_MLO.dcm");
        ImageProcess.saveImage(img, "images/test.png");
    }
}
