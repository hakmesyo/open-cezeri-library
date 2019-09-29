/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.types.TPanelData;
import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/**
 *
 * @author BAP1
 */
public class PanelImageHistogram extends TPanelData {

    private int[][] hist;
    private double scale = 1;

    public PanelImageHistogram(CMatrix cm) {
        super(cm);
//        this.hist = cm.to1DArrayInteger();
        if (cm.getMaxTotal() < 1) {
            cm = cm.multiplyScalar(10000);
        }
        this.hist = cm.toIntArray2D();
        initialize();
        repaint();
    }

    public int[][] getHistogramData() {
        return this.hist;
    }

    private void initialize() {
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();
    }

    @Override
    public void paint(Graphics gr1) {
        this.hist = getMatrix().toIntArray2D();
//        this.hist = getMatrix().to1DArrayDouble();
        Graphics2D gr = (Graphics2D) gr1;
        gr.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
 //        Font fnt = gr.getFont();
//        gr.setFont(new Font(fnt.getFontName(), 1, 18));
        gr.setColor(Color.white);
        int w = getWidth();
        int h = getHeight();
        gr.fillRect(0, 0, w, h);
        int px = 50;
        int py = 50;
        int dx = w - 2 * px;
        int dy = h - 2 * py;
        double normX = dx * 1.0 / hist[0].length;
        int maxY = (int) FactoryUtils.getMaximum(hist);
        double normY = (dy - 50) * 1.0 / maxY;
        int x = 0;
        int y = 0;
        Color[] rgb={Color.red,Color.green,Color.blue};
        for (int i = 0; i < hist.length; i++) {
            for (int j = 0; j < hist[0].length; j++) {
                x = (int) (j * normX);
                y = (int) (hist[i][j] * normY);
                if (i > 255) {
                    continue;
                }
                gr.setColor(new Color(j, j, j));
                gr.fillRect(px + x, (py + dy - 20), px + x + (int) normX + 1, 20);
                
                Composite oldComposite=gr.getComposite();
                float alpha = 0.35f;
                AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                gr.setComposite(alcom);
                
                if (hist.length==1) {
                    gr.setColor(Color.BLUE);
                }else if(hist.length==3){
                    gr.setColor(rgb[i]);
                }else{
                    gr.setColor(new Color((float)Math.random(),(float)Math.random(), (float)Math.random()));
                }                
                gr.drawLine(px + x, py + dy - y - 20, px + x, py + dy - 20);
                gr.setComposite(oldComposite);
            }
        }

//        Point[][] mp = mappingDataToScreenCoordinates(getMatrix().get2DArrayDouble(), dx, dy, px, py);
//        if (isActivateDataCursor()) {
//            gr.setColor(Color.red);
//            checkDataPoints(gr1, mp);
//        }

        gr.setColor(Color.black);
        gr.drawRect(px - 2, py, dx + 2, dy);
        drawAxisX(gr, dx, dy, px, py);
        drawAxisY(gr, dx, dy, px, py);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, w - 1, h - 1);
        gr.drawRect(1, 1, w - 3, h - 3);
        this.paintComponents(gr);
    }

    private void drawAxisX(Graphics2D gr, int dx, int dy, int px, int py) {
//        double[] d = getMatrix().getRow(0);
//        CMatrix cc=getMatrix();
        double[] d = FactoryUtils.toDoubleArray1D(hist[0]);
//        double[] d = (hist);
        int n = d.length / 5;
        int nd = FactoryUtils.getDigitNumber(n);
        int coeff = (nd) * 10;
        //biiznillah yuvarlıyor 0,50,100 mesela
        n = n / coeff * coeff;
        for (int i = 0; i <= 5; i++) {
            int x = px + (int) (dx * n * 1.0 / d.length * i);
            gr.drawString(i * n + "", x - 5, dy + py + 20);
            gr.drawLine(x, py, x, py + 5);
            gr.drawLine(x, dy + py, x, dy + py + 5);
        }
    }

    private void drawAxisY(Graphics2D gr, int dx, int dy, int px, int py) {
        int maxY = (int) FactoryUtils.getMaximum(hist);
        int minY = (int) FactoryUtils.getMinimum(hist);
        double normY = (dy - 50) * 1.0 / maxY;
        int qy = (int) (maxY * normY);
        qy = ((qy / 10) + 1) * 10;
        int deltaY = qy / 5;
        int nd = FactoryUtils.getDigitNumber(maxY);
        if (nd > 0) {
            int coeff = (int) Math.pow(10, nd - 1);
            //biiznillah yuvarlıyor 0,50,100 mesela
            int n = (maxY / coeff + 1) * coeff / 5;
            for (int i = 0; i <= 5; i++) {
                int y = py + deltaY * i - 30;
                gr.drawString(n * i + "", px - 40, dy + py - y);
                gr.drawLine(px, dy + py - y, px + 5, dy + py - y);
                gr.drawLine(px + dx - 5, dy + py - y, px + dx, dy + py - y);
            }
        } else {
            double n = (maxY - minY) / 5;
            for (int i = 0; i <= 5; i++) {
                int y = py + deltaY * i - 30;
                gr.drawString(String.format("%5.2e", n * i) + "", px - 40, dy + py - y);
                gr.drawLine(px, dy + py - y, px + 5, dy + py - y);
                gr.drawLine(px + dx - 5, dy + py - y, px + dx, dy + py - y);
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
