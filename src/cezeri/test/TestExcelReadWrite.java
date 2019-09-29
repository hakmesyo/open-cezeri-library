/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestExcelReadWrite {
    public static void main(String[] args) {
        String path="data\\temp.xlsx";
        CMatrix cm = CMatrix.getInstanceFromExcelCSV(path).println();
        CMatrix cm2 = CMatrix.getInstance().csvread(path).println();
    }
}
