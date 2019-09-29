/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TPanelData;
import cezeri.matrix_processing.CMatrix;
import cezeri.utils.FactoryUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/**
 *
 * @author BAP1
 */
public class PanelBar extends TPanelData {

    private int[][] hist;
    private double scale = 1;
    private Color[] color;

    public PanelBar(CMatrix cm) {
        super(cm);
        this.hist = FactoryUtils.transpose(getMatrix().toIntArray2D());
        color = FactoryUtils.generateColor(hist.length);
        initialize();
        repaint();
    }

    public int[][] getHistogramData() {
        return this.hist;
    }

    public void setHistogramData(CMatrix cm) {
        this.hist = FactoryUtils.transpose(getMatrix().toIntArray2D());
        repaint();

    }

    private void initialize() {
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();
    }

    @Override
    public void paint(Graphics gr1) {
        //this.hist = FactoryUtils.transpose(getMatrix().to2DArrayInteger());
        Graphics2D gr = (Graphics2D) gr1;
        gr.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
         Font fnt = gr.getFont();
//        gr.setFont(new Font(fnt.getFontName(), 1, 18));
        gr.setColor(Color.white);
        int w = getWidth();
        int h = getHeight();
        gr.fillRect(0, 0, w, h);
        int px = 10;
        int py = 10;
        int dx = w - 2 * px;
        int dy = h - 2 * py;

        Point[][] mp = mappingDataToScreenCoordinates(getMatrix().toDoubleArray2D(), dx, dy, px, py);

        if (isActivateDataCursor()) {
            gr.setColor(Color.red);
            checkDataPoints(gr1, mp);
        }

        gr.setColor(Color.black);
        gr.drawRect(px - 2, py, dx + 5, dy);
        drawAxisX(gr, dx, dy, px, py);
        drawAxisY(gr, dx, dy, px, py);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);
        this.paintComponents(gr);
    }

    private void drawAxisX(Graphics2D gr, int dx, int dy, int px, int py) {
        double[] d = FactoryUtils.toDoubleArray1D(hist[0]);
        int n = d.length / 5;
        int nd = FactoryUtils.getDigitNumber(n);
        if (nd == 1) {
            nd++;
        } else {
            int coeff = (nd - 1) * 10;
            //biiznillah yuvarlıyor 0,50,100 mesela
            n = (n / coeff) * coeff;
        }
        for (int i = 0; i <= 5; i++) {
            int x = px + (int) (dx * n * 1.0 / d.length * i);
            if (i * n != 0) {
                gr.drawString(i * n + "", x - 5, dy + py + 20);
            }
//            gr.drawLine(x, py, x, py + 5);
//            gr.drawLine(x, dy + py, x, dy + py + 5);
        }
    }

    private void drawAxisY(Graphics2D gr, int dx, int dy, int px, int py) {
        int maxY = FactoryUtils.getMaximum(hist);
        double normY = (dy - 30) * 1.0 / maxY;
        int qy = (int) (maxY * normY);
        qy = ((qy / 10) + 1) * 10;
        int deltaY = qy / 5;
        int nd = FactoryUtils.getDigitNumber(maxY);
        int coeff = (int) Math.pow(10, nd - 1);
        //biiznillah yuvarlıyor 0,50,100 mesela
//        int n = (maxY / coeff + 1) * coeff / 5;
        int n = (maxY / (coeff + 1)) * coeff / 5;
        int a = 50;
        int enBuyukY = 0;

//        for (int i = 0; i <= 5; i++) {
//            int y = py + deltaY * i;
//            enBuyukY = n * i;
//            gr.drawString(enBuyukY + "", px - 40, dy + py - y + a);
//            gr.drawLine(px - 5, dy + py - y + a, px + 5, dy + py - y + a);
//            gr.drawLine(px + dx - 5, dy + py - y + a, px + dx, dy + py - y + a);
//        }
        for (int k = 0; k < hist.length; k++) {
            double normX = dx * 1.0 / hist[k].length;
            int x = 0;
            int x2 = 0;
            int y = 0;
            for (int i = 0; i < hist[k].length; i++) {
                x = (int) (i * normX);
                x2 = (int) ((i + 1) * normX);
                y = (int) (hist[k][i] * normY);
//                if (i > 255) {
//                    continue;
//                }
                float alpha = 0.5f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
                gr.setColor(color[k]);
                gr.fillRect(px + x, py + dy - y, (x2 - x), y);
                gr.setColor(Color.black);
                gr.drawString(hist[k][i] + "", px + x + 2, dy + py - y - 3);
                gr.drawRect(px + x, py + dy - y, (x2 - x), y);
            }
        }
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
