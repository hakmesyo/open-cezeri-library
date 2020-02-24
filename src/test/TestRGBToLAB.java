/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author DELL LAB
 */
public class TestRGBToLAB {
    public static void main(String[] args) {
        
        double[] lab=ImageProcess.rgbToLab(255,0,0);
        for (int i = 0; i < lab.length; i++) {
            System.out.println(lab[i]);
        }
        Arrays.stream(lab).forEach(e->System.out.println(e+";"));
        Arrays.stream(lab).forEach(System.out::println);
        //d.forEach(System.out::println);
    }
}
