/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.image_processing;

/**
 *
 * @author musa-atas
 */

public class HarrisCornerDetection {

    static int[] input;
    static int[] output;
    int progress;
    int width;
    int height;
    int convolveX[] = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    int convolveY[] = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
    int templateSize = 3;
    double k = 0.04;
    double threshold = 10000000;

    public void harris() {
        progress = 0;
    }

    public void init(int[] inputIn, int widthIn, int heightIn, double k) {
        width = widthIn;
        height = heightIn;
        input = inputIn;
        output = new int[width * height];
        this.k = k;

    }

    public int[] process() {
        progress = 0;

        // first convolute image with x and y templates
        int diffx[] = new int[width * height];
        int diffy[] = new int[width * height];
        int diffxy[] = new int[width * height];
        int valx, valy;

        for (int x = templateSize / 2; x < width - (templateSize / 2); x++) {
            progress++;
            for (int y = templateSize / 2; y < height - (templateSize / 2); y++) {
                valx = 0;
                valy = 0;
                for (int x1 = 0; x1 < templateSize; x1++) {
                    for (int y1 = 0; y1 < templateSize; y1++) {
                        int pos = (y1 * templateSize + x1);
                        int imPos = (x + (x1 - (templateSize / 2))) + ((y + (y1 - (templateSize / 2))) * width);

                        valx += ((input[imPos] & 0xff) * convolveX[pos]);
                        valy += ((input[imPos] & 0xff) * convolveY[pos]);
                        //System.out.println("valx: " + valx + " valy: " + valy);
                        //System.out.println("imVal: " + (input[imPos]&0xff) + " convVal: " + convolveX[pos]);
                    }
                }
                diffx[x + (y * width)] = valx * valx;
                diffy[x + (y * width)] = valy * valy;
                diffxy[x + (y * width)] = valx * valy;
            }
        }

        // apply gaussian filters to difference
        FilterGaussian g = new FilterGaussian();
        g.init(diffx, 2, 12, width, height);
        g.generateTemplate();
        diffx = g.process();
        progress += width;
        g.init(diffy, 2, 12, width, height);
        g.generateTemplate();
        diffy = g.process();
        progress += width;
        g.init(diffxy, 2, 12, width, height);
        g.generateTemplate();
        diffxy = g.process();
        progress += width;

        // now perform "harris corner reposnse" function
        double hcr[] = new double[width * height];
        double val = 0;
        double max = 0;
        double maxA = 0;
        double min = 100;
        double minA = 100;
        double A, B, C;
        for (int x = 0; x < width; x++) {
            progress++;
            for (int y = 0; y < height; y++) {
                A = (diffx[y * width + x]);
                B = (diffy[y * width + x]);
                C = (diffxy[y * width + x]);
                val = ((A * B - (C * C)) - (k * ((A + B) * (A + B))));

                if (val > threshold) {
                    hcr[y * width + x] = val;
                } else {
                    hcr[y * width + x] = 0;
                }
                //System.out.println("diffx:"+ diffx[y * width + x] + "  diffy:"+ diffy[y * width + x] + "  diffxy:"+ diffxy[y * width + x] );
                //System.out.println("HCR: " + hcr[y * width + x]);

            }
        }

        // do non-max suppression on the hcr
        NonMax nmOp = new NonMax();
        nmOp.init(hcr, width, height);
        double nm[] = nmOp.process();

        for (int x = 0; x < width; x++) {
            progress++;
            for (int y = 0; y < height; y++) {
                if ((nm[y * width + x]) == 0) {
                    val = ((input[y * width + x] & 0xff) + 255) / 2;//hcr[y * width + x];//&0xff;
                } else {
                    //System.out.println("Got value " + (nm[y * width + x]&0xff));
                    val = 0;

                }
                //val = hcr[y * width + x];
                output[y * width + x] = 0xff000000 | ((int) (val) << 16 | (int) (val) << 8 | (int) (val));
            }
        }


        return output;
    }

    public int getProgress() {
        return progress / 6;

    }
}
