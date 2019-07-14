/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


/**
 *
 * RotatingCube.java
 *
 *   - It displays a 2D projection of a 3D rotation cube.
 *   - The user can rotate cube with mouse, including coasting on mouse up.
 *   - It runs as an application or an applet.
 *   - It includes various 3D classes and viewpoint projection stuff.
 *   - I wrote it as a starting point for playing around with 3D graphics
 *     for (perhaps eventually) things like RubiksCube-ish GUI's.
 *
 * All the JDK1.3/Swing based classes are here in this file.
 * (Yes, I know that isn't very java-ish.  So sue me.
 *  I'll likely do the package/jar edu.marlboro.cs.is.it.soup.yet stuff later.)
 *
 * Compile the lot from the command line with "javac RotatingCube.java".
 * Run it from the command line with "java RotatingCube", 
 * or from a web browser pointed at RotatingCube.html.
 *
 * v 0.33, July 17 2003
 * Copyright Jim Mahoney (mahoney@marlboro.edu), Marlboro College.
 * May be used and modified freely under the Artistic License,
 * http://cs.marlboro.edu/home/Artistic.html.
 *
 *  To do ideas :
 *    - break into a package and separate class files
 *    - write api tests
 *    - add a text window for diagnostics/commands
 *    - Rubik's stuff: multiple cubies, slice rotations, etc.
 *
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ====================================================================== //
// == The top-level application specific classes.
// == Only the first is public; all other classes in this file are private.
// ==   RotatingCube  is an ApplicationOrApplet containing a CubeWorld,
// ==   CubeWorld     is a World3D containing a spinnable CubeBody,
// ==   CubeBody      is a colored cube Body3D.
// ====================================================================== //

/**
 * Outermost application/applet for the rotating cube.
 * @see ApplicationOrApplet
 */
public class TestRotatingCube2 extends ApplicationOrApplet {
    public static void main(String[] args) {
        runMain(new TestRotatingCube2());
    }
    void buildUserInterface(Container container) {
        // If we're adding more than one piece, then we'll need a layout, like
        //   container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        // But right now I'm only adding one, so I don't bother.
        CubeWorld cw = new CubeWorld(this);
        container.add(cw);
    }
}

/**
 * A "World3D" environment that contains a single colored cube.
 * @see World3D
 */
class CubeWorld extends World3D {
    CubeWorld(ApplicationOrApplet controller){
        super(controller);
        itsBody = new CubeBody();
        // A randomly chosen starting orientation.
        itsBody.rotateInPlace( Math.PI/4.0, Math.PI/5.0, -Math.PI/7.0 );  
        startMouseInputAndAnimation( spinFromMouseDrag );
    }
    public void actionPerformed(ActionEvent e){
        continueSpinningAfterMouseup();
    }
}

/**
 * A cube with colored sides and darkened edges.
 * @see Body3D
 */
class CubeBody extends Body3D {
    CubeBody(){
        double borderSize = 1.0;
        double edgeLength = 10.0;
        double xBodyCenter = 0.0;
        double yBodyCenter = 0.0;
        double zBodyCenter = -5.0;
        // 4 corner points, each with 3 coords, around each of 6 sides.
        double a = edgeLength/2.0;
        double[] zPlus  = { a, a, a,  -a, a, a,  -a,-a, a,   a,-a, a};
        double[] zMinus = {-a,-a,-a,  -a, a,-a,   a, a,-a,   a,-a,-a};
        double[] xPlus  = { a, a, a,   a,-a, a,   a,-a,-a,   a, a,-a};
        double[] xMinus = {-a,-a,-a,  -a,-a, a,  -a, a, a,  -a, a,-a};
        double[] yPlus  = { a, a, a,   a, a,-a,  -a, a,-a,  -a, a, a};
        double[] yMinus = {-a,-a,-a,   a,-a,-a,   a,-a, a,  -a,-a, a};
        addSquareSide( zPlus,  Color.red,     borderSize);
        addSquareSide( zMinus, Color.blue,    borderSize);
        addSquareSide( xPlus,  Color.green,   borderSize);
        addSquareSide( xMinus, Color.magenta, borderSize);
        addSquareSide( yPlus,  Color.yellow,  borderSize);
        addSquareSide( yMinus, Color.orange,  borderSize);
        setCenter(new Vector3D(xBodyCenter,yBodyCenter,zBodyCenter));
        setRadius(a);  // approx radius for drag-rotate stuff.
    }
    /**
     * Adds one side to the cube, by calling addPolygon3D() repeatedly
     * to add a colored squares with borders made of 4 thin trapezoidal edges
     * for each of the six cube faces.
     */
    public void addSquareSide(double[] pts, Color faceColor, 
                              double borderSize){
        Vector3D[] v = new Vector3D[4];
        for (int i=0; i<4; i++){   // 4 corners per side, 3 coords per vector
            v[i] = new Vector3D( pts[3*i+0], pts[3*i+1], pts[3*i+2] );
        }
        Vector3D[] vInner = new Vector3D[4];
        for (int i=0; i<4; i++){
            Vector3D vToNext = v[(i+1)%4].minus(v[i]);
            Vector3D vToPrev = v[(i+3)%4].minus(v[i]);
            Vector3D toCenter = (vToNext.plus(vToPrev)).normalize();
            vInner[i] = v[i].plus( toCenter.scale(borderSize) );
        }
        // "border" darker trapezoid polygons around the inner square.
        for (int i=0; i<4; i++){            
            Polygon3D pBorder = new Polygon3D();
            pBorder.setFaceColor(faceColor.darker().darker());
            pBorder.addVector(v[i]);
            pBorder.addVector(v[(i+1)%4]);
            pBorder.addVector(vInner[(i+1)%4]);
            pBorder.addVector(vInner[i]);
            addPolygon3D(pBorder);
        }
        // "inner" colored square.
        Polygon3D pInner = new Polygon3D();
        pInner.setFaceColor(faceColor);
        for (int i=0; i<4; i++){
            pInner.addVector(vInner[i]);
        }
        addPolygon3D(pInner);
    }
}

// ======================================================================== //
// === generic ApplicationOrApplet framework ============================== //
// ======================================================================== //

/**
 * The structure of this application/applet template is
 * adapted from a Sun tutorial at http://java.sun.com/
 * docs/books/tutorial/uiswing/painting/example-swing/RectangleDemo.java.
 *
 * To use it, write a subclass which extends this one and
 * overrides the main() and buildUserInterface() methods.
 * 
 * @see TestRotatingCube2
 */
abstract class ApplicationOrApplet extends JApplet {
    public static boolean runningAsApplet;
    /** 
     * Called only when run as an application.
     *
     * Inherited classes should override this method like this:
     *  <code><pre>
     *  public static void main(String[] args){
     *    runMain(new YourSubclassName());
     *  }
     *  </pre></code>
     */
    public static void main(String[] args){    
        // runMain(new YourSubclassName()); 
    }
    /** 
     * Called only when this is run as an applet.
     */
    public void init() {
        runningAsApplet = true;
        buildUserInterface(getContentPane());
    }
    /**
     * The main() method calls this to run application.
     */
    public static void runMain(ApplicationOrApplet controller){
        runningAsApplet = false;
        String windowTitle = controller.getClass().getName();
        JFrame frame = new JFrame(windowTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller.buildUserInterface(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Add the various GUI components.
     * Subclasses should override this, adding their particular components.
     */
    void buildUserInterface(Container container) {
    }
}

// ====================================================================== //
// === generic 3D stuff : Vector3D, Polygon3D, Body3D, World3D ========== //
// ====================================================================== //

/**
 * A double-precision coordinate or direction in a three dimensional space, 
 * including various arithmetic operations, i.e. dot, plus, minus, etc.
 * (Note that this is unrelated to the java.util "Vector" collection class.)
 */
class Vector3D {
    public static final int nPoints = 3;
    private double[] p;
    private void init(){ 
        p = new double[nPoints]; 
    }
    /**
     * Create a Vector3D with the same coordinates as a given Vector3D.
     */
    Vector3D(Vector3D v){
        init();
        p[0]=v.p[0]; p[1]=v.p[1]; p[2]=v.p[2];
    }
    Vector3D(double x, double y, double z){
        init();
        p[0]=x; p[1]=y; p[2]=z;
    }
    Vector3D(int x, int y, int z){
        init();
        p[0]=(double)x; p[1]=(double)y; p[2]=(double)z;
    }
    /**
     * Default values for x, y, and z are 0.0.
     */
    Vector3D(){
        init();
        p[0]=0.0; p[1]=0.0; p[2]=0.0;
    }
    public double getX(){ return p[0]; }
    public double getY(){ return p[1]; }
    public double getZ(){ return p[2]; }
    public void setX(double x){ p[0]=x; }
    public void setY(double y){ p[1]=y; }
    public void setZ(double z){ p[2]=z; }
    public double[] getXYZ(){ return p; }
    public void setXYZ(double x, double y, double z){
        p[0]=x; p[1]=y; p[2]=z;
    }
    public void setXYZ(double[] xyz){
        p[0]=xyz[0]; p[1]=xyz[1]; p[2]=xyz[2];
    }
    public void setXYZ(Vector3D v){
        p[0]=v.p[0]; p[1]=v.p[1]; p[2]=v.p[2];
    }
    public double getNth(int n){ return p[n]; }
    public void setNth(int n, double a){ p[n] = a; }
    /**
     * The scalar "dot" product of two vectors, i.e.
     * <pre>
     *   A.dot(B) = A.getX()*B.getX() + A.getY()*B.getY() + A.getZ()*B.getZ()
     * </pre>
     */
    public double dot(Vector3D v){
        double s=0.0;
        for (int i=0; i<nPoints; i++){ 
            s += getNth(i)*v.getNth(i); 
        }
        return s;
    }
    /**
     * The vector sum of A and B is 
     * <pre>
     *   A.plus(B) = 
     *   Vector3D( A.getX()+B.getX(), A.getY()+B.getY(), A.getZ()+B.getZ() ).
     * </pre>
     */
    public Vector3D plus(Vector3D v){
        Vector3D p = new Vector3D();
        for (int i=0; i<nPoints; i++){ 
            p.setNth(i, getNth(i) + v.getNth(i) );
        }
        return p;
    }
    /**
     * The vector difference of A and B is 
     * <pre>
     *   A.minus(B) = 
     *   Vector3D( A.getX()-B.getX(), A.getY()-B.getY(), A.getZ()-B.getZ() ).
     * </pre>
     */
    public Vector3D minus(Vector3D v){
        Vector3D p = new Vector3D();
        for (int i=0; i<nPoints; i++){ 
            p.setNth(i, getNth(i) - v.getNth(i) );
        }
        return p;
    }
    /**
     * Return a new Vector3D by rotating the given one through
     * the Euler angles (phi,theta,psi), i.e.
     * <ol>
     *  <li> rotation by phi around the Z axis, then
     *  <li> rotation by theta around X axis, and then
     *  <li> rotation by psi around Z axis.
     * </ol>
     */
    public Vector3D rotate(double phi, double theta, double psi){
        double x,y,z,xNew,yNew,zNew,cos,sin;
        x=getX(); 
        y=getY(); 
        z=getZ();
        cos=Math.cos(phi);           // rotate around Z
        sin=Math.sin(phi);
        xNew=cos*x-sin*y; 
        yNew=sin*x+cos*y;
        x=xNew; 
        y=yNew;
        cos=Math.cos(theta);         // rotate around x
        sin=Math.sin(theta);
        yNew=cos*y-sin*z; 
        zNew=sin*y+cos*z;
        y=yNew; 
        z=zNew;
        cos=Math.cos(psi);          // rotate around z again
        sin=Math.sin(psi);
        xNew=cos*x-sin*y; 
        yNew=sin*x+cos*y;
        x=xNew; 
        y=yNew;
        // put results into a vector and return 'em
        return new Vector3D(x,y,z);
    }
    /** 
     * Modify the given Vector3D by a rotating it through the given
     * Euler angles (phi,theta,psi).  
     * (See the rotate() method for an explanation of these angles.)
     */
    public void rotateInPlace(double phi, double theta, double psi){
        rotateInPlace( Math.cos(phi),   Math.sin(phi),
                       Math.cos(theta), Math.sin(theta),
                       Math.cos(psi),   Math.sin(psi)
                       );
    }
    /** 
     * Modify the given Vector3D by a rotating it through the given
     * Euler angles (phi,theta,psi).  Since its common to rotate many
     * Vector3D's through the same angles, this version takes the cosine
     * and sine of the angles, computed in the calling routine, so that
     * they aren't recalculated repeatedly.
     * (See the rotate() method for an explanation of these angles.)
     */
    public void rotateInPlace(double cosPhi,   double sinPhi, 
                              double cosTheta, double sinTheta, 
                              double cosPsi,   double sinPsi){
        double x,y,z,xNew,yNew,zNew;
        x=getX(); 
        y=getY(); 
        z=getZ();
        xNew = x*cosPhi   - y*sinPhi;      // rotate around z
        yNew = x*sinPhi   + y*cosPhi;
        x=xNew; 
        y=yNew;
        yNew = y*cosTheta - z*sinTheta;    // rotate around x
        zNew = y*sinTheta + z*cosTheta;
        y=yNew; 
        z=zNew;
        xNew = x*cosPsi   - y*sinPsi;      // rotate around z again
        yNew = x*sinPsi   + y*cosPsi;
        x=xNew; y=yNew;
        setXYZ(x,y,z);                     // put results back into the vector.
    }
    /**
     * Return the length of the Vector3D, defined as
     * <pre>
     *   A.length() = 
     *   Math.sqrt( A.getX()*A.getX() + A.getY()*A.getY() + A.getZ()*A.getZ())
     * </pre>
     */
    public double length(){
        double answer=0.0;
        for (int i=0; i<nPoints; i++){
            double ith = getNth(i);
            answer += ith*ith;
        }
        return Math.sqrt(answer);
    }
    /**
     * Return a new Vector3D in the same direction as this one but
     * whose length is one.
     */
    public Vector3D normalize(){
        return scale(1.0/length());
    }
    /**
     * Return a new Vector3D whose length has been multiplied by the given
     * scalar.  
     * <pre>
     *   A.scale(r) = Vector3D( r*A.getX(), r*A.getY(), r*A.getZ() )
     * </pre>
     */
    public Vector3D scale(double scale){
        Vector3D v = new Vector3D();
        for (int i=0; i<nPoints; i++){ 
            v.setNth(i, scale*getNth(i) );
        }
        return v;
    }
    /**
     * The vector cross product of two Vector3D's, i.e.
     * <pre>
     *   A.cross(B) = Vector3D( A.getY()*B.getZ() - A.getZ()*B.getY(),
     *                          A.getZ()*B.getX() - A.getX()*B.getZ(),
     *                          A.getX()*B.getY() - A.getY()*B.getX() )
     * </pre>
     */
    public Vector3D crossProduct(Vector3D v){
        Vector3D p = new Vector3D();
        p.setNth(0, getNth(1)*v.getNth(2) - getNth(2)*v.getNth(1));
        p.setNth(1, getNth(2)*v.getNth(0) - getNth(0)*v.getNth(2));
        p.setNth(2, getNth(0)*v.getNth(1) - getNth(1)*v.getNth(0));
        return p;
    }
    /**
     * Return the coordinates of the Vector3D as a string
     * in the form "(x,y,z)".
     */
    String asText(){
        return "("+getNth(0)+","+getNth(1)+","+getNth(2)+")";
    }
}

/**
 * A collection of Vector3D's in a single plane, 
 * oriented by the typical right-hand rule - that is, the first three
 * points taken in the counter-clockwise direction define the 
 * face which is "outward" from the body.  
 * @see Vector3D
 */
class Polygon3D {
    private static final int initialArraySize = 6;
    private Color faceColor;
    private ArrayList vectors;
    private Vector3D meanPosition;
    /**
     * Return an empty Polygon3D.  After this initialization, 
     * use sequential calls to addVector3D() to define the vertices.
     */
    Polygon3D(){ 
        vectors = new ArrayList(initialArraySize); 
        meanPosition = null;
    }
    public void setFaceColor(Color c){ faceColor = c; }
    public Color getFaceColor(){ return faceColor; }
    /**
     * Append a Vector3D vertex to this Polygon3D.
     */
    public void addVector(Vector3D p){ 
        // Don't want same vector appearing twice, 
        // so I'll clone this before adding it in.
        vectors.add( new Vector3D(p) ); 
        meanPosition = null;   // signal that this is no longer correct.
    }
    private void setMeanPosition(){
        double meanX=0.0, meanY=0.0, meanZ=0.0;
        int nPoints = vectors.size();
        Iterator i = vectors.iterator();
        while (i.hasNext()){
            Vector3D v = (Vector3D)i.next();
            meanX += v.getX(); 
            meanY += v.getY(); 
            meanZ += v.getZ();
        }
        meanX /= nPoints; 
        meanY /= nPoints; 
        meanZ /= nPoints;
        meanPosition = new Vector3D(meanX, meanY, meanZ);
    }
    /**
     * The mean position is the average of the vertex Vector3D's,
     * essentially the "center" of the polygon.
     */
    public Vector3D getMeanPosition(){
        if (meanPosition==null){ setMeanPosition(); }
        return meanPosition;
    }
    /**
     * Return an iterator which will run over the polygon vertices.
     */
    public Iterator getVector3DIterator(){ return vectors.iterator(); }
    /**
     * Modify the given Polygon3D by rotating it through the given
     * Euler angles.  
     * @see Vector3D
     */
    public void rotateInPlace(double cosPhi,   double sinPhi, 
                              double cosTheta, double sinTheta, 
                              double cosPsi,   double sinPsi){
        //System.out.println("     Polygon3D.rotateInPlace "+this);
        //System.out.println("       number of vectors = "+vectors.size());
        Iterator i = vectors.iterator();
        while (i.hasNext()){
            Vector3D v = (Vector3D)i.next();
            //System.out.println("         "+v);
            v.rotateInPlace(cosPhi,sinPhi,cosTheta,sinTheta,cosPsi,sinPsi);
        }
        if (meanPosition==null){
            setMeanPosition();
        }
        else {
            meanPosition.rotateInPlace(cosPhi,sinPhi,
                                      cosTheta,sinTheta,cosPsi,sinPsi);
        }
    }
    /**
     * Return a normalized Vector3D perpendicular to the polygon plane
     * in the "outward" direction.
     */
    public Vector3D getOutwardVector3D(){
        // Let first three points be a,b,c.  
        // Then outward = (b-a) x (c-a)
        Vector3D a = (Vector3D)vectors.get(0);
        Vector3D b = (Vector3D)vectors.get(1);
        Vector3D c = (Vector3D)vectors.get(2);
        Vector3D bMinusA = b.minus(a);
        Vector3D cMinusA = c.minus(a);
        Vector3D outward = bMinusA.crossProduct(cMinusA);
        return outward.normalize();
    }
}

/**
 * A collection of Polygon3D's in single three-dimensional body.
 */
class Body3D {
    private static final int initialArraySize = 24;
    private Vector3D bodyCenter;
    private double bodyRadius;  // approx radial size, for drag-rotate stuff
    private ArrayList polygons;
    Body3D(){
        polygons = new ArrayList(initialArraySize);
    }
    /**
     * Append another Polygon3D to this Body3D.
     */
    public void addPolygon3D(Polygon3D p){ polygons.add(p); }
    /**
     * Define the center of this body.
     */
    public void setCenter(Vector3D v){ bodyCenter = v; }
    public Vector3D getCenter(){ return bodyCenter; }
    /**
     * The body radius is an average size of the body, 
     * used for turning the body with mouse click and drags.
     */
    public void setRadius(double r){   bodyRadius = r; }
    public double getRadius(){ return bodyRadius; }
    /**
     * Return an iterator which will loop over the Polygon3D's that
     * make up this Body3D.
     */
    public Iterator getPolygon3DIterator(){
        return polygons.iterator();
    }

    /**
     * Rotate around bodyCenter, as specified by given Euler angles,
     * i.e. rotate by phi around z, then theta around x, and then psi around z
     * @see Vector3D.rotateInPlace()
     */
    public void rotateInPlace(double phi, double theta, double psi){
        //System.out.println("\n Body3D.rotateInPlace "+this);
        //System.out.println(  "  number of polygons = "+polygons.size());
        //System.out.println(  "  phi   = pi/"+(Math.PI/phi));
        //System.out.println(  "  theta = pi/"+(Math.PI/theta));
        //System.out.println(  "  psi   = pi/"+(Math.PI/theta));
        double cosPhi   = Math.cos(phi);
        double sinPhi   = Math.sin(phi);
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double cosPsi   = Math.cos(psi);
        double sinPsi   = Math.sin(psi);
        Iterator i = polygons.iterator();
        while (i.hasNext()){
            Polygon3D p3D = (Polygon3D)i.next();
            //System.out.println("   "+p3D);
            p3D.rotateInPlace(cosPhi,sinPhi,cosTheta,sinTheta,cosPsi,sinPsi);
        }
    }
}

/**
 * A World3D can display a single Body3D from a given viewpoint, and
 * knows how to paint Body3D and Polygon3D objects,
 * It also includes some mouse event handlers and an animation timer
 * for rotating its Body3D which can be turned on from classes
 * which inherit from this one.
 *
 *     The pixel coord system in the Graphics object g is given
 *     by the JPanel's getX(), getY(), getWidth(), and getHeight() methods.
 *     typically with (0,0) at the top left.   X and Y increase to 
 *     the right and down respectively.  All these values are ints.
 *     I'll call them xpix, ypix (pixel) when I want to distinguish 
 *     them from other coordinate systems.
 * <pre>
       xPixOrigin   -    xPixOrigin+width
       -------------------- yPixOrigin
       |                  |
       |                  |  |             Graphics (xpix,ypix)
       |                  |  v
       |                  |  
       |                  |
       -------------------- yPixOrigin+height
 * </pre>
 *
 *     The World3D coord system uses doubles, with positive z out
 *     of the plane of the screen towards observer, negative z into
 *     the computer screen, and (x,y) increasing to the right and upwards,
 *     as in typical mathematical geometry.  The (0.0,0.0,0.0) origin is 
 *     typically somehere near the middle of the window.  The edges of
 *     the computer window are at xMin, xMax, yMin, yMax, z=0 as shown below.
 *     I'll call these (xcoord,ycoord,zcoord) when I want to distinguish
 *     them from the Graphics values.
 * <pre>
       xMin    -        xMax
       -------------------- yMax
       |                  |
       |      |           |  ^
       | -----+-------    |  |             World3D (xcoord,ycoord)
       |      |(0,0,0)    |                
       |                  |
       -------------------- yMin
 * </pre>
 *
 * @see CubeWorld
 */
class World3D extends JPanel implements ActionListener {
    JApplet controller;
    Graphics graphics;
    /**
     * The single Body3D contained in this world.
     */
    protected Body3D itsBody;      

    /**
     * Default reasonable values for sizes, viewpoint, and 3D coord
     * system are set when a World3D is created; if you want other
     * values, set them explictly afterward.
     * Applet parameters are also handled here, 
     * though only one (bgcolor) is currently recognized.
     */
    public World3D(ApplicationOrApplet controller){
        int preferredWindowWidth = 400;           // window ~ 400 x 400 pixels
        int preferredWindowHeight = 400;
        double xSize = 20.0;                      // coords ~ -20.0 to 20.0
        double ySize = 20.0;
        double xViewpoint = 0.0;
        double yViewpoint = 0.0;
        double zViewpoint = 50.0;
        Color bgColor = Color.white;
        this.controller = controller;
        itsBody = null;
        setPreferredSize(preferredWindowWidth,preferredWindowHeight);
        setCoordScales(-xSize/2,xSize/2,-ySize/2,ySize/2,
                       getX(),getY(),getWidth(),getHeight());
        setViewpoint(new Vector3D(xViewpoint,yViewpoint,zViewpoint));
        setBackground(bgColor);
        
        // applet parameters :
        //       <param name="bgcolor" value="#FFFFFF"></param>
        if (controller.runningAsApplet){
            try {
                String bgcolorParam = controller.getParameter("bgcolor");
                Integer c  = Integer.decode(bgcolorParam);
                setBackground(new Color(c.intValue()));
            }
            catch (Exception e){ 
                // ignore problems
            }
        }

        // Event handlers and animations should be started by the subclass,
        // though I suppose I could have done that here.  
        // For the spin/drag stuff, all it takes is
        //   startMouseInputAndAnimation(spinFromMouseDrag);
    }
    
    Vector3D viewpoint;
    public void setViewpoint(Vector3D vp){ viewpoint = vp; }
    public Vector3D getViewpoint(){ return viewpoint; }

    Dimension preferredSize;
    public void setPreferredSize(int width, int height){ 
        preferredSize = new Dimension(width,height);
    }
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    // -------- mouse event and animation routines  ---------------------
    //    * spin body from user click/drag
    //    * continue spinning (animation) after mouse is released

    boolean frozen = false;
    javax.swing.Timer timer;
    double framesPerSecond;
    long lastDragTime;
    final long releaseDelayThreshold = 50; // milliseconds
    int lastMouseX, lastMouseY;
    double oldX, oldY;
    double phiSpin, thetaSpin;
    int frameCounter = 0;
    final double spinDecayTime = 3.0;  // in seconds
    boolean isSpinning = false;
    double dragScale=0.0;
    /**
     * This messy routine returns a geometric multiplicative
     * factor used when rotating by dragging the mouse.
     * Let x = coord distance in z=0 plane, then
     * x*scaleFactor = angle theta that body is turned through, or
     * at least a reasonable approximation.
     */
    double getDragToThetaScale(){
        if (dragScale==0.0){  // calculate this once only.
            Vector3D bodyToView = viewpoint.minus(itsBody.getCenter());
            double r = itsBody.getRadius();
            double frac = (bodyToView.length()-r)/viewpoint.length();
            // The 1.5 factor below was found by good old trial'n'error.
            dragScale = Math.abs(1.5*frac/(2.0*r));
        }
        return dragScale;
    }
    /**
     * Define the angles that spin the object from the user's mouse click.
     * The last values are saved in lastMouseX, lastMouseY, oldX, oldY;
     * the calculated result is put in phiSpin and thetaSpin.
     */
    void setPhiThetaSpin(MouseEvent e){
        int xClick=e.getX();             
        int yClick=e.getY();
        double newX = xcoord(xClick);    
        double newY = ycoord(yClick);
        double x = newX - oldX;          
        double y = newY - oldY;
        lastMouseX = xClick;             
        lastMouseY = yClick;
        oldX = newX;                     
        oldY = newY;
        if (itsBody!=null){
            phiSpin   = Math.atan2(-x,-y);
            thetaSpin = Math.sqrt(x*x+y*y)*getDragToThetaScale();
        }
    }
    /**
     * This animation event handler spins the body after the mouse is released,
     * at a rate of thetaSpin per frame 
     * for half of spinDecayTime, then slows to a spin rate of theta=0 linearly.
     * The routine is designed to be called periodically from the Timer's
     * ActionPerformed method.  It uses rotation angles defined from the
     * spinMouseFromDrag mouse methods.
     */
    public void continueSpinningAfterMouseup(){
        if (isSpinning && !frozen){
            frameCounter++;
            double fractionDone = frameCounter/framesPerSecond/spinDecayTime;
            if (fractionDone>1.0){
                isSpinning = false;
                return;
            }
            double theta = thetaSpin;
            if (fractionDone > 0.5){
                theta = thetaSpin * 2.0*(1.0-fractionDone);
            }
            itsBody.rotateInPlace(phiSpin, theta, -phiSpin);
            repaint();
        }
    }
    /**
     * This event handler rotates the body based on mouse click and drags.
     * It or something similar should be passed to 
     * startMouseInputAndAnimation().
     */
    public MouseInputAdapter spinFromMouseDrag = new MouseInputAdapter(){
            public void mousePressed(MouseEvent e){
                oldX = xcoord(e.getX());
                oldY = ycoord(e.getY());
                isSpinning=false;
                thetaSpin = 0.0;
                phiSpin = 0.0;
            }
            public void mouseDragged(MouseEvent e){
                setPhiThetaSpin(e);
                lastDragTime = e.getWhen();
                if (thetaSpin > 0.0){ 
                    itsBody.rotateInPlace(phiSpin, thetaSpin, -phiSpin);
                    repaint();
                }
            }
            public void mouseReleased(MouseEvent e){
                if ( e.getWhen() - lastDragTime < releaseDelayThreshold ){
                    isSpinning = true;
                    frameCounter = 0;
                }
            }
        };

    /** 
     * Turn on a mouse event handler.
     */
    public void startMouseInputAndAnimation(MouseInputAdapter m){
        addMouseListener(m);
        addMouseMotionListener(m);
        initTimer();
    }

    /**
     * Create and initialize a Timer.
     */
    void initTimer(){
        initTimer(15);            // default is 15 frames per second.
    }
    void initTimer(double fps){   // frames per second
        framesPerSecond = fps;
        timer = new javax.swing.Timer((int)(1000/fps),this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        startAnimation();
    }

    /**
     * Turn on animation (i.e. window changes when there aren't user events).
     */ 
    public synchronized void startAnimation(){
        if (frozen){
            // do nothing.  Stopped at user request.
        }
        else {
            if (!timer.isRunning()){
                timer.start();
            }
        }
    }
    public synchronized void stopAnimation(){
        if (timer.isRunning()){ 
            timer.stop(); 
        }
    }

    /**
     * invoked by browser when running as applet only.
     */
    public void start(){  startAnimation(); }
    /**
     * invoked by browser when running as applet only.
     */
    public void stop(){   stopAnimation(); }

    /**
     * This is done each time an animation event is caught.
     */
    public void actionPerformed(ActionEvent e){
        // sub classes should override this routine as desired.
        // While there is no default animation in this base class,
        // I've defined a method which subclasses can use, namely
        //    continueSpinningAfterMouseup();
        //
        // Or a subclass could do something simpler in actionPerformed(),
        // for example continuous rotation by a fixed angle like this:
        //   itsBody.rotateInPlace(Math.PI/200.0,-Math.PI/50.0,-Math.PI/100.0);
        //   repaint();
    }

    // ------- coordinate systems  and painting ---------------------------

    // coordinate system bounds and constants
    double xPixDivCoords,yPixDivCoords;
    int xPixOrigin,yPixOrigin;
    int xPixWidth, yPixHeight;
    double xMax,xMin,yMax,yMin;

    /**
     * Convert from world coords to screen pixels in the horizontal direction.
     */    public int xpix(double xcoord){
        return (int)((xcoord-xMin)*xPixDivCoords+xPixOrigin);
    }
    /**
     * Convert from world coords to screen pixels in the vertical direction.
     */
    public int ypix(double ycoord){
        return (int)((yMax-ycoord)*yPixDivCoords+yPixOrigin);
    }
    /**
     * Convert from screen pixels to world coords in the horizontal direction.
     */
    public double xcoord(int xpix){
        return (xpix-xPixOrigin)/xPixDivCoords+xMin;
    }
    /**
     * Convert from screen pixels to world coords in the vertical direction.
     */
    public double ycoord(int ypix){
        return yMax-(ypix-yPixOrigin)/yPixDivCoords;
    }
    /**
     * Set up the conversion between world coords and screen pixels.
     * The passed values are
     * <pre>
          xMin, xMax, yMin, yMax  : window bounds in world coords.
          xPixOrigin, yPixOrigin  : top-left pixel origin, typically (0,0).
          width, height           : window size in pixels.
     * </pre>
     */ 
    // 
    private void setCoordScales(double xMin, double xMax, 
                                double yMin, double yMax,
                                int xPixOrigin, int yPixOrigin,
                                int width, int height){
        this.xMin=xMin; this.xMax=xMax; this.yMin=yMin; this.yMax=yMax;
        this.xPixWidth = width;     this.yPixHeight = height;
        this.xPixOrigin=xPixOrigin; this.yPixOrigin=yPixOrigin;
        xPixDivCoords = (double)(width)/(xMax-xMin);
        yPixDivCoords = (double)(height)/(yMax-yMin);

        /* --------  Debugging
        System.out.println("World3D.setCoordScales :");
        System.out.println(" xPixOrigin = "+xPixOrigin);
        System.out.println(" yPixOrigin = "+yPixOrigin);
        System.out.println(" width = "+pixBounds.width);
        System.out.println(" height = "+pixBounds.height);
        System.out.println(" xPixDivCoords = "+xPixDivCoords);
        System.out.println(" yPixDivCoords = "+yPixDivCoords);
        System.out.println(" ");
        ----------- */
    }
    /**
     * Draw the given Polygon3D into this World3D's window.
     */
    public void paintPolygon3D(Polygon3D p3D){
        Vector3D v = new Vector3D(0.0,0.0,0.0);
        paintPolygon3D(p3D,v);
    }
    /**
     * Draw the given Polygon3D at the "offset" position 
     * in this World3D's window.
     */
    public void paintPolygon3D(Polygon3D p3D, Vector3D offset){
        // For these formulats, see my notes at 
        // http://cs.marlboro.edu/docs/3D-projection-notes/3D-projection.html
        double xOff = offset.getX();
        double yOff = offset.getY();
        double zOff = offset.getZ();
        double xView = viewpoint.getX();
        double yView = viewpoint.getY();
        double zView = viewpoint.getZ();
        Polygon p = new Polygon();   // 2D projection of Polygon3D
        Iterator i = p3D.getVector3DIterator();
        while (i.hasNext()){
            Vector3D r = (Vector3D)i.next();
            double xR = r.getX() + xOff;
            double yR = r.getY() + yOff;
            double zR = r.getZ() + zOff;
            double xProjected = (xR*zView - xView*zR)/(zView-zR);
            double yProjected = (yR*zView - yView*zR)/(zView-zR);
            p.addPoint( xpix(xProjected), ypix(yProjected) );
        }
        Color originalColor = graphics.getColor();
        graphics.setColor(p3D.getFaceColor());
        graphics.fillPolygon(p);

        // ---- Put black outline around polygon edges.
        //graphics.setColor(Color.black);
        //graphics.drawPolygon(p);

        graphics.setColor(originalColor);
    }
    /**
     * Draw the given Body3D in this World3D window.
     */
    public void paintBody3D(Body3D b){
        if (b==null){return;}
        Vector3D bodyCenter = b.getCenter();
        Vector3D bodyToView = viewpoint.minus(bodyCenter);
        Iterator i = b.getPolygon3DIterator();
        while (i.hasNext()){
            Polygon3D p3D = (Polygon3D)i.next();
            Vector3D outward = p3D.getOutwardVector3D();
            Vector3D polyToView = bodyToView.minus(p3D.getMeanPosition());
            if ( outward.dot(polyToView) > 0.0 ){  
                paintPolygon3D(p3D,bodyCenter);
            }
        }
    }
    /**
     * Fill a rectangle given in world coords with the current graphics color.
     * (Used for testing.)
     */
    public void fillCoordRect(double x1, double y1, double x2, double y2){
        int xleft   = xpix(x1);
        int xright  = xpix(x2);
        int ytop    = ypix(y1);
        int ybottom = ypix(y2);
        graphics.fillRect(xleft,ytop, (xright-xleft), (ybottom-ytop));
    }
    /**
     * Adjust coordinate scales if the window was resized.
     */
    public void checkCoordScales(){
        int newWidth  = getWidth();
        int newHeight = getHeight();
        if ( (newWidth==xPixWidth) && (newHeight==yPixHeight) ){ 
            return; 
        }
        else {
            setCoordScales(xMin,xMax,yMin,yMax, 
                           getX(),getY(),newWidth,newHeight);
        }
    }
    /**
     * Display this World3D on the computer screen.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);             // paint background.
        graphics = g;                        // save for use in other methods.
        checkCoordScales();
        paintBody3D(itsBody);                // display the 3D thingy already.

        /* ------ debugging - test draw using world coords
        System.out.println(" World3D.paintComponent ");
        g.setColor(Color.yellow);
        fillCoordRect( xMin+1.0, yMax-1.0, xMax-1.0, yMin+1.0);
        g.setColor(Color.red);
        fillCoordRect( -2.0, 2.0, 2.0, -2.0 );
        ---------- */

    }
}
