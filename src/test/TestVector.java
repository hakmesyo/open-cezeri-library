/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix_processing.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestVector {
    public static void main(String[] args) {
        String path="images\\kaplan1.jpg";
        CMatrix y1 = CMatrix.getInstance().vector(0, 127).minusScalar(127).pow(8);
        CMatrix y2 = CMatrix.getInstance().vector(128, 255).minusScalar(128).pow(8);
        CMatrix y=y1.cat(2,y2).normalizeMinMax().plot();
        y=y.sign(127).plot();
                
//        CMatrix y = CMatrix.getInstance().vector(0, 255).minusScalar(128).pow(8).plot().normalizeMinMax().plot();
//        CMatrix pdf = CMatrix.getInstance().imread(path).rgb2gray().imshow().getImageHistogramData().normalizeMinMax().plot();
        
//        CMatrix pdf = CMatrix.getInstance().imread(path).rgb2gray().imshow().getPDFData().plot();
//        CMatrix target = y.multiplyElement(pdf).multiplyElement(y.signum(127)).plot();
//        System.out.println("sum:"+target.sumTotal());
    }
}
