/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.yolo_tensor_flow;

import cezeri.factory.FactoryUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author BAP1
 */
public class Main implements Classifier {

    private static final int BLOCK_SIZE = 32;
    private static final int MAX_RESULTS = 50;
    private static final int NUM_CLASSES = 20;
    private static final int NUM_BOXES_PER_BLOCK = 5;
//    private static final int INPUT_SIZE = 416;
    private static final String inputName = "input";
    private static final String outputName = "output";

    // Pre-allocated buffers.
    private static int[] intValues;
    private static float[] floatValues;
    private static String[] outputNames;

    // yolo 2
    private static final double[] ANCHORS = { 1.3221, 1.73145, 3.19275, 4.00944, 5.05587, 8.09892, 9.47112, 4.84053, 11.2364, 10.0071 };

    // tiny yolo
    //private static final double[] ANCHORS = { 1.08, 1.19, 3.42, 4.41, 6.63, 11.38, 9.42, 5.11, 16.62, 10.52 };

    private static final String[] LABELS = {
            "aeroplane",
            "bicycle",
            "bird",
            "boat",
            "bottle",
            "bus",
            "car",
            "cat",
            "chair",
            "cow",
            "diningtable",
            "dog",
            "horse",
            "motorbike",
            "person",
            "pottedplant",
            "sheep",
            "sofa",
            "train",
            "tvmonitor"
    };

    private static TensorFlowInferenceInterface inferenceInterface;
    private static BufferedImage convertedImg;

    public static void main(String[] args) {

        //String modelDir = "/home/user/JavaProjects/TensorFlowJavaProject"; // Ubuntu
//        String modelAndTestImagesDir = "D:\\JavaProjects\\TensorFlowJavaProject"; // Windows
        String modelAndTestImagesDir = "D:\\Dropbox\\NetbeansProjects\\YOLO_JAVA_1"; // Windows
//        String imageFile = modelAndTestImagesDir + File.separator + "images\\test_2.jpg"; // 416x416 test image
        String imageFile = "images\\yuzler.jpg"; // 416x416 test image

        outputNames = outputName.split(",");
//        floatValues = new float[INPUT_SIZE * INPUT_SIZE * 3];

        // yolo 2 voc
        inferenceInterface = new TensorFlowInferenceInterface(Paths.get(modelAndTestImagesDir, "tiny-yolo-voc.pb"));

        // tiny yolo voc
        //inferenceInterface = new TensorFlowInferenceInterface(Paths.get(modelAndTestImagesDir, "graph-tiny-yolo-voc.pb"));

        BufferedImage img;

        try {
            img = ImageIO.read(new File(imageFile));

            convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImg.getGraphics().drawImage(img, 0, 0, null);

            intValues = ((DataBufferInt) convertedImg.getRaster().getDataBuffer()).getData() ;
            floatValues = new float[convertedImg.getWidth() * convertedImg.getHeight() * 3];

            long t1=FactoryUtils.tic();
            List<Classifier.Recognition> recognitions = recognizeImage();
            t1=FactoryUtils.toc(t1);

            System.out.println("Result length " + recognitions.size());

            Graphics2D graphics = convertedImg.createGraphics();

            for (Recognition recognition : recognitions) {
                RectF rectF = recognition.getLocation();
                System.out.println(recognition.getTitle() + " " + recognition.getConfidence() + ", " +
                        (int) rectF.left + " " + (int) rectF.top + " " + (int) rectF.width() + " " + ((int) rectF.height()));
                Stroke stroke = graphics.getStroke();
                graphics.setStroke(new BasicStroke(3));
                graphics.setColor(Color.green);
                graphics.drawRoundRect((int) rectF.left, (int) rectF.top, (int) rectF.width(), (int) rectF.height(), 5, 5);
                graphics.setStroke(stroke);
            }

            graphics.dispose();
            ImageIcon icon=new ImageIcon(convertedImg);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(convertedImg.getWidth(),convertedImg.getHeight());
            JLabel lbl=new JLabel();
            frame.setTitle("Java (Win/Ubuntu), Tensorflow & Yolo");
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List<Classifier.Recognition> recognizeImage() {

        int n1=intValues.length;
        int n2=floatValues.length;
        
        for (int i = 0; i < intValues.length; ++i) {
            floatValues[i * 3 + 0] = ((intValues[i] >> 16) & 0xFF) / 255.0f;
            floatValues[i * 3 + 1] = ((intValues[i] >> 8) & 0xFF) / 255.0f;
            floatValues[i * 3 + 2] = (intValues[i] & 0xFF) / 255.0f;
        }
        inferenceInterface.feed(inputName, floatValues, 1, convertedImg.getWidth(), convertedImg.getHeight(), 3);

        inferenceInterface.run(outputNames, false);

        final int gridWidth = convertedImg.getWidth() / BLOCK_SIZE;
        final int gridHeight = convertedImg.getHeight() / BLOCK_SIZE;

        final float[] output = new float[gridWidth * gridHeight * (NUM_CLASSES + 5) * NUM_BOXES_PER_BLOCK];

        inferenceInterface.fetch(outputNames[0], output);

        // Find the best detections.
        final PriorityQueue<Classifier.Recognition> pq =
                new PriorityQueue<>(
                        1,
                        new Comparator<Classifier.Recognition>() {
                            @Override
                            public int compare(final Classifier.Recognition lhs, final Classifier.Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });

        for (int y = 0; y < gridHeight; ++y) {
            for (int x = 0; x < gridWidth; ++x) {
                for (int b = 0; b < NUM_BOXES_PER_BLOCK; ++b) {
                    final int offset =
                            (gridWidth * (NUM_BOXES_PER_BLOCK * (NUM_CLASSES + 5))) * y
                                    + (NUM_BOXES_PER_BLOCK * (NUM_CLASSES + 5)) * x
                                    + (NUM_CLASSES + 5) * b;

                    final float xPos = (x + expit(output[offset + 0])) * BLOCK_SIZE;
                    final float yPos = (y + expit(output[offset + 1])) * BLOCK_SIZE;

                    final float w = (float) (Math.exp(output[offset + 2]) * ANCHORS[2 * b + 0]) * BLOCK_SIZE;
                    final float h = (float) (Math.exp(output[offset + 3]) * ANCHORS[2 * b + 1]) * BLOCK_SIZE;

                    final RectF rect =
                            new RectF(
                                    Math.max(0, xPos - w / 2),
                                    Math.max(0, yPos - h / 2),
                                    Math.min(convertedImg.getWidth() - 1, xPos + w / 2),
                                    Math.min(convertedImg.getHeight() - 1, yPos + h / 2));

                    final float confidence = expit(output[offset + 4]);

                    int detectedClass = -1;
                    float maxClass = 0;

                    final float[] classes = new float[NUM_CLASSES];
                    for (int c = 0; c < NUM_CLASSES; ++c) {
                        classes[c] = output[offset + 5 + c];
                    }
                    softmax(classes);

                    for (int c = 0; c < NUM_CLASSES; ++c) {
                        if (classes[c] > maxClass) {
                            detectedClass = c;
                            maxClass = classes[c];
                        }
                    }

                    final float confidenceInClass = maxClass * confidence;
                    if (confidenceInClass > 0.01) {
                        pq.add(new Classifier.Recognition(detectedClass, LABELS[detectedClass], confidenceInClass, rect));
                    }
                }
            }
        }

        final ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
        for (int i = 0; i < Math.min(pq.size(), MAX_RESULTS); ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;

    }

    private static float expit(final float x) {
        return (float) (1. / (1. + Math.exp(-x)));
    }

    private static void softmax(final float[] vals) {
        float max = Float.NEGATIVE_INFINITY;
        for (final float val : vals) {
            max = Math.max(max, val);
        }
        float sum = 0.0f;
        for (int i = 0; i < vals.length; ++i) {
            vals[i] = (float) Math.exp(vals[i] - max);
            sum += vals[i];
        }
        for (int i = 0; i < vals.length; ++i) {
            vals[i] = vals[i] / sum;
        }
    }

    public void close() {
        inferenceInterface.close();
    }
}