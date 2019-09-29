/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class TestSerialization {
    public static void main(String[] args) {
        CMatrix cm=CMatrix.getInstance().rand(10,8).println().serialize("data\\res.res");
        System.out.println("merhaba");
        CMatrix cm2=CMatrix.getInstance().deSerialize("data\\res.res").println("deserialize matrix");
    }
}
