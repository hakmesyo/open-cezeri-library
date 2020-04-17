/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.matrix;

import java.io.Serializable;

/**
 *
 * @author BAP1
 */
public final class CPoint implements Serializable{
    public int row=0;
    public int column=0;
    public int weight=0;
    
    public CPoint(){
        
    }
    
    public CPoint(int r,int c){
        row=r;
        column=c;
    }
    
    public CPoint cloneCP(){
        CPoint ret=new CPoint(row,column);
        return ret;
    }
    
    public boolean equals(CPoint cp){
        return this.row==cp.row && this.column==cp.column;
    }
    
    @Override
    public String toString(){
        String s="row:"+row+",column:"+column+",weight:"+weight;
//        System.out.println(s);
        return s;
    }
}
