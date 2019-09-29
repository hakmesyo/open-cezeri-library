/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestPerformanceGUI {

    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan1.jpg").imshow("rgb");
        CMatrix cm1 = cm.rgb2gray().imshow("gray").binarizeOtsu().imshow("otsu");
    }
}
