/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.RasterToVector;
import cezeri.matrix.CMatrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author BAP1
 */
public class TestSVG {
    public static void main(String[] args) {
//        RasterToVector rtv=new RasterToVector("images\\kaplan1.jpg");
//        rtv.convertToSVG("images\\kaplan1.svg");
        RasterToVector rtv=new RasterToVector("images\\blob.jpg");
        rtv.convertToSVG("images\\blob.svg");
        
    }
}
