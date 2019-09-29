package test;

import cezeri.matrix_processing.CMatrix;
import cezeri.matrix_processing.FactoryMatrix;
import cezeri.utils.FactoryUtils;
import java.util.Random;

public class TestMatrixPerformance {

    static double[][] dd = new double[640][480];

    public static void main(String[] args) {
        long t1 = FactoryUtils.tic();
        Random rnd = new Random();
//        CMatrix cm = CMatrix.getInstance();
//        double[][] d=FactoryMatrix.matrixDoubleZeros(2048,2048);

        int nr = dd.length;
        int nc = dd[0].length;
        for (int i = 0; i < nr; i++) {
//            doubleFill(dd[i],3.0);
            for (int j = 0; j < nc; j++) {
                dd[i][j] = (int)(Math.random()*10);
            }
        }
        
        int r=3000;
        int c=3000;
        
        t1 = FactoryUtils.toc("rand:",t1);
        double[][] zeros=FactoryMatrix.matrixDoubleZeros(r,c);
        t1 = FactoryUtils.toc("zeros:",t1);
        double[][] temp=FactoryMatrix.clone(dd);
        t1 = FactoryUtils.toc("clone:",t1);
        double[][] ones=FactoryMatrix.matrixDoubleOnes(r,c);
        t1 = FactoryUtils.toc("ones:",t1);
//        FactoryMatrix.toString(ones);
        


//        double[][] d1=FactoryMatrix.matrixDoubleZeros(2,2);
//        d1[0][0]=12;
//        
//        double[][] d2=FactoryMatrix.cloneMatrix(d1);
//        d2[0][0]=5;
//        
//        System.out.println("d1:"+d1[0][0]);
//        System.out.println("d2:"+d2[0][0]);
    }
    

    /*
 * initialize a smaller piece of the array and use the System.arraycopy 
 * call to fill in the rest of the array in an expanding binary fashion
     */
    public static void bytefill(byte[] array, byte value) {
        int len = array.length;

        if (len > 0) {
            array[0] = value;
        }

        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }

    public static void doubleFill(double[] array, double value) {
        int len = array.length;

        if (len > 0) {
            array[0] = value;
        }

        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }
}
