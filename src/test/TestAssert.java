/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author BAP1
 */
public class TestAssert {
    public static void main(String[] args) {
        int b=110;
        int c=0;
        int a=divide(b,c);
        System.out.println("a:"+a);
    }

    private static int divide(int b, int c) {
        assert(c==0);
        return b/c;
    }
}
