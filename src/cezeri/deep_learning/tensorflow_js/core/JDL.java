/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.core;

import cezeri.deep_learning.tensorflow_js.enums.EnumBackEnd;
import cezeri.deep_learning.tensorflow_js.enums.EnumDataSource;
import cezeri.deep_learning.tensorflow_js.enums.EnumDevelopmentPlatform;
import cezeri.deep_learning.tensorflow_js.enums.EnumInferenceType;
import cezeri.deep_learning.tensorflow_js.enums.EnumLearningMode;
import cezeri.deep_learning.tensorflow_js.enums.EnumLearningType;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceCallBack;
import cezeri.deep_learning.tensorflow_js.utils.FactoryScripts;
import cezeri.factory.FactoryUtils;


/**
 *
 * @author BAP1
 */
public final class JDL {
    
    private static JDL jim;
    private static EnumBackEnd back_end;                                              
    private static EnumDevelopmentPlatform programming_language; 
    private static EnumDataSource data_source;
    private static String model_path;
    private static String scriptFile=null;
    private static String[] classLabels=null;
    private static boolean isConnectToPythonServer=false;
//    public static WebSocketClient client;
    

    private JDL(InterfaceCallBack cb) {
        FactoryUtils.startJavaServer();
        FactoryUtils.icbf=cb;
        FactoryUtils.delay(1000);
    }
    
    public static JDL getInstance(InterfaceCallBack cb) {
        if (jim == null) {
            jim = new JDL(cb);
        }
        return jim;
    }


    /**
     * 
     * @param be
     * @param modelPath
     * @param ds
     * @param class_labels
     * @param cb
     * @return 
     */
    public static JDL pythonTransferLearningWebCam(
            EnumBackEnd be,
            String modelPath,
            EnumDataSource ds,
            String[] class_labels,
            InterfaceCallBack cb
            
    ) {
        jim=getInstance(cb);
        isConnectToPythonServer=false;
        back_end=be;
        data_source=ds;
        programming_language=EnumDevelopmentPlatform.PYTHON;
        model_path=modelPath; 
        classLabels=FactoryUtils.clone(class_labels);
        scriptFile=FactoryScripts.generateScriptFilePythonWebCamTestTransferLearning(programming_language,back_end,data_source,model_path,classLabels);
        return jim;
    }
    
    public static JDL javaSciptTransferLearningWebCam(
            EnumBackEnd be,
            String modelPath,
            EnumDataSource ds,
            InterfaceCallBack cb
            
    ) {
        jim=getInstance(cb);
        isConnectToPythonServer=false;
        back_end=be;
        data_source=ds;
        programming_language=EnumDevelopmentPlatform.JAVA_SCRIPT;
        model_path=modelPath; 
        scriptFile=FactoryScripts.generateScriptFileJavaScriptWebCamTestTransferLearning(programming_language,back_end,data_source,model_path);
        return jim;
    }
    
    public static JDL transferLearningModelForOfflineTestImages(
            EnumDevelopmentPlatform pl,
            EnumBackEnd be,
            String modelPath,
            EnumDataSource ds,
            String test_path,
            InterfaceCallBack cb
            
    ) {
        jim=getInstance(cb);
        isConnectToPythonServer=false;
        back_end=be;
        data_source=ds;
        programming_language=pl;
        model_path=modelPath; 
        scriptFile=FactoryScripts.generateScriptFilePythonOfflineFileTestTransferLearning(programming_language,back_end,data_source,test_path,model_path);
        return jim;
    }
    
    
    public static JDL transferLearningModelForOnlineTestImage(
            EnumDevelopmentPlatform pl,
            EnumBackEnd be,
            String modelPath,
            EnumDataSource ds,
            InterfaceCallBack cb
            
    ) {
        jim=getInstance(cb);
        isConnectToPythonServer=true;
        back_end=be;
        data_source=ds;
        programming_language=pl;
        model_path=modelPath; 
        scriptFile=FactoryScripts.generateScriptFilePythonOnlineFileTestTransferLearning(programming_language,back_end,data_source,model_path);
        return jim;
    }
    
    /**
     * 
     * @param pl
     * @param be
     * @param modelPath
     * @param crossValidationRatio
     * @param datasetRootPath
     * @param learningRate
     * @param batchSize
     * @param epoch
     * @param cb
     * @return 
     */
    public static JDL CNN_TransferLearningModelForTraining(
            EnumDevelopmentPlatform pl,
            EnumBackEnd be,
            String modelPath,
            double crossValidationRatio,
            String datasetRootPath,
            double learningRate,
            double batchSize,
            double epoch,
            InterfaceCallBack cb
            
    ) {
        jim=getInstance(cb);
        return jim;
    }

    public JDL checkWebSocketStatus() {
        System.out.println("WebSocket Status is " + FactoryUtils.stopServer);
        return jim;
    }

    public JDL sendMessageToPythonServer(String msg) {
        if (FactoryUtils.client != null) {
            System.out.println("java sent msg = " + msg);
            FactoryUtils.client.send(msg);
        }
        return jim;
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
//     *
//     * @param str
//     * @return
//     */
    public JDL setBackEnd(EnumBackEnd backEnd) {
        if (backEnd.equals("python")) {
            if (true) {
                FactoryUtils.connectToPythonServer(new InterfaceCallBack() {
                    @Override
                    public void onMessageReceived(String str) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
                FactoryUtils.delay(1000);
            }

        } else if (backEnd.equals("js")) {

        } else if (backEnd.equals("dl4j")) {

        } else {
            System.out.println("back end only be python, js or dl4j");
        }
        return jim;
    }

    public JDL setInferenceModel(String path) {
        return jim;
    }

    public JDL setProgrammingLanguage(EnumDevelopmentPlatform p) {
        return jim;
    }

    public JDL setInferenceResponseTime(EnumInferenceType inferenceResponseTime) {
        return jim;
    }

    public JDL setLearningType(EnumLearningType learningType) {
        return jim;
    }

    public JDL setDataSource(EnumDataSource dataSource) {
        return jim;
    }

    public JDL setCallBackFunction(InterfaceCallBack cbi) {
        return jim;
    }

    public JDL setLearningMode(EnumLearningMode learningMode) {
        return jim;
    }

    public void execute() {
        if (scriptFile==null) {
            System.out.println("Severe Error has been occured script file could not generated");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FactoryUtils.executeCmdCommand("python "+scriptFile);               
                }
            }).start();
//            FactoryUtils.delay(5000);
//            if (isConnectToPythonServer) {
//                client=FactoryUtils.connectPythonServer();
//            }
        }
    }

    
}
