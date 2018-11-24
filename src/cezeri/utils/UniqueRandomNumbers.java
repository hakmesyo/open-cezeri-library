/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

/**
 *
 * @author BAP1
 */
import java.util.ArrayList;
import java.util.Collections;

public class UniqueRandomNumbers {

    public static void main(String[] args) {
        int[] k=getUniqueNumbers(0, 255, 255);
        for (int i = 0; i < k.length; i++) {
            System.out.println(i+".sayı:"+k[i]);
        }
    }

    public static int[] getUniqueNumbers(int from, int to, int count) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = from; i < to; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        Collections.shuffle(list);
        Collections.shuffle(list);
        Object[] obj=list.toArray();
        int[] ret=new int[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i]=(int)obj[i];
        }
        return ret;
    }
    
    public static int getUniqueNumber(int from, int to) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = from; i < to; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        int ret=list.get(0);
        return ret;
    }
}
