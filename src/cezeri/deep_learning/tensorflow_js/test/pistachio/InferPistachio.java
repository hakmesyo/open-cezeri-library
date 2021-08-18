/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.test.pistachio;

import cezeri.deep_learning.tensorflow_js.core.ConfigurationJavaScript;
import cezeri.deep_learning.tensorflow_js.enums.EnumBackEnd;
import cezeri.deep_learning.tensorflow_js.enums.EnumDataSource;
import cezeri.deep_learning.tensorflow_js.enums.EnumLearningMode;
import cezeri.deep_learning.tensorflow_js.interfaces.Configuration;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceConfiguration;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceDeepLearning;
import cezeri.factory.FactoryUtils;
import cezeri.image_processing.ImageProcess;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author cezerilab
 */
public class InferPistachio implements InterfaceDeepLearning {

//    private static boolean canSend = false;
    private final static String strML5 = ""
            + "<div>Teachable Machine Image Model - p5.js and ml5.js</div>\n"
            + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/p5.min.js\"></script>\n"
            + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/addons/p5.dom.min.js\"></script>\n"
            + "<script src=\"https://unpkg.com/ml5@latest/dist/ml5.min.js\"></script>\n"
            //            + "<script src=\"../scripts/p5.min.js\"></script>\n"
            //            + "<script src=\"../scripts/p5.dom.min.js\"></script>\n"
            //            + "<script src=\"../scripts/ml5.min.js\"></script>\n"
            + "<script type=\"text/javascript\">\n"
            + "	let classifier;\n"
            + "	let imageModelURL = './model/';\n"
            + "	let img;\n"
            + "	let imgName;\n"
            + "	let currentIndex = 0;\n"
            + "	let index = 0;\n"
            + "	let allImages = [];\n"
            + "	let images = [];\n"
            + "	let predictions = [];  \n"
            + "	let label = \"\";\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "	  connection.send('restart');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "   imgName=event.data;\n"
            //            + "	  connection.send(imgName+':close');\n"
            //            + "	  console.log(imgName);\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1') || imgName.includes('baslayabilirsin' )) {\n"
            + "	        connection.send(imgName);\n"
            + "	  }else{\n"
            + "	    images[index]=imgName;\n"
            + "	    index++;\n"
            + "	    loadImage(imgName, imageReady);\n"
            + "	   }\n"
            + " };    \n"
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
            //            + "	  loadImage('../temp.jpg', imageReady);\n"
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
            + "	  createDiv('image: ' +currentIndex+':'+ images[currentIndex]+':'+label);\n"
            + "	  console.log(images[currentIndex]+':'+label);\n"
            + "	  connection.send(images[currentIndex]+':'+label);\n"
            + "   console.log(new Date().toLocaleTimeString());\n"
            + "   //images.shift();\n"
            + "	  currentIndex++;\n"
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
            //FOR LINUX
//            Runtime.getRuntime().exec("/bin/bash -c http-server -p " + port);
        } catch (IOException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }

        FactoryUtils.startJavaServer(8887);
        FactoryUtils.icbf = config.getCall_back();

        FactoryUtils.delay(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String folder = config.getModelPath();
                folder = folder.substring(folder.lastIndexOf("\\") + 1);
                try {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:" + port + "/models/" + folder});
                    //FOR LINUX
//                  Runtime.getRuntime().exec(new String[]{"bash", "-c", "chromium-browser http://localhost:" + port + "/models/" + folder});
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
//        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/test";
//        files = FactoryUtils.getFileListInFolderForImages(imgPath);
        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/mixed";
        files = FactoryUtils.getFileListDataSetForImageClassification(imgPath);
        files = FactoryUtils.shuffle(files, new Random(123));
        InferPistachio jdlp = new InferPistachio();
        Configuration config = new ConfigurationJavaScript()
                .setTestFolderPath(imgPath)
                .setBackEnd(EnumBackEnd.CPU)
                .setDataSource(EnumDataSource.IMAGE_FILE)
                .setLearningMode(EnumLearningMode.TEST)
                .setModelPath(FactoryUtils.getDefaultDirectory() + "\\models\\pistachio_rest")
                .setCallBackFunction(InferPistachio::invokeMethod);
//                .setCallBackFunction(new InterfaceCallBack() {
//                    @Override
//                    public void onMessageReceived(String str) {
//                        invokeMethod(str);
//                    }
//
//                });

        jdlp.setConfiguration(config);
        jdlp.build();
        jdlp.execute(8080);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                }
//                int i=0;
                for (int i=0;i<files.length;i++) {
                    String s = files[i].getAbsolutePath();
                    s = "./" +s.replace(FactoryUtils.currDir + "\\models\\pistachio_rest\\", "");
                    System.out.println("tensorflowjs'ye giden mesaj: = " + s);
                    FactoryUtils.server.broadcast(s);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //i=i%files.length;
                }
            }
        }).start();

    }

    private static int hit = 0;
    private static int err = 0;
    private static double acc = 0;
    private static long t1 = FactoryUtils.tic();

    /**
     * aşağıdaki kodlarda buradaki bilgilerden faydalanılmıştır: amacımız java
     * ve javascript arasında imgenin obje ile gönderilmesini sağlamaktır. Bunun
     * yapmamızın temel nedeni, node.js nin imge pathindeki imgeyi load ederken
     * maliyetinin çok yüksek olmasıdır. 1-
     * https://stackoverflow.com/questions/17878201/how-to-send-an-image-from-a-java-websocket-server-to-use-in-an-html5-canvas
     *
     * 2-
     * https://stackoverflow.com/questions/10226046/java-convert-image-to-base64
     *
     * 3- veya bu
     * https://stackoverflow.com/questions/60531412/how-to-send-an-image-from-server-to-client-with-web-socket-using-java-javascri
     *
     * @param str
     */
    private static void invokeMethod(String str) {
        t1 = FactoryUtils.toc(t1);
        System.out.println("str gelen= " + str);
        if (str.contains("Welcome to the server!")) {
        } else {
            if (str.equals("restart")) {
                currentIndex = 0;
                hit = err = 0;
                FactoryUtils.server.broadcast("baslayabilirsin");
                return;
            }
            if (currentIndex >= files.length - 1) {
                acc = 1.0 * hit / files.length * 100;
                System.out.println("accuracy = " + acc);
                System.out.println("hit = " + hit);
                System.out.println("err = " + err);
            } else {
                if (str.split(":").length > 1) {
                    String path = str.split(":")[0];
                    String label = str.split(":")[1];
                    if (path.contains(label)) {
                        hit++;
                        System.out.println("hit = " + hit);
                    } else {
                        err++;
                        System.out.println("err = " + err);
                    }
                }
//                String s = files[currentIndex].getAbsolutePath();
//                s = s.replace(FactoryUtils.currDir+"\\models\\pistachio_rest\\", "");
//                //s = s.substring(1, s.length());
//                s="./"+s;
//                System.out.println("tensorflowjs'ye giden mesaj: = " + s);
//                FactoryUtils.server.broadcast(s);
//                BufferedImage img = ImageProcess.imread(s);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                try {
//                    ImageIO.write(img, "png", baos);
//                } catch (IOException ex) {
//                    Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                byte[] byteArray = baos.toByteArray();
//                String base64String = Base64.getEncoder().encodeToString(byteArray);
//                FactoryUtils.server.broadcast(base64String);
//                canSend = false;
                currentIndex++;
            }
        }
    }

}
