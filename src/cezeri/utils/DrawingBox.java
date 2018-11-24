/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

 

import java.awt.*;
import java.awt.event.*;

/**
 * <code>DrawingBox</code> is a frame upon which many of the 
 * <code>java.awt.Graphics</code> operations can be performed.  As a
 * frame, it exists as a separate window, and the operations on it may be
 * performed from anywhere -- not just from within a <code>paint</code>
 * method (as is normal with a frame's <code>Graphics</code> object).  
 * (However, to be used effectively as a teaching aid, operations on a
 * <code>DrawingBox</code> <i>should</i> be performed from within 
 * a single method, which later can be converted to a <code>paint</code> 
 * method.)  
 * <p>
 * Care must be taken that when this frame is obscured, that
 * subsequently it can repaint itself.  An off-screen <code>Graphics</code>
 * context keeps an up-to-date copy of this frame's <code>Graphics</code>
 * at all times.  The origin of the coordinate system is translated so that
 * it is in the upper left of the <i>drawable</i> portion of the
 * frame.  Consequently, <code>DrawingBox</code> behaves more like a
 * <code>Canvas</code> object that occupies the drawable portion of a frame.
 * <p>
 * In order to distinguish the <code>Frame</code> from its drawable portion,
 * methods <code>getDrawableWidth</code> and <code>getDrawableHeight</code>
 * are provided.  
 * <p>
 * <code>DrawingBox</code> uses inner classes <code>DBMouseAdapter</code> 
 * (extending <code>java.awt.event.MouseAdapter</code>) to implement mouse 
 * listeners, and <code>DBMouseMotionAdapter</code> (extending 
 * <code>java.awt.event.MouseMotionAdapter</code>) to implement mouse 
 * motion listeners.
 * 
 * @see     ClosableFrame
 * @see     java.awt.Graphics
 * 
 * @author  M. Dennis Mickunas
 * 
 */
public class DrawingBox extends ClosableFrame {

  /** 
   * the <code>Graphics</code> object affiliated with this <code>Frame</code>
   */
  private Graphics g;
  /**
   * the size of the user's screen
   */
  Dimension screenSize;
  /**
   * an off-screen image of everything that is done to <code>g</code>
   */
  private Image buffer;
  /**
   * the <code>Graphics</code> affiliated with the off-screen image
   */
  private Graphics gContext;

  /**
   * Constructs a DrawingBox with the default title.
   */ 
  public DrawingBox () {
    this ("DrawingBox");
  }

  /**
   * Constructs a DrawingBox with a specific title.
   *
   * @param title the string to use as this DrawingBox title.
   * 
   * @see     java.awt.Graphics#translate(int, int)
   * @see     java.awt.Container#getInsets()
   */
  public DrawingBox (String title) {
    super(title);
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBackground(Color.white);
    // By default, set size to 3/4 screen wide by 3/4 screen high.
    setSize(screenSize.width*3/4, screenSize.height*3/4);
    setVisible(true);
    g = getGraphics();
    validate();
    setFont(new Font("Courier", Font.PLAIN, 12));

    // Create an off-screen image, and get its Graphics object
    buffer = createImage(screenSize.width, screenSize.height);
    gContext = buffer.getGraphics();
    gContext.setColor(Color.white);
    gContext.fillRect(0, 0, screenSize.width, screenSize.height);
    gContext.setColor(Color.black);

    // Translate the origin to the upper-left corner of the drawable area.
    g.translate(getInsets().left, getInsets().top);

    addMouseListener(new DBMouseAdapter());
    addMouseMotionListener(new DBMouseMotionAdapter());

  }

    /**
     * <code>DBMouseAdapter</code> is an inner class that implements
     * the Mouse listener for <code>DrawingBox</code>.  It does this
     * by extending the Java convenience wrapper <code>MouseAdapter</code>.
     * <p>
     * Each <code>MouseEvent</code> must be translated to correspond
     * to the translated coordinate system of the <code>DrawingBox</code>. 
     * This translation remains in effect for any downstream mouse
     * listeners.
     *
     * @see     java.awt.event.MouseAdapter
     * @see     java.awt.event.MouseEvent#translatePoint(int int)
     * @see     java.awt.Container#getInsets()
     */
    public class DBMouseAdapter extends MouseAdapter {
      public void mouseClicked(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
      public void mouseEntered(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
      public void mouseExited(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
      public void mousePressed(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
      public void mouseReleased(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
    }
  
    /**
     * <code>DBMouseMotionAdapter</code> is an inner class that implements
     * the Mouse motion listener for <code>DrawingBox</code>.  It does this
     * by extending the Java convenience wrapper 
     * <code>MouseMotionAdapter</code>.  
     * <p>
     * Each <code>MouseEvent</code> must be translated to correspond to the 
     * translated coordinate system of the <code>DrawingBox</code>.  This 
     * translation remains in effect for any downstream mouse motion listeners.
     *
     * @see   java.awt.event.MouseMotionAdapter
     * @see   java.awt.event.MouseEvent#translatePoint(int int)
     * @see   java.awt.Container#getInsets()
     */
    public class DBMouseMotionAdapter extends MouseMotionAdapter {
      public void mouseDragged(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
      public void mouseMoved(MouseEvent e) {
        e.translatePoint(-getInsets().left, -getInsets().top);
      }
    }

  /**
   * Paints the drawing box.  This will be called when the drawing box must
   * be restored.  The off-screen image in <code>buffer</code> has a true
   * copy of what was drawn in the drawing box.
   *
   * @see     java.awt.Container#getInsets()
   */
  public void paint (Graphics g) {
    // Restore the DrawingBox from the offscreen cache,
    // making sure that the insets are avoided.
    if (buffer!=null) 
      g.drawImage(buffer, getInsets().left, getInsets().top, this);  
  }

  /** Returns the current drawable width (less insets) of this drawing box. 
   *
   * @see     java.awt.Container#getInsets()
   *
   * @return  the current drawable width of this drawing box.
   */
  public int getDrawableWidth () { 
    return getSize().width-getInsets().left-getInsets().right; 
  }

  /** Returns the current drawable height (less insets) of this drawing box. 
   *
   * @see     java.awt.Container#getInsets()
   *
   * @return  the current drawable height of this drawing box.
   */
  public int getDrawableHeight () { 
    return getSize().height-getInsets().top-getInsets().bottom; 
  }

  /**
   * Sets the drawable size of this drawing box by setting a smaller size,
   * then increasing it by the amount of the insets.
   *
   * @see     java.awt.Container#getInsets()
   *
   * @param   width   the desired drawable width
   * @param   height  the desired drawable height
   */
  public void setDrawableSize (int width, int height) {
    super.setSize(width, height);
    super.setSize(width+getInsets().left+getInsets().right,
                  height+getInsets().top+getInsets().bottom);
  }

  /**
   * Gets this drawing box's current color.
   *
   * @return this drawing box's current color.
   *
   * @see     java.awt.Color
   * @see     java.awt.Graphics#getColor
   */
  public Color getColor () { return g.getColor(); }

  /**
   * Sets this drawing box's color.
   *
   * @param   c  the desired drawing box's color.
   *
   * @see     java.awt.Color
   * @see     java.awt.Graphics#setColor
   */
  public void setColor (Color c) { 
    g.setColor(c);
    gContext.setColor(c); 
  }

  /** 
   * Draws a line, using the current color, between the points 
   * <i>(x1,y1)</i> and <i>(x2,y2)</i> 
   * in this drawing box's coordinate system. 
   *
   * @param   x1  the first point's <i>x</i> coordinate.
   * @param   y1  the first point's <i>y</i> coordinate.
   * @param   x2  the second point's <i>x</i> coordinate.
   * @param   y2  the second point's <i>y</i> coordinate.
   *
   * @see     java.awt.Graphics#drawLine
   */
  public void drawLine (int x1, int x2, int y1, int y2) {
    gContext.drawLine (x1, x2, y1, y2);
    g.drawLine (x1, x2, y1, y2);
  }

  /** 
   * Draws the outline of the specified rectangle. 
   * The left and right edges of the rectangle are at 
   * <code>x</code> and <code>x+width</code>. 
   * The top and bottom edges are at 
   * <code>y</code> and <code>y+height</code>. 
   * The rectangle is drawn using this drawing box's current color.
   *
   * @param   x       the <i>x</i> coordinate 
   *                       of the rectangle to be drawn.
   * @param   y       the <i>y</i> coordinate 
   *                       of the rectangle to be drawn.
   * @param   width   the width of the rectangle to be drawn.
   * @param   height  the height of the rectangle to be drawn.
   *
   * @see     java.awt.Graphics#drawRect
   */
  public void drawRect (int x, int y, int width, int height) {
    gContext.drawRect (x, y, width, height);
    g.drawRect (x, y, width, height);
  }

  /** 
   * Fills the specified rectangle. 
   * The left and right edges of the rectangle are at 
   * <code>x</code> and <code>x+width-1</code>. 
   * The top and bottom edges are at 
   * <code>y</code> and <code>y+height-1</code>. 
   * The resulting rectangle covers an area 
   * <code>width</code> pixels wide by 
   * <code>height</code> pixels tall.
   * The rectangle is filled using this drawing box's current color. 
   *
   * @param   x       the <i>x</i> coordinate 
   *                      of the rectangle to be filled.
   * @param   y       the <i>y</i> coordinate 
   *                      of the rectangle to be filled.
   * @param   width   the width of the rectangle to be filled.
   * @param   height  the height of the rectangle to be filled.
   *
   * @see     java.awt.Graphics#fillRect
   */
  public void fillRect (int x, int y, int width, int height) {
    gContext.fillRect (x, y, width, height);
    g.fillRect (x, y, width, height);
  }

  /** 
   * Draws the outline of a circle.
   * The result is a circle with center at <i>(x,y)</i>
   * and a given radius. 
   *
   * @param   x       the <i>x</i> coordinate of the center 
   *                      of the circle to be drawn.
   * @param   y       the <i>y</i> coordinate of the center
   *                      of the circle to be drawn.
   * @param   radius  the radius of the circle to be drawn.
   *
   * @see     java.awt.Graphics#drawOval
   */
  public void drawCircle (int x, int y, int radius) {
    gContext.drawOval (x-radius, y-radius, 2*radius, 2*radius);
    g.drawOval (x-radius, y-radius, 2*radius, 2*radius);
  }

  /** 
   * Draws the outline of a circle.
   * The result is a circle with center at <i>p</i>
   * and a given radius.
   *
   * @param   p       the point giving the center of the 
   *                      circle to be drawn.
   * @param   radius  the radius of the circle to be drawn.
   *
   * @see     CSLib#drawCircle(int, int, int)
   */
  public void drawCircle (Point p, int radius) {
    drawCircle (p.x, p.y, radius);
  }

  /** 
   * Fills a circle with center at <i>(x,y)</i>
   * and with a given radius using this drawing box's current color.
   *
   * @param   x       the <i>x</i> coordinate of the center
   *                      of the circle to be filled.
   * @param   y       the <i>y</i> coordinate of the center
   *                      of the circle to be filled.
   * @param   radius  the radius of the circle to be filled.
   *
   * @see     java.awt.Graphics#fillOval
   */
  public void fillCircle (int x, int y, int radius) {
    gContext.fillOval (x-radius, y-radius, 2*radius, 2*radius);
    g.fillOval (x-radius, y-radius, 2*radius, 2*radius);
  }

  /** 
   * Fills a circle with center at point <i>p</i>
   * and with a given radius, using this drawing box's current color.
   *
   * @param   p       the point giving the center of the
   *                      circle to be filled.
   * @param   radius  the radius of the circle to be filled.
   *
   * @see     CSLib#fillCircle(int, int, int)
   */
  public void fillCircle (Point p, int radius) {
    fillCircle(p.x, p.y, radius);
  }

  /** 
   * Draws the text given by the specified string, using this 
   * drawing box's current font and color. The baseline of the 
   * leftmost character is at position <i>(x,y)</i> in this 
   * drawing box's translated coordinate system. 
   *
   * @param   str  the string to be drawn.
   * @param   x    the <i>x</i> coordinate.
   * @param   y    the <i>y</i> coordinate.
   *
   * @see     java.awt.Graphics#drawString
   */
  public void drawString (String str, int x, int y) {
    gContext.drawString (str, x, y);
    g.drawString (str, x, y);
  }

  /** 
   * Fills an oval bounded by the specified rectangle with this
   * drawing box's current color.
   *
   * @param   x       the <i>x</i> coordinate of the upper left corner 
   *                      of the oval to be filled.
   * @param   y       the <i>y</i> coordinate of the upper left corner 
   *                      of the oval to be filled.
   * @param   width   the width of the oval to be filled.
   * @param   height  the height of the oval to be filled.
   *
   * @see     java.awt.Graphics#fillOval
   */
  public void fillOval (int x1, int y1, int width, int height) {
    gContext.fillOval (x1, y1, width, height);
    g.fillOval (x1, y1, width, height);
  }
 
  /** 
   * Draws the outline of an oval.
   * The result is an ellipse that fits within the rectangle 
   * specified by the <code>x</code>, <code>y</code>, 
   * <code>width</code>, and <code>height</code> arguments. 
   *
   * @param   x       the <i>x</i> coordinate of the upper left 
   *                      corner of the oval to be drawn.
   * @param   y       the <i>y</i> coordinate of the upper left 
   *                      corner of the oval to be drawn.
   * @param   width   the width of the oval to be drawn.
   * @param   height  the height of the oval to be drawn.
   *
   * @see     java.awt.Graphics#drawOval
   */
  public void drawOval (int x1, int y1, int width, int height) {
    gContext.drawOval (x1, y1, width, height);
    g.drawOval (x1, y1, width, height);
  }

  /** 
   * Clears the specified rectangle by filling it with the background
   * color of the current drawing box. 
   *
   * @param   x the <i>x</i> coordinate of the rectangle to clear.
   * @param   y the <i>y</i> coordinate of the rectangle to clear.
   * @param   width the width of the rectangle to clear.
   * @param   height the height of the rectangle to clear.
   *
   * @see     java.awt.Graphics#setColor
   * @see     java.awt.Graphics#fillRect
   * @see     java.awt.Graphics#clearRect
   */
  public void clearRect (int x, int y, int width, int height) {
    gContext.setColor(Color.white);
    gContext.fillRect (x, y, width, height);
    gContext.setColor(g.getColor());
    g.clearRect (x, y, width, height);
  }

  /**
   * Clears the entire drawing box.
   *
   * @see     java.awt.Graphics#setColor
   * @see     java.awt.Graphics#fillRect
   * @see     java.awt.Graphics#clearRect
   */
  public void clear() {
    gContext.setColor(Color.white);
    gContext.fillRect (0, 0, screenSize.width, screenSize.height);
    gContext.setColor(g.getColor());
    g.clearRect(0, 0, getDrawableWidth(), getDrawableHeight());
  }

  /** 
   * Draws the specified image with its top-left corner at 
   * <i>(x,y)</i> in this drawing box's coordinate system. 
   * <p>
   * This method waits for the image to be fully loaded.
   *
   * @param   img the specified image to be drawn.
   * @param   x   the <i>x</i> coordinate.
   * @param   y   the <i>y</i> coordinate.
   *
   * @see     java.awt.Graphics#drawImage 
   * @see     java.awt.Image
   * @see     java.awt.MediaTracker
   */
  public void drawImage (Image img, int x, int y) {
    // Wait for the image to be fully loaded.
    MediaTracker checker = new MediaTracker(this);
    checker.addImage(img, 0);
    try { checker.waitForID(0);}
    catch (InterruptedException e){};
    // Now draw it.
    gContext.drawImage(img, x, y, this);
    g.drawImage(img, x, y, this);
  } 
}