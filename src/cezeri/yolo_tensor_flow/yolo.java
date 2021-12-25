package cezeri.yolo_tensor_flow;

import cezeri.factory.FactoryUtils;
import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.utils.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

//import com.streambase.com.gs.collections.impl.Counter;
import java.util.ArrayList;
import java.util.List;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class yolo {

    private static List<String> getOutputNames(Net net) {
        List<String> names = new ArrayList<>();

        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layersNames = net.getLayerNames();

        outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));//unfold and create R-CNN layers from the loaded YOLO model//
        return names;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("path:" + FactoryUtils.getDefaultDirectory());
//        System.load(FactoryUtils.getDefaultDirectory() + "\\opencv_java420.dll"); // Load the openCV 4.2 dll //
//        System.load(FactoryUtils.getDefaultDirectory() + "\\opencv_java450.dll"); // Load the openCV 4.5 dll //
        System.load(FactoryUtils.getDefaultDirectory() + "\\opencv_java453.dll"); // Load the openCV 4.5 dll //

//        String modelWeights = "c:\\yolov3-tiny.weights"; //Download and load only wights for YOLO , this is obtained from official YOLO site//
//        String modelConfiguration = "cfg/yolov3_tiny.cfg";//Download and load cfg file for YOLO , can be obtained from official site//

        String modelWeights = "c:/yolov3.weights"; //Download and load only wights for YOLO , this is obtained from official YOLO site//
        String modelConfiguration = "cfg/yolov3.cfg";//Download and load cfg file for YOLO , can be obtained from official site//

//        String modelWeights = "c:\\yolov4.weights"; //Download and load only wights for YOLO , this is obtained from official YOLO site//
//        String modelConfiguration = "cfg/yolov4.cfg";//Download and load cfg file for YOLO , can be obtained from official site//

        String filePath = "videos/cars_small.mp4"; //My video  file to be analysed//
//        String filePath = "videos/cars.mp4"; //My video  file to be analysed//
        VideoCapture cap = new VideoCapture(filePath);// Load video using the videocapture method//
        Mat frame = new Mat(); // define a matrix to extract and store pixel info from video//
        Mat dst = new Mat();
        //cap.read(frame);
        JFrame jframe = new JFrame("Video"); // the lines below create a frame to display the resultant video with object detection and localization//
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(600, 600);
        jframe.setVisible(true);// we instantiate the frame here//

        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights); //OpenCV DNN supports models trained from various frameworks like Caffe and TensorFlow. It also supports various networks architectures based on YOLO//
        //Thread.sleep(5000);

        //Mat image = Imgcodecs.imread("D:\\yolo-object-detection\\yolo-object-detection\\images\\soccer.jpg");
        Size sz = new Size(288, 288);

        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = getOutputNames(net);
        long t1 = FactoryUtils.tic();
        long t2 = FactoryUtils.tic();
        while (true) {
            t2=FactoryUtils.toc("Overall Cost:",t2);
            if (cap.read(frame)) {
                t1 = FactoryUtils.toc("read frame cost:", t1);
                Mat blob = Dnn.blobFromImage(frame, 0.00392, sz, new Scalar(0), true, false); // We feed one frame of video into the network at a time, we have to convert the image to a blob. A blob is a pre-processed image that serves as the input.//
                net.setInput(blob);
                t1 = FactoryUtils.toc("blob cost:", t1);

                net.forward(result, outBlobNames); //Feed forward the model to get output //
                t1 = FactoryUtils.toc("dnn cost:", t1);

//                outBlobNames.forEach(System.out::println);
//                result.forEach(System.out::println);
//                float confThreshold = 0.6f; //Insert thresholding beyond which the model will detect objects//
                float confThreshold = 0.6f; //Insert thresholding beyond which the model will detect objects//
                List<Integer> clsIds = new ArrayList<>();
                List<Float> confs = new ArrayList<>();
                List<Rect2d> rects = new ArrayList<>();
                for (int i = 0; i < result.size(); ++i) {
                    // each row is a candidate detection, the 1st 4 numbers are
                    // [center_x, center_y, width, height], followed by (N-4) class probabilities
                    Mat level = result.get(i);
                    for (int j = 0; j < level.rows(); ++j) {
                        Mat row = level.row(j);
                        Mat scores = row.colRange(5, level.cols());
                        Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                        float confidence = (float) mm.maxVal;
                        //System.out.println("confidence = " + confidence);
                        Point classIdPoint = mm.maxLoc;
                        if (confidence > confThreshold) {
                            int centerX = (int) (row.get(0, 0)[0] * frame.cols()); //scaling for drawing the bounding boxes//
                            int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                            int width = (int) (row.get(0, 2)[0] * frame.cols());
                            int height = (int) (row.get(0, 3)[0] * frame.rows());
                            int left = centerX - width / 2;
                            int top = centerY - height / 2;

                            clsIds.add((int) classIdPoint.x);
                            confs.add((float) confidence);
                            rects.add(new Rect2d(left, top, width, height));
                        }
                    }
                }
                if (confs.size() > 0) {
                    float nmsThresh = 0.5f;
                    MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confs));
                    Rect2d[] boxesArray = rects.toArray(new Rect2d[0]);
                    MatOfRect2d boxes = new MatOfRect2d(boxesArray);
                    MatOfInt indices = new MatOfInt();
                    Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices); //We draw the bounding boxes for objects here//

                    int[] ind = indices.toArray();
                    int j = 0;
                    for (int i = 0; i < ind.length; ++i) {
                        int idx = ind[i];
                        Rect2d box = boxesArray[idx];
                        Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 0, 255), 2);
                    }
                    // Imgcodecs.imwrite("D://out.png", image);
                    //System.out.println("Image Loaded");
                    ImageIcon image = new ImageIcon(Mat2bufferedImage(frame)); //setting the results into a frame and initializing it //
                    vidpanel.setIcon(image);
                    vidpanel.repaint();
                    // System.out.println(j);
                    // System.out.println("Done");
                }
                t1 = FactoryUtils.toc("paint cost:", t1);
                System.out.println("");
            } else {
                System.exit(0);
            }
        }
    }

//	}
    private static BufferedImage Mat2bufferedImage(Mat image) {   // The class described here  takes in matrix and renders the video to the frame  //
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
}
