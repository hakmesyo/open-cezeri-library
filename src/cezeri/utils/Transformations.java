/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;


public class Transformations {
    private int[][] myPixels;

    /**
     * Constructor takes in the file that contains the image
     * and converts it to a BufferedImage. This image is stored
     * in an instance variable in the form of an integer array.
     *
     *         DO NOT MODIFY THIS METHOD
     * 
     * @param file, file that contains the image
     */
    public Transformations(File file) {
        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Incorrect File ");
            return;
        }
        if (img.getType() == BufferedImage.TYPE_INT_RGB) {
            myPixels = imageToPixels(img);
        } else {
            BufferedImage tmpImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            tmpImage.createGraphics().drawImage(img, 0, 0, null);
            myPixels = imageToPixels(tmpImage);
        }        
    }
    
    /**
     * Method to return the original image
     *
     *         DO NOT MODIFY THIS METHOD
     * 
     * @return image, the original image
     */
    public int[][] getPixels() {
        return myPixels;
    }
 
    /**
     * Make all the pixels in the image red
     * 
     */
    
    public void setRed() {
        // The color class is in java.awt.*
        // Color constructor:   Color(int r, int g, int b)
        // Each of these integers are between 0 and 255

        // int red = myColor.getRed();
        // int green = myColor.getGreen();
        // int blue = myColor.getBlue();
        // int RGBvalue = myColor.getRGB();

        // red
        Color myColor = new Color(255, 0, 0);
        for (int i = 0; i < myPixels.length; ++i) {
            for (int j = 0; j < myPixels[i].length; ++j) {
                myPixels[i][j] = myColor.getRGB();
            }
            
        }
        
       // myPixels[i][j] = myColor.getRGB();    
    }
        
    /**
     * Method to explore color
     * 
     */
    public void playColor() {
            System.out.println("image height " + myPixels.length + " image width " + myPixels[0].length);
            System.out.println("Color number " + myPixels[0][0] + " at pixel 0 0");
            System.out.println("Color number " + myPixels[120][40] + " at pixel 120 40");

            Color myColor; 
            myColor = new Color(myPixels[120][40]);
            int bluePixels = 0;
            
            // Transforming back and forth from a color object to an integer 
            for (int i = 0; i < myPixels.length; i++) {
                for (int j = 0; j < myPixels[i].length; j++) {
                    // identity function
                    // myColor = new Color(myPixels[i][j]);
                    // myPixels[i][j] = myColor.getRGB();
                    
                    if (j > 200) { 
                        Color white = new Color(255, 255, 255);
                        myPixels[i][j] = white.getRGB();
                    }
                    // myPixels[i][j] = myColor.getRGB();
                    
                    // if (myPixels[i][j] == myColor.getRGB()) {
                    //    bluePixels++; 
                    //}
                }
            }
            System.out.println(bluePixels + " Blue pixels of color " + myColor.getRGB());
     
        }
    /**
     * Converts a two-dimensional array of image data to a BufferedImage.
     *
     *         DO NOT MODIFY THIS METHOD
     *
     * @param pixels    the source two-dimensional array of image data
     *
     * @return  a BufferedImage representing the source image.
     *
     * @throws  IllegalArgumentException if the source image is
     *          ill-defined.
     */

    public static BufferedImage pixelsToImage(int[][] pixels) throws IllegalArgumentException {

        if (pixels == null) {
            throw new IllegalArgumentException();
        }
        
        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            image.setRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return image;
    }
    
    /**
     * Converts a BufferedImage to a two-dimensional array of image data.
     *
     *         DO NOT MODIFY THIS METHOD
     *
     * @param image the source BufferedImage
     *
     * @return  a two-dimensional array of image data representing the
     *          source image
     *
     * @throws  IllegalArgumentException if the source image is
     *          ill-defined.
     */
    public static int[][] imageToPixels(BufferedImage image) throws IllegalArgumentException {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels = new int[height][width];
        for (int row = 0; row < height; row++) {
            image.getRGB(0, row, width, 1, pixels[row], 0, width);
        }
        return pixels;
    }
    

    public static void main(String[] args) {
        Transformations tr = new Transformations(new File(".\\images\\t1.bmp"));
        DrawingBox box = new DrawingBox("Two Balls by Jasper Johns");
        
        // display the original image
        BufferedImage originalImage = pixelsToImage(tr.getPixels());
        box.drawImage(originalImage, 10, 10);
        
//         tr.setRed();
        
        tr.playColor();
        box.drawImage(pixelsToImage(tr.getPixels()), 10, 300);
        
        // tr.playColor();
        // box.drawImage(pixelsToImage(tr.getPixels()), 10, 300);

                               
        // add other transformations to the image
        // display at different (x, y) co-ordinates
        
        
    }
}