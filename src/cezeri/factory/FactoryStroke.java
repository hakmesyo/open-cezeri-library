/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 *
 * @author BAP1
 */
public class FactoryStroke {

    public static Stroke getDefaultStroke() {
        Stroke s = new BasicStroke(1.0f);
        return s;
    }
    
    public static Stroke getStandardSolidStroke() {
        Stroke s = new BasicStroke(1.0f);
        return s;
    }

    public static Stroke getStandardSolidStroke(float width) {
        Stroke s = new BasicStroke(width);
        return s;
    }

    public static Stroke getDashedStroke() {
        Stroke s = new BasicStroke(1.0f, // Width
                BasicStroke.CAP_ROUND, // End cap
                BasicStroke.JOIN_ROUND, // Join style
                10.0f, // Miter limit
                new float[]{3.0f, 1.0f}, // Dash pattern
                0.0f);                     // Dash phase
        return s;
    }
    
    public static Stroke getDashedStroke(float width) {
        Stroke s = new BasicStroke(width, // Width
                BasicStroke.CAP_ROUND, // End cap
                BasicStroke.JOIN_ROUND, // Join style
                10.0f, // Miter limit
                new float[]{3.0f, 1.0f}, // Dash pattern
                0.0f);                     // Dash phase
        return s;
    }
    
    public static Stroke getDashedWithDotStroke() {
        Stroke s = new BasicStroke(2, // Width
                BasicStroke.CAP_ROUND, // End cap
                BasicStroke.JOIN_ROUND, // Join style
                10.0f, // Miter limit
                new float[]{5,3,1,3}, // Dash pattern
                0.0f);                     // Dash phase
        return s;
    }
    
    public static Stroke getDashedWithDotStroke(float width) {
        Stroke s = new BasicStroke(width, // Width
                BasicStroke.CAP_ROUND, // End cap
                BasicStroke.JOIN_ROUND, // Join style
                10.0f, // Miter limit
                new float[]{5*width,3*width,1*width,3*width}, // Dash pattern
                0.0f);                     // Dash phase
        return s;
    }

}
