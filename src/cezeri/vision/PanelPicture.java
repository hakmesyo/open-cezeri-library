package cezeri.vision;

import cezeri.gui.FrameImageHistogram;
import cezeri.gui.FrameImage;
import cezeri.image_processing.ImageProcess;
import cezeri.types.TStatistics;
import cezeri.matrix_processing.CMatrix;
import cezeri.matrix_processing.CPoint;
import cezeri.matrix_processing.CRectangle;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class PanelPicture extends JPanel {

    private Point p = new Point();
    public boolean isChainProcessing = false;
    private Point mousePos = new Point(0, 0);
    private CPoint drawableMousePos = new CPoint(0, 0);
    ArrayList<CPoint> drawableRoiList = new ArrayList<>();
    private Point mousePosTopLeft = new Point(0, 0);
    private Point mousePosBottomRight = new Point(0, 0);
    private JLabel lbl = null;
    private boolean lblShow = false;
    private boolean showRegion = false;
    private boolean showDrawableRegion = false;
    private BufferedImage currBufferedImage;
    private BufferedImage originalBufferedImage;
    private BufferedImage originalBufferedImageTemp;
    private TimeWatch watch = TimeWatch.start();
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
    private boolean activateDrawableROI = false;
    private boolean activateCloneROI = false;
    private boolean activateGreenChannel = false;
    private boolean activateBlueChannel = false;
    private boolean activateRGB = false;
    private boolean activateGray = false;
    private boolean activateHSV = false;
    private boolean activateEdge = false;
    private boolean activateAutoSize = false;

    public boolean isActivateAutoSize() {
        return activateAutoSize;
    }

    public void setActivateAutoSize(boolean activateAutoSize) {
        this.activateAutoSize = activateAutoSize;
        repaint();
    }

    public boolean isActivateAutoSizeAspect() {
        return activateAutoSizeAspect;
    }

    public void setActivateAutoSizeAspect(boolean activateAutoSizeAspect) {
        this.activateAutoSizeAspect = activateAutoSizeAspect;
        repaint();
    }
    private boolean activateAutoSizeAspect = false;
    private TStatistics stat;
    private DecimalFormat df = new DecimalFormat("#");
    private String imagePath;
    private String imageFolder;
    private String fileName;
    private double[][] imgData;
    private FrameImageHistogram histFrame = null;
    private final int panWidth = 40;

    public PanelPicture() {
        initialize();
    }

    public void setImage(BufferedImage image) {
        currBufferedImage = image;
        originalBufferedImage = image;
        originalBufferedImageTemp = image;
        imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
        lbl.setText(getImageSize() + "X:Y");
        if (activateStatistics) {
            currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
            imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
            stat = TStatistics.getStatistics(currBufferedImage);
        }
        repaint();
    }

    public void setImage(BufferedImage image, double[][] data) {
        currBufferedImage = image;
        originalBufferedImage = image;
        originalBufferedImageTemp = image;
        imgData = data;
        lbl.setText(getImageSize() + "X:Y");
        if (activateStatistics) {
            currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
            imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
            stat = TStatistics.getStatistics(currBufferedImage);
        }
        repaint();
    }

    public BufferedImage getImage() {
        return currBufferedImage;
    }

    public TStatistics getStatistics() {
        stat = TStatistics.getStatistics(currBufferedImage);
        return stat;
    }

    @Override
    public void paint(Graphics gr) {
        gr.setColor(Color.BLACK);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.setColor(Color.GREEN);
        int wPanel = this.getWidth();
        int hPanel = this.getHeight();
        if (currBufferedImage != null) {
            if (activateAutoSize) {
                currBufferedImage = ImageProcess.resize(originalBufferedImageTemp, this.getWidth() - 2 * panWidth, this.getHeight() - 2 * panWidth);
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
                lbl.setText(getImageSize() + "X:Y");
//                gr.drawImage(currBufferedImage, fromLeft, fromTop, this);
            } else if (activateAutoSizeAspect) {
                currBufferedImage = ImageProcess.resizeAspectRatio(originalBufferedImageTemp, this.getWidth() - 2 * panWidth, this.getHeight() - 2 * panWidth);
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
                lbl.setText(getImageSize() + "X:Y");
//                gr.drawImage(currBufferedImage, fromLeft, fromTop, this);
            }
//            else {
//                gr.drawImage(currBufferedImage, fromLeft, fromTop, this);
//            }
            int wImg = currBufferedImage.getWidth();
            int hImg = currBufferedImage.getHeight();
            fromLeft = (wPanel - wImg) / 2;
            fromTop = (hPanel - hImg) / 2;
            gr.drawImage(currBufferedImage, fromLeft, fromTop, this);

            gr.setColor(Color.blue);
            gr.drawRect(fromLeft, fromTop, currBufferedImage.getWidth(), currBufferedImage.getHeight());

            if (activateHistogram && histFrame != null) {
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
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
                gr.setColor(Color.ORANGE);
                gr.drawString("Ideal Exposure Score", sPX, sPY += dh);
                gr.drawString("= " + stat.adaptiveExposureScore, sPX + 60, sPY += dh);
            }
            if (!activateROI && !activateDrawableROI) {
                if (lblShow && mousePos.x > fromLeft && mousePos.y > fromTop && mousePos.x < fromLeft + wImg && mousePos.y < fromTop + hImg) {
                    p.x = mousePos.x - fromLeft;
                    p.y = mousePos.y - fromTop;
                    if (currBufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                        lbl.setText(getImageSize() + " Pos=(" + p.y + ":" + p.x + ") Value=" + imgData[p.y][p.x] + "");
                    } else if (currBufferedImage.getType() == BufferedImage.TYPE_INT_RGB) {
                        String s = "" + new Color((int) imgData[p.y][p.x], true);
                        s = s.replace("java.awt.Color", "").replace("r", "h").replace("g", "s").replace("b", "v");
                        lbl.setText(getImageSize() + " Pos=(" + p.y + ":" + p.x + ") Value=" + s + "");// + " RGB=" + "(" + r + "," + g + "," + b + ")");
                    } else {
                        String s = "" + new Color((int) imgData[p.y][p.x], true);
                        s = s.replace("java.awt.Color", "");
                        lbl.setText(getImageSize() + " Pos=(" + p.y + ":" + p.x + ") Value=" + s + "");// + " RGB=" + "(" + r + "," + g + "," + b + ")");
                    }
                    gr.setColor(Color.blue);
                    gr.drawLine(0, mousePos.y, wPanel - 1, mousePos.y);
                    gr.drawLine(mousePos.x, 0, mousePos.x, hPanel - 1);
                    gr.setColor(Color.red);
                    gr.drawRect(mousePos.x - 1, mousePos.y - 1, 2, 2);
                    gr.drawRect(mousePos.x - 10, mousePos.y - 10, 20, 20);
                }
                if (drawableRoiList.size() > 0) {
                    CPoint prev = drawableRoiList.get(0);
                    for (CPoint p : drawableRoiList) {
                        gr.setColor(Color.red);
                        int wx = 5;
                        gr.fillRect(p.column + fromLeft - 2, p.row + fromTop - 2, wx, wx);
                        gr.setColor(Color.green);
                        gr.drawLine(prev.column + fromLeft, prev.row + fromTop, p.column + fromLeft, p.row + fromTop);
                        prev = p;
                    }
                }
            } else {
                if (showRegion && activateROI) {
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
                } else if (showDrawableRegion && drawableRoiList.size() > 0) {
                    CPoint prev = drawableRoiList.get(0);
                    for (CPoint p : drawableRoiList) {
                        gr.setColor(Color.red);
                        int wx = 5;
                        gr.fillRect(p.column + fromLeft - 2, p.row + fromTop - 2, wx, wx);
                        gr.setColor(Color.green);
                        gr.drawLine(prev.column + fromLeft, prev.row + fromTop, p.column + fromLeft, p.row + fromTop);
                        prev = p;
                    }
                    CPoint p = drawableMousePos;
                    gr.drawLine(prev.column + fromLeft, prev.row + fromTop, p.column + fromLeft, p.row + fromTop);
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
        String[] elements = {
            "Clone",
            "Load Image",
            "Save Image",
            "Revert",
            "AutoSize",
            "AutoSizeAspect",
            "Original",
            "Statistics",
            "Histogram",
            "Red",
            "Green",
            "Blue",
            "Gray",
            "HSV",
            "RGB",
            "Edge",
            "Smooth",
            "Sharpen",
            "ROI",
            "Clone ROI",
            "DROI",
            "Save DROI Corners",
            "Save DROI Pixels",
            "Load DROI Corners"
        };

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

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
//                    drawableMousePos = e.getPoint();
//                    drawableRoiList.add(drawableMousePos);
                    activateDrawableROI = false;
                }
            }

            public void mousePressed(java.awt.event.MouseEvent e) {

                if (activateROI && e.getButton() == MouseEvent.BUTTON1) {
                    showRegion = true;
                    mousePosTopLeft = e.getPoint();
                } else if (activateDrawableROI && e.getButton() == MouseEvent.BUTTON1) {
                    showDrawableRegion = true;

                } else {
                    lblShow = true;
                }
//                checkForTriggerEvent(e);
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (activateROI && e.getButton() == MouseEvent.BUTTON1) {
                    mousePos = e.getPoint();
                    mousePosBottomRight = e.getPoint();
                } else if (activateDrawableROI && e.getButton() == MouseEvent.BUTTON1) {
                    showDrawableRegion = true;
                    drawableMousePos = new CPoint(e.getPoint().y - fromTop, e.getPoint().x - fromLeft);
                    System.out.println("row:" + drawableMousePos.row + " col:" + drawableMousePos.column);
                    if (drawableRoiList.size() == 0) {
                        drawableRoiList.add(drawableMousePos);
                    } else if (drawableRoiList.get(drawableRoiList.size() - 1).column != drawableMousePos.column || drawableRoiList.get(drawableRoiList.size() - 1).row != drawableMousePos.row) {
                        drawableRoiList.add(drawableMousePos);
                    }

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
                drawableMousePos = new CPoint(e.getPoint().y - fromTop, e.getPoint().x - fromLeft);
                repaint();
            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                mousePos = e.getPoint();
//                System.out.println("row:"+(mousePos.y-fromTop)+" col:"+(mousePos.x-fromLeft));
                repaint();
            }
        });
    }

    private String getImageSize() {
        if (originalBufferedImage == null) {
            return "";
        }
        String str = "[" + currBufferedImage.getHeight() + "," + currBufferedImage.getWidth() + "] ";
        return str;
    }

    public void saveImage() {
        ImageProcess.imwrite(currBufferedImage);
    }

    public void saveImage(String fileName) {
        ImageProcess.imwrite(currBufferedImage, fileName);
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
//                    fileList = FactoryUtils.getFileListInFolder(imageFolder);
//                    currentImageIndex = getCurrentImageIndex();
                    imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
                    if (activateStatistics) {
                        stat = TStatistics.getStatistics(currBufferedImage);
                    }
                } else {
                    return;
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
                CMatrix.getInstance(currBufferedImage).imhist();
//                imgData = ImageProcess.imageToPixels255Double(originalBufferedImage);
//                currBufferedImage = ImageProcess.pixelsToBufferedImage255(imgData);
//                imgData = ImageProcess.imageToPixels255Double(currBufferedImage);
//                if (histFrame == null) {
////                    histFrame = new FrameImageHistogram(CMatrix.getInstance(imgData));
//                    histFrame = new FrameImageHistogram(CMatrix.getInstance(currBufferedImage));
//                } else {
//                    histFrame.setHistogramData(CMatrix.getInstance(imgData));
//                }
//                histFrame.setVisible(true);
                repaint();
                return;
            }
            if (obj.getText().equals("Statistics")) {
                setDefaultValues();
                activateStatistics = true;
                currBufferedImage = ImageProcess.toGrayLevel(originalBufferedImage);
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
                stat = TStatistics.getStatistics(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Red")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "red");
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Green")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "green");
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Blue")) {
                setDefaultValues();
                activateRedChannel = true;
                currBufferedImage = ImageProcess.isolateChannel(originalBufferedImage, "blue");
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("RGB")) {
                setDefaultValues();
                activateRGB = true;
                currBufferedImage = (originalBufferedImage);
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Gray")) {
                setDefaultValues();
                activateGray = true;
                currBufferedImage = ImageProcess.rgb2gray(originalBufferedImage);
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("HSV")) {
                setDefaultValues();
                activateHSV = true;
                currBufferedImage = ImageProcess.toHSVColorSpace(originalBufferedImage);
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
//                imgData = ImageProcess.imageToPixels255Double(currBufferedImage);
                repaint();
                return;
            }
            if (obj.getText().equals("Edge")) {
                setDefaultValues();
                activateEdge = true;
                currBufferedImage = ImageProcess.edgeDetectionCanny(currBufferedImage, 0.3f, 1.0f, 2.5f, 3, false);
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
                imgData = ImageProcess.imageToPixelsDouble(currBufferedImage);
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
//            if (obj.getText().equals("ROI")) {
//                setDefaultValues();
//                activateEdge = true;
//                currBufferedImage = ImageProcess.pixelsToBufferedImage255(ImageProcess.imageToPixels255Int(originalBufferedImage));
//                currBufferedImage = ImageProcess.edgeDetectionCanny(currBufferedImage, 0.5f, 1.0f, 3.0f, 3, false);
//                repaint();
//                return;
//            }
            if (obj.getText().equals("Revert")) {
                setDefaultValues();
                activateRevert = true;
                currBufferedImage = ImageProcess.revert(currBufferedImage);
                originalBufferedImageTemp = ImageProcess.clone(currBufferedImage);
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
//                CMatrix cm = CMatrix.getInstance(currBufferedImage).subMatrix(mousePosTopLeft, mousePosBottomRight);
                CRectangle cr = new CRectangle(mousePosTopLeft.y, mousePosTopLeft.x,
                        Math.abs(mousePosBottomRight.y - mousePosTopLeft.y), Math.abs(mousePosBottomRight.x - mousePosTopLeft.x));
                BufferedImage bf = ImageProcess.cropImage(currBufferedImage, cr);
//                BufferedImage bf = ImageProcess.pixelsToImageGray(cm.toIntArray2D());
                new FrameImage(bf, obj.getText()).setVisible(true);
                repaint();
                return;
            }
            if (obj.getText().equals("DROI")) {
                setDefaultValues();
                activateDrawableROI = true;
                activateROI = false;
                drawableRoiList.clear();
                repaint();
                return;
            }
            if (obj.getText().equals("Save DROI Corners")) {
                setDefaultValues();
                if (drawableRoiList.size() > 0) {
                    CPoint[] plst = new CPoint[drawableRoiList.size()];
                    plst = drawableRoiList.toArray(plst);
                    int[][] d = new int[drawableRoiList.size()][2];
                    for (int i = 0; i < drawableRoiList.size(); i++) {
                        d[i][0] = plst[i].row;
                        d[i][1] = plst[i].column;
                    }
                    String fileName = FactoryUtils.inputMessage("set roi file name:", "roi.txt");
                    FactoryUtils.writeToFile("data\\" + fileName, d);
                }
                repaint();
                return;
            }
            if (obj.getText().equals("Save DROI Pixels")) {
                setDefaultValues();
                if (drawableRoiList.size() > 0) {
                    CPoint[] plst = new CPoint[drawableRoiList.size()];
                    plst = drawableRoiList.toArray(plst);
                    CPoint[] pixels = FactoryUtils.getPointsInROI(plst);
                    FactoryUtils.savePointsInROI(pixels);
                }
                repaint();
                return;
            }
            if (obj.getText().equals("Load DROI Corners")) {
                setDefaultValues();
                drawableRoiList.clear();
                double[][] d = FactoryUtils.readFromFile(",");
                int[][] dd = FactoryUtils.toIntArray2D(d);
                for (int i = 0; i < d.length; i++) {
                    CPoint p = new CPoint(dd[i][0], dd[i][1]);
                    drawableRoiList.add(p);
                }
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
        activateDrawableROI = false;
        activateSaveImage = false;
        activateRevert = false;
        activateCloneROI = false;
        activateOriginal = false;
        activateHistogram = false;
        activateStatistics = false;
        activateRedChannel = false;
        activateGreenChannel = false;
        activateBlueChannel = false;
        activateRGB = false;
        activateGray = false;
        activateHSV = false;
        activateEdge = false;
        activateAutoSize = false;
        activateAutoSizeAspect = false;
    }

}
