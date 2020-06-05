/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam.opencv;

import cezeri.factory.FactoryUtils;
import cezeri.image_processing.ImageProcess;
import cezeri.vision.PanelPicture;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author elcezerilab
 */
public class OpenCVCamera {

    private VideoCap webcam = null;
    private BufferedImage bf = null;
    private boolean flag_webcam_stop=false;

    public OpenCVCamera() {
        webcam = new VideoCap(0, 640, 480);
    }

    public OpenCVCamera(int camID) {
        webcam = new VideoCap(camID, 640, 480);
    }

    public OpenCVCamera(int camID, int width, int height) {
        webcam = new VideoCap(camID, width, height);
    }
    
    public void stopCamera(){
        flag_webcam_stop=true;
    }
    
    public BufferedImage getOneFrame(){
        return webcam.getOneFrame();
    }

    public BufferedImage getOneFrame(boolean isFlipped){
        if (isFlipped) {
            return ImageProcess.flipVertical(webcam.getOneFrame());
        }else{
            return webcam.getOneFrame();
        }
    }

    public void startVideoStream(PanelPicture pan, boolean isFlipped) {
        bf = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (flag_webcam_stop) {
                        bf = null;
                        pan.setImage(null);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(OpenCVCamera.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        webcam.closeCamera();
                        webcam = null;
                        break;
                    } else {
                        bf = webcam.getOneFrame();
                        if (isFlipped) {
                            bf = ImageProcess.flipVertical(bf);
                        }
                        pan.setImage(bf);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(OpenCVCamera.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OpenCVCamera.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

}
