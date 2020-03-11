/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.transform;

import cezeri.image_processing.ImageProcess;
import cezeri.factory.FactoryNormalization;
import cezeri.factory.FactoryUtils;
import java.awt.image.BufferedImage;

public class TransformRadon {

    public static BufferedImage forwardProjectionImg(BufferedImage img, int numberOfAngles, int numberOfProjections) {
        int w = img.getWidth();
        int h = img.getHeight();
        double originX = w / 2;
        double originY = h / 2;
        double projectionLine = Math.sqrt(w * w + h * h); //imajın diagonal uzunluğu
        double t = 0.0D;
        //#projection aslında array uzunluğu dolayısıyla bununla gerçek 
        //t hesaplamalarındaki maxsimum diagonal arasında 1D interpolasyon gerekir
        double interpolationRatio = projectionLine / (double) numberOfProjections;
        double[][] projectedData = new double[numberOfAngles][numberOfProjections];
        int[][] imagePixels = ImageProcess.imageToPixelsInt(img);
        for (int i = 0; i < numberOfAngles; i++) {
            double teta = (double) i * (Math.PI / (double) numberOfAngles);
            double cosTeta = Math.cos(teta);
            double sinTeta = Math.sin(teta);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    t = (x - originX) * cosTeta + (y - originY) * sinTeta;
                    double dIndex = (t / interpolationRatio + (numberOfProjections / 2)) - 0.5;
                    int upNeighbourCell = (int) Math.ceil(dIndex);
                    int downNeighbourCell = (int) dIndex;
                    double downCellContribution = imagePixels[y][x] * (upNeighbourCell - dIndex);
                    double upCellContribution = imagePixels[y][x] * (dIndex - downNeighbourCell);
                    if (upNeighbourCell < numberOfProjections && downNeighbourCell > -1) {
                        projectedData[i][downNeighbourCell] += downCellContribution;
                        projectedData[i][upNeighbourCell] += upCellContribution;
                    }
                }
            }
        }
        BufferedImage backImage = ImageProcess.pixelsToImageGray(FactoryUtils.toIntArray2D(FactoryNormalization.normalizeMinMax(projectedData)));
        return backImage;
    }
    
    public static double[][] forwardProjection(double[][] imagePixels, int numberOfAngles, int numberOfProjections) {
        int w = imagePixels.length;
        int h = imagePixels[0].length;
        double originX = w / 2;
        double originY = h / 2;
        double projectionLine = Math.sqrt(w * w + h * h); //imajın diagonal uzunluğu
        double t = 0.0D;
        //#projection aslında array uzunluğu dolayısıyla bununla gerçek 
        //t hesaplamalarındaki maxsimum diagonal arasında 1D interpolasyon gerekir
        double interpolationRatio = projectionLine / (double) numberOfProjections;
        double[][] projectedData = new double[numberOfAngles][numberOfProjections];
        for (int i = 0; i < numberOfAngles; i++) {
            double teta = (double) i * (Math.PI / (double) numberOfAngles);
            double cosTeta = Math.cos(teta);
            double sinTeta = Math.sin(teta);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    t = (x - originX) * cosTeta + (y - originY) * sinTeta;
                    double dIndex = (t / interpolationRatio + (numberOfProjections / 2)) - 0.5;
                    int upNeighbourCell = (int) Math.ceil(dIndex);
                    int downNeighbourCell = (int) dIndex;
                    double downCellContribution = imagePixels[y][x] * (upNeighbourCell - dIndex);
                    double upCellContribution = imagePixels[y][x] * (dIndex - downNeighbourCell);
                    if (upNeighbourCell < numberOfProjections && downNeighbourCell > -1) {
                        projectedData[i][downNeighbourCell] += downCellContribution;
                        projectedData[i][upNeighbourCell] += upCellContribution;
                    }
                }
            }
        }
        projectedData=FactoryUtils.transpose(projectedData);
        return projectedData;
    }

    public static BufferedImage backwardProjectionImg(double[][] projArr, int imgSize) {
        double originX = imgSize / 2;
        double originY = imgSize / 2;
        int numberOfAngles = projArr.length;
        int numberOfProjections = projArr[0].length;
        double projectionLine = Math.sqrt(2) * imgSize;
        double t = 0.0D;
        double interpolationRatio = projectionLine / (double) numberOfProjections;
        double[][] backProjectedData = new double[imgSize][imgSize];

        for (int i = 0; i < numberOfAngles; i++) {
            double teta = (double) i * (Math.PI / (double) numberOfAngles);
            double cosTeta = Math.cos(teta);
            double sinTeta = Math.sin(teta);
            for (int y = 0; y < imgSize; y++) {
                for (int x = 0; x < imgSize; x++) {
                    t = (x - originX) * cosTeta + (y - originY) * sinTeta;
                    double dIndex = (t / interpolationRatio + (numberOfProjections / 2)) - 0.5;
                    int upNeighbourCell = (int) Math.ceil(dIndex);
                    int downNeighbourCell = (int) dIndex;
                    if (upNeighbourCell < numberOfProjections && downNeighbourCell > -1) {
                        double upCellContribution = projArr[i][upNeighbourCell] * (upNeighbourCell - dIndex);
                        double downCellContribution = projArr[i][downNeighbourCell] * (dIndex - downNeighbourCell);
                        backProjectedData[y][x] += upCellContribution;
                        backProjectedData[y][x] += downCellContribution;
                    }
                }
            }
        }
        BufferedImage backImage = ImageProcess.pixelsToImageGray(FactoryUtils.toIntArray2D(FactoryNormalization.normalizeMinMax(backProjectedData)));
        return backImage;
    }
    
    public static double[][] backwardProjection(double[][] projArr, int imgSize) {
        double originX = imgSize / 2;
        double originY = imgSize / 2;
        int numberOfAngles = projArr.length;
        int numberOfProjections = projArr[0].length;
        double projectionLine = Math.sqrt(2) * imgSize;
        double t = 0.0D;
        double interpolationRatio = projectionLine / (double) numberOfProjections;
        double[][] backProjectedData = new double[imgSize][imgSize];

        for (int i = 0; i < numberOfAngles; i++) {
            double teta = (double) i * (Math.PI / (double) numberOfAngles);
            double cosTeta = Math.cos(teta);
            double sinTeta = Math.sin(teta);
            for (int y = 0; y < imgSize; y++) {
                for (int x = 0; x < imgSize; x++) {
                    t = (x - originX) * cosTeta + (y - originY) * sinTeta;
                    double dIndex = (t / interpolationRatio + (numberOfProjections / 2)) - 0.5;
                    int upNeighbourCell = (int) Math.ceil(dIndex);
                    int downNeighbourCell = (int) dIndex;
                    if (upNeighbourCell < numberOfProjections && downNeighbourCell > -1) {
                        double upCellContribution = projArr[i][upNeighbourCell] * (upNeighbourCell - dIndex);
                        double downCellContribution = projArr[i][downNeighbourCell] * (dIndex - downNeighbourCell);
                        backProjectedData[y][x] += upCellContribution;
                        backProjectedData[y][x] += downCellContribution;
                    }
                }
            }
        }
        return backProjectedData;
    }

    public static BufferedImage doSheppLoganFilter(BufferedImage img, int numberOfAngles, int numberOfProjections) {
        double[][] dd=FactoryUtils.toDoubleArray2D(ImageProcess.imageToPixelsInt(img));
        double[][] ProjectionArray = forwardProjection(dd, numberOfAngles, numberOfProjections);
        int w = img.getWidth();
        int h = img.getHeight();
        double projectionLine = Math.sqrt(w * w + h * h); //imajın diagonal uzunluğu
        double interpolationRatio = projectionLine / (double) numberOfProjections;
        double distance = interpolationRatio;
        double temp = 4.0 / (Math.PI * distance * distance);
        double[] PHI = new double[numberOfProjections];
        PHI[0] = temp;
        for (int K = 1; K < numberOfProjections; ++K) {
            PHI[K] = -temp / (4 * K * K - 1.0);
        }
        double[][] d = doConvolution(ProjectionArray, numberOfAngles, numberOfProjections, PHI);
        BufferedImage imgRet = ImageProcess.pixelsToImageGray(FactoryUtils.toIntArray2D(d));
        return imgRet;
    }

    public static BufferedImage doRampFilter(BufferedImage img, int numberOfAngles, int numberOfProjections) {
        double[][] dd=FactoryUtils.toDoubleArray2D(ImageProcess.imageToPixelsInt(img));
        double[][] ProjectionArray = forwardProjection(dd, numberOfAngles, numberOfProjections);
        double distance = 0.1D;
        double temp = (1 / (2 * distance)) * (1 / (2 * distance));
        double[] PHI = new double[numberOfProjections];
        PHI[0] = temp;
        for (int K = 1; K < numberOfProjections; ++K) {
            if ((2 * (K / 2) - K) == 0.0) {
                PHI[K] = 0.0;
            } else {
                PHI[K] = (-4.0 * temp) / (K * K * Math.PI * Math.PI);
            }
        }
        double[][] d = doConvolution(ProjectionArray, numberOfAngles, numberOfProjections, PHI);
        BufferedImage imgRet = ImageProcess.pixelsToImageGray(FactoryUtils.toIntArray2D(d));
        return imgRet;
    }

    public static double[][] doConvolution(double[][] ProjectionArray, int numberOfAngles, int numberOfProjections, double[] PHI) {
        double[][] convolutionArray = new double[numberOfAngles][numberOfProjections];
        for (int m = 0; m < numberOfAngles; m++) {
            for (int i = 0; i < numberOfProjections; i++) {
                for (int j = 0; j < numberOfProjections; j++) {
                    convolutionArray[m][i] += ProjectionArray[m][j] * PHI[Math.abs(i - j)];
                }
            }
        }
        return convolutionArray;
    }

}
