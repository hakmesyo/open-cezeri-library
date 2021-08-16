/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.call_back_interface.CallBackWebSocket;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceCallBack;
import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestWebSocket {

    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .startWebSocket(new InterfaceCallBack() {
                    @Override
                    public void onMessageReceived(String str) {
                        System.out.println("str = " + str);
                    }
                });

//        CDL_Utils.startJavaServer();
//        CDL_Utils.connectPythonServer();
//        CDL_Utils.delay(3000);
//        CDL_Utils.stopWebsocketServer();
//        System.exit(0);
//        CMatrix.getInstance()
//                .switchToDeepLearning()
//                .setBackEnd("python",false)
//                .setLearningType("online")
//                .setInferenceModel("path")
//                .setDataSource("sensorType")
//                .setCallBackFunction(Interface)
//                
//                //                .generateTrainSet("")
//                //                .generateValidationSet()
//                //                .generateTestSet()
//                //                .loadModel()
//                //                .fitModel()
//                //                .evaluateModel()
////                .sendMessageToPythonServer("merhaba")
//                
////                .executeCommand("python "+CML.currDir+"\\scripts\\python\\pistachio\\Open_Closed_Inference_WebCam.py")
////                .executeCommand("python "+CML.currDir+"\\scripts\\python\\deneme.py")
////                .switchToCMatrix()
//                ;
//        System.out.println("qw");
////        System.exit(0);
//
////        System.out.println("Working Directory = "+ System.getProperty("user.dir"));
    }

}
