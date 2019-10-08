/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import cezeri.types.EPerformanceMetrics;
import cezeri.factory.FactoryStatistic;
import cezeri.factory.FactoryUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author BAP1
 */
public final class PanelScatterPlot extends javax.swing.JPanel {

    private CMatrix cm = CMatrix.getInstance().rand(2, 21, -100, 100);
    private JLabel lbl = null;
    private boolean lblShow = false;
    private Point mousePos = new Point(0, 0);
    private String strStat = "";
    private int fontSize = 18;
    private TFigureAttribute figureAttribute = new TFigureAttribute();
    private String[] axis = {"X", "Y"};

    public PanelScatterPlot() {
        initialize();
        repaint();
    }

    public PanelScatterPlot(CMatrix cm) {
        this.cm = cm;
        initialize();
        this.figureAttribute.perfMetricVal = FactoryStatistic.calculateStatistics(
                cm.toDoubleArray2D()[0],
                cm.toDoubleArray2D()[1],
                this.figureAttribute.perfMetricStr);
//        this.figureAttribute.axis = new String[]{"Observed", "Simulated"};
        this.figureAttribute.axis = axis;
        repaint();
    }

    public PanelScatterPlot(CMatrix cm, TFigureAttribute attr) {
        this.cm = cm;
        this.figureAttribute = attr;
        this.figureAttribute.perfMetricVal = FactoryStatistic.calculateStatistics(
                cm.toDoubleArray2D()[0],
                cm.toDoubleArray2D()[1],
                this.figureAttribute.perfMetricStr);
//        this.figureAttribute.axis = new String[]{"Observed", "Simulated"};
        this.figureAttribute.axis = axis;
        initialize();
        repaint();
    }

    @Override
    public void paint(Graphics gr1) {
        Graphics2D gr = (Graphics2D) gr1;
        gr.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
         if (cm.getRowNumber() == 1) {
            gr.drawString("You can't plot scatter diagram on single observation", getWidth() / 2 - 50, getHeight() / 2);
            super.paintComponents(gr);
            return;
        }
        Font fnt = gr.getFont();
        gr.setFont(new Font(fnt.getFontName(), 1, fontSize));
        gr.setColor(Color.white);
        int w = getWidth();
        int h = getHeight();
        gr.fillRect(0, 0, w, h);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);
        gr.setColor(Color.black);
//        writeStatistics(gr, w, h);

        int px = 100;
        int py = h - 70;
        Rectangle r = new Rectangle(px, 50, w - 260, h - 120);
        gr.setColor(Color.decode("#EAEAF2"));
        gr.fillRect(r.x, r.y, r.width, r.height);

        drawYAxis(gr, px, py, r.width, cm.toDoubleArray2D());
        drawXAxis(gr, px, py, r.height, w, cm.toDoubleArray2D());
        Point[] mp = mappingDataToScreenCoordinates(cm.toDoubleArray2D(), w, h, px, py);
        gr.setColor(Color.darkGray);
        paintDataPoints(gr, mp);
        if (lblShow) {
            checkDataPoints(gr, mp, px, py);
        }
        super.paintComponents(gr);
    }

    private void writeStatistics(Graphics gr, int w, int h) {
        if (this.figureAttribute == null) {
            return;
        }
        gr.setColor(Color.red);
        ArrayList<String> lst = this.figureAttribute.perfMetricStr;
        ArrayList<Double> metricVal = this.figureAttribute.perfMetricVal;
        int delta = 25;
        int py = 5;
        int px = w - 140;
        gr.drawRect(w - 150, 10, 140, lst.size() * 25);
        for (int i = 0; i < lst.size(); i++) {
            gr.drawString(lst.get(i) + " = " + FactoryUtils.formatDouble(metricVal.get(i), 2), px, py += delta);
        }
        gr.setColor(Color.black);
    }

    private void checkDataPoints(Graphics gr, Point[] mp, int px, int py) {
        lbl.setBackground(Color.white);
        lbl.setForeground(Color.red);
        for (int i = 0; i < mp.length; i++) {
            if (mp[i].x > mousePos.x - 5 && mp[i].x < mousePos.x + 5 && mp[i].y > mousePos.y - 5 && mp[i].y < mousePos.y + 5) {
                gr.setColor(Color.red);
                gr.fillRect(mp[i].x - 5, mp[i].y - 5, 10, 10);
                lbl.setText(FactoryUtils.formatDouble(cm.toDoubleArray2D()[0][i]) + " , " + FactoryUtils.formatDouble(cm.toDoubleArray2D()[1][i]));
                lbl.setLocation(mousePos.x - 30, mousePos.y - 25);
                break;
            }
        }
    }

    private void initialize() {
        lbl = new JLabel();
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

    private void paintDataPoints(Graphics2D gr, Point[] mp) {
        int r = (int)Math.round(10+getWidth()/670.0);
        for (int i = 0; i < mp.length; i++) {
            float alpha = 0.35f;
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            gr.setComposite(alcom);
            gr.setColor(Color.darkGray);
            gr.fillOval(mp[i].x - r / 2, mp[i].y - r / 2, r, r);
            gr.setColor(Color.black);
            gr.drawOval(mp[i].x - r / 2, mp[i].y - r / 2, r, r);
            alpha = 1f;
            alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            gr.setComposite(alcom);
        }
    }

    private void drawYAxis(Graphics2D gr, int x0, int y0, int w, double[][] d) {
        double maxY = getMaxYValue(d[1]);
        double minY = getMinYValue(d[1]);
        double deltaY = maxY - minY;
        double n = 10.0;
//        int l = y0 - 60;
        int l = y0 - 50;
        int top = 50;
        gr.drawLine(x0, top, x0, y0);
        double delta = deltaY / n;
        double[] yVal = new double[(int) n + 1];
        double dY = l / n;
        int q = 0;
        int art = 5;
        int shift = 0;
        for (int i = 1; i < n; i++) {
            gr.setColor(Color.darkGray);
            yVal[i] = (Math.round((i * delta + minY) * n)) / n;
            gr.drawString(FactoryUtils.formatDoubleAsString(yVal[i], 1) + "", x0 - 70, (int) (y0 - i * dY + shift) + art);
            q = (int) (y0 - i * dY + shift);
            gr.setColor(Color.white);
            gr.drawLine(x0, q, x0 + w, q);
        }
        gr.setColor(Color.darkGray);
        gr.drawString(figureAttribute.axis[1], x0 - 5, 35);
    }

    private void drawXAxis(Graphics2D gr, int x0, int y0, int h, int w, double[][] d) {
        double maxX = getMaxXValue(d[0]);
        double minX = getMinXValue(d[0]);
        double deltaX = Math.abs(maxX - minX);
        double n = 10.0;
        int m = 160;
        int strW = getMaxWidth(gr, figureAttribute.axis);
        int l = w - (x0 + m);
        double dx = l / n;
        double delta = deltaX / n;
        double[] xVal = new double[(int) n + 1];
        gr.setColor(Color.darkGray);
//        gr.drawLine(x0, y0, w - m, y0);
        gr.drawString(figureAttribute.axis[0], w - (140), y0);
        for (int i = 1; i < n; i++) {
            gr.setColor(Color.darkGray);
            int q = (int) (x0 + i * dx);
            xVal[i] = (Math.round((i * delta + minX) * n)) / n;
            gr.drawString(FactoryUtils.formatDoubleAsString(xVal[i], 1) + "", q - 10, y0 + 30);
            gr.setColor(Color.white);
            gr.drawLine(q, y0 + 2, q, y0 - h);
        }
    }

    private int getMaxWidth(Graphics gr, String[] t) {
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getWidth(), r2.getWidth()) + 20;
        return ret;
    }

    private int getMaxHeight(Graphics gr, String[] t) {
        Rectangle2D r1 = gr.getFont().getStringBounds(t[0], 0, t[0].length(), gr.getFontMetrics().getFontRenderContext());
        Rectangle2D r2 = gr.getFont().getStringBounds(t[1], 0, t[1].length(), gr.getFontMetrics().getFontRenderContext());
        int ret = (int) Math.max(r1.getHeight(), r2.getHeight());
        return ret;
    }

    private Point[] mappingDataToScreenCoordinates(double[][] d, int w, int h, int px, int py) {
        Point[] ret = new Point[d[0].length];
        double maxY = getMaxYValue(d[1]);
        double minY = getMinYValue(d[1]);
        double deltaY = Math.abs(maxY - minY);
        double maxX = getMaxXValue(d[0]);
        double minX = getMinXValue(d[0]);
        double deltaX = Math.abs(maxX - minX);
        int m = 160;
        double cellWidth = (w - (px + m)) / deltaX;
        double cellHeight = ((h - 140) / deltaY);

        for (int j = 0; j < d[0].length; j++) {
            Point p = new Point();
            p.x = (int) (px + (d[0][j] - minX) * cellWidth);
            p.y = (int) (py - (d[1][j] - minY) * cellHeight);
            ret[j] = p;
        }
        return ret;
    }

    private double getMaxYValue(double[] d) {
        double ret = 0;
        ret = d[0];
        for (int i = 0; i < d.length; i++) {
            if (ret < d[i]) {
                ret = d[i];
            }
        }
        return ret;
    }

    private double getMinYValue(double[] d) {
        double ret = 0;
        ret = d[0];
        for (int i = 0; i < d.length; i++) {
            if (ret > d[i]) {
                ret = d[i];
            }
        }
        return ret;
    }

    private double getMaxXValue(double[] d) {
        double ret = 0;
        ret = d[0];
        for (int i = 0; i < d.length; i++) {
            if (ret < d[i]) {
                ret = d[i];
            }
        }
        return ret;
    }

    private double getMinXValue(double[] d) {
        double ret = 0;
        ret = d[0];
        for (int i = 0; i < d.length; i++) {
            if (ret > d[i]) {
                ret = d[i];
            }
        }
        return ret;
    }

//    public void setOnlyR(boolean onlyR) {
//        this.onlyR = onlyR;
//        repaint();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
    }// </editor-fold>//GEN-END:initComponents

    public void setFigureAttribute(TFigureAttribute attr) {
        this.figureAttribute = attr;
        if (figureAttribute.perfMetricStr.size() == 0) {
            figureAttribute.perfMetricStr.add(EPerformanceMetrics.R.name());
            figureAttribute.perfMetricStr.add(EPerformanceMetrics.R2.name());
            figureAttribute.perfMetricStr.add(EPerformanceMetrics.NSEC.name());
            figureAttribute.perfMetricStr.add(EPerformanceMetrics.MPE.name());
            figureAttribute.perfMetricStr.add(EPerformanceMetrics.RMSE.name());
        }
        this.figureAttribute.perfMetricVal = FactoryStatistic.calculateStatistics(
                cm.toDoubleArray2D()[0],
                cm.toDoubleArray2D()[1],
                this.figureAttribute.perfMetricStr);
        this.figureAttribute.axis = new String[]{"Observed", "Simulated"};
        repaint();
    }

    public TFigureAttribute getFigureAttribute() {
        return this.figureAttribute;
    }

    public void setMatrix(CMatrix cm) {
        cm = cm.transpose();
        this.cm = cm;
        this.figureAttribute.perfMetricVal = FactoryStatistic.calculateStatistics(
                cm.toDoubleArray2D()[0],
                cm.toDoubleArray2D()[1],
                this.figureAttribute.perfMetricStr);
//        this.figureAttribute.axis = new String[]{"Observed", "Simulated"};
        this.figureAttribute.axis = axis;
        repaint();
    }

    public CMatrix getMatrix() {
        return this.cm;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
