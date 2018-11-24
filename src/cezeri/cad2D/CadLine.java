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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author BAP1
 */
public class CadLine extends Cad2DShape {

    Line2D.Double line;
    public double p1x, p1y, p2x, p2y;

    public CadLine(AffineTransform afft, double p1x, double p1y, double p2x, double p2y, Color col) {
        this.p1x = FactoryUtils.formatDouble(p1x);
        this.p1y = FactoryUtils.formatDouble(p1y);
        this.p2x = FactoryUtils.formatDouble(p2x);
        this.p2y = FactoryUtils.formatDouble(p2y);
        this.afft = afft;
        setLine_color(col);
    }

    @Override
    public void paint(Graphics2D gr, AffineTransform temp_afft, int panel_height) {
        gr.setColor(getLine_color());
        if (isSelected) {
            gr.setColor(Color.YELLOW);
            double r = 4;
            gr.setColor(Color.RED);
            Rectangle2D.Double rect = new Rectangle2D.Double(p1x - r / 2, p1y - r / 2, r, r);
            gr.draw(afft.createTransformedShape(rect));
            rect = new Rectangle2D.Double(p2x - r / 2, p2y - r / 2, r, r);
            gr.draw(afft.createTransformedShape(rect));

            gr.setColor(Color.YELLOW);
            AffineTransform orig = gr.getTransform();
            Point2D p1 = new Point2D.Double(
                    p1x,
                    p1y);
            Point2D sp1 = afft.transform(p1, new Point2D.Double());
            Point2D p2 = new Point2D.Double(
                    p2x,
                    p2y);
            Point2D sp2 = afft.transform(p2, new Point2D.Double());
            gr.setTransform(temp_afft);
            gr.drawString(" " + FactoryUtils.formatDouble(p1x, 2) + " : " + FactoryUtils.formatDouble(p1y, 2), (int) (sp1.getX()), (int) (panel_height - sp1.getY()));
            gr.drawString(" " + FactoryUtils.formatDouble(p2x, 2) + " : " + FactoryUtils.formatDouble(p2y, 2), (int) (sp2.getX()), (int) (panel_height - sp2.getY()));
            gr.setTransform(orig);
        }
        line = new Line2D.Double(p1x, p1y, p2x, p2y);
        gr.draw(afft.createTransformedShape(line));
    }

    @Override
    public boolean contains(Point p) {
        int n=3;
//        if (line.x1-n<=p.x && line.x2+n>=p.x && line.y1-n<=p.y && line.y2+n>=p.y) {
//           return true; 
//        }
//        return false;
        return afft.createTransformedShape(line).intersects(p.x-n, p.y-n, 2*n, 2*n);
//        return afft.createTransformedShape(line).contains(p);
    }
}
