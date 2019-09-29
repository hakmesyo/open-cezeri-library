/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author elcezerilab
 */
public class TestConcatenate {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().rand(2, 3, 1, 7)
                .round()
                .println()
                .concatenateRows()
                .println()
                .prev()
                .concatenateColumns()
                .println()
                ;
    }
}
