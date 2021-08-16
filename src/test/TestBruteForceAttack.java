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
public class TestBruteForceAttack {

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
            String pass = "zdemo";
            char[] pool = "abcdefghijklmnopqrstuvwxyz".toCharArray();
//            CMatrix cm = 
                    CMatrix.getInstance()
                    .bruteForceAttack(pool, pass, false)
                    ;
//        }
    }
}
