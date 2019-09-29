/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import cezeri.matrix_processing.CMatrix;

/**
 *
 * @author HP-pc
 */
public class TestArrayConversion {
    public static void main(String[] args){
        CMatrix cm=CMatrix.getInstance().tic().readImage("images\\horoz.jpg").toc("img read:").
                showImage().toc("show:").
                edgeDetectionCanny().toc("canny:").imshow().prev().imshow().
                edgeDetectionSobel(50).toc("sobel:").imshow().prev().
                edgeDetectionMusa(150).toc("musa:").imshow();
//        CMatrix cm=CMatrix.getInstance()
//                .tic()
//                .rand(1500,200).toc("rand:")
//                .eig().toc("eig:")
//                .entropy().toc("entropy:");
//        
//       CMatrix cm=CMatrix.getInstance(CMatrix.getInstance().
//               rand(5, 10).
//               //println().
//               to1DArrayDouble());
               //println().
               //toNewFile();
    //CMatrix cm2=CMatrix.getInstance().readFile("C:\\Users\\HP-pc\\Documents\\cm.txt");
    }
}
