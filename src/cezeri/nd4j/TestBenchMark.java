/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.nd4j;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author DELL LAB
 */
public class TestBenchMark {
    public static void main(String[] args) {
        int n = 5000;
        for (int i = 0; i < 10; i++) {
            long t1 = System.currentTimeMillis();
//            INDArray zeros = Nd4j.zeros(n, n);
            INDArray rnd1 = Nd4j.randn(n, n);
            INDArray rnd2 = Nd4j.randn(n, n);
            INDArray rnd3 = rnd1.mul(rnd2);

            long t2 = System.currentTimeMillis() - t1;
            System.out.println("t2 = " + t2);
        }
    }
    
}
