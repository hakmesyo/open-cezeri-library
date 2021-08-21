/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import cezeri.deep_learning.tensorflow_js.core.ConfigurationJavaScript;
import cezeri.interfaces.Configuration;
import cezeri.interfaces.InterfaceCallBack;
import cezeri.interfaces.InterfaceConfiguration;
import cezeri.interfaces.InterfaceDeepLearning;
import cezeri.deep_learning.tensorflow_js.test.pistachio.InferPistachio;
import cezeri.enums.EnumBackEnd;
import cezeri.enums.EnumDataSource;
import cezeri.enums.EnumLearningMode;
import cezeri.enums.EnumOperatingSystem;
import cezeri.websocket.SocketServer;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab
 */
public class FactoryTensorFlowJS implements InterfaceDeepLearning {

    private String strML5 = "\n"
            + " <div>Teachable Machine Image Model - p5.js and ml5.js</div>\n"
            + " <script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/p5.min.js\"></script>\n"
            + " <script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/addons/p5.dom.min.js\"></script>\n"
            + " <script src=\"https://unpkg.com/ml5@latest/dist/ml5.min.js\"></script>\n"
            //            + " <script src=\"../scripts/p5.min.js\"></script>\n"
            //            + " <script src=\"../scripts/p5.dom.min.js\"></script>\n"
            //            + " <script src=\"../scripts/ml5.min.js\"></script>\n"
            + " <script type=\"text/javascript\">\n"
            + "	let classifier;\n"
            + "	let imageModelURL = './model/';\n"
            + "	let imgName;\n"
            + "	let currentIndex = 0;\n"
            + "	let index = 0;\n"
            + "	let images = [];\n"
            + "	let label = '';\n"
            + " \n"
            + "	// Load the model first\n"
            + "	function preload() {\n"
            + "	  currentIndex=0;\n"
            + "	  console.log(\"merhaba\\n\");\n"
            + "	  classifier = ml5.imageClassifier(imageModelURL + 'model.json');\n"
            + "	}\n"
            + "	// The function below will determine and setup the image sizes, get the results as image.\n"
            + "	function setup() {\n"
            + "	  currentIndex=0;\n"
            + "	  createCanvas(224, 224);\n"
            + "	  background(0);\n"
            + "	}\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "   startTime=new Date();\n"
            + "	  images = [];\n"
            + "	  connection.send('restart');\n"
            + "	};\n"
            + " connection.onmessage = function (event) {\n"
            + "   imgName=event.data;\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1') || imgName.includes('baslayabilirsin' )) {\n"
            + "     connection.send(imgName);\n"
            + "	  }else{\n"
            + "	    images[index]=imgName;\n"
            + "	    if (index==0) loadImage(imgName, imageReady);\n"
            + "	    index++;\n"
            + "	   }\n"
            + " };\n"
            + "\n"
            + "	function imageReady(img) {\n"
            + "	  image(img, 0, 0);\n"
            + "	  classifier.classify(img, gotResult);\n"
            + "	}\n"
            + "\n"
            + "	// When we get the results\n"
            + "	function gotResult(err, results) {\n"
            + "	  if (err) {\n"
            + "		console.error(err);\n"
            + "	  }\n"
            + "	  label=results[0].label;\n"
            + "	  createDiv('image: ' +currentIndex+':'+ images[currentIndex]+':'+label);\n"
            + "	  connection.send(images[currentIndex]+':'+label);\n"
            + "   console.log(new Date().toLocaleTimeString());\n"
            + "	  currentIndex++;\n"
            + "   var checkExist = setInterval(function() {\n"
            + "       if (images.length>currentIndex) {\n"
            + "         clearInterval(checkExist);\n"
            + "         loadImage(images[currentIndex], imageReady);;\n"
            + "       }\n"
            + "     }, 1);\n"
            + "	}\n"
            + "</script>\n"
            + "";

    private String scriptFilePath = "";
    private ConfigurationJavaScript config = null;
    private File[] files;
    private int currentIndex = 0;
    private int hit = 0;
    private int err = 0;
    private double acc = 0;
    private long t1 = FactoryUtils.tic();
    private long t2 = FactoryUtils.tic();
    private int HTTP_SERVER_PORT;
    private int WEB_SOCKET_PORT;
    private String MODEL_PATH;
    private EnumOperatingSystem OS;
    private InterfaceCallBack icbf;

    private FactoryTensorFlowJS() {

    }

    public static FactoryTensorFlowJS getInstance() {
        return new FactoryTensorFlowJS();
    }

    public SocketServer startTensorFlowJS(EnumOperatingSystem os, String modelPath, final int SERVER_PORT, final int SOCKET_PORT, InterfaceCallBack callBackFunction) {
        MODEL_PATH = modelPath;
        HTTP_SERVER_PORT = SERVER_PORT;
        WEB_SOCKET_PORT = SOCKET_PORT;
        OS=os;
        icbf =callBackFunction;
//        FactoryUtils.icbf =callBackFunction;

        System.out.println("Application current path = " + FactoryUtils.getDefaultDirectory());

//        FactoryTensorFlowJS jdlp = new FactoryTensorFlowJS();
//        Configuration config = new ConfigurationJavaScript()
//                .setBackEnd(EnumBackEnd.WEBGL)
//                .setDataSource(EnumDataSource.IMAGE_FILE)
//                .setLearningMode(EnumLearningMode.TEST)
//                .setModelPath(FactoryUtils.getDefaultDirectory() + "/" + modelPath)
//                .setCallBackFunction(invokeMethod);
//
//        jdlp.setConfiguration(config);
        SocketServer server = execute();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return server;
    }

    @Override
    public void setConfiguration(InterfaceConfiguration cnf) {
        this.config = (ConfigurationJavaScript) cnf;
    }

//    @Override
//    public void build(int port) {
//        String str = "";
////        if (config.getLearningMode() == EnumLearningMode.TEST) {
////            if (config.getDataSource() == EnumDataSource.IMAGE_FILE) {
//        strML5 = strML5.replace("8887", "" + port);
//        str = strML5;
////            }
////        }
//        String folder = config.getModelPath();
//        scriptFilePath = folder + "/index.html";
//        FactoryUtils.writeToFile(scriptFilePath, str);
//    }
    @Override
    public SocketServer execute() {

        strML5 = strML5.replace("8887", "" + WEB_SOCKET_PORT);
        String str = strML5;
        FactoryUtils.writeToFile(MODEL_PATH + "/index.html", str);
        try {
            if (OS == EnumOperatingSystem.WINDOWS) {
                Runtime.getRuntime().exec("cmd /c http-server -p " + HTTP_SERVER_PORT);
            } else if (OS == EnumOperatingSystem.LINUX || OS == EnumOperatingSystem.JETSON || OS == EnumOperatingSystem.RASPI) {
                Runtime.getRuntime().exec("/bin/bash -c http-server -p " + HTTP_SERVER_PORT);
            } else if (OS == EnumOperatingSystem.MACOS) {
                throw new UnsupportedOperationException("Not supported yet.");
            } else if (OS == EnumOperatingSystem.ANDROID) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        } catch (IOException ex) {
            Logger.getLogger(FactoryTensorFlowJS.class.getName()).log(Level.SEVERE, null, ex);
        }

        SocketServer server = null;
        try {
            server = new SocketServer(WEB_SOCKET_PORT,icbf);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FactoryTensorFlowJS.class.getName()).log(Level.SEVERE, null, ex);
        }
        FactoryUtils.startJavaServer(server);
//        FactoryUtils.icbf = config.getCall_back();

        FactoryUtils.delay(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String folder = MODEL_PATH;
                folder = folder.substring(folder.lastIndexOf("/") + 1);
                try {
                    if (OS == EnumOperatingSystem.WINDOWS) {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome -new-window  http://localhost:" + HTTP_SERVER_PORT + "/models/" + folder});
                    } else if (OS == EnumOperatingSystem.LINUX || OS == EnumOperatingSystem.JETSON || OS == EnumOperatingSystem.RASPI) {
                        Runtime.getRuntime().exec(new String[]{"bash", "-c", "chromium-browser -new-window http://localhost:" + HTTP_SERVER_PORT + "/models/" + folder});
                    } else if (OS == EnumOperatingSystem.MACOS) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    } else if (OS == EnumOperatingSystem.ANDROID) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FactoryTensorFlowJS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        return server;
    }

//    private void invokeMethod(String str) {
//        System.out.println("str gelen= " + str);
//        t2=FactoryUtils.toc("invoke:", t2);
//        if (str.contains("Welcome to the server!") || str.contains("[new client connection]: 127.0.0.1")) {
//            //System.out.println("str gelen= " + str);
//        } else {
//            if (str.equals("restart")) {
//                //System.out.println("str gelen= " + str);
//                currentIndex = 0;
//                hit = err = 0;
//                FactoryUtils.server.broadcast("baslayabilirsin");
//                return;
//            } else {
//                if (str.split(":").length > 1) {
//                    String path = str.split(":")[0];
//                    String label = str.split(":")[1];
//                    if (path.contains(label)) {
//                        hit++;
//                    } else {
//                        err++;
//                    }
//                    acc = FactoryUtils.formatDouble(1.0 * hit / (hit + err) * 100);
//                    System.out.println("acc:" + acc + "% hit:" + hit + " err:" + err + " gelen mesaj:" + str);
//                }
//            }
//        }
//    }
}
