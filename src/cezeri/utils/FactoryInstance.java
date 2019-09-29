/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import cezeri.matrix.CMatrix;
import java.util.Enumeration;
import java.util.Vector;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author BAP1
 */
public class FactoryInstance {

    public static Instances generateInstances(String relationName, int nCols) {
        CMatrix cm = CMatrix.getInstance().zeros(1, nCols);
        FastVector att = new FastVector();
        for (int i = 0; i < cm.getColumnNumber(); i++) {
            att.addElement(new Attribute("f" + (i + 1)));
        }
        Instances ret = new Instances(relationName, att, cm.getRowNumber());
        for (int i = 0; i < cm.getRowNumber(); i++) {
            Instance ins = new Instance(cm.getColumnNumber());
            for (int j = 0; j < cm.getColumnNumber(); j++) {
                ins.setValue(j, cm.toDoubleArray2D()[i][j]);
            }
            ret.add(ins);
        }
        ret.setClassIndex(ret.numAttributes() - 1);
        return ret;
    }

    public static String[] getOriginalClasses(Instances data) {
        Attribute att = data.attribute(data.classIndex());
        String[] ret = new String[data.numClasses()];
        Enumeration enu = att.enumerateValues();
        int q = 0;
        while (enu.hasMoreElements()) {
            ret[q++] = (String) enu.nextElement();
        }
        return ret;
    }

    public static String[] getAttributeList(Instances data) {
        int n = data.numAttributes();
        String[] ret = new String[n];
        for (int i = 0; i < n; i++) {
            ret[i] = data.attribute(i).name();
        }
        return ret;
    }

    public static String[] getAttributeListExceptClassAttribute(Instances data) {
        int n = data.numAttributes();
        String[] ret = new String[n - 1];
        String classAtt = data.classAttribute().name();
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (!classAtt.equals(data.attribute(i).name())) {
                ret[k++] = data.attribute(i).name();
            }

        }
        return ret;
    }

    public static String[] getDefaultClasses(Instances data) {
//        String[] str = getOriginalClasses(data);
        int n=data.numDistinctValues(data.classAttribute());
//        int n = data.numClasses();
        String[] ret = new String[n];
        for (int i = 0; i < n; i++) {
            ret[i] = i + "";
        }
        return ret;
    }

    public static Instances generateInstances(String relationName, CMatrix cm) {
        FastVector att = new FastVector();
        for (int i = 0; i < cm.getColumnNumber(); i++) {
            att.addElement(new Attribute("f" + (i + 1)));
        }
        Instances ret = new Instances(relationName, att, cm.getRowNumber());
        for (int i = 0; i < cm.getRowNumber(); i++) {
            Instance ins = new Instance(cm.getColumnNumber());
            for (int j = 0; j < cm.getColumnNumber(); j++) {
                ins.setValue(j, cm.toDoubleArray2D()[i][j]);
            }
            ret.add(ins);
        }
        ret.setClassIndex(ret.numAttributes() - 1);
        return ret;
    }

    public static Instances generateInstances(String relationName, double[][] m) {
        FastVector att = new FastVector();
        for (int i = 0; i < m[0].length; i++) {
            att.addElement(new Attribute("f" + (i + 1)));
        }
        Instances ret = new Instances(relationName, att, m.length);
        for (int i = 0; i < m.length; i++) {
            Instance ins = new Instance(m[0].length);
            for (int j = 0; j < m[0].length; j++) {
                ins.setValue(j, m[i][j]);
            }
            ret.add(ins);
        }
        ret.setClassIndex(ret.numAttributes() - 1);
        return ret;
    }

    public static double[][] getData(Instances m) {
        double[][] ret = new double[m.numInstances()][m.numAttributes()];
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            ret[i] = ins.toDoubleArray();
        }
        return ret;
    }

    public static CMatrix getMatrix(Instances m) {
        return toMatrix(m);
    }

    public static CMatrix toMatrix(Instances m) {
        double[][] ret = new double[m.numInstances()][m.numAttributes()];
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            ret[i] = ins.toDoubleArray();
        }
        return CMatrix.getInstance(ret);
    }

    public static CMatrix fromInstances(Instances m) {
        double[][] ret = new double[m.numInstances()][m.numAttributes()];
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            ret[i] = ins.toDoubleArray();
        }
        return CMatrix.getInstance(ret);
    }

    /**
     *
     * @param m tüm dataset
     * @param cl
     * @return
     */
    public static Instances[] getSpecificInstancesBasedOnClassValue(Instances m, String[] cl) {
        Instances[] ret = new Instances[cl.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = FactoryInstance.generateInstances(m.relationName() + "_class=" + cl[i], m.numAttributes());
//            ret[i] = m.resampleWithWeights(new Random());
            ret[i].delete();
        }
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            for (int j = 0; j < cl.length; j++) {
                if (("" + (int) ins.classValue()).equals(cl[j])) {
                    ret[j].add(ins);
                }
            }
        }
        return ret;
    }

    public static double[] getClassData(Instances m) {
        double[] ret = new double[m.numInstances()];
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            ret[i] = ins.classValue();
        }
        return ret;
    }

    public static double[] getClassData(Instances m, int val) {
        Vector v = new Vector();
        for (int i = 0; i < m.numInstances(); i++) {
            Instance ins = m.instance(i);
            if ((int) ins.classValue() == val) {
                v.add(ins.classValue());
            }
        }
        double[] ret = FactoryUtils.toDoubleArray(v);
        return ret;
    }

    public static Instances getSubsetData(Instances data, String[] attList) {
        Instances temp = new Instances(data);
        for (int i = 0; i < data.numAttributes(); i++) {
            if (!temp.attribute(0).equals(temp.classAttribute())) {
                temp.deleteAttributeAt(0);
            }
        }
        double[][] m = new double[attList.length + 1][data.numInstances()];
        for (int i = 0; i < attList.length; i++) {
            int n=attList.length - 1 - i;
            String str=attList[n];
            Attribute t = data.attribute(str);
            double[] d = data.attributeToDoubleArray(t.index());
            m[n] = d;
            temp.insertAttributeAt(t, 0);
        }
        m[attList.length] = data.attributeToDoubleArray(data.classIndex());
        m = CMatrix.getInstance(m).transpose().toDoubleArray2D();

        FastVector att = new FastVector();
        for (int i = 0; i < temp.numAttributes(); i++) {
            att.addElement(temp.attribute(i));
        }
        Instances ret = new Instances(temp.relationName(), att, m.length);
        for (int i = 0; i < m.length; i++) {
            Instance ins = new Instance(m[0].length);
            for (int j = 0; j < m[0].length; j++) {
                ins.setValue(j, m[i][j]);
            }
            ret.add(ins);
        }
        ret.setClassIndex(temp.classIndex());

        return ret;
    }

}
