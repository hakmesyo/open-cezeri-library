package test;

import cezeri.matrix_processing.CMatrix;

public class TestResizeImage {
    public static void main(String[] args) {
        int kat=40;
        CMatrix cm = CMatrix.getInstance()
                .imread("images\\kaplan.jpg")
                .imshow("original")
                .cmd("0:"+kat+":end","0:"+kat+":end")
                .imshow("resized")
//                .imresize(0.5)
//                .imshow("ocl based resized")
                
                ;
//        double[][] img=cm.toDoubleArray2D();
//        double[][] d=new double[img.length/kat][img[0].length/kat];
//        for (int i = 0; i < img.length; i+=kat) {
//            for (int j = 0; j < img[0].length; j+=kat) {
//                d[i/kat][j/kat]=img[i][j];
//            }
//        }
//        cm=CMatrix.getInstance(d)
//                .imshow()
//                ;


    }
}
