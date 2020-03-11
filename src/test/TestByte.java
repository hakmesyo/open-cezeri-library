/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author hakmesyo
 */
public class TestByte {
    
    public static void main(String[] args) {
        byte a=127;
        System.out.println("a:"+a);
        a+=128;
        System.out.println("a:"+a);
        int b=a&0xFF;
        System.out.println("b:"+b);
        char c='a';
        byte q=(byte)c;
        System.out.println("q:"+q);
        short s=(short)255;
        System.out.println("short val:"+s);
        s+=127;
        System.out.println("short val:"+s);
        
    }
}
