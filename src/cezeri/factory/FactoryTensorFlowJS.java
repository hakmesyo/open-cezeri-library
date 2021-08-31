/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import cezeri.deep_learning.tensorflow_js.test.traffic_sign.TestInference;
import cezeri.interfaces.InterfaceCallBack;
import cezeri.interfaces.InterfaceDeepLearning;
import cezeri.enums.EnumOperatingSystem;
import cezeri.enums.EnumRunTime;
import cezeri.websocket.SocketServer;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab
 */
public class FactoryTensorFlowJS implements InterfaceDeepLearning {

    private String strML5 = "\n"
            + " <div>Jazari Inference Machine based on Teachable Machine + p5.js + ml5.js</div>\n"
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
            + "	let isDebug = 0;\n"
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
            + "	  createDiv('OUTPUTS');\n"
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
            + "	  //console.log(imgName);\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1')) {\n"
            + "     connection.send(imgName);\n"
            + "	  }else if(imgName.includes('baslayabilirsin')){\n"
            + "	    console.log(imgName);\n"
            + "     connection.send(imgName);\n"
            + "	    str=imgName.split(':')[1];\n"
            + "	    console.log(str);\n"
            + "	    if(str==='DEBUG') isDebug=1;\n"
            + "	    if(str==='RUN') isDebug=0;\n"
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
            + "	  if(isDebug == 1){\n"
            + "	    createDiv('<img src='+images[currentIndex].replace(/ /g, '%20')+' alt='+images[currentIndex]+':'+label+' width=224 height=224></img>');\n"
            + "	    createDiv('image: ' +currentIndex+':'+ images[currentIndex]+':'+label);\n"
            + "	  }\n"
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
    private int currentIndex = 0;
    private int hit = 0;
    private int err = 0;
    private double acc = 0;
    private long t1 = FactoryUtils.tic();
    private long t2 = FactoryUtils.tic();
    private int HTTP_SERVER_PORT;
    private int WEB_SOCKET_PORT;
    private String MODEL_PATH;
    private EnumRunTime RUNTIME;
    private EnumOperatingSystem OS;
    private InterfaceCallBack icbf;

    private FactoryTensorFlowJS() {

    }

    public static FactoryTensorFlowJS getInstance() {
        return new FactoryTensorFlowJS();
    }

    public SocketServer startTensorFlowJS(EnumRunTime runTime, EnumOperatingSystem os, String modelPath, final int SERVER_PORT, final int SOCKET_PORT, InterfaceCallBack callBackFunction) {
        RUNTIME = runTime;
        MODEL_PATH = modelPath;
        HTTP_SERVER_PORT = SERVER_PORT;
        WEB_SOCKET_PORT = SOCKET_PORT;
        OS = os;
        icbf = callBackFunction;
        System.out.println("Application current path = " + FactoryUtils.getDefaultDirectory());
        SocketServer server = execute();
        return server;
    }

    @Override
    public SocketServer execute() {
        String str = "";
        strML5 = strML5.replace("8887", "" + WEB_SOCKET_PORT);
        str = strML5;
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
            server = new SocketServer(WEB_SOCKET_PORT, icbf);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FactoryTensorFlowJS.class.getName()).log(Level.SEVERE, null, ex);
        }
        FactoryUtils.startJavaServer(server);
        FactoryUtils.delay(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String folder = MODEL_PATH;
                folder = folder.substring(folder.lastIndexOf("/") + 1);
                try {
                    if (OS == EnumOperatingSystem.WINDOWS) {
                        Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome --new-window --disable-application-cache http://localhost:" + HTTP_SERVER_PORT + "/models/" + folder});
                    } else if (OS == EnumOperatingSystem.LINUX || OS == EnumOperatingSystem.JETSON || OS == EnumOperatingSystem.RASPI) {
                        Runtime.getRuntime().exec(new String[]{"bash", "-c", "chromium-browser --new-window http://localhost:" + HTTP_SERVER_PORT + "/models/" + folder});
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestInference.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ek=(RUNTIME==EnumRunTime.DEBUG)?"DEBUG":"RUN";
        server.broadcast("baslayabilirsin:"+ek);
        return server;
    }

}
