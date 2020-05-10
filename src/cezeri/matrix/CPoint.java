/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.matrix;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author BAP1
 */
public final class CPoint implements Serializable{
    public int row=0;
    public int column=0;
    public int weight=0;
    public int limit=0;
    public int counter=0;
    public int rowVector=0;
    public int columnVector=0;
    public String dir;
    public CPoint assignedObject;
    public boolean isAssigned=false;
    
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
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.row;
        hash = 97 * hash + this.column;
        hash = 97 * hash + this.weight;
        hash = 97 * hash + Objects.hashCode(this.dir);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CPoint other = (CPoint) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        if (this.weight != other.weight) {
            return false;
        }
        if (!Objects.equals(this.dir, other.dir)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        String s="row:"+row+",column:"+column+",weight:"+weight;
//        System.out.println(s);
        return s;
    }
}
