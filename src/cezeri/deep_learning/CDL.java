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
public final class CDL {

    public static String currDir = System.getProperty("user.dir");
    private static CMatrix cm;
    private static CDL cdl;
    private String backEnd;  // python, js, dl4j

    private CDL() {

    }

    public static CDL getInstance(CMatrix cmx) {
        if (cdl == null) {
            cm = cmx;
            cdl = new CDL();
            CDL_Utils.startJavaServer();
            CDL_Utils.delay(1000);
        }
        return cdl;
    }
    
    public static CDL getInstance() {
        if (cdl == null) {
            cdl = new CDL();
            CDL_Utils.startJavaServer();
            CDL_Utils.delay(1000);
        }
        return cdl;
    }

    public CMatrix switchToCMatrix() {
        CDL_Utils.stopWebsocketServer();
        return cm;
    }

    public CDL checkWebSocketStatus() {
        System.out.println("WebSocket Status is " + CDL_Utils.stopServer);
        return cdl;
    }

    public CDL sendMessageToPythonServer(String msg) {
        if (CDL_Utils.client != null) {
            System.out.println("java sent msg = " + msg);
            CDL_Utils.client.send(msg);
        }
        return cdl;
    }

//    /**
//     * for python backend please pip install those in cmd window. First of all download latest
//     * python 64 bit version (python 3.7.5 date:12.12.2019) and install properly
//     * and allow add path variable and restart the pc. Then install those libraries with pip 
//     * 1- cmd: python -m pip install –upgrade pip 
//     * 2- cmd: pip install tensorflow 
//     * 3- cmd: pip install tensorflow-gpu (optional) 
//     * 4- cmd: pip install opencv-python 
//     * 5- cmd: pip install websocket-client 
//     * 6- cmd: pip install websocket-server 
//     * 7- cmd: pip install pillow 
//     * 8- cmd: pip install matplotlib 
//     * 9- cmd: pip install keras
//     * 10- make directory python_data in C:\
//     * 11- make directory python_data\models 
//     * 12- make directory python_data\scripts 
//     *
//     * all keras models should be placed in C:\python_data\models\ (for example keras_model.h5)
//     *
//     * @param str
//     * @return
//     */
    public CDL setBackEnd(BackEnd backEnd) {
        if (backEnd.equals("python")) {
            if (true) {
                CDL_Utils.connectPythonServer();
                CDL_Utils.delay(1000);
            }

        } else if (backEnd.equals("js")) {

        } else if (backEnd.equals("dl4j")) {

        } else {
            System.out.println("back end only be python, js or dl4j");
        }
        return cdl;
    }

    public CDL setInferenceModel(String path) {
        return cdl;
    }

    public CDL setProgrammingLanguage(ProgrammingLanguage p) {
        return cdl;
    }

    public CDL setInferenceResponseTime(InferenceResponseTime inferenceResponseTime) {
        return cdl;
    }

    public CDL setLearningType(LearningType learningType) {
        return cdl;
    }

    public CDL setDataSource(DataSource dataSource) {
        return cdl;
    }

    public CDL setCallBackFunction(NewInterface newInterface) {
        return cdl;
    }

    public CDL setLearningMode(LearningMode learningMode) {
        return cdl;
    }

    public void execute() {
    }

}
