/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.machine_learning.evaluater.FactoryEvaluation;
import cezeri.matrix.CMatrix;
import cezeri.types.TFigureAttribute;
import java.awt.BasicStroke;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;
public class TestMachineLearning {
    public static void main(String[] args) throws Exception {
        String dsTrain = "data\\runoff_4km_train.arff";
        String dsTest = "data\\runoff_4km_test.arff";
        Classifier[] models = {new REPTree(),new MultilayerPerceptron()};    
        Instances trainSCA_4 = CMatrix.getInstance().readARFF(dsTrain).plot().getWekaInstance();
        Instances testSCA_4 = CMatrix.getInstance().readARFF(dsTest).plot().getWekaInstance();
//        perform cross-validation
        for (int i = 0; i < models.length; i++) {
            FactoryEvaluation.performCrossValidate(models[i], trainSCA_4, 7,true,false);
        }
//        perform test process
        TFigureAttribute attr=new TFigureAttribute();
        attr.axis=new String[]{"Day", "m3/sn"};;
        attr.items=new String[]{"Observed", "Simulated"};
        attr.title="SCA 4km";
        attr.isStroke=true;
        float[] dashPattern1= { 5, 2, 5, 2 };
        attr.stroke.add(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,1,dashPattern1,0));
        float[] dashPattern2= { 10, 5, 10, 5 };
        attr.stroke.add(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < models.length; i++) {
            FactoryEvaluation.performTest(models[i], trainSCA_4, testSCA_4, true, true, attr);
        }
    }    
}
