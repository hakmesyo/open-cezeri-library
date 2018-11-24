/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
public class TestSinc {
    public static void main(String[] args) {
        long t1=FactoryUtils.tic();
        CMatrix x = CMatrix.getInstance().vector(-10,0.01,10);
        CMatrix cm=x.sinc();
        cm.clonePure().jitter(0.05).cat(1, cm).plot(x.toDoubleArray1D()).scatter();  
        long t2=FactoryUtils.toc(t1);
    }
}
