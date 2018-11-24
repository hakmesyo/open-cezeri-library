/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import cezeri.types.TFeatureRank;
import java.util.Comparator;

/**
 *
 * @author musa-atas
 */
public class CustomComparatorForFeatureRank implements Comparator<TFeatureRank> {
    @Override
    public int compare(TFeatureRank o1, TFeatureRank o2) {
        //büyükten küçüğe için o1.value>o2.value
        //küçükten büyüğe için o1.value<o2.value
        if(o1.value<o2.value)
            return -1;
        else if (o1.value==o2.value)
            return 0;
        else
            return 1;                  

    }
}