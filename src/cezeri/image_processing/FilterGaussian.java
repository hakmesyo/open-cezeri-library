/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

/**
 *
 * @author musa-atas
 */
import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.*;

// additional stuff for swing
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JApplet;
import javax.imageio.*;

public class FilterGaussian {

    int[] input;
    int[] output;
    float[] template;
    int progress=0;
    double sigma;
    int templateSize;
    int width;
    int height;

    public void init(int[] original, int sigmaIn, int tempSize, int widthIn, int heightIn) {
        if ((tempSize % 2) == 0) {
            templateSize = tempSize - 1;
        }
        sigma = (double) sigmaIn;
        templateSize = tempSize;
        width = widthIn;
        height = heightIn;
        input = new int[width * height];
        output = new int[width * height];
        template = new float[templateSize * templateSize];
        input = original;
    }

    public void init(int[] original, double sigmaIn, int tempSize, int widthIn, int heightIn) {
        if ((tempSize % 2) == 0) {
            templateSize = tempSize - 1;
        }
        sigma = sigmaIn;
        templateSize = tempSize;
        width = widthIn;
        height = heightIn;
        input = new int[width * height];
        output = new int[width * height];
        template = new float[templateSize * templateSize];
        input = original;
    }

    public void generateTemplate() {
        float center = (templateSize - 1) / 2;

        float total = 0;

        for (int x = 0; x < templateSize; x++) {
            for (int y = 0; y < templateSize; y++) {
                template[x * templateSize + y] = (float) (1 / (float) (2 * Math.PI * sigma * sigma)) * (float) Math.exp((float) (-((x - center) * (x - center) + (y - center) * (y - center)) / (2 * sigma * sigma)));
                total += template[x * templateSize + y];
            }
        }
        for (int x = 0; x < templateSize; x++) {
            for (int y = 0; y < templateSize; y++) {
                template[x * templateSize + y] = template[x * templateSize + y] / total;
            }
        }
    }

    public int[] process() {
        float sum;
        progress = 0;
        int outputsmaller[] = new int[(width - (templateSize - 1)) * (height - (templateSize - 1))];

        for (int x = (templateSize - 1) / 2; x < width - (templateSize + 1) / 2; x++) {
            progress++;
            for (int y = (templateSize - 1) / 2; y < height - (templateSize + 1) / 2; y++) {
                sum = 0;
                for (int x1 = 0; x1 < templateSize; x1++) {
                    for (int y1 = 0; y1 < templateSize; y1++) {
                        int x2 = (x - (templateSize - 1) / 2 + x1);
                        int y2 = (y - (templateSize - 1) / 2 + y1);
                        float value = (input[y2 * width + x2] & 0xff) * (template[y1 * templateSize + x1]);
                        sum += value;
                    }
                }
                outputsmaller[(y - (templateSize - 1) / 2) * (width - (templateSize - 1)) + (x - (templateSize - 1) / 2)] = 0xff000000 | ((int) sum << 16 | (int) sum << 8 | (int) sum);
            }
        }
        progress = width;

        Toolkit tk = Toolkit.getDefaultToolkit();

        Image tempImage = tk.createImage(new MemoryImageSource((width - (templateSize - 1)), (height - (templateSize - 1)), outputsmaller, 0, (width - (templateSize - 1)))).getScaledInstance(256, 256, Image.SCALE_SMOOTH);
        PixelGrabber grabber = new PixelGrabber(tempImage, 0, 0, width, height, output, 0, width);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e2) {
            System.out.println("error: " + e2);
        }
        progress = width;

        return output;
    }

    public int getProgress() {
        return progress;
    }
}
