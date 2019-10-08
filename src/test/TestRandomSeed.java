package test;

import cezeri.matrix.CMatrix;

public class TestRandomSeed {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .random_seed(1234)
                .randn(100,1)
                .random_sample(1)
                .dump()
                .setArray(CMatrix.getInstance().random_seed(1234).randn(100,1).toDoubleArray2D())
                .plot()
                .random_sample(1)
                .println()
                .plotRefresh()
//                .setRandomSeed(null)
                .randn(100,1)
                .random_sample(1)
                .println()
                
                ;
    }
}
