/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import ai.djl.Device;
import ai.djl.ndarray.BaseNDManager;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import cezeri.factory.FactoryMatrix;
import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;

/**
 *
 * @author cezerilab
 */
public class TestNDArray {

    public static void main(String[] args) {
        int n = 4000;
        ndManager(n, n);
        //ocl(50000,5000);
    }

    private static void ndManager(int nr, int nc) {
        long t = 0;
        try (NDManager manager = NDManager.newBaseManager()) {
//            NDArray nd = manager.randomInteger(121, 159, new Shape(nr, nc), DataType.INT32);//.mul(30);
            NDArray nd = manager.randomUniform(0,100,new Shape(nr, nc));//.mul(30);
            //System.out.println("nd = " + nd);
        }
        for (int i = 0; i < 10; i++) {
            long t1 = System.currentTimeMillis();
            try (NDManager manager = NDManager.newBaseManager(Device.cpu())) {
                //NDArray nd = manager.randomInteger(121, 159, new Shape(nr, nc), DataType.INT32);//.mul(30);
                NDArray nd1 = manager.randomNormal(new Shape(nr, nc));
                NDArray nd2 = manager.randomNormal(new Shape(nr, nc));
                NDArray nd3 = nd1.dot(nd2);
//                System.out.println("nd:"+nd);
//                long tt=FactoryUtils.tic();
//                double[][] d=FactoryUtils.toDoubleArray2D(nd.toFloatArray(),3,3);
//                tt=FactoryUtils.toc(tt);
                //CMatrix.getInstance(d).println();
//                NDArray nd2 = manager.randomUniform(0,1000,new Shape(nr, nc));
//                NDArray nd3 = nd.dot(nd2);
                //System.out.println("type:"+nd.getDataType());
                //System.out.println("nd = " + nd);
            }  
            
            long t2=(System.currentTimeMillis()-t1);
            System.out.println("t2 = " + t2);
            t += t2;
        }
        System.out.println("ort:" + t / 10);
    }

    private static void ocl(int nr, int nc) {
        for (int i = 0; i < 10; i++) {
            long t1 = FactoryUtils.tic();
            CMatrix cm = CMatrix.getInstance().rand(nr, nc, 121, 159);
            t1 = FactoryUtils.toc("ocl:", t1);
        }
    }
}
