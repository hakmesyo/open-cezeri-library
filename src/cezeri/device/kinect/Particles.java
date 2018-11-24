package cezeri.device.kinect;
// Particles, by Daniel Shiffman.

import java.awt.Point;
import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.DISABLE_DEPTH_MASK;
import static processing.core.PConstants.LANDSCAPE;
import static processing.core.PConstants.P2D;
import static processing.core.PConstants.QUAD;
import static processing.core.PConstants.TWO_PI;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

/**
 *
 * @author HP-pc
 */
public class Particles extends PApplet {

    ParticleSystem ps;
    PImage sprite;
    static CizWithKinect ciz;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{"cezeri.device.kinect.Particles"});
    }
    
    public void runApplet(CizWithKinect c) {
        PApplet.main(new String[]{"--present","cezeri.device.kinect.Particles"});
        ciz = c;
    }

    public void settings() {
        size(640, 480);
//        fullScreen();
    }
    
    public void setup() {
//        size(displayWidth, displayHeight, P2D);
        orientation(LANDSCAPE);
        sprite = loadImage("c:\\data\\particles\\sprite.png");
        ps = new ParticleSystem(100);
        hint(DISABLE_DEPTH_MASK);
    }

//    String previousCommand = "2,420,0;3,420,36";
//    int n = 0;

    public void draw() {
        background(0);
        ps.update();
        ps.display();
        if (ciz != null) {
            //Point p = ciz.getLocation();
            //ps.setEmitter(p.x, p.y);
            //System.out.println("x,y:"+p.x+":"+p.y);
        }else{
            System.out.println("ciz null geldi");
        }
        fill(255);
        textSize(16);
        text("Frame rate: " + (int) frameRate, 10, 20);
    }

    class ParticleSystem {

        ArrayList<Particle> particles;

        PShape particleShape;

        ParticleSystem(int n) {
            particles = new ArrayList<Particle>();
            particleShape = createShape(PShape.GROUP);

            for (int i = 0; i < n; i++) {
                Particle p = new Particle();
                particles.add(p);
                particleShape.addChild(p.getShape());
            }
        }

        void update() {
            for (Particle p : particles) {
                p.update();
            }
        }

        void setEmitter(float x, float y) {
            for (Particle p : particles) {
                if (p.isDead()) {
                    p.rebirth(x, y);
                }
            }
        }

        void display() {

            shape(particleShape);
        }
    }

    class Particle {

        PVector velocity;
        float lifespan = 255;

        PShape part;
        float partSize;

        PVector gravity = new PVector(0, 0.1f);

        Particle() {
            partSize = random(10, 60);
            part = createShape();
            part.beginShape(QUAD);
            part.noStroke();
            part.texture(sprite);
            part.normal(0, 0, 1);
            part.vertex(-partSize / 2, -partSize / 2, 0, 0);
            part.vertex(+partSize / 2, -partSize / 2, sprite.width, 0);
            part.vertex(+partSize / 2, +partSize / 2, sprite.width, sprite.height);
            part.vertex(-partSize / 2, +partSize / 2, 0, sprite.height);
            part.endShape();

            rebirth(width / 2, height / 2);
            lifespan = random(255);
        }

        PShape getShape() {
            return part;
        }

        void rebirth(float x, float y) {
            float a = random(TWO_PI);
            float speed = 0.5f + (float) Math.random() * 3.5f;
            velocity = new PVector(cos(a), sin(a));
            velocity.mult(speed);
            lifespan = 255;
            part.resetMatrix();
            part.translate(x, y);
        }

        boolean isDead() {
            if (lifespan < 0) {
                return true;
            } else {
                return false;
            }
        }

        public void update() {
            lifespan = lifespan - 1;
            velocity.add(gravity);

            part.setTint(color(255, lifespan));
            part.translate(velocity.x, velocity.y);
        }
    }

}
