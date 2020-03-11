/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.matrix;

/**
 *
 * @author BAP1
 */
public final class CRectangle {
    public int row;
    public int column;
    public int width;
    public int height;
    
    public CRectangle(){
        
    }
    
    public CRectangle(CRectangle cr){
        row=cr.row;
        column=cr.column;
        width=cr.width;
        height=cr.height;
    }
    
    public CRectangle(CPoint cp,int w,int h){
        row=cp.row;
        column=cp.column;
        width=w;
        height=h;
    }
    
    public CRectangle(int r,int c,int w,int h){
        row=r;
        column=c;
        width=w;
        height=h;                
    }
    
    @Override
    public String toString(){
        return "row:"+row+" column:"+column+" width:"+width+" height:"+height;
    }    
}
