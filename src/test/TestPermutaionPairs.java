/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.util.Arrays;

/**
 *
 * @author BAP1
 */
public class TestPermutaionPairs {
    public static void main(String[] args) {
        String pass="123de45mo";
        char[] pool="abcçdefgğhıijklmnoöprsştuüvwyz0123456789".toCharArray();
        int n=pool.length;
        String[] comb=CMatrix.getInstance().permutationPairs(pass.toCharArray(),4).permutationPairs;
        Arrays.asList(comb).forEach(e->{
            if (e.equals("demo")) {
                System.out.println("buldu");
            }
        });
        
    }
    
}
