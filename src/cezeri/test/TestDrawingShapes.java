/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.matrix.CMatrix;
import java.awt.Color;

/**
 *
 * @author BAP1
 */
public class TestDrawingShapes {

    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("images/horoz.jpg")
                .drawRect(10, 10, 100, 50, 5, Color.yellow)
                .imshow()
                
                ;
//        FrameImage frm=new FrameImage();
//        CMatrix cm = CMatrix.getInstance().ones(250).timesScalar(127).toNewColorSpace(BufferedImage.TYPE_3BYTE_BGR);
//        
//        cm
//                .drawRoundRect(10, 10, 100, 50, 5, 5, 5, Color.yellow).imshow()
//                .fillRoundRect(10, 10, 100, 50, 5, 5, Color.yellow).imshow()
//                .draw3DRect(10, 70, 100, 50, 5, 5, 5, Color.red).imshow()
//                .fill3DRect(10, 70, 100, 50, 5, 5, Color.green).imshow()
//                .drawArc(10, 70, 100, 100, 90, 220,5, Color.green).imshow()
//                .drawPolygon(new Polygon(new int[]{30,50,70,60,50,40,20},new int[]{10,150,40,60,50,40,20},5),2, Color.green).imshow()
//                .fillPolygon(new Polygon(new int[]{30,50,70,60,50,40,20},new int[]{10,150,40,60,50,40,20},5), Color.yellow).imshow()
//                .drawShape(new Polygon(new int[]{30,50,70,60,50,40,20},new int[]{10,150,40,60,50,40,20},5),2, Color.green).imshow()
//                .fillShape(new Polygon(new int[]{30,50,70,60,50,40,20},new int[]{10,150,40,60,50,40,20},5), Color.yellow).imshow()
//                
//                ;
//        CMatrix cm = CMatrix.getInstance().rand(250).timesScalar(255).toNewColorSpace(BufferedImage.TYPE_INT_RGB);
//        for (int i = 0; i < 100; i++) {
//            cm.drawRect(new CPoint(20+i,5+i),200,100,2, Color.blue).imshow(frm);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(TestDrawingShapes.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

    }

}
