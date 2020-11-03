/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import cezeri.gui.FrameImage;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author cezerilab
 */
public class FactoryWebCam {

    private static FactoryWebCam factWebCam = new FactoryWebCam();
    private static Webcam webCam;
    private FrameImage frm = new FrameImage();
    private boolean isMotionDetectionImage = false;
    private boolean isMotionDetectionVideo = false;
    private boolean isVideoRecord = false;
    private boolean isImageRecord = false;
    private static String folderPath = "recorded";
    private static Dimension size;
    private static boolean isFlipped=false;

    private FactoryWebCam() {
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }

    public static FactoryWebCam openWebCam() {
        webCam = Webcam.getDefault();
        size = WebcamResolution.VGA.getSize();
        webCam.setViewSize(size);
        webCam.open(true);
        return factWebCam;
    }

    public static FactoryWebCam openWebCam(int cameraIndex) {
        webCam = Webcam.getWebcams().get(cameraIndex);
        size = WebcamResolution.VGA.getSize();
        webCam.setViewSize(size);
        webCam.open(true);
        return factWebCam;
    }

    public static FactoryWebCam openWebCam(Dimension size) {
        webCam = Webcam.getDefault();
        size = WebcamResolution.VGA.getSize();
        webCam.setViewSize(size);
        webCam.open(true);
        return factWebCam;
    }

    public static FactoryWebCam openWebCam(int cameraIndex, Dimension size) {
        webCam = Webcam.getWebcams().get(cameraIndex);
        size = WebcamResolution.VGA.getSize();
        webCam.setViewSize(size);
        webCam.open(true);
        return factWebCam;
    }

    public FactoryWebCam startWithGUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                operates();
            }

        }).start();
        return factWebCam;
    }

    private void operates() {
        BufferedImage bf = webCam.getImage();
        BufferedImage prev_bf = webCam.getImage();
        double[][] bf_m = ImageProcess.bufferedImageToArray2D(bf);
        double[][] bf_prev_m = ImageProcess.bufferedImageToArray2D(prev_bf);
        double[][] diff = FactoryMatrix.clone(bf_m);
        int k = 0;
        String timeStamp = "temp";
        BufferedImage bf_rgb = null;
        long t_start = FactoryUtils.tic();
        long elapsed_time = 0;
        boolean isRecord = false;
        FactoryUtils.makeDirectory(folderPath);

        //for video
        File file = null;
        IMediaWriter writer = null;
        long start = System.currentTimeMillis();

        //warming up
        for (int i = 0; i < 1; i++) {
            bf = webCam.getImage();
            frm.setImage(bf);
            bf = ImageProcess.filterGaussian(bf, 5);
            bf = ImageProcess.toGrayLevel(bf);

            bf_m = ImageProcess.bufferedImageToArray2D(bf);
            double diffRatio = calculateDifferentPixels(bf_m, bf_prev_m);
            bf_prev_m = ImageProcess.bufferedImageToArray2D(bf);
        }

        while (true) {
            bf = webCam.getImage();
            if (isFlipped) {
                bf=ImageProcess.flipVertical(bf);
            }
            frm.setImage(bf);
            bf_rgb = ImageProcess.clone(bf);
            bf = ImageProcess.filterGaussian(bf, 5);
            bf = ImageProcess.toGrayLevel(bf);
            bf_m = ImageProcess.bufferedImageToArray2D(bf);
            double diffRatio = calculateDifferentPixels(bf_m, bf_prev_m);
            bf_prev_m = ImageProcess.bufferedImageToArray2D(bf);
            //System.out.println("diffRatio = " + diffRatio);
            if (isMotionDetectionVideo) {
                if (diffRatio != 1 && diffRatio > 0.15) {
                    if (!isRecord) {
                        k = 0;
                        timeStamp = FactoryUtils.getDateTime();
                        FactoryUtils.makeDirectory(folderPath + "/" + timeStamp);
                        file = new File(folderPath + "/" + timeStamp + "/video_captured.ts");
                        writer = ToolFactory.makeWriter(file.getAbsolutePath());
                        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
                        isRecord = true;
                        t_start = System.currentTimeMillis();
                        System.out.println(folderPath + "/" + timeStamp + "/video_captured.ts"+" videosu kaydediliyor");
                    }
                }
                elapsed_time = System.currentTimeMillis() - t_start;
                //System.out.println("<<<<<<<<<<<<<<<<<<<<<   **********************   elapsed_time = " + elapsed_time);

                if (isRecord && elapsed_time >= 5000) {
                    System.out.println("kayıt işlemi bitti");
                    writer.close();
                    t_start = System.currentTimeMillis();
                    isRecord = false;
                }
                if (isRecord && elapsed_time < 5000) {
                    BufferedImage image = ConverterFactory.convertToType(bf_rgb, BufferedImage.TYPE_3BYTE_BGR);
                    IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
                    IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
                    frame.setKeyFrame(k == 0);
                    frame.setQuality(0);
                    writer.encodeVideo(0, frame);
                }
                k++;

            }
            if (isMotionDetectionImage) {
                if (diffRatio != 1 && diffRatio > 0.15 && diffRatio < 0.5) {
                    if (!isRecord) {
                        k = 0;
                        timeStamp = FactoryUtils.getDateTime();
                        FactoryUtils.makeDirectory(folderPath + "/" + timeStamp);
                        isRecord = true;
                        t_start = System.currentTimeMillis();
                    }
                }
                elapsed_time = System.currentTimeMillis() - t_start;
                //System.out.println("<<<<<<<<<<<<<<<<<<<<<   **********************   elapsed_time = " + elapsed_time);

                if (isRecord && elapsed_time < 5000) {
                    System.out.println(k + ".resim kaydedildi");
                    ImageProcess.saveImageAsJPEG(folderPath + "/" + timeStamp, bf_rgb, ++k);
                } else {
                    isRecord = false;
                }

            }
        }
    }

    private static double calculateDifferentPixels(double[][] bf_m, double[][] bf_prev_m) {
        double diffRatio = 0;
        int nr = bf_m.length;
        int nc = bf_m[0].length;
        int cnt = 0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (Math.abs((bf_m[i][j] - bf_prev_m[i][j])) >= 3) {
                    cnt++;
                }
            }
        }
        //System.out.println("cnt = " + cnt);
        diffRatio = 1.0 * cnt / (nr * nc);
        //System.out.println("diffRatio = " + diffRatio);
        return diffRatio;
    }

    public BufferedImage getImage() {
        return webCam.getImage();
    }
    
    public FactoryWebCam flipImageAlongVerticalAxis(){
        isFlipped=!isFlipped;
        return factWebCam;
    }

    public FactoryWebCam startMotionDetectionImage() {
        isMotionDetectionImage = true;
        isMotionDetectionVideo = false;
        return factWebCam;
    }

    public FactoryWebCam startMotionDetectionImage(String folderPath) {
        FactoryWebCam.folderPath = folderPath;
        isMotionDetectionImage = true;
        isMotionDetectionVideo = false;
        return factWebCam;
    }

    public FactoryWebCam stopMotionDetectionImage() {
        isMotionDetectionImage = false;
        return factWebCam;
    }

    public FactoryWebCam startMotionDetectionVideo(String folderPath) {
        FactoryWebCam.folderPath = folderPath;
        isMotionDetectionVideo = true;
        isMotionDetectionImage = false;
        return factWebCam;
    }

    public FactoryWebCam stopMotionDetectionVideo() {
        isMotionDetectionVideo = false;
        return factWebCam;
    }
}
