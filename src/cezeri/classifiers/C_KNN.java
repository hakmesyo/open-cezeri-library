/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.classifiers;

import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author BAP1
 */
public class C_KNN {
    public static double[][] train=CMatrix.getInstance().rand(200,7).showDataGrid().toDoubleArray2D();
    public static double[][] test=CMatrix.getInstance().rand(50,7).toDoubleArray2D();
    
    public static void main(String[] args) {
        double[] predict=computeKNN(train, test, 3);
        double[] actual=FactoryUtils.transpose(test)[4];
        CMatrix cm=CMatrix.getInstance(predict).cat(1, CMatrix.getInstance(actual)).plot();
    }
    
    public static double[] computeKNN(double[][] train,double[][] test,int K){
        double[] p=new double[test.length];
        for (int i = 0; i < p.length; i++) {
            p[i]=getKNN(train,test[i],K);
        }
        return p;
    }

    public static double getKNN(double[][] train, double[] test, int K) {
        double ret=0;
        ArrayList<Double> lst=new ArrayList<Double>();
        for (int i = 0; i < train.length; i++) {
            double[] tr=train[i];
            double dist=FactoryUtils.getEucledianDistanceExceptLastElement(tr,test);
            System.out.println("dist = " + dist);
            double dist2=CMatrix.getInstance(tr).cmd(":","0:end").minus(CMatrix.getInstance(test)).pow(2).sum().sqrt().getValue();
            System.out.println("dist2 = " + dist2);
            lst.add(dist);
        }
        Double[] d=new Double[lst.size()];
        d=lst.toArray(d);
        Arrays.sort(d);
        for (int i = 0; i < K; i++) {
            ret+=d[i];
        }
        ret=ret/K;
        return ret;
    }
    
}
