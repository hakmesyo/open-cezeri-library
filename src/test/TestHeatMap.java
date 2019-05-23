/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author elcezerilab
 */
public class TestHeatMap {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .rand(6,6)
                .map(-100, 100)
                .heatmap()
                ;
    }
}
