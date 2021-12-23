package cezeri.machine_learning.classifiers.neural_net;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Preprocessing {

    public static double[][][] oneHotEncoding(String[] labels, HashSet<String> labelSet){
        LinkedHashMap<String, double[]> labelEncodeMap = new LinkedHashMap<>();
        double[][][] Y = new double[labels.length][1][labelSet.size()];

        double[] tempEncode;
        int i=0;
        Iterator<String> labelSetIterator = labelSet.iterator();
        while(labelSetIterator.hasNext()){
            tempEncode = new double[labelSet.size()];
            tempEncode[i] = 1.0;
            labelEncodeMap.put(labelSetIterator.next(), tempEncode);
            i++;
        }

        for(int j=0; j<labels.length; j++){
            Y[j][0] = labelEncodeMap.get(labels[j]);
        }


        return Y;
    }



}
