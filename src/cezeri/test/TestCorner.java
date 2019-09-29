/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

/**
 *
 * @author musa-atas
 */
import cezeri.image_processing.HarrisCornerDetection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestCorner extends JApplet {

    Image edgeImage, accImage, outputImage;
    MediaTracker tracker = null;
    PixelGrabber grabber = null;
    int width = 0, height = 0;
    String fileNames[] = {"lena.png", "microphone.png", "screw.png","chessboard.jpg"};
    javax.swing.Timer timer;
    //slider constraints
    static int TH_MIN = 0;
    static int TH_MAX = 200;
    static int TH_INIT = 40;
    double threshold = (double) TH_INIT;
    int imageNumber = 0;
    static int progress = 0;
    static int orig[] = null;
    Image image[] = new Image[fileNames.length];
    JProgressBar progressBar;
    JPanel selectionPanel, controlPanel, imagePanel, progressPanel;
    JLabel origLabel, outputLabel, modeLabel, comboLabel, sigmaLabel, thresholdLabel, processing;
    JSlider thresholdSlider;
    JButton thresholding;
    JComboBox imSel;
    static HarrisCornerDetection harrisOp;
    static Image edges;

    // Applet init function	
    public void init() {

        tracker = new MediaTracker(this);
        for (int i = 0; i < fileNames.length; i++) {
            image[i] = getImage(this.getCodeBase(), fileNames[i]);
            image[i] = image[i].getScaledInstance(256, 256, Image.SCALE_SMOOTH);
            tracker.addImage(image[i], i);
        }
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("error: " + e);
        }

        Container cont = getContentPane();
        cont.removeAll();
        cont.setBackground(Color.black);
        cont.setLayout(new BorderLayout());

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 4, 15, 0));
        controlPanel.setBackground(new Color(192, 204, 226));
        imagePanel = new JPanel();
        imagePanel.setBackground(new Color(192, 204, 226));
        progressPanel = new JPanel();
        progressPanel.setBackground(new Color(192, 204, 226));
        progressPanel.setLayout(new GridLayout(2, 1));

        comboLabel = new JLabel("IMAGE");
        comboLabel.setHorizontalAlignment(JLabel.CENTER);
        controlPanel.add(comboLabel);
        modeLabel = new JLabel(("K = " + ((double) TH_INIT / 1000)));
        modeLabel.setHorizontalAlignment(JLabel.CENTER);
        controlPanel.add(modeLabel);

        processing = new JLabel("Processing...");
        processing.setHorizontalAlignment(JLabel.LEFT);
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true); //get space for the string
        progressBar.setString("");          //but don't paint it
        progressPanel.add(processing);
        progressPanel.add(progressBar);

        width = image[imageNumber].getWidth(null);
        height = image[imageNumber].getHeight(null);

        imSel = new JComboBox(fileNames);
        imageNumber = imSel.getSelectedIndex();
        imSel.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        imageNumber = imSel.getSelectedIndex();
                        origLabel.setIcon(new ImageIcon(image[imageNumber]));
                        processImage();
                    }
                });
        controlPanel.add(imSel, BorderLayout.PAGE_START);

        timer = new javax.swing.Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                //System.out.println("Got value: " + harrisOp.getProgress());
                progressBar.setValue(harrisOp.getProgress());

            }
        });

        origLabel = new JLabel("Original Image", new ImageIcon(image[imageNumber]), JLabel.CENTER);
        origLabel.setVerticalTextPosition(JLabel.BOTTOM);
        origLabel.setHorizontalTextPosition(JLabel.CENTER);
        origLabel.setForeground(Color.blue);
        imagePanel.add(origLabel);

        outputLabel = new JLabel("Corner Detected", new ImageIcon(image[imageNumber]), JLabel.CENTER);
        outputLabel.setVerticalTextPosition(JLabel.BOTTOM);
        outputLabel.setHorizontalTextPosition(JLabel.CENTER);
        outputLabel.setForeground(Color.blue);
        imagePanel.add(outputLabel);

        thresholdSlider = new JSlider(JSlider.HORIZONTAL, TH_MIN, TH_MAX, TH_INIT);
        thresholdSlider.addChangeListener(new thresholdListener());
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setMinorTickSpacing(10);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);
        thresholdSlider.setBackground(new Color(192, 204, 226));
        controlPanel.add(thresholdSlider);

        cont.add(controlPanel, BorderLayout.NORTH);
        cont.add(imagePanel, BorderLayout.CENTER);
        cont.add(progressPanel, BorderLayout.SOUTH);

        processImage();

    }

    class thresholdListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                threshold = source.getValue();
                modeLabel.setText("K = " + ((double) source.getValue() / 1000));
                processImage();
            }
        }
    }

    private void processImage() {
        orig = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(image[imageNumber], 0, 0, width, height, orig, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e2) {
            System.out.println("error: " + e2);
        }
        progressBar.setMaximum(width - 4);

        processing.setText("Processing...");
        thresholdSlider.setEnabled(false);
        imSel.setEnabled(false);

        //edgedetector = new sobel();
        harrisOp = new HarrisCornerDetection();
        timer.start();

        new Thread() {

            public void run() {
                //edgedetector.init(orig,width,height);
                //orig=edgedetector.process();

                //edges = image[imageNumber];//createImage(new MemoryImageSource(width, height, orig, 0, width));

                harrisOp.init(orig, width, height, threshold / 1000);
                orig = harrisOp.process();

                final Image output = createImage(new MemoryImageSource(width, height, orig, 0, width));
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        outputLabel.setIcon(new ImageIcon(output));
                        //origLabel.setIcon(new ImageIcon(createImage(new MemoryImageSource(width, height, edgedetector.process(), 0, width))));	
                        processing.setText("Done");
                        thresholdSlider.setEnabled(true);
                        imSel.setEnabled(true);
                    }
                });
            }
        }.start();
    }
}
