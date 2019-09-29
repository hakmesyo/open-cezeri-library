/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BAP1
 */
public class TestMatrix {

    public static void main(String[] args) {
//        CMatrix m1 = CMatrix.getInstance().rand(5, 50, -250, 7500).formatDouble(2).transpose();
//        CMatrix m1 = CMatrix.getInstance().rand(5, 50, -2500, 2500).formatDouble(2).transpose();
//        CMatrix m1 = CMatrix.getInstance().rand(5, 50, -1, 1).formatDouble(2).transpose();
//        CMatrix m1 = CMatrix.getInstance().rand(20,30, 0, 255).formatDouble(2).plot("-*").plot("-.").plot("-o");
        TFigureAttribute attr=new TFigureAttribute();
        
        CMatrix m1 = CMatrix.getInstance().rand(20,30, 0, 255).formatDouble(2).plot("*").plot(".").plot("o");
        m1.tic();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
        m1.toc();
//        m1.imshow("merhaba");
//        m1.plot();
//        m1.toString();
//        CMatrix cm = CMatrix.getInstance().randPerm(150).println();
//        int num = 1;
//        for (int ii = 0; ii < 255; ii++) {
//            System.out.println(num);
//            num = (num * 2) % 101;
//        }

    }
}
