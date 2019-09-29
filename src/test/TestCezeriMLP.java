package test;

import cezeri.machine_learning.classifiers.C_MLP;
import cezeri.matrix_processing.CMatrix;

public class TestCezeriMLP {

    public static void main(String[] args) {
        float[] class_labels = {-1,1}; CMatrix[][] cm = CMatrix.getInstance().importdata("data\\diabetes.txt", ",").shuffleRows().crossValidationSets(10);
//        float[] class_labels = {1, 2, 3}; CMatrix[][] cm = CMatrix.getInstance().importdata("data\\iris.txt", ",").shuffleRows().crossValidationSets(10);
//        float[] class_labels = {-1, 1}; CMatrix[][] cm = CMatrix.getInstance().importdata("data\\tic-tac-toe_all.txt", ",").shuffleRows().crossValidationSets(10);
        
        float acc=0,sum=0;
        for (int i = 0; i < cm.length; i++) {
            float[][] dsTrain = cm[i][0].toFloatArray2D();
            float[][] dsTest = cm[i][1].toFloatArray2D();
            C_MLP mlp = new C_MLP(dsTrain, dsTest, class_labels, 1, 10, 3, 1000, 0.1f, 0.3f);
            mlp.doTrain();
            acc=mlp.doTest();
            sum+=acc;
            System.out.println(i+".fold accuracy:"+acc);
        }
        System.out.println("accuracy:"+sum/cm.length);

//        CMatrix cm = CMatrix.getInstance().vector(1,5).println().normalizeMinMax().println();

    }

}
