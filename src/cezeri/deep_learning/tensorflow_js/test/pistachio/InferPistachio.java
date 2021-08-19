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
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab linux ubuntuda yani jetsn nano da cpu ve gpu utilization
 * için sudo jtop çalıştır
 *
 */
public class InferPistachio implements InterfaceDeepLearning {

//    private static boolean canSend = false;
    private final static String strML5 = "\n"
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
            //            + "	  console.log(images[currentIndex]+':'+label);\n"
            + "	  connection.send(images[currentIndex]+':'+label);\n"
            + "   console.log(new Date().toLocaleTimeString());\n"
            + "	  currentIndex++;\n"
            + "   var checkExist = setInterval(function() {\n" +
            "       if (images.length>currentIndex) {\n" +
            "         clearInterval(checkExist);\n" +
            "         loadImage(images[currentIndex], imageReady);;\n" +
            "       }\n" +
            "     }, 1);\n"
//            + "	  if(currentIndex<images.length) loadImage(images[currentIndex], imageReady);\n"
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
        scriptFilePath = folder + "/index.html";
        FactoryUtils.writeToFile(scriptFilePath, str);
    }

    @Override
    public void execute(int port) {
        try {
            //FOR WINDOWS
            Runtime.getRuntime().exec("cmd /c http-server -p " + port);
            //FOR LINUX
            //Runtime.getRuntime().exec("/bin/bash -c http-server -p " + port);
        } catch (IOException ex) {
//            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }

        FactoryUtils.startJavaServer(8887);
        FactoryUtils.icbf = config.getCall_back();

        FactoryUtils.delay(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String folder = config.getModelPath();
                folder = folder.substring(folder.lastIndexOf("/") + 1);
                try {
                    //FOR WINDOWS
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:" + port + "/models/" + folder});
                    //FOR LINUX
                    //Runtime.getRuntime().exec(new String[]{"bash", "-c", "chromium-browser http://localhost:" + port + "/models/" + folder});
                } catch (IOException ex) {
//                    Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    private static File[] files;
    private static int currentIndex = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("user dir:" + FactoryUtils.getDefaultDirectory());
//        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/test/close";
//        files = FactoryUtils.getFileListInFolderForImages(imgPath);
        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/test";
        files = FactoryUtils.getFileListDataSetForImageClassification(imgPath);
        files = FactoryUtils.shuffle(files, new Random(123));
//        files = FactoryUtils.shuffle(files, new Random());
        System.out.println("files size:" + files.length);
        InferPistachio jdlp = new InferPistachio();
        Configuration config = new ConfigurationJavaScript()
                .setTestFolderPath(imgPath)
                .setBackEnd(EnumBackEnd.CPU)
                .setDataSource(EnumDataSource.IMAGE_FILE)
                .setLearningMode(EnumLearningMode.TEST)
                .setModelPath(FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest")
                .setCallBackFunction(InferPistachio::invokeMethod);
        jdlp.setConfiguration(config);
        jdlp.build();
        jdlp.execute(8080);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        simulateSendingImagesInDifferentThread();
    }

    private static int hit = 0;
    private static int err = 0;
    private static double acc = 0;
    private static boolean canSend = true;
    private static long t1 = FactoryUtils.tic();
    private static long t2 = FactoryUtils.tic();

    private static void simulateSendingImagesInDifferentThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long t1 = 0;
                String s = "";
//                int i = 0;
                for (int i=0;i<files.length;i++) {
//                for (;;) {
//                    if (i>560) {
//                        break;
//                    }
//                    int n = (int) (Math.random() * files.length);
//                    System.out.println("randomly selected index = " + n);
                    System.out.println("i = " + i);
                    s = files[i].getAbsolutePath();
//                    i = i % files.length;
                    //FOR WINDOWS
                    s = "./" + s.replace(FactoryUtils.currDir + "\\models\\pistachio_rest\\", "");
                    //FOR LINUX
//                    s = "./" + s.replace(FactoryUtils.currDir + "/models/pistachio_rest/", "");
                    System.out.println("tensorflowjs'ye giden mesaj: = " + s);
                    FactoryUtils.server.broadcast(s);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    t1 = FactoryUtils.toc(t1);
                }
            }
        }).start();

    }

    private static void invokeMethod(String str) {
        System.out.println("str gelen= " + str);
        t2=FactoryUtils.toc(t2);
        if (str.contains("Welcome to the server!") || str.contains("[new client connection]: 127.0.0.1")) {
            //System.out.println("str gelen= " + str);
        } else {
            if (str.equals("restart")) {
                //System.out.println("str gelen= " + str);
                currentIndex = 0;
                hit = err = 0;
                FactoryUtils.server.broadcast("baslayabilirsin");
                return;
            } else {
                if (str.split(":").length > 1) {
                    String path = str.split(":")[0];
                    String label = str.split(":")[1];
                    if (path.contains(label)) {
                        hit++;
                    } else {
                        err++;
                    }
                    acc = FactoryUtils.formatDouble(1.0 * hit / (hit + err) * 100);
                    System.out.println("acc:" + acc + "% hit:" + hit + " err:" + err + " gelen mesaj:" + str);
                }
            }
        }
    }

}
