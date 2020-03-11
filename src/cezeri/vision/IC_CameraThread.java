/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.vision;

import cezeri.device.ic_camera.IC_CameraControlPanel;
import cezeri.device.ic_camera.IC_CameraDLL;
import cezeri.image_processing.ImageProcess;
import cezeri.factory.FactoryUtils;
import com.sun.jna.Pointer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author BAP1
 */
public class IC_CameraThread extends Thread {

    private IC_CameraControlPanel cpanel = null;
    private IC_CameraDLL camDll;
    private int ic_grabber = 0;
    private long t1 = 0;
    private String name = "";
    private int color_format = 0;
    private int size = 640 * 480;
    private byte[] byte_array = null;
    private int[][] dizi = null;

    public IC_CameraThread(IC_CameraControlPanel frm, String id) {
        this.cpanel = frm;
        this.camDll = frm.camDll;
        this.ic_grabber = frm.ic_grabber;
        t1 = FactoryUtils.tic();
        color_format = frm.getColorFormat();
        if (color_format == 0) {
            size = 640 * 480;
        } else if (color_format == 1) {
            size = 3 * 640 * 480;
        } else if (color_format == 2) {
            size = 4 * 640 * 480;
        } else {
            size = 640 * 480;
        }
        name = "Test_" + id + ".jpg";
    }

    public void run() {
        for (;;) {
            if (cpanel.isCameraStart == 0 || cpanel.isCameraLive == 0) {
                break;
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(IC_CameraThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            int success = camDll.IC_SnapImage(ic_grabber, 1000);                            // Snap a frame into memory
//            System.out.println("success snap:" + success);
            if (success == 1) {
                t1 = FactoryUtils.tic();
//                t1 = FactoryUtils.toc("IC_SnapImage cost:",t1);
                Pointer p = camDll.IC_GetImagePtr(ic_grabber);
//                t1 = FactoryUtils.toc("IC_GetImagePtr cost:",t1);
                camDll.IC_SaveImage(ic_grabber, name, 1, 80);
                cpanel.img = ImageProcess.readImageFromFile(name);

//            byte_array = p.getByteArray(0, size);
//            if (color_format == 0 || color_format==3) {
//                dizi = FactoryUtils.to2DInt(FactoryUtils.toIntArray1D(byte_array), 480, 640);
//                frm.img = ImageProcess.pixelsToImageGray(dizi);
//            } else if (color_format == 1) {
//                frm.img=dispatchChannelRGB(byte_array);
//            } else if (color_format == 2) {
//                frm.img=dispatchChannelRGBA(byte_array);
//            }
//            frm.pp.setImage(frm.img);
                cpanel.onNewImage(cpanel.img);
                t1 = FactoryUtils.toc("camera thread cost:", t1);

            }
//            System.out.println("fps:" + FactoryUtils.fps(t1));
        }
        System.out.println("Thread is disposed");
    }

    private BufferedImage getImageFromByteArray(byte[] d) {

        InputStream bais = new ByteArrayInputStream(d);
        BufferedImage ret = null;
        try {
            ret = ImageIO.read(bais);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }

    private BufferedImage dispatchChannelRGB(byte[] d) {
        int[][][] ret = new int[480][640][4];
        int nr = ret.length;
        int nc = ret[0].length;
        int k = 0;
        int[] dd = FactoryUtils.toIntArray1D(d);
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j][0] = dd[k++];
                ret[i][j][1] = dd[k++];
                ret[i][j][2] = dd[k++];
                ret[i][j][3] = 0;
            }
        }
        BufferedImage img = ImageProcess.pixelsToImageColor(ret);
        return img;
    }

    private BufferedImage dispatchChannelRGBA(byte[] d) {
        int[][][] ret = new int[480][640][4];
        int nr = ret.length;
        int nc = ret[0].length;
        int k = 0;
        int[] dd = FactoryUtils.toIntArray1D(d);
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j][0] = dd[k++];
                ret[i][j][1] = dd[k++];
                ret[i][j][2] = dd[k++];
                ret[i][j][3] = dd[k++];
            }
        }
        BufferedImage img = ImageProcess.pixelsToImageColor(ret);
        return img;
    }
}
