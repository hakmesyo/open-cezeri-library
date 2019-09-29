package cezeri.vision;

import cezeri.machine_learning.extraction.FeatureExtractionLBP;
import cezeri.gui.FrameImageHistogram;
import cezeri.gui.FrameImage;
import cezeri.gui.FrameImageAdvanced;
import cezeri.image_processing.ImageProcess;
import cezeri.types.TStatistics;
import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
import cezeri.utils.TimeWatch;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class PicturePanelAdvanced extends JPanel {

    private Point p = new Point();
    public boolean isChainProcessing = false;
    private Point mousePos = new Point(0, 0);
    private Point mousePosTopLeft = new Point(0, 0);
    private Point mousePosBottomRight = new Point(0, 0);
    private JLabel lbl = null;
    private boolean lblShow = false;
    private boolean showRegion = false;
    private BufferedImage currBufferedImage;
    private BufferedImage originalBufferedImage;
    private TimeWatch watch = TimeWatch.start();
//    private ItemHandler handler;
    private JRadioButtonMenuItem items[];
    private final JPopupMenu popupMenu = new JPopupMenu();
    private int fromLeft = 10;
    private int fromTop = 30;

    private boolean activateOriginal = false;
    private boolean activateSaveImage = false;
    private boolean activateHistogram = false;
    private boolean activateStatistics = false;
    private boolean activateRedChannel = false;
    private boolean activateRevert = false;
    private boolean activateROI = false;
    private boolean activateCloneROI = false;
    private boolean activateGreenChannel = false;
    private boolean activateBlueChannel = false;
    private boolean activateRGB = false;
    private boolean activateGray = false;
    private boolean activateEdge = false;
    private boolean activateAutoSize = false;
    private boolean activateAutoSizeAspect = false;
    private TStatistics stat;
    private DecimalFormat df = new DecimalFormat("#");
    private String imagePath;
    private String imageFolder;
    private File[] fileList;
    private int currentImageIndex = 0;
    private String fileName;
    private FrameImageAdvanced frm = null;
    private int[][] imgData;
    private FrameImageHistogram histFrame = null;
    private JList jlist = null;
    private Vector<String> listImageFile = new Vector<String>();

    public PicturePanelAdvanced() {
        initialize();
    }

    public void setImage(BufferedImage image, String imagePath) {
        currBufferedImage = image;
        originalBufferedImage = image;
        imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
        this.imagePath = imagePath;
        lbl.setText(getImageSize() + "X:Y");
        fileName = FactoryUtils.getFileName(imagePath);
        this.imageFolder = FactoryUtils.getFolderPath(this.imagePath);
        fileList = FactoryUtils.getFileListInFolder(imageFolder);
        currentImageIndex = getCurrentImageIndex();
        if (activateStatistics) {
            currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
            imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
            stat = TStatistics.getStatistics(currBufferedImage);
        }
        repaint();
    }
    
    public void setImage(BufferedImage image) {
        currBufferedImage = image;
        originalBufferedImage = image;
        imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
        this.imagePath = imagePath;
        lbl.setText(getImageSize() + "X:Y");
        fileName = FactoryUtils.getFileName(imagePath);
//        this.imageFolder = FactoryUtils.getFolderPath(this.imagePath);
//        fileList = FactoryUtils.getFileListInFolder(imageFolder);
        currentImageIndex = getCurrentImageIndex();
        if (activateStatistics) {
            currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
            imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
            stat = TStatistics.getStatistics(currBufferedImage);
        }
        repaint();
    }

    public JList getJlist() {
        return jlist;
    }

    public void setJlist(JList jlist) {
        this.jlist = jlist;
    }

    public void setImage(BufferedImage image, String imagePath, FrameImageAdvanced frm) {
        setImage(image, imagePath);
        this.frm = frm;
    }

    public void setImage(BufferedImage image, FrameImageAdvanced frm) {
        setImage(image);
        this.frm = frm;
    }

    public void setFrame(FrameImageAdvanced frm) {
        this.frm = frm;
    }

    public BufferedImage getImage() {
        return currBufferedImage;
    }

    public TStatistics getStatistics() {
        stat = TStatistics.getStatistics(currBufferedImage);
        return stat;
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
            fromLeft = (wPanel - wImg) / 2;
            fromTop = (hPanel - hImg) / 2;

////            int px = (wPanel - wImg) / 2;
////            int py = (hPanel - hImg) / 2;
//            int px = 10;
//            int py = 30;
            if (activateAutoSize) {
                currBufferedImage = ImageProcess.resize(originalBufferedImage, this.getWidth(), this.getHeight());
            }
            if (activateAutoSizeAspect) {
                currBufferedImage = ImageProcess.resizeAspectRatio(originalBufferedImage, this.getWidth(), this.getHeight());
            }
            gr.drawImage(currBufferedImage, fromLeft, fromTop, this);

            //draw previous and next buttons for tracing images in current folder
            int tx = (this.getWidth() - 150) / 2;
            int ty = this.getHeight() - 30;
            gr.setColor(Color.red);
            gr.drawRect(tx, ty, 75, 20);
            gr.drawString("PREV Image", tx + 5, ty + 15);
            gr.setColor(Color.green);
            gr.drawRect(tx + 78, ty, 75, 20);
            gr.drawString("NEXT Image", tx + 83, ty + 15);
//            gr.drawString(imagePath, 10, 35);

            if (activateHistogram && histFrame != null) {
//                imgData = ImageProcess.imageToPixels255(originalBufferedImage);
//                currBufferedImage = ImageProcess.pixelsToBufferedImage255(imgData);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                histFrame.setHistogramData(CMatrix.getInstance(imgData));
                histFrame.setVisible(true);
            }
            if (activateStatistics && stat != null) {
                int sW = 150;
                int sH = 200;
                int sPX = this.getWidth() - sW - 5;
                int sPY = 5;
                int dh = 24;

                gr.setColor(Color.black);
                gr.fillRect(sPX, sPY, sW, sH);
                gr.setColor(Color.GREEN);
                gr.drawRect(sPX, sPY, sW, sH);
                sPX += 20;
                gr.drawString("Mean = " + (stat.mean), sPX, sPY += dh);
                gr.drawString("Std Dev = " + (stat.std), sPX, sPY += dh);
                gr.drawString("Entropy = " + (stat.entropy), sPX, sPY += dh);
                gr.drawString("Contrast = " + (stat.contrast), sPX, sPY += dh);
                gr.drawString("Kurtosis = " + (stat.kurtosis), sPX, sPY += dh);
                gr.drawString("Skewness = " + (stat.skewness), sPX, sPY += dh);
                gr.drawString("Ad.Exp.Score = " + (stat.adaptiveExposureScore), sPX, sPY += dh);
            }
            if (!activateROI) {
                if (lblShow && mousePos.x > fromLeft && mousePos.y > fromTop && mousePos.x < fromLeft + wImg && mousePos.y < fromTop + hImg) {
                    p.x = mousePos.x - fromLeft;
                    p.y = mousePos.y - fromTop;
//                    int rgb = currBufferedImage.getRGB(p.x, p.y);
//                    int r = (rgb >> 16) & 0xFF;
//                    int g = (rgb >> 8) & 0xFF;
//                    int b = (rgb >> 0) & 0xFF;
//                    int gray = (int) (0.33000000000000002D * (double) r + 0.56000000000000005D * (double) g + 0.11D * (double) b);
//                imgMatrix=ImageProcess.imageToPixels255(currBufferedImage);
//                int gray=imgMatrix[p.x][p.y];
                    lbl.setText(getImageSize() + " Pos=(" + p.x + ":" + p.y + ") Gray Level=" + imgData[p.x][p.y] + "");// + " RGB=" + "(" + r + "," + g + "," + b + ")");
                    gr.setColor(Color.blue);
                    gr.drawLine(0, mousePos.y, wPanel - 1, mousePos.y);
                    gr.drawLine(mousePos.x, 0, mousePos.x, hPanel - 1);
                    gr.setColor(Color.red);
                    gr.drawRect(mousePos.x - 1, mousePos.y - 1, 2, 2);
                    gr.drawRect(mousePos.x - 10, mousePos.y - 10, 20, 20);
                }
            } else {
                if (showRegion) {
                    gr.setColor(Color.blue);
                    int w = Math.abs(mousePos.x - mousePosTopLeft.x);
                    int h = Math.abs(mousePos.y - mousePosTopLeft.y);
                    gr.drawRect(mousePosTopLeft.x, mousePosTopLeft.y, w, h);
                    gr.setColor(Color.red);
                    int wx = 5;
                    gr.drawRect(mousePosTopLeft.x - 2, mousePosTopLeft.y - 2, wx, wx);
                    gr.drawRect(mousePosTopLeft.x - 2 + w, mousePosTopLeft.y - 2, wx, wx);
                    gr.drawRect(mousePosTopLeft.x - 2 + w, mousePosTopLeft.y - 2 + h, wx, wx);
                    gr.drawRect(mousePosTopLeft.x - 2, mousePosTopLeft.y - 2 + h, wx, wx);
                }
            }
            this.paintComponents(gr);
        }
        gr.setColor(Color.red);
        gr.drawRect(0, 0, wPanel - 1, hPanel - 1);
        gr.drawRect(1, 1, wPanel - 3, hPanel - 3);

    }

    private void initialize() {
        ItemHandler handler = new ItemHandler();
        String[] elements = {"Clone", "Load Image", "Save Image", "Revert", "ROI", "Clone ROI", "AutoSize", "AutoSizeAspect", "Original", "Statistics", "Histogram", "Red", "Green", "Blue", "Gray", "RGB", "Edge", "Smooth", "Sharpen"};
        ButtonGroup itemsGroup = new ButtonGroup();
        items = new JRadioButtonMenuItem[elements.length];

        // construct each menu item and add to popup menu; also  
        // enable event handling for each menu item  
        for (int i = 0; i < elements.length; i++) {
            items[i] = new JRadioButtonMenuItem(elements[i]);
            popupMenu.add(items[i]);
            itemsGroup.add(items[i]);
            items[i].addActionListener(handler);
        }

        lbl = new JLabel("X:Y");
        this.add(lbl);
        lbl.setBounds(new Rectangle(10, 0, 500, 30));
        lbl.setBackground(Color.yellow);
        lbl.setForeground(Color.GREEN);
        lbl.setVisible(true);
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        this.updateUI();

        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent e) {

                if (activateROI && e.getButton() == MouseEvent.BUTTON1) {
                    showRegion = true;
                    mousePosTopLeft = e.getPoint();
                } else {
                    lblShow = true;
                }
                checkPrevButton(e);
                checkNextButton(e);
                checkForTriggerEvent(e);
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (activateROI && e.getButton() == MouseEvent.BUTTON1) {
                    mousePos = e.getPoint();
                    mousePosBottomRight = e.getPoint();
                } else {
                    lblShow = false;
                }
                checkForTriggerEvent(e);
            }

            private void checkForTriggerEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
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

    private void checkPrevButton(MouseEvent e) {
        int tx = (this.getWidth() - 150) / 2;
        int ty = this.getHeight() - 30;
        if (e.getPoint().x > tx && e.getPoint().x < tx + 75 && e.getPoint().y > ty && e.getPoint().y < ty + 30) {
            if (currentImageIndex == 0) {
                currentImageIndex = fileList.length;
            }
            currentImageIndex = (currentImageIndex - 1) % fileList.length;
            currBufferedImage = ImageProcess.readImageFromFile(fileList[currentImageIndex]);
            originalBufferedImage = currBufferedImage;
            fileName = fileList[currentImageIndex].getName();
            imagePath = fileList[currentImageIndex].getAbsolutePath();
            if (frm != null) {
                frm.setTitle(imagePath);
            }
            if (jlist != null) {
                jlist.setSelectedIndex(currentImageIndex);
                jlist.ensureIndexIsVisible(jlist.getSelectedIndex());
            }
            if (activateStatistics) {
                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                stat = TStatistics.getStatistics(currBufferedImage);
            }
            repaint();
        }
    }

    private void checkNextButton(MouseEvent e) {
        int tx = (this.getWidth() - 150) / 2 + 77;
        int ty = this.getHeight() - 30;
        if (e.getPoint().x > tx && e.getPoint().x < tx + 75 && e.getPoint().y > ty && e.getPoint().y < ty + 30) {
            currentImageIndex = (currentImageIndex + 1) % fileList.length;
            currBufferedImage = ImageProcess.readImageFromFile(fileList[currentImageIndex]);
            originalBufferedImage = currBufferedImage;
            fileName = fileList[currentImageIndex].getName();
            imagePath = fileList[currentImageIndex].getAbsolutePath();
            if (frm != null) {
                frm.setTitle(imagePath);
            }
            if (jlist != null) {
                jlist.setSelectedIndex(currentImageIndex);
                jlist.ensureIndexIsVisible(jlist.getSelectedIndex());
            }
            if (activateStatistics) {
                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                stat = TStatistics.getStatistics(currBufferedImage);
            }
            repaint();
        }
    }

    private String getImageSize() {
        String str = "Res=[" + originalBufferedImage.getHeight() + "," + originalBufferedImage.getWidth() + "] ";
        return str;
    }

    public void saveImage() {
        ImageProcess.imwrite(currBufferedImage);
    }

    public void saveImage(String fileName) {
        ImageProcess.imwrite(currBufferedImage, fileName);
    }

    private int getCurrentImageIndex() {
        int ret = 0;
        if (fileList == null) {
            return ret;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].getName().equals(fileName)) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    private class ItemHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // determine which menu item was selected  
            setDefaultValues();
            JRadioButtonMenuItem obj = (JRadioButtonMenuItem) e.getSource();
            if (obj.getText().equals("Clone")) {
                new FrameImage(currBufferedImage, obj.getText()).setVisible(true);
                return;
            }
            if (obj.getText().equals("Load Image")) {
                setDefaultValues();
                activateStatistics = false;
                File fl = ImageProcess.readImageFileFromFolderWithDirectoryPath(imagePath);
                if (fl == null) {
                    return;
                }
                BufferedImage bf = ImageProcess.readImageFromFile(fl.getAbsolutePath());
                if (bf != null) {
                    originalBufferedImage = bf;
                    currBufferedImage = (originalBufferedImage);
                    imagePath = fl.getAbsolutePath();
                    imageFolder = FactoryUtils.getFolderPath(imagePath);
                    fileName = fl.getName();
                    fileList = FactoryUtils.getFileListInFolder(imageFolder);
                    currentImageIndex = getCurrentImageIndex();
                    imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                    if (activateStatistics) {
                        stat = TStatistics.getStatistics(currBufferedImage);
                    }
                } else {
                    return;
                }
                if (frm != null) {
                    listImageFile.clear();
                    for (File f : fileList) {
                        listImageFile.add(f.getName());
                        //System.out.println(f.getName());
                    }
                    if (jlist != null) {
                        jlist.setListData(listImageFile);
                        jlist.setSelectedIndex(0);
                        jlist.ensureIndexIsVisible(jlist.getSelectedIndex());
                    }
                    frm.getFolderPath().setText(imageFolder);
                }
                repaint();
                return;
            }
            if (obj.getText().equals("Save Image")) {
                setDefaultValues();
                ImageProcess.imwrite(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("ROI")) {
                setDefaultValues();
                activateROI = true;
                repaint();
                return;
            }
            if (obj.getText().equals("Clone ROI")) {
                setDefaultValues();
                showRegion = false;
                activateCloneROI = true;
                mousePosTopLeft.x -= fromLeft;
                mousePosTopLeft.y -= fromTop;
                mousePosBottomRight.x -= fromLeft;
                mousePosBottomRight.y -= fromTop;
                CMatrix cm = CMatrix.getInstance(currBufferedImage).subMatrix(mousePosTopLeft, mousePosBottomRight);
                BufferedImage bf = ImageProcess.pixelsToImageGray(cm.toIntArray2D());
                new FrameImage(bf, obj.getText()).setVisible(true);
                repaint();
                return;
            }
            if (obj.getText().equals("Original")) {
                setDefaultValues();
                activateOriginal = true;
                activateStatistics = false;
                currBufferedImage = (originalBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Histogram")) {
                setDefaultValues();
                activateHistogram = true;
                imgData = ImageProcess.imageToPixelsInt(originalBufferedImage);
                currBufferedImage = ImageProcess.pixelsToImageGray(imgData);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                if (histFrame == null) {
                    histFrame = new FrameImageHistogram(CMatrix.getInstance(imgData));
                } else {
                    histFrame.setHistogramData(CMatrix.getInstance(imgData));
                }
                histFrame.setVisible(true);
                repaint();
                return;
            }
            if (obj.getText().equals("Statistics")) {
                setDefaultValues();
                activateStatistics = true;
                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                stat = TStatistics.getStatistics(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Red")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "red");
                repaint();
                return;
            }
            if (obj.getText().equals("Green")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "green");
                repaint();
                return;
            }
            if (obj.getText().equals("Blue")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "blue");
                repaint();
                return;
            }
            if (obj.getText().equals("RGB")) {
                setDefaultValues();
                activateRGB = true;
                currBufferedImage = (originalBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Gray")) {
                setDefaultValues();
                activateGray = true;
//                currBufferedImage = ImageProcess.pixelsToBufferedImage255(ImageProcess.imageToPixels255(originalBufferedImage));
                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
                imgData = ImageProcess.imageToPixelsInt(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Edge")) {
                setDefaultValues();
                activateEdge = true;
                currBufferedImage = ImageProcess.pixelsToImageGray(ImageProcess.imageToPixelsInt(originalBufferedImage));
                currBufferedImage = ImageProcess.edgeDetectionCanny(currBufferedImage, 0.5f, 1.0f, 3.0f, 3, false);
                repaint();
                return;
            }
            if (obj.getText().equals("AutoSize")) {
                setDefaultValues();
                activateAutoSize = true;
                repaint();
                return;
            }
            if (obj.getText().equals("AutoSizeAspect")) {
                setDefaultValues();
                activateAutoSizeAspect = true;
                repaint();
                return;
            }
            if (obj.getText().equals("ROI")) {
                setDefaultValues();
                activateEdge = true;
                currBufferedImage = ImageProcess.pixelsToImageGray(ImageProcess.imageToPixelsInt(originalBufferedImage));
                currBufferedImage = ImageProcess.edgeDetectionCanny(currBufferedImage, 0.5f, 1.0f, 3.0f, 3, false);
                repaint();
                return;
            }
            if (obj.getText().equals("Revert")) {
                setDefaultValues();
                activateRevert = true;
                currBufferedImage = ImageProcess.revert(currBufferedImage);
                repaint();
                return;
            }
//            if (obj.getText().equals("LBP")) {
//                setDefaultValues();
//                activateLBP = true;
//                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
//                int[] lbp = LBP.getLBP(currBufferedImage);
//                FrameImageHistogram frm = new FrameImageHistogram(CMatrix.getInstance(lbp));
//                frm.setVisible(true);
//                repaint();
//                return;
//            }
        }

    }

    public void setActivateStatistics(boolean activateStatistics) {
        this.activateStatistics = activateStatistics;
//        currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
//        imgData = ImageProcess.imageToPixels255(currBufferedImage);
        //stat = TStatistics.getStatistics(currBufferedImage);
        //repaint();
    }

    private void setDefaultValues() {
        activateROI = false;
        activateSaveImage = false;
        activateRevert = false;
        activateCloneROI = false;
        activateOriginal = false;
        activateHistogram = false;
//        activateStatistics = false;
        activateRedChannel = false;
        activateGreenChannel = false;
        activateBlueChannel = false;
        activateRGB = false;
        activateGray = false;
        activateEdge = false;
        activateAutoSize = false;
        activateAutoSizeAspect = false;
    }

}
