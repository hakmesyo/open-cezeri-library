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
public class TestBenchmarkCMatrix {
    public static void main(String[] args) {
        int n=5000;
        for (int i = 0; i < 10; i++) {
            long t1 = System.currentTimeMillis();
            CMatrix cm = CMatrix.getInstance().randn(n, n).dot(CMatrix.getInstance().randn(n,n));
            long t2 = System.currentTimeMillis() - t1;
            System.out.println("t2 = " + t2);
        }
        
    }
}
