/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_app_server;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestCezeriDeepLearning {

    public static void main(String[] args) {
        executePythonBackEndOnlineWebCamInferenceModel();
    }    
    
    private static void executePythonBackEndOnlineWebCamInferenceModel() {
        CDL.getInstance()
                .setProgrammingLanguage(ProgrammingLanguage.PYTHON)
                .setBackEnd(BackEnd.CPU)
                .setLearningMode(LearningMode.TEST)
                .setInferenceResponseTime(InferenceResponseTime.REAL_TIME)
                .setLearningType(LearningType.CLASSIFICATION)
                .setInferenceModel("path")
                .setDataSource(DataSource.CAMERA)
                .setCallBackFunction(new NewInterface() {})
                .execute();
    }
}
