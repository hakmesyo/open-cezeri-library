/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hakmesyo
 */
public class TStatistics implements Serializable{

    public double mean = 0;
    public double std;
    public double contrast;
    public double entropy;
    public double kurtosis;
    public double skewness;
    public double adaptiveExposureScore;

    public static TStatistics getStatistics(BufferedImage img) {
        CMatrix cm = CMatrix.getInstance(img);
        return getStatistics(cm);
    }

    public static TStatistics getStatistics(CMatrix cm) {
        TStatistics ret = new TStatistics();
        ret.mean =FactoryUtils.formatDouble(cm.meanTotal());
        ret.std=FactoryUtils.formatDouble(cm.stdTotal());
        ret.contrast=FactoryUtils.formatDouble(cm.getContrast());
//        contrast yaklaşık std dev in 4 katı kadardır
//        ret.contrast=FactoryUtils.formatDouble(ret.std*4);
        ret.kurtosis=FactoryUtils.formatDouble(cm.getKurtosis());
        ret.skewness=FactoryUtils.formatDouble(cm.getSkewness());
        ret.entropy=FactoryUtils.formatDouble(cm.getEntropy());
        ret.adaptiveExposureScore=FactoryUtils.formatDouble(cm.getAdaptiveExposureScore());
        return ret;
    }
}
