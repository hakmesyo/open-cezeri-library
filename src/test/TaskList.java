/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TaskList {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String line;
        Process p = Runtime.getRuntime().exec("tasklist.exe");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
        
        CMatrix cm = CMatrix.getInstance().randn(5000,2).map(0, 100).hist(100);
    }

}