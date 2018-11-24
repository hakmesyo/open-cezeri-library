/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author BAP1
 */
public class TestFXPlot {

    public static void main(String[] args) {
        List<String> lst = new ArrayList();
        lst.add("pi");
        lst.add("e");
        lst.add("sinc");

        CMatrix cm = CMatrix.getInstance()
                .randn(3, 3, -20, 20)
                .setColumnNames(lst)
                .plotFX()
                
                .rand(50, 3, 0, 255)
                .setXData4FX(CMatrix.getInstance().linspace(-100,350,10).toDoubleArray1D())
                .setColumnNames(Arrays.asList("1998","1999","2000"))
                .plotFX("gauss dağılımı", "observation", "target")
                
                .linspace(0, 360,30)                
                .toRadians()
                .sin()
                .cat(1, CMatrix.getInstance().linspace(0,360, 30).toRadians().cos())
                .setXData4FX(CMatrix.getInstance().linspace(0, 360, 30).toDoubleArray1D())
                .setColumnNames(Arrays.asList("sin","cos"))
                .plotFX("Trigonometric Functions", "degree", "f(x)")
                ;
    }
}
