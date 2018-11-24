/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cad2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ZoomDemo extends JPanel implements Runnable {

    AffineTransform tx = new AffineTransform();

    Rectangle2D.Double rect1 = new Rectangle2D.Double(100, 100, 30, 60);
    Rectangle2D.Double rect2 = new Rectangle2D.Double(150, 250, 60, 40);
    

    public ZoomDemo() {
        this.addMouseWheelListener(new ZoomHandler());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.draw(tx.createTransformedShape(rect1));
        g2.setColor(Color.BLUE);
        g2.draw(tx.createTransformedShape(rect2));
    }

    private class ZoomHandler implements MouseWheelListener {

        double scale = 1.0;

        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

                Point2D p1 = e.getPoint();
                Point2D p2 = null;
                try {
                    p2 = tx.inverseTransform(p1, null);
                } catch (NoninvertibleTransformException ex) {
// should not get here
                    ex.printStackTrace();
                    return;
                }

                scale -= (0.1 * e.getWheelRotation());
                scale = Math.max(0.1, scale);

                tx.setToIdentity();
                tx.translate(p1.getX(), p1.getY());
                tx.scale(scale, scale);
                tx.translate(-p2.getX(), -p2.getY());

                ZoomDemo.this.revalidate();
                ZoomDemo.this.repaint();
            }
        }
    }

    public void run() {
        JFrame f = new JFrame("Zoom Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(this);
        f.setSize(600, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ZoomDemo());
    }
}
