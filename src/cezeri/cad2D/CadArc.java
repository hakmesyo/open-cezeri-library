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
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author BAP1
 */
public class CadArc extends Cad2DShape {

    public double cx;
    public double cy;
    public double radious;
    public double startAngle;
    public double endAngle;
    public double extentAngle;
    public boolean isCounterClockWise = true;
    public boolean isInner = true;
    Arc2D.Double arc;

    public CadArc(AffineTransform at, double x, double y, double r, double sa, double ea, boolean cc, Color line_color) {
        cx = x;
        setPx(cx-r);
        cy = y;
        setPy(cy-r);
        radious = r;
        setWidth(2*r);
        setHeight(2*r);
        startAngle = 360-sa;
        endAngle = 360-ea;
        if (startAngle < endAngle) {
            extentAngle = 360 - Math.abs(startAngle - endAngle);
        } else {
            extentAngle = startAngle-endAngle;
        }
        isCounterClockWise = cc;
        afft = at;
        setLine_color(line_color);
    }

    @Override
    public void paint(Graphics2D gr, AffineTransform temp_afft, int panel_height) {
        gr.setColor(getLine_color());
        if (isSelected) {
            gr.setColor(Color.YELLOW);
            Line2D.Double line = new Line2D.Double(cx, cy, cx + radious, cy);
            gr.draw(afft.createTransformedShape(line));
            double r=4;
            gr.setColor(Color.RED);
            Rectangle2D.Double rect=new Rectangle2D.Double(cx-radious-r/2, cy-radious-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(cx-radious-r/2, cy+radious-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(cx+radious-r/2, cy+radious-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect=new Rectangle2D.Double(cx+radious-r/2, cy-radious-r/2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            
            gr.setColor(Color.YELLOW);
            AffineTransform orig = gr.getTransform();
            Point2D c = new Point2D.Double(
                    cx,
                    cy);
            Point2D sp = afft.transform(c, new Point2D.Double());
            gr.setTransform(temp_afft);
            gr.drawString("r = " + FactoryUtils.formatDouble(radious,2) + " mm", (int) (sp.getX()), (int) (panel_height - sp.getY()));
            gr.drawString("cx = " + FactoryUtils.formatDouble(cx,2) + " mm   cy = " + FactoryUtils.formatDouble(cy,2) + " mm", (int) (sp.getX()), (int) (panel_height - 15 - sp.getY()));
            gr.setTransform(orig);
        }
        arc = new Arc2D.Double(getPx(), getPy(), 2*radious, 2*radious, startAngle, -extentAngle, Arc2D.OPEN);
        gr.draw(afft.createTransformedShape(arc));
    }

    @Override
    public boolean contains(Point p) {
        return afft.createTransformedShape(arc).contains(p);
    }

}
