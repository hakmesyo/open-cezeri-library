/*
 http://zerocool.is-a-geek.net/java-color-image-to-grayscale-conversion-algorithm/
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Image to grayscale algorithm(s)
 *
 * Author: Bostjan Cigan (http://zerocool.is-a-geek.net)
 *
 */
public class GrayScale {

    // The average grayscale method
    public static BufferedImage avg(BufferedImage original) {

        int value, alpha, red, green, blue;
        int newPixel;

        BufferedImage avg_gray = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] avgLUT = new int[766];
        for (int i = 0; i < avgLUT.length; i++) {
            avgLUT[i] = (int) (i / 3);
        }

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;
                newPixel = red + green + blue;
                newPixel = avgLUT[newPixel];
                // Return back to original format
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);

                // Write pixels into image
                avg_gray.setRGB(i, j, newPixel);

            }
        }

        return avg_gray;

    }

    // The luminance method
    public static BufferedImage luminosity(BufferedImage original) {

        int value,alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);

                // Write pixels into image
                lum.setRGB(i, j, newPixel);

            }
        }

        return lum;

    }

    // The desaturation method
    public static BufferedImage desaturation(BufferedImage original) {

        int value,alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage des = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[] desLUT = new int[511];
        for (int i = 0; i < desLUT.length; i++) {
            desLUT[i] = (int) (i / 2);
        }

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = (int) (findMax(pixel) + findMin(pixel));
                newval = desLUT[newval];

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                des.setRGB(i, j, newPixel);

            }
        }

        return des;

    }

    // The minimal decomposition method
    public static BufferedImage decompMin(BufferedImage original) {

        int value,alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = findMin(pixel);

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                decomp.setRGB(i, j, newPixel);

            }
        }

        return decomp;

    }

    // The maximum decomposition method
    public static BufferedImage decompMax(BufferedImage original) {

        int value,alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage decomp = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = findMax(pixel);

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                decomp.setRGB(i, j, newPixel);

            }

        }

        return decomp;

    }

    // The "pick the color" method
    public static BufferedImage rgb(BufferedImage original, int color) {

        int value,alpha, red, green, blue;
        int newPixel;

        int[] pixel = new int[3];

        BufferedImage rgb = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                value = original.getRGB(i, j);
                alpha = (value >> 24) & 0xff;
                red = (value >> 16) & 0xff;
                green = (value >> 8) & 0xff;
                blue = value & 0xff;

                pixel[0] = red;
                pixel[1] = green;
                pixel[2] = blue;

                int newval = pixel[color];

                // Return back to original format
                newPixel = colorToRGB(alpha, newval, newval, newval);

                // Write pixels into image
                rgb.setRGB(i, j, newPixel);

            }

        }

        return rgb;

    }

    // The simplest way to convert in Java
    public static BufferedImage javaWay(BufferedImage source) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(source, null);
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

    public static int findMin(int[] pixel) {

        int min = pixel[0];

        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] < min) {
                min = pixel[i];
            }
        }

        return min;

    }

    public static int findMax(int[] pixel) {

        int max = pixel[0];

        for (int i = 0; i < pixel.length; i++) {
            if (pixel[i] > max) {
                max = pixel[i];
            }
        }

        return max;

    }

}
