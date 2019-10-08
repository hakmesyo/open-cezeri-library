/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.types.TLearningType;
import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestWekaInstances {

    public static void main(String[] args) {
//        String str1 = "..\\DataSet\\Weka_Files\\dental_florisis\\kayac_dental_2C_8A_XRD.arff";
//        CMatrix cm1=CMatrix.fromARFF(str1).plot();
        
        String str2="..\\DataSet\\Weka_Files\\test\\rand_relation.arff";
//        CMatrix cm=CMatrix.getInstance().rand(1000,1).round().plot();
        CMatrix cm2=CMatrix.getInstance().rand(1000,5).cat(1, CMatrix.getInstance().rand(1000,1).round()).toWekaArff(str2,TLearningType.CLASSIFICATION).readARFF(str2).plot();
//        CMatrix cm3=CMatrix.fromARFF(str2).plot();
        
    }

}
