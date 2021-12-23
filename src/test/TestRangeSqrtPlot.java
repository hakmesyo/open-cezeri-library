/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author cezerilab
 */
public class TestRangeSqrtPlot {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .range(-200,300)
                .sqrt()
                .plot()
                
                ;
    }
}
