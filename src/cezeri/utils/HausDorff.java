/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import java.awt.*;
import java.applet.Applet;
import java.util.Vector;
import java.lang.Math;
import Jama.*;

public class HausDorff extends Applet
{
    PolyArea area;
    Panel control;
    Button runStop;
    boolean running;
    TextField comment;
    
    public void init()
    {
        setLayout (new BorderLayout());
        comment = new TextField ("", 60);
        comment.setEditable (false);
        area = new PolyArea (comment);
        add ("Center", area);
        
        control = new Panel();
        control.add (new Button ("step"));
        runStop = new Button ("run");
        control.add (runStop);
        control.add (new Button ("reset"));
        control.add (comment);
        add("South", control);
        
        running = false;
    }
    
    public boolean action (Event evt, Object arg) 
    {
        if ("step".equals(arg))
            area.stepAlgo();
        
        if ("run".equals(arg))
        {
            runStop.setLabel ("stop");
            startAnim();
            running = true;
        }
        
        if ("stop".equals(arg))
        {
            runStop.setLabel ("run");
            stopAnim();
            running = false;
        }
        
        if ("reset".equals(arg))
        {
            remove (area);
            stopAnim();
            area = new PolyArea (comment);
            add ("Center", area);
            runStop.setLabel ("run");
            validate();
            
            stopAnim();
            running = false;
        }
            
        return true;
    }
    
    public void start()
    {
        if (running)
            startAnim();
    }
    
    public void stop()
    {
        stopAnim();
    }
    
    public void startAnim()
    {
        if (area.animator == null)
        {
            area.animator = new Thread (area);
        }
        area.animator.start();
    }
    
    public void stopAnim()
    {
        area.animator = null;
    }
    
    public void paint(Graphics g) {
        Dimension d = getSize();
        g.setColor (Color.black);
        g.drawRect(0,0, d.width - 1, d.height - 1);
    }
    
    /* This is used to leave room to the black box painted in
     * the paint() method. If we don't do that, it is overwritten.
     */
    public Insets getInsets() {
        return new Insets(3,3,3,3);
    }
}


//=============================================================================


/*
 * The PolyArea class defines an area that will hold our two polygons.
 * It will first create them by catching mouse clicks events and adding
 * the points to the polygons, and it will then run the algorithm on the
 * polygons.
 */
class PolyArea extends Canvas implements Runnable
{
    Dimension offDimension;
    Image offImage;
    Graphics offGraphics;
    
    TextField comment;
    
    public Thread animator;
    
    FConvexPoly poly1, poly2;
    
    int nextStep;
    int currentV1;
    FPoint bestV1, bestV2, currentV2;
    double bestLength;
    int currentRegBase;
    boolean band;
    Polygon currentRegion;
    boolean trigo;
    
    public PolyArea (TextField comment)
    {
        animator = null;
        this.comment = comment;
        
        setBackground (Color.white);
        
        poly1 = new FConvexPoly (Color.green);
        poly2 = new FConvexPoly (Color.red);
        
        nextStep = -1;
        currentV1 = currentRegBase = -1;
        bestV1 = bestV2 = currentV2 = null;
        band = false;
        currentRegion = null;
        bestLength = 0;
        trigo = true;
        
        comment.setText ("Please enter the first polygon");
        comment.setBackground (new Color (220, 255, 230));
        comment.setForeground (Color.black);
    }
    
    public boolean handleEvent (Event e)
    {
        switch (e.id)
        {
            case Event.MOUSE_DOWN:
                if (nextStep == -1)
                {
                    if (! poly1.isClosed())
                    {
                        poly1.addPoint (new FPoint (e.x, e.y));
                        if (poly1.isClosed())
                        {
                            comment.setText ("Please enter the second polygon");
                            comment.setBackground (new Color (255, 220, 220));
                        }
                    }
                    else
                    {
                        poly2.addPoint (new FPoint (e.x, e.y));
                        if (poly2.isClosed())
                        {
                            comment.setText ("Press step or run to see the algorithm");
                            comment.setBackground (new Color (255, 255, 220));
                            nextStep = 0;
                        }
                    }
                }
                
                repaint();
                return true;
            
            default:
                return false;
        }
    }
    
    /* This method performs a step in the algorithm. It is called either
     * by the applet when the "step" button is clicked, or by the animator
     * thread if this one is running. */
    public void stepAlgo ()
    {
        Point p;
        
        switch (nextStep)
        {
        case 0:
            comment.setText ("Searching for the first vertex");
            comment.setBackground (new Color (255, 235, 200));
            
            currentV1 = 0;
            currentRegBase = 0;
            band = false;
            makeRegion();
            
            p = poly1.getPoint (currentV1);
            if (currentRegion.inside (p.x, p.y))
                nextStep = 2;
            else
                nextStep = 1;
            
            repaint();
            break;
        
        case 1:
            if (! band)
                band = true;
            else
            {
                currentRegBase++;
                band = false;
            }
            makeRegion();
            p = poly1.getPoint (currentV1);
            if (currentRegion.inside (p.x, p.y))
                nextStep = 2;
            else
                nextStep = 1;
            
            repaint();
            break;
        
        case 2:
            comment.setText ("Computing length");
            comment.setBackground (new Color (255, 220, 255));
            
            makeV2();
            bestV1 = poly1.getFPoint (currentV1);
            bestV2 = currentV2;
            bestLength = new FVector (bestV1, bestV2).length();
            
            nextStep = 3;
            repaint();
            break;
        
        case 3:
            comment.setText ("Searching for the next vertex");
            comment.setBackground (new Color (255, 235, 200));
            
            if (new FVector (currentV2, poly1.getFPoint (currentV1)).isTrigo
                  (new FVector (currentV2, poly1.getFPoint (currentV1 + 1))))
                trigo = true;
            else
                trigo = false;
            
            currentV1++;
            currentV2 = null;
            if (poly1.getFPoint (currentV1) == poly1.getFPoint (0))
                nextStep = 7;
            else
            {
                p = poly1.getPoint (currentV1);
                if (currentRegion.inside (p.x, p.y))
                    nextStep = 5;
                else
                    nextStep = 4;
            }
            
            repaint();
            break;
        
        case 4:
            if ((trigo || !poly2.isTrigo()) && !(trigo && !poly2.isTrigo()))
                if (! band)
                    band = true;
                else
                {
                    currentRegBase++;
                    band = false;
                }
            else
                if (! band)
                {
                    currentRegBase--;
                    band = true;
                }
                else
                    band = false;
                
            makeRegion();
            p = poly1.getPoint (currentV1);
            if (currentRegion.inside (p.x, p.y))
                nextStep = 5;
            else
                nextStep = 4;
            
            repaint();
            break;
        
        case 5:
            comment.setText ("Comparing this length with the previous best");
            comment.setBackground (new Color (255, 220, 255));
            
            makeV2();
            
            if (new FVector (poly1.getFPoint (currentV1), currentV2).length() > bestLength)
                nextStep = 6;
            else
                nextStep = 3;
            
            repaint();
            break;
        
        case 6:
            comment.setText ("This is the new best length");
            comment.setBackground (new Color (220, 255, 220));
            
            bestV1 = poly1.getFPoint (currentV1);
            bestV2 = currentV2;
            bestLength = new FVector (bestV1, bestV2).length();
            
            nextStep = 3;
            repaint();
            break;
        
        case 7:
            comment.setText ("Hausdorff distance from poly 1 to poly 2");
            comment.setBackground (new Color (220, 220, 255));
            
            currentV1 = -1;
            currentV2 = null;
            currentRegion = null;
            animator = null;
            
            repaint();
            break;
        
        default:
            break;
        }
    }
    
    /* The paint method draws the current state of the algorithm in the
     * given Graphics, including the two polygons, the yellow region and
     * the important points. */
    public void paint (Graphics g)
    {
        poly1.fill (g);
        poly2.fill (g);
        
        if (currentRegion != null)
        {
            g.setColor (Color.yellow);
            g.fillPolygon (currentRegion);
        }
        
        poly1.draw (g);
        poly2.draw (g);
        
        if (currentV1 >= 0)
        {
            Point p = poly1.getPoint (currentV1);
            g.setColor (Color.blue);
            g.drawOval (p.x-5, p.y-5, 10, 10);
        }
        
        if (currentV2 != null)
        {
            Point p1 = poly1.getPoint (currentV1);
            Point p2 = currentV2.getPoint();
            g.setColor (Color.blue);
            g.drawOval (p2.x-5, p2.y-5, 10, 10);
            g.setColor (Color.blue);
            g.drawLine (p1.x, p1.y, p2.x, p2.y);
        }
        
        if (bestV1 != null)
        {
            Point p1 = bestV1.getPoint();
            Point p2 = bestV2.getPoint();
            g.setColor (Color.magenta);
            g.drawLine (p1.x, p1.y, p2.x, p2.y);
        }
    }
    
    /* When called by the AWT, the update method clears its offscreen
     * buffer, calls paint() to draw in it, and then copies the buffer to the
     * screen. */
    public void update (Graphics g)
    {
        Dimension d = size();
        
        if ((offGraphics == null) ||
            (d.width != offDimension.width) ||
            (d.height != offDimension.height))
        {
            offDimension = d;
            offImage = createImage(d.width, d.height);
            offGraphics = offImage.getGraphics();
        }
        
        offGraphics.setColor (getBackground());
        offGraphics.fillRect (0, 0, d.width, d.height);
        paint (offGraphics);
        
        g.drawImage (offImage, 0, 0, this);
    }
    
    /* This method is called in the algorithm to make the new
     * yellow polygon that corresponds to the sweeping yellow
     * region. */
    void makeRegion()
    {
        Polygon region;
        FPoint p1, p2;
        FVector v1, v2, edge;
        
        p1 = poly2.getFPoint (currentRegBase);
        p2 = poly2.getFPoint (currentRegBase + 1);
        edge = new FVector (p1, p2);
        
        if (poly2.isTrigo())
            v1 = edge.normal().mult(-1);
        else
            v1 = edge.normal();
        
        if (band)
        {
            currentRegion = FConvexPoly.infiniteRegion (p1, v1, p2, v1);
        }
        else
        {
            p2 = poly2.getFPoint (currentRegBase - 1);
            edge = new FVector (p2, p1);
            if (poly2.isTrigo())
                v2 = edge.normal().mult(-1);
            else
                v2 = edge.normal();
            
            currentRegion = FConvexPoly.infiniteRegion (p1, v1, p1, v2);
        }
    }
    
    /* This method creates the new end of the current smallest distance
     * that's on the red polygon. If the current vertex of the green polygon
     * is in a yellow sector, it corresponds to the current vertex of the red
     * polygon. If it is in a band, it is the foot of the perpendicular to the
     * current edge of the red polygon that goes through the vertex. */
    void makeV2()
    {
        if (! band)
            currentV2 = poly2.getFPoint (currentRegBase);
        else
        {
            FPoint p1, p2;
            FVector vBase;
            FLine baseLine, perpendicular;
            
            p1 = poly2.getFPoint (currentRegBase);
            p2 = poly2.getFPoint (currentRegBase + 1);
            vBase = new FVector (p1, p2);
            baseLine = new FLine (p1, vBase);
            
            p2 = poly1.getFPoint (currentV1);
            perpendicular = new FLine (p2, vBase.normal());
            
            currentV2 = baseLine.intersect (perpendicular);
        }
    }
    
    /* The run method is called by the virtual machine to perform the
     * automated animation of the algorithm. It calls the stepAlgo() method
     * three times per second to animate the algorithme. */
    public void run()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        
        long startTime = System.currentTimeMillis();
        
        while (Thread.currentThread() == animator)
        {
            stepAlgo();
            
            try
            {
                startTime += 300;
                Thread.sleep (Math.max (0, startTime-System.currentTimeMillis()));
            }
            catch (InterruptedException e) { break; }
        }
    }
}


//=============================================================================


/*
 * The FConvexPoly class represents a convex polygon.
 */
class FConvexPoly
{
    public Vector v;
    boolean closed;
    boolean trigo;
    Color polyColor;
    
    public FConvexPoly (Color c)
    {
        v = new Vector();
        closed = false;
        trigo = false;
        polyColor = c;
    }
    
    public boolean isClosed ()
    {
        return closed;
    }
    
    public boolean isTrigo ()
    {
        return trigo;
    }
    
    public Point getPoint (int i)
    {
        return ((FPoint) v.elementAt (MesMath.mod (i, v.size()))).getPoint();
    }
    
    public FPoint getFPoint (int i)
    {
        return (FPoint) v.elementAt (MesMath.mod (i, v.size()));
    }
    
    public void addPoint (FPoint p)
    {
        if (! closed)
        {
            if (v.size() < 3)
            {
                v.addElement (p);
                if (v.size() == 3)
                    trigo = firstVector().isTrigo (lastVector());
            }
            else
            {
                if (p.equals ((FPoint) v.firstElement()))
                    closed = true;
                else
                {
                    if (isNextOK (p))
                        v.addElement (p);
                }
            }
        }
    }
    
    public boolean isNextOK (FPoint p)
    {
        FVector next;
        
        next = new FVector ((FPoint) v.firstElement(), p);
        if (firstVector().isTrigo (next) != trigo)
            return false;
        
        next = new FVector ((FPoint) v.lastElement(), p);
        if (lastVector().isTrigo (next) != trigo)
            return false;
        
        next = new FVector ((FPoint) v.firstElement(), p);
        if (boundVector().isTrigo (next) == trigo)
            return false;
        else
            return true;
   }
    
    public FVector firstVector()
    {
        return new FVector ((FPoint) v.elementAt (0),
                            (FPoint) v.elementAt (1));
    }
    
    public FVector lastVector()
    {
        return new FVector ((FPoint) v.elementAt (v.size()-2),
                            (FPoint) v.elementAt (v.size()-1));
    }
    
    public FVector boundVector()
    {
        return new FVector ((FPoint) v.elementAt (v.size()-1),
                            (FPoint) v.elementAt (0));
    }
    
    /* Draw the filled polygon on the given graphics. */
    public void fill (Graphics g)
    {
        Polygon p = new Polygon();
        Point pt, pt2;
        FPoint fpt;
        
        for (int a=0; a<v.size(); a++)
        {
            pt = ((FPoint) v.elementAt (a)).getPoint();
            p.addPoint (pt.x, pt.y);
        }
        
        g.setColor (polyColor);
        g.fillPolygon (p);
        
        if (!closed && v.size() >= 3)
        {
            Polygon nextReg = new Polygon();
            
            if (lastVector().isTrigo (firstVector()) == trigo)
            {
                pt = ((FPoint) v.firstElement()).getPoint();
                pt2 = ((FPoint) v.lastElement()).getPoint();
                nextReg.addPoint (pt.x, pt.y);
                nextReg.addPoint (pt2.x, pt2.y);
                
                FLine l1 = new FLine ((FPoint) v.firstElement(),
                                      firstVector());
                FLine l2 = new FLine ((FPoint) v.lastElement(),
                                      lastVector());
                
                fpt = l1.intersect (l2);
                
                pt = fpt.getPoint();
                nextReg.addPoint (pt.x, pt.y);
            }
            else
            {
                FVector firstInv = firstVector().mult (-1);
                nextReg = infiniteRegion ((FPoint) v.firstElement(),
                                          firstInv,
                                          (FPoint) v.lastElement(),
                                          lastVector());
            }
            
            g.setColor (Color.yellow);
            g.fillPolygon (nextReg);
        }
    }
    
    /* Draw the outline of the polygon on the given graphics. */
    public void draw (Graphics g)
    {
        Point pt, pt2;
        
        g.setColor (Color.blue);
        if (v.size() > 0)
        {
            pt = pt2 = ((FPoint) v.firstElement()).getPoint();
            
            for (int a=0; a<v.size()-1; a++)
            {
                pt2 = ((FPoint) v.elementAt (a+1)).getPoint();
                g.drawLine (pt.x, pt.y, pt2.x, pt2.y);
                pt = pt2;
            }
            pt = ((FPoint) v.firstElement()).getPoint();
            
            if (closed)
                g.drawLine (pt.x, pt.y, pt2.x, pt2.y);
            else
            {
                g.setColor (Color.red);
                g.drawOval (pt.x-5, pt.y-5, 10, 10);
            }
        }
    }
    
    /* This method returns a Polygon that will look infinite when
     * drawn by the Graphics methods. This should not be in here, it should
     * not be a static method, but it worked so I left it this way...*/
    public static Polygon infiniteRegion (FPoint p1, FVector v1, FPoint p2, FVector v2)
    {
        Polygon p = new Polygon();
        Point pt;
        double length;
        FVector middle;
        
        pt = p1.getPoint();
        p.addPoint (pt.x, pt.y);
        
        if (! p1.equals (p2))
        {
            pt = p2.getPoint();
            p.addPoint (pt.x, pt.y);
        }
        
        length = v2.length();
        pt = new Point ((int) (p2.x + (v2.x / length * 2000)),
                        (int) (p2.y + (v2.y / length * 2000)));
        p.addPoint (pt.x, pt.y);
        
        middle = (v1.mult (1/v1.length())).add (v2.mult (1/v2.length()));
        length = middle.length();
        pt = new Point ((int) (middle.x + (middle.x / length * 2000)),
                        (int) (middle.y + (middle.y / length * 2000)));
        p.addPoint (pt.x, pt.y);
        
        length = v1.length();
        pt = new Point ((int) (p1.x + (v1.x / length * 2000)),
                        (int) (p1.y + (v1.y / length * 2000)));
        p.addPoint (pt.x, pt.y);
        
        return p;
    }
}


//=============================================================================


/*
 * The FPoint class represents a point in the plane.
 */
class FPoint
{
    static double EPSILON = 10;
    
    double x;
    double y;
    
    public FPoint (double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public FPoint (int x, int y)
    {
        this.x = (double) x;
        this.y = (double) y;
    }
    
    public FPoint (Point p)
    {
        this.x = (double) p.x;
        this.y = (double) p.y;
    }
    
    public boolean equals (FPoint p)
    {
        if (new FVector (this, p).length() < EPSILON)
            return true;
        else
            return false;
    }
    
    public Point getPoint()
    {
        return new Point ((int) x, (int) y);
    }
}


//=============================================================================


/*
 * The FVector class provides a basic 2-dimensional vector type,
 * along with some uselful methods.
 */
class FVector
{
    double x;
    double y;
    
    public FVector (double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public FVector (FPoint a, FPoint b)
    {
        x = b.x - a.x;
        y = b.y - a.y;
    }
    
    /* Returns the dot product of the vector with v. */
    public double dot (FVector v)
    {
        return x * v.x + y * v.y;
    }
    
    /* Returns the vector normal to this vector. */
    public FVector normal()
    {
        return new FVector (-y, x);
    }
    
    /* Return true if v makes an angle with the vector in the
     * trigonometric direction (a left turn). */
    public boolean isTrigo (FVector v)
    {
        if (dot (v.normal()) <= 0)
            return true;
        else
            return false;
    }
    
    public double length()
    {
        return Math.sqrt (x*x + y*y);
    }
    
    public FVector add (FVector v)
    {
        return new FVector (x + v.x, y + v.y);
    }
    
    public FVector mult (double a)
    {
        return new FVector (x*a, y*a);
    }
}


//=============================================================================


/*
 * The FLine class represents a line. Here, it is only used to call
 * its intersect() method to compute the intersection of two lines.
 */
class FLine
{
    /* The line is under the form ax+by=c */
    double a;
    double b;
    double c;
    
    /*
     * Creates a new FLine that goes through p and is supported
     * by v.
     */
    public FLine (FPoint p, FVector v)
    {
        FVector vn = v.normal();
        a = vn.x;
        b = vn.y;
        
        c = a * p.x + b * p.y;
    }
    
    /* Creates a new FLine that goes through both a and b. */
    public FLine (FPoint a, FPoint b)
    {
        this (a, new FVector (a, b));
    }
    
    /*
     * The methode computes the intersection point of two
     * lines. We use the JAMA matrix package (see
     * <a href="http://math.nist.gov/javanumerics/jama/">
     * http://math.nist.gov/javanumerics/jama/</a>
     * for details).
     */
    public FPoint intersect (FLine l)
    {
        double[][] tabA = {{  a,   b},
                           {l.a, l.b}};
        
        Matrix matA = new Matrix (tabA);
        
        double[][] tabB = {{  c},
                           {l.c}};
        
        Matrix matB = new Matrix (tabB);
        
        double[][] tabX = (matA.solve (matB)).getArray();
        
        return new FPoint (tabX[0][0], tabX[1][0]);
    }
}


//=============================================================================


/*
 * The MesMath class is an abstract class that is used to perform
 * division related operations on integers. I haven't been able to tell
 * java that I needed my divisions to be rounded toward minus infinity
 * rather than zero, so I've had to do it myself.
 */
abstract class MesMath
{
    public static int div (int a, int b)
    {
        if (a >= 0)
            return a / b;
        else
            return (a - b + 1) / b;
    }
    
    public static int mod (int a, int b)
    {
            return a - div (a, b) * b;
    }
}















