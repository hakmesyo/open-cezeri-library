/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

/**
 * ***********************************************
 * File: XYZSet.java Description: Conversion between RGB, XYZ and CIELAB color
 * spaces. Author: Gene Vishnevsky Oct. 15, 1997
************************************************
 */
/**
 * This class implements conversions between RGB, CIE XYZ and CIELAB color
 * spaces.
 */
public class XYZSet {

    float L, aa, bb;	// current CIE Lab coords
    float X, Y, Z;		// CIE XYZ coords
    int r, g, b;		// RGB coords
    float rf, gf, bf;	// [0,1] RGB

    /**
     * x color matching function tabulated at 20-nm intervals. (10-degree 1964
     * CIE suppl. std. observer)
     */
    static float Px[] = {
        0.00327f, 0.03505f, 0.06577f, 0.05182f,
        0.01380f, 0.00065f, 0.02017f, 0.06458f,
        0.12087f, 0.17384f, 0.19266f, 0.14677f,
        0.07398f, 0.02616f, 0.00701f, 0.00165f
    };

    /**
     * y color matching function tabulated at 20-nm intervals. (10-degree 1964
     * CIE suppl. std. observer)
     */
    static float Py[] = {
        0.00034f, 0.00367f, 0.01064f, 0.02197f,
        0.04347f, 0.07898f, 0.13058f, 0.16489f,
        0.17094f, 0.14893f, 0.11284f, 0.06824f,
        0.03082f, 0.01034f, 0.00273f, 0.00062f

    };

    /**
     * z color matching function tabulated at 20-nm intervals. (10-degree 1964
     * CIE suppl. std. observer)
     */
    static float Pz[] = {
        0.01474f, 0.16669f, 0.33720f, 0.29917f,
        0.13234f, 0.03745f, 0.01040f, 0.00235f,
        0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
    };

    /**
     * Constructs a converter with no arguments.
     */
    public XYZSet() {;
    }

    /**
     * Constructs a converter with an argument.
     *
     * @param rgb RGB components that are used to compute initial XYZ and LAB
     * values.
     * @see RGBCoord
     */
    public XYZSet(RGBCoord rgb) {
        convertFromRGB(rgb);
    }

    /**
     * Converts from RGB to XYZ and LAB.
     *
     * @param rgb RGB components to convert from.
     * @see RGBCoord
     */
    public void convertFromRGB(RGBCoord rgb) {
        RGBtoXYZ(rgb);
        XYZtoLAB();
    }

    /**
     * Computes XYZ coordinates for a spectrum.
     *
     * @param data[] an array of 16 spectrum data points.
     */
    public void spectrumToXYZ(float data[]) {

        X = 0.f;
        Y = 0.f;
        Z = 0.f;

        // integrate
        for (int wl = 0; wl < 16; wl++) {
            X += data[wl] * Px[wl];
            Y += data[wl] * Py[wl];
            Z += data[wl] * Pz[wl];
        }
    }

    /**
     * Converts from XYZ to RGB internally.
     *
     * @return 24-bit RGB color if conversion successful; 0 otherwise
     */
    public int XYZtoRGB() {

        // using D65 white point
        r = (int) ((3.063f * X - 1.393f * Y - 0.476f * Z) * 255);
        if (r < 0) {
            return 0;
        }
        if (r > 255) {
            return 0;
        }

        g = (int) ((-0.969f * X + 1.876f * Y + 0.042f * Z) * 255);
        if (g < 0) {
            return 0;
        }
        if (g > 255) {
            return 0;
        }

        b = (int) ((0.068f * X - 0.229f * Y + 1.069f * Z) * 255);
        if (b < 0) {
            return 0;
        }
        if (b > 255) {
            return 0;
        }

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Converts from XYZ to RGB.
     *
     * @param rgb RGBCoord object to set with converted values.
     */
    public void XYZtoRGB(RGBCoord rgb) {

        if (XYZtoRGB() == 0) {
            rgb.setValid(false);
        } else {
            rgb.setRGB(r, g, b);
        }
    }

    /**
     * Converts from RGB to XYZ.
     *
     * @param rgb input RGB values.
     */
    public void RGBtoXYZ(RGBCoord rgb) {

        // scale to [0,1] range
        rf = 0.0039215f * rgb.getRed();		// 0..1
        gf = 0.0039215f * rgb.getGreen();	// 0..1
        bf = 0.0039215f * rgb.getBlue();	// 0..1

        X = 0.431f * rf + 0.342f * gf + 0.178f * bf;
        Y = 0.222f * rf + 0.707f * gf + 0.071f * bf;
        Z = 0.020f * rf + 0.130f * gf + 0.939f * bf;

    }

    /**
     * Converts from LAB to XYZ internally.
     */
    public void LABtoXYZ() {
        LABtoXYZ(aa, bb);
    }

    /**
     * Converts from LAB to XYZ. Uses internal L-value.
     *
     * @param a the a-value
     * @param b the b-value
     */
    public void LABtoXYZ(float a, float b) {

        float frac = (L + 16) / 116;

        if (L < 7.9996f) {
            Y = L / 903.3f;
            X = a / 3893.5f + Y;
            Z = Y - b / 1557.4f;
        } else {
            float tmp;
            tmp = frac + a / 500;
            X = tmp * tmp * tmp;
            Y = frac * frac * frac;
            tmp = frac - b / 200;
            Z = tmp * tmp * tmp;
        }
    }

    /**
     * Converts from XYZ to LAB internally.
     */
    public void XYZtoLAB() {

        if (Y > 0.008856) {
            L = (float) (116f * Math.pow(Y, 0.3333333) - 16);
        } else {
            L = 903.3f * Y;
        }

        aa = 500f * (float) (Math.pow(X, 0.3333333)
                - Math.pow(Y, 0.3333333));

        bb = 200f * (float) (Math.pow(Y, 0.3333333)
                - Math.pow(Z, 0.3333333));

    }

}
