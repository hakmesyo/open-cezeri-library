/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.cad2D;

import cezeri.utils.FactoryUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

/**
 *
 * @author BAP1
 */
public class PanelCadViewer extends JPanel implements MouseWheelListener {

    private AffineTransform afft = new AffineTransform();
    private AffineTransform temp_afft = new AffineTransform();
    private AffineTransform inverseScale = new AffineTransform();
    private JRadioButtonMenuItem items[];
    private final JPopupMenu popupMenu = new JPopupMenu();
    private Point mousePos = new Point(0, 0);
    private JLabel lbl = null;
//    private Point p = new Point();
    private List<Cad2DShape> shapeList;
    private List<Cad2DShape> candidateShapeList = new ArrayList<>();
    private double zoom = 1;
    private int xAxisLength = Integer.MAX_VALUE;
    private int yAxisLength = Integer.MAX_VALUE;
    private boolean isActionDragged = false;
    private Point2D startPosForDragging;
    private Point2D pointDrag = new Point();
    private Cad2DShape selectedShape;

    public PanelCadViewer() {
        initialize();
//        populateManualShapeList();
    }

    public PanelCadViewer(List<Cad2DShape> shapeList) {
        initialize();
        this.shapeList = shapeList;
    }

    public void setShapeList(List<Cad2DShape> shapeList) {
        this.shapeList = shapeList;
        repaint();
    }

    public void addShape(Cad2DShape shape) {
        if (this.shapeList == null) {
            this.shapeList = new ArrayList<>();
        }
        this.shapeList.add(shape);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        lbl.setBounds(new Rectangle(getWidth() - 200, getHeight() - 40, 500, 30));
        super.paint(g);
        Graphics2D gr = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
//        Font fnt = gr.getFont();
//        gr.setFont(new Font(fnt.getFontName(), 1, 10));
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, w, h);

        Point2D p2d = getScreenCoordinate();
        lbl.setText(" Pos= [" + p2d.getX() + " , " + p2d.getY() + " ] Zoom:" + FactoryUtils.formatDouble(zoom, 1));
        this.paintComponents(gr);

        //draw axes
        gr.setColor(Color.GREEN);
        gr.drawString("Y", 18, h - 110);
        gr.drawLine(20, h - 20, 20, h - 100);
        gr.drawLine(15, h - 95, 20, h - 100);
        gr.drawLine(25, h - 95, 20, h - 100);
        gr.setColor(Color.RED);
        gr.drawString("X", 100, h - 25);
        gr.drawLine(10, h - 30, 90, h - 30);
        gr.drawLine(85, h - 35, 90, h - 30);
        gr.drawLine(85, h - 25, 90, h - 30);

        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);

        temp_afft = gr.getTransform();
        gr.translate(0.0, h);      // Move the origin to the lower left for actual cartesian coordinate system
        gr.scale(1.0, -1.0);       // Flip the sign of the coordinate system

        if (shapeList != null) {
            gr.setColor(Color.WHITE);
            drawShapes(gr);
        }
        gr.setColor(Color.blue);
        gr.drawLine(0, mousePos.y, w - 1, mousePos.y);
        gr.drawLine(mousePos.x, 0, mousePos.x, h - 1);
        gr.setColor(Color.red);
        gr.drawRect(mousePos.x - 1, mousePos.y - 1, 2, 2);
        gr.drawRect(mousePos.x - 10, mousePos.y - 10, 20, 20);
    }

    private void drawShapes(Graphics2D gr) {
        shapeList.forEach((obj) -> {
            obj.paint(gr, temp_afft, getHeight());
        });
    }

    private void initialize() {
        this.setBounds(0, 0, 800, 600);
        lbl = new JLabel("X:Y");
        this.add(lbl);
        lbl.setBounds(new Rectangle(0, 0, 500, 30));
        lbl.setBackground(Color.yellow);
        lbl.setForeground(Color.GREEN);
        lbl.setVisible(true);
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        PanelCadViewer.ItemHandler handler = new PanelCadViewer.ItemHandler();
        String[] elements = {
            "Set Default Position",
            "Load DXF File"
        };

        ButtonGroup itemsGroup = new ButtonGroup();
        items = new JRadioButtonMenuItem[elements.length];

        // construct each menu item and add to popup menu; also  
        // enable event handling for each menu item  
        for (int i = 0; i < elements.length; i++) {
            items[i] = new JRadioButtonMenuItem(elements[i]);
            popupMenu.add(items[i]);
            itemsGroup.add(items[i]);
            items[i].addActionListener(handler);
        }

        addMouseWheelListener(this);

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
//                    System.out.println("double clicked");
                    e.consume();
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectShape(e);
                }
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectShape(e);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
//                    System.out.println("right pressed");
                    startPosForDragging = e.getPoint();
                    startPosForDragging.setLocation(startPosForDragging.getX(), getHeight() - startPosForDragging.getY());
                }
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (!isActionDragged) {
                    handlerPopup(e);
                }
                if (SwingUtilities.isLeftMouseButton(e)) {
//                    System.out.println("left relased");
                }
                if (SwingUtilities.isRightMouseButton(e)) {
//                    System.out.println("right released");
//                    afft = new AffineTransform(temp_afft);
                }
                revalidate();
                repaint();
            }

            private void handlerPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            private void selectShape(MouseEvent e) {
                candidateShapeList.clear();
                Point p = e.getPoint();
                p.y = getHeight() - p.y;
                if (shapeList != null) {
                    shapeList.forEach((shp) -> {
                        if (shp.contains(p)) {
//                        System.out.println("name: = " + shp.toString());
                            candidateShapeList.add(shp);
                        }
                    });
                    if (!candidateShapeList.isEmpty()) {
                        selectedShape = candidateShapeList.get(0);
                        double min_area = selectedShape.getArea();
                        for (Cad2DShape shp : candidateShapeList) {
                            if (min_area > shp.getArea()) {
                                min_area = shp.getArea();
                                selectedShape = shp;
                            }
                        }
                        if (selectedShape != null) {
                            selectedShape.isSelected = true;
                            for (Cad2DShape shp : shapeList) {
                                if (shp != selectedShape) {
                                    shp.isSelected = false;
                                }
                            }
                        }
                    } else {
                        selectedShape = null;
                        shapeList.forEach((shp) -> {
                            shp.isSelected = false;
                        });
                    }
                }

            }

        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                mousePos.y = getHeight() - mousePos.y;
                isActionDragged = false;
                repaint();
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                mousePos.y = getHeight() - mousePos.y;
                isActionDragged = true;
                if (SwingUtilities.isLeftMouseButton(e)) {
//                    System.out.println("left mouse");
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point2D p1 = e.getPoint();
                    p1.setLocation(p1.getX(), getHeight() - p1.getY());
                    pointDrag.setLocation(p1.getX() - startPosForDragging.getX(), p1.getY() - startPosForDragging.getY());
//                    Point2D p2 = null;
//                    try {
//                        p2 = afft.inverseTransform(p1, null);
//                    } catch (NoninvertibleTransformException ex) {
//                        ex.printStackTrace();
//                        return;
//                    }
//                    afft.setToIdentity();
//                    afft.translate(p1.getX()-startPosForDragging.getX(), p1.getY()-startPosForDragging.getY());
//                    afft.translate(-p2.getX(), -p2.getY());
                }
//                revalidate();
                repaint();
            }
        });

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

            Point2D p1 = e.getPoint();
            p1.setLocation(p1.getX(), getHeight() - p1.getY());
            Point2D p2 = null;
            try {
                p2 = afft.inverseTransform(p1, null);
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
                return;
            }

            zoom -= (0.1 * e.getWheelRotation());
            zoom = Math.max(0.01, zoom);

            afft.setToIdentity();
            afft.translate(p1.getX(), p1.getY());
            afft.scale(zoom, zoom);
            afft.translate(-p2.getX(), -p2.getY());

            try {
                inverseScale = afft.createInverse();
            } catch (NoninvertibleTransformException ex) {
                Logger.getLogger(PanelCadViewer.class.getName()).log(Level.SEVERE, null, ex);
            }

            revalidate();
            repaint();
        }
    }

    private Point2D getScreenCoordinate() {
        Point2D c = new Point2D.Double(
                mousePos.x,
                mousePos.y);
        Point2D screenPoint = inverseScale.transform(c, new Point2D.Double());
        screenPoint.setLocation(FactoryUtils.formatDouble(screenPoint.getX(), 2), FactoryUtils.formatDouble(screenPoint.getY(), 2));
        return screenPoint;
    }

    private void populateManualShapeList() {

        shapeList = new ArrayList<>();
        CadLine x_axis = new CadLine(afft, -xAxisLength, 3, xAxisLength, 3, Color.red);
        shapeList.add(x_axis);
        CadLine y_axis = new CadLine(afft, 3, -yAxisLength, 3, yAxisLength, Color.green);
        shapeList.add(y_axis);

        CadCircle circle_1 = new CadCircle(afft, 100, 100, 50, Color.white);
        shapeList.add(circle_1);
        CadCircle circle_2 = new CadCircle(afft, 150, 200, 10, Color.white);
        shapeList.add(circle_2);
        CadRectangle rect_1 = new CadRectangle(afft, 25, 45, 231, 237, Color.white);
        shapeList.add(rect_1);

    }

    public void populateShapeList(List<Cad2DShape> lst) {
        this.shapeList = lst;
        CadLine x_axis = new CadLine(afft, -xAxisLength, 0, xAxisLength, 0, Color.red);
        shapeList.add(x_axis);
        CadLine y_axis = new CadLine(afft, 0, -yAxisLength, 0, yAxisLength, Color.green);
        shapeList.add(y_axis);
        for (Cad2DShape shp : shapeList) {
            shp.afft = afft;
        }
    }

    private class ItemHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButtonMenuItem obj = (JRadioButtonMenuItem) e.getSource();
            if (obj.getText().equals("Set Default Position")) {
            }
            if (obj.getText().equals("Load DXF File")) {

            }
        }
    }

}
