/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import org.bytedeco.javacpp.Pointer;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
//import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.video.KalmanFilter;
import org.opencv.video.SparseOpticalFlow;
import org.opencv.video.SparsePyrLKOpticalFlow;
import org.opencv.videoio.VideoCapture;
import org.opencv.ximgproc.SelectiveSearchSegmentation;
import org.opencv.xphoto.GrayworldWB;
import org.opencv.xphoto.WhiteBalancer;


/**
 *
 * @author cezerilab
 */
public class TestOpenCVSelectiveSearch {
    //static { Loader.load(); }
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Pointer p=new Pointer();
//        SelectiveSearchSegmentation s=SelectiveSearchSegmentation.__fromPtr__(p.address());
        Mat mat=ImageProcess.ocv_img2Mat(ImageProcess.readImage("images/blob.jpg"));
//        s.setBaseImage(mat);
        
        //CvMatNDArray p=new CvMatNDArray(12);
        //SelectiveSearchSegmentationStrategyFill s=new SelectiveSearchSegmentationStrategyFill();
//        CMatrix cm = CMatrix.getInstance()
////                .imread("images/blob.jpg")
////                .imread("images/dog_cat.jpg")
//                .imread("images/traffic_1.jpg")
//                .imshow()
//                //.rgb2hsv()
//                .filterGaussian(5)
//                .imshow()
//                ;
    }
}
