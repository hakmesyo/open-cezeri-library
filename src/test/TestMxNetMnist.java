/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.Mnist;
import ai.djl.basicmodelzoo.basic.Mlp;
import cezeri.deep_learning.ai.djl.examples.training.util.Arguments;
import ai.djl.inference.Predictor;
import ai.djl.metric.Metrics;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Activation;
import ai.djl.nn.Block;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.SaveModelTrainingListener;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import cezeri.factory.FactoryUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author cezerilab
 */
public class TestMxNetMnist {

    public static void main(String[] args) {
//        mnistAsANN();
//        mnistAsANN_Sequential();
        minstANNInference();
    }

    private static TrainingResult mnistAsANN() {
        int input = 28 * 28;
        int output = 10;
        int[] hidden = {128, 64};
        System.out.println("device count:" + Device.getDevices()[0]);
        Block block = new Mlp(input, output, hidden);
        Model model = Model.newInstance("mlp");
        model.setBlock(block);
        Arguments arguments = Arguments.parseArgs(new String[]{});
        try {
            // get training and validation dataset
            RandomAccessDataset trainingSet = getDataset(Dataset.Usage.TRAIN, arguments);
            RandomAccessDataset validateSet = getDataset(Dataset.Usage.TEST, arguments);

            // setup training configuration
            DefaultTrainingConfig config = setupTrainingConfig(arguments);
            try (Trainer trainer = model.newTrainer(config)) {
                trainer.setMetrics(new Metrics());

                /*
                 * MNIST is 28x28 grayscale image and pre processed into 28 * 28 NDArray.
                 * 1st axis is batch axis, we can use 1 for initialization.
                 */
                Shape inputShape = new Shape(1, Mnist.IMAGE_HEIGHT * Mnist.IMAGE_WIDTH);

                // initialize trainer with proper input shape
                trainer.initialize(inputShape);

                EasyTrain.fit(trainer, arguments.getEpoch(), trainingSet, validateSet);

                return trainer.getTrainingResult();

            } catch (IOException ex) {
                Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TranslateException ex) {
                Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static DefaultTrainingConfig setupTrainingConfig(Arguments arguments) {
        String outputDir = arguments.getOutputDir();
        SaveModelTrainingListener listener = new SaveModelTrainingListener(outputDir);
        listener.setSaveModelCallback(
                trainer -> {
                    TrainingResult result = trainer.getTrainingResult();
                    Model model = trainer.getModel();
                    float accuracy = result.getValidateEvaluation("Accuracy");
                    model.setProperty("Accuracy", String.format("%.5f", accuracy));
                    model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));
                });
        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .optDevices(Device.getDevices(arguments.getMaxGpus()))
                //.optDevices(Device.getDevices())                
                .addTrainingListeners(TrainingListener.Defaults.logging(outputDir))
                .addTrainingListeners(listener);
    }

    private static RandomAccessDataset getDataset(Dataset.Usage usage, Arguments arguments)
            throws IOException {
        Mnist mnist
                = Mnist.builder()
                        .optUsage(usage)
                        .setSampling(arguments.getBatchSize(), true)
                        .optLimit(arguments.getLimit())
                        .build();
        mnist.prepare(new ProgressBar());
        return mnist;
    }

    private static void mnistAsANN_Sequential() {
        int inputSize = 28 * 28;
        int outputSize = 10;

        SequentialBlock block = new SequentialBlock();
        block.add(Blocks.batchFlattenBlock(inputSize));
        block.add(Linear.builder().setUnits(128).build());
        block.add(Activation::relu);
        block.add(Linear.builder().setUnits(64).build());
        block.add(Activation::relu);
        block.add(Linear.builder().setUnits(outputSize).build());
        System.out.println(block);

        int batchSize = 32;
        Mnist mnist = Mnist.builder().setSampling(batchSize, true).build();
        try {
            mnist.prepare(new ProgressBar());
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }

        Model model = Model.newInstance("mlp");
        model.setBlock(block);
        DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                //softmaxCrossEntropyLoss is a standard loss for classification problems
                .addEvaluator(new Accuracy()) // Use accuracy so we humans can understand how accurate the model is
                .addTrainingListeners(TrainingListener.Defaults.logging());

        // Now that we have our training configuration, we should create a new trainer for our model
        Trainer trainer = model.newTrainer(config);
        trainer.initialize(new Shape(1, 28 * 28));

        int epoch = 2;

        try {
            EasyTrain.fit(trainer, epoch, mnist, null);
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TranslateException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }
        Path modelDir = Paths.get("build/mlp");
        try {
            Files.createDirectories(modelDir);
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }

        model.setProperty("Epoch", String.valueOf(epoch));

        try {
            model.save(modelDir, "mlp");
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void minstANNInference() {
        Image img = null;
        try {
            img = ImageFactory.getInstance().fromUrl("https://resources.djl.ai/images/0.png");
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }
        img.getWrappedImage();

        Path modelDir = Paths.get("build/mlp");
        Model model = Model.newInstance("mlp");
        model.setBlock(new Mlp(28 * 28, 10, new int[]{128, 64}));
        try {
            model.load(modelDir);
        } catch (IOException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedModelException ex) {
            Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
        }

        Translator<Image, Classifications> translator = new Translator<Image, Classifications>() {
            @Override
            public NDList processInput(TranslatorContext ctx, Image input) {
                // Convert Image to NDArray
                NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.GRAYSCALE);
                return new NDList(NDImageUtils.toTensor(array));
            }

            @Override
            public Classifications processOutput(TranslatorContext ctx, NDList list) {
                // Create a Classifications with the output probabilities
                NDArray probabilities = list.singletonOrThrow().softmax(0);
                List<String> classNames = IntStream.range(0, 10).mapToObj(String::valueOf).collect(Collectors.toList());
                return new Classifications(classNames, probabilities);
            }

            @Override
            public Batchifier getBatchifier() {
                // The Batchifier describes how to combine a batch together
                // Stacking, the most common batchifier, takes N [X1, X2, ...] arrays to a single [N, X1, X2, ...] array
                return Batchifier.STACK;
            }
        };
        Predictor predictor = model.newPredictor(translator);
        for (int i = 0; i < 10; i++) {
            long t=FactoryUtils.tic();
            try {
                Classifications predictions = (Classifications) predictor.predict(img);
                String s=predictions.toString();
                System.out.println(predictions.best());
            } catch (TranslateException ex) {
                Logger.getLogger(TestMxNetMnist.class.getName()).log(Level.SEVERE, null, ex);
            }
            t=FactoryUtils.toc(t);
        }
    }
}
