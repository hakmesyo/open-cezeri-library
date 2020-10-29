/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.types.TMatrixOperator;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class TestSil_2 {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images/cezeri_logo.bmp")
                .rgb2gray()
                //.imshow()
                .println()
                .findIndex(TMatrixOperator.EQUALS, 0)
                .println()
                ;
        int[] d=cm.toIntArray1D();
        int[] satirlar=new int[d.length];
        int[] sutunlar=new int[d.length];
        for (int i = 0; i < d.length; i++) {
            satirlar[i]=d[i]/84;
            sutunlar[i]=d[i]%84;
        }
        System.out.println(Arrays.toString(satirlar));
        System.out.println(Arrays.toString(sutunlar));
        System.out.println(1894/84);
        System.out.println(1894%84);
    }
   
}
