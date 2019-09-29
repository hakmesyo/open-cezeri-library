/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.feature_extraction;

import cezeri.machine_learning.evaluater.FactoryEvaluation;
import cezeri.matrix_processing.CMatrix;
import cezeri.matrix_processing.FactoryMatrix;
import cezeri.types.TLearningType;
import cezeri.types.TMatrixOperator;
import cezeri.utils.FactoryUtils;
import java.util.ArrayList;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;

/**
 *
 * @author BAP1
 */
public class LBP1D {

    public static int[] getLBP_1D_with_no_padding(int[] d, int PL, int PR) {
        int nbit = PL + PR + 1;
        int size = (int) Math.pow(2, nbit);
        int[] hist = new int[size];
        int[] v = new int[d.length - (nbit - 1)];
        String sequence = "";
        for (int i = PL; i < d.length - PR; i++) {
            sequence = "";
            for (int j = i - PL; j < i; j++) {
                int dif = d[j] - d[i];
                if (dif >= 0) {
                    sequence += "1";
                } else {
                    sequence += "0";
                }
            }
            for (int j = i + 1; j <= i + PR; j++) {
                int dif = d[j] - d[i];
                if (dif >= 0) {
                    sequence += "1";
                } else {
                    sequence += "0";
                }
            }
            System.out.println("sequence = " + sequence);
            System.out.println("int val: = " + Integer.valueOf(sequence, 2));
            v[i - PL] = Integer.valueOf(sequence, 2);
        }
        return hist;
    }

    public static int[] getLBP_1D(int[] d, int PL, int PR) {
        int nbit = PL + PR;
        int size = (int) Math.pow(2, nbit);
        int[] hist = new int[size + 1];
        int[] v = new int[d.length - nbit];
        String sequence = "";
        String sv = "";
        String seq = "";
        for (int i = PL; i < d.length - PR; i++) {
            sequence = "";
            for (int j = i - PL; j < i; j++) {
                int dif = d[j] - d[i];
                if (dif >= 0) {
                    sequence += "1";
                } else {
                    sequence += "0";
                }
            }
            for (int j = i + 1; j <= i + PR; j++) {
                int dif = d[j] - d[i];
                if (dif >= 0) {
                    sequence += "1";
                } else {
                    sequence += "0";
                }
            }
//            System.out.println("sequence = " + sequence);
//            System.out.println("int val: = " + Integer.valueOf(sequence, 2));
//            System.out.print(Integer.valueOf(sequence, 2)+" ");
            seq = seq + sequence + " ";
            sv = sv + Integer.valueOf(sequence, 2) + " ";
            v[i - PL] = Integer.valueOf(sequence, 2);
        }
        int[] hist1 = FactoryUtils.hist(v, 256);
        for (int i = 0; i < hist1.length; i++) {
            hist[i]=hist1[i];
        }
        System.out.println("sequence:"+seq);
        System.out.println("value:"+sv);
        return hist;
    }

    public static int[] toInt(String str) {
//        System.out.print(str+":");
        int[] ret = new int[str.length()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = str.charAt(i);
//            System.out.print(ret[i] + " ");
        }
//        System.out.println("");
        return ret;
    }

    public static String clean(String str) {
        String ret = str;
        ret = ret.trim();
        ret = ret.replace(" ", "")
                .replace("?", "")
                .replace(".", "")
                .replace(";", "")
                .replace("\\", "")
                .replace("\n", "")
                .replace("\t", "")
                .replace("-", "");

        return ret;
    }

    public static String addPadding(String s, int c, int PL, int PR) {
        String sPL = "";
        for (int i = 0; i < PL; i++) {
            sPL += (char) c;
        }
        String sPR = "";
        for (int i = 0; i < PR; i++) {
            sPR += (char) c;
        }
        String ret = sPL + s + sPR;
        return ret;
    }

    public static int[] perform1DLBP(String str, int PL, int PR) {
        str = clean(str);
//        System.out.println("str = " + str);
        str = addPadding(str, 0, PL, PR);
//        System.out.println("after adding pad to str = " + str);
        int[] d = toInt(str);
//        CMatrix.getInstance(d).println();
//        CMatrix.getInstance(d).plot("*");
        int[] hist = getLBP_1D(d, PL, PR);
//        CMatrix cm = CMatrix.getInstance(hist).plot("*");
//        CMatrix.getInstance(hist).println();
        return hist;
    }

    private static void buildArffFile() {
        String[][] ds = CMatrix.getInstance().readArffAsStringArray("data\\sms_spam.arff", 0);
//        FactoryMatrix.println(ds);
        String[][] ds1 = new String[ds.length][ds[0].length];
        int[][] ds2 = new int[ds.length][257];
        for (int i = 0; i < ds.length; i++) {
            ds1[i][0] = ds[i][1];
            ds1[i][1] = ds[i][0];
        }
        for (int i = 0; i < ds2.length; i++) {
            ds2[i] = perform1DLBP(ds1[i][0], 4, 4);
            if (ds1[i][1].equals("ham")) {
                ds2[i][ds2[0].length - 1] = 0;
            } else {
                ds2[i][ds2[0].length - 1] = 1;
            }
        }
        CMatrix cm = CMatrix.getInstance(ds2).toWekaArff("data\\sms_spam_lbp.arff", TLearningType.CLASSIFICATION);

//        FactoryUtils.writeToFile("data\\spam_sms_new.txt",ds1);
//        perform1DLBP("merhaba",4,4);
//        perform1DLBP("nfsıbcb",4,4);
    }
    
    public static void main(String[] args) {
//        CMatrix.getInstance(perform1DLBP("merhaba",4,4)).plot();
//        CMatrix.getInstance(perform1DLBP("merhaba",4,4)).plot();
//        perform1DLBP("nfsıbcb",4,4);
//        perform1DLBP("I think your mentor is , but not 100 percent sure",4,4);
//        perform1DLBP("Camera - You are awarded a SiPix Digital Camera! call 09061221066 fromm landline. Delivery within 28 days.",4,4);
        
        
//        buildArffFile();

//        CMatrix cm = CMatrix.getInstanceFromARFF("data\\sms_spam_lbp.arff");
//        CMatrix cm1=getClass(cm,0).shuffleRows().commandParser("0:746", ":");
//        CMatrix cm2=getClass(cm,1).shuffleRows();
//        CMatrix cm3 = cm1.cat(2, cm2).shuffleRows();
//        cm3.println();
//        cm3.toWekaArff("data\\sms_spam_lbp_balanced.arff", TLearningType.CLASSIFICATION);

//        CMatrix cm = CMatrix.getInstanceFromARFF("data\\sms_spam_lbp_balanced.arff");

        Instances trainX = CMatrix.getInstance().readARFF("data\\sms_spam_lbp_balanced.arff").shuffleRowsWeka().getWekaInstance();       
//        Classifier[] models = {new REPTree(),new IBk(1),new NaiveBayes(),new SMO()};    
        Classifier model = new SMO();   
        FactoryEvaluation.performTest(model, trainX, trainX, true, false);
//        for (int i = 0; i < models.length; i++) {
//            FactoryEvaluation.performCrossValidate(models[i], trainX, 2,true,false);
//        }        
    }

    public static CMatrix getClass(CMatrix cm, int n) {
        double[][] d=cm.toDoubleArray2D();
        ArrayList lst=new ArrayList();
        int k=d.length;
        for (int i = 0; i < k; i++) {
            if (d[i][d[i].length-1]==n) {
                lst.add(d[i]);
            }
        }
//        Double[] ret=new Double[lst.size()];
//        ret=lst.toArray(ret);
        return CMatrix.getInstance(lst);
    }


}
