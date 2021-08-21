/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.test.pistachio;

import cezeri.deep_learning.tensorflow_js.core.ConfigurationJavaScript;
import cezeri.enums.EnumBackEnd;
import cezeri.enums.EnumDataSource;
import cezeri.enums.EnumLearningMode;
import cezeri.interfaces.Configuration;
import cezeri.interfaces.InterfaceCallBack;
import cezeri.interfaces.InterfaceConfiguration;
import cezeri.interfaces.InterfaceDeepLearning;
import cezeri.enums.EnumOperatingSystem;
import cezeri.factory.FactoryTensorFlowJS;
import cezeri.factory.FactoryUtils;
import cezeri.websocket.SocketServer;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
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

    String python_script = "# -*- coding: utf-8 -*-\n"
            + "\"\"\"\n"
            + "Created on Thu Aug 19 15:37:07 2021\n"
            + "\n"
            + "@author: cezerilab\n"
            + "\"\"\"\n"
            + "import tensorflow as tf\n"
            + "import keras\n"
            + "from keras import backend as K\n"
            + "from keras.layers.core import Dense, Activation\n"
            + "from keras.optimizers import Adam\n"
            + "from keras.metrics import categorical_crossentropy\n"
            + "from keras.preprocessing.image import ImageDataGenerator\n"
            + "from keras.preprocessing import image\n"
            + "from keras.models import Model\n"
            + "from keras.applications import imagenet_utils\n"
            + "from keras.layers import Dense,GlobalAveragePooling2D\n"
            + "from keras.applications import MobileNet\n"
            + "from keras.applications.mobilenet import preprocess_input\n"
            + "import numpy as np\n"
            + "from IPython.display import Image\n"
            + "from keras.optimizers import Adam\n"
            + "import timeit\n"
            + "\n"
            + "\n"
            + "print(tf.__version__)\n"
            + "\n"
            + "mobile = keras.applications.mobilenet.MobileNet()\n"
            + "def prepare_image(file):\n"
            + "    img_path = ''\n"
            + "    img = image.load_img(img_path + file, target_size=(224, 224))\n"
            + "    img_array = image.img_to_array(img)\n"
            + "    img_array_expanded_dims = np.expand_dims(img_array, axis=0)\n"
            + "    return keras.applications.mobilenet.preprocess_input(img_array_expanded_dims)\n"
            + "\n"
            + "start = timeit.default_timer()\n"
            + "for i in range(50):\n"
            + "    preprocessed_image = prepare_image('./images/mixed/'+str(i)+'.jpg')\n"
            + "    predictions = mobile.predict(preprocessed_image)\n"
            + "    results = imagenet_utils.decode_predictions(predictions)\n"
            + "    print(results[0][0])\n"
            + "\n"
            + "stop = timeit.default_timer()\n"
            + "print('Time: ', stop - start,' seconds')  ";

    private final static String strML5_sync = "\n"
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
            + " connection.onmessage = async function (event) {\n"
            + "   imgName=event.data;\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1') || imgName.includes('baslayabilirsin' )) {\n"
            + "     connection.send(imgName);\n"
            + "	  }else{\n"
            //            + "	    images[index]=imgName;\n"
            + "	    let img=await loadImage(imgName); imageReady(img,imgName);\n"
            //            + "	    if (index==0) { let img=loadImage(imgName); imageReady(img,imgName); }\n"
            //            + "	    index++;\n"
            + "	   }\n"
            + " };\n"
            + "\n"
            + "	async function imageReady(img,imgName) {\n"
            //            + "   console.log('path burada='+imgName);\n"
            + "	  image(img, 0, 0);\n"
            + "	  let err;\n"
            + "	  let labels=await classifier.classify(img);\n"
            + "	  gotResult(err,labels,imgName);\n"
            + "	}\n"
            + "\n"
            + "	// When we get the results\n"
            + "	function gotResult(err, results,imgName) {\n"
            //            + "   console.log('ikinci path burada='+imgName);\n"
            + "	  if (err) {\n"
            + "		console.error(err);\n"
            + "	  }\n"
            + "	  label=results[0].label;\n"
            + "	  createDiv('image: ' +currentIndex+':'+ imgName+':'+label);\n"
            + "	  connection.send(imgName+':'+label);\n"
            + "   console.log(new Date().toLocaleTimeString());\n"
            + "	  currentIndex++;\n"
            //            + "   var checkExist = setInterval(function() {\n" +
            //            "       if (images.length>currentIndex) {\n" +
            //            "         clearInterval(checkExist);\n" +
            ////            "         loadImage(images[currentIndex], imageReady);\n" +
            //            "         let img=loadImage(imgName); imageReady(img,imgName);\n" +
            //            "       }\n" +
            //            "     }, 1);\n"
            //            + "	  if(currentIndex<images.length) loadImage(images[currentIndex], imageReady);\n"
            + "	}\n"
            + "</script>\n"
            + "";

    /**
     * JS tek threadle çalıştığı için ve multi-threaded yapısı olmadığı için,
     * promise then ile yapılmaya çalışılsa bile classification için gereken
     * süre azaltılamıyor
     */
    private final static String strML5_Promise = "\n"
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
            + "	    let img=loadImage(imgName); if(img) {imageReady(img,imgName);}\n"
            + "	  }\n"
            + " };\n"
            + "\n"
            + " function imageReady(img,imgName) {\n"
            + "	  image(img, 0, 0);\n"
            + "	  classifier.classify(img)\n"
            + "	  .then(results => {"
            + "                     //console.log(results);\n"
            + "                     console.log(new Date().toLocaleTimeString());\n"
            + "                     label=results[0].label;\n"
            + "                     createDiv('image: ' +currentIndex+':'+ imgName+':'+label);\n"
            + "                     connection.send(imgName+':'+label);\n"
            + "                     currentIndex++;\n"
            + "                     })\n"
            + "	}\n"
            + "\n"
            + "</script>\n"
            + "";

    private static String strML5 = "\n"
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
            //            + "	    loadImage(imgName, imageReady);\n"
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
            + "   var checkExist = setInterval(function() {\n"
            + "       if (images.length>currentIndex) {\n"
            + "         clearInterval(checkExist);\n"
            + "         loadImage(images[currentIndex], imageReady);;\n"
            + "       }\n"
            + "     }, 1);\n"
            //            + "	  if(currentIndex<images.length) loadImage(images[currentIndex], imageReady);\n"
            + "	}\n"
            + "</script>\n"
            + "";
    String scriptFilePath = "";
    ConfigurationJavaScript config = null;
    private static int HTTP_SERVER_PORT;
    private static int WEB_SOCKET_PORT;
    private static String MODEL_PATH;
    private static EnumOperatingSystem OS;

    public SocketServer startTensorFlowJS(EnumOperatingSystem OS, String modelPath, final int SERVER_PORT, final int SOCKET_PORT, InterfaceCallBack invokeMethod) {
        MODEL_PATH = modelPath;
        HTTP_SERVER_PORT = SERVER_PORT;
        WEB_SOCKET_PORT = SOCKET_PORT;
        strML5 = strML5.replace("8887", "" + WEB_SOCKET_PORT);
        String str = strML5;
        scriptFilePath = modelPath + "/index.html";
        FactoryUtils.writeToFile(scriptFilePath, str);
        
        System.out.println("Application current path = " + FactoryUtils.getDefaultDirectory());

        InferPistachio jdlp = new InferPistachio();
//        Configuration config = new ConfigurationJavaScript()
//                .setBackEnd(EnumBackEnd.WEBGL)
//                .setDataSource(EnumDataSource.IMAGE_FILE)
//                .setLearningMode(EnumLearningMode.TEST)
//                .setModelPath(FactoryUtils.getDefaultDirectory() + "/" + modelPath)
//                .setCallBackFunction(invokeMethod);
//
//        jdlp.setConfiguration(config);
//        FactoryUtils.icbf =config.getCall_back();
        FactoryUtils.icbf=invokeMethod;
        SocketServer server = jdlp.execute();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return server;
    }

    @Override
    public void setConfiguration(InterfaceConfiguration cnf) {
        this.config = (ConfigurationJavaScript) cnf;
    }

//    @Override
//    public void build(int port) {
//        String str = "";
//        if (config.getLearningMode() == EnumLearningMode.TEST) {
//            if (config.getDataSource() == EnumDataSource.IMAGE_FILE) {
//                strML5=strML5.replace("8887",""+port);                
//                str = strML5;
//            }
//        }
//        String folder = config.getModelPath();
//        scriptFilePath = folder + "/index.html";
//        FactoryUtils.writeToFile(scriptFilePath, str);
//    }
    @Override
    public SocketServer execute() {

        try {
            //FOR WINDOWS
            Runtime.getRuntime().exec("cmd /c http-server -p " + HTTP_SERVER_PORT);
            //FOR LINUX
            //Runtime.getRuntime().exec("/bin/bash -c http-server -p " + serverPort);
        } catch (IOException ex) {
//            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }

        SocketServer server = null;
        try {
            server = new SocketServer(WEB_SOCKET_PORT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FactoryTensorFlowJS.class.getName()).log(Level.SEVERE, null, ex);
        }

        FactoryUtils.startJavaServer(server);
//        FactoryUtils.icbf = config.getCall_back();

        FactoryUtils.delay(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String folder = config.getModelPath();
//                folder = folder.substring(folder.lastIndexOf("/") + 1);
                try {
                    //FOR WINDOWS
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome -new-window  http://localhost:" + HTTP_SERVER_PORT + "/" + MODEL_PATH});
                    //FOR LINUX
                    //Runtime.getRuntime().exec(new String[]{"bash", "-c", "chromium-browser -new-window http://localhost:" + serverPort + "/models/" + folder});
                } catch (IOException ex) {
//                    Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        return server;
    }

    private static File[] files;
    private static int currentIndex = 0;
    private SocketServer server = null;

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
        jdlp.server=jdlp.startTensorFlowJS(EnumOperatingSystem.WINDOWS, "models/pistachio_rest", 8080, 8887, jdlp::invokeMethod);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        simulateSendingImagesInDifferentThread(jdlp.server);
    }

    private static int hit = 0;
    private static int err = 0;
    private static double acc = 0;
    private static long t1 = FactoryUtils.tic();
    private static long t2 = FactoryUtils.tic();

    private static void simulateSendingImagesInDifferentThread(SocketServer server) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = "";
//                int i = 0;
                for (int i = 0; i < files.length; i++) {
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
                    server.broadcast(s);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    t1 = FactoryUtils.toc(t1);
                }
            }
        }).start();

    }

    private void invokeMethod(String str) {
        System.out.println("str gelen= " + str);
        t2 = FactoryUtils.toc("invoke:", t2);
        if (str.contains("Welcome to the server!") || str.contains("[new client connection]: 127.0.0.1")) {
            //System.out.println("str gelen= " + str);
        } else {
            if (str.equals("restart")) {
                //System.out.println("str gelen= " + str);
                currentIndex = 0;
                hit = err = 0;
                server.broadcast("baslayabilirsin");
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
