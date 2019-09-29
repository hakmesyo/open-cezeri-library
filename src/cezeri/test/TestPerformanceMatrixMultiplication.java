/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.machine_learning.classifiers.deeplearning_blas.Matrix;
import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
import no.uib.cipr.matrix.DenseMatrix;

/**
 *
 * @author BAP1
 */
public class TestPerformanceMatrixMultiplication {

    public static void main(String[] args) {
        int n=5000;
        long t1 = FactoryUtils.tic();
        no.uib.cipr.matrix.Matrix a = new DenseMatrix(n, n);
        no.uib.cipr.matrix.Matrix b = new DenseMatrix(n, n);
        no.uib.cipr.matrix.Matrix c = new DenseMatrix(n, n);
        
        t1 = FactoryUtils.toc("array generation cost:", t1);
        a.mult(b, c);
        t1 = FactoryUtils.toc("native blas matrix mult cost:",t1);
        a.mult(b, c);
        t1 = FactoryUtils.toc("native blas matrix mult cost:",t1);
        a.mult(b, c);
        t1 = FactoryUtils.toc("native blas matrix mult cost:",t1);
        a.mult(b, c);
        t1 = FactoryUtils.toc("native blas matrix mult cost:",t1);

        
//        int r = 5;
//        int c = 3;
//        CMatrix cm1 = CMatrix.getInstance().tic().rand(r,c).toc("random cost:");
//        CMatrix cm2 = CMatrix.getInstance().tic().rand(r,c).toc("random cost:");
//        //CMatrix cm3 = cm1.tic().times(cm2).toc("multiply:");
//        
//        Matrix m1=new Matrix(cm1.get2DArrayDouble());
//        m1.transpose();
//        Matrix m2=new Matrix(cm2.get2DArrayDouble());
//        long t1=FactoryUtils.tic();
//        Matrix m3=m1.trans1mult(m2);
//        long t2=FactoryUtils.toc("mtj multiplication:",t1);
        
//        double[][] d1=cm1.get2DArrayDouble();
//        double[][] d2=cm2.get2DArrayDouble();
//        long t1=FactoryUtils.tic();
//        double[][] m=multiplyByMatrix(d1, d2);
//        long t2=FactoryUtils.toc("array multiplication:",t1);
        
    }

    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength) {
            return null; // matrix multiplication is not possible
        }
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

}
