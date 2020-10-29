/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.yolo_tensor_flow;

import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class MishActivationFunction {
    public static void main(String[] args) {
        CMatrix x = CMatrix.getInstance()
                .range(-5, 5, 0.1)               
                ;
        CMatrix cm = x.exp().plusScalar(1).log().tanh().multiplyElement(x).plot(x.toDoubleArray1D());
    }
}
