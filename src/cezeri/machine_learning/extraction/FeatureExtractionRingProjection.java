/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.feature_extraction;

import java.util.ArrayList;

/**
 *
 * @author BAP1
 */
public class FeatureExtractionRingProjection {
    
    public static double[] getRingProjection(double[][] d,int samplingWidth){
        double[] ret=new double[1];
        if (d.length!=d[0].length) {
            return ret;
        }else{
            int n=d.length/(2*samplingWidth);
            ret=new double[n];
            int k=0;
            int cx=d[0].length/2;
            int cy=d.length/2;
            int prevX=0,prevY=0;
            for (int r = 1; r < d.length/2; r+=samplingWidth) {
                int sum=0;
                ArrayList<String> lst=new ArrayList<>();
                for (double theta = 0; theta < 360; theta+=1) {
                    int px=(int)Math.round(cx+r*Math.cos(theta*Math.PI/180));
                    int py=(int)Math.round(cy-r*Math.sin(theta*Math.PI/180));
                    String s=px+","+py;
                    if (!lst.contains(s)) {
                        sum+=d[py][px];
                        lst.add(s);
                    }
//                    if (prevX!=px||prevY!=py) {
//                        sum+=d[py][px];
//                        prevX=px;
//                        prevY=py;
//                    }                    
                }
                ret[k]=sum/360;
                k++;
            }
        }        
        return ret;
    } 
    
    public static void main(String[] args) {
        
    }
}
