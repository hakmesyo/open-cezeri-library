/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import cezeri.matrix.CPoint;
import java.awt.Point;

/**
 *
 * @author HP-pc
 */
public class TRoi {

    public Point cp;
    public CPoint centerPoint;
    public int pr;
    public int pc;
    public int width;
    public int height;

    @Override
    public String toString() {
        return centerPoint.row+","+centerPoint.column+","+pr + "," + pc + "," + width + "," + height;
    }
}
