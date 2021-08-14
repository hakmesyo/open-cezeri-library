/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_rest.test.pistachio;

import cezeri.deep_learning_rest.core.ConfigurationJavaScript;
import cezeri.deep_learning_rest.enums.EnumBackEnd;
import cezeri.deep_learning_rest.enums.EnumDataSource;
import cezeri.deep_learning_rest.enums.EnumLearningMode;
import cezeri.deep_learning_rest.interfaces.Configuration;
import cezeri.deep_learning_rest.interfaces.InterfaceCallBack;
import cezeri.deep_learning_rest.interfaces.InterfaceConfiguration;
import cezeri.deep_learning_rest.interfaces.InterfaceDeepLearning;
import cezeri.factory.FactoryUtils;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab
 */
public class InferPistachio implements InterfaceDeepLearning {

    private final static String strML5 = ""
            + "<div>Teachable Machine Image Model - p5.js and ml5.js</div>\n"
//            + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/p5.min.js\"></script>\n"
//            + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/addons/p5.dom.min.js\"></script>\n"
//            + "<script src=\"https://unpkg.com/ml5@latest/dist/ml5.min.js\"></script>\n"
            + "<script src=\"../scripts/p5.min.js\"></script>\n"
            + "<script src=\"../scripts/p5.dom.min.js\"></script>\n"
            + "<script src=\"../scripts/ml5.min.js\"></script>\n"
            + "<script type=\"text/javascript\">\n"
            + "	// Model URL\n"
            + "	let classifier;\n"
            + "	let imageModelURL = './model/';\n"
            + "	let img;\n"
            + "	let imgName;\n"
            + "	let temp_imgName;\n"
            + "	let currentIndex = -2;\n"
            + "	let allImages = [];\n"
            + "	let predictions = [];  \n"
            + "	let label = \"\";\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "	  //connection.send('Ping from js');\n"
            + "	  connection.send('restart');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "   imgName=event.data;\n"
            + "   temp_imgName=(' ' + imgName).slice(1);\n"
//            + "   console.log(imgName);\n"
            + "	  currentIndex++;\n" 
            + "   if (!imgName.includes('Welcome to the server!') && !imgName.includes('[new client connection]: 127.0.0.1') ) {\n"
            + "		loadImage(temp_imgName, imageReady);\n"
            + "	  }else{\n"
            + "		loadImage('../temp.jpg', imageReady);\n"
            + "	   }\n"
            + "	  createDiv('image: ' +currentIndex+':'+ temp_imgName+':'+label);\n"
//            + "	  document.write('<div>'image: ' +currentIndex+':'+ temp_imgName+':'+label</div>');\n"
            + "	  console.log(temp_imgName+':'+label);\n"
            + "	  connection.send(temp_imgName+':'+label);\n"
            + " };    \n"
            + "	// Load the model first\n"
            + "	function preload() {\n"
            + "	  currentIndex=-2;\n"
            + "	  console.log(\"merhaba\\n\");\n"
            + "	  classifier = ml5.imageClassifier(imageModelURL + 'model.json');\n"
            + "	}\n"
            + "	// The function below will determine and setup the image sizes, get the results as image.\n"
            + "	function setup() {\n"
            + "	  currentIndex=-2;\n"
            + "	  createCanvas(224, 224);\n"
            + "	  background(0);\n"
            + "	  loadImage('../temp.jpg', imageReady);\n"
            + "	}\n"
            + "\n"
            + "	function imageReady(img) {\n"
            + "	  image(img, 0, 0);\n"
            + "	  classifier.classify(img, gotResult);\n"
            + "	}\n"
            + "\n"
            + "	function savePredictions() {\n"
            + "	  predictionsJSON = {\n"
            + "		predictions: predictions\n"
            + "	  };\n"
            + "	  saveJSON(predictionsJSON, 'predictions.json');\n"
            + "	}\n"
            + "\n"
            + "	// When we get the results\n"
            + "	function gotResult(err, results) {\n"
            + "	  // If there is an error, show in the console\n"
            + "	  if (err) {\n"
            + "		console.error(err);\n"
            + "	  }\n"
            + "\n"
            + "	  information = {\n"
            + "		name: allImages[currentIndex],\n"
            + "		result: results\n"
            + "	  };\n"
            + "\n"
//            + "	  predictions.push(information);\n"
//            + "	  createDiv('ID: ' + temp_imgName);\n"
            + "	  label=results[0].label;\n"
//            + "	  createDiv('Label: ' + label);\n"
//            + "	  createDiv('Confidence: ' + nf(results[0].confidence, 0, 2));\n"
//            + "	  connection.send(temp_imgName+':'+label);\n"
            + "	  \n"
            + "	}\n"
            + "</script>\n"
            + "";

    String scriptFilePath = "";
    ConfigurationJavaScript config = null;

    @Override
    public void setConfiguration(InterfaceConfiguration cnf) {
        this.config = (ConfigurationJavaScript) cnf;
    }

    @Override
    public void build() {
        String str = "";
        if (config.getLearningMode() == EnumLearningMode.TEST) {
            if (config.getDataSource() == EnumDataSource.IMAGE_FILE) {
                str = strML5;
                /*
                File[] dirs = FactoryUtils.getDirectories(config.getTestFolderPath());
                String ek = "class_names = [";
                for (int i = 0; i < dirs.length; i++) {
                    ek += "'" + dirs[i].getName() + "',";
                }
                ek = ek.substring(0, ek.length() - 1);
                ek = ek + "]\n";
                ek += "test_path=r'" + config.getTestFolderPath() + "'\n"
                        + "hist_hit=np.zeros(len(class_names))\n"
                        + "hist_error=np.zeros(len(class_names))\n";

                str += ek;
                 */
            }
        }
        String folder = config.getModelPath();
        scriptFilePath = folder + "\\index.html";
        FactoryUtils.writeToFile(scriptFilePath, str);
    }

    @Override
    public void execute(int port) {
        try {
            Runtime.getRuntime().exec("cmd /c http-server -p " + port);
        } catch (IOException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }

        FactoryUtils.startJavaServer();
        FactoryUtils.icbf = config.getCall_back();

        FactoryUtils.delay(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String folder = config.getModelPath();
                folder = folder.substring(folder.lastIndexOf("\\") + 1);
                try {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:" + port + "/models/" + folder});
                } catch (IOException ex) {
                    Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    private static File[] files;
    private static int currentIndex = 0;
    
    public static void main(String[] args) {
        System.out.println("user dir:" + FactoryUtils.getDefaultDirectory());
        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/open";
        files=FactoryUtils.getFileListInFolderForImages(imgPath);
        InferPistachio jdlp = new InferPistachio();
        Configuration config = new ConfigurationJavaScript()
                .setTestFolderPath(imgPath)
                .setBackEnd(EnumBackEnd.CPU)
                .setDataSource(EnumDataSource.IMAGE_FILE)
                .setLearningMode(EnumLearningMode.TEST)
                .setModelPath(FactoryUtils.getDefaultDirectory() + "\\models\\pistachio_rest")
                .setCallBackFunction(new InterfaceCallBack() {
                    @Override
                    public void onMessageReceived(String str) {
                        invokeMethod(str);
                    }

                });
        jdlp.setConfiguration(config);
        jdlp.build();
        jdlp.execute(8080);

    }

    private static long t1=FactoryUtils.tic();
    private static void invokeMethod(String str) {
        t1=FactoryUtils.toc(t1);
        System.out.println("str gelen= " + str);
        if (str.equals("restart")) {
            currentIndex=0;
            return;
        }
        if (currentIndex>=files.length-1) {
            System.out.println(currentIndex+":currentIndex index overflow error");
            return;
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        String s=files[currentIndex].getAbsolutePath();
        s=s.replace(FactoryUtils.currDir,"");
        System.out.println("tensorflowjs'ye giden mesaj: = " + s);
        FactoryUtils.server.broadcast(s);
        currentIndex++;
        t1=FactoryUtils.toc(t1);
    }

}
