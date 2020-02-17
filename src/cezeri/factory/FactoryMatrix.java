package cezeri.factory;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import static cezeri.factory.FactoryUtils.showMessage;
import static cezeri.factory.FactoryUtils.toDoubleArray1D;
import cezeri.matrix.Dimension;
import cezeri.utils.PerlinNoise;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import jwave.Transform;
import jwave.transforms.FastWaveletTransform;
import jwave.transforms.wavelets.haar.Haar1;
import jwave.transforms.wavelets.daubechies.Daubechies2;
import jwave.transforms.wavelets.daubechies.Daubechies3;
import jwave.transforms.wavelets.daubechies.Daubechies4;
import jwave.transforms.wavelets.daubechies.Daubechies5;
import jwave.transforms.wavelets.daubechies.Daubechies6;
import jwave.transforms.wavelets.daubechies.Daubechies7;

/**
 * @author Dr. Musa ATAŞ It is developed for performance issues for
 * resource-constrained devices
 */
public final class FactoryMatrix implements Serializable {

    private final static int BLOCK_WIDTH = 60;
    private final static int BLOCK_WIDTH_CHOL = 20;
    private final static int BLOCK_SIZE = BLOCK_WIDTH * BLOCK_WIDTH;
    private final static int BLOCK_THR = 10000;
    private final static double MACHEPS = 2E-16;

    /**
     * generates two dimensional Type array include default values
     *
     * @param nRows:i
     * @param nCols:i
     * @param obj:i
     * @return q
     */
    public static Object[][] matrixObjectDefault(int nRows, int nCols, Object obj) {
        Object[][] ret = new Object[nRows][nCols];
        for (Object[] ret1 : ret) {
            for (Object item : ret1) {
                item = new Object();
            }
        }
        return ret;
    }

    /**
     * generates two dimensional double array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static double[][] matrixDoubleZeros(int nRows, int nCols) {
        return new double[nRows][nCols];
    }

    public static double[] matrixDoubleZeros1D(int nRows) {
        return new double[nRows];
    }

    public static double[][] matrixDoubleZeros(int p) {
        return matrixDoubleZeros(p, p);
    }

    public static double[] matrixDoubleOnes1D(int nRows) {
        double[] d = new double[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1.0;
        }
        return d;
    }

    public static double[] matrixDoubleValue1D(int nRows, double val) {
        double[] d = new double[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static double[][] matrixDoubleOnes(int p) {
        return matrixDoubleValue(p, p, 1.0);
    }

    public static double[][] matrixDoubleOnes(int nRows, int nCols) {
        return matrixDoubleValue(nRows, nCols, 1.0);
    }

    public static double[][] matrixDoubleValue(int p, double val) {
        return matrixDoubleValue(p, p, val);
    }

    public static double[][] matrixDoubleValue(int nRows, int nCols, double val) {
        double[][] d = new double[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional float array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static float[][] matrixFloatZeros(int nRows, int nCols) {
        return new float[nRows][nCols];
    }

    public static float[] matrixFloatZeros1D(int nRows) {
        return new float[nRows];
    }

    public static float[][] matrixFloatZeros(int p) {
        return matrixFloatZeros(p, p);
    }

    public static float[] matrixFloatOnes1D(int nRows) {
        float[] d = new float[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1.0f;
        }
        return d;
    }

    public static float[] matrixFloatValue1D(int nRows, float val) {
        float[] d = new float[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static float[][] matrixFloatOnes(int p) {
        return matrixFloatValue(p, p, 1.0f);
    }

    public static float[][] matrixFloatOnes(int nRows, int nCols) {
        return matrixFloatValue(nRows, nCols, 1.0f);
    }

    public static float[][] matrixFloatValue(int p, float val) {
        return matrixFloatValue(p, p, val);
    }

    public static float[][] matrixFloatValue(int nRows, int nCols, float val) {
        float[][] d = new float[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional int array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static int[][] matrixIntZeros(int nRows, int nCols) {
        return new int[nRows][nCols];
    }

    public static int[] matrixIntZeros1D(int nRows) {
        return new int[nRows];
    }

    public static int[][] matrixIntZeros(int p) {
        return matrixIntZeros(p, p);
    }

    public static int[] matrixIntOnes1D(int nRows) {
        int[] d = new int[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1;
        }
        return d;
    }

    public static int[] matrixIntValue1D(int nRows, int val) {
        int[] d = new int[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1;
        }
        return d;
    }

    public static int[][] matrixIntOnes(int p) {
        return matrixIntValue(p, p, 1);
    }

    public static int[][] matrixIntOnes(int nRows, int nCols) {
        return matrixIntValue(nRows, nCols, 1);
    }

    public static int[][] matrixIntValue(int p, int val) {
        return matrixIntValue(p, p, val);
    }

    public static int[][] matrixIntValue(int nRows, int nCols, int val) {
        int[][] d = new int[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional byte array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static byte[][] matrixByteZeros(int nRows, int nCols) {
        return new byte[nRows][nCols];
    }

    public static byte[] matrixByteZeros1D(int nRows) {
        return new byte[nRows];
    }

    public static byte[][] matrixByteZeros(int p) {
        return matrixByteZeros(p, p);
    }

    public static byte[] matrixByteOnes1D(int nRows) {
        byte[] d = new byte[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1;
        }
        return d;
    }

    public static byte[] matrixByteValue1D(int nRows, byte val) {
        byte[] d = new byte[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static byte[][] matrixByteOnes(int p) {
        return matrixByteValue(p, p, (byte) 1);
    }

    public static byte[][] matrixByteOnes(int nRows, int nCols) {
        return matrixByteValue(nRows, nCols, (byte) 1);
    }

    public static byte[][] matrixByteValue(int p, byte val) {
        return matrixByteValue(p, p, val);
    }

    public static byte[][] matrixByteValue(int nRows, int nCols, byte val) {
        byte[][] d = new byte[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional long array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static long[][] matrixLongZeros(int nRows, int nCols) {
        return new long[nRows][nCols];
    }

    public static long[] matrixLongZeros1D(int nRows) {
        return new long[nRows];
    }

    public static long[][] matrixLongZeros(int p) {
        return matrixLongZeros(p, p);
    }

    public static long[] matrixLongOnes1D(int nRows) {
        long[] d = new long[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1;
        }
        return d;
    }

    public static long[] matrixLongValue1D(int nRows, long val) {
        long[] d = new long[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static long[][] matrixLongOnes(int p) {
        return matrixLongValue(p, p, (long) 1);
    }

    public static long[][] matrixLongOnes(int nRows, int nCols) {
        return matrixLongValue(nRows, nCols, (long) 1);
    }

    public static long[][] matrixLongValue(int p, long val) {
        return matrixLongValue(p, p, val);
    }

    public static long[][] matrixLongValue(int nRows, int nCols, long val) {
        long[][] d = new long[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional short array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static short[][] matrixShortZeros(int nRows, int nCols) {
        return new short[nRows][nCols];
    }

    public static short[] matrixShortZeros1D(int nRows) {
        return new short[nRows];
    }

    public static short[] matrixShortOnes1D(int nRows) {
        short[] d = new short[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = 1;
        }
        return d;
    }

    public static short[] matrixShortValue1D(int nRows, short val) {
        short[] d = new short[nRows];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static short[][] matrixShortZeros(int p) {
        return matrixShortZeros(p, p);
    }

    public static short[][] matrixShortOnes(int p) {
        return matrixShortValue(p, p, (short) 1);
    }

    public static short[][] matrixShortOnes(int nRows, int nCols) {
        return matrixShortValue(nRows, nCols, (short) 1);
    }

    public static short[][] matrixShortValue(int p, short val) {
        return matrixShortValue(p, p, val);
    }

    public static short[][] matrixShortValue(int nRows, int nCols, short val) {
        short[][] d = new short[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional boolean array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static boolean[][] matrixBooleanFalse(int nRows, int nCols) {
        return new boolean[nRows][nCols];
    }

    public static boolean[][] matrixBooleanFalse(int p) {
        return matrixBooleanFalse(p, p);
    }

    public static boolean[] matrixBooleanFalse1D(int p) {
        return new boolean[p];
    }

    public static boolean[][] matrixBooleanTrue(int p) {
        return matrixBooleanValue(p, p, true);
    }

    public static boolean[] matrixBooleanTrue1D(int p) {
        return matrixBooleanValue1D(p, true);
    }

    public static boolean[][] matrixBooleanTrue(int nRows, int nCols) {
        return matrixBooleanValue(nRows, nCols, true);
    }

    public static boolean[][] matrixBooleanValue(int p, boolean val) {
        return matrixBooleanValue(p, p, val);
    }

    public static boolean[][] matrixBooleanValue(int nRows, int nCols, boolean val) {
        boolean[][] d = new boolean[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    public static boolean[] matrixBooleanValue1D(int nRows, boolean val) {
        boolean[] d = new boolean[nRows];
        for (int i = 0; i < nRows; i++) {
            d[i] = val;
        }
        return d;
    }

    /**
     * generates two dimensional char array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static char[][] matrixCharEmpty(int nRows, int nCols) {
        return new char[nRows][nCols];
    }

    public static char[][] matrixCharEmpty(int p) {
        return matrixCharEmpty(p, p);
    }

    public static char[] matrixCharEmpty1D(int p) {
        return new char[p];
    }

    public static char[] matrixCharValue1D(int p, char val) {
        char[] d = new char[p];
        for (int i = 0; i < p; i++) {
            d[i] = val;
        }
        return d;
    }

    public static char[][] matrixCharValue(int p, char val) {
        char[][] d = new char[p][p];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    public static char[][] matrixCharValue(int nRows, int nCols, char val) {
        char[][] d = new char[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * generates two dimensional String array
     *
     * @param nRows:i
     * @param nCols:i
     * @return q
     */
    public static String[][] matrixStringEmpty(int nRows, int nCols) {
        return new String[nRows][nCols];
    }

    public static String[][] matrixStringEmpty(int p) {
        return matrixStringEmpty(p, p);
    }

    public static String[] matrixStringEmpty1D(int p) {
        return new String[p];
    }

    public static String[] matrixStringValue1D(int p, String val) {
        String[] d = new String[p];
        for (int i = 0; i < d.length; i++) {
            d[i] = val;
        }
        return d;
    }

    public static String[][] matrixStringValue(int p, String val) {
        String[][] d = new String[p][p];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    public static String[][] matrixStringValue(int nRows, int nCols, String val) {
        String[][] d = new String[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                d[i][j] = val;
            }
        }
        return d;
    }

    /**
     * ************************************************************************
     * Clone Operation : using fastest method of System.arraycopy()
     * ************************************************************************
     * @param input:i
     * @return q
     */
    public static int[][] clone(int[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        int[][] result = new int[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static int[] clone(int[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int[] result = new int[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static double[][] clone(double[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        double[][] result = new double[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
            //clone can not be as fast as System.arraycopy
//            result[i] = input[i].clone();
        }
        return result;
    }

    public static double[] clone(double[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        double[] result = new double[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static float[][] clone(float[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        float[][] result = new float[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static float[] clone(float[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        float[] result = new float[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static short[][] clone(short[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        short[][] result = new short[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static short[] clone(short[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        short[] result = new short[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static long[][] clone(long[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        long[][] result = new long[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static long[] clone(long[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        long[] result = new long[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static byte[][] clone(byte[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        byte[][] result = new byte[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static byte[] clone(byte[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        byte[] result = new byte[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static boolean[][] clone(boolean[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        boolean[][] result = new boolean[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static boolean[] clone(boolean[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        boolean[] result = new boolean[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static char[][] clone(char[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        char[][] result = new char[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static char[] clone(char[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        char[] result = new char[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static String[][] clone(String[][] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        int c = input[0].length;
        String[][] result = new String[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(input[i], 0, result[i], 0, c);
        }
        return result;
    }

    public static String[] clone(String[] input) {
        if (input == null) {
            return null;
        }
        int r = input.length;
        String[] result = new String[r];
        System.arraycopy(input, 0, result, 0, r);
        return result;
    }

    public static void dump(double[][] p) {
        System.out.println(toString(p));
    }

    public static void dump(double[] p) {
        System.out.println(toString(p));
    }

    public static void dump(String str, double[][] p) {
        System.out.println(str);
        System.out.println(toString(p));
    }

    public static void dump(String str, double[] p) {
        System.out.println(str);
        System.out.println(toString(p));
    }

    public static void println(double[][] p) {
        System.out.println(toString(p));
    }

    public static void println(String[][] p) {
        System.out.println(toString(p));
    }

    public static void println(double[] p) {
        System.out.println(toString(p));
    }

    public static String toString(double[][] p) {
        StringBuilder str = new StringBuilder("[" + p.length + "x" + p[0].length + "]=\n");
        for (double[] p1 : p) {
            for (int j = 0; j < p[0].length; j++) {
                str.append(p1[j]);
                str.append("\t");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static String toString(String[][] p) {
        StringBuilder str = new StringBuilder("[" + p.length + "x" + p[0].length + "]=\n");
        for (String[] p1 : p) {
            for (int j = 0; j < p[0].length; j++) {
                str.append(p1[j]);
                str.append("\t");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static String toString(double[] p) {
        StringBuilder str = new StringBuilder("[" + p.length + "]=\n");
        for (double p1 : p) {
            str.append(p1);
            str.append("\t");
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * ************************************************************************
     * Matrix Transpose Methods : faster approach is used here
     * ************************************************************************
     * @param d:i
     * @return q
     */
    public static double[][] transposeSlower(double[][] d) {
        int r = d.length;
        int c = d[0].length;
        double[][] ret = new double[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                ret[j][i] = d[i][j];
            }
        }
        return ret;
    }

    /**
     * Efficient and fast matrix transpose in terms of memory and computation
     *
     * @param d:i
     * @return q
     */
    public static double[][] transposeSquare(double[][] d) {
        double temp;
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < (r / 2 + 1); i++) {
            for (int j = i; j < c; j++) {
                temp = d[i][j];
                d[i][j] = d[j][i];
                d[j][i] = temp;
            }
        }
        return d;
    }

    public static double[][] transposeBlock(double[][] d) {
        int r = d.length;
        int c = d[0].length;
        double[][] ret = new double[c][r];
        if (r == c) {
            ret = transposeSquare(d);
        } else if (r > BLOCK_THR && c > BLOCK_THR) {
            ret = transposeBlock(d, BLOCK_WIDTH);
        } else {
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    ret[j][i] = d[i][j];
                }
            }
        }
        return ret;
    }

    private static double[][] transposeBlock(double[][] d, final int blockLength) {
        long time = FactoryUtils.tic();
        int r = d.length;
        int c = d[0].length;
        double[][] t = new double[c][r];
        double[] d1 = FactoryUtils.toDoubleArray1D(d);
        time = FactoryUtils.toc("1d cost:", time);
        double[] t1 = new double[r * c];

        for (int i = 0; i < r; i += blockLength) {
            int blockHeight = Math.min(blockLength, r - i);

            int indexSrc = i * c;
            int indexDst = i;

            for (int j = 0; j < c; j += blockLength) {
                int blockWidth = Math.min(blockLength, c - j);
                int indexSrcEnd = indexSrc + blockWidth;
                for (; indexSrc < indexSrcEnd; indexSrc++) {
                    int rowSrc = indexSrc;
                    int rowDst = indexDst;
                    int end = rowDst + blockHeight;
                    for (; rowDst < end; rowSrc += c) {
                        // faster to write in sequence than to read in sequence
                        t1[rowDst++] = d1[rowSrc];
                    }
                    indexDst += r;
                }
            }
        }
        time = FactoryUtils.toc("block cost:", time);
        t = FactoryUtils.to2Ddouble(t1, c, r);
        time = FactoryUtils.toc("2d cost:", time);
        return t;
    }

    public static double[][] transpose(double[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static float[][] transpose(float[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        float[][] ret = new float[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static int[][] transpose(int[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        int[][] ret = new int[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static long[][] transpose(long[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        long[][] ret = new long[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static byte[][] transpose(byte[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        byte[][] ret = new byte[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static short[][] transpose(short[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        short[][] ret = new short[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static char[][] transpose(char[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        char[][] ret = new char[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static boolean[][] transpose(boolean[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        boolean[][] ret = new boolean[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static String[][] transpose(String[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        String[][] ret = new String[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static <T> T[][] deepCopy(T[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    public static <T> T[][] deepClone(T[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    public static Object[][] transpose(Object[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        Object[][] dc = deepCopy(d);
        Object[][] ret = new Object[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                //ret[i][j] = FactoryUtils.deepClone(dc[j][i]);
                ret[i][j] = dc[j][i];
            }
        }
        return ret;
    }

    /**
     * *************************************************************************
     * Populate Array with Random number
     * *************************************************************************
     * @param p:i
     * @return q
     */
    public static double[][] matrixDoubleRandom(int p) {
        return matrixDoubleValue(p, Math.random());
    }

    public static double[][] matrixDoubleRandom(int r, int c) {
        return matrixDoubleValue(r, c, Math.random());
    }

    public static double[][] matrixDoubleRandom(int r, int c, int from, int to) {
        return matrixDoubleValue(r, c, Math.random() * (to - from) + from);
    }

    public static float[][] matrixFloatRandom(int p) {
        return matrixFloatValue(p, (float) Math.random());
    }

    public static float[][] matrixFloatRandom(int r, int c) {
        return matrixFloatValue(r, c, (float) Math.random());
    }

    public static float[][] matrixFloatRandom(int r, int c, int from, int to) {
        return matrixFloatValue(r, c, (float) (Math.random() * (to - from) + from));
    }

    public static int[][] matrixIntRandom(int p) {
        return matrixIntValue(p, (int) Math.round(Math.random()));
    }

    public static int[][] matrixIntRandom(int r, int c) {
        return matrixIntValue(r, c, (int) Math.round(Math.random()));
    }

    public static int[][] matrixIntRandom(int r, int c, int from, int to) {
        return matrixIntValue(r, c, (int) Math.round(Math.random() * (to - from) + from));
    }

    public static byte[][] matrixByteRandom(int p) {
        return matrixByteValue(p, (byte) Math.round(Math.random()));
    }

    public static byte[][] matrixByteRandom(int r, int c) {
        return matrixByteValue(r, c, (byte) Math.round(Math.random()));
    }

    public static byte[][] matrixByteRandom(int r, int c, int from, int to) {
        return matrixByteValue(r, c, (byte) Math.round(Math.random() * (to - from) + from));
    }

    public static short[][] matrixShortRandom(int p) {
        return matrixShortValue(p, (short) Math.round(Math.random()));
    }

    public static short[][] matrixShortRandom(int r, int c) {
        return matrixShortValue(r, c, (short) Math.round(Math.random()));
    }

    public static short[][] matrixShortRandom(int r, int c, int from, int to) {
        return matrixShortValue(r, c, (short) Math.round(Math.random() * (to - from) + from));
    }

    public static long[][] matrixLongRandom(int p) {
        return matrixLongValue(p, (long) Math.round(Math.random()));
    }

    public static long[][] matrixLongRandom(int r, int c) {
        return matrixLongValue(r, c, (long) Math.round(Math.random()));
    }

    public static long[][] matrixLongRandom(int r, int c, int from, int to) {
        return matrixLongValue(r, c, (long) Math.round(Math.random() * (to - from) + from));
    }

    public static double[][] excludeRows(double[][] p1, double[][] p2) {
        int r = p1.length;
        int c = p1[0].length;
        ArrayList<Integer> lst_index = new ArrayList();
        for (int i = 0; i < r; i++) {
            for (double[] item : p2) {
                if (Arrays.equals(p1[i], item)) {
                    if (!lst_index.contains(i)) {
                        if (lst_index.size() == p2.length) {
                            break;
                        }
                        lst_index.add(i);
                    }
                }
            }
        }
        double[][] ret = new double[r - lst_index.size()][c];
        int k = 0;
        for (int i = 0; i < r; i++) {
            if (!lst_index.contains(i)) {
                ret[k++] = Arrays.copyOf(p1[i], c);
            }
        }
        return ret;
    }

//    /**
//     * B.Matlabdeki randperm in aynısı Görevi kendisine verilen n sayısına kadar
//     * random indexler üretmek
//     *
//     * @param n
//     * @return
//     */
    public static int[] randPermInt(int n) {
        int[] m = new int[n];
        ArrayList<Integer> v = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            v.add(new Integer(i));
        }
        for (int i = 0; i < n; i++) {
            int a = new Random().nextInt(n - i);
            m[i] = v.get(a);
            v.remove(a);
        }
        return m;
    }

    public static double[][] shuffle(double[][] ds) {
        double[][] ret = clone(ds);
        int[] d = randPermInt(ds.length);
        int n = d.length;
        for (int i = 0; i < n; i++) {
            ret[i] = ds[d[i]];
        }
        return ret;
    }

    public static float[][] shuffleRows(float[][] ds) {
        float[][] ret = clone(ds);
        int[] d = randPermInt(ds.length);
        int n = d.length;
        for (int i = 0; i < n; i++) {
            ret[i] = ds[d[i]];
        }
        return ret;
    }

    /**
     * it splits the dataset into nFolds disjoint folds for test and remaining
     * as train sets. Hence, it holds 2D CMatrix array from which, first
     * dimensions train and second one is test
     *
     * @param d:i
     * @param nFolds:i
     * @return q
     */
    public static double[][][][] crossValidationSets(double[][] d, int nFolds) {
//        double[][] d=clone(input);
        double[][][][] ret = new double[nFolds][2][d.length][d[0].length];
        int nRows = d.length;
        int nCols = d[0].length;
        int delta = nRows / nFolds;
        double[][] test = new double[delta][d[0].length];
        double[][] train = new double[d.length - delta][d[0].length];
        for (int i = 0; i < nFolds; i++) {
            int from = i * delta;
            int to = from + delta;
            int k = 0;
            for (int j = from; j < to; j++) {
                test[k++] = clone(d[j]);
            }
            if (from == 0) {
                int t = 0;
                for (int j = to; j < nRows; j++) {
                    train[t++] = clone(d[j]);
                }
            } else {
                int t = 0;
                for (int j = 0; j < from; j++) {
                    train[t++] = clone(d[j]);
                }
                for (int j = to; j < nRows; j++) {
                    train[t++] = clone(d[j]);
                }
            }
            ret[i][0] = clone(train);
            ret[i][1] = clone(test);
        }
        return ret;
    }

    /**
     * Tries to interpret String parameter as Matlab vectorization commands like
     * that interpret(d,"5:end","0:3") means get subset based on the specified
     * criteria
     *
     * @param d:i
     * @param p1:i
     * @param p2:i
     * @return q
     */
    public static double[][] interpret(double[][] d, String p1, String p2) {
        p1 = p1.replace("[", "").replace("]", "").replace("(", "").replace(")", "");
        p2 = p2.replace("[", "").replace("]", "").replace("(", "").replace(")", "");
        double[][] ret = clone(d);
        if (p1.equals(":") && p2.equals(":")) {
            return ret;
        }
        if (p1.equals(":")) {
            if (p2.equals(":")) {
                return d;
            } else if (p2.length() == 1) {
                int[] p = {Integer.parseInt(p2)};
                ret = columns(d, p);
            } else {
                int[] p = checkParam(p2, d[0].length);
                ret = columns(d, p);
            }
        } else if (p2.equals(":")) {
            if (p1.equals(":")) {
                return d;
            } else if (p1.length() == 1) {
                int[] p = {Integer.parseInt(p1)};
                ret = rows(d, p);
            } else {
                int[] p = checkParam(p1, d.length);
                ret = rows(d, p);
            }
        } else {
            int[] pp1 = checkParam(p1, d.length);
            int[] pp2 = checkParam(p2, d[0].length);
            ret = transpose(subset(d, pp1, pp2));
        }
        return ret;
    }

    /**
     * subset tries to extract new matrix from the original one based on the
     * specified rows and then columns as in matlab like that
     * a=b([1,4,11],[3,5]); It supports dynamic parameter which means you can
     * call the subset methods with one or more vector
     *
     * Biiznillah Matlab de herhangi bir matrisin içeriğini mesela
     * a=b([1,4,11]); şeklinde alabiliyorduk burada aynı işlem yapılmaya
     * çalışılmıştır. 1 parametre girilse row lardan ilgili index teki verileri
     * filtreler 2 paramatre girilirse hem row hem column filtresi yapar.
     *
     * dynamic parameter
     *
     * @param input:i
     * @param p:i
     * @return q
     */
    public static double[][] subset(double[][] input, int[]... p) {
        double[][] d;
        if (p.length == 0) {
            return null;
        } else if (p.length == 1) {
            int[] rows = p[0];
            d = new double[rows.length][input[0].length];
            for (int i = 0; i < rows.length; i++) {
                d[i] = input[rows[i]];
            }
        } else {
            int[] rows = p[0];
            int[] cols = p[1];
            d = new double[rows.length][input[0].length];
            for (int i = 0; i < rows.length; i++) {
                d[i] = input[rows[i]];
            }
            d = transpose(d);
            double[][] d2 = new double[cols.length][d[0].length];
            for (int i = 0; i < cols.length; i++) {
                d2[i] = d[cols[i]];
            }
            d = transpose(d2);
        }
        double[][] ret = clone(d);
        return ret;
    }

    private static int[] checkParam(String p, int n) {
//        String s = p.substring(1, p.length() - 1);
        String s = p;
        int[] ret = null;
        char[] chr = s.toCharArray();
        if (s.indexOf(":") != -1) {
            String[] ss = s.split(":");
            if (ss.length <= 2) {
                if (ss[1].indexOf("end") != -1) {
                    ss[1] = ss[1].replace("end", (n - 1) + "");
                }
                ret = FactoryUtils.toIntArray1D(FactoryUtils.toDoubleArray1D(vector(Integer.parseInt(ss[0]) * 1.0, Integer.parseInt(ss[1]) * 1.0)));
            } else {
                if (ss[2].indexOf("end") != -1) {
                    ss[2] = ss[2].replace("end", (n - 1) + "");
                }
                ret = FactoryUtils.toIntArray1D(FactoryUtils.toDoubleArray1D(vector(Integer.parseInt(ss[0]) * 1.0, Integer.parseInt(ss[1]) * 1.0, Integer.parseInt(ss[2]) * 1.0)));
            }
        } else {
            if (s.length() > 1) {
//                String[] str = str = s.split(chr[1] + "");
                String[] str;
                if (s.contains(" ")) {
                    str = s.split(" ");
                } else if (s.contains(",")) {
                    str = s.split(",");
                } else if (s.contains(";")) {
                    str = s.split(";");
                } else {
                    ret = new int[1];
                    ret[0] = Integer.parseInt(s);
                    return ret;
                }
                ret = new int[str.length];
                for (int i = 0; i < str.length; i++) {
                    ret[i] = Integer.parseInt(str[i]);
                }
            } else {
//                ret = new int[s.length()];
//                ret[0] = Integer.parseInt(s.charAt(0) + "");
                ret = new int[1];
                ret[0] = Integer.parseInt(s);
                return ret;
            }
        }
        return ret;
    }

    public static double[][] vector(double from, double to) {
        if (from > to) {
            throw new UnsupportedOperationException("from should be smaller than to, other wise use other constructor");
        }
        int n = (int) (Math.abs(to - from) + 1);
        double[][] ret = new double[1][n];
        for (int i = 0; i < n; i++) {
            ret[0][i] = from + i;
        }
        return transpose(ret);
    }

    public static double[][] vector(double from, double incr, double to) {
        if (from < to && incr < 0) {
            throw new UnsupportedOperationException("incr should be positive");
        }
        if (from > to && incr > 0) {
            throw new UnsupportedOperationException("incr should be negative");
        }
        double delta = Math.abs(to - from);
        int n = (int) (delta / incr);
        double[][] ret = new double[1][n];
        for (int i = 0; i < n; i++) {
            ret[0][i] = from + i * incr;
        }
        return transpose(ret);
    }

    public static double[][] columns(double[][] input, int[] cols) {
        double[][] yedek = transpose(input);
        double[][] d = new double[cols.length][yedek[0].length];
        for (int i = 0; i < cols.length; i++) {
            d[i] = yedek[cols[i]];
        }
        double[][] ret = transpose(d);
        return ret;
    }

    public static double[][] rows(double[][] input, int[] rows) {
        double[][] ret = new double[rows.length][input[0].length];
        for (int i = 0; i < rows.length; i++) {
            ret[i] = input[rows[i]];
        }
        return ret;
    }

    /**
     * Sutun bazlı toplama işlemi yapar mxn matrisinin tüm elemanlarını toplamak
     * istiyorsanız sumTotal veya iki defa sum çağırınız
     *
     * @param d:i
     * @return CMatrix
     */
    public static double[] sum(double[][] d) {
        double[] ret = new double[d[0].length];
        double[][] cm = transpose(clone(d));
        for (int i = 0; i < cm.length; i++) {
            ret[i] = FactoryUtils.sum(cm[i]);
        }
        return ret;
    }

    public static double sum(double[] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static double sumTotal(double[][] d) {
        double ret = 0;
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                ret += d[i][j];
            }
        }
        return ret;
    }

    /**
     * Column based inner multiplication similar to the sum method if you want
     * to get total product you should call prodTotal or prod(prod())
     *
     * @param d : input 2D double array
     * @return double[] : column vector
     */
    public static double[] prod(double[][] d) {
        double[] ret = new double[d[0].length];
        double[][] cm = transpose(clone(d));
        for (int i = 0; i < cm.length; i++) {
            ret[i] = FactoryUtils.prod(cm[i]);
        }
        return ret;
    }

    /**
     * get the column wise product of the content in the array
     *
     * @param d:i
     * @return double
     */
    public static double prod(double[] d) {
        double ret = 1;
        for (int i = 0; i < d.length; i++) {
            ret *= d[i];
        }
        return ret;
    }

    public static double prodTotal(double[][] d) {
        double ret = 1;
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < 10; j++) {
                ret *= d[i][j];
            }
        }
        return ret;
    }

    public static double[][] getMinMax(double[][] m) {
        int nr = m.length;
        double[][] ret = new double[nr][2];
        for (int i = 0; i < nr; i++) {
            ret[i][0] = FactoryUtils.getMinimum(m[i]);
            ret[i][1] = FactoryUtils.getMaximum(m[i]);
        }
        return ret;
    }

    public static double[][] dot(double[][] ret, double[][] cm) {
        return dot(ret, cm, Dimension.row);
    }

    public static double[][] dot(double[][] ret, double[][] cm, Dimension dm) {
        int nr = ret.length;
        int nc = ret[0].length;
        if (dm == Dimension.row) {
            double[][] d = new double[nc][1];
            for (int i = 0; i < nc; i++) {
                double sum = 0;
                for (int j = 0; j < nr; j++) {
                    sum += ret[j][i] * cm[j][i];
                }
                d[i][0] = sum;
            }
            return d;
        } else {
            double[][] d = new double[nr][1];
            for (int i = 0; i < nr; i++) {
                double sum = 0;
                for (int j = 0; j < nc; j++) {
                    sum += ret[i][j] * cm[i][j];
                }
                d[i][0] = sum;
            }
            return d;
        }
    }

    /**
     * column based variance calculation (N-1) based
     *
     * @param array:i
     * @return 2D double array
     */
    public static double[] var(double[][] array) {
        double[][] d = transpose(array);
        int nr = array.length;
        int nc = array[0].length;
        double[] ret = new double[nc];
        for (int i = 0; i < nc; i++) {
            ret[i] = FactoryUtils.var(d[i]);
        }
        return ret;
    }

    /**
     * column based standard deviation calculation (N-1) based
     *
     * @param array:i
     * @return 2D double array
     */
    public static double[] std(double[][] array) {
        double[][] d = transpose(array);
        int nr = array.length;
        int nc = array[0].length;
        double[] ret = new double[nc];
        for (int i = 0; i < nc; i++) {
            ret[i] = FactoryUtils.std(d[i]);
        }
        return ret;
    }

    /**
     * column based mean calculation based
     *
     * @param array:i
     * @return 2D double array
     */
    public static double[] mean(double[][] array) {
        double[][] d = transpose(array);
        int nr = array.length;
        int nc = array[0].length;
        double[] ret = new double[nc];
        for (int i = 0; i < nc; i++) {
            ret[i] = FactoryUtils.mean(d[i]);
        }
        return ret;
    }

    /**
     * column based covariance calculation based
     *
     * @param array:i
     * @return 2D double array
     */
    public static double[][] cov(double[][] array) {
        double[][] d = transpose(array);
        int nr = array.length;
        int nc = array[0].length;
        double[][] ret = new double[nc][nc];
        double m_i = 0;
        double m_j = 0;

        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nc; j++) {
                m_i = FactoryUtils.mean(d[i]);
                m_j = FactoryUtils.mean(d[j]);
                double sum = 0;
                for (int k = 0; k < nr; k++) {
                    sum += (d[i][k] - m_i) * (d[j][k] - m_j);
                }
                if (i != j) {
                    sum = sum / (nr - 1);
                } else {
                    sum = FactoryUtils.var(d[i]);
                }
                ret[i][j] = sum;
            }
        }
        return ret;
    }

    /**
     * column based correlation coefficient values
     *
     * @param array
     * @return 2D double array
     */
    public static double[][] corrcoef(double[][] array) {
        double[][] cov = cov(array);
        double[] std = std(array);
        int nr = cov.length;
        int nc = cov[0].length;
        double[][] corr = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                corr[i][j] = cov[i][j] / (std[i] * std[j]);
            }
        }
        return corr;
    }

    /**
     * Calculates pseudo inverse of the given matrix (2D double array)
     *
     * @param array:i
     * @return q
     */
    public static double[][] pinv(double[][] array) {
        Matrix a = new Matrix(array);
        a = pinv(a);
        return a.getArray();
    }

    /**
     * Computes the Moore–Penrose pseudoinverse using the SVD method.
     *
     * Modified version of the original implementation by Kim van der Linde.
     */
    private static Matrix pinv(Matrix x) {
        int rows = x.getRowDimension();
        int cols = x.getColumnDimension();
        if (rows < cols) {
            Matrix result = pinv(x.transpose());
            if (result != null) {
                result = result.transpose();
            }
            return result;
        }
        SingularValueDecomposition svdX = new SingularValueDecomposition(x);
        if (svdX.rank() < 1) {
            return null;
        }
        double[] singularValues = svdX.getSingularValues();
        double tol = Math.max(rows, cols) * singularValues[0] * MACHEPS;
        double[] singularValueReciprocals = new double[singularValues.length];
        for (int i = 0; i < singularValues.length; i++) {
            if (Math.abs(singularValues[i]) >= tol) {
                singularValueReciprocals[i] = 1.0 / singularValues[i];
            }
        }
        double[][] u = svdX.getU().getArray();
        double[][] v = svdX.getV().getArray();
        int min = Math.min(cols, u[0].length);
        double[][] inverse = new double[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < u.length; j++) {
                for (int k = 0; k < min; k++) {
                    inverse[i][j] += v[i][k] * singularValueReciprocals[k] * u[j][k];
                }
            }
        }
        return new Matrix(inverse);
    }

    public static double[][] sigmoid(double[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = 1.0 / (1 + Math.exp(-array[i][j]));
            }
        }
        return ret;
    }

    public static double[][] sigmoid(double[][] array, double beta) {
        int nr = array.length;
        int nc = array[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = 1.0 / (1 + Math.exp(-beta * array[i][j]));
            }
        }
        return ret;
    }

    public static double[][] sigmoid(double[][] array, double alpha, double beta) {
        int nr = array.length;
        int nc = array[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = 1.0 / (1 + Math.exp(-alpha * (array[i][j] - beta)));
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static double[][] padarray(double[][] d, int nr, int nc, double val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        double[][] ret = matrixDoubleValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static float[][] padarray(float[][] d, int nr, int nc, float val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        float[][] ret = matrixFloatValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static int[][] padarray(int[][] d, int nr, int nc, int val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        int[][] ret = matrixIntValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static long[][] padarray(long[][] d, int nr, int nc, long val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        long[][] ret = matrixLongValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static short[][] padarray(short[][] d, int nr, int nc, short val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        short[][] ret = matrixShortValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static byte[][] padarray(byte[][] d, int nr, int nc, byte val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        byte[][] ret = matrixByteValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    /**
     * Matlab compliants padding around the matrix with value padding amount is
     * determined by nr and nc
     *
     * @param d : original matrix
     * @param nr: padding number of rows (padding width as vertical)
     * @param nc: padding number of columns (padding width as horizontal)
     * @param val : padding value
     * @return q
     */
    public static String[][] padarray(String[][] d, int nr, int nc, String val) {
        int r = d.length + 2 * nr;
        int c = d[0].length + 2 * nc;
        String[][] ret = matrixStringValue(r, c, val);
        for (int i = nr; i < r - nr; i++) {
            for (int j = nc; j < c - nc; j++) {
                ret[i][j] = d[i - nr][j - nc];
            }
        }
        return ret;
    }

    public static int[] getHistogram(int[] d, int n) {
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    public static int[] getHistogram(short[] d, int n) {
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j] == i) {
                    ret[i]++;
                }
            }
        }
        return ret;
    }

    public static int[][] getHistogram(int[][][] d, int n) {
        int n1 = d.length;
        int n2 = d[0].length;
        int n3 = d[0][0].length;
        int[][] ret = new int[n3 - 1][n];
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n3; j++) {
                for (int k = 0; k < n1; k++) {
                    for (int m = 0; m < n2; m++) {
                        if (d[k][m][j] == i) {
                            ret[j - 1][i]++;
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static double[][] reverseOrder(double[][] d) {
        double[][] temp = transpose(d);
        double[][] rt = clone(temp);
        int nr = rt.length;
        for (int i = 0; i < nr; i++) {
            rt[i] = reverseVector(temp[i]);
        }
        return transpose(rt);
    }

    private static double[] reverseVector(double[] d) {
        double[] rt = clone(d);
        int n = d.length;
        for (int i = 0; i < n; i++) {
            rt[i] = d[n - 1 - i];
        }
        return rt;
    }

    public static double[][] absDifference(double[][] d1, double[][] d2) {
        double[][] rt = clone(d1);
        int nr = rt.length;
        int nc = rt[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                rt[i][j] = Math.abs(d1[i][j] - d2[i][j]);
            }
        }
        return rt;
    }

    public static double[][] bitPlaneMSB(double[][] a) {
        double[][] ret = clone(a);
        int nr = a.length;
        int nc = a[0].length;
        double[][] d7 = bitPlane(a, 7);
        double[][] d6 = bitPlane(a, 6);
        double[][] d5 = bitPlane(a, 5);
        double[][] d4 = bitPlane(a, 4);
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
//                ret[i][j]=ret[i][j]-15;
                ret[i][j] = d7[i][j] + d6[i][j] + d5[i][j] + d4[i][j];
            }
        }
        return ret;
    }

    public static double[][] bitPlane(double[][] a, int planeNumber) {
        double[][] ret = clone(a);
        int nr = a.length;
        int nc = a[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                char c = FactoryUtils.formatBinary((int) ret[i][j]).toCharArray()[7 - planeNumber];
                char[] cd = "00000000".toCharArray();
                cd[7 - planeNumber] = c;
                ret[i][j] = Integer.parseInt(String.valueOf(cd), 2);
            }
        }
        return ret;
    }

    public static double[][] smoothColumns(double[][] array, int size) {
        double[][] d = clone(array);
        d = transpose(d);
        for (int i = 0; i < d.length; i++) {
            d[i] = smoothVector(d[i], size);
        }
        return transpose(d);
    }

    public static double[] smoothVector(double[] d, int size) {
        return convolveMean1D(d, size);
    }

    public static double[] convolveMean1D(double[] d, int size) {
        if (size % 2 == 0) {
            size++;
            System.out.println("kernel size adjusted to the odd due to the center point for convolution process");
        }
        double[] kernel = new double[size];
        int mp = size / 2;  //mid point
        double mean = 0;
        double[] ret = clone(d);
        for (int i = mp; i < d.length - (mp + 1); i++) {
            mean = 0;
            for (int j = -mp; j <= mp; j++) {
                mean += d[i + j];
            }
            mean = (int) (mean / size);
            ret[i] = mean;
        }
        return ret;
    }

    public static double[][] getMagnitude(double[][] array) {
        double[][] d = clone(array);
        d = transpose(d);
        double[][] ret = new double[d.length][1];
        for (int i = 0; i < ret.length; i++) {
            ret[i][0] = getMagnitude(d[i]);
        }
        return ret;
    }

    public static double getMagnitude(double[] d) {
        double ret = 0;
        double top = 0;
        for (int i = 0; i < d.length; i++) {
            top += d[i] * d[i];
        }
        ret = Math.sqrt(top);
        return ret;
    }

    public static float[] log1p(float[] d) {
        float[] ret = clone(d);
        int length = ret.length;
        for (int i = 0; i < length; i++) {
            if (d[i] <= 0) {
                ret[i] = (float) Math.log1p(Math.abs(d[i]));
            } else {
                ret[i] = (float) Math.log(d[i]);
            }
        }
        return ret;
    }

    /**
     * Get Wavelet Transform Type based on the user input
     *
     * @param type:i
     * @return default is "haar"
     */
    public static Transform getTransform(String type) {
        Transform t = new Transform(new FastWaveletTransform(new Haar1()));
        switch (type) {
            case "haar": {
                t = new Transform(new FastWaveletTransform(new Haar1()));
                break;
            }
            case "db2": {
                t = new Transform(new FastWaveletTransform(new Daubechies2()));
                break;
            }
            case "db3": {
                t = new Transform(new FastWaveletTransform(new Daubechies3()));
                break;
            }
            case "db4": {
                t = new Transform(new FastWaveletTransform(new Daubechies4()));
                break;
            }
            case "db5": {
                t = new Transform(new FastWaveletTransform(new Daubechies5()));
                break;
            }
            case "db6": {
                t = new Transform(new FastWaveletTransform(new Daubechies6()));
                break;
            }
            case "db7": {
                t = new Transform(new FastWaveletTransform(new Daubechies7()));
                break;
            }
        }
        return t;
    }

    public static double[][] fwt_1D_decompose(double[] d, String type) {
        Transform t = getTransform(type);
        double[][] ret = t.decompose(d);
        return ret;
    }

    public static double[] fwt_1D_forward(double[] d, String type, int depth) {
        Transform t = getTransform(type);
        double[] ret = t.forward(d, depth);
        return ret;
    }

    public static double[] fwt_1D_reverse(double[] d, String type, int depth) {
        Transform t = getTransform(type);
        double[] ret = t.reverse(d, depth);
        return ret;
    }

    public static double[] concatenateRows(double[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        double[] ret = new double[nr * nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[j + i * nc] = d[i][j];
            }
        }
        return ret;
    }

    public static double[] concatenateColumns(double[][] d) {
        return concatenateRows(FactoryMatrix.transpose(d));
    }

    public static float[][] cat(String type, float[][] f1, float[] f2) {
        float[][] ret = null;
        if (type.equals("horizontal")) {
            if (f1.length != f2.length) {
                FactoryUtils.showMessage("number of rows of both matrices should be same for horizontal concatenation");
                return f1;
            } else {
                int nr = f1.length;
                int nc = f1[0].length;
                ret = new float[nr][nc + 1];
                for (int i = 0; i < nr; i++) {
                    for (int j = 0; j < nc; j++) {
                        ret[i][j] = f1[i][j];
                    }
                    ret[i][nc] = f2[i];
                }
            }
        } else if (type.equals("vertical")) {
            if (f1[0].length != f2.length) {
                FactoryUtils.showMessage("number of columns of first matrix should be the same as number of rows of the seconf array for vertical concatenation");
                return f1;
            } else {
                int nr = f1.length;
                int nc = f1[0].length;
                ret = new float[nr + 1][nc];
                for (int i = 0; i < nr; i++) {
                    for (int j = 0; j < nc; j++) {
                        ret[i][j] = f1[i][j];
                    }
                }
                for (int j = 0; j < nc; j++) {
                    ret[nr][j] = f2[j];
                }
            }
        }
        return ret;
    }

    public static float[][] cat(String type, float[][] f1, float[][] f2) {
        float[][] ret = null;
        if (type.equals("horizontal")) {
            if (f1.length != f2.length) {
                FactoryUtils.showMessage("number of rows of both matrices should be same for horizontal concatenation");
                return f1;
            } else {
                int nr = f1.length;
                int nc = f1[0].length;
                int ncc = f2[0].length;
                ret = new float[nr][nc + ncc];
                for (int i = 0; i < nr; i++) {
                    for (int j = 0; j < nc; j++) {
                        ret[i][j] = f1[i][j];
                    }
                    for (int j = 0; j < ncc; j++) {
                        ret[i][nc + j] = f2[i][j];
                    }
                }
            }
        } else if (type.equals("vertical")) {
            if (f1[0].length != f2[0].length) {
                FactoryUtils.showMessage("number of columns of first matrix should be the same as number of rows of the seconf array for vertical concatenation");
                return f1;
            } else {
                int nr = f1.length;
                int nrr = f2.length;
                int nc = f1[0].length;
                ret = new float[nr + nrr][nc];
                for (int i = 0; i < nr; i++) {
                    for (int j = 0; j < nc; j++) {
                        ret[i][j] = f1[i][j];
                    }
                }
                for (int i = 0; i < nrr; i++) {
                    for (int j = 0; j < nc; j++) {
                        ret[i + nr][j] = f2[i][j];
                    }
                }
            }
        }
        return ret;
    }

    public static float[][] deleteColumn(float[][] f1, int index) {
        float[][] ret = null;
        if (index < 0 || index >= f1[0].length) {
            FactoryUtils.showMessage("erroneous column index");
            return f1;
        } else {
            float[][] f2 = transpose(f1);
            ret = new float[f1[0].length - 1][f1.length];
            for (int i = 0; i < index; i++) {
                ret[i] = clone(f2[i]);
            }
            for (int i = index; i < ret.length; i++) {
                ret[i] = clone(f2[i + 1]);
            }
        }
        return transpose(ret);
    }

    public static float[][] deleteRow(float[][] f1, int index) {
        float[][] ret = null;
        if (index < 0 || index >= f1.length) {
            FactoryUtils.showMessage("erroneous column index");
            return f1;
        } else {
            ret = new float[f1.length][f1[0].length - 1];
            for (int i = 0; i < index; i++) {
                ret[i] = clone(f1[i]);
            }
            for (int i = index; i < f1.length; i++) {
                ret[i] = clone(f1[i + 1]);
            }
        }
        return ret;
    }

    public static float[][] deleteRows(float[][] f1, int from, int to) {
        float[][] ret = null;
        if (from < 0 || from >= f1.length || from >= to || to < 0 || to >= f1.length) {
            FactoryUtils.showMessage("erroneous column index");
            return f1;
        } else {
            int delta = to - from;
            List lst1 = FactoryUtils.toListFrom2DArray(f1);
            List lst2 = FactoryUtils.toListFrom2DArray(f1);
            for (int i = from; i < to; i++) {
                lst1.remove(lst2.get(i));
            }
            ret = FactoryUtils.to2DArrayFromList(lst1);
        }
        return ret;
    }

    public static double[][] filterMembershipFunction(double[][] d, double[] f) {
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (int) Math.round(f[(int) d[i][j]] * d[i][j]);
            }
        }
        return ret;
    }

    public static double[][] highPass(double[][] d1, double[][] d2) {
        int nr = d1.length;
        int nc = d2[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (d1[i][j] >= d2[i][j]) {
                    ret[i][j] = d1[i][j];
                } else {
                    ret[i][j] = d2[i][j];
                }
            }
        }
        return ret;
    }

    public static double[][] lowPass(double[][] d1, double[][] d2) {
        int nr = d1.length;
        int nc = d2[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (d1[i][j] <= d2[i][j]) {
                    ret[i][j] = d1[i][j];
                } else {
                    ret[i][j] = d2[i][j];
                }
            }
        }
        return ret;
    }

    public static double[] filterMedian1D(double[] d, int window_size) {
        int n = d.length;
        double[] ret = clone(d);
        int center = window_size / 2;
        double[] dm = new double[window_size];
        for (int i = 0; i < center; i++) {
            dm = crop1D(d, i, center);
            Arrays.sort(dm);
            ret[i] = dm[0];
        }
        for (int i = center; i < n - center; i++) {
            dm = crop1D(d, i - center, window_size);
            Arrays.sort(dm);
            ret[i] = dm[center];
        }
        for (int i = n - center; i < n; i++) {
            dm = crop1D(d, i - center, center);
            Arrays.sort(dm);
            ret[i] = dm[center - 1];
        }
        return ret;
    }

    private static double[] filterMedian1DLossy(double[] d, int window_size) {
        int n = d.length;
        double[] ret = clone(d);
        int center = window_size / 2;
        double[] dm = new double[window_size];
        for (int i = center; i < n - center; i++) {
            dm = crop1D(d, i - center, window_size);
            Arrays.sort(dm);
            ret[i] = dm[center];
        }
        return ret;
    }

    public static double[] crop1D(double[] d, int from, int size) {
        double[] ret = new double[size];
        for (int i = from; i < from + size; i++) {
            ret[i - from] = d[i];
        }
        return ret;
    }

    public static double[] filterMean1D(double[] d, int window_size) {
        int n = d.length;
        double[] ret = clone(d);
        int center = window_size / 2;
        double[] dm = new double[window_size];
        double temp = 0;
        for (int i = 0; i < center; i++) {
            dm = crop1D(d, i, center);
            temp = FactoryUtils.mean(dm);
            ret[i] = temp;
        }
        for (int i = center; i < n - center; i++) {
            dm = crop1D(d, i - center, window_size);
            temp = FactoryUtils.mean(dm);
            ret[i] = temp;
        }
        for (int i = n - center; i < n; i++) {
            dm = crop1D(d, i - center, center);
            temp = FactoryUtils.mean(dm);
            ret[i] = temp;
        }
        return ret;
    }

    private static double[] filterMean1DLossy(double[] d, int window_size) {
        int n = d.length;
        double[] ret = clone(d);
        int center = window_size / 2;
        double[] dm = new double[window_size];
        double temp = 0;
        for (int i = center; i < n - center; i++) {
            dm = crop1D(d, i - center, window_size);
            temp = FactoryUtils.mean(dm);
            ret[i] = temp;
        }
        return ret;
    }

    public static double[][] sort(double[][] array, String dim, String order) {
        double[][] ret = FactoryMatrix.clone(array);
        if (order.equals("ascend") && dim.equals("column")) {
            ret = FactoryMatrix.transpose(ret);
            int nr = ret.length;
            for (int i = 0; i < nr; i++) {
                ret[i] = FactoryUtils.sortArrayAscend(ret[i]);
            }
            ret = FactoryMatrix.transpose(ret);
        } else if (order.equals("ascend") && dim.equals("row")) {
            int nr = ret.length;
            for (int i = 0; i < nr; i++) {
                ret[i] = FactoryUtils.sortArrayAscend(ret[i]);
            }
        } else if (order.equals("descend") && dim.equals("column")) {
            ret = FactoryMatrix.transpose(ret);
            int nr = ret.length;
            for (int i = 0; i < nr; i++) {
                ret[i] = FactoryUtils.sortArrayDescend(ret[i]);
            }
            ret = FactoryMatrix.transpose(ret);
        } else if (order.equals("descend") && dim.equals("row")) {
            int nr = ret.length;
            for (int i = 0; i < nr; i++) {
                ret[i] = FactoryUtils.sortArrayDescend(ret[i]);
            }
        }
        return ret;
    }

    public static double[][] clone(double[][] d, int nr, int nc) {
        double[][] ret = new double[nr][nc];
        int delta_row = nr - d.length;
        int delta_col = nc - d[0].length;
        if (delta_row == 0 && delta_col == 0) {
            return clone(d);
        } else if (delta_row == 0 && delta_col > 0) {
            for (int i = 0; i < nr; i++) {
                System.arraycopy(d[i], 0, ret[i], 0, nc - delta_col);
            }
        } else if (delta_col == 0 && delta_row > 0) {
            for (int i = 0; i < nr - delta_row; i++) {
                System.arraycopy(d[i], 0, ret[i], 0, nc);
            }
        } else if (delta_row > 0 && delta_col > 0) {
            for (int i = 0; i < nr - delta_row; i++) {
                System.arraycopy(d[i], 0, ret[i], 0, nc - delta_col);
            }
        } else if ((delta_row == 0 && delta_col < 0) || (delta_col == 0 && delta_row < 0) || (delta_col < 0 && delta_row < 0)) {
            for (int i = 0; i < nr; i++) {
                System.arraycopy(d[i], 0, ret[i], 0, nc);
            }
        }
        return ret;
    }

    public static double[][] catHorizontal(double[][] d, int... val) {
        int ek = val.length;
        double[][] ret = clone(d, d.length, d[0].length + ek);
        int nr = ret.length;
        int nc = ret[0].length;
        int q = d[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < ek; j++) {
                ret[i][q + j] = val[j];
            }
        }
        return ret;
    }

    /**
     * Concatenates two double vectors horizontally [A,B]
     *
     * @param c1:i
     * @param c2:i
     * @return q
     */
    public static double[][] catHorizontalMatrix(double[] c1, double[] c2) {
        if (c1.length != c2.length) {
            System.err.println("array dimension mismatch by FactoryMatrix.catHorizontal(double[],double[])");
            return new double[1][1];
        }
        int n = c1.length;
        double[][] ret = new double[2][n];
        ret[0] = FactoryMatrix.clone(c1);
        ret[1] = FactoryMatrix.clone(c2);
        ret = FactoryMatrix.transpose(ret);
        return ret;
    }

    /**
     * Concatenates two double vectors vertically [A;B] which produce single
     * column matrix
     *
     * @param c1:i
     * @param c2:i
     * @return q
     */
    public static double[][] catVerticalMatrix(double[] c1, double[] c2) {
        int n = c1.length + c2.length;
        double[][] ret = new double[n][1];
        for (int i = 0; i < c1.length; i++) {
            ret[i][0] = c1[i];
        }
        for (int i = c1.length; i < n; i++) {
            ret[i][0] = c2[i - c1.length];
        }
        return ret;
    }

    public static double[] cat(double[] c1, double[] c2) {
        int n = c1.length + c2.length;
        double[] ret = new double[n];
        for (int i = 0; i < c1.length; i++) {
            ret[i] = c1[i];
        }
        for (int i = c1.length; i < n; i++) {
            ret[i] = c2[i - c1.length];
        }
        return ret;
    }

    public static double[][] catVertical(double[][] d, int... val) {
        int ek = val.length;
        double[][] ret = clone(d, d.length + ek, d[0].length);
        int nr = ret.length;
        int nc = ret[0].length;
        int q = d.length;
        for (int i = 0; i < ek; i++) {
            for (int j = 0; j < nc; j++) {
                ret[q + i][j] = val[i];
            }
        }
        return ret;
    }

    public static double[] generateRandomNormal_1D(int n, double mean, double var) {
        SecureRandom r = new SecureRandom();
        double[] d = new double[n];
        for (int i = 0; i < d.length; i++) {
            d[i] = mean + r.nextGaussian() * var;
        }
        return d;
    }

    public static double[][] generateRandomNormal_2D(int nr, int nc, double mean, double var) {
        SecureRandom r = new SecureRandom();
        double[][] d = new double[nc][nr];
        for (int i = 0; i < nc; i++) {
            for (int j = 0; j < nr; j++) {
                d[i][j] = mean + r.nextGaussian() * var;
            }
        }
        return transpose(d);
    }

    public static double[][] fillRandMatrix(double[][] d) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextDouble();
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, Random rnd) {
        int nr = d.length;
        int nc = d[0].length;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                d[i][j] = rnd.nextDouble();
//                d[i][j] = Math.random();
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, int max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextDouble() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, int max, Random rnd) {
//        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = rnd.nextDouble() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, double max, Random rnd) {
//        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = rnd.nextDouble() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, double max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextDouble() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, int min, int max, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + rnd.nextDouble() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, int min, int max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + r.nextDouble() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, double min, double max, Random rnd) {
//        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + rnd.nextDouble() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandMatrix(double[][] d, double min, double max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + r.nextDouble() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = rnd.nextGaussian();
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextGaussian();
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, int max, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = rnd.nextGaussian() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, int max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextGaussian() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, double max, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = rnd.nextGaussian() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, double max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = r.nextGaussian() * max;
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, int min, int max, Random rnd) {
        double q = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                q = rnd.nextGaussian();
                d[i][j] = min + q * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, int min, int max) {
        SecureRandom r = new SecureRandom();
        double q = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                q = r.nextGaussian();
                d[i][j] = min + q * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, double min, double max, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + rnd.nextGaussian() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrix(double[][] d, double min, double max) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = min + r.nextGaussian() * (max - min);
            }
        }
        return d;
    }

    public static double[][] fillRandNormalMatrixMeanVar(double[][] d, double mean, double var, Random rnd) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = mean + rnd.nextGaussian() * var;
            }
        }
        return d;
    }

    public static float[] fillRandNormal(float[] d, double mean, double var, Random rnd) {
        int nr = d.length;
        for (int i = 0; i < nr; i++) {
            d[i] = (float) (mean + rnd.nextGaussian() * var);
        }
        return d;
    }

    public static float[] fillRandNormal(int nr, double mean, double var, Random rnd) {
        float[] d = new float[nr];
        for (int i = 0; i < nr; i++) {
            d[i] = (float) (mean + rnd.nextGaussian() * var);
        }
        return d;
    }

    public static float[] zeros(int nr) {
        float[] ret = new float[nr];
        return ret;
    }

    public static float[] ones(int nr) {
        return values(nr, 1);
    }

    public static float[] values(int nr, float val) {
        float[] ret = new float[nr];
        for (int i = 0; i < nr; i++) {
            ret[i] = val;
        }
        return ret;
    }

    public static float[][] zeros(int nr, int nc) {
        float[][] ret = new float[nr][nc];
        return ret;
    }

    public static float[][] ones(int nr, int nc) {
        return values(nr, nc, 1);
    }

    public static float[][] values(int nr, int nc, float val) {
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = val;
            }
        }
        return ret;
    }

    public static double[][] fillRandNormalMatrixMeanVar(double[][] d, double mean, double var) {
        SecureRandom r = new SecureRandom();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d[i][j] = mean + r.nextGaussian() * var;
            }
        }
        return d;
    }

    public static int[] rand(int n, int scale, Random rnd) {
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = rnd.nextInt(scale);
        }
        return ret;
    }

    public static int[] rand(int n, int scale) {
        SecureRandom r = new SecureRandom();
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = r.nextInt(scale);
        }
        return ret;
    }

    public static double[][] meshGridX(double from, double to, int numberOf) {
        double[][] ret = new double[numberOf][numberOf];
//        double incr = (to - from + 1) / numberOf;
        double incr = (to - from) / (numberOf - 1);
        for (int i = 0; i < numberOf; i++) {
            for (int j = 0; j < numberOf; j++) {
                ret[i][j] = from + j * incr;
            }
        }
        return ret;
    }

    public static double[][] meshGridY(double from, double to, int numberOf) {
        return transpose(meshGridX(from, to, numberOf));
    }

    public static double[][] meshGridX(double[][] array, double from, double to) {
        int nr = array.length;
        int nc = array[0].length;
        double incr = (to - from) / (nc - 1);
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                array[i][j] = from + j * incr;
            }
        }
        return array;
    }

    public static double[][] meshGridY(double[][] array, double from, double to) {
        return transpose(meshGridX(array, from, to));
    }

    public static double[][] meshGridIterateForward(double[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        double[][] ret = clone(array);
        int mid = nc / 2;
        double[] left = new double[mid];
        double[] right = new double[mid];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < mid - 1; j++) {
                ret[i][j] = array[i][j + 1];
            }
            ret[i][mid - 1] = array[i][0];
            //ret[i][mid]=array[i][1];
            ret[i][mid] = 0;
            ret[i][mid + 1] = array[i][nc - 1];

            for (int j = mid + 2; j < nc; j++) {
                ret[i][j] = array[i][j - 1];
            }
        }
        return ret;
    }

    public static float[][] randMatrix(int dim) {
        float[][] ret = new float[dim][dim];
        SplittableRandom rnd = new SplittableRandom();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                ret[i][j] = (float) rnd.nextDouble();
            }
        }
        return ret;
    }

    public static float[][] randMatrix(int nr, int nc) {
        float[][] ret = new float[nr][nc];
        SplittableRandom rnd = new SplittableRandom();
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) rnd.nextDouble();
            }
        }
        return ret;
    }

    public static float[][] randMatrix(int nr, int nc, double bound) {
        float[][] ret = new float[nr][nc];
        SplittableRandom rnd = new SplittableRandom();
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) rnd.nextDouble(bound);
            }
        }
        return ret;
    }

    public static float[][] randMatrix(int nr, int nc, double from, double to) {
        float[][] ret = new float[nr][nc];
        SplittableRandom rnd = new SplittableRandom();
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) rnd.nextDouble(from, to);
            }
        }
        return ret;
    }

    public static float[][] make_blobs(int n_samples, int n_features, int n_groups, int mean_scale, int var_scale, Random random) {
        if (n_samples < n_groups) {
            FactoryUtils.showMessage("number of samples can't be less than number of groups");
            return null;
        }
        float[][] ret = new float[n_samples][n_features + 1];
        for (int i = 0; i < n_groups; i++) {
            float[][] cm = new float[n_samples][1];

            for (int j = 0; j < n_features; j++) {
                double mean = Math.random() * mean_scale - mean_scale / 2;
                double var = Math.sqrt(var_scale) + Math.random() * var_scale;
                float[] f = fillRandNormal(n_samples, mean, var, random);
                cm = cat("horizontal", cm, f);
            }

            cm = deleteColumn(cm, 0);
            cm = cat("horizontal", cm, values(n_samples, i));
            ret = cat("vertical", ret, cm);
        }
        ret = deleteRows(ret, 0, n_samples);
        ret = shuffleRows(ret);
        return ret;
    }

    public static float[] getLastColumn(float[][] f) {
        float[] ret = new float[f.length];
        int nr = f.length;
        int nc = f[0].length;
        for (int i = 0; i < nr; i++) {
            ret[i] = f[i][nc - 1];
        }
        return ret;
    }

    public static float[] getFirstColumn(float[][] f) {
        float[] ret = new float[f.length];
        int nr = f.length;
        int nc = f[0].length;
        for (int i = 0; i < nr; i++) {
            ret[i] = f[i][0];
        }
        return ret;
    }

    public static float[] getLastRow(float[][] f) {
        return f[f.length - 1];
    }

    public static float[] getFirstRow(float[][] f) {
        return f[0];
    }

    public static double[][] inverseLog(double[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Math.pow(10, Math.log(d[i][j] + 1));
            }
        }
        return ret;
    }

    public static double[][] inversePower(double x, double[][] d) {
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Math.pow(x, d[i][j]);
            }
        }
        return ret;
    }

    public static double[][] addClassLabel(double[][] cm, double cl) {
        int nr = cm.length;
        int nc = cm[0].length;
        double[][] ret = new double[nr][nc + 1];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = cm[i][j];
            }
            ret[i][nc] = cl;
        }
        return ret;
    }

    public static double[][] reshape(double[][] d, int r, int c) {
        if (d.length * d[0].length != r * c) {
            System.err.println("size mismatch");
            return d;
        }
        double[][] ret = new double[r][c];
        double[] a = toDoubleArray1D(d);
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = a[k++];
            }
        }
        return ret;
    }

    public static Object[][] reshape(Object[][] d, int r, int c) {
        if (d.length * d[0].length != r * c) {
            System.err.println("size mismatch");
            return d;
        }
        Object[][] ret = new Object[r][c];
        Object[] a = toDoubleArray1D(d);
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = a[k++];
            }
        }
        return ret;
    }

    public static double[][] reshape(double[] d, int r, int c) {
        double[][] ret = new double[r][c];
        if (d.length != r * c) {
            showMessage("size mismatch");
            return ret;
        }
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = d[k++];
            }
        }
        return ret;
    }

    public static Object[][] reshape(Object[] d, int r, int c) {
        Object[][] ret = new Object[r][c];
        if (d.length != r * c) {
            showMessage("size mismatch");
            return ret;
        }
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = d[k++];
            }
        }
        return ret;
    }

    public static double[][] perlinNoise(double[][] d) {
        int nr = d.length;
        int nc = d[0].length;

        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = PerlinNoise.noise(i * 0.1, j * 0.1, 0);
            }
        }
        return ret;
    }

    public static double[][] perlinNoise(double[][] d, double scale) {
        int nr = d.length;
        int nc = d[0].length;

        double[][] ret = new double[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = PerlinNoise.noise(i * scale, j * scale, 1.44);
            }
        }
        return ret;
    }

    public static double[][] convolve(double[][] d, double[][] kernel) {
        int mid = kernel.length / 2;
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nr - 2 * mid][nc - 2 * mid];
        for (int i = mid; i < nr - mid; i++) {
            for (int j = mid; j < nc - mid; j++) {
                double t = 0;
                for (int k = 0; k < kernel.length; k++) {
                    for (int l = 0; l < kernel[0].length; l++) {
                        t += kernel[k][l] * d[i - mid + k][j - mid + l];
                    }
                    ret[i - mid][j - mid] = t / (kernel.length * kernel[0].length);
                }
            }
        }
        return ret;
    }

    public static double[][] applyFunction(double[][] d, double[] f) {
        int nr = d.length;
        int nc = d[0].length;
        double[][] ret = new double[nr][nc];
        int val = 0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                val = (int) d[i][j];
                ret[i][j] = f[val];
            }
        }
        return ret;
    }

}

