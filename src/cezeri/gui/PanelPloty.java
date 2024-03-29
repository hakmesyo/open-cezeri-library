/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;
import cezeri.matrix.CPoint;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 *
 * @author BAP1
 */
public final class PanelPloty extends javax.swing.JPanel {

    private CMatrix cm = null;
    private JLabel lbl = null;
    private boolean lblShow = false;
    private Point mousePos = new Point(0, 0);
    private String strStat = "";
    private int fontSize = 18;
    private TFigureAttribute figureAttribute = null;
    private String[] axis = new String[]{"Range", "Value"};
    private Color[] distinct_colors;
    private double[] distinct_color_indexes;
    private long rand_seed;
    private String[] items;
    private String title = "Basic Plot";

    public PanelPloty() {
        initialize();
        repaint();
    }

    public PanelPloty(CMatrix cm) {
        this.cm = cm;
        initialize();
        double[][] dm = cm.toDoubleArray2D();
        this.items = new String[dm[0].length];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = "Line-" + (i + 1);
        }
        distinct_colors = FactoryUtils.getRandomColors(dm[0].length, rand_seed);
        repaint();
    }

    public PanelPloty(CMatrix cm, TFigureAttribute attr) {
        this.cm = cm;
        this.figureAttribute = attr;
        this.axis = attr.axis_names;
        double[][] dm = cm.toDoubleArray2D();
        if (attr.items != null) {
            this.items = attr.items;
        } else {
            this.items = new String[dm[0].length];
            for (int i = 0; i < items.length; i++) {
                this.items[i] = "Line-" + (i + 1);
            }
        }
        distinct_colors = FactoryUtils.getRandomColors(dm[0].length, rand_seed);
        this.title = attr.title;
        initialize();
        repaint();
    }

    public void setFigureAttribute(TFigureAttribute attr) {
        this.figureAttribute = attr;
        this.axis = attr.axis_names;
        this.items = attr.items;
        this.title = attr.title;
        repaint();
    }

    public void setMatrix(CMatrix cm) {
        this.cm = cm;
        //this.axis = attr.axis_names;
        double[][] dm = cm.toDoubleArray2D();
        this.items = new String[dm[0].length];
        for (int i = 0; i < items.length; i++) {
            this.items[i] = "Line-" + (i + 1);
        }
        distinct_colors = FactoryUtils.getRandomColors(dm[0].length, rand_seed);
        repaint();
    }

    public CMatrix getMatrix() {
        return this.cm;
    }

    @Override
    public void paint(Graphics gr1) {
        Graphics2D gr = (Graphics2D) gr1;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        gr.drawString(this.title, (w - title.length() * 10) / 2, 30);

        int px = 100;
        int py = h - 70;
        Rectangle r = new Rectangle(px, 50, w - 260, h - 120);
        gr.setColor(Color.decode("#EAEAF2"));
        gr.fillRect(r.x, r.y, r.width, r.height);

        drawYAxis(gr, px, py, r.width, cm.toDoubleArray2D());
        drawXAxis(gr, px, py, r.height, w, cm.toDoubleArray2D());
        CPoint[][] mp = mappingDataToScreenCoordinates(cm.toDoubleArray2D(), w, h, px, py);
        drawItems(gr, this.items, w, h);

        gr.setColor(Color.darkGray);
        paintDataPoints(gr, mp);
        if (lblShow) {
            checkDataPoints(gr, mp, px, py);
        } else {
            lbl.setVisible(false);
        }
        super.paintComponents(gr);
    }

    private void checkDataPoints(Graphics gr, CPoint[][] mp, int px, int py) {
        int nr = mp.length;
        int nc = mp[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (mp[i][j].column > mousePos.x - 5 && mp[i][j].column < mousePos.x + 5 && mp[i][j].row > mousePos.y - 5 && mp[i][j].row < mousePos.y + 5) {
                    gr.setColor(Color.red);
                    gr.fillOval(mp[i][j].column - 5, mp[i][j].row - 5, 10, 10);

                    String msg = " " + FactoryUtils.formatDouble(cm.toDoubleArray2D()[0][i], 2) + " , " + FactoryUtils.formatDouble(cm.toDoubleArray2D()[1][i], 2);
                    lbl.setText("<html><body>" + items[mp[i][j].itemIndex] + "<br>" + msg + "</body></html>");
                    lbl.setLocation(mousePos.x + 10, mousePos.y - 15);
                    return;
                }
            }
        }
        lbl.setVisible(false);
    }

    private void initialize() {
        lbl = new JLabel();
        this.add(lbl);
        lbl.setBounds(new Rectangle(10, 0, 100, 30));
        lbl.setForeground(Color.black);
        lbl.setOpaque(true);
        lbl.setBackground(Color.decode("#EAEAF2"));
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblShow = true;
                lbl.setVisible(true);
                mousePos = evt.getPoint();
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblShow = false;
            }

        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblShow = true;
                lbl.setVisible(true);
                mousePos = evt.getPoint();
                repaint();
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                lbl.setVisible(true);
                repaint();
            }
        });
    }

    private void paintDataPoints(Graphics2D gr, CPoint[][] mp) {
        int r = (int) Math.round(10 + getWidth() / 670.0);
        int nr = mp.length;
        int nc = mp[0].length;
        for (int i = 0; i < nr - 1; i++) {
            for (int j = 0; j < nc - 1; j++) {
                float alpha = 0.35f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
                gr.setColor(distinct_colors[j]);
                //gr.drawPolyline(xp, yp, f.getColumnNumber());
                gr.drawLine(mp[i][j].column, mp[i][j].row, mp[i + 1][j + 1].column, mp[i + 1][j + 1].row);
                alpha = 1f;
                alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
            }
        }
    }

    private void drawItems(Graphics2D gr, String[] items, int w, int h) {
        gr.setColor(Color.gray);
        gr.drawString("Items", w - 110, 100);
        gr.drawString("--------------", w - 130, 120);
        int dt = 25;
        for (int i = 0; i < items.length; i++) {
            gr.setColor(distinct_colors[i]);
            gr.drawString(items[i], w - 120, 140 + dt * i);
        }

    }

    private void drawYAxis(Graphics2D gr, int x0, int y0, int w, double[][] d) {
        double maxY = FactoryUtils.getMaximum(d[1]);
        double minY = FactoryUtils.getMinimum(d[1]);
        double deltaY = maxY - minY;
        double n = 10.0;
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
        gr.drawString(this.axis[1], x0 - 70, 35);
    }

    private void drawXAxis(Graphics2D gr, int x0, int y0, int h, int w, double[][] d) {
        double maxX = FactoryUtils.getMaximum(d[0]);
        double minX = FactoryUtils.getMinimum(d[0]);
        double deltaX = Math.abs(maxX - minX);
        double n = 10.0;
        int m = 160;
        int strW = FactoryUtils.getMaxWidth(gr, this.axis);
        int l = w - (x0 + m);
        double dx = l / n;
        double delta = deltaX / n;
        double[] xVal = new double[(int) n + 1];
        gr.setColor(Color.darkGray);
        gr.drawString(this.axis[0], w - (140), y0 + 30);
        for (int i = 1; i < n; i++) {
            gr.setColor(Color.darkGray);
            int q = (int) (x0 + i * dx);
            xVal[i] = (Math.round((i * delta + minX) * n)) / n;
            gr.drawString(FactoryUtils.formatDoubleAsString(xVal[i], 1) + "", q - 10, y0 + 30);
            gr.setColor(Color.white);
            gr.drawLine(q, y0 + 2, q, y0 - h);
        }
    }

    private CPoint[][] mappingDataToScreenCoordinates(double[][] d, int w, int h, int px, int py) {
        CPoint[][] ret = new CPoint[d.length][d[0].length];
        double maxY = FactoryUtils.getMaximum(d[1]);
        double minY = FactoryUtils.getMinimum(d[1]);
        double deltaY = Math.abs(maxY - minY);
        double maxX = FactoryUtils.getMaximum(d[0]);
        double minX = FactoryUtils.getMinimum(d[0]);
        double deltaX = Math.abs(maxX - minX);
        int m = 160;
        double cellWidth = (w - (px + m)) / deltaX;
        double cellHeight = ((h - 140) / deltaY);
        int nr = d.length;
        int nc = d[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                CPoint p = new CPoint();
                p.column = (int) (px + (d[0][j] - minX) * cellWidth);
                p.row = (int) (py - (d[1][j] - minY) * cellHeight);
                ret[i][j] = p;
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

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        repaint();
    }

    void setRandomSeed(long seed) {
        rand_seed = seed;
    }

    void setClassTitle(String class_title) {
        items = class_title.split(",");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
