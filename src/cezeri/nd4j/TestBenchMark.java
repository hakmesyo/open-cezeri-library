/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.nd4j;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import cezeri.matrix.*;

/**
 *
 * @author DELL LAB
 */
public class TestBenchMark {
    public static void main(String[] args) {
        int n = 1000;
        for (int i = 0; i < 10; i++) {
            long t1 = System.currentTimeMillis();
//            INDArray zeros = Nd4j.zeros(n, n);
//            INDArray rnd1 = Nd4j.randn(n, n);
//            INDArray rnd2 = Nd4j.randn(n, n);
//            INDArray rnd3 = rnd1.mul(rnd2);
//            long tt = System.currentTimeMillis();
//            //double[][] d=rnd3.toDoubleMatrix(); 
//            INDArray rnd4=rnd3.dup();
//            System.out.println("conversion cost = " + (System.currentTimeMillis() - tt)+" shape:"+rnd4.shapeInfoToString());
////            long[] q=rnd4.shape();
////            for (int j = 0; j < q.length; j++) {
////                System.out.println("q[j] = " + q[j]);
////            }
//            long t2 = System.currentTimeMillis() - t1;
//            System.out.println("\nnd4j cost = " + t2);
            
            long cmt=System.currentTimeMillis();
            CMatrix cm1=CMatrix.getInstance().randn(n, n).multiplyScalar(100).round();
            CMatrix cm2=CMatrix.getInstance().randn(n, n).multiplyScalar(100).round();
            CMatrix cm3=cm1.dot(cm2);            
            System.out.println("\ncmatrix cost = " + (System.currentTimeMillis() - cmt));
            
            System.gc();
        }
    }
    
}
