/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.matrix_processing;

/**
 *
 * @author BAP1
 */
public final class CSize {

    int width;
    int height;
    
    public CSize(int w,int h){
        this.width=w;
        this.height=h;
    }

    @Override
    public String toString() {
        String s = "width:" + width + ", height:" + height;
        return s;
    }

}
