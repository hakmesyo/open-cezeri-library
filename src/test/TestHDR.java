package test;

import cezeri.matrix.CMatrix;
import cezeri.types.TMatrixOperator;

public class TestHDR {
    public static void main(String[] args) {
        CMatrix cm_under = CMatrix.getInstance().imread("images\\p1.jpg")                
//                .imresize(0.5)
                .rgb2gray()
                .imshow("under exposure")
                ;
        CMatrix cm_over = CMatrix.getInstance().imread("images\\p13.jpg")                
//                .imresize(0.5)
                .rgb2gray()
                .imshow("over exposure")
                ;
        CMatrix cm_auto = CMatrix.getInstance().imread("images\\samsung_auto.jpg")
                .rgb2gray()
                .imshow("camera auto exposure")
                ;
        
        CMatrix cm = cm_over.add(cm_under).divideScalar(2).imshow("simple average approach");
        
        double[][] d=cm_under.toDoubleArray2D();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j]=(d[i][j]<50)?0:d[i][j];
            }
        }
        cm_under=cm_under.setArray(d);
        cm_under.imshow();
        
        d=cm_over.toDoubleArray2D();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j]=(d[i][j]>200)?0:d[i][j];
            }
        }
        cm_over=cm_over.setArray(d);       
        cm_over.imshow("after transforming");
        
        CMatrix cm_ortlama = cm_under.add(cm_over).divideScalar(2.0).imshow("ortlama");
        
    }
}
