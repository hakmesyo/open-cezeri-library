package cezeri.gui;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelCanvas extends JPanel {

    private JLabel lbl;
    private int fromLeft = 10;
    private int fromTop = 30;
    private Point mousePos = new Point(0, 0);
    private CPoint[] nodes, edges;
    private CPoint[] norm_nodes, norm_edges;
    public boolean isEdgeVisible = false;
    public boolean isNodeVisible = false;
    private int pan_width, pan_height;
    private double totDist = 0;
    private boolean isManual = false;
    private CPoint prevCity, currentCity;

    public PanelCanvas() {
        initialize();
    }

    public void updatePath(CPoint[] _nodes, boolean nodevis, CPoint[] _edges, boolean edgevis) {
        isNodeVisible = nodevis;
        isEdgeVisible = edgevis;
        if (_nodes != null) {
            nodes = _nodes;
            norm_nodes = new CPoint[nodes.length];
        }
        if (_edges != null) {
            edges = _edges;
            norm_edges = new CPoint[edges.length + 1];
            totDist = calculateTotalCost() - 2;
        }
        repaint();
    }

    @Override
    public void paint(Graphics _gr) {
        Graphics2D gr = (Graphics2D) _gr;
        pan_width = this.getWidth();
        pan_height = this.getHeight();
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.setColor(Color.GREEN);
        int wPanel = this.getWidth();
        int hPanel = this.getHeight();
        if (isNodeVisible) {
            normalizeNodeData();
            drawNodes(gr, norm_nodes);
        }
        if (isEdgeVisible) {
            normalizeEdgeData();
            drawEdges(gr, norm_edges);
        }
        if (isManual) {
            drawManualEdges(gr);
        }
        int px = mousePos.x - fromLeft;
        int py = mousePos.y - fromTop;
        lbl.setText("Total Path Cost = " + (int) totDist + " Pos = [ " + py + " : " + px + " ] Selected Cities : " + (k - 1));
        this.paintComponents(gr);
        gr.setColor(Color.red);
        gr.drawRect(0, 0, wPanel - 1, hPanel - 1);
        gr.drawRect(1, 1, wPanel - 3, hPanel - 3);

    }

    int k = 0;

    private void initialize() {
        lbl = new JLabel("X:Y");
        this.add(lbl);
        lbl.setBounds(new Rectangle(10, 0, 500, 30));
        lbl.setBackground(Color.yellow);
        lbl.setForeground(Color.GREEN);
        lbl.setVisible(true);
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean lblShow;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    if (checkFirstCity(e)) {
                        isManual = !isManual;
                        System.out.println("başlangıç şehiri tıklandımı?" + isManual);
                        if (isManual) {
                            edges = new CPoint[nodes.length + 1];
                            norm_edges = new CPoint[nodes.length + 1];
                            edges[0] = nodes[0].cloneCP();
                            currentCity = nodes[0];
                            prevCity = currentCity.cloneCP();
                            k++;
                            normalizeNodeData();
                            normalizeEdgeData();
                        }
                    }
                } else if (isManual && e.getClickCount() == 1) {
                    int city_index = getClickedCity(e);
                    if (city_index != -1 && city_index != -2) {
                        System.out.println("city_index = " + city_index);
                        currentCity = nodes[city_index].cloneCP();
                        double dist = getDistance(currentCity, prevCity);
                        System.out.println("dist = " + dist);
                        totDist += dist;
                        edges[k++] = currentCity;
                        prevCity = currentCity.cloneCP();
                        normalizeNodeData();
                        normalizeEdgeData();
                    }

                }
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                lblShow = true;
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                lblShow = false;
            }

            private boolean checkFirstCity(MouseEvent e) {
                boolean ret = false;
                if (Math.abs(e.getPoint().x - norm_nodes[0].column) < 10 && Math.abs(e.getPoint().y - norm_nodes[0].row) < 10) {
                    ret = true;
                }
                return ret;
            }

            private int getClickedCity(MouseEvent e) {
                int n = -1;
                for (int i = 0; i < norm_nodes.length; i++) {
                    if (Math.abs(e.getPoint().x - norm_nodes[i].column) < 10 && Math.abs(e.getPoint().y - norm_nodes[i].row) < 10) {
                        if (!norm_nodes[i].equals(currentCity)) {
                            return i;
                        } else {
                            return -2;
                        }

                    }
                }
                return n;
            }

        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                float dw = pan_width / 500.f;
                float dh = pan_height / 500.f;
                mousePos = e.getPoint();
                mousePos.x = (int) (mousePos.x / dw) + fromLeft;
                mousePos.y = (int) (mousePos.y / dh) + fromTop;
                repaint();
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
    }

    private void drawNodes(Graphics gr, CPoint[] nodes) {
        gr.setColor(Color.yellow);
        gr.fillOval(nodes[0].column - 5, nodes[0].row - 5, 20, 20);
        for (int i = 1; i < nodes.length; i++) {
            gr.setColor(Color.blue);
            gr.fillOval(nodes[i].column, nodes[i].row, 10, 10);
            gr.setColor(Color.red);
            gr.drawOval(nodes[i].column, nodes[i].row, 10, 10);
            gr.setColor(Color.yellow);
            gr.drawString(""+i,nodes[i].column,nodes[i].row);

        }
    }

    private int calculateTotalCost() {
        int pr = edges[0].row;
        int pc = edges[0].column;
        int n = edges.length;
        double dist = 0;
        double t = 0;

        for (int i = 1; i < edges.length; i++) {
            dist = getDistance(edges[i - 1], edges[i]);
            t += dist;
        }
        dist = getDistance(edges[n - 1], edges[0]);
        t += dist;
        return (int) t;
    }

    private void drawEdges(Graphics gr, CPoint[] _edges) {
        gr.setColor(Color.green);
        int pr = _edges[0].row;
        int pc = _edges[0].column;
        int n = _edges.length - 1;
        double dist = 0;

        for (int i = 1; i < _edges.length - 1; i++) {
            dist = getDistance(edges[i - 1], edges[i]);
            pr = _edges[i - 1].row;
            pc = _edges[i - 1].column;
            gr.drawLine(pc + 5, pr + 5, _edges[i].column + 5, _edges[i].row + 5);
            gr.drawString("" + FactoryUtils.formatDouble(dist, 0), (_edges[i].column + _edges[i - 1].column) / 2, (_edges[i].row + _edges[i - 1].row) / 2);
        }
        dist = getDistance(edges[n - 1], edges[0]);
        gr.drawLine(_edges[n - 1].column + 5, _edges[n - 1].row + 5, _edges[0].column + 5, _edges[0].row + 5);
        gr.drawString("" + FactoryUtils.formatDouble(dist, 0), (_edges[0].column + _edges[n - 1].column) / 2, (_edges[0].row + _edges[n - 1].row) / 2);
    }

    private void normalizeNodeData() {
        int w = this.getWidth();
        int h = this.getHeight();
        float dw = w / 500.f;
        float dh = h / 500.f;
        for (int i = 0; i < nodes.length; i++) {
            norm_nodes[i] = nodes[i].cloneCP();
            norm_nodes[i].column = (int) (nodes[i].column * dw);
            norm_nodes[i].row = (int) (nodes[i].row * dh);
        }
    }

    private void normalizeEdgeData() {
        int w = this.getWidth();
        int h = this.getHeight();
        float dw = w / 500.f;
        float dh = h / 500.f;
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == null) {
                break;
            }
            norm_edges[i] = edges[i].cloneCP();
            norm_edges[i].column = (int) (norm_edges[i].column * dw);
            norm_edges[i].row = (int) (norm_edges[i].row * dh);
        }
    }

    private double getDistance(CPoint e1, CPoint e2) {
        return Math.sqrt(Math.pow(e1.column - e2.column, 2) + Math.pow(e1.row - e2.row, 2));
    }

    private void drawManualEdges(Graphics2D gr) {
        normalizeNodeData();
        normalizeEdgeData();
        if (k < 2) {
            return;
        }
        gr.setColor(Color.GREEN);
        double dist = 0;
        for (int i = 1; i < k; i++) {
            dist = FactoryUtils.formatDouble(getDistance(edges[i - 1], edges[i]), 0);
            gr.drawLine(norm_edges[i - 1].column + 5, norm_edges[i - 1].row + 5, norm_edges[i].column + 5, norm_edges[i].row + 5);
            gr.drawString("" + dist, (norm_edges[i].column + norm_edges[i - 1].column) / 2, (norm_edges[i].row + norm_edges[i - 1].row) / 2);
        }
    }

}
