/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import cezeri.matrix.CMatrix;
import java.util.List;

/**
 *
 * @author BAP1
 */
public class TestRadon {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().imread("images\\kaplan.jpg").imshow().cmd("0:2:end", "0:2:end").imshow();
//        CMatrix cm1=CMatrix.getInstance().imread("images\\chessboard.png").rgb2gray().imshow().imresize(200, 200).imshow();
//        CMatrix cm2=cm1.transformRadonForward(180,200).updateImage().imshow("radon");
//        CMatrix cm3=cm2.transpose().transformRadonBackward(cm1.getRowNumber()).normalizeWithRange(0, 255).transpose().imshow();
    }   
}
