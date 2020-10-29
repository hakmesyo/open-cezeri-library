/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import cezeri.factory.FactoryWebCam;

/**
 *
 * @author Science
 */
public class MotionDetection {
    public static void main(String[] args) {
        //FactoryWebCam.openWebCam(0).startWithGUI().startMotionDetectionImage("C:\\recorded_images");
        FactoryWebCam.openWebCam(0).startWithGUI().startMotionDetectionVideo("C:\\recorded_videos");        
    }
}
