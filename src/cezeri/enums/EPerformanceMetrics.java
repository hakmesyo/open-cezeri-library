/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.enums;

/**
 *
 * @author BAP1
 */
public enum EPerformanceMetrics {

    R, //Correlation Coefficient
    R2, //R Square
    CRCF, //Correlation Coefficient
    NSEC, //Nash–Sutcliffe efficiency coefficient
    RELATIVE_NSEC, //Relative NSEC
    MSE, //Mean Squared Error
    RMSE, //Root Mean Squared Error
    MAE, //Mean Absolute Error
    MPE, //Mean Percent Error İsa HFSS
    IOA, //Index of Agreement
    ARE, //Average Relative Error
    RAE, //Relative Absolute Error
    RRSE, //Root Relative Squared Error
    PEARSON, //PEARSON
    KENDALL, //KENDALL
    SPEARMAN;      //SPEARMAN

    public static String[] names() {
        EPerformanceMetrics[] states = values();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].name();
        }

        return names;
    }
    
    public static String getName(int i){
        return values()[i].name();
    }

}
