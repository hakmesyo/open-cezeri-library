package cezeri.device.webcam;

import cezeri.matrix.CMatrix;
import cezeri.utils.TimeWatch;
import com.lti.civil.*;
import com.lti.civil.awt.AWTImageConverter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class WebCameraPanel extends JPanel implements CaptureObserver {

    private Point p = new Point();
    public boolean isChainProcessing = false;
    private Point mousePos = new Point(0, 0);
//    private Point pos1;
    private JLabel lbl = null;
    //private Point pos2;
    private boolean lblShow = false;
    CaptureStream captureStream = null;
    CaptureStream streamVideo;
    com.lti.civil.Image currImg;
    public boolean isVideoLive = false;
    private BufferedImage currBufferedImage;
    private int[][] imgMatrix;
    private WebCameraInterface camInt;
    List<WebCameraInterface> listeners = new ArrayList<>();
    private static int maxFps = 30;
    private static int currFps = 30;
    private TimeWatch watch = TimeWatch.start();
    public List<BufferedImage> imageList = new ArrayList<BufferedImage>();
    public boolean isImageStartList = false;
    CMatrix cm1 = CMatrix.getInstance();

    public void resetImageList() {
        imageList.clear();
    }

    public void addListener(WebCameraInterface frm) {
        listeners.add(frm);
    }

    public WebCameraPanel(WebCameraInterface calledFrame) {
        this.camInt = calledFrame;
        if (camInt != null) {
            addListener(camInt);
        }
        initialize();
    }

    public void setImage(BufferedImage image) {
        currBufferedImage = image;
        repaint();
    }

    public BufferedImage getImage() {
        return currBufferedImage;
    }

    public void paint(Graphics gr) {
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.setColor(Color.GREEN);
        int wPanel = this.getWidth();
        int hPanel = this.getHeight();
        if (currBufferedImage != null) {
            int wImg = currBufferedImage.getWidth();
            int hImg = currBufferedImage.getHeight();
            int px = (wPanel - wImg) / 2;
            int py = (hPanel - hImg) / 2;
            gr.drawImage(currBufferedImage, px, py, this);
            if (isVideoLive) {
                lbl.setText("Max Frame Rate=" + maxFps + " fps");
            }
            if (lblShow && mousePos.x > px && mousePos.y > py && mousePos.x < px + wImg && mousePos.y < py + hImg) {
                p.x = mousePos.x - px;
                p.y = mousePos.y - py;
//                int rgb = currBufferedImage.getRGB(p.x, p.y);
//                int r = (rgb >> 16) & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = (rgb & 0xFF);
////                int gray = (r + g + b) / 3;
                int gray = currBufferedImage.getRGB(p.x, p.y) & 0xFF;
//                imgMatrix=ImageProcess.imageToPixels255(currBufferedImage);
//                int gray=imgMatrix[p.x][p.y];
                lbl.setText("Frame Rate=" + maxFps + " (" + p.x + ":" + p.y + ") Gray Level=" + gray);
                gr.setColor(Color.blue);
                gr.drawLine(0, mousePos.y, wPanel - 1, mousePos.y);
                gr.drawLine(mousePos.x, 0, mousePos.x, hPanel - 1);
                gr.setColor(Color.red);
                gr.drawRect(mousePos.x, mousePos.y, 2, 2);
                gr.drawRect(mousePos.x - 10, mousePos.y - 10, 20, 20);

            }
            this.paintComponents(gr);
        }
        gr.setColor(Color.red);
        gr.drawRect(0, 0, wPanel - 1, hPanel - 1);
        gr.drawRect(1, 1, wPanel - 3, hPanel - 3);

    }

    private void initialize() {
        lbl = new JLabel("X:Y");
        this.add(lbl);
        lbl.setBounds(new Rectangle(10, 10, 300, 40));
        lbl.setBackground(Color.yellow);
        lbl.setForeground(Color.GREEN);
        lbl.setVisible(true);
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblShow = true;
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblShow = false;
            }
        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseMoved(java.awt.event.MouseEvent e) {
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }
        });
    }

//    protected void showToolTipText(String text, Point p) {
//        lbl.setText(text);
//        Point p1 = new Point();
//        p1.x = p.x;
//        p1.y = p.y + 5;
//        lbl.setLocation(p1);
//    }
    int k = 0;

    @Override
    public void onNewImage(CaptureStream stream, com.lti.civil.Image image) {
        cm1.tic();
        this.captureStream = stream;
        this.currImg = image;
        //System.out.println((k++)+".yeni image geldi");
        long passedTimeInMs = watch.time();
        if (passedTimeInMs == 0) {
            passedTimeInMs = 1;
        }
        maxFps = (int) (1000 / passedTimeInMs);
        if (isVideoLive) {
            takeVideoLive();
        }
        watch = TimeWatch.start();

    }

    public static int getMaxFrameRate() {
        return maxFps;
    }

    public static void setFrameRate(int fps) {
        currFps = fps;
    }

    private void takeVideoLive() {
        BufferedImage myImage = AWTImageConverter.toBufferedImage(currImg);
        currBufferedImage = myImage;
        //cm1.toc();
        if (isImageStartList) {
            imageList.add(currBufferedImage);
        }
        setImage(myImage);
        // Notify everybody that may be interested.
        for (WebCameraInterface cam : listeners) {
            cam.sendVideoLiveFrameWebCam(currBufferedImage);
        }
    }

    public BufferedImage takeSnapShot() {
        BufferedImage myImage = AWTImageConverter.toBufferedImage(currImg);
        currBufferedImage = myImage;
        setImage(currBufferedImage);
        //System.out.println("New Image Captured");
        isVideoLive = false;
        return currBufferedImage;
    }

    public BufferedImage saveSnapShot() {
        System.out.println("New Image Captured and jpg file saved");
        byte bytes[] = null;
        try {
            if (currImg == null) {
                bytes = null;
                return null;
            }
            File file = new File("images\\snapshots\\img_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
            ImageIO.write(currBufferedImage, "jpg", file);
            BufferedImage myImage = ImageIO.read(file);
            currBufferedImage = myImage;
            setImage(myImage);
            isVideoLive = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return currBufferedImage;
    }

    public BufferedImage saveSnapShot(String fullName, String fileType) {
        byte bytes[] = null;
        try {
            if (currImg == null) {
                bytes = null;
                return null;
            }
            File file = new File(fullName + "." + fileType);
            ImageIO.write(currBufferedImage, fileType, file);
            BufferedImage myImage = ImageIO.read(file);
            currBufferedImage = myImage;
            setImage(myImage);
            isVideoLive = false;
            System.out.println("Elh. " + fullName + " image was captured and saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return currBufferedImage;
    }

    @Override
    public void onError(CaptureStream stream, CaptureException ce) {
        JOptionPane.showMessageDialog(null, "Hata oldu:" + ce.getMessage().toString());
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<CaptureDeviceInfo> getCaptureDevices() {
        CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
        CaptureSystem system;
        List list = new ArrayList();
        try {
            system = factory.createCaptureSystem();
            system.init();
            list = system.getCaptureDeviceInfoList();
        } catch (CaptureException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void startCaptureStream(String targetCamera) {
        CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
        CaptureSystem system;

        try {
            system = factory.createCaptureSystem();
            system.init();
            //String targetCamera = "USB2.0 PC CAMERA"; //trying to find inca web cam usb2.0 PC camera
            //String targetCamera = "Logitech HD Webcam C270";
            List list = system.getCaptureDeviceInfoList();
            for (Object lst : list) {
                CaptureDeviceInfo info = (CaptureDeviceInfo) lst;
                if (targetCamera.equals(info.getDescription()+":"+info.getDeviceID())) {
                    System.out.println(targetCamera + " device was b. found. ");
                    System.out.println("info = " + info.getDeviceID());
                    captureStream = system.openCaptureDeviceStream(info.getDeviceID());
                    captureStream.setObserver(this);
                    break;
                }
            }
        } catch (CaptureException ex) {
            ex.printStackTrace();
        }
        try {
            captureStream.start();
            System.out.println(targetCamera+ " device is started now");
        } catch (CaptureException ex) {
            ex.printStackTrace();
        }
        
    }

    public void stopCaptureStream() {
        try {
            if (captureStream != null) {
                captureStream.stop();
            }
        } catch (CaptureException ex) {
            ex.printStackTrace();
        }
        System.out.println("B.WebCam device was closed successfully");
    }
}
