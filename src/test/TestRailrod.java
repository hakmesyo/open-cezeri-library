/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CRectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author BAP1
 */
public class TestRailrod {

    private static String[] folders = {"BOLT_LEFT", "BOLT_RIGHT", "FASTENING_MODEL_LEFT", "FASTENING_MODEL_RIGHT"};
    private static String path = "C:\\Users\\BAP1\\Downloads\\RailDataSet\\RailDataSet\\";

    public static void main(String[] args) {
        //reIndexFolder();
        //cropROI();
        //preProcess();
        evaluateClassifierPerformance();
    }

    private static void reIndexFolder() {
        for (int i = 0; i < folders.length; i++) {
            FactoryUtils.renameFilesAsAscIndex(path + "\\" + folders[i], "jpg");
        }
    }

    private static void cropROI() {
        //her klasöre göre resim sayısı ve roi değişiyor
        for (int i = 0; i < 30; i++) {
            CMatrix cm = CMatrix.getInstance().imread(path + "\\" + folders[1] + "\\" + i + ".jpg")
                    .imresize(400, 480)
                    .imshow();

            BufferedImage img = ImageProcess.cropImage(cm.getImage(), new CRectangle(150, 50, 200, 200));
            cm.setImage(img)
                    .imshow()
                    .writeImage(path + "\\" + folders[1] + "\\yeni\\" + (i + 32) + ".jpg");
        }
    }

    private static void preProcess() {
        String dsPath = "C:\\Users\\BAP1\\Downloads\\RailDataSet\\ds\\bolted\\";
        int index=61;
//        String dsPath = "C:\\Users\\BAP1\\Downloads\\RailDataSet\\ds\\fastened\\";
//        int index=205;
        double[] scores=new double[index];
        for (int i = 1; i <= index; i++) {
            CMatrix cm = CMatrix.getInstance()
                    .imread(dsPath + i + ".jpg")
                    //.imshow("original")
                    .rgb2gray()
                    //.imshow("gray")
                    .threshold(10)
                    //.imshow("thresholded")
                    .imcomplement()
                    //.imshow("negative")
                    .cmd("0:125",":")
                    //.imshow("negative cropped")
                    .sum()
                    //.plot()
                    ;
            double n=cm.sumTotal();
            System.out.println(i+":n = " + n);
            scores[i-1]=n;
        }
        
        CMatrix cm1 = CMatrix.getInstance(scores);
        System.out.println("mean = " + cm1.getMean());
        System.out.println("min = " + cm1.getMinTotal());  
        //446896.0 found for fastened class which can be considered as threshold value for linear classifier
        System.out.println("max = " + cm1.getMaxTotal());

    }

    private static void evaluateClassifierPerformance() {
        double thr=900000;
        int nBolted=61;
        int nFastened=205;
        double nTotal=266;
        int errorFastened=getErrorForFastenedClass("C:/Users\\elcezerilab\\Downloads\\ds\\fastened\\",205,thr);
        int errorBolted=getErrorForBoltedClass("C:\\Users\\elcezerilab\\Downloads\\ds\\bolted\\",61,thr);
        double errorRate=(errorBolted+errorFastened)/nTotal*100;
        System.out.println("errorRate = " + errorRate);
    }

    private static int getErrorForBoltedClass(String path, int n, double thr) {
        int nErr=0;
        for (int i = 1; i <= n; i++) {
            double score=calculateWhitePixelCount(path,i);
            if (score>thr) {
                nErr++;
            }
        }
        return nErr;
    }
    
    private static int getErrorForFastenedClass(String path, int n, double thr) {
        int nErr=0;
        for (int i = 1; i <= n; i++) {
            double score=calculateWhitePixelCount(path,i);
            if (score<thr) {
                nErr++;
            }
        }
        return nErr;
    }

    private static double calculateWhitePixelCount(String path, int i) {
            CMatrix cm = CMatrix.getInstance()
                    .imread(path + i + ".jpg")
                    .imshow("original")
                    .rgb2gray()
                    .imshow("gray")
                    .threshold(10)
                    .imshow("thresholded")
                    .imcomplement()
                    .imshow("negative")
                    .cmd("0:125",":")
                    .imshow("negative cropped")
                    .sum()
                    .plot()
                    ;
            return cm.sumTotal();
    }
}
