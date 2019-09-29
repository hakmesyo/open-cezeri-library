/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;
import javax.swing.GrayFilter;

/**
 *
 * @author BAP1
 */
public class TestHSV {

    public static void main(String[] args) {

        CMatrix cm = CMatrix.getInstance()
                .imread("images/pullar.png")
                .imshow();

        CMatrix cm2=cm.rgb2hsv().imshow().rgb2gray().imshow();

        CMatrix cm3=cm.toGrayLevel().imshow();
        
        CMatrix cm4 = cm2.multiplyElement(cm3).imshow();
    }
}
