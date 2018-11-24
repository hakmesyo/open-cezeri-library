package cezeri.device.kinect;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PApplet;
import static processing.core.PConstants.TWO_PI;
import processing.core.PImage;

public class Desert_Flowers extends PApplet {

    PImage img;
    PImage banner;
    //PImage flower;
    List<Flower> flowersArray = new CopyOnWriteArrayList<>();
    Iterator<Flower> iterator = flowersArray.iterator();
    static CizWithKinect ciz;
    int cl;
    int cellSize = 10;
    Rectangle roi;
    double mapX, mapY;
    int px = -100, py = -100;

    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.Desert_Flowers"});
    }

    public void runApplet(CizWithKinect c) {
//        PApplet.main(new String[]{"--present", "animations.Desert_Flowers"});
        PApplet.main(new String[]{"cezeri.device.kinect.Desert_Flowers"});
        ciz = c;
    }

    public void settings() {
        size(800, 600);
//        fullScreen();
    }

    public void setup() {
//        img = loadImage("c:\\data\\desert_flowers\\desert_7.jpg");
        img = loadImage("c:\\data\\desert_flowers\\grass_3.jpg");
        banner = loadImage("c:\\data\\banner\\banner_1.png");
        img.resize(800, 600);
        banner.resize(img.width, img.height);
//        smooth();
//        frameRate(15);
    }

    public void draw() {
        image(img, 0, 0);
        Point p = locationHandler();
        if (p != null) {
            motionEvent(p.x, p.y);
            px = p.x;
            py = p.y;
        }
        tint(255, 100);
        image(banner, 0, 0);
        tint(255, 255);
        textSize(16);
        text("Frame rate: " + (int) frameRate, 10, 20);
        removeOldFlowers();
        renderFlowers();
        produceNewFlowers(1, px, py);

    }
    int delay = 10;

    public void motionEvent(int x, int y) {
        if (delay <= 0) {
            delay = 10;
            produceNewFlowers(1, x, y);
            px = x;
            py = y;
        }else{
            delay--;
        }
    }

    private void renderFlowers() {
        for (Flower fl : flowersArray) {
            fl.render();
        }
    }

    public void mouseMoved() {
        motionEvent(mouseX, mouseY);
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

    private void produceNewFlowers(float f, int x, int y) {
//        System.out.println("flowers count:" + flowersArray.size());
        for (int i = 0; i < (int) f; i++) {
            Flower flo = new Flower(x, y);
            flowersArray.add(flo);
        }
    }

    private void removeOldFlowers() {
        for (Flower fl : flowersArray) {
            if (fl.alpha <= 0) {
                flowersArray.remove(fl);
            }
        }
    }

    public class Flower {

        Rectangle r;
        float counter=0;
        float decay = 0.9f;
        PImage img;
        int alpha = 500;
        Point endP;
        int rx, ry;
        int delta = 20;
        float vx=0, vy=0;
        float incr_x = 10f;
        float incr_y = 10f;
        float shiftDecay=0.03f;
        int direction=1;
        float rotateDelta=30;

        public Flower(int x, int y) {
            float d = 0.1f;
            rx = (int) random(-delta, delta);
            ry = (int) random(-delta, delta);
            vx = random(-d, d);
            vy = random(-d, d);
            direction=(random(-1,1)<0)?-1:1;
            if (vx <= 0) {
                incr_x = -incr_x;
            }
            if (vy <= 0) {
                incr_y = -incr_y;
            }
            r = new Rectangle(x + rx, y + ry, 50, 50);
            img = loadImage("c:\\data\\desert_flowers\\papatya.png");
            img.resize(r.width, r.height);
        }

        public void render() {
            incr_x=incr_x*shiftDecay;
            incr_y=incr_y*shiftDecay;
            vx += incr_x;
            vy += incr_y;
            r.x+=vx;
            r.y+=vy;
            resetMatrix();
            int rx = img.width/2 + r.x;
            int ry = img.height/2 + r.y;
            translate(rx, ry);
            rotateDelta=rotateDelta*decay;
            counter+=rotateDelta;
            if (direction<0) {                
                rotate(-counter * TWO_PI / 360);
            }else{
                rotate(counter * TWO_PI / 360);
            }            
            translate(-rx, -ry);
            alpha -= 10;
            tint(255, alpha);
            image(img, r.x , r.y);
            tint(255, 255);
        }

    }
}
