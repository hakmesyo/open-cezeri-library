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
 * @author cezerilab
 */
public class InferPistachio_2 implements InterfaceDeepLearning {

    private static boolean canSend = false;

    private final static String strML5_blob = ""
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
            + "	let allImages = [];\n"
            + "	let predictions = [];  \n"
            + "	let label = \"\";\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + " connection.binaryType = 'blob';\n"
            + "	connection.onopen = function () {\n"
            + "	  connection.send('restart');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "   imgName=event.data;\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1') ) {\n"
            + "             console.log('onmessage ::  ' + imgName);\n"
            + "	        connection.send(imgName);\n"
            + " }else{;    \n"
            //            + "        if (imgName instanceof Base64) {\n"
            + "               var msg = imgName;\n"
            //            + "               var image = new Image(224,224);\n"
            //            + "               image.src = event;\n"
            //            + "                 var blobUrl = URL.createObjectURL(new Blob([msg]));\n" +
            //            "                   var imge = new Image(224,224);\n" +
            //            "                   imge.src = blobUrl;\n"
            //            + "                 var arrayBuffer;\n" +
            //                "               var fileReader = new FileReader();\n" +
            //                "               fileReader.onload = function() {\n" +
            //                "                   arrayBuffer = this.result;\n" +
            //                "               };\n" +
            //                "               fileReader.readAsArrayBuffer(imgName);"+
            //                "               var byteArray = new Uint8Array(arrayBuffer);\n" +
            //                "               for (var i = 0; i < byteArray.byteLength; i++) {\n" +
            //                "               // do something with each byte in the array\n" +
            //                "               } "
            //             "                 int blobLength = (int) msg.length();  \n" 
            //            + "                 byte[] blobAsBytes = msg.getBytes(1, blobLength);\n"
            + "byteArray=_base64ToArrayBuffer(msg);\n"
            + "                 console.log('msg size: '+byteArray.length);\n"
            + "                 img=createImage(224,224);\n"
            + "                 img.loadPixels();\n"
            + //             "                 for (var i = 0; i < img.pixels.length; i++) {\n" +
            //            "                   img.pixels[i] = image.pixels[i];\n" +
            //            "                 }\n" +
            "                   let k=0;\n"
            + "                   for (let x = 0; x < img.width; x++) {\n"
            + "                       for (let y = 0; y < img.height; y++) {\n"
            + "                           img.set(x, y, [byteArray[0], byteArray[1], byteArray[2], byteArray[3]]);\n"
            + "                       }\n"
            + "                   }\n"
            + "                 img.updatePixels();"
            + "                 imageReady(img);\n"
            //            + "           }else{connection.send('send me images now');}\n"
            //            + "	    loadImage(imgName, imageReady);\n"
            //                        + "}else{\n"
            + "           };    \n"
            + " };    \n"
            + "function _base64ToArrayBuffer(base64) {\n"
            + "    var binary_string = window.atob(base64);\n"
            + "    var len = binary_string.length;\n"
            + "    var bytes = new Uint8Array(len);\n"
            + "    for (var i = 0; i < len; i++) {\n"
            + "        bytes[i] = binary_string.charCodeAt(i);\n"
            + "    }\n"
            + "    return bytes;\n"
            + "}"
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
            + "	  currentIndex++;\n"
            + "	  label=results[0].label;\n"
            + "	  createDiv('image: ' +currentIndex+':'+ imgName+':'+label);\n"
            + "	  console.log(imgName+':'+label);\n"
            + "	  connection.send(imgName+':'+label);\n"
            + "	  \n"
            + "	}\n"
            + "</script>\n"
            + "";

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
            + "	let allImages = [];\n"
            + "	let predictions = [];  \n"
            + "	let label = \"\";\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "	  connection.send('restart');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "   imgName=event.data;\n"
            + "   if (imgName.includes('Welcome to the server!') || imgName.includes('[new client connection]: 127.0.0.1') ) {\n"
            + "	        connection.send(imgName);\n"
            + "	  }else{\n"
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
            + "	  currentIndex++;\n"
            + "	  label=results[0].label;\n"
            + "	  createDiv('image: ' +currentIndex+':'+ imgName+':'+label);\n"
            + "	  console.log(imgName+':'+label);\n"
            + "	  connection.send(imgName+':'+label);\n"
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
            Logger.getLogger(InferPistachio_2.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(InferPistachio_2.class.getName()).log(Level.SEVERE, null, ex);
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
        String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/test";
        files = FactoryUtils.getFileListDataSetForImageClassification(imgPath);
        files = FactoryUtils.shuffle(files, new Random(new Random().nextInt()));
        InferPistachio_2 jdlp = new InferPistachio_2();
        Configuration config = new ConfigurationJavaScript()
                .setTestFolderPath(imgPath)
                .setBackEnd(EnumBackEnd.CPU)
                .setDataSource(EnumDataSource.IMAGE_FILE)
                .setLearningMode(EnumLearningMode.TEST)
                .setModelPath(FactoryUtils.getDefaultDirectory() + "\\models\\pistachio_rest")
                .setCallBackFunction(InferPistachio_2::invokeMethod);
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

    }

    private static int hit=0;
    private static int err=0;
    private static double acc =0;
    private static long t1=FactoryUtils.tic();
    
    /**
     * aşağıdaki kodlarda buradaki bilgilerden faydalanılmıştır: amacımız java ve javascript arasında imgenin obje ile gönderilmesini
     * sağlamaktır. Bunun yapmamızın temel nedeni, node.js nin imge pathindeki imgeyi load ederken maliyetinin çok yüksek olmasıdır.
     * 1- https://stackoverflow.com/questions/17878201/how-to-send-an-image-from-a-java-websocket-server-to-use-in-an-html5-canvas
     * 
     * 2- https://stackoverflow.com/questions/10226046/java-convert-image-to-base64
     * 
     * 3- veya bu https://stackoverflow.com/questions/60531412/how-to-send-an-image-from-server-to-client-with-web-socket-using-java-javascri
     * 
     * @param str 
     */
    private static void invokeMethod(String str) {
        t1=FactoryUtils.toc(t1);
        System.out.println("str gelen= " + str);
        if (str.contains("Welcome to the server!")) {
        } else if (str.contains("restart")) {
            currentIndex = 0;
            hit=err=0;
        } else {
            if (currentIndex >= files.length - 1) {
                acc=1.0*hit/files.length*100;
                System.out.println("accuracy = " + acc);
                System.out.println("hit = " + hit);
                System.out.println("err = " + err);
            } else {
                String path=str.split(":")[0];
                String label=str.split(":")[1];
                if (path.contains(label)) {
                    hit++;
                    System.out.println("hit = " + hit);
                }else{
                    err++;
                    System.out.println("err = " + err);
                }
                String s = files[currentIndex].getAbsolutePath();
                s = s.replace(FactoryUtils.currDir, "");
                System.out.println("tensorflowjs'ye giden mesaj: = " + s);
                FactoryUtils.server.broadcast(s);
                canSend = false;
                currentIndex++;
            }
        }
    }

}
