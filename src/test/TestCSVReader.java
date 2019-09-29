/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author elcezerilab
 */
public class TestCSVReader {
    public static void main(String[] args) {
        
        CMatrix cmx = CMatrix.getInstance()
                .randn(1000, 1, 100, 120)
                .cat(1, CMatrix.getInstance().randn(1000, 1, -10, 65))
                .cat(1, CMatrix.getInstance().randnMeanVariance(1000, 1, 50, 40.5))
                .hist(50)
                ;
        
        List lst=Arrays.asList(1,2,3,4);
        CMatrix cm2 = CMatrix.getInstance(lst)
                .println()
                ;
        CMatrix cm = CMatrix.getInstance()
                .readCSV("data\\iris.csv")
                .head()
                .pow(2)
                .head()
                ;
    }
}
