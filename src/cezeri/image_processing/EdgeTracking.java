/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

import static cezeri.image_processing.ImageProcess.getWindowEdgePixelPositions;
import cezeri.utils.FactoryUtils;
import cezeri.utils.GCode;
import cezeri.utils.GCodeList;
import cezeri.utils.GCodeWorkList;
import cezeri.vision.PanelPicture;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author BAP1
 */
public class EdgeTracking {

    public static GCodeWorkList processImageForGCode(BufferedImage img, PanelPicture pp, int windowSize,int t1,int t2) {

        GCodeWorkList w = new GCodeWorkList();
        int[][] m = ImageProcess.imageToPixelsInt(img);
        int[] hist = ImageProcess.getHistogram(m);
        boolean isFinished = false;
        int k = 0;

        for (int i = 0; i < m.length; i++) {
            for (int t = 0; t < m[0].length; t++) {
                if (m[i][t] != 255) {
                    continue;
                }

                Point p = new Point(i, t);
                GCodeList gcl = new GCodeList();
                w.wlst.add(gcl);
                GCode gc = new GCode();
                gc.p.x = p.x;
                gc.p.y = p.y;
                gc.writeMode = false;
                gc.currentPos.x = p.x;
                gc.currentPos.y = p.y;
                gcl.gCodeList.add(gc);
                int prevX = p.x;
                int prevY = p.y;

                //yaz(p.x+","+p.y);
                isFinished = false;
                inner:
                while (!isFinished) {
                    m[p.x][p.y] = 254;
                    boolean pixelBuldu = false;
                    second_inner:
                    for (int j = 1; j <= windowSize; j++) {
                        if (lookNeighbourPixels(m, p, j,t1,t2)) {
                            pixelBuldu = true;
                            break second_inner;
                        } else {
                            continue second_inner;
                        }
                    }
                    if (!pixelBuldu) {
                        isFinished = true;
                        break inner;
                    }

                    GCode gcx = new GCode();
                    gcx.currentPos.x = p.x;
                    gcx.currentPos.y = p.y;
                    gcx.p.x = p.x - prevX;
                    gcx.p.y = p.y - prevY;

//                    System.out.println(p.x + "," + p.y + " (" + gcx.p.x + "," + gcx.p.y + ")");
//                    drawLine(m, prevX, prevY, p.x, p.y, 254);
                    gcx.writeMode = true;
                    gcl.gCodeList.add(gcx);
                    prevX = p.x;
                    prevY = p.y;
                    img = ImageProcess.pixelsToImageGray(m);
                    pp.setImage(img);
                }
                System.out.println((++k) + ". work list is building size of " + gcl.gCodeList.size());
                img = ImageProcess.pixelsToImageGray(m);
                pp.setImage(img);
            }
        }
        w.img = img;
        System.out.println("iş listesi boyutu:" + w.wlst.size());
        return w;
    }

    private static boolean lookNeighbourPixels(int[][] img, Point p, int r,int t1,int t2) {
        ArrayList<Point> lst = getWindowEdgePixelPositions(img, p, r);
        for (Point pt : lst) {
            if (img[pt.x][pt.y]>t1 &&img[pt.x][pt.y]<t2) {
                p.x = pt.x;
                p.y = pt.y;
                //yaz("konumlandı: p.x:"+p.x+" p.y:"+p.y);
                return true;
            }
        }
        return false;
    }

    private static void drawLine(int[][] m, int x1, int y1, int x2, int y2, int color) {
        int w = x2 - x1;
        int h = y2 - y1;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if (w < 0) {
            dx1 = -1;
        } else if (w > 0) {
            dx1 = 1;
        }
        if (h < 0) {
            dy1 = -1;
        } else if (h > 0) {
            dy1 = 1;
        }
        if (w < 0) {
            dx2 = -1;
        } else if (w > 0) {
            dx2 = 1;
        }
        int longest = Math.abs(w);
        int shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) {
                dy2 = -1;
            } else if (h > 0) {
                dy2 = 1;
            }
            dx2 = 0;
        }
        int numerator = longest >> 1;
        for (int i = 0; i <= longest; i++) {
            m[x1][y1]=color;
            numerator += shortest;
            if (!(numerator < longest)) {
                numerator -= longest;
                x1 += dx1;
                y1 += dy1;
            } else {
                x1 += dx2;
                y1+= dy2;
            }

        }
    }

}
