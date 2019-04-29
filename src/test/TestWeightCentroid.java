/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author BAP1
 */
public class TestWeightCentroid {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images\\yaprak.jpg");
        CMatrix cm1=cm
                .rgb2gray()
                .imshow()
                .binarizeOtsu()
                .imcomplement()
                .imshow()      
//                .edgeDetectionCanny()
//                .imshow()
                
                ;
        
        //Aşağıdaki kod bloğu binary bir imgenin merkezini bulmaya yarar        
        double[][] d=cm1.toDoubleArray2D();
        int total=0;
        int sumX=0;
        int sumY=0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j]>0) {
                    sumY+=i;
                    sumX+=j;
                    total++;
                }
                
            }
        }
        int cX=sumX/total;
        int cY=sumY/total;
        
        CMatrix cm2=cm.drawRect(cY-10, cX-10, 20, 20,1, Color.red).imshow("Center of the image");
    }
}
