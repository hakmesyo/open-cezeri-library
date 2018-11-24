/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.ic_camera;

import cezeri.image_processing.ImageProcess;
import cezeri.utils.FactoryUtils;
import cezeri.vision.PanelPicture;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

/**
 *
 * @author BAP1
 */
public class IC_CameraControlPanel extends javax.swing.JPanel {

    IntByReference phw = null;
    IntByReference perr = null;
    public IC_CameraDLL camDll;
    int ic_success = 0;
    public int ic_grabber = 0;
    int hw_varispec = 0;
    IntByReference p_grabber = null;
    public int isCameraLive = 0;
    public int isCameraStart = 0;
    private IC_CameraThread cam_thread = null;
    public BufferedImage img = null;
    public IC_CameraInterface camInterface;
    public List<IC_CameraInterface> listeners = new ArrayList<IC_CameraInterface>();

    /**
     * Creates new form ICPanel
     */
    public IC_CameraControlPanel(IC_CameraInterface calledFrame) {
        initComponents();
        this.camInterface = calledFrame;
        addListener(camInterface);
        searchAvailableCameras();
//        startCamera();
    }
    
//    public IC_CameraControlPanel(PanelPicture pp, IC_CameraInterface calledFrame) {
//        initComponents();
//        this.camInterface = calledFrame;
//        addListener(camInterface);
////        this.pp = pp;
//        searchAvailableCameras();
////        startCamera();
//    }

    public void addListener(IC_CameraInterface frm) {
        listeners.add(frm);
    }

    public void onNewImage(BufferedImage img) {
        // Notify everybody that may be interested.
        for (IC_CameraInterface cam : listeners) {
            cam.sendVideoLiveFrame(img,getActiveCameraIndex());
        }        
    }
    
    public void sendCameraStarted() {
        // Notify everybody that may be interested.
        for (IC_CameraInterface cam : listeners) {
            cam.isCameraStarted();
        }
    }

    public void startCamera(int index) {
        int dn = searchAvailableCameras();
        if (dn > 0 && index < dn) {
            combo_cams.setSelectedIndex(index);
            startCamera();
        }
    }
    
    public int getActiveCameraIndex(){
        return combo_cams.getSelectedIndex();
    }
    
    public String getActiveCameraName(){
        return combo_cams.getSelectedItem().toString();
    }
    
    public int getActiveColorFormatIndex(){
        return combo_format.getSelectedIndex();
    }
    
    public void setActiveColorFormatIndex(int n){
        combo_format.setSelectedIndex(n);
    }
    
    public String getActiveColorFormatName(){
        return combo_format.getSelectedItem().toString();
    }

    public void startCamera(String id) {
        int dn = searchAvailableCameras();
        if (dn > 0 && ((DefaultComboBoxModel) combo_cams.getModel()).getIndexOf("id") != -1) {
            combo_cams.setSelectedItem(id);
            startCamera();
        }
    }

    private int searchAvailableCameras() {
        camDll = IC_CameraDLL.INSTANCE;
        ic_success = camDll.IC_InitLibrary("0");
        ic_grabber = camDll.IC_CreateGrabber();
        int dev_count = camDll.IC_GetDeviceCount();
//        System.out.println("device count:" + dev_count);
        if (dev_count == 0) {
            FactoryUtils.showMessage("IC Camera was not detected.");
            isCameraStart = 0;
            isCameraLive = 0;
            return 0;
        } else {
            String[] cam_list = getCameraList(dev_count, camDll);
            DefaultComboBoxModel model = new DefaultComboBoxModel(cam_list);
            combo_cams.setModel(model);
        }
        return dev_count;
    }

    private void startCamera() {
//        camDll.IC_ShowDeviceSelectionDialog(ic_grabber);
        isCameraStart = 0;
        String dev_name = camDll.IC_GetDevice(combo_cams.getSelectedIndex());
        System.out.println("device name:" + dev_name);
        isCameraStart = camDll.IC_OpenVideoCaptureDevice(ic_grabber, dev_name);
//        System.out.println("isCameraStart = " + isCameraStart);
        int scc = camDll.IC_SetFormat(ic_grabber, combo_format.getSelectedIndex());
        p_grabber = new IntByReference(ic_grabber);
        float fps = camDll.IC_GetFrameRate(ic_grabber);
//        System.out.println("fps before= " + fps);
        camDll.IC_SetFrameRate(ic_grabber, 90.0f);
        fps = camDll.IC_GetFrameRate(ic_grabber);
//        System.out.println("fps after = " + fps);
    }

    private String[] getCameraList(int dev_count, IC_CameraDLL camDll) {
        String[] ret = new String[dev_count];
        for (int i = 0; i < dev_count; i++) {
            ret[i] = camDll.IC_GetDevice(i);
        }
        return ret;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        combo_cams = new javax.swing.JComboBox<>();
        btn_live = new javax.swing.JToggleButton();
        btn_snap_image = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        combo_format = new javax.swing.JComboBox<>();

        jLabel2.setText("Cameras:");

        combo_cams.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_camsItemStateChanged(evt);
            }
        });

        btn_live.setText("Start Live Video");
        btn_live.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_liveActionPerformed(evt);
            }
        });

        btn_snap_image.setText("Snap Image");
        btn_snap_image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_snap_imageActionPerformed(evt);
            }
        });

        jLabel1.setText("Format:");

        combo_format.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Y800 (Gray Scale)", "RGB24", "RGB32", "UYVY" }));
        combo_format.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_formatItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo_cams, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo_format, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_live, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_snap_image, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(combo_cams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_live)
                    .addComponent(btn_snap_image)
                    .addComponent(jLabel1)
                    .addComponent(combo_format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_snap_imageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_snap_imageActionPerformed
        snapImage();
    }//GEN-LAST:event_btn_snap_imageActionPerformed

    private void btn_liveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_liveActionPerformed
        if (btn_live.isSelected()) {
            startLiveVideo();
            btn_live.setText("Stop Live Video");
        } else {
            stopLiveCamera();
            btn_live.setText("Start Live Video");
        }
    }//GEN-LAST:event_btn_liveActionPerformed

    private void combo_formatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_formatItemStateChanged
//        stopCamera();
//        startCamera();
    }//GEN-LAST:event_combo_formatItemStateChanged

    private void combo_camsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_camsItemStateChanged
//        stopCamera();
//        startCamera();
    }//GEN-LAST:event_combo_camsItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btn_live;
    private javax.swing.JButton btn_snap_image;
    private javax.swing.JComboBox<String> combo_cams;
    private javax.swing.JComboBox<String> combo_format;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

    String id = UUID.randomUUID().toString();

    public void startLiveVideo() {
        camDll.IC_StopLive(ic_grabber);
        int isLive = camDll.IC_StartLive(ic_grabber, 0);
        if (isLive == 0) {
            FactoryUtils.showMessage("Camera can not set to Live Mode");
            isCameraLive = 0;
        } else {
            isCameraLive = 1;
//            System.out.println("isCameraLive = " + isCameraLive);
            cam_thread = new IC_CameraThread(this, id);
            cam_thread.start();
        }
    }

    public BufferedImage getBufferedImage() {
        return img;
    }

    public void stopCamera() {
        synchronized (this) {
            isCameraStart = 0;
            camDll.IC_CloseVideoCaptureDevice(ic_grabber);
        }
    }

    public int getColorFormat() {
        return combo_format.getSelectedIndex();
    }

    public void stopLiveCamera() {
        synchronized (this) {
            isCameraLive = 0;
            camDll.IC_StopLive(ic_grabber);
//            cam_thread = null;
        }
    }

    public void snapImage() {
        if (isCameraStart == 1) {
            if (btn_live.isSelected()) {
                btn_live.doClick();
            }
            int isLive = camDll.IC_StartLive(ic_grabber, 1);
            camDll.IC_SnapImage(ic_grabber, 1000);                            // Snap a frame into memory

            camDll.IC_SaveImage(ic_grabber, "temp.bmp", 0, 0);
            img = ImageProcess.readImageFromFile("temp.bmp");

//            Pointer p = camDll.IC_GetImagePtr(ic_grabber);
//            int size = 640 * 480;
//            byte[] id = p.getByteArray(0, size);
//            int[][] dizi = FactoryUtils.to2DInt(FactoryUtils.toIntArray1D(id), 480, 640);
//            img = ImageProcess.pixelsToImageGray(dizi);
//            pp.setImage(img);
            for (IC_CameraInterface cam : listeners) {
                cam.sendSnapShot(img);
            }
        }
    }

//    public void setPanel(PanelPicture pan) {
//        this.pp = pan;
//    }

}
