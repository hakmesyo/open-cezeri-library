/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import cezeri.matrix_processing.CMatrix;
import cezeri.utils.MersenneTwister;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author BAP1
 */
public class TPanelData extends javax.swing.JPanel {

    private double scale = 1;
    private CMatrix dataMatrix;
    private Point mousePos = new Point(0, 0);
    private boolean activateDataCursor = false;
    private JRadioButtonMenuItem items[];
    private final JPopupMenu popupMenu = new JPopupMenu();

    public TPanelData(CMatrix cm) {
        this.dataMatrix = cm;
        initialize();
        repaint();
    }

    public Color[] generateColor(int n) {
        Color[] cl = new Color[n];
        if (n == 1) {
            MersenneTwister r = new MersenneTwister();
            int red = new MersenneTwister(r.nextInt(1000)).nextInt(255);
            int green = new MersenneTwister(r.nextInt(10)).nextInt(255);
            int blue = new MersenneTwister(r.nextInt(100)).nextInt(255);
            cl[0] = new Color(red, green, blue);
        }
        if (n == 2) {
            cl[0] = new Color(255, 0, 0);
            cl[1] = new Color(0, 0, 255);
        } else {
            MersenneTwister r = new MersenneTwister();
            for (int i = 0; i < n; i++) {
                int red = r.nextInt(255);
                int green = r.nextInt(255);
                int blue = r.nextInt(255);
                cl[i] = new Color(red, green, blue);
            }
        }
        return cl;
    }

    private void initialize() {
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();
        ItemHandler handler = new ItemHandler();
        String[] elements = {"Data Cursor Active", "Data Cursor Passive"};
        ButtonGroup itemsGroup = new ButtonGroup();
        items = new JRadioButtonMenuItem[elements.length];

        // construct each menu item and add to popup menu; also  
        // enable event handling for each menu item  
//        for (int i = 0; i < elements.length; i++) {
//            items[i] = new JRadioButtonMenuItem(elements[i]);
//            popupMenu.add(items[i]);
//            itemsGroup.add(items[i]);
//            items[i].addActionListener(handler);
//        }
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                checkForTriggerEvent(e);
                repaint();
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                checkForTriggerEvent(e);
                repaint();
            }

            private void checkForTriggerEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
    }

    private class ItemHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
//            JRadioButtonMenuItem obj = (JRadioButtonMenuItem) e.getSource();
//            if (obj.getText().equals("Data Cursor Active")) {
//                activateDataCursor = true;
//                repaint();
//                return;
//            }
//            if (obj.getText().equals("Data Cursor Passive")) {
//                activateDataCursor = false;
//                repaint();
//                return;
//            }
        }

    }

    public Point getMousePos() {
        return mousePos;
    }

    public void setMousePos(Point mousePos) {
        this.mousePos = mousePos;
    }

    public CMatrix getMatrix() {
        return dataMatrix;
    }

    public void setMatrix(CMatrix dataMatrix) {
        this.dataMatrix = dataMatrix;
        repaint();
    }

    public void checkDataPoints(Graphics gr, Point[][] mp) {
        int px = 0;
        int py = 0;
        int w=5;
        int p=5;
        for (Point[] mp1 : mp) {
            for (int j = 0; j < mp[0].length; j++) {
//                if (mp1[j].x > mousePos.x - w && mp1[j].x < mousePos.x + w && mp1[j].y > mousePos.y - w && mp1[j].y < mousePos.y + w) {
                if (mp1[j].x > mousePos.x - w && mp1[j].x < mousePos.x + w ) {
                    px = mp1[j].x;
                    py = mp1[j].y;
                    gr.setColor(Color.red);
                    gr.fillRect(px - 4, py - 4, 8, 8);
                    gr.setColor(Color.white);
                    gr.fillRect(px+p, py - 30-p, 60, 30);
                    gr.setColor(Color.darkGray);
                    gr.drawRect(px+p, py - 30-p, 60, 30);
                    gr.drawString("X = " + j, px + 10, py - 20);
                    gr.drawString("Y = " + (int)dataMatrix.getValue(0, j), px + 10, py - 8);
                    break;
                }
            }
        }
    }

    public Point[][] mappingDataToScreenCoordinates(double[][] d, int w, int h,int px,int py) {
        Point[][] ret = new Point[d.length][d[0].length];
        double maxY = getMaxYValue(d);
        double minY = getMinYValue(d);
        double deltaY = maxY - minY;
        double maxX = getMaxXValue(d);
        double cellWidth = w * 1.0 / maxX;
        double cellHeight = (h-50) * 1.0 / deltaY;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                Point p = new Point();
                p.x = (int) (j * cellWidth)+px;
                p.y = (py+h-20)-(int) ((d[i][j] - minY) * cellHeight);
//                p.y = (int) (py - (d[i][j] - minY) * cellHeight);
                ret[i][j] = p;
            }
        }
        return ret;
    }

    public double getMaxYValue(double[][] d) {
        double ret = 0;
        ret = d[0][0];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (ret < d[i][j]) {
                    ret = d[i][j];
                }
            }
        }
        return ret;
    }

    public double getMinYValue(double[][] d) {
        double ret = 0;
        ret = d[0][0];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (ret > d[i][j]) {
                    ret = d[i][j];
                }
            }
        }
        return ret;
    }

    public double getMaxXValue(double[][] d) {
        double ret = 0;
        ret = d[0].length;
        for (int i = 0; i < d.length; i++) {
            if (ret < d[i].length) {
                ret = d[i].length;
            }
        }
        return ret;
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public double getScale() {
        return scale;
    }

    public int getFontMaxWidth(Graphics gr, String[] t) {
        if (t.length == 1) {
            Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
            return (int) r1.getWidth();
        }
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getWidth(), r2.getWidth()) + 20;
        return ret;
    }

    public int getFontMaxHeight(Graphics gr, String[] t) {
        if (t.length == 1) {
            Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
            return (int) r1.getHeight();
        }
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getHeight(), r2.getHeight());
        return ret;
    }

    public boolean isActivateDataCursor() {
        return activateDataCursor;
    }

    public void setActivateDataCursor(boolean activateDataCursor) {
        this.activateDataCursor = activateDataCursor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
