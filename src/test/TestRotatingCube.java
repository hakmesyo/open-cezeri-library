/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author cappello
 */
public class TestRotatingCube extends JPanel {

    private double vertex[][][][]
            = {
                {
                    {
                        {-1, -1, -1},
                        {1, -1, -1}
                    },
                    {
                        {-1, 1, -1},
                        {1, 1, -1}
                    }
                },
                {
                    {
                        {-1, -1, 1},
                        {1, -1, 1}
                    },
                    {
                        {-1, 1, 1},
                        {1, 1, 1}
                    }
                }
            };

    // rotations in radians
    private double xyR = 0.005,
            xzR = 0.005,
            yzR = 0.005;

    // view attributes
    private final static int IMAGE_SIZE = 500,
            SCALE = 100,
            OFFSET = 200,
            DIAMETER = 8;

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

        // rotate cube
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    xyRotate(vertex[x][y][z], Math.sin(xyR), Math.cos(xyR));
                    xzRotate(vertex[x][y][z], Math.sin(xzR), Math.cos(xzR));
                    yzRotate(vertex[x][y][z], Math.sin(yzR), Math.cos(yzR));
                }
            }
        }

        // draw cube edges
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                graphics.setColor(Color.red);
                drawEdge(vertex[x][y][0][0], vertex[x][y][0][1], vertex[x][y][1][0], vertex[x][y][1][1], graphics);
                graphics.setColor(Color.blue);
                drawEdge(vertex[x][0][y][0], vertex[x][0][y][1], vertex[x][1][y][0], vertex[x][1][y][1], graphics);
                graphics.setColor(Color.green);
                drawEdge(vertex[0][x][y][0], vertex[0][x][y][1], vertex[1][x][y][0], vertex[1][x][y][1], graphics);
            }
        }

        // draw cube vertices
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    drawVertex(vertex[x][y][z][0], vertex[x][y][z][1], graphics);
                }
            }
        }
    }

    // apply plane rotation to 3D point, only 2 coordinates are affected
    final void xyRotate(double p[], double sin, double cos) {
        double temp;
        temp = cos * p[0] + sin * p[1];
        p[1] = -sin * p[0] + cos * p[1];
        p[0] = temp;
    }

    final void xzRotate(double p[], double sin, double cos) {
        double temp;
        temp = cos * p[0] + sin * p[2];
        p[2] = -sin * p[0] + cos * p[2];
        p[0] = temp;
    }

    final void yzRotate(double p[], double sin, double cos) {
        double temp;
        temp = cos * p[1] + sin * p[2];
        p[2] = -sin * p[1] + cos * p[2];
        p[1] = temp;
    }

    final void drawEdge(double x1, double y1, double x2, double y2, Graphics g) {
        g.drawLine((int) (x1 * SCALE) + OFFSET, (int) (-y1 * SCALE) + OFFSET, (int) (x2 * SCALE) + OFFSET, (int) (-y2 * SCALE) + OFFSET);
    }

    final void drawVertex(double x, double y, Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval((int) (SCALE * x) + OFFSET - DIAMETER / 2, (int) (SCALE * (-y)) + OFFSET - DIAMETER / 2, DIAMETER, DIAMETER);

        g.setColor(Color.black);
        g.drawOval((int) (SCALE * x) + OFFSET - DIAMETER / 2, (int) (SCALE * (-y)) + OFFSET - DIAMETER / 2, DIAMETER, DIAMETER);
    }

    /**
     * @param args the command line arguments: Unused
     */
    public static void main(String[] args) {
        TestRotatingCube jPanel = new TestRotatingCube();
        JFrame application = new JFrame("Animation");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.add(jPanel);
        application.setSize(500, 500);
        application.setVisible(true);
        while (true) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestRotatingCube.class.getName()).log(Level.SEVERE, null, ex);
            }
            jPanel.repaint();
        }
    }
}
