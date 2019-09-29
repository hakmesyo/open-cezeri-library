/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import cezeri.device.webcam.TestWebCamViewer;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BAP1
 */
public class TestCameraObserverSarxos implements WebcamListener{
    private Webcam webcam;
    
    public static void main(String[] args) {
        TestCameraObserverSarxos frm=new TestCameraObserverSarxos();
        frm.start();
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        System.out.println("Webcam is opened now");
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
        System.out.println("Webcam is closed now");
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        System.out.println("Webcam is disposed now");
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        System.out.println("received new image");
    }

    private void start() {
        webcam = Webcam.getWebcams().get(1);
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.addWebcamListener(TestCameraObserverSarxos.this);
        webcam.open();  
        BufferedImage bf=webcam.getImage();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestCameraObserverSarxos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
