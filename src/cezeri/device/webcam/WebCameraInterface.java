/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import java.awt.image.BufferedImage;

/**
 *
 * @author musa-atas
 */
public interface WebCameraInterface {
    public boolean isWebCameraStarted();
    public void sendSnapShot(BufferedImage img);
    public void sendVideoLiveFrameWebCam(BufferedImage currentImageInVideo);
}
