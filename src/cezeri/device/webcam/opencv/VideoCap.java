/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam.opencv;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import org.opencv.videoio.Videoio;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

public class VideoCap {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    public VideoCap(int id,int width,int height){
        cap = new VideoCapture();
        cap.open(id);
//        cap.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
//        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);
//        cap.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
//        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
        cap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
        
    }

    public BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
    
    public void closeCamera(){
        cap.release();
    }
}
