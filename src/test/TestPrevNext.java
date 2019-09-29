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
public class TestPrevNext {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().randn(2,10).println("en baş").timesScalar(100).pow(2).log().println("en son");
        CMatrix cm1=cm.prevFirst().println("en baş").nextLast().println("en son");
        CMatrix cm2=cm.prev(5).println("en baş").next(5).println("en son");
//        cm1.println();
//        cm1=cm1.next().println();
//        cm1=cm1.next().println();
//        cm1=cm1.next().println();
//        cm1=cm1.next().println();
//        if (cm1.nextMatrix==null) {
//            System.out.println("null");
//        }
//        CMatrix cm2=cm.nextLast().println("en son");
//        cm.println("cm nin kendisi");
    }
}
