package cezeri.factory;

public class FactoryNormalization {

    /**
     *
     * @return It's a pretty simple concept. Suppose that a teacher wants to
     * rank her students on a scale of 1-10. She uses their average test score
     * percentages to rank them.
     *
     * If the best average score is 95, and the worst is 23, then she wants a
     * normalization function f(x) such that
     *
     * f(minimum average test score) = minimum rank f(maximum average test
     * score) = maximum rank
     *
     * So if her normalization function is linear, then she can write it as
     *
     * f(x) = Ax + B
     *
     * We can solve for A and B by using the two known values:
     *
     * A * (minimum score) + B = minimum rank A * (maximum score) + B = maximum
     * rank
     *
     * So A = (maximum rank - minimum rank)/(maximum score - minimum score) and
     * B = minimum rank - A * (minimum score)
     *
     * For the example above, A = (10-1)/(95-23) = 0.125. B = 1 - 0.125 * 23 =
     * -1.875
     *
     * So the students who score 95 will have rank 10, and those who score 23
     * will have rank 1, and those who score in the middle, say 50, will have
     * rank:
     *
     * 0.125*50 - 1.875 = 4.375
     *
     * Min-max normalisation just means linearly mapping your data so that the
     * max and min of your input data are mapped to the predefined min and max
     * of your desired scale.
     *
     * Other kinds of normalization map the input data to achieve a predefined
     * mean and standard deviation, instead of a predefined minimum and maximum.
     * Normalization in general just means mapping your data into some more
     * standard form.
     *
     * Xnew=(X-Xmin)/(Xmax-Xmin)
     */
    public static double[][] normalizeZScore(double[][] p) {
        double[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
//            ret[i] = normalizeUnitVector(ret[i]);
            ret[i] = normalizeZScore(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }

    public static double[] normalizeZScore(double[] v) {
        double[] ret = FactoryMatrix.clone(v);
        double mean = FactoryUtils.getMean(v);
        double std = FactoryStatistic.getStandardDeviation(v);
        for (int i = 0; i < v.length; i++) {
            if (std == 0.0) {
                ret[i] = 0;
            } else {
                ret[i] = FactoryUtils.formatDouble((v[i] - mean) / std);
            }
        }
        return ret;
    }

    public static double[][] normalizeUnitVector(double[][] p) {
        double[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = normalizeUnitVector(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }

    public static double[] normalizeUnitVector(double[] v) {
        double[] ret = FactoryMatrix.clone(v);
        double magnitude = FactoryUtils.getMagnitude(ret);
        for (int i = 0; i < v.length; i++) {
            ret[i] = FactoryUtils.formatDouble(v[i] / magnitude);
        }
        return ret;
    }

    public static double[][] normalizeTanH(double[][] p) {
        double[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = normalizeTanH(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }

    public static double[] normalizeTanH(double[] v) {
        double[] ret = FactoryMatrix.clone(v);
        double mean = FactoryUtils.getMean(v);
        double std = FactoryStatistic.getStandardDeviation(v);
        for (int i = 0; i < v.length; i++) {
            ret[i] = FactoryUtils.formatDouble(0.5 * (Math.tanh(0.01 * ((v[i] - mean) / std)) + 1));
        }
        return ret;
    }

    public static double[][] normalizeSigmoidal(double[][] p) {
        double[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = normalizeSigmoidal(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }

    public static double[] normalizeSigmoidal(double[] v) {
        double[] ret = FactoryMatrix.clone(v);
        for (int i = 0; i < v.length; i++) {
            ret[i] = FactoryUtils.formatDouble(1.0 / (1 + Math.exp(-v[i])));
        }
        return ret;
    }

    public static double[][] normalizeMinMax(double[][] p) {
        double[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = normalizeMinMax(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }
    
    public static float[][] normalizeMinMax(float[][] p) {
        float[][] ret = FactoryUtils.transpose(FactoryMatrix.clone(p));
        for (int i = 0; i < ret.length; i++) {
            ret[i] = normalizeMinMax(ret[i]);
        }
        ret = FactoryUtils.transpose(ret);
        return ret;
    }

    public static int[] normalizeMinMax(int[] p) {
        int min = FactoryUtils.getMinimum(p);
        int max = FactoryUtils.getMaximum(p);
        int[] r = new int[p.length];
        int delta = (max - min);
        for (int i = 0; i < p.length; i++) {
            r[i] = (p[i] - min) / delta;
        }
        return r;
    }

    public static double[] normalizeMinMax(double[] v) {
        double[] ret = FactoryMatrix.clone(v);
        double min = FactoryUtils.getMinimum(v);
        double max = FactoryUtils.getMaximum(v);
        double delta = (max - min);
        for (int i = 0; i < v.length; i++) {
            ret[i] = FactoryUtils.formatDouble((v[i] - min) / delta);
        }
        return ret;
    }
    
    public static float[] normalizeMinMax(float[] v) {
        float[] ret = FactoryMatrix.clone(v);
        double min = FactoryUtils.getMinimum(v);
        double max = FactoryUtils.getMaximum(v);
        double delta = (max - min);
        for (int i = 0; i < v.length; i++) {
            ret[i] = (float)FactoryUtils.formatDouble((v[i] - min) / delta);
        }
        return ret;
    }

    public static double[] normalizeIntensity(double[] v, double dmin, double dmax) {
        double[] ret = FactoryMatrix.clone(v);
        double min = FactoryUtils.getMinimum(v);
        double max = FactoryUtils.getMaximum(v);
        double delta1 = (max - min);
        double delta2 = (dmax - dmin);
        double r = delta2 / delta1;
        for (int i = 0; i < v.length; i++) {
            ret[i] = FactoryUtils.formatDouble((v[i] - min) * r + dmin);
        }
        return ret;
    }

    public static double[][] normalizeWithRange(double[][] param, double min, double max) {        
        double[][] temp = param.clone();
        double[][] temp2=normalizeMinMax(param.clone());
        double range = max - min;
        for (int i = 0; i < param.length; i++) {
            for (int j = 0; j < param[0].length; j++) {
                double d=temp2[i][j];
                double tmp = d*range+min;
                temp[i][j] = tmp;
            }
        }

//        for (int i = 0; i < param.length; i++) {
//            for (int j = 0; j < param[0].length; j++) {
//                double tmp = ((param[i][j] * (double) max) / maximum);
//                if (tmp < min) {
//                    tmp = min;
//                }
//                temp[i][j] = tmp;
//            }
//        }
        return temp;
    }

    public static double[][] normalizeWithRange(double[][] param, int min, int max) {
        double maximum = FactoryUtils.getMaximum(param);
        double[][] temp = param.clone();
        for (int i = 0; i < param.length; i++) {
            for (int j = 0; j < param[0].length; j++) {
                int tmp = (int) ((param[i][j] * (double) max) / maximum);
                if (tmp < min) {
                    tmp = min;
                }
                temp[i][j] = tmp;
            }
        }
        return temp;
    }
}
