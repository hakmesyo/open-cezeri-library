/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cezeri.utils;

import cezeri.types.TVoteMap;
import java.util.Comparator;

/**
 *
 * @author BAP1
 */
public class CustomComparatorForVoteMap implements Comparator<TVoteMap> {
    @Override
    public int compare(TVoteMap o1, TVoteMap o2) {
        //büyükten küçüğe için o1.value>o2.value
        //küçükten büyüğe için o1.value<o2.value
        if(o1.value>o2.value)
            return -1;
        else if (o1.value==o2.value)
            return 0;
        else
            return 1;                  

    }

    
}
