/**
 * TODO: ImageProcess ve diğer core classlardaki tüm işlemler parametre olarak
 * double[][] almalı.
 *
 * ****************************************************************************
 * OpenCV komutlarını çağıran harici uygulamalarda
 * System.loadLibrary(Core.NATIVE_LIBRARY_NAME); eklenmeli ve 64 bit jar add jar
 * ile eklendikten sonra dll dosyası da kök dizinde bulunmalı
 * ****************************************************************************
 */
package cezeri.image_processing;

import cezeri.types.TRoi;
import cezeri.types.TGrayPixel;
import cezeri.types.TWord;
import cezeri.feature.extraction.FeatureExtractionLBP;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.matrix.CRectangle;
import cezeri.matrix.FactoryMatrix;
import cezeri.utils.*;
import com.jhlabs.composite.ColorDodgeComposite;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PointFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author venap3
 */
public final class ImageProcess {
//    static{
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }

    /**
     * Canny Edge Detector
     *
     * @param double[][] : inputImage
     * @param lowThreshold: default value is 0.3f
     * @param highTreshold:default value is 1.0f
     * @param gaussianKernelRadious:default value is 2.5f
     * @param guassianKernelWidth:default value is 3
     * @param isContrastNormalized : default false
     * @return BufferedImage
     */
    public static double[][] edgeDetectionCanny(double[][] d) {
        float lowThreshold = 0.3f;
        float highTreshold = 1.0f;
        float gaussianKernelRadious = 2.5f;
        int guassianKernelWidth = 3;
        boolean isContrastNormalized = false;
        BufferedImage currBufferedImage = ImageProcess.pixelsToImageGray(d);
        currBufferedImage = edgeDetectionCanny(currBufferedImage, lowThreshold, highTreshold, gaussianKernelRadious, guassianKernelWidth, isContrastNormalized);
        currBufferedImage = toGrayLevel(currBufferedImage);
        return imageToPixelsDouble(currBufferedImage);
    }

    public static BufferedImage edgeDetectionCannyAsImage(double[][] d) {
        float lowThreshold = 0.3f;
        float highTreshold = 1.0f;
        float gaussianKernelRadious = 2.5f;
        int guassianKernelWidth = 3;
        boolean isContrastNormalized = false;
        BufferedImage currBufferedImage = ImageProcess.pixelsToImageGray(d);
        currBufferedImage = edgeDetectionCanny(currBufferedImage, lowThreshold, highTreshold, gaussianKernelRadious, guassianKernelWidth, isContrastNormalized);
        currBufferedImage = toGrayLevel(currBufferedImage);
        return currBufferedImage;
    }

    public static double[][] edgeDetectionCanny(BufferedImage img) {
        float lowThreshold = 0.3f;
        float highTreshold = 1.0f;
        float gaussianKernelRadious = 2.5f;
        int guassianKernelWidth = 3;
        boolean isContrastNormalized = false;
        BufferedImage currBufferedImage = edgeDetectionCanny(img, lowThreshold, highTreshold, gaussianKernelRadious, guassianKernelWidth, isContrastNormalized);
        return imageToPixelsDouble(currBufferedImage);
    }

    public static Mat ocv_edgeDetectionCanny(Mat imageGray) {
        Mat imageCanny = new Mat();
        Imgproc.Canny(imageGray, imageCanny, 10, 100, 3, true);
        return imageCanny;
    }

    public static BufferedImage ocv_edgeDetectionCanny(BufferedImage img) {
        Mat imageGray = ocv_img2Mat(img);
        Mat imageCanny = new Mat();
        Imgproc.Canny(imageGray, imageCanny, 10, 100, 3, true);
        return ocv_mat2Img(imageCanny);
    }

    public static Mat ocv_blendImagesMat(Mat img1, Mat img2) {
        Mat dst = new Mat();
        Core.addWeighted(img1, 0.5, img2, 0.5, 0.0, dst);
        return dst;
    }
    
    public static Mat ocv_blendImagesMat(BufferedImage img1, BufferedImage img2) {
        Mat src1 = ImageProcess.ocv_img2Mat(img1);
        Mat src2 = ImageProcess.ocv_img2Mat(img2);
        Mat dst = new Mat();
        Core.addWeighted(src1, 0.5, src2, 0.5, 0.0, dst);
        return dst;
    }

    public static BufferedImage ocv_blendImagesBuffered(BufferedImage img1, BufferedImage img2) {
        Mat src1 = ImageProcess.ocv_img2Mat(img1);
        Mat src2 = ImageProcess.ocv_img2Mat(img2);
        Mat dst = new Mat();
        Core.addWeighted(src1, 0.5, src2, 0.5, 0.0, dst);
        BufferedImage bf_3 = ImageProcess.ocv_mat2Img(dst);
        return bf_3;
    }

    public static BufferedImage ocv_edgeDetectionCanny(double[][] d) {
        BufferedImage img = pixelsToImageGray(d);
        Mat imageGray = ocv_img2Mat(img);
        Mat imageCanny = new Mat();
//        Imgproc.Canny(imageGray, imageCanny, 10, 100, 3, true);
        Imgproc.Canny(imageGray, imageCanny, 10, 150, 3, true);
        return ocv_mat2Img(imageCanny);
    }

    public static double[][] ocv_edgeDetectionCanny2D(BufferedImage img) {
        Mat imageGray = ocv_img2Mat(img);
        Mat imageCanny = new Mat();
//        Imgproc.Canny(imageGray, imageCanny, 50, 200, 3, true);
//        Imgproc.Canny(imageGray, imageCanny, 20, 150, 3, true);
//        Imgproc.Canny(imageGray, imageCanny, 20, 100, 3, true);
        Imgproc.Canny(imageGray, imageCanny, 50, 100, 3, true);
        double[][] ret = imageToPixelsDouble(ocv_mat2Img(imageCanny));
        return ret;
    }

    /**
     * Musa Edge Detector
     *
     * @param double[][] d: inputImage
     * @param thr: threshold double value
     * @return
     */
    public static double[][] edgeDetectionMusa(double[][] d, int thr) {
//        double thr=30;
        double[][] a1 = FactoryUtils.shiftOnRow(d, 1);
        double[][] a2 = FactoryUtils.shiftOnRow(d, -1);
        double[][] a3 = FactoryUtils.shiftOnColumn(d, 1);
        double[][] a4 = FactoryUtils.shiftOnColumn(d, -1);

        double[][] ret1 = FactoryUtils.subtractWithThreshold(d, a1, thr);
        double[][] ret2 = FactoryUtils.subtractWithThreshold(d, a2, thr);
        double[][] ret3 = FactoryUtils.subtractWithThreshold(d, a3, thr);
        double[][] ret4 = FactoryUtils.subtractWithThreshold(d, a4, thr);

        double[][] retx = FactoryUtils.add(ret1, ret2);
        double[][] rety = FactoryUtils.add(ret3, ret4);
        double[][] ret = FactoryUtils.add(retx, rety);
//        double[][] ret=FactoryUtils.add(ret1,ret3);
        return ret;
    }

    /**
     * Canny Edge Detector
     *
     * @param double[][] : inputImage
     * @param lowThreshold: default value is 0.3f
     * @param highTreshold:default value is 1.0f
     * @param gaussianKernelRadious:default value is 2.5f
     * @param guassianKernelWidth:default value is 3
     * @param isContrastNormalized : default false
     * @return BufferedImage
     */
    public static int[][] edgeDetectionCanny(
            double[][] d,
            float lowThreshold,
            float highTreshold,
            float gaussianKernelRadious,
            int guassianKernelWidth,
            boolean isContrastNormalized) {
        BufferedImage currBufferedImage = ImageProcess.pixelsToImageGray(d);
        currBufferedImage = edgeDetectionCanny(currBufferedImage, lowThreshold, highTreshold, gaussianKernelRadious, guassianKernelWidth, isContrastNormalized);
        return imageToPixelsInt(currBufferedImage);
    }

    /**
     * Canny Edge Detector
     *
     * @param img : inputImage
     * @param lowThreshold: default value is 0.3f
     * @param highTreshold:default value is 1.0f
     * @param gaussianKernelRadious:default value is 2.5f
     * @param guassianKernelWidth:default value is 3
     * @param isContrastNormalized : default false
     * @return BufferedImage
     */
    public static BufferedImage edgeDetectionCanny(
            BufferedImage img,
            float lowThreshold,
            float highTreshold,
            float gaussianKernelRadious,
            int guassianKernelWidth,
            boolean isContrastNormalized) {
        BufferedImage currBufferedImage = img;
        CannyEdgeDetector detector = new CannyEdgeDetector();

        detector.setLowThreshold(lowThreshold);
        detector.setHighThreshold(highTreshold);
        detector.setGaussianKernelRadius(gaussianKernelRadious);
        detector.setGaussianKernelWidth(guassianKernelWidth);
        detector.setContrastNormalized(isContrastNormalized);

        detector.setSourceImage(currBufferedImage);
        detector.process();
        currBufferedImage = detector.getEdgesImage();
        return ImageProcess.rgb2gray(currBufferedImage);
    }

    public static BufferedImage isolateChannel(BufferedImage image, String channel) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int iAlpha = 0;
        int iRed = 0;
        int iGreen = 0;
        int iBlue = 0;
        int newPixel = 0;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                newPixel = 0;

                iAlpha = rgb >> 24 & 0xff;
                iRed = rgb >> 16 & 0xff;
                iGreen = rgb >> 8 & 0xff;
                iBlue = rgb & 0xff;
                if (channel.equals("red")) {
//                    Tevafuk için eklenmişti
//                    if (iRed > 110 && iRed < 220 && iGreen > 20 && iGreen < 110 && iBlue > 20 && iBlue < 110) {
//                        newPixel = 0 | 170 << 16;
//                    } else {
//                        newPixel = 0;
//                    }
                    newPixel = newPixel | iRed << 16;
                }

                if (channel.equals("green")) {
                    newPixel = newPixel | iGreen << 8;
                }

                if (channel.equals("blue")) {
                    newPixel = newPixel | iBlue;
                }

                result.setRGB(i, j, newPixel);
            }
        }

        return result;
    }

    public static BufferedImage showRedPixels(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage redImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color color;
        int t = 140;
        //int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        //yaz(pixels);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //int pp = img.getRGB(x, y);

//                printPixelARGB(pp);
                Color cl = new Color(img.getRGB(x, y));
                int r = cl.getRed();
                int g = cl.getGreen();
                int b = cl.getBlue();
//                float[] hsv = new float[3];
//                hsv = cl.RGBtoHSB(r, g, b, hsv);
//                int q = 15;
//                if (hsv[0] * 360 > 345 || hsv[0] * 360 < 15) {
//                    //yaz("red:" + (hsv[0] * 360));
//                }else{
//                    yaz("bulmadi");
//                }
//

                yaz("rgb:" + r + "," + g + "," + b);
//                if (red == green && red == blue) {
//                    color = new Color(0, 0, 0);
//                } else if (red > t && green < t && blue < t) {
//                    color = new Color(255, 0, 0);
//                } else {
//                    color = new Color(0, 0, 0);
//                }
//
//                redImage.setRGB(x, y, color.getRGB());
            }
        }
        return redImage;
    }

    public static void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    }

    public static BufferedImage changeToRedMonochrome(BufferedImage grayImage) {

        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        BufferedImage redImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color grayColor = new Color(grayImage.getRGB(x, y));
                int gray = grayColor.getRed();

                int red = (gray > 127) ? 255 : gray / 2;
                int blue = (gray > 127) ? gray / 2 : 0;
                int green = (gray > 127) ? gray / 2 : 0;

                Color redColor = new Color(red, blue, green);
                redImage.setRGB(x, y, redColor.getRGB());
            }
        }
        return redImage;
    }

    public static BufferedImage DCT(BufferedImage bimg) {
        final int N = 8;  // Block size
        int nrows, ncols, m, n, x, y, u, v, img[][], in[][];
        double dct[][], sum, au, av;
        double n1 = Math.sqrt(1.0 / N), n2 = Math.sqrt(2.0 / N);
        img = imageToPixelsInt(bimg);
        nrows = img.length;
        ncols = img[0].length;
        if (nrows % N != 0 || ncols % N != 0) {
            System.out.println("Nrows and ncols should be 8's power");
            return bimg;
//            System.exit(0);
        }
        in = new int[nrows][ncols];
        dct = new double[nrows][ncols];
        // For each NxN block[m,n]
        for (m = 0; m < nrows; m += N) {
            for (n = 0; n < ncols; n += N) {
                // For each pixel[u,v] in block[m,n]
                for (u = m; u < m + N; u++) {
                    au = (u == m) ? n1 : n2;
                    for (v = n; v < n + N; v++) {
                        av = (v == n) ? n1 : n2;

                        // Sum up all pixels in the block
                        for (x = m, sum = 0; x < m + N; x++) {
                            for (y = n; y < n + N; y++) {
                                in[x][y] = img[x][y] - 128;  // Subtract by 128
                                sum += in[x][y] * Math.cos((2 * (x - m) + 1) * (u - m) * Math.PI / (2 * N))
                                        * Math.cos((2 * (y - n) + 1) * (v - n) * Math.PI / (2 * N));
                            }
                        }
                        dct[u][v] = au * av * sum;
                    } // for v
                } // for u
            }  // for n
        }  // for m
        return ImageProcess.pixelsToImageGray(FactoryUtils.toIntArray2D(dct));
    }

    public static BufferedImage cropImage(BufferedImage src, CRectangle rect) {
        if (src.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            return src.getSubimage(rect.column, rect.row, rect.width, rect.height);
        } else {
            return toBGR(src.getSubimage(rect.column, rect.row, rect.width, rect.height));
        }

//        BufferedImage dest = clone(src);
//        try {
//            src = src.getSubimage(rect.column, rect.row, rect.width, rect.height);
//        } catch (Exception ex) {
//            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return dest;
    }

    public static BufferedImage convertImageToPencilSketch(BufferedImage src) {
        PointFilter grayScaleFilter = new GrayscaleFilter();
        BufferedImage grayScale = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        grayScaleFilter.filter(src, grayScale);

//inverted gray scale
        BufferedImage inverted = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        PointFilter invertFilter = new InvertFilter();
        invertFilter.filter(grayScale, inverted);

//gaussian blurr
        GaussianFilter gaussianFilter = new GaussianFilter(20);
        BufferedImage gaussianFiltered = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        gaussianFilter.filter(inverted, gaussianFiltered);

//color dodge
        ColorDodgeComposite cdc = new ColorDodgeComposite(1.0f);
        CompositeContext cc = cdc.createContext(inverted.getColorModel(), grayScale.getColorModel(), null);
        Raster invertedR = gaussianFiltered.getRaster();
        Raster grayScaleR = grayScale.getRaster();
        BufferedImage composite = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        WritableRaster colorDodgedR = composite.getRaster();
        cc.compose(invertedR, grayScaleR, colorDodgedR);

//==================================
        return composite;
    }

    public static boolean lookNeighbourPixels(int[][] img, Point p, int r) {
        ArrayList<Point> lst = getWindowEdgePixelPositions(img, p, r);
        for (Point pt : lst) {
            if (img[pt.x][pt.y] != 127 && img[pt.x][pt.y] != 0) {
                p.x = pt.x;
                p.y = pt.y;
                //yaz("konumlandı: p.x:"+p.x+" p.y:"+p.y);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Point> getWindowEdgePixelPositions(int[][] img, Point p, int r) {
        Point m = new Point();
        m.x = p.x - r;
        m.y = p.y - r;
        Point pt = null;
        ArrayList<Point> lst = new ArrayList<Point>();
        if (m.x <= 0 || m.y <= 0 || m.x + 2 * r >= img.length || m.y + 2 * r >= img[0].length) {
            return lst;
        }
        for (int i = 0; i < 2 * r + 1; i++) {
            for (int j = 0; j < 2 * r + 1; j++) {
                pt = new Point(m.x + j, m.y + i);
                lst.add(pt);
            }
        }
        lst = FactoryUtils.shuffleList(lst);
        return lst;
    }

    /**
     * return Alpha, Red, Green and Blue values of original RGB image
     *
     * @param image
     * @return
     */
    public static int[][][] imageToPixelsColorInt(BufferedImage image) {
        int numRows = image.getHeight();
        int numCols = image.getWidth();
        // Now we make our array.
        int[][][] pixels = new int[numRows][numCols][4];
//        int[] outputChannels=new int[4];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
//                image.getRaster().getPixel(col,row,outputChannels);
//                pixels[row][col]=outputChannels;
                Color c = new Color(image.getRGB(col, row));
                pixels[row][col][0] = c.getAlpha();  // Alpha
                pixels[row][col][1] = c.getRed();  // Red
                pixels[row][col][2] = c.getGreen();  // Green
                pixels[row][col][3] = c.getBlue();        // Blue
            }
        }
        return pixels;
    }

    /**
     * return Alpha, Red, Green and Blue values of original RGB image
     *
     * @param image
     * @return
     */
    public static double[][][] imageToPixelsColorDouble(BufferedImage image) {
        int numRows = image.getHeight();
        int numCols = image.getWidth();
        // Now we make our array.
        double[][][] pixels = new double[numRows][numCols][4];
//        double[] outputChannels=new double[4];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
//                image.getRaster().getPixel(col,row,outputChannels);
//                pixels[row][col]=outputChannels;
                Color c = new Color(image.getRGB(col, row));
                pixels[row][col][0] = c.getAlpha();  // Alpha
                pixels[row][col][1] = c.getRed();  // Red
                pixels[row][col][2] = c.getGreen();  // Green
                pixels[row][col][3] = c.getBlue();        // Blue
            }
        }
        return pixels;
    }

    /**
     * return Alpha, Red, Green and Blue values of original RGB image
     *
     * @param image
     * @return
     */
    public static int[][][] imageToPixelsColorIntV2(BufferedImage image) {
        // Java's peculiar way of extracting pixels is to give them
        // back as a one-dimensional array from which we will construct
        // our version.

        int numRows = image.getHeight();
        int numCols = image.getWidth();
        int[] oneDPixels = new int[numRows * numCols];

        // This will place the pixels in oneDPixels[]. Each int in
        // oneDPixels has 4 bytes containing the 4 pieces we need.
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, numCols, numRows,
                oneDPixels, 0, numCols);
        try {
            grabber.grabPixels(0);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        // Now we make our array.
        int[][][] pixels = new int[numRows][numCols][4];
        for (int row = 0; row < numRows; row++) {
            // First extract a row of int's from the right place.
            int[] aRow = new int[numCols];
            for (int col = 0; col < numCols; col++) {
                int element = row * numCols + col;
                aRow[col] = oneDPixels[element];
            }

            // In Java, the most significant byte is the alpha value,
            // followed by R, then G, then B. Thus, to extract the alpha
            // value, we shift by 24 and make sure we extract only that byte.
            for (int col = 0; col < numCols; col++) {
                pixels[row][col][0] = (aRow[col] >> 24) & 0xFF;  // Alpha
                pixels[row][col][1] = (aRow[col] >> 16) & 0xFF;  // Red
                pixels[row][col][2] = (aRow[col] >> 8) & 0xFF;  // Green
                pixels[row][col][3] = (aRow[col]) & 0xFF;        // Blue
            }
        }

        return pixels;
    }

    /**
     * return Alpha, Red, Green and Blue values of original RGB image
     *
     * @param image
     * @return
     */
    public static double[][][] imageToPixelsColorDoubleFaster(BufferedImage image) {
        // Java's peculiar way of extracting pixels is to give them
        // back as a one-dimensional array from which we will construct
        // our version.

        int numRows = image.getHeight();
        int numCols = image.getWidth();
        int[] oneDPixels = new int[numRows * numCols];

        // This will place the pixels in oneDPixels[]. Each int in
        // oneDPixels has 4 bytes containing the 4 pieces we need.
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, numCols, numRows,
                oneDPixels, 0, numCols);
        try {
            grabber.grabPixels(0);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        // Now we make our array.
        double[][][] pixels = new double[4][numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            // First extract a row of int's from the right place.
            int[] aRow = new int[numCols];
            for (int col = 0; col < numCols; col++) {
                int element = row * numCols + col;
                aRow[col] = oneDPixels[element];
            }

            // In Java, the most significant byte is the alpha value,
            // followed by R, then G, then B. Thus, to extract the alpha
            // value, we shift by 24 and make sure we extract only that byte.
            for (int col = 0; col < numCols; col++) {
                pixels[0][row][col] = (aRow[col] >> 24) & 0xFF;  // Alpha
                pixels[1][row][col] = (aRow[col] >> 16) & 0xFF;  // Red
                pixels[2][row][col] = (aRow[col] >> 8) & 0xFF;  // Green
                pixels[3][row][col] = (aRow[col]) & 0xFF;        // Blue
            }
        }

        return pixels;
    }

    public static BufferedImage pixelsToImageColor(int[][][] pixels) {
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        int[] oneDPixels = new int[numRows * numCols];

        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                oneDPixels[index] = ((pixels[row][col][0] << 24) & 0xFF000000)
                        | ((pixels[row][col][1] << 16) & 0x00FF0000)
                        | ((pixels[row][col][2] << 8) & 0x0000FF00)
                        | ((pixels[row][col][3]) & 0x000000FF);
                index++;
            }
        }

        // The MemoryImageSource class is an ImageProducer that can
        // build an image out of 1D pixels. Then, rather confusingly,
        // the createImage() method, inherited from Component, is used
        // to make the actual Image instance. This is simply Java's
        // confusing, roundabout way. An alternative is to use the
        // Raster models provided in BufferedImage.
        MemoryImageSource imSource = new MemoryImageSource(numCols, numRows, oneDPixels, 0, numCols);
        Image imG = Toolkit.getDefaultToolkit().createImage(imSource);
        BufferedImage I = imageToBufferedImage(imG);
        return I;

    }

    public static BufferedImage pixelsToImageColor(double[][][] pixels) {
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        int[] oneDPixels = new int[numRows * numCols];

        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                oneDPixels[index] = (((int) pixels[row][col][0] << 24) & 0xFF000000)
                        | (((int) pixels[row][col][1] << 16) & 0x00FF0000)
                        | (((int) pixels[row][col][2] << 8) & 0x0000FF00)
                        | (((int) pixels[row][col][3]) & 0x000000FF);
                index++;
            }
        }

        // The MemoryImageSource class is an ImageProducer that can
        // build an image out of 1D pixels. Then, rather confusingly,
        // the createImage() method, inherited from Component, is used
        // to make the actual Image instance. This is simply Java's
        // confusing, roundabout way. An alternative is to use the
        // Raster models provided in BufferedImage.
        MemoryImageSource imSource = new MemoryImageSource(numCols, numRows, oneDPixels, 0, numCols);
        Image imG = Toolkit.getDefaultToolkit().createImage(imSource);
        BufferedImage I = imageToBufferedImage(imG);
        return I;

    }

    public static BufferedImage pixelsToImageColorArgbFormat(double[][][] pixels) {
        int numRows = pixels[0].length;
        int numCols = pixels[0][0].length;
        int[] oneDPixels = new int[numRows * numCols];

        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                oneDPixels[index] = (((int) pixels[0][row][col] << 24) & 0xFF000000)
                        | (((int) pixels[1][row][col] << 16) & 0x00FF0000)
                        | (((int) pixels[2][row][col] << 8) & 0x0000FF00)
                        | (((int) pixels[3][row][col]) & 0x000000FF);
                index++;
            }
        }

        // The MemoryImageSource class is an ImageProducer that can
        // build an image out of 1D pixels. Then, rather confusingly,
        // the createImage() method, inherited from Component, is used
        // to make the actual Image instance. This is simply Java's
        // confusing, roundabout way. An alternative is to use the
        // Raster models provided in BufferedImage.
        MemoryImageSource imSource = new MemoryImageSource(numCols, numRows, oneDPixels, 0, numCols);
        Image imG = Toolkit.getDefaultToolkit().createImage(imSource);
        BufferedImage I = imageToBufferedImage(imG);
        return I;

    }

    public static byte[] getBytes(BufferedImage img) {
        return ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
    }

    public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static int[][] imageToPixelsInt(BufferedImage img) {
        double[][] d = imageToPixelsDouble(img);
        int[][] original = FactoryUtils.toIntArray2D(d);
        return original;
    }

    public static double[][] imageToPixelsDouble(BufferedImage img) {
        if (img == null) {
            return null;
        }
        double[][] original = new double[img.getHeight()][img.getWidth()]; // where we'll put the image
        if ((img.getType() == BufferedImage.TYPE_CUSTOM)
                || (img.getType() == BufferedImage.TYPE_INT_RGB)
                || (img.getType() == BufferedImage.TYPE_INT_ARGB)
                || (img.getType() == BufferedImage.TYPE_3BYTE_BGR)
                || (img.getType() == BufferedImage.TYPE_4BYTE_ABGR)) {
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    original[i][j] = img.getRGB(j, i);
                }
            }
        } else {
            Raster image_raster = img.getData();
            //get pixel by pixel
            int[] pixel = new int[1];
            int[] buffer = new int[1];

            // declaring the size of arrays
            original = new double[img.getHeight()][img.getWidth()];

            //get the image in the array
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    pixel = image_raster.getPixel(j, i, buffer);
                    original[i][j] = pixel[0];
                }
            }
        }
        return original;
    }

    public static int[][] imageToPixels255_CIZ(BufferedImage img) {
        MediaTracker mt = new MediaTracker(null);
        mt.addImage(img, 0);
        try {
            mt.waitForID(0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        int w = img.getWidth();
        int h = img.getHeight();
        //System.out.println("w:"+w+" h:"+h);
        int pixels[] = new int[w * h];
        int fpixels[] = new int[w * h];
        int dpixel[][] = new int[w][h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            // TODO: handle exception
        }
        int red = (pixels[0] >> 16 & 0xff);
        int green = pixels[0] >> 8 & 0xff;
        int blue = pixels[0] & 0xff;

        if (red == green && red == blue) {
            for (int i = 0; i < pixels.length; i++) {
                fpixels[i] = pixels[i] & 0xff;
            }
        } else {
            for (int i = 0; i < pixels.length; i++) {
                int r = pixels[i] >> 16 & 0xff;
                int g = pixels[i] >> 8 & 0xff;
                int b = pixels[i] & 0xff;
                int y = (int) (0.33000000000000002D * (double) r + 0.56000000000000005D * (double) g + 0.11D * (double) b);
                fpixels[i] = y;
//            fpixels[i] = pixels[i];
            }
        }
        int k = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                dpixel[j][i] = fpixels[k];
                k++;
            }
        }

        return dpixel;
    }

    public static int[] imageToPixelsTo1D(BufferedImage img) {
        return FactoryUtils.toIntArray1D(imageToPixelsInt(img));
    }

    public static int[][] imageToPixelsROI(BufferedImage img, Rectangle roi) {

        MediaTracker mt = new MediaTracker(null);
        mt.addImage(img, 0);
        try {
            mt.waitForID(0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        int w = img.getWidth();
        int h = img.getHeight();
        //System.out.println("w:"+w+" h:"+h);
        int pixels[] = new int[w * h];
        int fpixels[] = new int[w * h];
        int dpixel[][] = new int[roi.width][roi.height];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            // TODO: handle exception
        }
        int cnt = 0;
        int py = 0;
        for (int i = roi.y * w + roi.x; i < (roi.y + roi.height) * w + roi.x; i++) {
            if (cnt < roi.width) {
                int r = pixels[i] >> 16 & 0xff;
                int g = pixels[i] >> 8 & 0xff;
                int b = pixels[i] >> 0 & 0xff;
                int y = (int) (0.33000000000000002D * (double) r + 0.56000000000000005D * (double) g + 0.11D * (double) b);
                dpixel[cnt][py] = y;
                cnt++;
            } else {
                cnt = 0;
                i = i + w - roi.width;
                py++;
            }
        }
        return dpixel;
    }

    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    public static BufferedImage pixelsToImageGray(int dizi[][]) {
        int[] pixels = FactoryUtils.toIntArray1D(dizi);
        int h = dizi.length;
        int w = dizi[0].length;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, w, h, pixels);
        return image;
    }

    public static BufferedImage pixelsToImageGray(double dizi[][]) {
        int[] pixels = FactoryUtils.toIntArray1D(dizi);
        int h = dizi.length;
        int w = dizi[0].length;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, w, h, pixels);
        return image;
    }

    public static BufferedImage pixelsToBufferedImage255_CIZ(int dizi[][]) {
        int w = dizi.length;
        int h = dizi[0].length;
        int ai[] = new int[w * h];
        int aix[] = new int[w * h];
        int k = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                ai[k] = dizi[j][i];
                aix[k] = (0xff000000 | ai[k] << 16 | ai[k] << 8 | ai[k]);
                k++;
            }
        }
        Image imG = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, aix, 0, w));
        BufferedImage myImage = imageToBufferedImage(imG);
        return myImage;
    }

//    public static BufferedImage toGrayLevel(BufferedImage img) {
//        return rgb2gray(img);
//    }
    public static BufferedImage rgb2hsv(BufferedImage img) {
        int[][][] ret = convertHSV(img);
        BufferedImage rimg = pixelsToImageColor(ret);
        return rimg;
    }

    public static BufferedImage hsv2rgb(BufferedImage img) {
        int[][][] d = imageToPixelsColorInt(img);
        int nr = d.length;
        int nc = d[0].length;
        int ch = d[0][0].length;
        int[][][] ret = new int[nr][nc][ch];

        int red, green, blue;
        float hue, saturation, brightness;

        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                hue = d[i][j][1] / 255.0f;
                saturation = d[i][j][2] / 255.0f;
                brightness = d[i][j][3] / 255.0f;
                int rgb = Color.HSBtoRGB(hue, saturation, brightness);
                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;
                ret[i][j][0] = 255;//alpha channel
                ret[i][j][1] = red;
                ret[i][j][2] = green;
                ret[i][j][3] = blue;
            }
        }

        BufferedImage rimg = pixelsToImageColor(ret);
        return rimg;
    }

    public static BufferedImage toHSVColorSpace(BufferedImage img) {
        return rgb2hsv(img);
    }

    public static BufferedImage getHueChannel(BufferedImage img) {
        int[][][] ret = convertHSV(img);
        int[][] d = new int[ret.length][ret[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                d[i][j] = ret[i][j][1];
            }
        }
        BufferedImage bf = ImageProcess.pixelsToImageGray(d);
        return bf;
    }

    public static BufferedImage getSaturationChannel(BufferedImage img) {
        int[][][] ret = convertHSV(img);
        int[][] d = new int[ret.length][ret[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                d[i][j] = ret[i][j][2];
            }
        }
        BufferedImage bf = ImageProcess.pixelsToImageGray(d);
        return bf;
    }

    public static BufferedImage getValueChannel(BufferedImage img) {
        int[][][] ret = convertHSV(img);
        int[][] d = new int[ret.length][ret[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                d[i][j] = ret[i][j][3];
            }
        }
        BufferedImage bf = ImageProcess.pixelsToImageGray(d);
        return bf;
    }

    public static int[][][] convertHSV(BufferedImage img) {
        int[][][] d = imageToPixelsColorInt(img);
        int[][][] ret = new int[d.length][d[0].length][d[0][0].length];
        int n = 255;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                int[] val = FactoryMatrix.clone(d[i][j]);
                float[] q = toHSV(val[1], val[2], val[3]);
                val[1] = (int) (q[0] * n);
                val[2] = (int) (q[1] * n);
                val[3] = (int) (q[2] * n);
                ret[i][j] = val;
            }
        }
        return ret;
    }

    public static float[] toHSV(int red, int green, int blue) {
        float[] hsv = new float[3];
        hsv = Color.RGBtoHSB(red, green, blue, null);
        return hsv;
    }

    public static float[] rgb2hsv(int red, int green, int blue) {
        return toHSV(red, green, blue);
    }

    public static float[] toRGB(int hue, int sat, int val) {
        float[] rgb = new float[3];
        int n = Color.HSBtoRGB(hue, sat, val);
        return rgb;
    }

    public static BufferedImage rgb2gray(BufferedImage img) {
//        return toNewColorSpace(img, BufferedImage.TYPE_BYTE_GRAY);
        return toGrayLevel(img);
    }

    public static BufferedImage ocv_rgb2gray(BufferedImage img) {
        Mat rgbImage = ocv_img2Mat(img);
        Mat imageGray = new Mat();
        Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_BGR2GRAY);
        return ocv_mat2Img(imageGray);
    }

    public static BufferedImage ocv_rgb2gray(Mat rgbImage) {
        Mat imageGray = new Mat();
        Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_BGR2GRAY);
        return ocv_mat2Img(imageGray);
    }

    public static Mat ocv_rgb2grayMat(Mat rgbImage) {
        Mat imageGray = new Mat();
        Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_BGR2GRAY);
        return imageGray;
    }

    public static BufferedImage ocv_mat2Img(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public static Mat ocv_img2Mat(BufferedImage in) {
        if (in.getType() == BufferedImage.TYPE_INT_RGB) {
            Mat out;
            byte[] data;
            int r, g, b;
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            }
            return out;
        }

        byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
        Mat mat = null;
        if (in.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            mat = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
        } else {
            mat = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
        }
        mat.put(0, 0, pixels);
        return mat;
    }

    public static double[][] rgb2gray2D(BufferedImage img) {
//        BufferedImage retImg = toNewColorSpace(img, BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage retImg = toGrayLevel(img);
        double[][] ret = imageToPixelsDouble(retImg);
        return ret;
    }

    private static void yaz(int[] p) {
        for (int i = 0; i < p.length; i++) {
            System.out.println(p[i]);
        }
    }

    private static void yaz(String s) {
        System.out.println(s);
    }

    public static BufferedImage getHistogramImage(int[] lbp) {
        lbp = FactoryNormalization.normalizeMinMax(lbp);
        BufferedImage img = new BufferedImage(lbp.length * 10, 300, BufferedImage.TYPE_BYTE_GRAY);
        Graphics gr = img.getGraphics();
        gr.setColor(Color.white);
        int w = img.getWidth();
        int h = img.getHeight();
        int a = 20;
        gr.drawRect(a, a, w - 2 * a, h - 2 * a);
        return img;
    }

//    public static BufferedImage getHistogramImage(BufferedImage imgx) {
//        int[] hist=getHistogramData(imgx);
//        BufferedImage img = new BufferedImage(hist.length * 10, 300, BufferedImage.TYPE_BYTE_GRAY);
//        Graphics gr = img.getGraphics();
//        gr.setColor(Color.white);
//        int w = img.getWidth();
//        int h = img.getHeight();
//        int a = 20;
//        gr.drawRect(a, a, w - 2 * a, h - 2 * a);
//        return img;
//    }
    public static int[] getHistogram(BufferedImage imgx) {
        int[] d = imageToPixelsTo1D(imgx);
        int[] ret = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

//    public static CMatrix getHistogram(BufferedImage imgx) {
//        CMatrix cm;
//        int[] d = imageToPixels255_1D(imgx);
//        int[] returnedValue = getHistogram(d);
//        cm = CMatrix.getInstance(returnedValue);
//        return cm;
//    }
    /**
     * 8 bit gray level histogram
     *
     * @param d
     * @return
     */
    public static int[] getHistogram(int[] d) {
        int[] ret = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    /**
     * N bins of histogram
     *
     * @param d
     * @return
     */
    public static int[] getHistogram(int[] d, int N) {
        int[] ret = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    public static int[] getHistogram(double[][] p) {
        double[] d = FactoryUtils.toDoubleArray1D(p);
        int[] ret = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < d.length; j++) {
                if ((int) d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    public static int[] getHistogram(int[][] p) {
        int[] d = FactoryUtils.toIntArray1D(p);
        int[] ret = new int[256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < d.length; j++) {
                if ((int) d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    public static CMatrix getHistogramRed(CMatrix cm) {
        int[] d = cm.getRedChannelColor().toIntArray1D();
        int[] ret = ImageProcess.getHistogram(d);
        cm = CMatrix.getInstance(ret).transpose();
        return cm;
    }

    public static CMatrix getHistogramGreen(CMatrix cm) {
        int[] d = cm.getGreenChannelColor().toIntArray1D();
        int[] ret = ImageProcess.getHistogram(d);
        cm = CMatrix.getInstance(ret).transpose();
        return cm;
    }

    public static CMatrix getHistogramBlue(CMatrix cm) {
        int[] d = cm.getBlueChannelColor().toIntArray1D();
        int[] ret = ImageProcess.getHistogram(d);
        cm = CMatrix.getInstance(ret).transpose();
        return cm;
    }

    public static CMatrix getHistogramAlpha(CMatrix cm) {
        int[] d = cm.getAlphaChannelColor().toIntArray1D();
        int[] ret = ImageProcess.getHistogram(d);
        cm = CMatrix.getInstance(ret).transpose();
        return cm;
    }

    public static CMatrix getHistogram(CMatrix cm) {
        if (cm.getImage().getType() == BufferedImage.TYPE_BYTE_GRAY) {
            short[] d = cm.toShortArray1D();
            int[] ret = FactoryMatrix.getHistogram(d, 256);
            cm.setArray(ret);
            cm = cm.transpose();
        } else {
            int[][][] d = imageToPixelsColorInt(cm.getImage());
            int[][] ret = FactoryMatrix.getHistogram(d, 256);
            cm.setArray(ret);
        }
        cm.name += "|" + "Histogram";
        return cm;
    }

    public static BufferedImage revert(BufferedImage img) {
        BufferedImage ret = null;
        int[][] d = imageToPixelsInt(img);
        int[][] q = new int[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                q[i][j] = Math.abs(255 - d[i][j]);
            }
        }
        ret = ImageProcess.pixelsToImageGray(q);
        return ret;
    }

    /**
     * resize the image to desired width and height value by using
     * Image.SCALE_SMOOTH format
     *
     * @param img:BufferedImage
     * @param w:width
     * @param h:height
     * @return
     */
    public static BufferedImage resize(BufferedImage src, int w, int h) {
////        Image tmp = img.getScaledInstance(w, h, Image.SCALE_FAST);
//        Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
////        Image tmp = img.getScaledInstance(w, h, Image.SCALE_REPLICATE);
//        BufferedImage dimg = new BufferedImage(w, h, img.getType());
//        Graphics2D g2d = dimg.createGraphics();
//        g2d.drawImage(tmp, 0, 0, null);
//        g2d.dispose();
//        return dimg;

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        g2 = img.createGraphics();
        g2.drawImage(resizedImg, 0, 0, w, h, null);
        g2.dispose();
        return img;
    }
    
    public static BufferedImage resizeSmooth(BufferedImage src, int w, int h) {
        Image img = src.getScaledInstance(w, h ,BufferedImage.SCALE_SMOOTH);
        BufferedImage ret=toBufferedImage(img);
        return ret;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     *
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    public static BufferedImage resizeAspectRatio(BufferedImage src, int w, int h) {
        int finalw = w;
        int finalh = h;
        double factor = 1.0d;
        if (src.getWidth() > src.getHeight()) {
            factor = ((double) src.getHeight() / (double) src.getWidth());
            finalh = (int) (finalw * factor);
        } else {
            factor = ((double) src.getWidth() / (double) src.getHeight());
            finalw = (int) (finalh * factor);
        }

        BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();

        BufferedImage img = new BufferedImage(finalw, finalh, BufferedImage.TYPE_3BYTE_BGR);
        g2 = img.createGraphics();
        g2.drawImage(resizedImg, 0, 0, finalw, finalh, null);
        g2.dispose();

//        return resizedImg;
        return img;
    }

    public static BufferedImage rotateImage(BufferedImage img, double theta) {
        double radians = theta * Math.PI / 180;
        AffineTransform transform = new AffineTransform();
        transform.rotate(radians, img.getWidth() / 2, img.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        img = op.filter(img, newImage);
        return newImage;
    }

    public static BufferedImage rotateImage(BufferedImage img, CPoint cp, double theta) {
        double radians = theta * Math.PI / 180;
        AffineTransform transform = new AffineTransform();
        transform.rotate(radians, cp.column, cp.row);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        img = op.filter(img, newImage);
        return newImage;
    }

    public static ArrayList<TRoi> getSpecialROIPositions(BufferedImage img) {
        ArrayList<TRoi> pos = new ArrayList<>();
        int[][] m = imageToPixelsInt(img);
        m = binarizeImage(m);
        m = FactoryUtils.transpose(m);
        int hImg = m.length;
        //each page of Quran is made up of 15 rows
        int hRow = hImg / 15;
        for (int i = 0; i < 15; i++) {
            addPosForEachRow(m, pos, i, hRow);
        }
        return pos;
    }

    private static int[][] binarizeImage(int[][] m) {
        int[][] ret = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                if (m[i][j] > 0) {
                    ret[i][j] = 255;
                }
            }
        }
        return ret;
    }

    private static void addPosForEachRow(int[][] m, ArrayList<TRoi> pl, int n, int hRow) {
        CPoint p1 = new CPoint(n * hRow, 0);
        CPoint p2 = new CPoint(n * hRow + hRow, m[0].length);
        int[][] subMatrix = FactoryUtils.getSubMatrix(m, p1, p2);
        int[] prjMatrix = FactoryUtils.getProjectedMatrixOnX(subMatrix);
        TWord[] points = FactoryUtils.getPoints(prjMatrix);
        for (int i = 0; i < points.length; i++) {
            Point p = new Point(n * hRow + hRow / 2, points[i].centerPos);
            TRoi roi = new TRoi();
            roi.p = p;
            roi.width = points[i].width;
            pl.add(roi);
            //yaz((n + 1) + ".satır da "+(i+1)+". yer"+" pos:"+p.x+","+p.y);
        }
    }

    private static double checkWindowDensity(int[][] m, int x, int y, int dx) {
        int w = 30;
        int h = 30 - dx;
        CPoint p1 = new CPoint(x, y);
        CPoint p2 = new CPoint(x + h, y + w);
        int[][] subMatrix = FactoryUtils.getSubMatrix(m, p1, p2);
//        double mean = Utils.getMeanValue(subMatrix);
        double mean = FactoryUtils.getPixelCount(subMatrix);
        yaz("roi pos:" + x + "," + y + " mean:" + mean);
        return mean;
    }

    /**
     * 16.04.2014 Musa Ataş Bir imgenin çerisinde küçük bir imgeyi correlation
     * coefficient tabanlı arar, en iyi correlation coefficient değerine sahip
     * koordinatı geri gönderir.
     */
    public static Point getPositionOfSubImageFromParentImage(int[][] parentImg, int[][] subImg, String filterType) {
        Point ret = new Point();
        ArrayList<TGrayPixel> cr = null;
        if (filterType.equals("DirectCorrelationBased")) {
            cr = performConvolveOperationForCorrelation(parentImg, subImg);
        }
        if (filterType.equals("LBP")) {
            cr = performConvolveOperationForLBP(parentImg, subImg);
        }
        for (int i = 0; i < 10; i++) {
            FactoryUtils.yaz(cr.get(i).toString());
        }
        return ret;
    }

    private static double[] to1D(int[][] parentImg) {
        int index = -1;
        double[] d = new double[parentImg.length * parentImg[0].length];
        for (int i = 0; i < parentImg.length; i++) {
            for (int j = 0; j < parentImg[0].length; j++) {
                d[++index] = parentImg[i][j] * 1.0;
            }
        }
        return d;
    }

    private static ArrayList<TGrayPixel> performConvolveOperationForCorrelation(int[][] parentImg, int[][] subImg) {
        return getPossiblePixelsAfterConvolution(parentImg, subImg, "PearsonCorrelationCoefficient");
    }

    private static ArrayList<TGrayPixel> performConvolveOperationForLBP(int[][] parentImg, int[][] subImg) {
        return getPossiblePixelsAfterConvolution(parentImg, subImg, "LBP");
    }

    public static ArrayList<TGrayPixel> getPossiblePixelsAfterConvolution(int[][] parentImg, int[][] subImg, String filterType) {
        ArrayList<TGrayPixel> ret = new ArrayList<TGrayPixel>();
        int pw = parentImg.length;
        int ph = parentImg[0].length;

        int sw = subImg.length;
        int sh = subImg[0].length;
        FactoryUtils.yazln("pw:" + pw + " ph:" + ph + " sw:" + sw + " sh:" + sh);
        double[] m2 = to1D(subImg);
        double[] m1 = null;
        for (int i = sw / 2; i < pw - sw; i++) {
            for (int j = sh / 2; j < ph - sh; j++) {
                //Utils.yaz("i:"+i+" j:"+j);
                int[][] d = FactoryUtils.getSubMatrix(parentImg, new CPoint(i, j), new CPoint(i + sw, j + sh));
                m1 = to1D(d);
                double cor = 0;
                if (filterType.equals("PearsonCorrelationCoefficient")) {
                    cor = FactoryStatistic.PEARSON(m1, m2);
                }
                if (filterType.equals("LBP")) {
                    int[] subImgLBP = FeatureExtractionLBP.getLBP(subImg, true);
                    int[] parentImgLBP = FeatureExtractionLBP.getLBP(d, true);
                    //cor = Distance.getCorrelationCoefficientDistance(Utils.to2DArrayDouble(subImgLBP),Utilto2DArrayDoubleay(parentImgLBP));
                    cor = FactorySimilarityDistance.getEuclideanDistance(FactoryUtils.toDoubleArray1D(subImgLBP), FactoryUtils.toDoubleArray1D(parentImgLBP));
                }
//                if (cor > 0.85) {
//                    Utils.yazln("high Correlation:" + cor + " coordinates:" + i + ":" + j);
//                    Utils.yaz("M1=");
//                    Utils.yaz(m1);
//                    Utils.yaz("M2=");
//                    Utils.yaz(m2);
//                }

                TGrayPixel gp = new TGrayPixel();
                gp.corValue = cor;
                gp.x = i;
                gp.y = j;
                ret.add(gp);
            }
        }
        Collections.sort(ret, new CustomComparatorForGrayPixelCorrelation());
        return ret;
    }

    public static int[][] getAbsoluteMatrixDifferenceWithROI(int[][] m_prev, int[][] m_curr, Rectangle r) {
        int[][] ret = new int[r.width][r.height];
        int k = 0;
        int l = 0;
        for (int i = r.x; i < r.x + r.width - 1; i++) {
            l = 0;
            for (int j = r.y; j < r.y + r.height - 1; j++) {
                int a = Math.abs(m_prev[i][j] - m_curr[i][j]);
                ret[k][l++] = (a < 20) ? 0 : a;
            }
            k++;
        }
        return ret;
    }

    public static int[][] segmentImageDifference(int[][] m, int size) {
        int w = m.length;
        int h = m[0].length;
        int nx = w / size;
        int ny = h / size;
        int[][] segM = new int[nx][ny];
        int[][] v = new int[size][size];
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                v = cropMatrix(m, new Rectangle(i * size, j * size, size, size));
                segM[i][j] = (int) FactoryUtils.getMean(v);
            }
        }
        return segM;
    }

    public static int[][] cropMatrix(int[][] m, Rectangle r) {
        int[][] ret = new int[r.width][r.height];
        int k = 0;
        int l = 0;
        for (int i = r.x; i < r.x + r.width - 1; i++) {
            l = 0;
            for (int j = r.y; j < r.y + r.height - 1; j++) {
                ret[k][l++] = m[i][j];
            }
            k++;
        }
        return ret;
    }

    public static CPoint getCenterOfGravityGray(BufferedImage img) {
        int[][] m = imageToPixelsInt(img);
        return ImageProcess.getCenterOfGravityGray(m);
    }

    public static CPoint getCenterOfGravityGray(BufferedImage img, boolean isShowCenter) {
        int[][] m = null;
        if (img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            BufferedImage temp = rgb2gray(img);
            m = imageToPixelsInt(temp);
        } else {
            m = imageToPixelsInt(img);
        }
        CPoint cp = ImageProcess.getCenterOfGravityGray(m);
        if (isShowCenter) {
            img = fillRectangle(img, cp.row - 2, cp.column - 2, 5, 5, Color.black);
        }
        return cp;
    }

    public static CPoint getCenterOfGravityColor(BufferedImage img, boolean isShowCenter) {
        int[][][] m = null;
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            System.err.println("You should not use gray level image for this particular case");
            return new CPoint();
        } else {
            m = imageToPixelsColorInt(img);
        }

        BufferedImage red = ImageProcess.getRedChannelGray(img);
        BufferedImage green = ImageProcess.getGreenChannelGray(img);
        BufferedImage blue = ImageProcess.getRedChannelGray(img);
        CPoint cpRed = ImageProcess.getCenterOfGravityGray(red);
        CPoint cpGreen = ImageProcess.getCenterOfGravityGray(green);
        CPoint cpBlue = ImageProcess.getCenterOfGravityGray(blue);
        CPoint cp = new CPoint();
        cp.row = (int) ((cpRed.row + cpGreen.row + cpBlue.row) / 3.0);
        cp.column = (int) ((cpRed.column + cpGreen.column + cpBlue.column) / 3.0);
        if (isShowCenter) {
            img = fillRectangle(img, cp.row - 2, cp.column - 2, 5, 5, Color.red);
        }
        return cp;
    }

    public static CPoint getCenterOfGravityGray(int[][] m) {
        int w = m.length;
        int h = m[0].length;
        int sumX = 0;
        int sumY = 0;
        int np = 0;
        CPoint cp = new CPoint();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (m[i][j] > 0) {
                    sumX += i;
                    sumY += j;
                    np++;
                }
            }
        }
        double pr = 0;
        double pc = 0;
        if (np != 0) {
            pr = sumX * 1.0 / np;
            pc = sumY * 1.0 / np;
            cp.row = (int) pr;
            cp.column = (int) pc;
        }
        return cp;
    }

    public static int[][] getCenterOfGravityCiz(int[][] m) {
        int w = m.length;
        int h = m[0].length;
        int[][] ret = new int[w][h];
        int sumX = 0;
        int sumY = 0;
        int np = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (m[i][j] > 0) {
                    sumX += i;
                    sumY += j;
                    np++;
                }
            }
        }
        //avoid division by zero
        double px = 0;
        double py = 0;
        if (np != 0) {
            px = sumX * 1.0 / np;
            py = sumY * 1.0 / np;
            ret[(int) px][(int) py] = 255;
            //writeToFile((int) px * zoom, 400 - (int) py * zoom, ++ID);
        }
        return ret;
    }

    public static CPoint getCenterOfGravityGray(double[][] m) {
        int w = m.length;
        int h = m[0].length;
        int sumX = 0;
        int sumY = 0;
        int np = 0;
        CPoint cp = new CPoint();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (m[i][j] > 0) {
                    sumX += i;
                    sumY += j;
                    np++;
                }
            }
        }
        double pr = 0;
        double pc = 0;
        if (np != 0) {
            pr = sumX * 1.0 / np;
            pc = sumY * 1.0 / np;
            cp.row = (int) pr;
            cp.column = (int) pc;
        }
        return cp;
    }

    public static double getInverseDiffMoment(int[][] img) {
        int col = img.length;
        int row = img[0].length;
        double IDF = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                IDF += img[i][j] / (1 + (i - j) * (i - j));
            }
        }
        return IDF;
    }

    public static double getInverseDiffMoment(BufferedImage img) {
        int[][] d = imageToPixelsInt(img);
        return getInverseDiffMoment(d);
    }

    public static double getInverseDiffMoment(double[][] img) {
        int col = img.length;
        int row = img[0].length;
        double IDF = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                IDF += img[i][j] / (1 + (i - j) * (i - j));
            }
        }
        return IDF;
    }

    public static double getContrast(BufferedImage img) {
        int[][] d = imageToPixelsInt(img);
        return getContrast(d);
    }

    public static double getContrast(int[][] img) {
        int col = img.length;
        int row = img[0].length;
        double contrast = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                contrast += img[i][j] * ((i - j) * (i - j));
            }
        }
        return contrast;
    }

    public static double getContrast(double[][] img) {
        int col = img.length;
        int row = img[0].length;
        double contrast = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                contrast += img[i][j] * ((i - j) * (i - j));
            }
        }
        return contrast;
    }

    public static double getEntropy(int[][] img) {
        int col = img.length;
        int row = img[0].length;
        double entropy = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                entropy += img[i][j] * Math.log(img[i][j]);
            }
        }
        return entropy;
    }

    public static double getEntropy(double[][] img) {
        int col = img[0].length;
        int row = img.length;
        double entropy = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                entropy += img[i][j] * Math.log(img[i][j] + 1);
            }
        }
        return entropy;
    }

    public static double getEntropy(BufferedImage actualImage) {
        ArrayList<String> values = new ArrayList<String>();
        int n = 0;
        Map<Integer, Integer> occ = new HashMap<>();
        for (int i = 0; i < actualImage.getHeight(); i++) {
            for (int j = 0; j < actualImage.getWidth(); j++) {
                int pixel = actualImage.getRGB(j, i);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                //0.2989 * R + 0.5870 * G + 0.1140 * B greyscale conversion
//System.out.println("i="+i+" j="+j+" argb: " + alpha + ", " + red + ", " + green + ", " + blue);
                int d = (int) Math.round(0.2989 * red + 0.5870 * green + 0.1140 * blue);
                if (!values.contains(String.valueOf(d))) {
                    values.add(String.valueOf(d));
                }
                if (occ.containsKey(d)) {
                    occ.put(d, occ.get(d) + 1);
                } else {
                    occ.put(d, 1);
                }
                ++n;
            }
        }
        double e = 0.0;
        for (Map.Entry<Integer, Integer> entry : occ.entrySet()) {
            int cx = entry.getKey();
            double p = (double) entry.getValue() / n;
            e += p * Math.log(p);
        }
        return -e;
    }

    public static double getEntropyWithHistogram(double[][] d) {
        int[] hist = ImageProcess.getHistogram(d);
        double sum = FactoryUtils.sum(hist);
        double e = 0.0;
        for (int h : hist) {
            double p = h / sum;
            if (p > 0) {
                e += p * Math.log(p);
            }
        }
        return -e;
    }

    /**
     * yanlış hesaplıyor güncellenmesi gerekir
     *
     * @param d
     * @return
     */
    public static double getHomogeneity(double[][] d) {
        //aşağıdaki kod yanlış update edilmesi gerekiyor
        int[] hist = ImageProcess.getHistogram(d);
        double sum = FactoryUtils.sum(hist);
        double e = 0.0;
        for (int h : hist) {
            double p = h / sum;
            if (p > 0) {
                e += p * Math.log(p);
            }
        }
        return -e;
    }

    public static double getEnergy(int[][] img) {
        int col = img.length;
        int row = img[0].length;
        double energy = 0.0d;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                energy += img[i][j] * img[i][j];
            }
        }
        return energy;
    }

    public static double getKurtosis(int[][] img) {
        int[] nums = FactoryUtils.toIntArray1D(img);
        int n = nums.length;
        double mean = FactoryUtils.getMean(nums);
        double deviation = 0.0d;
        double variance = 0.0d;
        double k = 0.0d;

        for (int i = 0; i < n; i++) {
            deviation = nums[i] - mean;
            variance += Math.pow(deviation, 2);
            k += Math.pow(deviation, 4);
        }
        //variance /= (n - 1);
        variance = variance / n;
        if (variance != 0.0) {
            //k = k / (n * variance * variance) - 3.0;
            k = k / (n * variance * variance);
        }
        return k;
    }

    public static double getSkewness(int[][] img) {
        int[] nums = FactoryUtils.toIntArray1D(img);
        int n = nums.length;
        double mean = FactoryUtils.getMean(nums);
        double deviation = 0.0d;
        double variance = 0.0d;
        double skew = 0.0d;

        for (int i = 0; i < n; i++) {
            deviation = nums[i] - mean;
            variance += Math.pow(deviation, 2);
            skew += Math.pow(deviation, 3);
        }
        //variance /= (n - 1);
        variance /= n;
        double standard_deviation = Math.sqrt(variance);
        if (variance != 0.0) {
            skew /= (n * variance * standard_deviation);
        }
        return skew;
    }

//    public static BufferedImage clone(BufferedImage img) {
//        BufferedImage ret = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
//        ret.getGraphics().drawImage(img, 0, 0, null);
//        return ret;
//    }
    public static BufferedImage clone(BufferedImage img) {
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(img.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static Image clone(Image img) {
        BufferedImage bf = ImageProcess.toBufferedImage(img);
        BufferedImage ret = new BufferedImage(bf.getWidth(), bf.getHeight(), bf.getType());
        ret.getGraphics().drawImage(img, 0, 0, null);
        return ret;
    }

    public static int[] getImagePixels(BufferedImage image) {
        int[] dummy = null;
        int wid, hgt;

        // compute size of the array
        wid = image.getWidth();
        hgt = image.getHeight();

        // start getting the pixels
        Raster pixelData;
        pixelData = image.getData();

        System.out.println("wid:" + wid);
        System.out.println("hgt:" + hgt);
        System.out.println("Channels:" + pixelData.getNumDataElements());
        return pixelData.getPixels(0, 0, wid, hgt, dummy);
    }

    public static int[][] to2DRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = image.getRGB(col, row);
            }
        }

        return result;
    }

    public static int[][] to2D(BufferedImage image) {
//        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        //image = new ImageIcon(image).getImage();
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);
        boolean hasAlpha = false;

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static BufferedImage imread() {
        return readImageFromFile();
    }

    public static BufferedImage readImageFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("images"));
        chooser.setDialogTitle("select Data Set file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        File file;
        BufferedImage ret = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                ret = ImageIO.read(file);
            } catch (IOException ex) {
                //Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    public static BufferedImage readImageFromFile(File file) {
        BufferedImage ret = null;
        try {
            ret = ImageIO.read(file);
        } catch (IOException ex) {
            //Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static File readImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("images"));
        chooser.setDialogTitle("select Data Set file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        File file = null;
        BufferedImage ret = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                ret = ImageIO.read(file);
            } catch (IOException ex) {
                //Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    public static File readImageFileFromFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("images"));
        chooser.setDialogTitle("select Data Set file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        File file = null;
        BufferedImage ret = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        return file;
    }

    public static File readImageFileFromFolderWithDirectoryPath(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return readImageFileFromFolder();
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(directoryPath));
        chooser.setDialogTitle("select Data Set file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        File file = null;
        BufferedImage ret = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        return file;
    }

    public static BufferedImage readImageFromFileWithDirectoryPath(String directoryPath) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(directoryPath));
        chooser.setDialogTitle("select Data Set file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        File file;
        BufferedImage ret = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            try {
                ret = ImageIO.read(file);
            } catch (IOException ex) {
                //Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

    public static BufferedImage imread(String fileName) {
        return readImageFromFile(fileName);
    }

    public static BufferedImage readImageFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        BufferedImage ret = null;
        try {
            ret = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, "Problem reading " + fileName + "\n" + ex);
        }
        return ret;
    }

    public static CMatrix getMatrix(BufferedImage img) {
        CMatrix cm = CMatrix.getInstance(imageToPixelsInt(img));
        return cm;
    }

    public static boolean writeImage(BufferedImage img) {
        return saveImage(img);
    }

    public static boolean writeImage(BufferedImage img, String fileName) {
        return saveImage(img, fileName);
    }

    public static boolean imwrite(BufferedImage img) {
        return saveImage(img);
    }

    public static boolean imwrite(BufferedImage img, String fileName) {
        return saveImage(img, fileName);
    }

    public static boolean saveImage(BufferedImage img) {
        JFileChooser FC = new JFileChooser("C:/");
        int retrival = FC.showSaveDialog(null);
        if (retrival == FC.APPROVE_OPTION) {
            File fileToSave = FC.getSelectedFile();
            String extension = FactoryUtils.getFileExtension(fileToSave);
            try {
                boolean ret = ImageIO.write(img, extension, fileToSave);
                return ret;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean saveImage(BufferedImage img, String fileName) {
        File file = new File(fileName);
        String extension = FactoryUtils.getFileExtension(fileName);
        boolean ret = false;
        try {
            ret = ImageIO.write(img, extension, file);
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static void saveImageWithFormat(BufferedImage img, String path, String fileType) {
        File outputFile = new File(path);
        try {
            ImageIO.write(img, fileType, outputFile);
        } catch (IOException ex) {
            Logger.getLogger(ImageProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean ocv_saveImageWithFormat(BufferedImage img, String path) {
        Mat imgM = ocv_img2Mat(img);
        return Imgcodecs.imwrite(path, imgM);
    }

    public static Mat ocv_saveImage(BufferedImage img, String path) {
        Mat imgM = ocv_img2Mat(img);
        Imgcodecs.imwrite(path, imgM);
        return imgM;
    }

    public static BufferedImage ocv_imRead(String path) {
        Mat imgM = Imgcodecs.imread(path);
        return ocv_mat2Img(imgM);
    }

    public static void saveGridImage(BufferedImage gridImage, String filePath) {
        File output = new File(filePath);
        output.delete();

        final String formatName = "png";

        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
            try {
                ImageWriter writer = iw.next();
                ImageWriteParam writeParam = writer.getDefaultWriteParam();
                ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
                IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
                if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                    continue;
                }

                try {
                    setDPI(metadata, 300);

                } catch (IIOInvalidTreeException ex) {
                    Logger.getLogger(FactoryUtils.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
                try {
                    writer.setOutput(stream);
                    writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam);
                } finally {
                    stream.close();
                }
                break;

            } catch (IOException ex) {
                Logger.getLogger(FactoryUtils.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void setDPI(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {

        // for PMG, it's dots per millimeter inc to cm=2.54
//        double dotsPerMilli = 1.0 * DPI / 10 / 2.54;
        double dotsPerMilli = 1.0 * DPI;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }

    public static BufferedImage getBufferedImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }

    public static BufferedImage saveImageAsJPEG(String filePath, BufferedImage currBufferedImage, int k) {
        System.out.println("New Image Captured and jpg file saved");
        try {
            if (currBufferedImage == null) {
                return null;
            }
            File file = new File(filePath + "\\img_" + k + ".jpg");
            ImageIO.write(currBufferedImage, "jpg", file);
            BufferedImage myImage = ImageIO.read(file);
            currBufferedImage = myImage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return currBufferedImage;
    }

    public static BufferedImage getAlphaChannelGray(BufferedImage bf) {
        return rgb2gray(getAlphaChannelColor(bf));
    }

    public static BufferedImage getRedChannelGray(BufferedImage bf) {
        return rgb2gray(getRedChannelColor(bf));
    }

    public static BufferedImage getGreenChannelGray(BufferedImage bf) {
        return rgb2gray(getGreenChannelColor(bf));
    }

    public static BufferedImage getBlueChannelGray(BufferedImage bf) {
        return rgb2gray(getBlueChannelColor(bf));
    }

    public static BufferedImage getAlphaChannelColor(BufferedImage bf) {
        int[][][] d = imageToPixelsColorInt(bf);
        int[][] ret = new int[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][0];
            }
        }
        return pixelsToImageColor(d);
    }

    public static BufferedImage getRedChannelColor(BufferedImage bf) {
        int[][][] d = imageToPixelsColorInt(bf);
        int[][] ret = new int[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][1];
            }
        }
        return pixelsToImageColor(d);
    }

    public static BufferedImage getGreenChannelColor(BufferedImage bf) {
        int[][][] d = imageToPixelsColorInt(bf);
        int[][] ret = new int[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][2];
            }
        }
        return pixelsToImageColor(d);
    }

    public static BufferedImage getBlueChannelColor(BufferedImage bf) {
        int[][][] d = imageToPixelsColorInt(bf);
        int[][] ret = new int[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][2];
            }
        }
        return pixelsToImageColor(d);
    }

    public static double[][] getRedChannelDouble(double[][][] d) {
        double[][] ret = new double[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][1];
            }
        }
        return ret;
    }

    public static double[][] getGreenChannelDouble(double[][][] d) {
        double[][] ret = new double[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][2];
            }
        }
        return ret;
    }

    public static double[][] getBlueChannelDouble(double[][][] d) {
        double[][] ret = new double[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[i][j] = d[i][j][3];
            }
        }
        return ret;
    }

    public static CMatrix getHistogramRGB(BufferedImage image) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static BufferedImage filterMedian(BufferedImage imgx) {
        return filterMedian(imgx, 3);
    }

    public static BufferedImage filterGaussian(BufferedImage imgx, int size) {
        GaussianFilter gaussianFilter = new GaussianFilter(size);
        BufferedImage gaussianFiltered = clone(imgx);
        gaussianFilter.filter(imgx, gaussianFiltered);
        return gaussianFiltered;
    }

    public static BufferedImage filterMedian(BufferedImage imgx, int size) {
        int w = imgx.getHeight(null);
        int h = imgx.getWidth(null);
        int[] kernel = new int[size * size];
        int dizi[][] = new int[w][h];
        int temp[][] = new int[w][h];
        dizi = imageToPixelsInt(imgx);
        temp = (int[][]) dizi.clone();
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                int f = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int t = -1; t <= 1; t++) {
                        kernel[f] = dizi[i + k][j + t];
                        f++;
                    }
                }
                temp[i][j] = medianKernel(kernel);
            }
        }
        dizi = (int[][]) temp.clone();
        BufferedImage ret_img = ImageProcess.pixelsToImageGray(dizi);
        return ret_img;
    }

    public static int medianKernel(int[] kernel) {
        int buffer;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = i; j < kernel.length; j++) {
                if (kernel[j] > kernel[i]) {
                    buffer = kernel[j];
                    kernel[j] = kernel[i];
                    kernel[i] = buffer;
                }
            }
        }
        int mid = kernel.length / 2;
        return kernel[mid];
    }

    public static BufferedImage filterMean(BufferedImage imgx) {
        return ImageProcess.filterMean(imgx, 3);
    }

    public static double[][] filterMean(double[][] d) {
        double[][] ret = imageToPixelsDouble(filterMean(pixelsToImageGray(d), 3));
        return ret;
    }

    public static BufferedImage filterMean(BufferedImage imgx, int size) {
        int w = imgx.getWidth(null);
        int h = imgx.getHeight(null);
        int[] kernel = new int[size * size];
        int dizi[][] = new int[w][h];
        int temp[][] = new int[w][h];
        dizi = imageToPixelsInt(imgx);
        temp = FactoryMatrix.clone(dizi);
        int sum = 0;
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                int f = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int t = -1; t <= 1; t++) {
                        kernel[f] = dizi[i + k][j + t];
                        sum += kernel[f];
                        f++;
                    }
                }
//                if (sum / 9 > 0) {
//                    System.out.println("org:" + temp[i][j] + " avg:" + (sum / (size * size)) + " r,c:" + j + ":" + i);
//                }
                temp[i][j] = sum / (size * size);
                sum = 0;
            }
        }
        int[][] d = FactoryMatrix.clone(temp);
        BufferedImage ret_img = ImageProcess.pixelsToImageGray(d);
        return ret_img;
    }

//    public static double[][] meanFilter(double[][] imgx) {
//        int r = imgx.length;
//        int c = imgx[0].length;
//        int[] kernel = new int[9];
//        double temp[][] = new double[r][c];
//        temp = FactoryMatrix.clone(imgx);
//        int sum = 0;
//        for (int row = 1; row < r - 1; row++) {
//            for (int column = 1; column < c - 1; column++) {
//                int f = 0;
//                for (int k = -1; k <= 1; k++) {
//                    for (int t = -1; t <= 1; t++) {
//                        kernel[f] = (int) imgx[row + k][column + t];
//                        sum += kernel[f];
//                        f++;
//                    }
//                }
//                temp[row][column] = (int) (sum / 9.0);
//                sum = 0;
//            }
//        }
//        return temp;
//    }
    public static BufferedImage erode(BufferedImage img, int[][] kernel) {
        BufferedImage imgx = clone(img);
        int w = imgx.getWidth(null);
        int h = imgx.getHeight(null);
        int dizi[][] = new int[h][w];
        int temp[][] = new int[h][w];
        dizi = imageToPixelsInt(imgx);
        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                if (dizi[i - 1][j - 1] == kernel[0][0] && dizi[i - 1][j] == kernel[0][1] && dizi[i - 1][j + 1] == kernel[0][2]
                        && dizi[i][j - 1] == kernel[1][0] && dizi[i][j] == kernel[1][1] && dizi[i][j + 1] == kernel[1][2]
                        && dizi[i + 1][j - 1] == kernel[2][0] && dizi[i + 1][j] == kernel[2][1] && dizi[i + 1][j + 1] == kernel[2][2]) {

                    temp[i][j] = 0;
                } else {
                    temp[i][j] = 255;
                }
            }
        }
        dizi = temp;
        BufferedImage ret_img = ImageProcess.pixelsToImageGray(dizi);
        return ret_img;
    }

    public static BufferedImage erode(BufferedImage imgx) {
        int[][] kernel = {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}};
        return dilate(imgx, kernel);
    }

    public static BufferedImage dilate(BufferedImage img, int[][] kernel) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int dizi[][] = new int[h][w];
        int temp[][] = new int[h][w];
        BufferedImage imgx = clone(img);
        dizi = imageToPixelsInt(imgx);
        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                if (dizi[i - 1][j - 1] == kernel[0][0] || dizi[i - 1][j] == kernel[0][1] || dizi[i - 1][j + 1] == kernel[0][2]
                        || dizi[i][j - 1] == kernel[1][0] || dizi[i][j] == kernel[1][1] || dizi[i][j + 1] == kernel[1][2]
                        || dizi[i + 1][j - 1] == kernel[2][0] || dizi[i + 1][j] == kernel[2][1] || dizi[i + 1][j + 1] == kernel[2][2]) {
                    temp[i][j] = 0;
                } else {
                    temp[i][j] = 255;
                }
            }
        }
        dizi = temp;
        BufferedImage ret_img = ImageProcess.pixelsToImageGray(dizi);
        return ret_img;
    }

    public static BufferedImage dilate(BufferedImage imgx) {
        int[][] kernel = {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}};
        return dilate(imgx, kernel);
    }

    public static BufferedImage kernelFilter(BufferedImage imgx) {
        int w = imgx.getWidth(null);
        int h = imgx.getHeight(null);
        int dizi[][] = new int[w][h];
        int kw = 15;
        int kh = 15;
        int med = kw / 2;

        int[][] kernel = new int[kw][kh];
        for (int i = 2; i < kw - 2; i++) {
            for (int j = 2; j < kh - 2; j++) {
                kernel[i][j] = 255;
            }
        }

        dizi = imageToPixelsInt(imgx);
        for (int i = med; i < w - med; i++) {
            for (int j = med; j < h - med; j++) {
                if (dizi[i - med][j - med] == 0 && dizi[i - med][j + med] == 0 && dizi[i + med][j - med] == 0 && dizi[i + med][j + med] == 0
                        && dizi[i][j] == 255 && dizi[i - 1][j - 1] == 255 && dizi[i - 1][j + 1] == 255 && dizi[i + 1][j - 1] == 255 && dizi[i + 1][j + 1] == 255) {
                    for (int k = 0; k < med; k++) {
                        for (int m = 0; m < med; m++) {
                            if (dizi[i + k][j + m] == 255) {
                                dizi[i + k][j + m] = 0;
                            }

                        }
                    }
                    //writeln("bulundu:"+i+","+j);
                }
            }
        }
        //dizi=temp;
        BufferedImage ret_img = ImageProcess.pixelsToImageGray(dizi);
        return ret_img;
    }

    /**
     * Get binary int treshold using Otsu's method
     */
    public static int getOtsuTresholdValue(BufferedImage original) {
        double[][] d = ImageProcess.imageToPixelsDouble(original);
        return getOtsuTresholdValue(d);
    }

    /**
     * Get binary int treshold using Otsu's method
     */
    public static int getOtsuTresholdValue(double[][] d) {
        int[] histogram = ImageProcess.getHistogram(d);
        int total = d.length * d[0].length;

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;

            if (wF == 0) {
                break;
            }

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        return threshold;
    }

    /**
     * Binarize Color Image with a given threshold value i.e. otsuThreshold
     *
     * @param original
     * @return
     */
    public static BufferedImage binarizeColorImage(BufferedImage original, int threshold) {
        int red;
        int newPixel;
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);

            }
        }
        return binarized;
    }

    /**
     * Binarize Color Image with a given threshold value i.e. otsuThreshold
     *
     * @param original
     * @return
     */
    public static BufferedImage binarizeColorImage(BufferedImage original) {
        int threshold = getOtsuTresholdValue(original);
        return binarizeColorImage(original, threshold);
    }

    /**
     * Binarize Image with a given threshold value hint: you can determine
     * threshold value from otsu approach and then pass as an argument
     *
     * @param original
     * @return
     */
    public static BufferedImage binarizeGrayScaleImage(BufferedImage original, int threshold) {
        int[][] d = imageToPixelsInt(original);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] > threshold) {
                    d[i][j] = 255;
                } else {
                    d[i][j] = 0;
                }
            }
        }
        BufferedImage binarized = ImageProcess.pixelsToImageGray(d);
        return binarized;
    }

    /**
     * Binarize Image with a given threshold value you can determine threshold
     * value from otsu approach and then pass as an argument
     *
     * @param original
     * @return
     */
    public static BufferedImage binarizeGrayScaleImage(BufferedImage original) {
        int threshold = getOtsuTresholdValue(original);
        return binarizeGrayScaleImage(original, threshold);
    }

    /**
     * Binarize Image with a given threshold value you can determine threshold
     * value from otsu approach
     *
     * @param original
     * @return
     */
    public static BufferedImage binarizeGrayScaleImage(double[][] d, int threshold) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] > threshold) {
                    d[i][j] = 255;
                } else {
                    d[i][j] = 0;
                }
            }
        }

        BufferedImage binarized = pixelsToImageGray(d);
        return binarized;
    }

    // Convert R, G, B, Alpha to standard 8 bit
    public static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

//    /**
//     * convert let say gray scale image to RGB or any other types
//     *
//     * @param img
//     * @param newType
//     * @return
//     */
//    public static BufferedImage toNewColorSpace(BufferedImage img, int newType) {
//        BufferedImage ret = new BufferedImage(img.getWidth(), img.getHeight(), newType);
//        ret.getGraphics().drawImage(img, 0, 0, null);
//        return ret;
//    }
//
    /**
     * convert let say gray scale image to RGB or any other types
     *
     * @param img
     * @param newType
     * @return
     */
    public static BufferedImage toBufferedImage(BufferedImage img, int newType) {
        BufferedImage ret = new BufferedImage(img.getWidth(), img.getHeight(), newType);
        ret.getGraphics().drawImage(img, 0, 0, null);
        return ret;
    }

    public static double[][] highPassFilter(double[][] m, int t) {
        double[][] d = FactoryMatrix.clone(m);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] < t) {
                    d[i][j] = 0;
                }
            }
        }
        return d;
    }

    public static double[][] lowPassFilter(double[][] m, int t) {
        double[][] d = FactoryMatrix.clone(m);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] > t) {
                    d[i][j] = 0;
                }
            }
        }
        return d;
    }

    public static double[][] swapColor(double[][] m, int c1, int c2) {
        double[][] d = FactoryMatrix.clone(m);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] == c1) {
                    d[i][j] = c2;
                }
            }
        }
        return d;
    }

    public static double[][] imageToPixels2DFromOpenCV(Mat m) {
        double[][] ret = new double[m.height()][m.width()];
        for (int i = 0; i < m.height(); i++) {
            for (int j = 0; j < m.width(); j++) {
                ret[i][j] = m.get(i, j)[0];
            }
        }
        return ret;
    }

//    public static Rectangle[] detectFacesRectangles(String type, BufferedImage img) {
//        
//        String xml = "";
//        if (type.equals("haar")) {
//            xml = "etc\\haarcascades\\haarcascade_frontalface_alt.xml";
////            xml = "etc\\haarcascades\\haarcascade_frontalface_alt_tree.xml";
//        }
//        if (type.equals("lbp")) {
//            xml = "etc\\lbpcascades\\lbpcascade_frontalface.xml";
//        }
////        System.out.println("xml_file = " + xml);
//        CascadeClassifier faceDetector = new CascadeClassifier(xml);
//        Mat imageGray = ocv_img2Mat(img);
//
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(imageGray, faceDetections);
////        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
//        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(imageGray, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height),
//                    new Scalar(0, 255, 255), 2);
//
//        }
//        return ocv_mat2Img(imageGray);
//    }
    public static BufferedImage drawRectangle(BufferedImage img, int x, int y, int w, int h, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawRect(y, x, w, h);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawLine(BufferedImage img, int r1, int c1, int r2, int c2, int thickness, Color color) {
        BufferedImage ret = null;
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(c1, r1, c2, r2);
        g2d.dispose();
        return img;
    }

    final public static BufferedImage toNewColorSpace(BufferedImage image, int newType) {
        BufferedImage ret = null;
        try {
            ret = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    newType);
            ColorConvertOp xformOp = new ColorConvertOp(null);
            xformOp.filter(image, ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static BufferedImage fillRectangle(BufferedImage img, int x, int y, int w, int h, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fillRect(y, x, w, h);
        g2d.dispose();
        return img;
    }

    public static BufferedImage draw3DRectangle(BufferedImage img, int x, int y, int w, int h, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.draw3DRect(y, x, w, h, true);
        g2d.dispose();
        return img;
    }

    public static BufferedImage fill3DRectangle(BufferedImage img, int x, int y, int w, int h, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fill3DRect(y, x, w, h, true);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawRoundRectangle(BufferedImage img, int x, int y, int w, int h, int arcw, int arch, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawRoundRect(y, x, w, h, arcw, arch);
        g2d.dispose();
        return img;
    }

    public static BufferedImage fillRoundRectangle(BufferedImage img, int x, int y, int w, int h, int arcw, int arch, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fillRoundRect(y, x, w, h, arcw, arch);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawOval(BufferedImage img, int x, int y, int w, int h, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawOval(y, x, w, h);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawShape(BufferedImage img, Shape p, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.draw(p);
        g2d.dispose();
        return img;
    }

    public static BufferedImage fillShape(BufferedImage img, Shape p, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fill(p);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawPolygon(BufferedImage img, Polygon p, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawPolygon(p);
        g2d.dispose();
        return img;
    }

    public static BufferedImage fillPolygon(BufferedImage img, Polygon p, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fillPolygon(p);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawArc(BufferedImage img, int x, int y, int w, int h, int startAngle, int arcAngle, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawArc(y, x, w, h, startAngle, arcAngle);
        g2d.dispose();
        return img;
    }

    public static BufferedImage drawPolyLine(BufferedImage img, int[] xPoints, int[] yPoints, int nPoints, int thickness, Color color) {
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setStroke(new BasicStroke(thickness));
        g2d.setColor(color);
        g2d.drawPolyline(yPoints, xPoints, nPoints);
        g2d.dispose();
        return img;
    }

    public static BufferedImage fillOval(BufferedImage img, int x, int y, int w, int h, Color color) {
        BufferedImage ret = null;
        if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            img = toNewColorSpace(img, BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(color);
        g2d.fillOval(y, x, w, h);
        g2d.dispose();
        return img;
    }

    public static BufferedImage detectFaces(String type, BufferedImage img) {
        String xml = "";
        if (type.equals("haar")) {
            xml = "etc\\haarcascades\\haarcascade_frontalface_alt.xml";
        }
        if (type.equals("lbp")) {
            xml = "etc\\lbpcascades\\lbpcascade_frontalface.xml";
        }
        BufferedImage img2 = ImageProcess.clone(img);
        CascadeClassifier faceDetector = new CascadeClassifier(xml);
        Mat imageGray = ocv_img2Mat(img2);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imageGray, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(imageGray, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height),
//                    new Scalar(0, 255, 255), 2);
            drawRectangle(img, rect.y - (int) (rect.height * 0.1), rect.x, rect.width, rect.height + (int) (rect.height * 0.2), 1, Color.yellow);

        }
//        return ocv_mat2Img(imageGray);
        return img;
    }

    public static BufferedImage detectFaces(String type, BufferedImage img, CRectangle r, boolean showRect) {
        String xml = "";
        if (type.equals("haar")) {
            xml = "etc\\haarcascades\\haarcascade_frontalface_alt.xml";
        }
        if (type.equals("lbp")) {
            xml = "etc\\lbpcascades\\lbpcascade_frontalface.xml";
        }
        BufferedImage img2 = ImageProcess.clone(img);
        CascadeClassifier faceDetector = new CascadeClassifier(xml);
        Mat imageGray = ocv_img2Mat(img2);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imageGray, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(imageGray, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height),
//                    new Scalar(0, 255, 255), 2);
            if (showRect) {
                drawRectangle(img, rect.y - (int) (rect.height * 0.1), rect.x, rect.width, rect.height + (int) (rect.height * 0.2), 1, Color.yellow);
            }
            r.row = rect.y - (int) (rect.height * 0.1);
            r.column = rect.x;
            r.width = rect.width;
            r.height = rect.height + (int) (rect.height * 0.2);

        }
//        return ocv_mat2Img(imageGray);
        return img;
    }

    public static Rectangle[] getFacesRectangles(String type, BufferedImage img) {
        String xml = "";
        if (type.equals("haar")) {
            xml = "etc\\haarcascades\\haarcascade_frontalface_alt.xml";
//            xml = "etc\\haarcascades\\haarcascade_frontalface_alt_tree.xml";
        }
        if (type.equals("lbp")) {
            xml = "etc\\lbpcascades\\lbpcascade_frontalface.xml";
        }
        BufferedImage img2 = ImageProcess.clone(img);
        CascadeClassifier faceDetector = new CascadeClassifier(xml);
        Mat imageGray = ocv_img2Mat(img2);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imageGray, faceDetections);
        Rect[] rects = faceDetections.toArray();
        Rectangle[] ret = new Rectangle[faceDetections.toArray().length];
        for (int i = 0; i < rects.length; i++) {
            Rect r = rects[i];
            ret[i] = new Rectangle(r.y, r.x, r.width, r.height);
        }
        return ret;
    }

    public static BufferedImage toARGB(BufferedImage image) {
        return toNewColorSpace(image, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage toBGR(BufferedImage image) {
        return toNewColorSpace(image, BufferedImage.TYPE_3BYTE_BGR);
    }

    public static BufferedImage toGrayLevel(BufferedImage image) {
//        return toNewColorSpace(image, BufferedImage.TYPE_BYTE_GRAY);
        return pixelsToImageGray(imageToPixelsDouble(image));
    }

    public static BufferedImage toBinary(BufferedImage image) {
        return toNewColorSpace(image, BufferedImage.TYPE_BYTE_BINARY);
    }

    public static BufferedImage flipVertical(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        BufferedImage bf = buildTransformed(image, at);

//        BufferedImage bf = new BufferedImage(
//                image.getWidth(), image.getHeight(),
//                BufferedImage.TYPE_3BYTE_BGR);
//        Graphics2D g = image.createGraphics();
//        g.drawImage(image , 0,0,-image.getWidth(),image.getHeight(),null);
        return bf;
    }

    public static BufferedImage flipHorizontal(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return buildTransformed(image, at);
    }

    public static BufferedImage buildTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage invertImage(BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
            image = toARGB(image);
        }
        LookupTable lookup = new LookupTable(0, 4) {
            @Override
            public int[] lookupPixel(int[] src, int[] dest) {
                dest[0] = (int) (255 - src[0]);
                dest[1] = (int) (255 - src[1]);
                dest[2] = (int) (255 - src[2]);
                return dest;
            }
        };
        LookupOp op = new LookupOp(lookup, new RenderingHints(null));
        return op.filter(image, null);
    }

    /**
     * if location and size are not given overlayed image is positioned at 0,0
     * white pixels are ignored through overlaying process
     *
     * @param bf
     * @param overlay
     * @return
     */
    public static BufferedImage overlayImage(BufferedImage bf, BufferedImage overlay) {
        int[][][] bfp = imageToPixelsColorInt(bf);
        int[][][] ovp = imageToPixelsColorInt(overlay);
        int r = ovp.length;
        int c = ovp[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (ovp[i][j][0] != 255 || ovp[i][j][1] != 255 || ovp[i][j][2] != 255 || ovp[i][j][3] != 255) {
                    for (int k = 0; k < 4; k++) {
                        bfp[i][j][k] = ovp[i][j][k];
                    }
                }
            }
        }
        return pixelsToImageColor(bfp);
    }

    /**
     * overlay image onto original image
     *
     * @param bf
     * @param overlay
     * @param rect
     * @param bkg
     * @return
     */
    public static BufferedImage overlayImage(BufferedImage bf, BufferedImage overlay, CRectangle rect, int bkg) {
        int[][][] bfp = imageToPixelsColorInt(bf);
        int[][][] ovp = imageToPixelsColorInt(overlay);
        int r1 = bfp.length;
        int c1 = bfp[0].length;
        int r = ovp.length;
        int c = ovp[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
//                if (ovp[i][j][0] != bkg || ovp[i][j][1] != bkg || ovp[i][j][2] != bkg || ovp[i][j][3] != bkg) {
                if (ovp[i][j][1] != bkg || ovp[i][j][2] != bkg || ovp[i][j][3] != bkg) {
                    for (int k = 1; k < 4; k++) {
                        if (i + rect.row > 0 && j + rect.column > 0 && i + rect.row < r1 && j + rect.column < c1) {
                            bfp[i + rect.row][j + rect.column][k] = ovp[i][j][k];
                        }
                    }
                }
            }
        }
        return pixelsToImageColor(bfp);
    }

    public static BufferedImage overlayImage(BufferedImage bf, BufferedImage overlay, CPoint cp, int bkg) {
        int[][][] bfp = imageToPixelsColorInt(bf);
        int[][][] ovp = imageToPixelsColorInt(overlay);
        int r1 = bfp.length;
        int c1 = bfp[0].length;
        int r = ovp.length;
        int c = ovp[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (ovp[i][j][1] != bkg || ovp[i][j][2] != bkg || ovp[i][j][3] != bkg) {
                    for (int k = 1; k < 4; k++) {
                        if (i + cp.row > 0 && j + cp.column > 0 && i + cp.row < r1 && j + cp.column < c1) {
                            bfp[i + cp.row][j + cp.column][k] = ovp[i][j][k];
                        }
                    }
                }
            }
        }
        return pixelsToImageColor(bfp);
    }

    public static BufferedImage overlayImage(BufferedImage bgImage, BufferedImage fgImage, double alpha) {
        int w = bgImage.getWidth();
        int h = bgImage.getHeight();
        BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImg.createGraphics();

// Clear the image (optional)
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, w, h);

// Draw the background image
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(bgImage, 0, 0, null);

// Draw the overlay image
        g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
        g.drawImage(fgImage, 0, 0, null);

        g.dispose();
        return newImg;
    }

    public static BufferedImage overlayImage(BufferedImage bgImage, BufferedImage fgImage, Point p, double alpha) {
        int w = bgImage.getWidth();
        int h = bgImage.getHeight();
        BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImg.createGraphics();

// Clear the image (optional)
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, w, h);

// Draw the background image
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(bgImage, 0, 0, null);

// Draw the overlay image
        g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
        g.drawImage(fgImage, p.x, p.y, null);

        g.dispose();
        return newImg;
    }

    public static BufferedImage drawText(BufferedImage bf, String str, int x, int y, Color col) {
        BufferedImage newImage = new BufferedImage(
                bf.getWidth(), bf.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D gr = newImage.createGraphics();
        gr.drawImage(bf, 0, 0, null);
        gr.setColor(col);
        gr.drawString(str, x, y);
        gr.dispose();
        return newImage;
    }

    public static BufferedImage adaptiveThreshold(double[][] d, int t1, int t2) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] >= t1 && d[i][j] <= t2) {
                    d[i][j] = 255;
                } else {
                    d[i][j] = 0;
                }
            }
        }
        BufferedImage binarized = ImageProcess.pixelsToImageGray(d);
        return binarized;
    }

    public static BufferedImage thresholdGray(BufferedImage img, int t1, int t2) {
        double[][] d = imageToPixelsDouble(img);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] >= t1 && d[i][j] <= t2) {
                    d[i][j] = 255;
                } else {
                    d[i][j] = 0;
                }
            }
        }
        BufferedImage binarized = ImageProcess.pixelsToImageGray(d);
        return binarized;
    }

    public static BufferedImage thresholdHSV(BufferedImage bf, int h1, int h2, int s1, int s2, int v1, int v2) {
        BufferedImage ret = null;
        int[][] dh = imageToPixelsInt(rgb2gray(getHueChannel(bf)));
        int[][] ds = imageToPixelsInt(rgb2gray(getSaturationChannel(bf)));
        int[][] dv = imageToPixelsInt(rgb2gray(getValueChannel(bf)));
        int nr = dh.length;
        int nc = dh[0].length;
        int[][] d = new int[nr][nc];
        int h, s, v;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                h = dh[i][j];
                s = ds[i][j];
                v = dv[i][j];
                if (h >= h1 && h <= h2 && s >= s1 && s <= s2 && v >= v1 && v <= v2) {
                    d[i][j] = 255;
                }
            }
        }
        ret = pixelsToImageGray(d);
        return ret;
    }

    public static BufferedImage ocv_img2hsv(BufferedImage bf) {
//        Mat frame = ImageProcess.ocv_img2Mat(bf);
//        Mat hsvImage = new Mat();
//        Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);
//        BufferedImage img = ocv_mat2Img(hsvImage);
//        return img;
        return ocv_rgb2hsv(bf);
    }

    public static BufferedImage ocv_rgb2hsv(BufferedImage bf) {
        Mat frame = ImageProcess.ocv_img2Mat(bf);
        Mat hsvImage = new Mat();
        // convert the frame to HSV
        Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);
        BufferedImage img = ocv_mat2Img(hsvImage);
        return img;
    }

    public static BufferedImage ocv_hsvThreshold(BufferedImage bf, int h1, int h2, int s1, int s2, int v1, int v2) {
        Mat frame = ImageProcess.ocv_img2Mat(bf);
//        Mat hsvImage = new Mat();
        Mat mask = new Mat();

        // convert the frame to HSV
//        Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);
        // get thresholding values from the UI
        Scalar minValues = new Scalar(h1, s1, v1);
        Scalar maxValues = new Scalar(h2, s2, v2);

        Core.inRange(frame, minValues, maxValues, mask);
        BufferedImage img = ocv_mat2Img(mask);
        return img;
    }

    public static BufferedImage ocv_medianFilter(BufferedImage bf) {
        Mat frame = ImageProcess.ocv_img2Mat(bf);
        Mat blurredImage = new Mat();
        Imgproc.medianBlur(frame, blurredImage, 5);
        BufferedImage out = ocv_mat2Img(blurredImage);
        return out;
    }

    public static BufferedImage cropBoundingBox(BufferedImage img) {
        BufferedImage bf = clone(img);
        int[][] d = imageToPixelsInt(img);
        int nr = d.length;
        int nc = d[0].length;
        int left = nc, right = 0;
        int top = nr, bottom = 0;

        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (d[i][j] > 0) {
                    if (left > j) {
                        left = j;
                    }
                    if (right < j) {
                        right = j;
                    }
                    if (top > i) {
                        top = i;
                    }
                    if (bottom < i) {
                        bottom = i;
                    }
                }
            }
        }
        bf = cropImage(bf, new CRectangle(top, left, right - left, bottom - top));
        return bf;
    }

    public static CPoint getCenterPoint(BufferedImage img) {
        CPoint cp = new CPoint();
        cp.row = img.getHeight() / 2;
        cp.column = img.getWidth() / 2;
        return cp;
    }

    public static BufferedImage changeQuantizationLevel(BufferedImage image, int n) {
        double[][] d = imageToPixelsDouble(image);
        int nr = d.length;
        int nc = d[0].length;

        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {

            }
        }
        return image;
    }

    public static BufferedImage equalizeHistogram(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        int anzpixel = width * height;
        int[] histogram = new int[256];
        int[] iarray = new int[1];
        int i = 0;

        //read pixel intensities into histogram
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int valueBefore = bi.getRaster().getPixel(x, y, iarray)[0];
                histogram[valueBefore]++;
            }
        }

        int sum = 0;
        // build a Lookup table LUT containing scale factor
        float[] lut = new float[anzpixel];
        for (i = 0; i < 256; ++i) {
            sum += histogram[i];
            lut[i] = sum * 255 / anzpixel;
        }

        // transform image using sum histogram as a Lookup table
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = bi.getRaster().getPixel(x, y, iarray)[0];
                int valueAfter = (int) lut[valueBefore];
                iarray[0] = valueAfter;
                bi.getRaster().setPixel(x, y, iarray);
            }
        }
        return bi;
    }

    public static double[] getHuMoments(BufferedImage img) {
        double[] moments = new double[7];
        Mat imagenOriginal;
        imagenOriginal = new Mat();
        Mat binario;
        binario = new Mat();
        Mat Canny;
        Canny = new Mat();

        imagenOriginal = ImageProcess.ocv_img2Mat(img);
        Mat gris = new Mat(imagenOriginal.width(), imagenOriginal.height(), imagenOriginal.type());
        Imgproc.cvtColor(imagenOriginal, gris, Imgproc.COLOR_RGB2GRAY);
        org.opencv.core.Size s = new Size(3, 3);
        Imgproc.GaussianBlur(gris, gris, s, 2);

        Imgproc.threshold(gris, binario, 100, 255, Imgproc.THRESH_BINARY);
        Imgproc.Canny(gris, Canny, 50, 50 * 3);

        java.util.List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Mat hierarcy = new Mat();

        Imgproc.findContours(Canny, contours, hierarcy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(Canny, contours, -1, new Scalar(Math.random() * 255, Math.random() * 255, Math.random() * 255));
        Moments p = new Moments();
        java.util.List<Moments> nu = new ArrayList<Moments>(contours.size());

        for (int i = 0; i < contours.size(); i++) {
            nu.add(i, Imgproc.moments(contours.get(i), false));
            p = nu.get(i);
        }
        double n20 = p.get_nu20(),
                n02 = p.get_nu02(),
                n30 = p.get_nu30(),
                n12 = p.get_nu12(),
                n21 = p.get_nu21(),
                n03 = p.get_nu03(),
                n11 = p.get_nu11();

        //First moment
        moments[0] = n20 + n02;

        //Second moment
        moments[1] = Math.pow((n20 - 02), 2) + Math.pow(2 * n11, 2);

        //Third moment
        moments[2] = Math.pow(n30 - (3 * (n12)), 2)
                + Math.pow((3 * n21 - n03), 2);

        //Fourth moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n12 + n03), 2);

        //Fifth moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (3 * n21 - n03) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

        //Sixth moment
        moments[5] = (n20 - n02)
                * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                + 4 * n11 * (n30 + n12) * (n21 + n03);

        //Seventh moment
        moments[6] = (3 * n21 - n03) * (n30 + n12)
                * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                + (n30 - 3 * n12) * (n21 + n03)
                * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));

//        //Eighth moment
//        moments[7] = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
//                - (n20 - n02) * (n30 + n12) * (n03 + n21);
        return moments;
    }

    public static int[][] rgb2lab(int[][] img) {
        int r = img.length;
        int c = img[0].length;
        int[][] ret = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                int val = img[i][j];
                Color col = new Color(val, true);
                int red = col.getRed();
                int green = col.getGreen();
                int blue = col.getBlue();
                ColorSpaceLAB lab = ColorSpaceLAB.fromRGB(red, green, blue, 255);
            }
        }
        return ret;
    }

}
