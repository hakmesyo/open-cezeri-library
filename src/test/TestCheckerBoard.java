/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author cezerilab
 */
public class TestCheckerBoard {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .ones(50,50)
                .cat(1, CMatrix.getInstance().zeros(50,50))                
                .cat(2, CMatrix.getInstance().zeros(50,50).cat(1, CMatrix.getInstance().ones(50,50)))
                .map(0, 255)                
//                .replicateColumn(3)
//                .replicateRow(3)
//                .replicate(3)
                .replicate(3,3)
                .imshow()  
//                .heatmap(Color.BLUE)
                
                ;
    }
}
