package cezeri.machine_learning.classifiers;

import cezeri.factory.FactoryMatrix;
import cezeri.factory.FactoryNormalization;
import cezeri.factory.FactoryUtils;
import java.util.ArrayList;
import java.util.List;

public final class C_MLP {


    public class MLPNode {
        public String classLabel;
        public int pos;
        public float totalWeight;
    }

    private List<Float> currentOutput = new ArrayList<>();
    private List<Float> desiredOutput = new ArrayList<>();
    public List LAYER_OUTPUT;
    public List LAYER_DELTA;
    public List LAYER_BIAS;
    public List LAYER_DBIAS;
    public List LAYER_WEIGHTS;
    public List RANDOM_LAYER_WEIGHTS;
    public List LAYER_DWEIGHTS;
    private float[][] TRAINING_PATTERN;
    private float[][] VALIDATION_PATTERN;
    private float[][] TEST_PATTERN;
    private long seed;
//    private String[][] F_TEST;
//    private String[][] F_VALIDATION;
    private int inputLayerSize;
    private int outputLayerSize;
    private int numberOfHiddenLayers;
    private int iteration;
    private int nHidden1;
    private int nHidden2;
    private int nTotal;
    private int numberOfPatterns;
    private int numberOfTestPatterns;
    private int dimensionReductionSize;
    private int evaluationType;
    private int[] layerSizes;
    private float[] TRAINING_INPUT_ELEMENT;
    private float totalSquareError;
    private float maximumSquareError;
    public float averageSquareError; //in the mean time it means train error
    private float TARGET_OUTPUT[];
    private float learningRate;
    private float momentum;
    private float offset;
    private String fileName;
    private String driver;
    private int shuffleTrigger = 0;
    public float confidenceLevel = 0.0f;
    public float minErrorDeviationRatio = 0.0f;
    public double continousTreshold;
    public double meanSquareError = 0.0;
    public float toplam_err;
    public float accuracy;
    public int threadSleep = 10;
//    public int desiredValue = 0;
    public int rankingMethod = 0;
    public String[] pepperNames;
    public String currentTestSample;
    public String weightInitializationType;
//    public String classLabels = "";
//    public String testPepperList = "";
    public String desiredValueList = "";
    public boolean isStepWiseTraining = false;
    public boolean isNextClick = false;
    public boolean isRankingFeatures = false;
    public boolean isRankingMadeAbsolute = true;
    public boolean isShuffled = false;
    public boolean isPrediction = false;
    public boolean isAutoIterated = false;
    public boolean isAutoLearned = false;
    public boolean isWeightInitHiddenBased = false;
    public boolean DEBUG = false;
    public String fileNameConnWeights = "";
    public boolean isCustomWeightUse = false;
    public boolean isInitialRandomWeightUse = false;
    public boolean isOnlyPepper = false;
    public boolean antiOverfitting = false;
    public boolean isWeightNormalized = false;
    public int rectangleWidth = 1;
    public float fixedWeightValue;
    public float weightFrom;
    public float weightTo;
    public boolean addJitterToWeight = false;
    public boolean showLargeResult;
    private float[] class_labels;

//	****************************************************************************
//	*** constructors  **********************************************************
//	****************************************************************************
    private C_MLP() {
        inputLayerSize = 100;
        outputLayerSize = 0;
        numberOfHiddenLayers = 0;
        iteration = 1000;
        learningRate = 0.5F;
        momentum = 0.0F;
        layerSizes = null;
        numberOfPatterns = 15;
        numberOfTestPatterns = 15;
        TRAINING_INPUT_ELEMENT = new float[inputLayerSize];
        LAYER_OUTPUT = new ArrayList();
        LAYER_DELTA = new ArrayList();
        LAYER_BIAS = new ArrayList();
        LAYER_DBIAS = new ArrayList();
        LAYER_WEIGHTS = new ArrayList();
        RANDOM_LAYER_WEIGHTS = new ArrayList();
        LAYER_DWEIGHTS = new ArrayList();
        totalSquareError = 0.0F;
        maximumSquareError = 0.0F;
        averageSquareError = 0.0F;
        offset = 0.0F;
        seed = 0L;
    }

    public C_MLP(float[][] trainDS, float[][] testDS, float[] class_labels,
            int nOutput, int nHidden1, int nHidden2, int iteration,
            float learningRate, float momentum) {

        this.inputLayerSize = trainDS[0].length-1;
        this.outputLayerSize = nOutput;
        this.nTotal = inputLayerSize + nOutput;
        this.nHidden1 = nHidden1;
        this.nHidden2 = nHidden2;
        this.iteration = iteration;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.class_labels=class_labels;
        layerSizes = new int[3];
        layerSizes[0] = nHidden1;
        layerSizes[1] = nHidden2;
        layerSizes[2] = nOutput;

        this.numberOfPatterns = trainDS.length;
        this.numberOfTestPatterns = testDS.length;
        TRAINING_INPUT_ELEMENT = new float[inputLayerSize];
        LAYER_OUTPUT = new ArrayList();
        LAYER_DELTA = new ArrayList();
        LAYER_BIAS = new ArrayList();
        LAYER_DBIAS = new ArrayList();
        LAYER_WEIGHTS = new ArrayList();
        RANDOM_LAYER_WEIGHTS = new ArrayList();
        LAYER_DWEIGHTS = new ArrayList();
        totalSquareError = 0.0F;
        maximumSquareError = 0.0F;
        averageSquareError = 0.0F;
        offset = 0.0F;
        seed = 0L;

        TRAINING_PATTERN=FactoryMatrix.clone(trainDS);
        TEST_PATTERN=FactoryMatrix.clone(testDS);

        int layerSet[] = new int[4];
        layerSet[0] = inputLayerSize;
        layerSet[1] = nHidden1;
        layerSet[2] = nHidden2;
        layerSet[3] = nOutput;
        try {
            Initialize(layerSet);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    @Override
    public String toString(){
        String ret="Average Accuracy:"+accuracy;
        return ret;
    }
//****************************************************************************
//*** core methods  **********************************************************
//****************************************************************************	
//****************************************************************************
//*** public methods *********************************************************
//****************************************************************************	

    @SuppressWarnings("unchecked")
    private boolean Initialize(int layers[]) {
        int inputSize = layers[0];
        int[] ai = new int[layers.length - 1];
        for (int j = 1; j < layers.length; j++) {
            ai[j - 1] = layers[j];
        }
        inputLayerSize = inputSize;
        numberOfHiddenLayers = ai.length - 1;
        layerSizes = new int[ai.length];
        for (int k = 0; k < ai.length; k++) {
            layerSizes[k] = ai[k];
        }

        outputLayerSize = layerSizes[layerSizes.length - 1];
        TRAINING_INPUT_ELEMENT = new float[inputLayerSize];
        LAYER_OUTPUT.clear();
        LAYER_DELTA.clear();
        LAYER_BIAS.clear();
        LAYER_DBIAS.clear();
        LAYER_WEIGHTS.clear();
        RANDOM_LAYER_WEIGHTS.clear();
        LAYER_DWEIGHTS.clear();
        for (int l = 0; l < ai.length; l++) {
            LAYER_OUTPUT.add(new float[layerSizes[l]]);
            LAYER_DELTA.add(new float[layerSizes[l]]);
            LAYER_BIAS.add(new float[layerSizes[l]]);
            LAYER_DBIAS.add(new float[layerSizes[l]]);
            if (l == 0) {
                LAYER_WEIGHTS.add(new float[layerSizes[l]][inputLayerSize]);
                RANDOM_LAYER_WEIGHTS.add(new float[layerSizes[l]][inputLayerSize]);
                LAYER_DWEIGHTS.add(new float[layerSizes[l]][inputLayerSize]);
            } else {
                LAYER_WEIGHTS.add(new float[layerSizes[l]][layerSizes[l - 1]]);
                RANDOM_LAYER_WEIGHTS.add(new float[layerSizes[l]][layerSizes[l - 1]]);
                LAYER_DWEIGHTS.add(new float[layerSizes[l]][layerSizes[l - 1]]);
            }
        }
        TARGET_OUTPUT = new float[outputLayerSize];
        if (isCustomWeightUse) {
            println("custom weights are used...");
        } else {
//			Random random = new Random(my_Seed++);
            float from = weightFrom;
            float to = weightTo;
            if (weightInitializationType.equals("V && Alternate -0.5;+0.5;-0.5")) {
                initializeWeights(from, to, inputSize, "V && Alternate", addJitterToWeight);
            } else if (weightInitializationType.equals("V Fixed Fair -0.5;+0.5;-0.5")) {
                initializeWeights(from, to, inputSize, "V", addJitterToWeight);
            } else if (weightInitializationType.equals("^ Fixed Fair +0.5;-0.5;+0.5")) {
                initializeWeights(-from, -to, inputSize, "V", addJitterToWeight);
            } else if (weightInitializationType.equals("/ Fixed Fair -0.5;+0.5")) {
                initializeWeights(from, to, inputSize, "linear", addJitterToWeight);
            } else if (weightInitializationType.equals("\\ Fixed Fair +0.5;-0.5")) {
                initializeWeights(-from, -to, inputSize, "linear", addJitterToWeight);
            } else if (weightInitializationType.equals("Nguyen-Widrow")) {
                initializeWeights(-from, -to, inputSize, "nGuyenWidrow", addJitterToWeight);
            } else if (weightInitializationType.equals("Gaussian")) {
                initializeWeights(from, to, inputSize, "gaussian", addJitterToWeight);
            } else if (weightInitializationType.equals("Sigmoidal-logistic")) {
                initializeWeights(from, to, inputSize, "sigmoidal", addJitterToWeight);
            } else if (weightInitializationType.equals("Symmetric Random")) {
//                RANDOM_LAYER_WEIGHTS = frm.RANDOM_LAYER_WEIGHTS;
                initializeWeights(from, to, inputSize, "Symmetric Random", addJitterToWeight);
            } else if (weightInitializationType.equals("Sigmoidal-tanh")) {
                initializeWeights(from, to, inputSize, "tanh", addJitterToWeight);
            } else if (weightInitializationType.equals("Rectangular")) {
                initializeWeights(from, to, inputSize, "rectangular", addJitterToWeight);
            } else {
                for (int i1 = 0; i1 < layerSizes.length; i1++) {
                    float af[] = (float[]) LAYER_BIAS.get(i1);
                    float af1[] = (float[]) LAYER_DBIAS.get(i1);
                    for (int k1 = 0; k1 < layerSizes[i1]; k1++) {
                        af[k1] = genRandomNumberX();
                        af1[k1] = 0.0F;
                    }
                }
                for (int j1 = 0; j1 < layerSizes.length; j1++) {
                    int j2;
                    if (j1 == 0) {
                        j2 = inputSize;
                    } else {
                        j2 = layerSizes[j1 - 1];
                    }
                    float af2[][] = (float[][]) LAYER_WEIGHTS.get(j1);
                    float af3[][] = (float[][]) LAYER_DWEIGHTS.get(j1);
                    for (int l1 = 0; l1 < layerSizes[j1]; l1++) {
                        for (int i2 = 0; i2 < j2; i2++) {
                            af2[l1][i2] = genRandomNumberX();
                            af3[l1][i2] = 0.0F;
                        }
                    }
                    if (isInitialRandomWeightUse && j1 == 0) {
                        float[][] in_weights = FactoryMatrix.clone((float[][]) RANDOM_LAYER_WEIGHTS.get(j1));
                    }
                }
            }
        }

        System.gc();
        return true;
    }

    private float[] getNGuyenWidrowWeights(int hiddenSize, int inputSize) {
        float[] w = new float[hiddenSize];
        for (int i = 0; i < w.length; i++) {
            w[i] = (float) (Math.random() - 0.5);
        }
        float toplam = 0;
        for (int i = 0; i < w.length; i++) {
            toplam += w[i] * w[i];
        }
        float euclidianNorm = (float) Math.sqrt(toplam);
        float beta = 0.7F * (float) Math.pow(hiddenSize, 1 / inputSize);
        //rescale the weights using beta and norm
        for (int i = 0; i < w.length; i++) {
            w[i] = (beta * w[i] / euclidianNorm);
        }
        return w;
    }

    private float[] getGaussianWeights(float from, float to, int nodeSize) {
        nodeSize = (nodeSize == 1) ? ++nodeSize : nodeSize;
        float sigma = 0.3F;
        float[] ret = new float[nodeSize];
        float increment = (Math.abs(from - to) / (nodeSize - 1));
//		System.out.println("hidden nodes size:"+nodeSize);
        float x = from;
        for (int i = 0; i < ret.length; i++) {
            float y = (float) (1.0 / (Math.sqrt(2 * Math.PI) * sigma) * Math.exp(-(x * x) / (2 * sigma * sigma)) - 0.83);
//			System.out.println(i+".y="+y);
            ret[i] = y;
            x += increment;
        }
        return ret;
    }

    private float[] getSigmoidalWeights(float from, float to, int nodeSize) {
        nodeSize = (nodeSize == 1) ? ++nodeSize : nodeSize;
        float[] ret = new float[nodeSize];
        float increment = (Math.abs(from - to) / (nodeSize - 1));
//		System.out.println("hidden nodes size:"+nodeSize);
        float x = from;
        for (int i = 0; i < ret.length; i++) {
//			float y=(float)(1.0/(1+Math.exp(-x))-0.5);
            float y = 1.0f / (1.0f + (float) Math.exp(-x * ret.length));
//			System.out.println(i+".y="+y);
            ret[i] = y;
            x += increment;
        }
        return ret;
    }

    private float[] getHyperbolicTangentWeights(float from, float to, int nodeSize) {
        nodeSize = (nodeSize == 1) ? ++nodeSize : nodeSize;
        float[] ret = new float[nodeSize];
        float increment = (Math.abs(from - to) / (nodeSize - 1));
        float x = from;
        for (int i = 0; i < ret.length; i++) {
            float y = (float) 0.5 * (float) Math.tanh((float) (ret.length / 2 * 2.0 / 3.0) * (float) x);
            ret[i] = y;
            x += increment;
        }
        return ret;
    }

    private void initializeWeights(float from, float to, int inputSize, String type, boolean addJitter) {
        int n = 0;
        for (int i = 0; i < layerSizes.length; i++) {
            float bias[] = (float[]) LAYER_BIAS.get(i);
            float deltaBias[] = (float[]) LAYER_DBIAS.get(i);
            for (int j = 0; j < layerSizes[i]; j++) {
                bias[j] = 0.0F;
                deltaBias[j] = 0.0F;
            }
        }
        float increment = 0.1F;

        if (isWeightInitHiddenBased) {
            for (int i = 0; i < 1; i++) {
                float weights[][] = (float[][]) LAYER_WEIGHTS.get(i);
                float deltaWeights[][] = (float[][]) LAYER_DWEIGHTS.get(i);
                //j designates input nodes 
                for (int j = 0; j < weights[0].length; j++) {
                    float toplam = from;
                    if (type.equals("V") || type.equals("V && Alternate")) {
                        increment = 1.0F / ((float) (weights.length) / 2.0F);
                    } else {
                        increment = 1.0F / ((float) (weights.length - 1));
                    }
                    increment = (from < 0) ? increment : -increment;
//					n1=-n1;

                    //V shaped
                    if (type.equals("V")) {
                        for (int k = 0; k < weights.length / 2 + 1; k++) {
                            n++;
                            weights[k][j] = (addJitter) ? toplam + (float) (Math.random() * 0.01) : toplam;
                            deltaWeights[k][j] = 0.0F;
//							println(k+".hidden node:weight index:"+n+":"+weights[k][j]);
                            toplam += increment;
//							toplam+=increment+Math.random()*0.1;
                        }
                        toplam -= increment;
                        for (int k = weights.length / 2 + 1; k < weights.length; k++) {
                            n++;
                            toplam -= increment;
//							toplam-=increment+Math.random()*0.1;
                            weights[k][j] = (addJitter) ? toplam + (float) (Math.random() * 0.01) : toplam;
                            deltaWeights[k][j] = 0.0F;
//							println(k+".hidden node:weight index:"+n+":"+weights[k][j]);
                        }

                        if (isWeightNormalized) {
                        }

                    }
                    //V && Alternate
                    int r = 1;
                    if (type.equals("V && Alternate")) {
                        for (int k = 0; k < weights.length / 2 + 1; k++) {
                            n++;
                            weights[k][j] = toplam * (r *= -1);
                            deltaWeights[k][j] = 0.0F;
//							println(k+".hidden node:weight index:"+n+":"+weights[k][j]);
                            toplam += increment;
                        }
                        toplam -= increment;
                        for (int k = weights.length / 2 + 1; k < weights.length; k++) {
                            n++;
                            toplam -= increment;
                            weights[k][j] = toplam * (r *= -1);
                            deltaWeights[k][j] = 0.0F;
//							println(k+".hidden node:weight index:"+n+":"+weights[k][j]);
                        }

                    } //linear
                    else if (type.equals("linear")) {
                        for (int k = 0; k < weights.length; k++) {
                            n++;
                            weights[k][j] = toplam;
                            deltaWeights[k][j] = 0.0F;
//							println(k+".hidden node:weight index:"+n+":"+weights[k][j]);
                            toplam += increment;
                        }

                    } //nGuyenWidrow
                    else if (type.equals("nGuyenWidrow")) {
                        float[] w = getNGuyenWidrowWeights(weights.length, weights[0].length);
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
//							println(t+".input node:"+weights[j][t]+" j:"+j);
                            deltaWeights[k][j] = 0.0F;
                        }
                    } //gaussian
                    else if (type.equals("gaussian")) {
                        float[] w = getGaussianWeights(from, to, weights.length);
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
//							println(t+".input node:"+weights[j][t]+" j:"+j);
                            deltaWeights[k][j] = 0.0F;
                        }
                    } //sigmoidal
                    else if (type.equals("sigmoidal")) {
                        float[] w = getSigmoidalWeights(from, to, weights.length);
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
//							println(t+".input node:"+weights[j][t]+" j:"+j);
                            deltaWeights[k][j] = 0.0F;
                        }
                    } //hyperbolic tangent
                    else if (type.equals("tanh")) {
                        float[] w = getHyperbolicTangentWeights(from, to, weights.length);
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
//							println(j+".input node:"+weights[k][j]);
                            deltaWeights[k][j] = 0.0F;
                        }
                    } //rectangular
                    else if (type.equals("rectangular")) {
                        float[] w = new float[weights.length];
                        boolean flag = false;
                        for (int k = 0; k < weights.length; k++) {
                            if (k % rectangleWidth == 0) {
                                flag = !flag;
                            }
                            w[k] = (flag) ? from : to;
//							println(k+".rectangle weight:"+w[k]);

                        }
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
//							println(j+".input node:"+weights[k][j]);
                            deltaWeights[k][j] = 0.0F;
                        }
                    } //Symmetric Random
                    else if (type.equals("Symmetric Random")) {
                        float[] w = null;
                        if (isInitialRandomWeightUse) {
                            w = getSymmetricRandomWeightsFromPreviousRandomWeight(weights.length, j);
                        } else {
                            w = getSymmetricRandomWeights(from, to, weights.length);
                        }
                        for (int k = 0; k < weights.length; k++) {
                            weights[k][j] = w[k];
                            deltaWeights[k][j] = 0.0F;
                        }
                    }
                }
            }

        } else {
            //connection weights are initialized based on input nodes
            for (int i = 0; i < layerSizes.length; i++) {
                float weights[][] = (float[][]) LAYER_WEIGHTS.get(i);
                float deltaWeights[][] = (float[][]) LAYER_DWEIGHTS.get(i);
                for (int j = 0; j < weights.length; j++) {
                    float toplam = from;
                    if (type.equals("V")) {
                        increment = 1.0F / ((float) (weights[0].length) / 2.0F);
                    } else {
                        increment = 1.0F / ((float) (weights[0].length - 1));
                    }
                    increment = (from < 0) ? increment : -increment;
                    if (type.equals("V")) {
                        for (int k = 0; k < weights[0].length / 2 + 1; k++) {
                            n++;
                            weights[j][k] = toplam;
                            deltaWeights[j][k] = 0.0F;
//							println(k+".input node:weight index:"+n+":"+weights[j][k]);
                            toplam += increment;
                        }
                        toplam -= increment;
                        for (int k = weights[0].length / 2 + 1; k < weights[0].length; k++) {
                            n++;
                            toplam -= increment;
                            weights[j][k] = toplam;
                            deltaWeights[j][k] = 0.0F;
//							println(k+".input node:weight index:"+n+":"+weights[j][k]);
                        }

                    } //linear
                    else if (type.equals("linear")) {
                        for (int k = 0; k < weights[0].length; k++) {
                            n++;
                            weights[j][k] = toplam;
                            deltaWeights[j][k] = 0.0F;
//							println(k+".input node:weight index:"+n+":"+weights[j][k]);
                            toplam += increment;
                        }

                    } //nGuyenWidrow
                    else if (type.equals("nGuyenWidrow")) {
                        float[] w = getNGuyenWidrowWeights(weights.length, weights[0].length);
                        for (int k = 0; k < weights[0].length; k++) {
                            weights[j][k] = w[k];
//							println(t+".input node:"+weights[j][t]+" j:"+j);
                            deltaWeights[j][k] = 0.0F;
                        }
                    } //gaussian
                    else if (type.equals("gaussian")) {
                        float[] w = getGaussianWeights(from, to, weights.length);
                        for (int k = 0; k < weights[0].length; k++) {
                            weights[j][k] = w[k];
//							println(t+".input node:"+weights[j][t]+" j:"+j);
                            deltaWeights[j][k] = 0.0F;
                        }
                    } //sigmoidal
                    else if (type.equals("sigmoidal")) {
                        float[] w = getSigmoidalWeights(from, to, weights.length);
                        for (int k = 0; k < weights[0].length; k++) {
                            weights[j][k] = w[k];
                            deltaWeights[j][k] = 0.0F;
                        }
                    }

                }
            }
        }
    }

    private float[] getSymmetricRandomWeightsFromPreviousRandomWeight(int n, int j) {
        float[] ret = new float[n];
        float weights[][] = (float[][]) RANDOM_LAYER_WEIGHTS.get(0);
        if (n % 2 == 0) {
            for (int i = 0; i < ret.length / 2; i++) {
                float f = weights[i][j];
                ret[i] = f;
                ret[ret.length - 1 - i] = -f;
            }
        } else {
            for (int i = 0; i < ret.length / 2; i++) {
                float f = weights[i][j];
                ret[i] = f;
                ret[ret.length - 1 - i] = -f;
            }
            ret[ret.length / 2] = weights[ret.length / 2][j];
        }
        return ret;
    }

    private float[] getSymmetricRandomWeights(float from, float to, int n) {
        float[] ret = new float[n];
        if (n % 2 == 0) {
            for (int i = 0; i < ret.length / 2; i++) {
                float f = (float) (Math.random() - 0.5);
                ret[i] = f;
                ret[ret.length - 1 - i] = -f;
            }
        } else {
            for (int i = 0; i < ret.length / 2; i++) {
                float f = (float) (Math.random() - 0.5);
                ret[i] = f;
                ret[ret.length - 1 - i] = -f;
            }
            ret[ret.length / 2] = (float) (Math.random() - 0.5);
        }
        for (int i = 0; i < ret.length; i++) {
            println(i + ".weight:" + ret[i]);
        }
        return ret;
    }

    public float[] backPropRecognize(float af[]) {
        return doActivate(af);
    }

    public void doValidation() {
        int t_err = 0;
        double toplam_err = 0;
        int n = VALIDATION_PATTERN.length;
        //println("length:"+n);
        for (int i = 0; i < n; i++) {
            //1 fazlalik dosyanin ismini de testte gonderiyoruz.
//            String[] val_input = new String[inputLayerSize + 1];
            float[] val_input = VALIDATION_PATTERN[i];
            float[] temp_input = new float[inputLayerSize];
            temp_input=FactoryUtils.clone(val_input);
//            for (int j = 0; j < temp_input.length; j++) {
//                temp_input[j] = Float.parseFloat(val_input[j]);
//            }
            float[] af = backPropRecognize(temp_input);
            int er = Math.round(af[0]);
            int desired = Math.round(val_input[inputLayerSize]);
            if (er != desired) {
                t_err++;
            }
            //yaz("recognize edilen gercek output ; desired ouput:("+er+":"+test_input[10]+") hata sayisi:"+t_err);
        }
        toplam_err = (double) t_err / n;
    }

    public float doTest() {
        float t_err = 0;
        int n = TEST_PATTERN.length;
        int n0 = 0;//eger hepsi 0 olsa hata ne kadar olur
        int n1 = 0;//eger hepsi 1 olsa hata ne kadar olur
        float totalPositive = 0;//tum contamine olanlar
        float totalNegative = 0;//tum saglikli olanlar
        for (int i = 0; i < n; i++) {
            float[] test_input = TEST_PATTERN[i];
            float[] asking = new float[test_input.length-1];
            System.arraycopy(test_input, 0, asking, 0, asking.length);
            float[] af = backPropRecognize(asking);

            if (af.length == 1) {
                confidenceLevel += af[0];
                float predicted_value = af[0];
                float actual_output_value = test_input[inputLayerSize];
//                desiredValue = desired;

                if (isPrediction) {
                    double desiredV = test_input[inputLayerSize];
                    if (af[0] < continousTreshold) {
                        if (desiredV > continousTreshold) {
                            t_err++;
                        }
                    } else {
                        if (desiredV < continousTreshold) {
                            t_err++;
                        }
                    }
                    meanSquareError = (af[0] - desiredV) * (af[0] - desiredV);
                    if (i == 0) {
                       desiredValueList += desiredV + ";";
//                        testPepperList += test_input[inputLayerSize + 1] + ";";
                    } 
                } else {
                    float predicted_class=getPredictedClass(predicted_value,class_labels);
                    if (actual_output_value != predicted_class) {
                        n0++;
                        totalNegative++;
                        t_err++;
                    }
                    else {
                        n1++;
                        totalPositive++;
                    }
//                    if (predicted_value != actual_output_value) {
//                        if (actual_output_value == 0) {
//                        } else {
//                        }
//                        t_err++;
//
//                    }
                }
                if (isPrediction) {
                    int d = 0;
                    int q = 0;
                    double des = test_input[inputLayerSize];
                    if (des > continousTreshold) {
                        d = 1;
                    } else {
                        d = 0;
                    }
                    if (af[0] > continousTreshold) {
                        q = 1;
                    } else {
                        q = 0;
                    }
                    String sqw = test_input[inputLayerSize + 1] + "\t(" + d + " : " + q + ") \t output value:<" + FactoryUtils.formatFloat(af[0]) + ">\n";
                    if (showLargeResult) {
                    }
                } else {
                    String sqw = test_input[inputLayerSize] + "\t(" + Math.round(test_input[inputLayerSize]) + " : " + predicted_value + ") \t output value:<" + FactoryUtils.formatFloat(af[0]) + ">\n";
                    if (showLargeResult) {
                    }
                }
                currentTestSample = FactoryUtils.toString(test_input,",");

            }
            confidenceLevel /= n;
        }
        toplam_err = totalNegative / n*100;
        accuracy=totalPositive / n*100;
        return accuracy;
    }
    
    private float getPredictedClass(float predicted_value, float[] class_labels) {
        float[] normalized_class_labels=FactoryNormalization.normalizeMinMax(class_labels);
        float ret=class_labels[0];
        float delta=Math.abs(predicted_value-normalized_class_labels[0]);
        for (int i = 0; i < class_labels.length; i++) {
            if (Math.abs(predicted_value-normalized_class_labels[i])<delta) {
               delta=Math.abs(predicted_value-normalized_class_labels[i]); 
               ret=class_labels[i];
            }
        }
        return ret;
    }


    private void setTrainData(String trainds[][]) {
        numberOfPatterns = trainds.length;
        TRAINING_PATTERN = new float[numberOfPatterns][inputLayerSize + outputLayerSize];
        for (int i = 0; i < numberOfPatterns; i++) {
            for (int j = 0; j < inputLayerSize + outputLayerSize; j++) {
                TRAINING_PATTERN[i][j] = Float.parseFloat(trainds[i][j]);
            }
        }
    }

//    public void setValidationData(String[][] fValidation) {
//        this.F_VALIDATION = fValidation;
//        VALIDATION_PATTERN = new float[fValidation.length][fValidation[0].length - 1];
//        for (int i = 0; i < fValidation.length; i++) {
//            for (int j = 0; j < fValidation[0].length - 1; j++) {
//                VALIDATION_PATTERN[i][j] = Float.parseFloat(fValidation[i][j]);
//            }
//        }
//    }
//
//    public void setTestData(String[][] ftest) {
//        this.F_TEST = ftest;
//        TEST_PATTERN = new float[ftest.length][ftest[0].length - 1];
//        for (int i = 0; i < ftest.length; i++) {
//            for (int j = 0; j < ftest[0].length - 1; j++) {
//                TEST_PATTERN[i][j] = Float.parseFloat(ftest[i][j]);
//            }
//        }
//    }

//	****************************************************************************
//	*** private methods ********************************************************
//	****************************************************************************	
    private float[] doActivate(float trInput[]) {
        if (DEBUG) {
            println("\t\t" + "[...ACTIVATE...]");
        }
        float trInp[] = FactoryMatrix.clone(trInput);
        for (int k = 0; k < layerSizes.length; k++) {
            if (DEBUG) {
                println("\t\t" + k + ".layer");
            }
            float bias[] = (float[]) LAYER_BIAS.get(k);
            float outPut[] = (float[]) LAYER_OUTPUT.get(k);
            float weights[][] = (float[][]) LAYER_WEIGHTS.get(k);
            for (int i = 0; i < layerSizes[k]; i++) {
                float f = bias[i];
                for (int j = 0; j < trInp.length; j++) {
                    f += weights[i][j] * trInp[j];
                    if (DEBUG) {
                        println("\t\t\t" + j + ".inp node " + trInp[j] + " * " + weights[i][j] + " = " + weights[i][j] * trInp[j]);
                    }
                }
                if (DEBUG) {
                    println("\t\t\t" + i + ".node total sum:" + f);
                }
                outPut[i] = sigmoidFunction(f);
                if (DEBUG) {
                    println("\t\t\t" + i + ".node output (after sigmoid):" + outPut[i]);
                }
            }
            trInp = outPut;
        }
        return trInp;
    }

    private void computeDeltas() {
        if (DEBUG) {
            println("\t\t[..COMPUTING DELTA...]");
        }
        float outPut[] = (float[]) LAYER_OUTPUT.get(layerSizes.length - 1);
        //considering single output
        currentOutput.add(outPut[0]);
        float delta[] = (float[]) LAYER_DELTA.get(layerSizes.length - 1);
        for (int i = 0; i < outputLayerSize; i++) {
            float mse = 0.0f;
            if (antiOverfitting) {
                float targ = TARGET_OUTPUT[i];
                if (TARGET_OUTPUT[i] == 1) {
                    targ = 0.75f;
                    if (outPut[i] > 0.85f || outPut[i] < 0.65) {
                        delta[i] = (targ - outPut[i]) * outPut[i] * (1.0F - outPut[i]);
                    } else {
                        delta[i] = 0.0f;
                    }

                }
                if (TARGET_OUTPUT[i] == 0) {
                    targ = 0.25f;
                    if (outPut[i] > 0.35f || outPut[i] < 0.15) {
                        delta[i] = (targ - outPut[i]) * outPut[i] * (1.0F - outPut[i]);
                    } else {
                        delta[i] = 0.0f;
                    }
                }
                mse = (targ - outPut[i]) * (targ - outPut[i]);
            } else {
                delta[i] = (TARGET_OUTPUT[i] - outPut[i]) * outPut[i] * (1.0F - outPut[i]);
                mse = (TARGET_OUTPUT[i] - outPut[i]) * (TARGET_OUTPUT[i] - outPut[i]);
            }
            if (DEBUG) {
                println("\t\t\t" + i + ".output delta=( " + TARGET_OUTPUT[i] + " - " + outPut[i] + ") * " + outPut[i] + " * " + "(1.0 - " + outPut[i] + ")=" + delta[i]);
            }
//			println("Otput Delta:"+delta[i]);

//			float mse = (TARGET_OUTPUT[i] - outPut[i])* (TARGET_OUTPUT[i] - outPut[i]);
//			println("Otput mse:"+mse);
            totalSquareError += mse;
            if (mse > maximumSquareError) {
                maximumSquareError = mse;
            }
        }
        //println("totalSquareError:"+totalSquareError);

        int n = outputLayerSize;
        float w[][] = (float[][]) LAYER_WEIGHTS.get(layerSizes.length - 1);
        delta = (float[]) LAYER_DELTA.get(layerSizes.length - 1);
        for (int l = layerSizes.length - 2; l >= 0; l--) {
            float o[] = (float[]) LAYER_OUTPUT.get(l);
            float d[] = (float[]) LAYER_DELTA.get(l);
            for (int j = 0; j < layerSizes[l]; j++) {
                float f = 0.0F;
                for (int k = 0; k < n; k++) {
                    f += w[k][j] * delta[k];
                }
                d[j] = f * o[j] * (1.0F - o[j]);
                if (DEBUG) {
                    println("\t\t\t" + j + ".input delta=( " + f + ") * " + o[j] + " * " + "(1.0 - " + o[j] + ")=" + d[j]);
                }
            }
            n = layerSizes[l];
            w = (float[][]) LAYER_WEIGHTS.get(l);
            delta = d;
        }

    }

    private void computeWeights() {
        for (int k = layerSizes.length - 1; k >= 0; k--) {
            int l;
            float elements[];
            if (k == 0) {
                l = inputLayerSize;
                elements = TRAINING_INPUT_ELEMENT;
            } else {
                l = layerSizes[k - 1];
                elements = (float[]) LAYER_OUTPUT.get(k - 1);
            }
            float dWeights[][] = (float[][]) LAYER_DWEIGHTS.get(k);
            float weights[][] = (float[][]) LAYER_WEIGHTS.get(k);
            float delta[] = (float[]) LAYER_DELTA.get(k);
            float bias[] = (float[]) LAYER_BIAS.get(k);
            float dBias[] = (float[]) LAYER_DBIAS.get(k);
            if (DEBUG) {
                println("\t\t" + "[...WEIGHT UPDATE PROCEDURES...]");
                println("\t\tweight[i][j]+ = learningRate * delta[i] * elements[j] + momentum* weights[i][j]");
            }
            for (int i = 0; i < layerSizes[k]; i++) {
                if (DEBUG) {
                    println("\t\t\t" + i + ". LAYER");
                }
                for (int j = 0; j < l; j++) {
                    if (DEBUG) {
                        print("\t\t\t\t" + j + ".node updated weight[" + i + "][" + j + "]=" + learningRate
                                + " * " + FactoryUtils.formatFloat(delta[i]) + " * "
                                + elements[j] + " + "
                                + momentum + " * "
                                + dWeights[i][j] + "=");
                    }
                    dWeights[i][j] = learningRate * delta[i] * elements[j] + momentum * dWeights[i][j];
                    weights[i][j] += dWeights[i][j];
                    if (DEBUG) {
                        println("" + weights[i][j]);
                    }
                }
                dBias[i] = learningRate * delta[i] + momentum * dBias[i];
                bias[i] += dBias[i];
            }

        }

    }

    private void computeTotalWeights() {
        float total = 0;
        float absTotal = 0;
        for (int i = 0; i < LAYER_WEIGHTS.size(); i++) {
            float[][] w = (float[][]) LAYER_WEIGHTS.get(i);
            for (float[] w1 : w) {
                for (int k = 0; k < w[0].length; k++) {
                    total += w1[k];
                    absTotal += Math.abs(w1[k]);
                }
            }
        }
//        if (simClassification != null) {
//            simClassification.totalNetworkConnectionWeight = total;
//            simClassification.totalAbsoluteNetworkConnectionWeight = absTotal;
//        }
    }

    private void trainNN() {
        shuffleTrigger++;
        if (isShuffled && shuffleTrigger % 10 == 0) {
            TRAINING_PATTERN = shuffleTrainPattern(TRAINING_PATTERN);
            shuffleTrigger = 0;
        }
        totalSquareError = 0.0F;
        maximumSquareError = 0.0F;
        for (int i = 0; i < numberOfPatterns; i++) {
            if (curr_epoch > 8) {
                int q = 0;
            }
//            for (int j = 0; j < TRAINING_PATTERN[i].length; j++) {
//                TRAINING_INPUT_ELEMENT[j]=TRAINING_PATTERN[i][j];
//            }
            System.arraycopy(TRAINING_PATTERN[i], 0, TRAINING_INPUT_ELEMENT, 0, inputLayerSize);

            for (int k = 0; k < outputLayerSize; k++) {
                TARGET_OUTPUT[k] = TRAINING_PATTERN[i][inputLayerSize + k];
            }
            if (DEBUG) {
                println("\t" + i + ".pattern");
            }
            doActivate(TRAINING_INPUT_ELEMENT);     //feedforward phase call the activation function
            computeDeltas();                        //back-propagation phase
            computeWeights();                       //back-propagation phase
            computeTotalWeights();                  //add all connection weights for keeping neutrality in the network
        }
        averageSquareError = totalSquareError / (float) (numberOfPatterns * outputLayerSize);
    }

    private int curr_epoch = 0;

    public long doTrain() {
        long l = System.currentTimeMillis();
        float weightArrayHidden[][] = null;
        float weightArrayOut[][] = null;
        float savedLR = learningRate;
        desiredOutput.clear();
        float moment = momentum;
        for (int k = 0; k < numberOfPatterns; k++) {
            desiredOutput.add(TRAINING_PATTERN[k][inputLayerSize]);
        }
        ArrayList<MLPNode> inputWeightedNodes = null;
        if (isAutoIterated) {
            int i = 0;
            trainNN();
            float err_0 = averageSquareError;
            float epsilon = 0;
            float err_prev = err_0, err_curr;
            int count = 0;
            while (true) {
                i++;
                currentOutput.clear();
                curr_epoch++;
                trainNN();
                err_curr = averageSquareError;
                if (VALIDATION_PATTERN != null) {
                    doValidation();
                }
                weightArrayHidden = FactoryMatrix.clone((float[][]) LAYER_WEIGHTS.get(0));
                weightArrayOut = FactoryMatrix.clone((float[][]) LAYER_WEIGHTS.get(1));
                if (rankingMethod == 1) {
                    for (int j = 0; j < weightArrayHidden.length; j++) {
                        for (int k = 0; k < weightArrayHidden[0].length; k++) {
                            weightArrayHidden[j][k] = weightArrayHidden[j][k] * weightArrayOut[0][j];
                        }
                    }
                }
                epsilon = (err_prev - err_curr) / err_0;
                if (isAutoLearned) {
                    learningRate *= 0.999;
                    moment *= 0.999;
                }
                err_prev = err_curr;
                if (epsilon < 0) {
                    continue;
                }
                //eger 10 dan daha fazla belirlenen tresholddan daha kucuk hata farki orani varsa trainingi sonlandir 
                if (epsilon < minErrorDeviationRatio && count > 50) {
                    break;
                } else if (epsilon < minErrorDeviationRatio && count <= 50) {
                    count++;
                } else {
                    count = 0;
                }
            }
        } else {
            for (int i = 1; i <= iteration; i++) {
                currentOutput.clear();
                if (DEBUG) {
                    println(i + ".epoch");
                }
                trainNN();
                if (VALIDATION_PATTERN != null) {
                    doValidation();
                }
                weightArrayHidden = FactoryMatrix.clone((float[][]) LAYER_WEIGHTS.get(0));
                weightArrayOut = FactoryMatrix.clone((float[][]) LAYER_WEIGHTS.get(1));
                if (rankingMethod == 1) {
                    for (int j = 0; j < weightArrayHidden.length; j++) {
                        for (int k = 0; k < weightArrayHidden[0].length; k++) {
                            weightArrayHidden[j][k] = weightArrayHidden[j][k] * weightArrayOut[0][j];
                        }
                    }
                }
            }
        }
        return System.currentTimeMillis() - l;
    }

    private float[][] shuffleTrainPattern(float[][] p) {
        float[][] ptr = new float[p.length][p[0].length];
        List<float[]> v1 = new ArrayList();
        List<float[]> v2 = new ArrayList();
        for (int i = 0; i < p.length; i++) {
            v1.add(p[i]);
        }
        //println("v1 size:"+v1.size());
        while (!v1.isEmpty()) {
            int n = (int) (Math.random() * v1.size());
            //println("n:"+n);
            v2.add(v1.get(n));
            v1.remove(n);
        }
        //println("v2 size:"+v2.size());
        for (int i = 0; i < ptr.length; i++) {
            ptr[i] = v2.get(i);
        }
        return ptr;
    }

    private float genRandomNumberX() {
        float f = 0.0F;
        if (weightInitializationType.equals("Fixed UnFair -0.5;+0.5")) {
            //eksiden artiya artarsa
            f = -0.5F + offset;
            offset += 0.01F;
            if (offset > 1.0F) {
                offset = 0.0F;
            }
        }
        if (weightInitializationType.equals("Fixed UnFair +0.5;-0.5")) {
            //artidan eksiye dogru
            f = 0.5F - offset;
            offset += 0.01F;
            if (offset > 1.0F) {
                offset = 0.0F;
            }
        }
        if (weightInitializationType.equals("Random")) {
            f = (float) (Math.random() - 0.5);
        }
        if (weightInitializationType.equals("All High Value")) {
            f = fixedWeightValue;
        }
        if (weightInitializationType.equals("All zero")) {
            f = 0.0F;
        }
        if (weightInitializationType.equals("Alternate")) {
            f = lastF * -1.0F;
            lastF = f;
        }
        return f;
    }

    private float lastF = 0.5F;

    private float sigmoidFunction(float f) {
        return (float) 1 / ((float) 1 + (float) Math.exp(-f));
    }

    private float sigmoidFunctionTanH(float f) {
        float ret = (float) 1.7159 * (float) Math.tanh((float) (2.0 / 3.0) * (float) f);
        return ret;
    }

//*************************************************************************
//***** assist methods ****************************************************
//*************************************************************************
    public void setIterations(int i) {
        if (i < 1) {
            println("hata:too small iterations");
        } else {
            iteration = i;
        }
    }

    public void setLearningRate(float f) {
        if (f < 0.0F || f > 1.0F) {
            println("learning rate out of bound");
        } else {
            learningRate = f;
        }
    }

    public void setLearningMomentum(float f) {
        if (f < 0.0F || f > 1.0F) {
            println("learning momentum is out of allowed range");
        } else {
            momentum = f;
        }
    }

    public void setDimensionReductionSize(String str) {
        dimensionReductionSize = Integer.parseInt(str);
    }

    public void setEvaluationType(int selectedIndex) {
        this.evaluationType = selectedIndex;
    }

    private void println(float[] w) {
        for (int i = 0; i < w.length; i++) {
            System.out.println(i + ":" + w[i]);
        }
    }

    private void println(float[][] w) {
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[0].length; j++) {
                System.out.println(i + ":" + j + "=" + w[i][j]);
            }
        }
    }

    private void println(String str) {
        System.out.println(str);
    }

    private void print(String str) {
        System.out.print(str);
    }
}
