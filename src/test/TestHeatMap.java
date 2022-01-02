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
 * @author elcezerilab
 */
public class TestHeatMap {
    public static void main(String[] args) {
//        CMatrix cm2 = CMatrix.getInstance()
//                .imread("images/pullar.png")
//                .rgb2gray()
//                .imhist()
//                .transpose();
//        
//        cm2=    cm2.pow(2)
//                .timesScalar(3)
//                .minus(cm2.powerByScalar(4))
//                .head()
//                ;
//        CMatrix cm = CMatrix.getInstance()
//                .zeros(30,50)
//                .perlinNoise(0.5)
////                .rand()
////                .map(0, 255)
////                .round()
////                .heatmap()
////                .head()
////                .heatmap()
////                .heatmap(Color.decode("0xFFFF00"))
////                .heatmap(Color.RED)
//                .heatmap(Color.cyan)
//                
//                .imread("images/pullar.png")
//                .imshow()
//                .imhist()
//                .getRedChannelColor()
//                .rgb2gray()
//                .imshow()
//                .imhist()
//                .prevFirst()
//                .getGreenChannelColor()
//                .rgb2gray()
//                .imshow()
//                .imhist()
//                .prevFirst()
//                .getBlueChannelColor()
//                .rgb2gray()
//                .imshow()
//                .imhist()
//                .prevFirst()
//                
////                .rgb2gray()
////                .imshow()
////                .map(-100, 100)
////                .heatmap(true)
//                
//                
                ;
                
                
                double[][] d={
                        {0.77,-0.11,0.11,0.33,0.55,-0.11,0.33},
                        {-0.11,1,-0.11,0.33,-0.11,0.11,-0.11},
                        {0.11,-0.11,1.00,-0.33,0.11,-0.11,0.55},
                        {0.33,0.33,-0.33,0.55,-0.33,0.33,0.33},
                        {0.55,-0.11,0.11,-0.33,1.00,-0.11,0.11},
                        {-0.11,0.11,-0.11,0.33,-0.11,1.00,-0.11},
                        {0.33,-0.11,0.55,0.33,0.11,-0.11,0.77}
                };
                CMatrix cm = CMatrix.getInstance(d)
//                        .scatter()
//                        .plot()
//                        .heatmap(Color.cyan,500,500,true,true)
                        .imread("images/pullar.png")  
//                        .imshow()
//                        .imhist()
//                        .rgb2gray()
                        .hist(256)
//                        .prev()
//                        .rgb2gray()
//                        .imshow()
//                        .hist()
                        //.plot()
                        //.heatmap()
                        ;

                
    }
}
