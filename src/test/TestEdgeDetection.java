/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestEdgeDetection {
    public static void main(String[] args) {
//        CMatrix cm = CMatrix.getInstance().imread(".//images//rice.png").imshow().detectEdgeCanny().imshow();
//        CMatrix cm = CMatrix.getInstance().imread(".//images//rice.png").rgb2gray().imshow().
//                
//                tic().edgeDetectionCanny().toc().imshow().
//                edgeDetectionMusa(10).toc().imshow();
        CMatrix cm2 = CMatrix.getInstance().readImage(".//images//horoz.jpg").imshow().rgb2gray().imshow().showHistogram().log().imshow().println();
    }
}
