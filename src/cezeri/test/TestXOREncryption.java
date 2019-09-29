/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.utils.FactoryUtils;

/**
 *
 * @author BAP1
 */
public class TestXOREncryption {
    public static void main(String[] args) {
        int toBeEncrypted = 9;
        int salt          = 9;
        int encryptedVal  =  FactoryUtils.encrypt(toBeEncrypted, salt);
        int decryptedVal  =  FactoryUtils.decrypt(encryptedVal, salt);
        System.out.println("original:"+toBeEncrypted);
        System.out.println("encrypted:"+encryptedVal);
        System.out.println("decrypted:"+decryptedVal);
        
        int a=21;
        int b=13;
        
//        a ^= (b = (a ^= b)^b);
        a = a ^ b; 
        b = a ^ b; 
        a = a ^ b;
        
        System.out.println("a:"+a+",b:"+b);
    }
}
