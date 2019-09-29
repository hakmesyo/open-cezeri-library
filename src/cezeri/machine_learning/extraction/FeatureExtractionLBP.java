/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.machine_learning.feature_extraction;

import cezeri.image_processing.ImageProcess;
import cezeri.matrix.FactoryMatrix;
import cezeri.utils.FactoryUtils;
import java.awt.image.BufferedImage;

/**
 *
 * @author musa-atas
 */
public class FeatureExtractionLBP {

    /**
     * 18.04.2014 Musa Ataş Bir matris imgesinin Local Binary Pattern öznitelik
     * vektörünü bulur.
     */
    public static int[] getLBP(BufferedImage img,boolean isRegular) {
        int[][] dizi = ImageProcess.imageToPixelsInt(img);
        return getLBP(dizi,isRegular);
    }

    public static int[] getLBP(int[][] img, boolean isRegular) {
        img = FactoryUtils.transpose(img);
        int[] ret;
        int[] lbp = new int[(img.length - 2) * (img[0].length - 2)];
        int[] map = new int[256];
        int[] mapping = getMapping();
        int k = 0;
        for (int x = 1; x < img.length - 1; x++) {
            //dış for, satırları sayıyor
            for (int y = 1; y < img[0].length - 1; y++) {
                //iç for, sütunları sayıyor
                String s = getLBPPattern(img, x, y);
                if (isRegular) {
                    if (isRegularLbp(s)) {
                        map[Integer.valueOf(s, 2)] = 1;
                    } else {
                        map[Integer.valueOf(s, 2)] = -1;
                    }
                }
                lbp[k] = Integer.valueOf(s, 2);
                k++;
            }
        }
        if (isRegular) {
            ret = getHistogram(lbp, mapping, 256);
        }else{
            ret = ImageProcess.getHistogram(lbp);
        }
        return ret;
    }

//    /**
//     * return 8 bit LBP for 1D signal by default 58 unique patterns are taken
//     * into account
//     *
//     * @param 1D signal
//     * @return
//     */
//    public static int[] getLBP(double[] d) {
//        int N = 8;  //8 bit
//        int[] ret = new int[d.length];
//        int[] lbp = new int[d.length];
//        int[] map = new int[256];
//        int[] mapping = getMapping();
//        for (int i = 0; i < d.length; i++) {
//            String s = getLBPPattern(d, i, N);
//            if (isRegularLbp(s)) {
//                map[Integer.valueOf(s, 2)] = 1;
//            } else {
//                map[Integer.valueOf(s, 2)] = -1;
//            }
//            lbp[i] = Integer.valueOf(s, 2);
//        }
//        ret = getHistogram(lbp, mapping, 256);
//        return ret;
//    }

    /**
     * N bit LBP for 1D signal
     *
     * @param d:1D signal
     * @param N:for N bit i.e:8 bit produces totally 256 LBP features
     * @param isRegular:considering regularity of LBP for 58 represantative
     * pattern
     * @return LBP histogram
     */
    public static int[] getLBP(double[] d, int N, boolean isRegular) {
        int[] ret = new int[d.length];
        int[] lbp = new int[d.length];
        int n = (int) Math.pow(2, N);
        int[] map = new int[n];
        int[] mapping = getMapping();
        for (int i = 0; i < d.length; i++) {
            String s = getLBPPattern(d, i, N);
            if (isRegular) {
                if (isRegularLbp(s)) {
                    map[Integer.valueOf(s, 2)] = 1;
                } else {
                    map[Integer.valueOf(s, 2)] = -1;
                }
            }
            lbp[i] = Integer.valueOf(s, 2);
        }
        if (isRegular) {
            ret = getHistogram(lbp, mapping, n);
        } else {
            ret = ImageProcess.getHistogram(lbp,n);
        }
        return ret;
    }
    
    public static int[][] getLBP(double[][] d, int N, boolean isRegular) {
        int[][] ret = new int[d[0].length][];
        d=FactoryMatrix.transpose(d);
        int nr=d.length;
        for (int i = 0; i < nr; i++) {
            ret[i]=getLBP(d[i], N, isRegular);
        }
        return FactoryMatrix.transpose(ret);
    }

    private static int[] getMapping() {
        int[] ret = new int[256];
        int index = 0;
        for (int i = 0; i <= 255; i++) {
            if (isRegularLbp(Integer.toBinaryString(i))) {
                ret[i] = index;
                index++;
            } else {
                ret[i] = 58;
            }
        }
        return ret;
    }

    private static String addZeros(String s) {
        if (s.length() == 8) {
            return s;
        }
        String str = "";
        int n = 8 - s.length();
        for (int i = 0; i < n; i++) {
            str += "0";
        }
        str = str + s;
        return str;
    }

    private static boolean isRegularLbp(String str) {
        boolean ret = true;
        int tr = 0;
        String s = addZeros(str);
        char c = s.charAt(0);

        for (int i = 1; i < s.length(); i++) {
            if (c != s.charAt(i)) {
                tr++;
                if (tr > 2) {
                    return false;
                }
                c = s.charAt(i);
            }
        }
        return ret;
    }

    private static int[] getHistogram(int[] d, int[] map, int n) {
        int[] r = new int[n];
        int[] ret = new int[59];
        for (int i = 0; i < d.length; i++) {
            r[d[i]]++;
        }
        int k = 0;
        int t = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[i] == 58) {
                t += r[i];
            } else {
                ret[map[i]] = r[i];
                //k++;

            }
        }
        ret[58] = t;
        return ret;
    }

    private static String getLBPPattern(int[][] img, int x, int y) {
        //x satırları
        //y sütunları temsil etmektedir
        String s = "";
        double n = 2 * Math.PI / 8; //for bilinear interpolation detect angle increment
        int r = 1; //radious
        double[][] spoints = new double[8][2];
        for (int i = 0; i < 8; i++) {
            spoints[i][0] = -Math.sin(i * n);
            spoints[i][1] = Math.cos(i * n);
        }

        s += (img[x][y + 1] < img[x][y]) ? "0" : "1";
        s += (img[x + 1][y + 1] < img[x][y]) ? "0" : "1";
        s += (img[x + 1][y] < img[x][y]) ? "0" : "1";
        s += (img[x + 1][y - 1] < img[x][y]) ? "0" : "1";
        s += (img[x][y - 1] < img[x][y]) ? "0" : "1";
        s += (img[x - 1][y - 1] < img[x][y]) ? "0" : "1";
        s += (img[x - 1][y] < img[x][y]) ? "0" : "1";
        s += (img[x - 1][y + 1] < img[x][y]) ? "0" : "1";

//        for (int i = a + 1; i >= a - 1; i--) {
//            for (int j = b + 1; j >= b - 1; j--) {
//                if (i == a && j == b) {
//                    continue;
//                }
//                if (img[i][j] < img[a][b]) {
//                    s += "0";
//                } else {
//                    s += "1";
//                }
//            }
//        }
        s = FactoryUtils.reverseString(s);
        return s;
    }

//    private static String getLBPPattern(double[] d, int x) {
//        String s = "";
//        s += (x - 4 < 0 || d[x - 4] < d[x]) ? "0" : "1";
//        s += (x - 3 < 0 || d[x - 3] < d[x]) ? "0" : "1";
//        s += (x - 2 < 0 || d[x - 2] < d[x]) ? "0" : "1";
//        s += (x - 1 < 0 || d[x - 1] < d[x]) ? "0" : "1";
//        s += (x + 1 > d.length - 1 || d[x + 1] < d[x]) ? "0" : "1";
//        s += (x + 2 > d.length - 1 || d[x + 2] < d[x]) ? "0" : "1";
//        s += (x + 3 > d.length - 1 || d[x + 3] < d[x]) ? "0" : "1";
//        s += (x + 4 > d.length - 1 || d[x + 4] < d[x]) ? "0" : "1";
//        s = FactoryUtils.reverseString(s);
//        return s;
//    }
    /**
     * constuct LBP string of x'th item of the 1D signal for N bit
     *
     * @param d: 1D signal
     * @param x: processed item (center of the LBP window)
     * @param n: N bit
     * @return
     */
    private static String getLBPPattern(double[] d, int x, int n) {
        String s = "";
        for (int i = n / 2; i > 0; i--) {
            s += (x - i < 0 || d[x - i] < d[x]) ? "0" : "1";
        }
        for (int i = 1; i <= n / 2; i++) {
            s += (x + i > d.length - 1 || d[x + i] < d[x]) ? "0" : "1";
        }
        s = FactoryUtils.reverseString(s);
        return s;
    }

    public static void saveLBPFeatures(int[] f, String fileName) {
        String s = fileName + ";";
        for (int i = 0; i < f.length; i++) {
            s += f[i] + ";";
        }
        s += "\n";
        FactoryUtils.writeOnFile(fileName, s);
    }
}
