/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import cezeri.matrix_processing.CMatrix;

/**Concatenate two matrix horizontally and vertically
 *
 * @author BAP1
 */
public class TestMatrixConcatenation {
    public static void main(String[] args) {
        CMatrix m1=CMatrix.getInstance().zeros(5,5).println();
        CMatrix m2=CMatrix.getInstance().ones(5,5).println();
        m1.cat(1, m2).println();
        m1.cat(2, m2).println();
    }
}
