/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;
import org.opencv.core.Core;

/**
 *
 * @author BAP1
 */
public class TestFaceDetection {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //links https://sites.google.com/site/rachelmanoni/home/face-detection/viola-and-jones-face-detection
        
//        CMatrix cm = CMatrix.getInstance().imread("images\\yuzler.jpg").detectFaces("haar").imshow();
//        CMatrix cm = CMatrix.getInstance().imread("images\\yuzler_2.jpg").detectFaces("haar").imshow();
        CMatrix cm = CMatrix.getInstance().imread("images\\yuzler_3.jpg").detectFaces("haar").imshow();
    }
}
