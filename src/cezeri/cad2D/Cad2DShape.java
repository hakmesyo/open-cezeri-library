/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cad2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 *
 * @author BAP1
 */
public abstract class Cad2DShape {    
    private double px = 0;
    private double py = 0;
    private double width = 100;
    private double height = 100;
    private Color line_color = Color.green;
    private Color fill_color = Color.LIGHT_GRAY;
    private int line_width = 1;
    private boolean isFill = false;
    private double zoom = 1;
    public AffineTransform afft;
    public boolean isSelected=false;

    public abstract void paint(Graphics2D gr,AffineTransform temp_afft,int panel_height);
    public abstract boolean contains(Point p);
    
    public double getArea(){
        return width*height;
    }
    
    
    public double getPx() {
        return px;
    }

    public void setPx(double px) {
        this.px = px;
    }

    public double getPy() {
        return py;
    }

    public void setPy(double py) {
        this.py = py;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Color getLine_color() {
        return line_color;
    }

    public void setLine_color(Color line_color) {
        this.line_color = line_color;
    }

    public Color getFill_color() {
        return fill_color;
    }

    public void setFill_color(Color fill_color) {
        this.fill_color = fill_color;
    }

    public int getLine_width() {
        return line_width;
    }

    public void setLine_width(int line_width) {
        this.line_width = line_width;
    }

    public boolean isIsFill() {
        return isFill;
    }

    public void setIsFill(boolean isFill) {
        this.isFill = isFill;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    
}
