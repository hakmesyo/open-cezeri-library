/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.factory.FactoryUtils;
import static cezeri.factory.FactoryUtils.generateColor;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;

/**
 *
 * @author BAP1
 */
public class PanelPlot extends javax.swing.JPanel {

    private CMatrix f;
    private double[] xAxis;
    private Color[] color;
    private boolean isShowPoint = true;
    private boolean lblShow = false;
    private Point mousePos = new Point(0, 0);
    private JLabel lbl = null;
    private double scale = 1;
    private TFigureAttribute figureAttribute;
    private String[] items;
    private long rand_seed;
    private float alpha_blending = 0.65f;
    private boolean isMousePressed = false;
    Graphics2D gr;
    CPoint[][] mp;
    int selectedLineIndex = -1;
    boolean isLineSelected = false;

    public PanelPlot(CMatrix ff) {
        this.f = ff.transpose();
        items = new String[f.getRowNumber()];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Line-" + (i + 1);
        }
        this.figureAttribute = new TFigureAttribute();
        initialize();
        repaint();
    }

    public PanelPlot(CMatrix ff, double[] x) {
        this.xAxis = x;
        this.f = ff.transpose();
        items = new String[f.getRowNumber()];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Line-" + (i + 1);
        }
        this.figureAttribute = new TFigureAttribute();
        initialize();
        repaint();
    }

    public PanelPlot(CMatrix ff, TFigureAttribute attr) {
        this.f = ff.transpose();
        this.figureAttribute = attr;
        this.items = attr.items;
        initialize();
        repaint();
    }

    public PanelPlot(CMatrix ff, TFigureAttribute attr, double[] x) {
        this.xAxis = x;
        this.f = ff.transpose();
        this.figureAttribute = attr;
        this.items = attr.items;
        initialize();
        repaint();
    }

    public void setXAxis(double[] x) {
        this.xAxis = x;
        repaint();
    }

    public void paint(Graphics gr1) {
        gr = (Graphics2D) gr1;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font fnt = gr.getFont();
        gr.setFont(new Font(fnt.getFontName(), 1, 18));
        gr.setColor(Color.white);
        int w = getWidth();
        int h = getHeight();
        int fromRight = 150;
        int fromLeft = 100;
        int fromTop = 50;
        int fromBottom = 70;
        int canvas_width = getWidth() - (fromLeft + fromRight);
        int canvas_height = getHeight() - (fromBottom + fromTop);
        gr.fillRect(0, 0, w, h);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);
        gr.setColor(Color.darkGray);
        int titleWidth = FactoryUtils.getGraphicsTextWidth(gr, figureAttribute.title);
        int pTitleX = (w - titleWidth) / 2;
        gr.drawString(figureAttribute.title, pTitleX, 30);
        int px = 100;
        int py = h - 90;
        gr.setColor(Color.white);
        gr.setColor(Color.decode("#EAEAF2"));
        gr.fillRect(fromLeft, fromTop, canvas_width, canvas_height);
        drawYAxis(gr, px, py, w, fromRight, f.toDoubleArray2D());
        drawXAxis(gr, px, py, w, fromRight, f.toDoubleArray2D());
        drawItems(gr, this.items, w, h);

        mp = mappingDataToScreenCoordinates(f.toDoubleArray2D(), fromLeft, fromTop, canvas_width, canvas_height);
        if (isMousePressed) {
            selectedLineIndex = selectLine(mp);
            isMousePressed = false;
        }
        for (int r = 0; r < f.getRowNumber(); r++) {
            drawPolyLines(gr, mp, r);
        }
        drawMouseAxis(gr, mousePos, fromLeft, fromTop, canvas_width, canvas_height);
        checkDataPoints(gr, mp, fromLeft, fromTop, canvas_width, canvas_height);
        gr.setStroke(new BasicStroke());
        gr.setColor(Color.black);
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
                if (c == 0) {
                    p.column = (int) Math.round(fromLeft + (c) * cellWidth);
                } else {
                    p.column = (int) Math.round(fromLeft + (c + 1) * cellWidth);
                }

                p.row = (int) Math.round((fromTop + h) - (d[r][c] - minY) * cellHeight);
                ret[r][c] = p;
            }
        }
        return ret;
    }

    public void setMatrix(CMatrix m) {
        this.f = m.transpose();
        rand_seed = System.currentTimeMillis();
        color = FactoryUtils.getRandomColors(f.getRowNumber(), rand_seed);
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
                isMousePressed = true;
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                isMousePressed = false;
            }

        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
//                mousePos = e.getPoint();
//                lbl.setVisible(true);
//                repaint();
            }
        });

    }

    private int selectLine(CPoint[][] mp) {
        int ret = -1;
        int nr = mp.length;
        int nc = mp[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (mp[i][j].row > mousePos.y - 5
                        && mp[i][j].row < mousePos.y + 5
                        && mp[i][j].column > mousePos.x - 5
                        && mp[i][j].column < mousePos.x + 5) {
                    ret = i;
                    isLineSelected = true;
                    return ret;

                }
            }
        }
        isLineSelected = false;
        isMousePressed = false;
        return ret;
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

    private void drawPolyLines(Graphics2D gr, CPoint[][] mp, int nr) {
        gr.setColor(color[nr]);
        int[] xp = new int[f.getColumnNumber()];
        int[] yp = new int[f.getColumnNumber()];
        for (int j = 0; j < f.getColumnNumber(); j++) {
            CPoint p = mp[nr][j];
            yp[j] = p.row;
            xp[j] = p.column;
        }
        String pType = figureAttribute.pointType;
        Stroke prev = gr.getStroke();
        Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        if (selectedLineIndex != -1) {
            if (selectedLineIndex == nr) {
//                System.out.println("buldu:" + nr);
                float alpha = 0.75f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
                stroke = new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            } else {
                float alpha = 0.15f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
                stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
        } else {
            float alpha = this.alpha_blending;
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            gr.setComposite(alcom);
        }
        if (pType.contains("-")) {
            if (figureAttribute.isStroke) {
                gr.setStroke(figureAttribute.stroke.get(nr));
                gr.drawPolyline(xp, yp, f.getColumnNumber());
            } else {
                gr.setStroke(stroke);
                gr.drawPolyline(xp, yp, f.getColumnNumber());
            }
        }
        if (isShowPoint) {
            paintDataPoints(gr, xp, yp);
        }
        float alpha = 1f;
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        gr.setComposite(alcom);
        gr.setStroke(prev);
    }

    private void drawMouseAxis(Graphics2D gr, Point mousePos, int fromLeft, int fromTop, int canvas_width, int canvas_height) {
        if (mousePos.x > fromLeft && mousePos.x < fromLeft + canvas_width && mousePos.y > fromTop && mousePos.y < fromTop + canvas_height) {
            Color prevColor = gr.getColor();
            gr.setColor(Color.red);
            Stroke prev = gr.getStroke();
            Stroke s = new BasicStroke(2.0f, // Width 
                    BasicStroke.CAP_SQUARE, // End cap 
                    BasicStroke.JOIN_MITER, // Join style 
                    10.0f, // Miter limit 
                    new float[]{5.0f, 5.0f}, // Dash pattern 
                    0.0f);
            gr.setStroke(s);
            float alpha = 0.35f;
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            gr.setComposite(alcom);
            gr.drawLine(fromLeft, mousePos.y, fromLeft + canvas_width, mousePos.y);
            gr.drawLine(mousePos.x, fromTop, mousePos.x, fromTop + canvas_height);

            alpha = 1f;
            alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            gr.setComposite(alcom);
            gr.setStroke(prev);
            gr.setColor(prevColor);
        }
    }

    private void checkDataPoints(Graphics gr, CPoint[][] mp, int fromLeft, int fromTop, int canvas_width, int canvas_height) {
        for (int i = 0; i < mp.length; i++) {
            for (int j = 0; j < mp[0].length; j++) {
//                if (    mp[i][j].row > mousePos.y - 5 && 
//                        mp[i][j].row < mousePos.y + 5 && 
//                        mp[i][j].column > mousePos.x - 5 && 
//                        mp[i][j].column < mousePos.x + 5) {
                if (mp[i][j].column > mousePos.x - 5 && mp[i][j].column < mousePos.x + 5) {
                    gr.setColor(Color.red);
                    gr.fillRect(mp[i][j].column - 5, mp[i][j].row - 5, 10, 10);
                    gr.setColor(Color.red);
                    gr.drawRect(mp[i][j].column - 25, fromTop - 20, 50, 20);
                    gr.drawRect(mp[i][j].column - 25, fromTop + canvas_height + 3, 50, 20);
                    gr.setColor(Color.gray);
                    gr.drawString("" + j, mp[i][j].column - 17, fromTop - 3);
                    gr.drawString("" + j, mp[i][j].column - 17, fromTop + canvas_height + 20);
                    break;
                }
            }
        }
    }

    private void paintDataPoints(Graphics gr, int[] xp, int[] yp) {
        int r = 6;
        for (int i = 0; i < xp.length; i++) {
            gr.fillRect(xp[i] - r / 2, yp[i] - r / 2, r, r);
        }
    }

    private void drawYAxis(Graphics gr, int x0, int y0, int w, int fromRight, double[][] d) {
        double maxY = getMaxYValue(d);
        double minY = getMinYValue(d);
        double deltaY = maxY - minY;
        double n = Math.round(y0 / 100.0);
        int l = y0 - 50;
        int top = 50;
        gr.drawLine(x0, top, x0, y0 + 20);
        gr.drawLine(w - fromRight, top, w - fromRight, y0 + 20);
        double delta = deltaY / n;
        double[] yVal = new double[(int) n + 1];
        double dY = l / n;
        int q = 0;
        int art = 5;
        int shift = 20;
        for (int i = 0; i <= n; i++) {
            gr.setColor(Color.darkGray);
            if (maxY > 1) {
                yVal[i] = Math.round(Math.round((i * delta + minY) * n) / n);
            } else {
                yVal[i] = (i * delta + minY);
            }

            gr.drawString(FactoryUtils.formatDoubleAsString(yVal[i], 3) + "", x0 - 80, (int) (y0 - i * dY + shift) + art);
            q = (int) (y0 - i * dY + shift);
            gr.setColor(Color.white);
            if (i < n) {
                gr.drawLine(x0, q - (int) dY / 2, x0 + w - fromRight, q - (int) dY / 2);
            }
            gr.drawLine(x0, q, x0 + w - fromRight, q);
        }
        gr.setColor(Color.darkGray);
        gr.drawString(figureAttribute.axis_names[1], 20, 35);
    }

    private void drawXAxis(Graphics gr, int x0, int y0, int w, int fromRight, double[][] d) {
        gr.setColor(Color.darkGray);
        double maxX = 0;
        double minX = 0;
        double n = Math.round(w / 150.0);
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
        gr.drawString(figureAttribute.axis_names[0], w - 110, y0 + 40);
        int q = 0;
        for (int i = 0; i <= n; i++) {
            gr.setColor(Color.darkGray);
            if (i < n) {
                xVal[i] = (Math.round(minX + i * delta));
            } else {
                xVal[i] = (Math.round(minX + i * delta)) - 1;
            }

            q = (int) (x0 + i * dx);
            gr.drawString(FactoryUtils.formatDoubleAsString(xVal[i], 3), q - 10, y0 + 40);
            gr.setColor(Color.white);
            if (i < n) {
                gr.drawLine(q + (int) dx / 2, top, q + (int) dx / 2, y0 + 20);
            }
            gr.drawLine(q, top, q, y0 + 20);
        }
        gr.setColor(Color.darkGray);
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
    private void drawItems(Graphics2D gr, String[] items, int w, int h) {
        gr.setColor(Color.gray);
        gr.drawString("Items", w - 110, 100);
        gr.drawString("--------------", w - 130, 120);
        int dt = 25;
        for (int i = 0; i < items.length; i++) {
            gr.setColor(color[i]);
            gr.drawString(items[i], w - 120, 140 + dt * i);
        }

    }

    public void setRandomSeed(long currentTimeMillis) {
        this.rand_seed = currentTimeMillis;
        color = FactoryUtils.getRandomColors(f.getRowNumber(), rand_seed);
    }

}
