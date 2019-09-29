/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

/**
 *
 * @author BAP1
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class TestStroke{
  public static void main(String[] args) {
    JFrame jf = new JFrame("Demo");
    Container cp = jf.getContentPane();
    MyCanvas tl = new MyCanvas();
    cp.add(tl);
    jf.setSize(300, 200);
    jf.setVisible(true);
  }
}

class MyCanvas extends JComponent {
  Shape shape;

  public MyCanvas() {
    shape = create();
  }

  protected Shape create() {
    float cm = 72 / 2.54f;
    return new Rectangle2D.Float(cm, cm, 2 * cm, cm);
  }


  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    Stroke s = new BasicStroke(4.0f,                      // Width
                           BasicStroke.CAP_SQUARE,    // End cap
                           BasicStroke.JOIN_MITER,    // Join style
                           10.0f,                     // Miter limit
                           new float[] {16.0f,20.0f}, // Dash pattern
                           0.0f);                     // Dash phase
    Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,new float[] { 3, 1 }, 0);
    Stroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    g2.setStroke(s);

    
    g2.setPaint(Color.black);
    g2.draw(shape);
    
  }
}
