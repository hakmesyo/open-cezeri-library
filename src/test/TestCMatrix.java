/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;
import cezeri.types.TMatrixOperator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static processing.core.PApplet.println;

/**
 *
 * @author BAP1
 */
public class TestCMatrix {

    public static void main(String[] args) {
        System.out.println("default dir:" + FactoryUtils.getDefaultDirectory());

        test_vector_or_range();

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

    private static void test_vector_or_range() {
//        IntStream s1 = IntStream.range(1, 3);
//        IntStream s2 = IntStream.range(1, 3);
//
//        IntStream s3 = Arrays.stream(new int[5]);
//
//        IntStream ist = new Random().ints(850, 0, 100).distinct();

//        for (int i = 0; i < 3; i++) {
//            long t1 = FactoryUtils.tic();
//            new Random()
//                    .ints(85_000_000, 0, 10_000)
//                    .distinct()
//                    .map(e->(int)(2*e*e-3*Math.sin(e*Math.PI/180)))
//                    .filter(e -> (e >= 66 && e <= 720))
//                    .boxed()
//                    .sorted(Collections.reverseOrder())
//                    .forEach(System.out::println);
//            t1 = FactoryUtils.toc("java 8 stream api:", t1);
//        }

//        Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9);
//        for (int i = 0; i < 100; i++) {
//            long t1 = FactoryUtils.tic();
//            CMatrix cm1 = CMatrix.getInstance()
//                    .rand(1, 85_000, 0, 10_000)
//                    .round()
//                    .distinct();
//            cm1 = cm1.cmd(cm1.find(TMatrixOperator.BETWEEN, 66, 72).toIntArray1D())
//                    .sort("column", "descend")
//                    .println();
//            ;
//            t1 = FactoryUtils.toc("OCL api:", t1);
//        }

//        IntStream ds=IntStream.range(0, 100)
//                .
    }
}
