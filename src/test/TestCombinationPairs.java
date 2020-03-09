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
public class TestCombinationPairs {
    public static void main(String[] args) {
        String pass="aabd";
        char[] pool="abcçdefgğhıijklmnoöprsştuüvwyz0123456789".toCharArray();
        int n=pool.length;
        String[] comb=CMatrix.getInstance().combinationPairs(pool,4).combinationPairs;
        Arrays.asList(comb).forEach(System.out::println);        
    }
    
}
