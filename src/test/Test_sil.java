/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class Test_sil {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                
                .tic()
                .rand(20000, 300000)
                .toc()
                .imshow()
                .log()
                .multiplyScalar(255)
                .toc()
                .imshow()
                .prevFirst()
                
                ;
    }
}
