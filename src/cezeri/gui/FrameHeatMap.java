/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author BAP1
 */
public final class FrameHeatMap extends javax.swing.JFrame {

    private CMatrix cm;
    /**
     * Creates new form CPlot
     */
    public FrameHeatMap() {
        initComponents();
    }

    public FrameHeatMap(CMatrix cm) {
        this.cm = cm;
        initComponents();
        getHeatMapPanel().setMatrix(cm);
        this.setTitle(cm.name+" :: HeatMap");
        cm.plotPanel=getHeatMapPanel();
    }
    
    public FrameHeatMap(CMatrix cm, int width, int height) {
        this.cm = cm;
        initComponents();
        getHeatMapPanel().setMatrix(cm);
        this.setTitle(cm.name+" :: HeatMap");
        this.setSize(width, height+60);
        cm.plotPanel=getHeatMapPanel();
        repaint();
    }

    public FrameHeatMap(CMatrix cm, int width, int height, boolean showCellEdge) {
        this.cm = cm;
        initComponents();
        getHeatMapPanel().setMatrix(cm);
        getHeatMapPanel().setShowCellEdges(showCellEdge);
        this.setTitle(cm.name+" :: HeatMap");
        this.setSize(width, height+60);
        cm.plotPanel=getHeatMapPanel();
        repaint();
    }

    public FrameHeatMap(CMatrix cm, int width, int height, boolean showCellEdge, boolean showValue) {
        this.cm = cm;
        initComponents();
        getHeatMapPanel().setMatrix(cm);
        getHeatMapPanel().setShowCellEdges(showCellEdge);
        getHeatMapPanel().setShowValue(showValue);
        this.setTitle(cm.name+" :: HeatMap");
        this.setSize(width, height+60);
        cm.plotPanel=getHeatMapPanel();
        repaint();
    }

    public void setMatrix(CMatrix cm) {
        cm = cm.transpose();
        this.cm = cm;
        repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_heat_map = new cezeri.gui.PanelHeatMap(cm);
        jPanel2 = new javax.swing.JPanel();
        btn_dataGrid = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(700, 730));

        panel_heat_map.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panel_heat_mapLayout = new javax.swing.GroupLayout(panel_heat_map);
        panel_heat_map.setLayout(panel_heat_mapLayout);
        panel_heat_mapLayout.setHorizontalGroup(
            panel_heat_mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panel_heat_mapLayout.setVerticalGroup(
            panel_heat_mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 563, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_dataGrid.setText("Data Grid");
        btn_dataGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dataGridActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn_dataGrid)
                .addContainerGap(720, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_dataGrid)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_heat_map, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_heat_map, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_dataGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dataGridActionPerformed
        CMatrix cm = getHeatMapPanel().getMatrix();
        new FrameDataGrid(cm).setVisible(true);
    }//GEN-LAST:event_btn_dataGridActionPerformed

    public PanelHeatMap getHeatMapPanel() {
        return (PanelHeatMap) panel_heat_map;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePlot.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrameHeatMap frm = new FrameHeatMap(CMatrix.getInstance().randn(2, 130).timesScalar(1));
                frm.setVisible(true);
                String[] s = {"Observed", "Simulated"};
//                frm.getScatterPlotPanel().setAxisText(s);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_dataGrid;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panel_heat_map;
    // End of variables declaration//GEN-END:variables

    public void setFromColor(Color fromColor) {
        getHeatMapPanel().setFromColor(fromColor);
    }
    public void setCenterColor(Color centerColor) {
        getHeatMapPanel().setCenterColor(centerColor);
    }
    public void setToColor(Color toColor) {
        getHeatMapPanel().setToColor(toColor);
    }

}
