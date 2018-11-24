/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;


import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author venap3
 */
public class GCodeWorkList {
    public BufferedImage img;
    public ArrayList<GCodeList> wlst=new ArrayList<GCodeList>();
    
    public void println(String s){
        int i=0;
        String str=s+"\n";
        for (GCodeList gcl : wlst) {
            Point p1=gcl.gCodeList.get(0).currentPos;
            Point p2=gcl.gCodeList.get(gcl.gCodeList.size()-1).currentPos;
            str+=("başı:"+p1.x+","+p1.y+" sonu:"+p2.x+","+p2.y)+"\n";
//            Utils.yaz("-------------------"+(++i)+".iş listesi--------------------------------------------");
//            for (GCode gc : gcl.gCodeList) {
//                Utils.yaz("writemode:"+gc.writeMode);
//                Utils.yaz("current pos("+gc.currentPos.x+","+gc.currentPos.y+")");
//                Utils.yaz("adim ("+gc.p.x+","+gc.p.y+")");
//            }
        }
        FactoryUtils.yaz(str);
    }
}

