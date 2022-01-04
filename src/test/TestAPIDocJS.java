/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.lang.reflect.Method;

/**
 *
 * @author cezerilab
 */
public class TestAPIDocJS {
    public static void main(String[] args) {
        String s=FactoryUtils.readFile("src/cezeri/matrix/CMatrix.java");
        String[] ss=s.split("public ");
        System.out.println("len:"+ss.length);
        Method[] methods=CMatrix.class.getDeclaredMethods();
        System.out.println("length:"+methods.length);
        for (Method method : methods) {
            String s1=method.toString()
                    .replace("cezeri.matrix.", "")
                    .replace("CMatrix.", "")
                    .replace("java.lang.", "")
                    .replace("call_back_interface.", "")
                    .replace("cezeri.interfaces.", "")
                    .replace("java.awt.", "")
                    .replace("java.awt.image.", "")
                    .replace("java.awt.", "")
                    .replace("java.util.", "")
                    .replace("cezeri.types.", "")
                    .replace("cezeri.gui.", "")
                    .replace("cezeri.enums.", "")
                    ;
            System.out.println(s1);
        }
    }
}
