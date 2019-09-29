/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;
import cezeri.utils.FactoryUtils;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author BAP1
 */
public class TestCMatrix {

    public static void main(String[] args) {
        System.out.println("default dir:"+FactoryUtils.getDefaultDirectory());
        
//        CMatrix.getInstance().rand(500,300).imshow().scale(255).imshow();
//        String path = ".\\images\\E1.jpg";
//        Path p = Paths.get(path);
//        String file = p.getFileName().toString();
//        System.out.println(file);
//        Java approach
//        CMatrix.getInstance().readImage().show().showHistogram().showRGB().showHistogramRed().showHistogramGreen().showHistogramBlue().showHistogramAlpha();
//        Matlab approach
//        CMatrix cm=CMatrix.getInstance().imread("C:\\Machine Vision Images\\BAP\\Test\\450_0.01.bmp").imshow();//.imhist().imhistAlpha().imhistRed().imhistGreen().imhistBlue().imhistGray();//.imshowRGB();//.imhist().imshowRGB().pdf().imshow().imhist().showDataGrid();
//        System.out.println("skewness:"+CMatrix.getInstance().imread(".\\images\\t1.bmp").getSkewness());
//        System.out.println("kurtosis:"+CMatrix.getInstance().imread(".\\images\\t1.bmp").getKurtosis());
//        CMatrix.getInstance().randn(500).timesScalar(10000).imshow();
//        CMatrix.getInstance().linspace(0, 360, 360).scale(Math.PI/180).sin().plot().showDataGrid().showImage();
//        CMatrix.getInstance().imread(".\\images\\E2.jpg").imshowRGB().imhist();
//        CMatrix.getInstance().imread(".\\images\\E3.jpg").imshowRGB().imhist();
//        CMatrix.getInstance().readImage().showHistogramAlpha();
        
        
//        CMatrix.getInstance().vector(0,255).minusScalar(127).pow(2).plot();
        
//        CMatrix y=CMatrix.getInstance().vector(0,255).minusScalar(127).pow(2);
//        double n=0.3;
//        System.out.println("n:"+n);
//        System.out.println("n:"+FactoryUtils.formatDouble(n,2));
//        System.out.println("n:"+FactoryUtils.formatDoubleAsString(n, 4));
    }
}
