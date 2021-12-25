/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class TestSplitPath {
    public static void main(String[] args) {
        String path="./\\images\\test\\dfemale\\h_693.jpg";
        String[] str=FactoryUtils.splitPath(path);
        System.out.println("str = " + Arrays.toString(str));
    }
    
}
