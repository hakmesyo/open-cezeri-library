/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;

/**
 *
 * @author DELL LAB
 */
public class TestTrainValidTestFolderGenerator {

    public static void main(String[] args) {
        trainValidTest();
    }

    private static void trainValidTest() {
//        String base_path = "C:\\python_data\\dataset\\gatenet\\datasets\\pollens\\ds\\";
        String base_path ="C:\\Users\\cezerilab\\Desktop\\pollens_224\\ds";
        
        FactoryUtils.splitTrainValidTestFolder(base_path,0.7,0.1,0.2);
    }
}
