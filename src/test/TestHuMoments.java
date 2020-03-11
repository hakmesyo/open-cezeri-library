/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 *
 * @author BAP1
 */
public class TestHuMoments {

    public static void main(String[] args) {
        BufferedImage img = ImageProcess.readImageFromFile("images\\kaplan1.jpg");
//        double[] huMoments=getHuMoments(img);
//        System.out.println("huMoments = " + Arrays.toString(huMoments));
        double[] moms=ImageProcess.getHuMoments(img);
        System.out.println("moms = " + Arrays.toString(moms));
    }

    //note that this method was also implemented in the ImageProcess Class
    //therefore no need to call manually. Instead call driectly from ImageProcess class
    private static double[] getHuMoments(BufferedImage img) {
        double[] moments=new double[8];
        Mat imagenOriginal;
        imagenOriginal = new Mat();
        Mat binario;
        binario = new Mat();
        Mat Canny;
        Canny = new Mat();
        

        imagenOriginal = ImageProcess.ocv_img2Mat(img);
//        Utils.bitmapToMat(bitmap, imagenOriginal);
        Mat gris = new Mat(imagenOriginal.width(), imagenOriginal.height(), imagenOriginal.type());
        Imgproc.cvtColor(imagenOriginal, gris, Imgproc.COLOR_RGB2GRAY);
        org.opencv.core.Size s = new Size(3, 3);
        Imgproc.GaussianBlur(gris, gris, s, 2);

        Imgproc.threshold(gris, binario, 100, 255, Imgproc.THRESH_BINARY);
        Imgproc.Canny(gris, Canny, 50, 50 * 3);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarcy = new Mat();

        Imgproc.findContours(Canny, contours, hierarcy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(Canny, contours, -1, new Scalar(Math.random() * 255, Math.random() * 255, Math.random() * 255));
        Moments p = new Moments();
        List<Moments> nu = new ArrayList<Moments>(contours.size());

        for (int i = 0; i < contours.size(); i++) {
            nu.add(i, Imgproc.moments(contours.get(i), false));
            p = nu.get(i);
        }
        double
                n20 = p.get_nu20(),
                n02 = p.get_nu02(),
                n30 = p.get_nu30(),
                n12 = p.get_nu12(),
                n21 = p.get_nu21(),
                n03 = p.get_nu03(),
                n11 = p.get_nu11();

        //First moment
        moments[0] = n20 + n02;

        //Second moment
        moments[1] = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);

        //Third moment
        moments[2] = Math.pow(n30 - (3 * (n12)), 2)
                + Math.pow((3 * n21 - n03), 2);

        //Fourth moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);

        //Fifth moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (3 * n21 - n03) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

        //Sixth moment
        moments[5] = (n20 - n02)
                * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                + 4 * n11 * (n30 + n12) * (n21 + n03);

        //Seventh moment
        moments[6] = (3 * n21 - n03) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (n30 - 3 * n12) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

        //Eighth moment
        moments[7] = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
                - (n20 - n02) * (n30 + n12) * (n03 + n21);

        return moments;        
    }
}
