/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

/**
 *
 * @author musa-atas
 */
public class TGrayPixel {
    public int value;
    public double corValue; //correlation value
    public int x;
    public int y;    
    
    @Override
    public String toString(){
        String str="value:"+value+"\ncorValue:"+corValue+"\nx:"+x+"\ny:"+y+"\n";
        return str;
    }
}
