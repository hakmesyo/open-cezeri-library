/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;
import java.awt.Color;

/**
 *
 * @author BAP1
 */
public class TestConvolution {

    public static void main(String[] args) {
        double[][] d_X = {
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, -1, 1, -1, -1, -1, 1, -1, -1},
            {-1, -1, -1, 1, -1, 1, -1, -1, -1},
            {-1, -1, -1, -1, 1, -1, -1, -1, -1},
            {-1, -1, -1, 1, -1, 1, -1, -1, -1},
            {-1, -1, 1, -1, -1, -1, 1, -1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
        double[][] d_M = {
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, 1, 1, -1, -1, -1, 1, 1, -1},
            {-1, 1, -1, 1, -1, 1, -1, 1, -1},
            {-1, 1, -1, -1, 1, -1, -1, 1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, 1, -1, -1, -1, -1, -1, 1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
        double[][] k_diag_1 = {
            {1, -1, -1},
            {-1, 1, -1},
            {-1, -1, 1}
        };
        double[][] k_diag_2 = {
            {-1, -1, 1},
            {-1, 1, -1},
            {1, -1, -1}
        };
        int w=400;
        CMatrix ck_dg1 = CMatrix.getInstance(k_diag_1).heatmap(Color.cyan, 200, 200, true, true);;
        CMatrix ck_dg2 = CMatrix.getInstance(k_diag_2).heatmap(Color.cyan, 200, 200,true, true);;
        CMatrix cm = CMatrix.getInstance(d_X)
                .heatmap(Color.gray, w, w, true, true)
                ;
        CMatrix cm_dg1=cm.convolve(ck_dg1)
                .heatmap(Color.cyan, w, w, true, true);
        CMatrix cm_dg2=cm.convolve(ck_dg2)
                .heatmap(Color.cyan, w, w, true, true);
    }
}
