/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.kinect;

import processing.core.PApplet;
import org.openkinect.freenect.*;
import org.openkinect.processing.*;
import processing.core.PVector;

/**
 *
 * @author BAP1
 */
public class PointCloud extends PApplet {

    Kinect kinect;
    float a = 0;
    float b = 0;
    float[] depthLookUp = new float[2048];

    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.PointCloud"});
    }

    @Override
    public void settings() {
        // Rendering in P3D
        size(800, 600, P3D);
    }

    public void setup() {
        kinect = new Kinect(this);
        kinect.initDepth();

        // Lookup table for all possible depth values (0 - 2047)
        for (int i = 0; i < depthLookUp.length; i++) {
            depthLookUp[i] = rawDepthToMeters(i);
        }
    }

    public void draw() {
        background(0);
        int[] depth = kinect.getRawDepth();
        // We're just going to calculate and draw every 4th pixel (equivalent of 160x120)
        int skip = 4;
        pushMatrix();
        translate(width / 2, height / 2, -50);
        rotateY(a);

        for (int x = 0; x < kinect.width; x += skip) {
            for (int y = 0; y < kinect.height; y += skip) {
                int offset = x + y * kinect.width;

                // Convert kinect data to world xyz coordinate
                int rawDepth = depth[offset];
                PVector v = depthToWorld(x, y, rawDepth);

                stroke(255);
                pushMatrix();
                // Scale up by 200
                float factor = 200;
                translate(v.x * factor, v.y * factor, factor - v.z * factor);
                // Draw a point
                point(0, 0);
                popMatrix();
            }
        }

        // Rotate
        a += 0.015f;
        popMatrix();
        textSize(16);
        text("Frame rate: " + (int) frameRate + " b:" + b, 10, 50);
    }

    @Override
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                a++;
            } else if (keyCode == DOWN) {
                a--;
            }
        }
    }

// These functions come from: http://graphics.stanford.edu/~mdfisher/Kinect.html
    float rawDepthToMeters(int depthValue) {
        if (depthValue < 2047) {
            return (float) (1.0 / ((double) (depthValue) * -0.0030711016 + 3.3309495161));
        }
        return 0.0f;
    }

    PVector depthToWorld(int x, int y, int depthValue) {

        final double fx_d = 1.0 / 5.9421434211923247e+02;
        final double fy_d = 1.0 / 5.9104053696870778e+02;
        final double cx_d = 3.3930780975300314e+02;
        final double cy_d = 2.4273913761751615e+02;

        PVector result = new PVector();
        double depth = depthLookUp[depthValue];//rawDepthToMeters(depthValue);
        result.x = (float) ((x - cx_d) * depth * fx_d);
        result.y = (float) ((y - cy_d) * depth * fy_d);
        result.z = (float) (depth);
        return result;
    }
}
