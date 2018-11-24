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
public class CadCircle extends Cad2DShape {

    public double cx;
    public double cy;
    public double radious;
    public boolean isInner = true;
    Ellipse2D.Double circle;

    public CadCircle(AffineTransform afft, double cx, double cy, double radious, Color line_color) {
        this.cx = FactoryUtils.formatDouble(cx,3);
        this.cy = FactoryUtils.formatDouble(cy,3);
        this.radious = FactoryUtils.formatDouble(radious);
        this.afft = afft;
        setPx(cx - radious);
        setPy(cy - radious);
        setWidth(2 * radious);
        setHeight(2 * radious);
        setLine_color(line_color);
    }

    @Override
    public void paint(Graphics2D gr, AffineTransform temp_afft, int panel_height) {
        gr.setColor(getLine_color());
        if (isSelected) {
            gr.setColor(Color.YELLOW);
            Line2D.Double line = new Line2D.Double(cx, cy, cx + radious, cy);
            gr.draw(afft.createTransformedShape(line));
            gr.setColor(Color.RED);
            double r=4;
            circle = new Ellipse2D.Double(cx-r/2, cy-r/2, r, r);
            gr.draw(afft.createTransformedShape(circle));
//            gr.setColor(Color.BLUE);
//            Rectangle2D.Double rect=new Rectangle2D.Double(center_x-radious, center_y-radious, 2*radious, 2*radious);
//            gr.draw(afft.createTransformedShape(rect));
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
                    cy + 3);
            Point2D sp = afft.transform(c, new Point2D.Double());
            gr.setTransform(temp_afft);
            gr.drawString("r = " + FactoryUtils.formatDouble(radious,2) + " mm", (int) (sp.getX()), (int) (panel_height - sp.getY()));
            gr.drawString("cx = " + FactoryUtils.formatDouble(cx,2) + " mm   cy = " + FactoryUtils.formatDouble(cy,2) + " mm", (int) (sp.getX()), (int) (panel_height - 15 - sp.getY()));
            gr.setTransform(orig);

        }

        circle = new Ellipse2D.Double(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        gr.draw(afft.createTransformedShape(circle));
    }

    @Override
    public boolean contains(Point p) {
        return afft.createTransformedShape(circle).contains(p);
    }

    @Override
    public String toString() {
        return "CadCircle{" + "center_x=" + cx + ", center_y=" + cy + ", radious=" + radious + ", isInner=" + isInner + ", circle=" + circle + '}';
    }
}
