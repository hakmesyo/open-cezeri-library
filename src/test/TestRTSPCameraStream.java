/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author cezerilab
 */
public class TestRTSPCameraStream {

    public static void main(String[] args) {
        System.loadLibrary("opencv_java450");
        System.loadLibrary("opencv_videoio_ffmpeg450_64");
//        VideoCapture ipcamera = new VideoCapture("rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov");
        VideoCapture ipcamera = new VideoCapture(""
//                +"https://r4---sn-u0g3uxax3-qpte.googlevideo.com/videoplayback?expire=1632957736&ei=yKBUYcSaBtjNgAfvpKvoDA&ip=78.161.63.189&id=Gzj8WhyH2Sg.2&itag=140&source=yt_live_broadcast&requiressl=yes&mh=gs&mm=44,29&mn=sn-u0g3uxax3-qpte,sn-nv47lnlz&ms=lva,rdu&mv=m&mvi=4&pl=21&initcwndbps=722500&vprv=1&live=1&hang=1&noclen=1&mime=audio/mp4&ns=_yM2fbvOqDFf_Tnixh784aMG&gir=yes&mt=1632935309&fvip=4&keepalive=yes&fexp=24001373,24007246&beids=23886215&c=WEB_EMBEDDED_PLAYER&n=tel0p0YP4QtoMA&sparams=expire,ei,ip,id,itag,source,requiressl,vprv,live,hang,noclen,mime,ns,gir&sig=AOq0QJ8wRAIgN2DDLqeF95XaZnUBQ0-pBGyualLgemxqCnmZ2mgE95oCIC-PYhAnkKRSppJqwj5QzyHHlo3moAl1GlIiKFY-r81u&lsparams=mh,mm,mn,ms,mv,mvi,pl,initcwndbps&lsig=AG3C_xAwRAIgQFCp3On2mbfos_7x8YZ2mLX1zlv6mqbQFzaPz1_Cdu8CIBblKXPIHXlFK-XtroZ-F2Mlzug-z5C896EIXCFWmkuF&alr=yes&cpn=JYZRJgB1TMunCpeZ&cver=1.20210922.1.1&sq=1899384&rn=45&rbuf=16338"
//                + "https://5a78c55e99e82.streamlock.net/setbasimeydani/smil:setbasimeydani/playlist.m3u8?wowzatokenendtime=1632937627&wowzatokenhash=jKZBYKt59o3CW0CwaW8a6XjGv7wa2p0BhZfIJ712WHqDsdNv80CfmbgaCIQ8IJ52&wowzatokenstarttime=1632935827"
                +"https://5a78c55e99e82.streamlock.net/altiparmakmeydani/smil:altiparmakmeydani/chunklist_w1745683752_b700000_tkd293emF0b2tlbmVuZHRpbWU9MTYzMzIzMDc0OCZ3b3d6YXRva2VuaGFzaD1tRTBrMk01SWJFZGt5aWVnRXNLVTZvdXRQMWdzeldDR0dfR1kzNGNieWhSazRnX3RSY3R3WlFWNFlYTjRfN3ZKJndvd3phdG9rZW5zdGFydHRpbWU9MTYzMzIyODk0OA==.m3u8"
                + "");
        
        if (ipcamera.isOpened()) {
            System.out.println("Video is captured");
        } else {
            System.out.println("");
        }
        videoCamera cam = new videoCamera(ipcamera);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cam);
        frame.setSize(800, 800);
        frame.setVisible(true);

        while (ipcamera.isOpened()) {
            cam.repaint();

        }

    }

    private static class videoCamera extends JPanel {

        VideoCapture camera;

        public videoCamera(VideoCapture cam) {

            camera = cam;

        }


        public BufferedImage Mat2BufferedImage(Mat m) {

            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage img = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return img;

        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Mat mat = new Mat();

            if (camera.read(mat)) {
                System.out.print("IMAGE");

            }

            BufferedImage image = Mat2BufferedImage(mat);
            //Mat gray = turnGray(mat);
            //MatOfRect objects = new MatOfRect();
            //CascadeClassifier cas = new CascadeClassifier();
            //cas.detectMultiScale(gray,objects);
            //Mat thresh  = threash( gray);

            //BufferedImage image = Mat2BufferedImage(thresh);
            g.drawImage(image, 10, 10, image.getWidth(), image.getHeight(), null);

        }

        public Mat turnGray(Mat img) {
            Mat mat1 = new Mat();
            Imgproc.cvtColor(img, mat1, Imgproc.COLOR_RGB2GRAY);
            return mat1;
        }

        public Mat threash(Mat img) {
            Mat threshed = new Mat();
            int SENSITIVITY_VALUE = 100;
            Imgproc.threshold(img, threshed, SENSITIVITY_VALUE, 255, Imgproc.THRESH_BINARY);
            return threshed;
        }

    }

}
