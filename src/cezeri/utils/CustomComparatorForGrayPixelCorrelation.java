/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import cezeri.types.TGrayPixel;
import java.util.Comparator;

/**
 *
 * @author musa-atas
 */
public class CustomComparatorForGrayPixelCorrelation implements Comparator<TGrayPixel> {
    @Override
    public int compare(TGrayPixel o1, TGrayPixel o2) {
        if(o1.corValue>o2.corValue)
            return -1;
        else if (o1.corValue==o2.corValue)
            return 0;
        else
            return 1;                  

    }
}