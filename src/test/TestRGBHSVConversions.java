/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;

/**
 *
 * @author BAP1
 */
public class TestRGBHSVConversions {
    public static void main(String[] args) { 
        String str="images\\blob.jpg";
        CMatrix cmRGB=CMatrix.getInstance().imread(str).imshow();
//        CMatrix cmRed=CMatrix.getInstance().imread(str).getRedChannel().imshow();
//        CMatrix cmGreen=CMatrix.getInstance().imread(str).getGreenChannel().imshow();
//        CMatrix cmBlue=CMatrix.getInstance().imread(str).getBlueChannel().imshow();
//        CMatrix cmHSV=CMatrix.getInstance().imread(str).toHSV().imshow();
        CMatrix cmHue=CMatrix.getInstance().imread(str).getHueChannel().imshow();
//        CMatrix cmSaturation=CMatrix.getInstance().imread(str).getSaturationChannel().imshow();
//        CMatrix cmValue=CMatrix.getInstance().imread(str).getValueChannel().imshow();
    }    
}
