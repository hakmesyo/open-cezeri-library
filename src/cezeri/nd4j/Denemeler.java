/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.nd4j;

import cezeri.factory.FactoryUtils;
import java.util.Arrays;
import org.bytedeco.javacpp.Pointer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.transforms.Histogram;
import org.nd4j.linalg.api.ops.random.impl.UniformDistribution;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import static org.nd4j.linalg.ops.transforms.Transforms.*;

/**
 *
 * @author cezerilab
 */
public class Denemeler {
    public static void main(String[] args) {
//        INDArray d=Nd4j.linspace(1, 10, 100);
//        System.out.println("d:"+d);
        
//        INDArray d=Nd4j.rand(3, new long[]{10,2});
        INDArray d = Nd4j.arange(0,100,2.5);
        d=d.reshape(new int[]{1,(int)d.length()});
        System.out.println("shape = " + d.shapeInfoToString());
        System.out.println("d = " + d);
        INDArray d2=d.add(-10);
        System.out.println("d2 = " + d2);
        System.out.println("d = " + d);
//        INDArray d = Nd4j.rand(3,5).mul(20-3).add(3);
        float[][] f=d.toFloatMatrix();
        
        String s=FactoryUtils.formatDoubleAsString(1.0159973f, 3);
        System.out.println("s = " + s);
        
//        d=Transforms.round(d);
//        System.out.println("d = " + d);
//        d=Nd4j.sort(d, 0, true);
//        System.out.println("d = " + d);
//        d=d.slice(0, 1);
//        System.out.println("d = " + d);
//        Histogram hist=new Histogram(d, 256);
//        System.out.println("hist = " + hist.get);
//        System.out.println("sum:"+d.sum(1)+" sum:"+d.sumNumber());
//        INDArray d=Nd4j.arange(0, 100, 10);
//        System.out.println("d = " + d+" \nlength:"+d.length());
//        INDArray b=Nd4j.argMax(d);
//        Float n=d.getFloat(b.toIntVector()[0]);
//        System.out.println("d = " + d+" \n"+Arrays.toString(b.toIntVector())+"\n"+n);
//        d=Transforms.log(d);
//        System.out.println("d = " + d);
//        INDArray d1 = Nd4j.rand(3,2).mul(20-3).add(3);
//        INDArray d2 = Nd4j.rand(3,2).mul(20-3).add(3);
//        System.out.println("dot product = " + Transforms.dot(d1, d2));
        
    }
}
