package cezeri.test;

import cezeri.matrix.CMatrix;

public class TestMultiDimensionalGaussianDistribution {
    public static void main(String[] args) {
        CMatrix cm1 = CMatrix.getInstance()
                .make_blobs(600, 2, 3)
                .scatter()                
                ;
        CMatrix cm2 = CMatrix.getInstance()
                .make_blobs(1000, 2, 5, 1000, 150)
                .scatter()                
                ;
        double[] d1=CMatrix.getInstance().randn(1000, 1).toDoubleArray1D();
        double[] d2=CMatrix.getInstance().randn(1000, 1).toDoubleArray1D();
        CMatrix.getInstance().scatter(d1, d2);
        
//        CMatrix.getInstance(d1).hist(10);
        
    }
}
