/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.matrix_processing;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 *
 * @author BAP1
 */
public final class CRegion implements Serializable{
    public CPoint topLeft;
    public int width;
    public int height;
    
    @Override
    public String toString(){
        String s="row:"+topLeft.row+",column:"+topLeft.column+",width:"+width+",height:"+height;
        System.out.println(s);
        return s;
    }
}
