/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import cezeri.factory.FactoryUtils;

/**
 *
 * @author BAP1
 */
public class TFeatureRank {
    public String featureName = "";
    public String index;
    public double value;

    public String toString() {
//        String s = "[" + index + "] " + featureName + " :" + FactoryUtils.formatDouble(value);
        String s = featureName + ":" + FactoryUtils.formatDouble(value);
//        System.out.println(s);
        return s;
    }
    
    public String println() {
//        String s = "[" + index + "] " + featureName + " :" + FactoryUtils.formatDouble(value);
        String s = featureName + ":" + FactoryUtils.formatDouble(value);
        System.out.println(s);
        return s;
    }

}
