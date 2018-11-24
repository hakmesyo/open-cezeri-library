/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.ic_camera;

import java.awt.image.BufferedImage;

/**
 *
 * @author musa-atas
 */
public interface IC_CameraInterface {
    public boolean isCameraStarted();
    public void sendSnapShot(BufferedImage img);
    public void sendVideoLiveFrame(BufferedImage img,String device_name);
    public void sendVideoLiveFrame(BufferedImage img,int device_index);
}
