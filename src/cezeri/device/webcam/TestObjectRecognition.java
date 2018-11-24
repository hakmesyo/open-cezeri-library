/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import cezeri.gui.FrameImage;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.utils.FactoryUtils;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author BAP1
 *
 * daha geniş anlatım ve örnekler için http://webcam-capture.sarxos.pl/
 */
public class TestObjectRecognition {

    long t = FactoryUtils.tic();
    long tt = FactoryUtils.tic();

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        TestObjectRecognition q = new TestObjectRecognition();

        List<Webcam> lst = Webcam.getWebcams();
        int index = 0;
        int counter = 0;
        for (Webcam wbc : lst) {
            System.out.println("wbc = " + wbc.getName());
//            if (wbc.getName().equals("A4 TECH HD PC Camera 1")) {
            if (wbc.getName().equals("HD Pro Webcam C920 1")) {
                index = counter;
                break;
            }
            counter++;
        }
        Dimension[] nonStandardResolutions = new Dimension[]{
            WebcamResolution.PAL.getSize(),
            WebcamResolution.HD720.getSize(),
            new Dimension(800,600),
            new Dimension(2000, 1000),
            new Dimension(1000, 500),};
        
        index=1;
        Webcam webcam = lst.get(index);
        
        webcam.setCustomViewSizes(nonStandardResolutions);
        
        for (Dimension dim : webcam.getCustomViewSizes()) {
            System.out.println("dim = " + dim);
        }
//        webcam.setCustomViewSizes(nonStandardResolutions);
//        webcam.setViewSize(new Dimension(800,600));
        webcam.setViewSize(WebcamResolution.HD720.getSize());
//        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        Webcam webcam = Webcam.getDefault();
//        webcam.setViewSize(new Dimension(176, 144));
        webcam.open();

        BufferedImage bf = webcam.getImage();
        FrameImage frm = new FrameImage(bf);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
        long t2 = FactoryUtils.tic();
        while (true) {
            bf = webcam.getImage();
////            t2=FactoryUtils.toc("image acquisition:",t2);
//            bf=ImageProcess.toBGR(bf);
////            t2=FactoryUtils.toc("toBGR:",t2);
//            bf = q.doOCL(bf);
////            bf = q.doOpenCV(bf);
////            bf = q.doOpenCV_via_OCL(bf);
            frm.setImage(bf);
        }
    }

    public BufferedImage doOpenCV_via_OCL(BufferedImage img) {
        t = FactoryUtils.toc("enterance:", t);
        BufferedImage out = ImageProcess.ocv_hsvThreshold(img, 14, 70, 150, 255, 150, 255);
        t = FactoryUtils.toc("hsv threshold:", t);

        System.out.println("--------------------------------");
        tt = FactoryUtils.toc("overall cost:", tt);
        System.out.println("--------------------------------");
        return out;
    }

    public BufferedImage doOpenCV(BufferedImage img) {
        Mat blurredImage = new Mat();
        Mat hsvImage = new Mat();
        Mat mask = new Mat();
        Mat morphOutput = new Mat();
        t = FactoryUtils.toc("enterance:", t);
        Mat frame = ImageProcess.ocv_img2Mat(img);
        t = FactoryUtils.toc("img to mat:", t);

        // remove some noise
        Imgproc.blur(frame, blurredImage, new Size(7, 7));
        t = FactoryUtils.toc("blur:", t);

        // convert the frame to HSV
        Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
        t = FactoryUtils.toc("hsv conversion:", t);

        // get thresholding values from the UI
        // remember: H ranges 0-180, S and V range 0-255
        Scalar minValues = new Scalar(0, 150, 150);
        Scalar maxValues = new Scalar(50, 255, 255);

        // show the current selected HSV range
        String valuesToPrint = "Hue range: " + minValues.val[0] + "-" + maxValues.val[0]
                + "\tSaturation range: " + minValues.val[1] + "-" + maxValues.val[1] + "\tValue range: "
                + minValues.val[2] + "-" + maxValues.val[2];

        // threshold HSV image to select tennis balls
        Core.inRange(hsvImage, minValues, maxValues, mask);
        t = FactoryUtils.toc("hsv threshold:", t);

//        // morphological operators
//        // dilate with large element, erode with small ones
//        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
//        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));
//
//        Imgproc.erode(mask, morphOutput, erodeElement);
//        Imgproc.erode(mask, morphOutput, erodeElement);
//
//        Imgproc.dilate(mask, morphOutput, dilateElement);
//        Imgproc.dilate(mask, morphOutput, dilateElement);
//        t = FactoryUtils.toc("morphological operations::", t);
        // convert the Mat object (OpenCV) to Image (JavaFX)
//        BufferedImage bf = ImageProcess.ocv_mat2Img(frame);
        BufferedImage bf = ImageProcess.ocv_mat2Img(hsvImage);
        System.out.println("--------------------------------");
        tt = FactoryUtils.toc("overall cost:", tt);
        System.out.println("--------------------------------");
        return bf;
    }

    private BufferedImage doOCL(BufferedImage bf) {
//        t = FactoryUtils.toc("entrance:", t);
//        bf = ImageProcess.flipVertical(bf);
//        t = FactoryUtils.toc("flip:", t);

//        bf=ImageProcess.rgb2hsv(bf);
//        bf=ImageProcess.ocv_img2hsv(bf);
//        t = FactoryUtils.toc("hsv conv:", t);
        /**
         * turuncu pinpon topu için en iyi değerler hue : 14-50 saturation:
         * 150-255 brightness: 150-255
         */
//        bf = ImageProcess.hsvThresholdOCL(bf, 17, 50, 150, 255, 150, 255);
        
//        bf=ImageProcess.ocv_medianFilter(bf);
//        t = FactoryUtils.toc("medain filter:", t);
        bf = ImageProcess.ocv_hsvThreshold(bf, 14, 50, 150, 255, 150, 255);
//        t = FactoryUtils.toc("hsvThreshold:", t);
        CPoint cp = ImageProcess.getCenterOfGravityGray(bf);
//        t = FactoryUtils.toc("center:", t);
        System.out.println("cp = " + cp);
        
//        bf = ImageProcess.fillOval(bf, cp.row - 5, cp.column - 5, 10, 10, Color.red);
//        t = FactoryUtils.toc("fill_oval:", t);

////////        ImageProcess.saveImage(bf, "imgeler/"+(index++)+"_imge.jpg");
////        try {
////            Thread.sleep(10);
////        } catch (InterruptedException ex) {
////            Logger.getLogger(TestObjectRecognition.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        bf = ImageProcess.drawText(bf, "True FPS:" + Math.round(1E9 / (System.nanoTime() - tt)), 1, 10, Color.yellow);
        tt = FactoryUtils.toc("overall cost:", tt);
        System.out.println("--------------------------------");
        return bf;
    }

}
