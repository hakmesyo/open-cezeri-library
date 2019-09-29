/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TFigureAttribute;
import cezeri.matrix_processing.CMatrix;
import cezeri.matrix_processing.CPoint;
import cezeri.types.TPointType;
import cezeri.utils.FactoryUtils;
import static cezeri.utils.FactoryUtils.generateColor;
import cezeri.utils.MersenneTwister;
import cezeri.utils.UniqueRandomNumbers;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JLabel;

/**
 *
 * @author BAP1
 */
public class PanelPlot extends javax.swing.JPanel {

    private CMatrix f;
    private double[] xAxis;
    private Color[] color;
    private boolean isShowPoint = false;
    private boolean lblShow = false;
    private Point mousePos = new Point(0, 0);
    private JLabel lbl = null;
    private double scale = 1;
//    private String type="-";
    private TFigureAttribute figureAttribute;

    public PanelPlot(CMatrix ff) {
        this.f = ff.transpose();
        color = generateColor(f.getRowNumber());
        this.figureAttribute = new TFigureAttribute();
        repaint();
    }

    public PanelPlot(CMatrix ff, double[] x) {
        this.xAxis = x;
        this.f = ff.transpose();
        color = generateColor(f.getRowNumber());
        this.figureAttribute = new TFigureAttribute();
        repaint();
    }

    public PanelPlot(CMatrix ff, TFigureAttribute attr) {
        this.f = ff.transpose();
        color = generateColor(f.getRowNumber());
        this.figureAttribute = attr;
        repaint();
    }

    public PanelPlot(CMatrix ff, TFigureAttribute attr, double[] x) {
        this.xAxis = x;
        this.f = ff.transpose();
        color = generateColor(f.getRowNumber());
        this.figureAttribute = attr;
        repaint();
    }

    public void setXAxis(double[] x) {
        this.xAxis = x;
        repaint();
    }

    public void paint(Graphics gr1) {
        Graphics2D gr = (Graphics2D) gr1;
        gr.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
         Font fnt = gr.getFont();
        gr.setFont(new Font(fnt.getFontName(), 1, 18));
        gr.setColor(Color.lightGray);
        int w = getWidth();
        int h = getHeight();
        gr.fillRect(0, 0, w, h);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);
        gr.setColor(Color.black);
        int titleWidth = FactoryUtils.getGraphicsTextWidth(gr, figureAttribute.title);
        int pTitleX = (w - titleWidth) / 2;
        gr.drawString(figureAttribute.title, pTitleX, 40);
        int px = 100;
        int py = h - 70;
        int fromRight = 100;
        gr.setColor(Color.white);
        int fromLeft = 100;
        int fromTop = 50;
        int canvas_width = w - 2 * fromLeft;
        int canvas_height = h - 2 * fromTop;
        gr.fillRect(fromLeft, fromTop, canvas_width, canvas_height);

        CPoint[][] mp = mappingDataToScreenCoordinates(f.toDoubleArray2D(), fromLeft, fromTop, canvas_width, canvas_height);
        for (int r = 0; r < f.getRowNumber(); r++) {
            gr.setColor(color[r]);
            int[] xp = new int[f.getColumnNumber()];
            int[] yp = new int[f.getColumnNumber()];

            for (int j = 0; j < f.getColumnNumber(); j++) {
                CPoint p = mp[r][j];
                yp[j] = p.row;
                xp[j] = p.column;
            }
            if (r > 0) {
//                gr.setStroke(thindashed);
            }
            drawDataPoints(gr, xp, yp, f.getColumnNumber(), r);

            if (isShowPoint) {
                paintDataPoints(gr, xp, yp);
            }
            if (lblShow) {
                checkDataPoints(gr, mp, px, py);
            }

        }
        gr.setStroke(new BasicStroke());
        paintItemList(gr, figureAttribute.items);
        gr.setColor(Color.black);
        drawYAxis(gr, px, py, w, fromRight, f.toDoubleArray2D());
        drawXAxis(gr, px, py, w, fromRight, f.toDoubleArray2D());
    }

    private CPoint[][] mappingDataToScreenCoordinates(double[][] d, int fromLeft, int fromTop, int w, int h) {
        CPoint[][] ret = new CPoint[d.length][d[0].length];
        double maxY = getMaxYValue(d);
        double minY = getMinYValue(d);
        double deltaY = maxY - minY;
        double maxX = getMaxXValue(d);
        double cellWidth = w / maxX;
        double cellHeight = h / deltaY;
        for (int r = 0; r < d.length; r++) {
            for (int c = 0; c < d[0].length; c++) {
                CPoint p = new CPoint();
                p.column = (int) (fromLeft + c * cellWidth);
                p.row = (int) ((fromTop + h) - (d[r][c] - minY) * cellHeight);
                ret[r][c] = p;
            }
        }
        return ret;
    }

    public void setMatrix(CMatrix m) {
        this.f = m.transpose();
        color = generateColor(f.getRowNumber());
        figureAttribute.items = generateItemText(f.getRowNumber());
        repaint();
    }

    public CMatrix getMatrix() {
        return this.f.transpose();
    }

    private void initialize() {
        lbl = new JLabel("X:Y");
        this.add(lbl);
        lbl.setBounds(new Rectangle(10, 0, 100, 20));
        lbl.setForeground(Color.black);
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblShow = true;
                lbl.setVisible(true);
                mousePos = evt.getPoint();
                repaint();
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblShow = false;
            }
        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent e) {
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                lbl.setVisible(true);
                repaint();
            }
        });
    }

    private void paintDataPoints(Graphics gr, Point[] mp) {
        int r = 6;
        for (int i = 0; i < mp.length; i++) {
            gr.drawOval(mp[i].x - r / 2, mp[i].y - r / 2, r, r);
        }
    }

    private String[] generateItemText(int n) {
        if (figureAttribute.items.length != 0) {
            return figureAttribute.items;
        }
        String[] ret = new String[n];
        for (int i = 0; i < n; i++) {
            ret[i] = "Item-" + (i + 1);
        }
        return ret;
    }

    private void drawDataPoints(Graphics2D gr, int[] xp, int[] yp, int ncols, int nr) {
        String pType = figureAttribute.pointType;
        if (pType.contains("-")) {
            if (figureAttribute.isStroke) {
                gr.setStroke(figureAttribute.stroke.get(nr));
                gr.drawPolyline(xp, yp, f.getColumnNumber());
//            gr.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            } else {
                gr.drawPolyline(xp, yp, f.getColumnNumber());
            }
        }
        if (!(pType.contains("-") && pType.length()==1)) {
            String p = ".";
            if (pType.length() > 1) {
                p = pType.substring(1, 2);
            } else {
                p = pType;
            }
            switch (p) {
                case ".": {
                    int a = 5;
                    int b = 2;
                    for (int i = 0; i < xp.length; i++) {
                        gr.fillRect(xp[i] - a / 2, yp[i] - a / 2, a, a);
                    }
                    break;
                }
                case "o": {
                    int a = 4;
                    int b = 8;
                    for (int i = 0; i < xp.length; i++) {
                        gr.fillOval(xp[i] - a, yp[i] - a, b, b);
                    }
                    break;
                }
                default: {
                    for (int i = 0; i < xp.length; i++) {
                        gr.drawString(p, xp[i]-3, yp[i]+10);
                    }
                    break;

                }
            }

            /*
            if (figureAttribute.pointType.equals("-o")) {
                int a = 4;
                int b = 8;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawOval(xp[i] - a, yp[i] - a, b, b);
                }
            }
            if (figureAttribute.pointType.equals("-.")) {
                int a = 1;
                int b = 2;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawLine(xp[i] - a, yp[i] - a, xp[i] + a, yp[i] + a);
                    gr.drawLine(xp[i] + a, yp[i] - a, xp[i] - a, yp[i] + a);
                    gr.drawLine(xp[i] - b, yp[i], xp[i] + b, yp[i]);
                    gr.drawLine(xp[i], yp[i] - b, xp[i], yp[i] + b);
                }
            }
            if (figureAttribute.pointType.equals("-*")) {
                int a = 2;
                int b = 4;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawLine(xp[i] - a, yp[i] - a, xp[i] + a, yp[i] + a);
                    gr.drawLine(xp[i] + a, yp[i] - a, xp[i] - a, yp[i] + a);
                    gr.drawLine(xp[i] - b, yp[i], xp[i] + b, yp[i]);
                    gr.drawLine(xp[i], yp[i] - b, xp[i], yp[i] + b);
                }
            }
            if (figureAttribute.pointType.equals(".")) {
                int a = 1;
                int b = 2;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawLine(xp[i] - a, yp[i] - a, xp[i] + a, yp[i] + a);
                    gr.drawLine(xp[i] + a, yp[i] - a, xp[i] - a, yp[i] + a);
                    gr.drawLine(xp[i] - b, yp[i], xp[i] + b, yp[i]);
                    gr.drawLine(xp[i], yp[i] - b, xp[i], yp[i] + b);
                }
            }
            if (figureAttribute.pointType.equals("*")) {
                int a = 2;
                int b = 4;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawLine(xp[i] - a, yp[i] - a, xp[i] + a, yp[i] + a);
                    gr.drawLine(xp[i] + a, yp[i] - a, xp[i] - a, yp[i] + a);
                    gr.drawLine(xp[i] - b, yp[i], xp[i] + b, yp[i]);
                    gr.drawLine(xp[i], yp[i] - b, xp[i], yp[i] + b);
                }
            }
            if (figureAttribute.pointType.equals("o")) {
                int a = 4;
                int b = 8;
                for (int i = 0; i < xp.length; i++) {
                    gr.drawOval(xp[i] - a, yp[i] - a, b, b);
                }
            }
             */
        }
    }

    private void checkDataPoints(Graphics gr, CPoint[][] mp, int px, int py) {
        for (int i = 0; i < mp.length; i++) {
            for (int j = 0; j < mp[0].length; j++) {
                if (mp[i][j].row > mousePos.x - 5 && mp[i][j].row < mousePos.x + 5
                        && mp[i][j].column > mousePos.y - 5 && mp[i][j].column < mousePos.y + 5) {
                    gr.setColor(Color.red);
                    gr.fillRect(mp[i][j].row - 5, mp[i][j].column - 5, 10, 10);
                    lbl.setText(FactoryUtils.formatDouble(f.toDoubleArray2D()[0][i]) + " , " + FactoryUtils.formatDouble(f.toDoubleArray2D()[1][i]));
                    lbl.setLocation(mousePos.x - 30, mousePos.y - 25);
                    break;
                }
            }
        }
    }

    private void paintItemList(Graphics2D gr, String[] t) {
        if (figureAttribute.items == null || figureAttribute.items.length == 0) {
            return;
        }
        if (color.length != t.length) {
            return;
        }
        int w = getWidth();
        int h = getHeight();
        gr.setColor(Color.red);
        int strWidth = getMaxWidth(gr, t);
        int strHeight = getMaxHeight(gr, t);
        int itemW = (int) (strWidth * scale) - 15;
        int itemH = (int) (strHeight * scale);
//        gr.drawRect(w - itemW, 20, itemW+10, itemH * t.length + 10);
        for (int i = 0; i < color.length; i++) {
            gr.setColor(this.color[i]);
            if (figureAttribute.isStroke) {
                int hh = 70 + (itemH + 20) * i;
                gr.drawString(t[i], w - itemW, hh - 10);
                gr.setStroke(figureAttribute.stroke.get(i));
                gr.drawLine(w - itemW, hh, w - 10, hh);
                gr.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            } else {
                gr.drawString(t[i], w - itemW, 70 + itemH * i);
            }
        }
    }

    private void paintDataPoints(Graphics gr, int[] xp, int[] yp) {
        int r = 5;
        for (int i = 0; i < xp.length; i++) {
            gr.drawRect(xp[i] - r / 2, yp[i] - r / 2, r, r);
        }
    }

    private void drawYAxis(Graphics gr, int x0, int y0, int w, int fromRight, double[][] d) {
        double maxY = getMaxYValue(d);
        double minY = getMinYValue(d);
        double deltaY = maxY - minY;
        double n = 10.0;
//        int l = y0 - 60;
        int l = y0 - 50;
        int top = 50;
        gr.drawLine(x0, top, x0, y0 + 20);
        gr.drawLine(w - fromRight, top, w - fromRight, y0 + 20);
        double delta = deltaY / n;
        double[] yVal = new double[(int) n + 1];
        double dY = l / n;
        int q = 0;
        int art = 5;
        int shift = 25;
        for (int i = 0; i <= n; i++) {
            if (maxY > 1) {
                yVal[i] = (Math.round((i * delta + minY) * n)) / n;
            } else {
                yVal[i] = (i * delta + minY);
            }

//            gr.drawString(FactoryUtils.formatDoubleAsString(yVal[i],1) + "", x0 - 80, (int) (y0 - i * dY+shift) + art);
            gr.drawString(FactoryUtils.formatDoubleAsString(yVal[i], 3) + "", x0 - 80, (int) (y0 - i * dY + shift) + art);
            q = (int) (y0 - i * dY + shift);
            gr.drawLine(x0, q, x0 + art, q);
            gr.drawLine(w - fromRight, q, w - fromRight - art, q);
        }
        gr.drawString(figureAttribute.axis[1], 20, 35);
    }

    private void drawXAxis(Graphics gr, int x0, int y0, int w, int fromRight, double[][] d) {
        double maxX = 0;
        double minX = 0;
        double n = 10.0;
        int l = w - (x0 + fromRight);
        double dx = l / n;
        double[] xVal = new double[(int) n + 1];
        int top = 50;
        if (this.xAxis != null && this.xAxis.length != 0) {
            maxX = FactoryUtils.getMaximum(this.xAxis);
            minX = FactoryUtils.getMinimum(this.xAxis);
            maxX = Math.abs(maxX - minX);
        } else {
            maxX = getMaxXValue(d);
        }
        double delta = maxX / n;
        gr.drawLine(x0, top, w - fromRight, 50);
        gr.drawLine(x0, y0 + 20, w - fromRight, y0 + 20);
        gr.drawString(figureAttribute.axis[0], w - 90, y0 + 20);
        int q = 0;
        for (int i = 0; i <= n; i++) {
            xVal[i] = (Math.round(minX + i * delta));
            q = (int) (x0 + i * dx);
            gr.drawString((int) xVal[i] + "", q - 10, y0 + 40);
            gr.drawLine(q, top, q, top + 5);
            gr.drawLine(q, y0 + 20, q, y0 + 20 - 5);
        }
    }

    private double getMaxYValue(double[][] d) {
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

    private double getMinYValue(double[][] d) {
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

    private double getMaxXValue(double[][] d) {
        double ret = 0;
        ret = d[0].length;
        for (int i = 0; i < d.length; i++) {
            if (ret < d[i].length) {
                ret = d[i].length;
            }
        }
        return ret;
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
    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public double getScale() {
        return scale;
    }

    private int getMaxWidth(Graphics gr, String[] t) {
        if (t.length == 1) {
            Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
            return (int) r1.getWidth();
        }
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getWidth(), r2.getWidth()) + 20;
        return ret;
    }

    private int getMaxHeight(Graphics gr, String[] t) {
        if (t.length == 1) {
            Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
            return (int) r1.getHeight();
        }
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getHeight(), r2.getHeight());
        return ret;
    }

    public void setPlotType(String type) {
        this.figureAttribute.pointType = type;
        repaint();
    }

    public void setFigureAttribute(TFigureAttribute figureAttribute) {
        this.figureAttribute = figureAttribute;
        repaint();
    }

    public TFigureAttribute getFigureAttribute() {
        return this.figureAttribute;
    }

//    private Stroke[] generateStroke(int rowNumber) {
//        Stroke[] ret = new Stroke[rowNumber];
//        for (int i = 0; i < rowNumber; i++) {
//            ret[i] = new BasicStroke((i + 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//        }
//        return ret;
//    }
}
