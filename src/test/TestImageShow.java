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
public class TestImageShow {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().readImage("images\\peppers.png").showImage();
    }
}
