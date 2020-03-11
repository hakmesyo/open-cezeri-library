/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import cezeri.image_processing.ImageProcess;
import cezeri.factory.FactoryUtils;
import cezeri.vision.PanelPicture;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author BAP1
 */
public class Camera {
    private static boolean isLive=false;
    private  BufferedImage image=null;
    private Webcam webcam; 

    public List<Webcam> detectCameras() {
        List<Webcam> lst = Webcam.getWebcams();
        return lst;
    }

    public List<Webcam> getCameras() {
        List<Webcam> lst = Webcam.getWebcams();
        return lst;
    }

    public List<Webcam> getWebCams() {
        List<Webcam> lst = Webcam.getWebcams();
        return lst;
    }

    public void startLiveVideoStream(Webcam wb) {
        isLive=true;
        webcam=wb;
        webcam.open();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                return doWorkerCam(webcam);
            }
        };
        worker.execute();
    }
    
    public void stopLiveVideoStream(Webcam webcam) {
        isLive=false;
        webcam.close();
    }
    
    private Void doWorkerCam(Webcam webcam) {
        while (isLive) {
            image=webcam.getImage();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
        return null;
    }

    public BufferedImage showFPS(Webcam webcam,BufferedImage bf) {
        return ImageProcess.drawText(bf, "FPS:" + FactoryUtils.formatDouble(webcam.getFPS(),1), 10, 40, Color.yellow);
    }
    
    public BufferedImage showTrueFPS(double fps) {
        return ImageProcess.drawText(image, "True FPS:" + FactoryUtils.formatDouble(fps,1), 10, 20, Color.yellow);
    }

    public BufferedImage getImage() {
        return image;
    }

}
