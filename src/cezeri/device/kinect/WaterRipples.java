package cezeri.device.kinect;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class WaterRipples extends PApplet {

    PImage img;
    PImage banner;
    Ripple ripple;
    static CizWithKinect ciz;
    PFont f;
    int cl;
    int cellSize = 10;
    Rectangle roi;
    double mapX, mapY;

    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.WaterRipples"});
    }

    public String tersYaz(String s) {
        String ss = "";
        for (int i = 0; i < s.length(); i++) {
            ss += s.charAt(s.length() - 1 - i);
        }
        return ss;
    }

    public void runApplet(CizWithKinect c) {
        PApplet.main(new String[]{"--present", "animations.WaterRipples"});
        ciz = c;
    }

    @Override
    public void settings() {
        size(800, 600);
//        fullScreen();
    }

    public void setup() {
        img = loadImage("c:\\data\\water_ripples\\water_zemin.jpg");
        banner = loadImage("c:\\data\\banner\\banner_1.png");
        img.resize(800, 600);
        banner.resize(img.width, img.height);
        //size(img.width, img.height);
        ripple = new Ripple();
        frameRate(500);
    }

    public void draw() {
        loadPixels();
        img.loadPixels();
        for (int loc = 0; loc < width * height; loc++) {
            pixels[loc] = ripple.col[loc];
        }
        updatePixels();
        ripple.newframe();
        Point p = locationHandler();
        if (p != null) {
            motionEvent(p.x, p.y);
        }
        tint(255, 100);
        image(banner, 0, 0);
        textSize(16);
        text("Frame rate: " + (int) frameRate, 10, 20);
    }

    private Point locationHandler() {
        Point ret = null;
        if (ciz != null) {
//            ret = ciz.getLocation();
//            roi = ciz.getRoi();
//            mapX = width * 1.0 / roi.width;
//            mapY = height * 1.0 / roi.height;
//            ret.x = (int) Math.round(ret.x * mapX);
//            ret.y = (int) Math.round(ret.y * mapY);
//            System.out.println("x,y:" + ret.x + ":" + ret.y);
        } else {
//            System.out.println("ciz null geldi");
        }
        return ret;
    }

    private String readFile(String path) {
        String str = "";
        try {
            File fin = new File(path);

            // Construct BufferedReader from FileReader
            BufferedReader br = new BufferedReader(new FileReader(fin));

            String line = "";
            while ((line = br.readLine()) != null) {
                str += line + ";";
            }
            br.close();

            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.close();

            //if(deleteFile(path)){
            //newFile(path);
            //}
        } catch (IOException ex) {
        }
        return str;
    }

    class Ripple {

        int i, a, b;
        int oldind, newind, mapind;
        short ripplemap[]; // the height map
        int col[]; // the actual pixels
        int riprad;
        int rwidth, rheight;
        int ttexture[];
        int ssize;

        public Ripple() {
            // constructor
            riprad = 3;
            rwidth = width >> 1;
            rheight = height >> 1;
            ssize = width * (height + 2) * 2;
            ripplemap = new short[ssize];
            col = new int[width * height];
            ttexture = new int[width * height];
            oldind = width;
            newind = width * (height + 3);
        }

        void newframe() {
            // update the height map and the image
            i = oldind;
            oldind = newind;
            newind = i;
            int sabit = 1024;

            i = 0;
            mapind = oldind;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    short data = (short) ((ripplemap[mapind - width] + ripplemap[mapind + width]
                            + ripplemap[mapind - 1] + ripplemap[mapind + 1]) >> 1);
                    data -= ripplemap[newind + i];
                    data -= data >> 5;
                    if (x == 0 || y == 0) // avoid the wraparound effect
                    {
                        ripplemap[newind + i] = 0;
                    } else {
                        ripplemap[newind + i] = data;
                    }

                    // where data = 0 then still, where data > 0 then wave
                    data = (short) (sabit - data);

                    // offsets
                    a = ((x - rwidth) * data / sabit) + rwidth;
                    b = ((y - rheight) * data / sabit) + rheight;

                    //bounds check
                    if (a >= width) {
                        a = width - 1;
                    }
                    if (a < 0) {
                        a = 0;
                    }
                    if (b >= height) {
                        b = height - 1;
                    }
                    if (b < 0) {
                        b = 0;
                    }

                    col[i] = img.pixels[a + (b * width)];
                    mapind++;
                    i++;
                }
            }
        }
    }

    public void motionEvent(int x, int y) {
        for (int j = y - ripple.riprad; j < y + ripple.riprad; j++) {
            for (int k = x - ripple.riprad; k < x + ripple.riprad; k++) {
                if (j >= 0 && j < height && k >= 0 && k < width) {
                    ripple.ripplemap[ripple.oldind + (j * width) + k] += 512;
                }
            }
        }
    }

    public void mouseMoved() {
        motionEvent(mouseX, mouseY);
    }
}
