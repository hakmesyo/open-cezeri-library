/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;

/**
 *
 * @author BAP1
 */
public class TestBitPlaneSlicing {

    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan1.jpg").rgb2gray().imshow();
                cm.bitPlaneMSB().imshow("MSB image");
                cm.bitPlane(7).add(cm.bitPlane(6)).add(cm.bitPlane(5)).imshow("first three msb image");
                cm.bitPlane(0).imshow("first slice image");
                cm.bitPlane(1).imshow("second slice image");
                cm.bitPlane(2).imshow("third slice image");
                cm.bitPlane(3).imshow("forth slice image");
                cm.bitPlane(4).imshow("fifth slice image");
                cm.bitPlane(5).imshow("sixth slice image");
                cm.bitPlane(6).imshow("seventh slice image");
                cm.bitPlane(7).imshow("eighth slice image the most significant bit");

//        int a = 23;
//        System.out.println("a = " + Integer.toBinaryString(a));
//        System.out.println("a = " + FactoryUtils.formatBinary(8,a));
//        for (int i = 0; i < 8; i++) {
//            char c=Integer.toBinaryString(a).toCharArray()[i];
//            System.out.print(c);
//        }
//        System.out.println("");

    }

    private static String formatBinary(int n,int p) {
        char[] chars = new char[n];
        for (int j = 0; j < n; j++) {
            chars[j] = (char) (((p >>> (n - j - 1)) & 1) + '0');
        }
        System.out.println(chars);
        return String.valueOf(chars);
    }
}
