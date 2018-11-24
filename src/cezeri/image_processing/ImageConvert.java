/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;
/* Image conversion utilities.
 * 
 * Copyright (c) 2006 Jean-Sebastien Senecal (js@drone.ws)
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

public class ImageConvert {

	/**
	 * Converts an AWT Image into a grayscale BufferedImage.
	 * 
	 * @param image the image to convert
	 * @return a BufferedImage
	 */
	public static BufferedImage toGrayBufferedImage(Image image) {
		if (image instanceof BufferedImage && ((BufferedImage)image).getType() == BufferedImage.TYPE_BYTE_GRAY)
			return (BufferedImage)image;

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}


	/**
	 * Converts an AWT Image into a BufferedImage, with specific type.
	 * 
	 * @param image the image to convert
	 * @param type the BufferedImage type
	 * @return a BufferedImage with the contents of the image
	 */
	public static BufferedImage toBufferedImage(Image image, int type) {
		if (image instanceof BufferedImage) {
			return convertType((BufferedImage)image, type);
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	/**
	 * Converts an AWT Image into a BufferedImage.
	 * 
	 * @param image the image to convert
	 * @return a BufferedImage with the contents of the image
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage)image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = hasAlpha ? Transparency.BITMASK : Transparency.OPAQUE;

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
			int type = hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public static BufferedImage toOpaque(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		int[] alpha = new int[w*h];
		for (int i=0; i<alpha.length; ++i)
			alpha[i] = Transparency.OPAQUE;
		int buf[] = new int[w];
		for (int y = 0; y < h; y++) {
			src.getRGB(0, y, w, 1, buf, 0, w);
			dest.setRGB(0, y, w, 1, buf, 0, w);
		}
		dest.getAlphaRaster().setPixels(0, 0, w, h, alpha);
		return dest;
	}

	/**
	 * Converts a BufferedImage to the specified type.
	 * @param src the source image
	 * @param type the BufferedImage type
	 * @return a BufferedImage with the right type
	 */
	public static BufferedImage convertType(BufferedImage src, int type) {
		if (src.getType() == type)
			return src;
		
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage image = new BufferedImage(w, h, type);
		Graphics2D g2 = image.createGraphics();
		g2.drawRenderedImage(src, null);
		g2.dispose();
		return image;
	}
	
	/**
	 * Convert pixels from java default ARGB int format to byte array in ABGR format.
	 * @param pixels the pixels to convert
	 */
	public static void convertARGBtoABGR(int[] pixels)
	{
		int p, r, g, b, a;
		for (int i = 0; i < pixels.length; i++) {
			p = pixels[i];
			a = (p >> 24) & 0xFF;  // get pixel bytes in ARGB order
			r = (p >> 16) & 0xFF;
			g = (p >> 8) & 0xFF;
			b = (p >> 0) & 0xFF;
			pixels[i] = (a << 24) | (b << 16) | (g << 8) | (r << 0);
		}
	}

	/**
	 * Convert pixels from java default ABGR int format to byte array in ARGB format.
	 * @param pixels the pixels to convert
	 */
	public static void convertABGRtoARGB(int[] pixels)
	{
		int p, r, g, b, a;
		for (int i = 0; i < pixels.length; i++) {
			p = pixels[i];
			a = (p >> 24) & 0xFF;  // get pixel bytes in ARGB order
			b = (p >> 16) & 0xFF;
			g = (p >> 8) & 0xFF;
			r = (p >> 0) & 0xFF;
			pixels[i] = (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
	}

	/**
	 * Returns true if the specified image has transparent pixels.
	 * 
	 * @param image the image to test
	 * @return true if the specified image has transparent pixels
	 */
	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage)image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

}