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
public class TestSil {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .zeros(10, 20)
                .rand(100, 50)
                .println()
                ;
    }
}
