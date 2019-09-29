package cezeri.machine_learning.classifiers.deeplearning_ocl;

import java.io.Reader;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * (Convolutional) Neural network implementation with dropout and rectified
 * linear units. Can perform regression or classification. Training is done by
 * multithreaded mini-batch gradient descent with native matrix lib.
 *
 * @author Johannes Amtén
 *
 */
public class DeepNeuralNetwork implements Serializable {

    private NNParams params = null;

    private Matrix[] thetas = null;
    private boolean softmax = false;
    private int inputHeight = 0;
    private int numOutputs = 0;
    private NNParams.NNLayerParams[] layerParams = null;

    private double[] averages = null;
    private double[] stdDevs = null;

    private transient final ReentrantReadWriteLock thetasLock = new ReentrantReadWriteLock();
    private transient ExecutorService executorService;

    /**
     * Create an empty Neural Network. Use train() to generate weights.
     *
     * @param p The parameters of the neural network.
     */
    public DeepNeuralNetwork(NNParams p) {
        params = p;
    }

//    public MyNeuralNetwork saveClassifier(){
//        XStream xs=new XStream(new StaxDriver());
//        String xml=xs.toXML(this);
//        MyNeuralNetwork obj=(MyNeuralNetwork)(xs.fromXML(xml));
//        return obj;
//    }
    /**
     * Train neural network
     *
     * @param x Training data, input. One row for each training example. One
     * column for each attribute.
     * @param y Training data, correct output.
     * @throws Exception
     */
    public void train(Matrix x, Matrix y) throws Exception {
        if (params.numCategories == null) {
            params.numCategories = new int[x.numColumns()];
            Arrays.fill(params.numCategories, 1);
        }
        softmax = params.numClasses > 1;
        numOutputs = params.numClasses > 1 ? params.numClasses : y.numColumns();

        // Add null params for layer 0, which is just the input and last layer, which is just output.
        layerParams = new NNParams.NNLayerParams[params.hiddenLayerParams.length + 2];
        System.arraycopy(params.hiddenLayerParams, 0, layerParams, 1, params.hiddenLayerParams.length);
        layerParams[layerParams.length - 1] = new NNParams.NNLayerParams(numOutputs);

        if (params.normalizeNumericData) {
            if (params.dataLoader != null) {
                throw new Exception("With load on demand, data must be normalized before being sent to NeuralNetwork.");
            }
            x = x.copy();
            averages = new double[x.numColumns()];
            stdDevs = new double[x.numColumns()];
            for (int col = 0; col < x.numColumns(); col++) {
                if (params.numCategories[col] <= 1) {
                    // Normalize numeric column.
                    averages[col] = MatrixUtils.getAverage(x, col);
                    stdDevs[col] = MatrixUtils.getStandardDeviation(x, col);
                    MatrixUtils.normalizeData(x, col, averages[col], stdDevs[col]);
                }
            }
        } else {
            averages = null;
            stdDevs = null;
        }

        // Expand nominal values to groups of booleans.
        x = MatrixUtils.expandNominalAttributes(x, params.numCategories);
        if (softmax) {
            y = MatrixUtils.expandNominalAttributes(y, new int[]{numOutputs});
        }

        int inputSize = params.dataLoader == null ? x.numColumns() : params.dataLoader.getDataSize();

        if (layerParams[1].isConvolutional()) {
            // Convolutional network. Save width/height of input images.
            params.numInputChannels = params.numInputChannels > 0 ? params.numInputChannels : 1;
            if (params.inputWidth == 0) {
                // Assume input image has equal width and height.
                params.inputWidth = (int) Math.sqrt(inputSize / params.numInputChannels);
                inputHeight = (int) Math.sqrt(inputSize / params.numInputChannels);
            } else {
                inputHeight = inputSize / (params.numInputChannels * params.inputWidth);
            }
        } else {
            // Non-convolutional network. Input only has one dimension (width).
            params.inputWidth = inputSize;
        }

        if (params.batchSize == 0) {
            // Auto-choose batch-size.
            // 100 for fully connected network and 1 for convolutional network.
            params.batchSize = layerParams[1].isConvolutional() ? 1 : 100;
        }

        initThetas();
        // If threads == 0, use the same number of threads as cores.
        params.numThreads = params.numThreads > 0 ? params.numThreads : Runtime.getRuntime().availableProcessors();
//       myParams.numThreads = myParams.numThreads > 0 ? myParams.numThreads : 20;
        executorService = Executors.newFixedThreadPool(params.numThreads);

        Reader keyboardReader = System.console() != null ? System.console().reader() : null;
        boolean halted = false;

        List<Matrix> batchesX = new ArrayList<>();
        List<Matrix> batchesY = new ArrayList<>();
        MatrixUtils.split(x, y, params.batchSize, batchesX, batchesY);
        if (params.learningRate == 0.0) {
            // Auto-find initial learningRate.
            params.learningRate = findInitialLearningRate(x, y, params.batchSize, params.weightPenalty, params.debug);
        }

        double cost = getCostThreaded(batchesX, batchesY, params.weightPenalty);
        LinkedList<Double> fiveLastCosts = new LinkedList<>();
        LinkedList<Double> tenLastCosts = new LinkedList<>();
        if (params.debug) {
            System.out.println("\n\n*** Training network. Press <enter> to halt. ***\n");
            if (softmax) {
                System.out.println("Iteration: 0" + ", Cost: " + String.format("%.3E", cost) + ", Learning rate: " + String.format("%.1E", params.learningRate));
            } else {
                System.out.println("Iteration: 0" + ", RMSE: " + String.format("%.3E", Math.sqrt(cost * 2.0 / numOutputs)) + ", Learning rate: " + String.format("%.1E", params.learningRate));
            }
        }
        for (int i = 0; i < params.maxIterations && !halted; i++) {
            // Regenerate the batches each iteration, to get random samples each time.
            MatrixUtils.split(x, y, params.batchSize, batchesX, batchesY);
            trainOneIterationThreaded(batchesX, batchesY, params.learningRate, params.weightPenalty);
            cost = getCostThreaded(batchesX, batchesY, params.weightPenalty);

            if (fiveLastCosts.size() == 5) {
                // Lower learning rate if cost haven't decreased for 5 iterations.
                double oldCost = fiveLastCosts.remove();
                double minCost = Math.min(cost, Collections.min(fiveLastCosts));
                if (minCost >= oldCost) {
                    params.learningRate = params.learningRate * 0.1;
                    fiveLastCosts.clear();
                }
            }
            if (tenLastCosts.size() == 10) {
                double minCost = Math.min(cost, Collections.min(tenLastCosts));
                double maxCost = Math.max(cost, Collections.max(tenLastCosts));
                tenLastCosts.remove();
                if ((maxCost - minCost) / minCost < params.convergenceThreshold) {
                    // If cost hasn't moved by more than the threshold fraction for the last 10 iterations,
                    // we declare convergence and stop training.
                    halted = true;
                }
            }

            if (params.debug) {
                if (softmax) {
                    System.out.println("Iteration: " + (i + 1) + ", Cost: " + String.format("%.3E", cost) + ", Learning rate: " + String.format("%.1E", params.learningRate));
                } else {
                    System.out.println("Iteration: " + (i + 1) + ", RMSE: " + String.format("%.3E", Math.sqrt(cost * 2.0 / numOutputs)) + ", Learning rate: " + String.format("%.1E", params.learningRate));
                }
            }
            fiveLastCosts.add(cost);
            tenLastCosts.add(cost);

            if (params.debug && System.in.available() > 0) {
                while (System.in.available() > 0) {
                    System.in.read();
                }
                System.out.println("Training halted by user.");
                halted = true;
            }
        }

        executorService.shutdown();
    }

    /**
     * Get predictions for a number of input examples.
     *
     * @param x Matrix with one row for each input example and one column for
     * each input attribute.
     * @return Matrix with one row for each example. If regression, only one
     * column containing the predicted value. If classification, one column for
     * each class, containing the predicted probability of that class.
     */
    public Matrix getPredictions(Matrix x) throws Exception {
        if (averages != null) {
            x = x.copy();
            for (int col = 0; col < x.numColumns(); col++) {
                if (params.numCategories[col] <= 1) {
                    // Normalize numeric column.
                    MatrixUtils.normalizeData(x, col, averages[col], stdDevs[col]);
                }
            }
        }
        // Expand nominal values to groups of booleans.
        x = MatrixUtils.expandNominalAttributes(x, params.numCategories);

        if (x.numRows() > params.batchSize) {
            // Batch and thread calculations.
            final Matrix predictions = new Matrix(x.numRows(), numOutputs);
            ExecutorService es = Executors.newFixedThreadPool(params.numThreads);

            List<Future> queuedJobs = new ArrayList<>();
            for (int row = 0; row < x.numRows(); row += params.batchSize) {
                final int startRow = row;
                final int endRow = Math.min(startRow + params.batchSize - 1, x.numRows() - 1);
                final Matrix batchX = x.getRows(startRow, endRow);

                Runnable predictionsCalculator = new Runnable() {
                    public void run() {
                        FeedForwardResult[] ffRes = feedForward(batchX, null);
                        Matrix batchPredictions = ffRes[ffRes.length - 1].output;
                        for (int batchRow = 0; batchRow < batchPredictions.numRows(); batchRow++) {
                            for (int batchCol = 0; batchCol < batchPredictions.numColumns(); batchCol++) {
                                predictions.set(startRow + batchRow, batchCol, batchPredictions.get(batchRow, batchCol));
                            }
                        }
                    }
                };
                queuedJobs.add(es.submit(predictionsCalculator));
            }

            // Wait for all gradient calcs to be done.
            for (Future job : queuedJobs) {
                job.get();
            }
            es.shutdown();

            return predictions;
        } else {
            FeedForwardResult[] res = feedForward(x, null);
            return res[res.length - 1].output;
        }
    }

    /**
     * Get classification predictions for a number of input examples.
     *
     * @param x Matrix with one row for each input example and one column for
     * each input attribute.
     * @return Matrix with one row for each example and one column containing
     * the predicted class.
     */
    public int[] getPredictedClasses(Matrix x) throws Exception {
        Matrix y = getPredictions(x);
        int[] predictedClasses = new int[x.numRows()];
        for (int row = 0; row < y.numRows(); row++) {
            int prediction = 0;
            double predMaxValue = -1.0;
            for (int col = 0; col < y.numColumns(); col++) {
                if (y.get(row, col) > predMaxValue) {
                    predMaxValue = y.get(row, col);
                    prediction = col;
                }
            }
            predictedClasses[row] = prediction;
        }
        return predictedClasses;
    }

//    public double[] getPredictions(double[] x) {
//        Matrix xMatrix = new Matrix(new double[][]{x});
//        Matrix[] a = feedForward(xMatrix, null);
//        return a[a.length-1].getData();
//    }
    private Matrix createTheta(int rows, int cols) {
        Matrix theta = MatrixUtils.random(rows, cols);
        double epsilon = Math.sqrt(6) / Math.sqrt(rows + cols);
        theta.scale(epsilon * 2);
        theta.add(-epsilon);

        // Set the weight of the biases to a small positive value.
        // This prevents rectified linear units to be stuck at a zero gradient from the beginning.
        for (int row = 0; row < theta.numRows(); row++) {
            theta.set(row, 0, epsilon);
        }
        return theta;
    }

    private void initThetas() {
        ArrayList<Matrix> thetas = new ArrayList<>();

        int numLayers = layerParams.length;

        // Input layer has no theta.
        thetas.add(null);

        // Hidden layers
        for (int layer = 1; layer < numLayers; layer++) {
            if (layerParams[layer].isConvolutional()) {
                int previousLayerNumFeatureMaps = layer > 1 ? layerParams[layer - 1].numFeatures : params.numInputChannels;
                int numFeatureMaps = layerParams[layer].numFeatures;
                int patchSize = layerParams[layer].patchWidth * layerParams[layer].patchHeight;
                Matrix temp=createTheta(numFeatureMaps, previousLayerNumFeatureMaps * patchSize + 1);
                thetas.add(temp);
            } else {
                int layerInputs;
                if (layer - 1 == 0) {
                    layerInputs = getWidth(0) + 1;
                } else if (layerParams[layer - 1].isConvolutional()) {
                    layerInputs = layerParams[layer - 1].numFeatures * getWidth(layer - 1) * getHeight(layer - 1) + 1;
                } else {
                    layerInputs = layerParams[layer - 1].numFeatures + 1;
                }
                int layerOutputs = layerParams[layer].numFeatures;
                Matrix temp=createTheta(layerOutputs, layerInputs);
                thetas.add(temp);
            }
        }

        this.thetas = thetas.toArray(new Matrix[thetas.size()]);
    }

    private FeedForwardResult[] feedForward(Matrix x, Matrix[] dropoutMasks) {
        if (params.dataLoader != null) {
            // Load data on demand. To large dataset to fit in memory.
            try {
                x = params.dataLoader.loadData(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int numExamples = x.numRows();
        int numLayers = layerParams.length;

        FeedForwardResult[] ffr = new FeedForwardResult[numLayers];

        ffr[0] = new FeedForwardResult();
        ffr[0].output = x.copy();
        for (int layer = 1; layer < numLayers; layer++) {
            ffr[layer] = new FeedForwardResult();

            if (layerParams[layer].isConvolutional()) {
                //Convolutional layer
                int patchWidth = layerParams[layer].patchWidth;
                int patchHeight = layerParams[layer].patchHeight;
                int poolWidth = layerParams[layer].poolWidth;
                int poolHeight = layerParams[layer].poolHeight;

                ffr[layer].input = layer == 1 ? Convolutions.generatePatchesFromInputLayer(ffr[layer - 1].output, getWidth(layer - 1), getHeight(layer - 1), patchWidth, patchHeight)
                        : Convolutions.generatePatchesFromHiddenLayer(ffr[layer - 1].output, getWidth(layer - 1), getHeight(layer - 1), patchWidth, patchHeight);
                ffr[layer].input = MatrixUtils.addBiasColumn(ffr[layer].input); // Move into generatePatches() ?
                
                Matrix mm1=ffr[layer].input.copy();
                ffr[layer].output = mm1.trans2mult(thetas[layer]);

                if (layerParams[layer].isPooled()) {
                    Convolutions.PoolingResult pr = Convolutions.maxPool(ffr[layer].output, (getWidth(layer - 1) - patchWidth + 1), (getHeight(layer - 1) - patchHeight + 1), poolWidth, poolHeight);
                    ffr[layer].numRowsPrePool = ffr[layer].output.numRows();
                    ffr[layer].output = pr.pooledActivations;
                    ffr[layer].prePoolRowIndexes = pr.prePoolRowIndexes;
                }
            } else {
                // Fully connected layer
                if (layer > 1 && layerParams[layer - 1].isConvolutional()) {
                    // Reorder output from previous convolutional layer, so that all patches are columns instead of rows.
                    int numPatches = getWidth(layer - 1) * getHeight(layer - 1);
                    int numFeatureMaps = layerParams[layer - 1].numFeatures;
                    ffr[layer - 1].output = Convolutions.movePatchesToColumns(ffr[layer - 1].output, numExamples, numFeatureMaps, numPatches);
                }
                if (dropoutMasks != null && dropoutMasks[layer - 1] != null) {
                    // Dropout hidden nodes, if performing training with dropout.
                    ffr[layer - 1].output.multElements(dropoutMasks[layer - 1], ffr[layer - 1].output);
                } else {
                    double dropoutRate = layer - 1 == 0 ? params.inputLayerDropoutRate : params.hiddenLayersDropoutRate;
                    if (dropoutRate > 0.0) {
                        // Adjust values if training was done with dropout.
                        ffr[layer - 1].output.scale(1.0 - dropoutRate);
                    }
                }
                ffr[layer].input = MatrixUtils.addBiasColumn(ffr[layer - 1].output);
                Matrix mm1=ffr[layer].input.copy();
                ffr[layer].output = mm1.trans2mult(thetas[layer]);
            }
            if (layer < numLayers - 1) {
                MatrixUtils.rectify(ffr[layer].output);
            } else if (softmax) {
                MatrixUtils.softmax(ffr[layer].output);
            }
        }
//        Matrix mm=ffr[2].input;
        return ffr;
    }

    private int numberOfNodes() {
        int nodes = 0;
        for (int t = 1; t < thetas.length; t++) {
            nodes += (thetas[t].numColumns() - 1) * thetas[t].numRows();
        }
        return nodes;
    }

    private double getCost(Matrix x, Matrix y, double weightPenalty, int batchSize) {
        double c = 0.0;

        FeedForwardResult[] ffr = feedForward(x, null); //Can't use getPredictions because of normalization.
        Matrix h = ffr[ffr.length - 1].output;

        if (softmax) {
            for (int row = 0; row < y.numRows(); row++) {
                for (int col = 0; col < y.numColumns(); col++) {
                    if (y.get(row, col) > 0.99) {
                        c -= Math.log(h.get(row, col));
                    }
                }
            }
            // Have to use batch size and not number of rows, in case that the last batch contains fewer examples.
            c = c / batchSize;
        } else {
            // t1=(h-y).*(h-y)
            Matrix t1 = h.copy().add(-1, y);
            t1.multElements(t1, t1);
            // sum = sum(sum(t1))
            double[][] d = t1.data.toDoubleArray2D();
            int r = d.length;
            int col = d[0].length;
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < col; j++) {
                    c += d[i][j];
                }
            }
//            for (MatrixElement me: t1) {
//                c += me.value();
//            }
            // Have to use batch size and not number of rows, in case that the last batch contains fewer examples.
            c = c / (2 * batchSize);
        }

        if (weightPenalty > 0) {
            // Regularization
            double regSum = 0.0;
            for (int t = 1; t < thetas.length; t++) {
                Matrix q = thetas[t].getColumns(1, -1);
                double[][] d = q.data.toDoubleArray2D();
                int r = d.length;
                int col = d[0].length;
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < col; j++) {
                        regSum += Math.abs(d[i][j]);
                    }
                }

//                for (MatrixElement me : myThetas[t].getColumns(1, -1)) {
//                    regSum += Math.abs(me.value());
//                }
            }
            c += regSum * weightPenalty / numberOfNodes();
        }

        return c;
    }

    private double getCostThreaded(List<Matrix> batchesX, List<Matrix> batchesY, final double weightPenalty) throws Exception {
        final int batchSize = batchesX.get(0).numRows();
        // Queue up cost calculation in thread pool
        List<Future<Double>> costJobs = new ArrayList<>();
        for (int batchNr = 0; batchNr < batchesX.size(); batchNr++) {
            final Matrix bx = batchesX.get(batchNr);
            final Matrix by = batchesY.get(batchNr);
            Callable<Double> costCalculator = new Callable<Double>() {
                public Double call() throws Exception {
                    thetasLock.readLock().lock();
                    double cost = getCost(bx, by, weightPenalty, batchSize);
                    thetasLock.readLock().unlock();
                    return cost;
                }
            };
            costJobs.add(executorService.submit(costCalculator));
        }

        // Get cost
        double cost = 0;
        for (Future<Double> job : costJobs) {
            cost += job.get();
        }
        cost = cost / batchesX.size();
        return cost;
    }

    private Matrix[] getGradients(Matrix x, Matrix y, double weightPenalty, Matrix[] dropoutMasks, int batchSize) {

        int numLayers = layerParams.length;
        int numExamples = x.numRows();

        // Feed forward and save activations for each layer
        FeedForwardResult[] ffr = feedForward(x, dropoutMasks);
//        for (FeedForwardResult ff : ffr) {
//            if (ff.input!=null) ff.input.println();
//            if (ff.output!=null) ff.output.println();
//        }

        Matrix[] delta = new Matrix[numLayers];
        // Start with output layer and then work backwards.
        // delta[numlayers-1] = a[numLayers-1]-y;
        // (delta[numLayers-1] dim is numExamples*output nodes)
        delta[numLayers - 1] = ffr[numLayers - 1].output.copy().add(-1, y);

        for (int layer = numLayers - 2; layer >= 1; layer--) {
            Matrix temp=delta[layer + 1].copy().multiply(thetas[layer + 1]);
            delta[layer] = temp.getColumns(1, -1);
            if (dropoutMasks != null && dropoutMasks[layer] != null) {
                delta[layer].multElements(dropoutMasks[layer], delta[layer]);
            }
            if (layerParams[layer].isConvolutional()) {
                // Convolutional layer.
                int numFeatureMaps = layerParams[layer].numFeatures;
                int patchWidth = layerParams[layer].patchWidth;
                int patchHeight = layerParams[layer].patchHeight;
                if (!layerParams[layer + 1].isConvolutional()) {
                    int numPatches = getWidth(layer) * getHeight(layer);
                    delta[layer] = Convolutions.movePatchesToRows(delta[layer], numExamples, numFeatureMaps, numPatches);
                } else {
                    delta[layer] = Convolutions.antiPatchDeltas(delta[layer], getWidth(layer), getHeight(layer), patchWidth, patchHeight);
                }
                if (layerParams[layer].isPooled()) {
                    delta[layer] = Convolutions.antiPoolDelta(delta[layer], ffr[layer].prePoolRowIndexes, ffr[layer].numRowsPrePool);
                }
            }
            Matrix temp2=ffr[layer].input.copy().multiply(thetas[layer].copy().transpose());
            delta[layer].copy().multElements(MatrixUtils.rectifyGradient(temp2), delta[layer]);
        }

        // Compute gradients for each theta
        Matrix[] thetaGrad = new Matrix[numLayers];
        for (int layer = 1; layer < numLayers; layer++) {
            // thetaGrad[layer] = delta[layer+1]'*a[layer];
            // (thetaGrad[layer] dim is nodes in a[layer+1]*nodes in a[layer])
            thetaGrad[layer] = delta[layer].trans1mult(ffr[layer].input);
            // Have to use batch size and not number of rows, in case that the last batch contains fewer examples.
            thetaGrad[layer].scale(1.0 / batchSize);
        }

        if (weightPenalty > 0) {
            // Add regularization terms
            int numNodes = numberOfNodes();
            for (int thetaNr = 1; thetaNr < numLayers; thetaNr++) {
                Matrix theta = thetas[thetaNr];
                Matrix grad = thetaGrad[thetaNr];
                for (int row = 0; row < grad.numRows(); row++) {
                    for (int col = 1; col < grad.numColumns(); col++) {
                        double regTerm = weightPenalty / numNodes * Math.signum(theta.get(row, col));
                        grad.add(row, col, regTerm);
                    }
                }
            }
        }

        return thetaGrad;
    }

    private Matrix[] generateDropoutMasks(int numExamples) {
        // Create own random generator instead of making calls to Math.random from each thread, which would block each other.
        Random rnd = new Random(11);

        int numLayers = layerParams.length;
        // Don't dropout output layer.
        Matrix[] masks = new Matrix[numLayers - 1];

        for (int l = 0; l < masks.length; l++) {
            // Don't dropout input to convolutional layers.
            if (!layerParams[l + 1].isConvolutional()) {
                if ((l == 0 && params.inputLayerDropoutRate > 0) || (l > 0 && params.hiddenLayersDropoutRate > 0)) {
                    int numColumns = l == 0 ? getWidth(0) : layerParams[l].numFeatures * getWidth(l) * getHeight(l);
                    Matrix mask = new Matrix(numExamples, numColumns);
                    double dropoutRate = l == 0 ? params.inputLayerDropoutRate : params.hiddenLayersDropoutRate;
                    double[][] d=mask.data.toDoubleArray2D();
                    int r=d.length;
                    int c=d[0].length;
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            d[i][j]=rnd.nextDouble() > dropoutRate ? 1.0 : 0.0;
                        }
                    }
                    mask.data.setArray(d);
//                    for (MatrixElement me : mask) {
//                        me.set(rnd.nextDouble() > dropoutRate ? 1.0 : 0.0);
//                    }
                    masks[l] = mask;
                }
            }
        }

        return masks;
    }

    private void trainOneIterationThreaded(List<Matrix> batchesX, List<Matrix> batchesY, final double learningRate, final double weightPenalty) throws Exception {
        final int batchSize = batchesX.get(0).numRows();

        // Queue up all batches for gradient computation in the thread pool.
        List<Future> queuedJobs = new ArrayList<>();
        for (int batchNr = 0; batchNr < batchesX.size(); batchNr++) {
            final Matrix bx = batchesX.get(batchNr);
            final Matrix by = batchesY.get(batchNr);

            Runnable batchGradientCalculator = new Runnable() {
                public void run() {
                    boolean useDropout = params.inputLayerDropoutRate > 0.0 || params.hiddenLayersDropoutRate > 0.0;
                    Matrix[] dropoutMasks = useDropout ? generateDropoutMasks(by.numRows()) : null;
                    thetasLock.readLock().lock();
                    Matrix[] gradients = getGradients(bx, by, weightPenalty, dropoutMasks, batchSize);
                    thetasLock.readLock().unlock();
                    thetasLock.writeLock().lock();
                    for (int theta = 1; theta < thetas.length; theta++) {
                        thetas[theta].add(-learningRate, gradients[theta]);
                    }
                    thetasLock.writeLock().unlock();
                }
            };
            queuedJobs.add(executorService.submit(batchGradientCalculator));
        }

        // Wait for all gradient calcs to be done.
        for (Future job : queuedJobs) {
            job.get();
        }
    }

    private Matrix[] deepCopy(Matrix[] ms) {
        Matrix[] res = new Matrix[ms.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = ms[i] != null ? ms[i].copy() : null;
        }
        return res;
    }

    private double findInitialLearningRate(Matrix x, Matrix y, int batchSize, double weightPenalty, boolean debug) throws Exception {
        int numUsedTrainingExamples = 5000;
        int numBatches = numUsedTrainingExamples / batchSize;

        List<Matrix> batchesX = new ArrayList<>();
        List<Matrix> batchesY = new ArrayList<>();
        MatrixUtils.split(x, y, batchSize, batchesX, batchesY);
        while (batchesX.size() < numBatches) {
            batchesX.addAll(batchesX);
            batchesY.addAll(batchesY);
        }
        if (batchesX.size() > numBatches) {
            batchesX = batchesX.subList(0, numBatches);
            batchesY = batchesY.subList(0, numBatches);
        }

        Matrix[] startThetas = deepCopy(thetas);
        double lr = 1.0E-10;
        trainOneIterationThreaded(batchesX, batchesY, lr, weightPenalty);
        double cost = getCostThreaded(batchesX, batchesY, weightPenalty);
        if (debug) {
            System.out.println("\n\nAuto-finding learning rate.");
            System.out.println("Learning rate: " + String.format("%.1E", lr) + " Cost: " + cost);
        }
        thetas = deepCopy(startThetas);
        double lastCost = Double.MAX_VALUE;
        double lastLR = lr;
        while (cost < lastCost) {
            lastCost = cost;
            lastLR = lr;
            lr = lr * 10.0;
            trainOneIterationThreaded(batchesX, batchesY, lr, weightPenalty);
            cost = getCostThreaded(batchesX, batchesY, weightPenalty);
            if (debug) {
                System.out.println("Learning rate: " + String.format("%.1E", lr) + " Cost: " + cost);
            }
            thetas = deepCopy(startThetas);
        }

        if (debug) {
            System.out.println("Using learning rate: " + String.format("%.1E", lastLR));
        }
        return lastLR;
    }

    private int getWidth(int layer) {
        if (layer > 0 && !layerParams[layer].isConvolutional()) {
            return 1;
        }

        int width = params.inputWidth;
        for (int l = 1; l <= layer; l++) {
            // Calc number of patch-rows next layer will have. Convolution and pooling.
            int patchWidth = layerParams[l].patchWidth;
            int poolWidth = layerParams[l].poolWidth;
            width = width - patchWidth + 1;
            width = width % poolWidth == 0 ? width / poolWidth
                    : width / poolWidth + 1;
        }
        return width;
    }

    private int getHeight(int layer) {
        if (layer > 0 && !layerParams[layer].isConvolutional()) {
            return 1;
        }

        int height = inputHeight;
        for (int l = 1; l <= layer; l++) {
            // Calc number of patch-rows next layer will have. Convolution and pooling.
            int patchHeight = layerParams[l].patchHeight;
            int poolHeight = layerParams[l].poolHeight;
            height = height - patchHeight + 1;
            height = height % poolHeight == 0 ? height / poolHeight
                    : height / poolHeight + 1;
        }
        return height;
    }

    private static class FeedForwardResult {

        public Matrix input = null;
        public Matrix output = null;
        public Matrix prePoolRowIndexes = null;
        public int numRowsPrePool = 0;
    }

}
