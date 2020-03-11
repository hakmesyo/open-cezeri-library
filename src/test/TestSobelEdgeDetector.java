/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.SobelEdgeDetector;
import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class TestSobelEdgeDetector {
    public static void main(String[] args) {
        CMatrix cm1 = CMatrix.getInstance().imread("images\\yaprak.jpg").rgb2gray().tic().edgeDetectionSobel(10).toc().imshow();
        CMatrix cm2 = CMatrix.getInstance().imread("images\\yaprak.jpg").rgb2gray().imshow().tic().edgeDetectionCanny().toc().imshow();
    }
}
