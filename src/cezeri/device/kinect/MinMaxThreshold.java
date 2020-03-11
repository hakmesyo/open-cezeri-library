package cezeri.device.kinect;

import org.openkinect.processing.Kinect;
import processing.core.PApplet;
import static processing.core.PConstants.P3D;
import processing.core.PImage;

public class MinMaxThreshold extends PApplet {

    Kinect kinect;
    float angle;
    PImage depthImg;
// Which pixels do we care about?
    int minDepth = 60;
    int maxDepth = 860;

    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.MinMaxThreshold"});
    }

    @Override
    public void settings() {
        size(640, 480);        
    }

    @Override
    public void setup() {
        kinect = new Kinect(this);
        kinect.initDepth();
        depthImg = new PImage(kinect.width, kinect.height);
    }

    public void draw() {
        image(kinect.getDepthImage(), 0, 0);

        // Threshold the depth image
        int[] rawDepth = kinect.getRawDepth();
        for (int i = 0; i < rawDepth.length; i++) {
            if (rawDepth[i] >= minDepth && rawDepth[i] <= maxDepth) {
                depthImg.pixels[i] = color(255);
            } else {
                depthImg.pixels[i] = color(0);
            }
        }
        // Draw the thresholded image
        depthImg.updatePixels();
        image(depthImg, kinect.width, 0);

        depthImg.updatePixels();
        image(depthImg, 0, 0);
        fill(0);
        text("TILT: " + angle, 10, 20);
        frameRate(200);
        text("Frame rate: " + (int) frameRate, 10, 50);
    }

    @Override
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                angle++;
            } else if (keyCode == DOWN) {
                angle--;
            }
        }
    }

}
