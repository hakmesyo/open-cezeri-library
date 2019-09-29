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
public class TestMap {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().vector(0, 255);        
                cm.cat(1,cm.pow(0.04).map(0,255))
                .cat(1,cm.pow(0.1).map(0, 255))
                .cat(1,cm.pow(0.2).map(0, 255))
                .cat(1,cm.pow(0.4).map(0, 255))
                .cat(1,cm.pow(0.67).map(0, 255))
                .cat(1,cm.pow(1).map(0, 255))
                .cat(1,cm.pow(1.5).map(0, 255))
                .cat(1,cm.pow(2.5).map(0, 255))
                .cat(1,cm.pow(5).map(0, 255))
                .cat(1,cm.pow(10).map(0, 255))
                .cat(1,cm.pow(20).map(0, 255))
                
                .plot();
    }
        
    
}
