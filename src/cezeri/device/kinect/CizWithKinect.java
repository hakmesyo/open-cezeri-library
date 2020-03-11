/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.kinect;

import org.openkinect.processing.*;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author BAP1
 */
public class CizWithKinect extends PApplet {

    Kinect kinect;

// Depth image
    PImage depthImg;

// Which pixels do we care about?
    int minDepth = 60;
    int maxDepth = 950;

// What is the kinect's angle
    float angle;

    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.CizWithKinect"});
    }

    @Override
    public void settings() {
        size(640, 480);
//        fullScreen();
    }

    @Override
    public void setup() {
        kinect = new Kinect(this);
        kinect.initDepth();
        angle = kinect.getTilt();
        depthImg = new PImage(kinect.width, kinect.height);
    }
    
    

    @Override
    public void draw() {
        //background(0);
        // Draw the raw image
        //image(kinect.getDepthImage(), 0, 0);

        // Threshold the depth image
        int[] rawDepth = kinect.getRawDepth();
        depthImg.loadPixels();
        for (int i = 0; i < rawDepth.length; i++) {
            if (rawDepth[i] >= minDepth && rawDepth[i] <= maxDepth) {
                depthImg.pixels[i] = color(255);
            } else {
                depthImg.pixels[i] = color(0);
            }
        }
        // Draw the thresholded image
        depthImg.updatePixels();
        image(depthImg, 0, 0);

        //textSize(16);
        fill(255);
        //text("TILT: " + angle, 10, 20);
        text("THRESHOLD: [" + minDepth + ", " + maxDepth + "]", 10, 20);        
        text("Frame rate: " + (int) frameRate, 10, 40);
    }

    // Adjust the angle and the depth threshold min and max
    @Override
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                angle++;
            } else if (keyCode == DOWN) {
                angle--;
            }
            angle = constrain(angle, 0, 30);
            kinect.setTilt(angle);
        } else if (key == 'a') {
            minDepth = constrain(minDepth + 10, 0, maxDepth);
        } else if (key == 's') {
            minDepth = constrain(minDepth - 10, 0, maxDepth);
        } else if (key == 'z') {
            maxDepth = constrain(maxDepth + 10, minDepth, 2047);
        } else if (key == 'x') {
            maxDepth = constrain(maxDepth - 10, minDepth, 2047);
        }
    }
}
