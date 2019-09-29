package cezeri.test;

import cezeri.matrix.CMatrix;
import cezeri.matrix.FactoryMatrix;

public class TestQuantizedImage {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images\\kaplan.jpg")
                //.imshow("original")
                .rgb2gray()
                
                .imshow()
                
                ;
        int q=128;
        double[][] img=cm.toDoubleArray2D();
        double[][] d=FactoryMatrix.clone(img);
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                d[i][j]=(int)img[i][j]/(256/q);
            }
        }
        CMatrix cm1 = CMatrix.getInstance(d)               
                //.imshow("quantized image")
                .multiplyScalar((256/q))
                
                .imshow()
        
        ;
        
    }
}
