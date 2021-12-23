/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author DELL LAB
 */
public class TestSil_1 {
int y = 11;
    private static int z = 1;

    public TestSil_1(int i) {
        y /= i;
        z = y;
    }

    public TestSil_1(int i, int i2) {
        y += (i + i2) * z;
        System.out.println(y);
    }

    public int method(int i) {
        y -= i;
        return y;
    }

    public static int getZ() {
        return z;
    }

    public static void main(String[] args) {
        new TestSil_1(new TestSil_1(3).method(1), TestSil_1.getZ());
    }
    
}
