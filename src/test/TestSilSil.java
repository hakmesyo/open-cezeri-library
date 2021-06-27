/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.types.TFigureAttribute;

/**
 *
 * @author cezerilab
 */
public class TestSilSil {
    public static void main(String[] args) {
        int length=100;
        TFigureAttribute attrib = new TFigureAttribute();
        attrib.pointType = "-o";
        CMatrix cm = CMatrix.getInstance()
               .random_seed(12)
                .randTimeSeries(length, 1, -1, 1)
                .cat(1, CMatrix.getInstance()
                        .random_seed(13)
                        .randTimeSeries(length, 1, -1, 1))
                .map(0, length)
                .plot(attrib)                
                ;
//        double[] d1=CMatrix.getInstance()
//                .random_seed(12)
//                .randTimeSeries(length, 1, -1, 1)
//                .map(0, length).toDoubleArray1D();
//        double[] d2=CMatrix.getInstance()
//                .random_seed(13)
//                .randTimeSeries(length, 1, -1, 1)
//                .map(0, length).toDoubleArray1D();
//        double sum_abs_dif=sumAbsDif(d1,d2);
//        System.out.println("sum_abs_dif = " + sum_abs_dif);
                
        
//        CMatrix cm1 = CMatrix.getInstance(cm.transpose().toDoubleArray2D()[0]).plot();
//        CMatrix cm2 = CMatrix.getInstance(cm.transpose().toDoubleArray2D()[1]).plot();
//        System.out.println(cm1.absDifference(cm2).println().sumTotal());
//        
//        cm1.addScalar(-0.5).plot();
        
    }

    private static double sumAbsDif(double[] d1, double[] d2) {
        double ret=0;
        int n=d1.length;
        for (int i = 0; i < n; i++) {
            ret+=Math.abs(d1[i]-d2[i]);
        }
        return ret;
    }
}
