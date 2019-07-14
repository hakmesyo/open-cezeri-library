/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import cezeri.matrix.FactoryMatrix;
import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author BAP1
 */
public class FactoryColorMap {
    
    /**
     * Generate default color map. Color ranges from blue(darkest) to red color. 
     * At the center green color is positioned
     * @param d : 1D array to be colored
     * @return : 1D Color Array
     */
    public static Color[] mapColorDefault(double[] d){
        Color[] ret=new Color[d.length];
        double[] n=FactoryNormalization.normalizeIntensity(d, -1,1);
        System.out.println(Arrays.toString(n));
        for (int i = 0; i < n.length; i++) {
            if (n[i]<0) {
                ret[i]=new Color(0.0f, (float)(1.0+n[i]), (float)(-n[i]));
            }else if(n[i]==0){
                ret[i]=new Color(0f,225f,0f); 
            }else{
                ret[i]=new Color((float)(n[i]), (float)(1.0-n[i]), 0f);
            }
            
        }
        return ret;
    }
    
    /**
     * Generate default color map. Color ranges from blue(darkest) to red color. 
     * At the center green color is positioned
     * @param d : 1D array to be colored
     * @return : 1D Color Array
     */
    public static Color[][] mapColorDefault(double[][] d2){
        int nr=d2.length;
        int nc=d2[0].length;
        double[] d=FactoryMatrix.concatenateRows(d2);
        Color[] ret=new Color[d.length];
        double[] n=FactoryNormalization.normalizeIntensity(d, -1,1);
//        System.out.println(Arrays.toString(n));
        for (int i = 0; i < n.length; i++) {
            if (n[i]<0) {
                ret[i]=new Color(0.0f, (float)(1.0+n[i]), (float)(-n[i]));
            }else if(n[i]==0){
                ret[i]=new Color(0f,1f,0f); 
            }else{
                ret[i]=new Color((float)(n[i]), (float)(1.0-n[i]), 0f);
            }
            
        }
        Color[][] col= new Color[nr][nc];
        int z=0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                col[i][j]=ret[z++];
            }
        }
        return col;
    }
    
    /**
     * Generate centered color map. Color ranges from black(coldest) to white (hottest) color. 
     * around the center color given
     * @param d2 : value of center color
     * @param d : 1D array to be colored
     * @return : 1D Color Array
     */
    public static Color[][] mapColorAroundCenterColor(double[][] d2,Color centeredColor){
        int nr=d2.length;
        int nc=d2[0].length;
        double[] d=FactoryMatrix.concatenateRows(d2);
        Color[] ret=new Color[d.length];
        double[] n=FactoryNormalization.normalizeIntensity(d,0,1);
//        System.out.println(Arrays.toString(n));
        double r=centeredColor.getRed();        
        double g=centeredColor.getGreen();
        double b=centeredColor.getBlue();
        
        double d_up_red=255-r;
        double d_up_green=255-g;
        double d_up_blue=255-b;
        
        
        for (int i = 0; i < n.length; i++) {
            if(n[i]==0.5){
                ret[i]=centeredColor; 
            }else if (n[i]<0.5){
                ret[i]=new Color((float)((r-r*2*(0.5-n[i]))/255),
                        (float)((g-g*2*(0.5-n[i]))/255),
                        (float)((b-b*2*(0.5-n[i]))/255));
            }else{
                ret[i]=new Color((float)((r+d_up_red*2*(n[i]-0.5))/255),
                        (float)((g+d_up_green*2*(n[i]-0.5))/255),
                        (float)((b+d_up_blue*2*(n[i]-0.5))/255));
            }
            
        }
        Color[][] col= new Color[nr][nc];
        int z=0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                col[i][j]=ret[z++];
            }
        }
        return col;
    }
}
