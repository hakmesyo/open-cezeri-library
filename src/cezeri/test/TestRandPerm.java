/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestRandPerm {
    public static void main(String[] args) {
//        CMatrix randData=CMatrix.getInstance().rand(62, 20, 0, 255).round();
//        CMatrix[] subsets=split(randData,5); //5 means 5-fold CV which equals to 80% and 20% partitions
        CMatrix rand_indexes=CMatrix.getInstance().randPerm(62).println();
    }

    private static CMatrix[] split(CMatrix randData, int nFolds) {
        CMatrix[] ret=new CMatrix[nFolds];
        int nRows=randData.getRowNumber();
        int nCols=randData.getColumnNumber();
        int delta=nRows/nFolds;
        for (int i = 0; i < nFolds-1; i++) {
            ret[i]=randData.commandParser(i*delta+":"+((i+1)*delta-1), ":").println((i+1)+".subset");
        }
        CMatrix cm=CMatrix.getInstance().zeros(1,nCols);
        for (int i = (nFolds-1)*delta; i < nRows; i++) {
            CMatrix temp=randData.getRowMatrix(i).transpose();
            cm=cm.cat(2, temp);
        }
        cm=cm.deleteRow(0);
        ret[nFolds-1]=cm.println((nFolds)+".subset");
        return ret;
    }    
    
}
