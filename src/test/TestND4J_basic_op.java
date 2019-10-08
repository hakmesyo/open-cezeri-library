/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 *
 * @author BAP1
 */
public class TestND4J_basic_op {

    public static void main(String[] args) {
        int n = 30000;
        long t=0;
        long t1 = FactoryUtils.tic();
//        while (t<n*n){
//            t++;
//        }
//        for (double i = 0; i < n*n; i++) {
//            
            INDArray cm2 = Nd4j.randn(n, n);
//        }
        t1 = FactoryUtils.toc(t1);
//        INDArray cm4=Nd4j.zeros(n, n).add(12).mul(1255555);
//        t1=FactoryUtils.toc(t1);
//        CMatrix cm3 = CMatrix.getInstance()
//                .randn(n, n)
//                ;
//        
//        t1=FactoryUtils.toc(t1);
//        System.out.println(cm);

    }
}
