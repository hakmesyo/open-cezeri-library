/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

/**
 * ***********************************************
 * File:	RGBCoord Description: This class encapsulates shared RGB coordinates.
 *
 * Author: Gene Vishnevsky Oct. 15, 1997
************************************************
 */
/**
 * This class encapsulates shared RGB coordinates.
 */
public class RGBCoord implements Cloneable {

    /**
     * RGB space coordinates (0...255)
     */
    protected int red, green, blue;

    /**
     * Flag set to true if the coordinates are valid.
     */
    protected boolean valid;			// 

    /**
     * Constructs RGBCoord given initial values. The values must be 0 to 255.
     */
    public RGBCoord(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        valid = true;
    }

    /**
     * Copy new RGB values includin the valid flag.
     *
     * @param rgb source for the new RGB values.
     */
    public synchronized void getFromRGB(RGBCoord rgb) {
        red = rgb.red;
        green = rgb.green;
        blue = rgb.blue;
        valid = rgb.valid;
    }

    /**
     * Returns the red component.
     */
    public synchronized int getRed() {
        return red;
    }

    /**
     * Returns the green component.
     */
    public synchronized int getGreen() {
        return green;
    }

    /**
     * Returns the blue component.
     */
    public synchronized int getBlue() {
        return blue;
    }

    /**
     * Returns true if the coordinates are valid.
     */
    public synchronized boolean isValid() {
        return valid;
    }

    /**
     * Copies the components into array.
     *
     * @param rgb[] array to place red, green and blue components, respectively.
     * @return the valid flag
     */
    public synchronized boolean getRGB(int rgb[]) {
        rgb[0] = red;
        rgb[1] = green;
        rgb[2] = blue;
        return valid;
    }

    /**
     * Sets the red component.
     */
    public synchronized void setRed(int red) {
        this.red = red;
    }

    /**
     * Sets the green component.
     */
    public synchronized void setGreen(int green) {
        this.green = green;
    }

    /**
     * Sets the blue component.
     */
    public synchronized void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * Sets the valid flag.
     */
    public synchronized void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Sets the RGB coordinates to specified values. The values must be 0 to
     * 255.
     */
    public synchronized void setRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.valid = true;		// of course, it's true
    }

    /**
     * Compares RGB components. Use when verifying if components must be updated
     * from another source.
     *
     * @param rgb set of components to compare with.
     * @return true if different in any respect; false otherwise
     */
    public synchronized boolean differentFrom(RGBCoord rgb) {

        // don't update from invalid coordinates
        if (rgb.isValid() == false) {
            return false;
        }

        if (rgb.getRed() != this.getRed()
                || rgb.getGreen() != this.getGreen()
                || rgb.getBlue() != this.getBlue()) {
            return true;
        }

        return false;

    }

    /**
     * Overrides Cloneable
     *
     * @exception InternalError
     */
    public synchronized Object clone() {

        try {

            return super.clone();

        } catch (CloneNotSupportedException e) {

            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Converts to String.
     *
     * @return String
     */
    public String toString() {

        if (valid) {
            return getClass().getName()
                    + " [r=" + red + ",g=" + green + ",b=" + blue + "]";
        } else {
            return getClass().getName() + " RGB invalid.";
        }
    }

}
