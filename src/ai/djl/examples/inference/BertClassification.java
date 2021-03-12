/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.examples.inference;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.nlp.SimpleVocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BertClassification {

    private static final Logger logger = LoggerFactory.getLogger(BertQaInference.class);

    private BertClassification() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        List<String> inputs = new ArrayList<>();
        inputs.add("this is\t sample input");
        inputs.add("hello, world!");
        inputs.add("DJL is awesome!!!");

        Classifications[] results = predict(inputs);
        if (results == null) {
            logger.info("This example only works for TensorFlow Engine");
        } else {
            for (int i = 0; i < inputs.size(); i++) {
                logger.info("Prediction for: " + inputs.get(i) + "\n" + results[i].toString());
            }
        }
    }

    public static Classifications[] predict(List<String> inputs)
            throws MalformedModelException, ModelNotFoundException, IOException,
                    TranslateException {
        if (!"TensorFlow".equals(Engine.getInstance().getEngineName())) {
            return null;
        }
        // refer to
        // https://medium.com/delvify/bert-rest-inference-from-the-fine-tuned-model-499997b32851
        // for converting public bert checkpoints to saved model format.
        String modelUrl = "file:///path/to/saved_model/";
        String vocabularyPath = "/path/to/vocab.txt";

        Criteria<String[], Classifications[]> criteria =
                Criteria.builder()
                        .setTypes(String[].class, Classifications[].class)
                        .optModelUrls(modelUrl)
                        .optTranslator(new MyTranslator(vocabularyPath, 128))
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<String[], Classifications[]> model = ModelZoo.loadModel(criteria);
                Predictor<String[], Classifications[]> predictor = model.newPredictor()) {
            return predictor.predict(inputs.toArray(new String[0]));
        }
    }

    private static final class MyTranslator implements Translator<String[], Classifications[]> {
        private final List<String> classes =
                Arrays.asList("class1", "class2", "class3", "class4", "class5");
        private BertFullTokenizer tokenizer;
        private final int maxSequenceLength;
        private final String vocabularyPath;

        MyTranslator(String vocabularyPath, int maxSequenceLength) {
            this.maxSequenceLength = maxSequenceLength;
            this.vocabularyPath = vocabularyPath;
        }

        /** {@inheritDoc} */
        @Override
        public Batchifier getBatchifier() {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public void prepare(NDManager manager, Model model) throws IOException {
            SimpleVocabulary vocabulary =
                    SimpleVocabulary.builder()
                            .optMinFrequency(1)
                            .addFromTextFile(Paths.get(vocabularyPath))
                            .optUnknownToken("[UNK]")
                            .build();
            tokenizer = new BertFullTokenizer(vocabulary, true);
        }

        /** {@inheritDoc} */
        @Override
        public NDList processInput(TranslatorContext ctx, String[] inputs) {
            NDManager inputManager = ctx.getNDManager();
            List<NDList> tokenizedInputs =
                    Arrays.stream(inputs)
                            .map(s -> tokenizeSingleString(inputManager, s))
                            .collect(Collectors.toList());
            NDList inputList = new NDList();
            inputList.add(stackInputs(tokenizedInputs, 0, "input_ids"));
            inputList.add(stackInputs(tokenizedInputs, 1, "input_mask"));
            inputList.add(stackInputs(tokenizedInputs, 2, "segment_ids"));
            inputList.add(stackInputs(tokenizedInputs, 3, "label_ids"));
            return inputList;
        }

        private NDArray stackInputs(List<NDList> tokenizedInputs, int index, String inputName) {
            NDArray stacked =
                    NDArrays.stack(
                            tokenizedInputs
                                    .stream()
                                    .map(list -> list.get(index).expandDims(0))
                                    .collect(Collectors.toCollection(NDList::new)));
            stacked.setName(inputName);
            return stacked;
        }

        private NDList tokenizeSingleString(NDManager manager, String input) {
            String[] inputs = input.split("\t");
            ConcurrentHashMap<String, Long> labelMap = new ConcurrentHashMap<>();
            for (int i = 0; i < classes.size(); i++) {
                labelMap.put(classes.get(i), (long) i);
            }
            List<String> tokensA = tokenizer.tokenize(inputs[2]);
            if (tokensA.size() > maxSequenceLength - 2) {
                tokensA = tokensA.subList(0, maxSequenceLength - 2);
            }

            List<String> tokens = new ArrayList<>();
            List<Long> segmentIds = new ArrayList<>();
            tokens.add("[CLS]");
            segmentIds.add(0L);
            for (String token : tokensA) {
                tokens.add(token);
                segmentIds.add(0L);
            }
            tokens.add("[SEP]");
            segmentIds.add(0L);
            List<Long> inputIds = new ArrayList<>();
            List<Long> inputMask = new ArrayList<>();

            for (String token : tokens) {
                inputIds.add(tokenizer.getVocabulary().getIndex(token));
                inputMask.add(1L);
            }
            while (inputIds.size() < maxSequenceLength) {
                inputIds.add(0L);
                inputMask.add(0L);
                segmentIds.add(0L);
            }
            Long labelId = labelMap.get(inputs[1]);
            NDList outputList = new NDList();
            outputList.add(manager.create(inputIds.stream().mapToLong(l -> l).toArray()));
            outputList.add(manager.create(inputMask.stream().mapToLong(l -> l).toArray()));
            outputList.add(manager.create(segmentIds.stream().mapToLong(l -> l).toArray()));
            outputList.add(manager.create(labelId));

            return outputList;
        }

        /** {@inheritDoc} */
        @Override
        public Classifications[] processOutput(TranslatorContext ctx, NDList list) {
            NDArray batchOutput = list.singletonOrThrow();
            int numOutputs = (int) batchOutput.getShape().get(0);
            Classifications[] output = new Classifications[numOutputs];

            for (int i = 0; i < numOutputs; i++) {
                output[i] = new Classifications(classes, batchOutput.get(i));
            }
            return output;
        }
    }
}
