/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;
import cezeri.types.TLearningType;

/**
 *
 * @author elcezerilab
 */
public class TestPerformDataSetArff {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstanceFromFile("C:\\Users\\elcezerilab\\Downloads\\iris.data", ",")
                .toWekaArff("C:\\Users\\elcezerilab\\Downloads\\iris.arff", TLearningType.CLASSIFICATION)
                
                
                
                ;
    }
}
