/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author BAP1
 */
public class Test4KImage {

    public static void main(String[] args) {
//        BufferedImage
//        CMatrix cm=CMatrix.getInstance().imread("images\\leopard4k.jpeg")
//        CMatrix cm=CMatrix.getInstance().imread("images\\kaplan1.jpg")
        CMatrix cm = CMatrix.getInstance().imread("images\\pullar.png")
                .imshow("rgb")
                .imhist("rgb")
                .rgb2hsv()
                .imshow("hsv")
                .im2bw()
                .imshow("binary")
//                .rgb2hsv()
//                .imshow("hsv")
//                .rgb2gray()
//                .imshow("gray")
//                .im2bw(185,195)
//                .imshow("binary")
//                .imwrite("images\\output.png")
                //                .imhist("pullar rgb")                
                //                .imhistRed("red channel histogram")
                //                .imhistGreen("green channel histogram")
                //                .imhistBlue("blue channel histogram")
//                .rgb2hsv()
//                .imshow("hsv")
//                .hsv2rgb()
//                .imshow("rgb yeniden") 
//                .imhist("hsv")
                //                .rgb2gray()
                //                .imshow("gray")
                //                .toNewColorSpace(BufferedImage.TYPE_3BYTE_BGR)
                //                .imshow("rgb")
                //                .imhist()
                ;
    }
}
