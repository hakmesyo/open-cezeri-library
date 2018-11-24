/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cad2D;

import cezeri.utils.FactoryUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author BAP1
 */
public class CadRectangle extends Cad2DShape {

    Rectangle2D.Double rect;

    public CadRectangle(AffineTransform afft, double px, double py, double w, double h, Color line_color) {
        this.setPx(FactoryUtils.formatDouble(px));
        this.setPy(FactoryUtils.formatDouble(py));
        this.setWidth(FactoryUtils.formatDouble(w));
        this.setHeight(FactoryUtils.formatDouble(h));
        this.afft = afft;
        setLine_color(line_color);
    }

    @Override
    public void paint(Graphics2D gr,AffineTransform temp_afft,int panel_height) {
        gr.setColor(getLine_color());
        if (isSelected) {
            double r=4;
//            gr.setColor(Color.BLUE);
//            Rectangle2D.Double rect=new Rectangle2D.Double(getPx()-r, getPy()-r, getWidth()+2*r, getHeight()+2*r);
            gr.draw(afft.createTransformedShape(rect));
            gr.setColor(Color.RED);
            rect=new Rectangle2D.Double(getPx()-r/2, getPy()-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(getPx()-r/2, getPy()+getHeight()-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(getPx()+getWidth()-r/2, getPy()+getHeight()-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(getPx()+getWidth()-r/2, getPy()-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            
            gr.setColor(Color.YELLOW);
            AffineTransform orig = gr.getTransform();
            Point2D cw = new Point2D.Double(
                    getPx()+getWidth()/3,
                    getPy() + getHeight()+5);
            Point2D spw = afft.transform(cw, new Point2D.Double());
            Point2D ch = new Point2D.Double(
                    getPx()+getWidth(),
                    getPy() + getHeight()/2+5);
            Point2D sph = afft.transform(ch, new Point2D.Double());
            Point2D p1 = new Point2D.Double(
                    getPx(),
                    getPy());
            Point2D sp1 = afft.transform(p1, new Point2D.Double());
            gr.setTransform(temp_afft);
            gr.drawString("width = " + FactoryUtils.formatDouble(getWidth(),2) + " mm", (int) (spw.getX()), (int) (panel_height - spw.getY()));            
            gr.drawString("height = " + FactoryUtils.formatDouble(getHeight(),2)+ " mm", (int) (sph.getX()+5), (int) (panel_height-sph.getY()));
            gr.drawString("" + FactoryUtils.formatDouble(getPx(),2)+ " : "+ FactoryUtils.formatDouble(getPy(),2), (int) (sp1.getX()-40), (int) (panel_height-sp1.getY()+20));
            gr.setTransform(orig);
        }
        int lx = (int) Math.round(this.getPx() * 1);
        int ly = (int) Math.round(this.getPy() * 1);
        int w = (int) Math.round(this.getWidth() * 1);
        int h = (int) Math.round(this.getHeight() * 1);
        rect = new Rectangle2D.Double(lx, ly, w, h);
        gr.draw(afft.createTransformedShape(rect));
    }

    @Override
    public boolean contains(Point p) {
        return afft.createTransformedShape(rect).contains(p);
    }

    @Override
    public String toString() {
        return "CadRectangle{" + "rect=" + rect + '}';
    }
}
