/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.matrix.CRectangle;
import java.awt.Color;

/**
 *
 * @author cezerilab
 */
public class TestFaceLocation {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images/yuzler_3.jpg")               
                ;
        //cm.detectFaces("haar").imshow();
        CRectangle[] rects=cm.getFaceLocationsAsCRectangle("haar");
        for (int i = 0; i < rects.length; i++) {
            cm=cm.drawRect(rects[i], 5, Color.yellow);
        }
        cm.imshow();
    }
}
