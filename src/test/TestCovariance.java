/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import cezeri.types.TMatrixOperator;

/**
 *
 * @author BAP1
 */
public class TestCovariance {
    public static void main(String[] args) {
        double[][] d={{1,0,2,12},{-3,4,1,0},{1,1,11,-121}};
//        CMatrix cm = CMatrix.getInstance(d).println();
        CMatrix cmx = CMatrix.getInstanceFromFile("D:\\Google Drive\\LECTURE NOTES\\EEM533_Y.LİSANS_MACHINE_LEARNING\\homeworks\\homework_5\\data\\gender_train.txt", ",");
        int nc=cmx.getColumnNumber();
        CMatrix cm=cmx.matrix(cmx.cmd(":",(nc-1)+"").findIndex(TMatrixOperator.EQUALS,1).toIntArray1D());
        cm=cm.cmd(":","0:"+(nc-2)).println();
        cm.mean().println();
        cm.std().println();
        cm.var().println();
        cm.cov().println();
    }
    
}
