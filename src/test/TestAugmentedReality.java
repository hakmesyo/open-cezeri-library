/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.gui.FrameImage;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.matrix.CRectangle;
import cezeri.factory.FactoryUtils;
import com.github.sarxos.webcam.Webcam;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import org.opencv.core.Core;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author BAP1
 */
public class TestAugmentedReality {

    private static CRectangle rect = new CRectangle();
    private static CRectangle prevRect = new CRectangle(rect);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        CMatrix cm = CMatrix.getInstance().imread("images\\yuzler.jpg").detectFaces("haar").imshow();

        Webcam webcam = Webcam.getDefault();

        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
        BufferedImage bf = webcam.getImage();
        FrameImage frm = new FrameImage(bf);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
        
        BufferedImage[] imgs=new BufferedImage[3];
        imgs[0]=ImageProcess.readImageFromFile("images\\60_imge.png");
        imgs[1]=ImageProcess.readImageFromFile("images\\90_imge.png");
        imgs[2]=ImageProcess.readImageFromFile("images\\120_imge.png");
        BufferedImage overlayOriginal = imgs[0];
        BufferedImage overlay;

        double scale = 1.5;
        long t1 = FactoryUtils.tic();
        long t2 = FactoryUtils.tic();
        CRectangle r = new CRectangle();
        BufferedImage temp;
        int angleIndex=0;
        
        //left and top coefficients
        double[] lc={2.7,4.5,9}; 
        double[] tc={19,19,19};

        double rectDifference = 0.03;  //bir önceki rectangle dan yüzde ne kadar farklı
        while (true) {
            System.out.println("----------------------------------------------");
            t1 = FactoryUtils.toc("entrance:", t1);
            bf = webcam.getImage();
            t1 = FactoryUtils.toc("get image:", t1);

            bf = ImageProcess.flipVertical(bf);
            t1 = FactoryUtils.toc("flip vertical:", t1);

            bf = ImageProcess.detectFaces("haar", bf, rect, false);
            System.out.println("rect = " + rect);
            t1 = FactoryUtils.toc("face detection:", t1);

            //haar ile elde edilen rectangle ile yüz kırpılıyor ve 
            //merkezi bulunuyor. Eğer merkezin x koordinatı rectangle merkezinden sol tarafta ise yüz sağa dönmüş
            //tersi olduğunda sola dönmüştür. Ağırlık merkezinin uzaklaşmasının şiddeti dönmenin büyüklüğüne işaret eder.
            temp = ImageProcess.cropImage(bf, rect);
            t1 = FactoryUtils.toc("crop image:", t1);
            temp = ImageProcess.revert(temp);
            t1 = FactoryUtils.toc("revert image:", t1);
            temp = ImageProcess.binarizeColorImage(temp, 200);
            t1 = FactoryUtils.toc("binarization:", t1);
            CPoint cp = ImageProcess.getCenterOfGravityGray(temp, false);
            t1 = FactoryUtils.toc("center of gravity:", t1);
            temp = ImageProcess.fillRectangle(temp, cp.row - 2, cp.column - 2, 5, 5, Color.red);
            t1 = FactoryUtils.toc("draw center:", t1);
            
//            CMatrix.frameImage=temp;
//            CMatrix.frameImage.setBounds(700, 70, 350, 350);
            CMatrix cm=CMatrix.getInstance(temp);
            cm.imshowRefresh(cp.toString());
            t1 = FactoryUtils.toc("image show:", t1);
            System.out.println("cp = " + cp);
            if (cp.column<(rect.width/2-10)) {
                angleIndex=0;
            }else if(cp.column>rect.width/2+10){
                angleIndex=2;
            }else{
                angleIndex=1;                
            }
            overlayOriginal = imgs[angleIndex];
            t1 = FactoryUtils.toc("reload image:", t1);
            
            if (isBigDifference(rectDifference)) {
                overlay = ImageProcess.resize(overlayOriginal, (int) (rect.width * scale), (int) (rect.height * scale));
                r.column = rect.column - (int) (rect.width / lc[angleIndex]);
                r.row= rect.row - (int) (rect.height / tc[angleIndex]);
            } else {
                overlay = ImageProcess.resize(overlayOriginal, (int) (prevRect.width * scale), (int) (prevRect.height * scale));
                r.column = prevRect.column - (int) (prevRect.width / lc[angleIndex]);
                r.row = prevRect.row - (int) (prevRect.height / tc[angleIndex]);
            }
            t1 = FactoryUtils.toc("correct rect calc:", t1);

            bf = ImageProcess.overlayImage(bf, overlay, r, 0);
            t1 = FactoryUtils.toc("overlay image:", t1);
            bf = ImageProcess.drawText(bf, "True FPS:" + Math.round(1E9 / (System.nanoTime() - t2)), 5, 15, Color.yellow);
            t1 = FactoryUtils.toc("drawText:", t1);
            frm.setImage(bf);
            t1 = FactoryUtils.toc("frame set image:", t1);
            t2 = FactoryUtils.toc("each cycle:", t2);
        }
    }

    private static boolean isBigDifference(double threshold) {
        double widthDifference = 1.0 * Math.abs(rect.width - prevRect.width) / rect.width;
        double heightDifference = 1.0 * Math.abs(rect.height - prevRect.height) / rect.height;
        double xDiff = 1.0 * Math.abs(rect.column - prevRect.column) / rect.column;
        double yDiff = 1.0 * Math.abs(rect.row - prevRect.row) / rect.row;
        if (widthDifference >= threshold || heightDifference >= threshold || xDiff >= threshold || yDiff >= threshold) {
            prevRect = new CRectangle(rect);
//            System.out.println("big difference is detected\n************************");
            return true;
        } else {
//            System.out.println("diff width:" + widthDifference + " diff height:" + heightDifference + " diff x:" + xDiff + " diff y:" + yDiff);
//            rect=new Rectangle(prevRect);
            return false;
        }
    }
}
