/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_rest.test.pistachio;

import cezeri.deep_learning_rest.core.ConfigurationJavaScript;
import cezeri.deep_learning_rest.core.JDL_JavaScript;
import cezeri.deep_learning_rest.enums.EnumBackEnd;
import cezeri.deep_learning_rest.enums.EnumDataSource;
import cezeri.deep_learning_rest.enums.EnumLearningMode;
import cezeri.deep_learning_rest.interfaces.Configuration;
import cezeri.deep_learning_rest.interfaces.InterfaceCallBack;
import cezeri.deep_learning_rest.interfaces.InterfaceConfiguration;
import cezeri.deep_learning_rest.interfaces.InterfaceDeepLearning;
import cezeri.utils.SerialType;
import cezeri.factory.FactoryUtils;
import cezeri.utils.SerialLib;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j;

/**
 *
 * @author cezerilab
 */
public class YedekInferPistachio implements InterfaceDeepLearning {

    private final static String strJSImageFile
            = ""
            + "<html>\n"
            + "<body >\n"
            + "<link rel=\"shortcut icon\" href=\"#\" />\n"
            + "<div>Teachable Machine Image Model</div>\n"
            + "<button type=\"button\" onclick=\"init()\">Start</button>\n"
            + "<div id=\"webcam-container\"></div>\n"
            + "<div id=\"label-container\"></div>\n"
            + "<script src=\"https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@1.3.1/dist/tf.min.js\"></script>\n"
            + "<script src=\"https://cdn.jsdelivr.net/npm/@teachablemachine/image@0.8/dist/teachablemachine-image.min.js\"></script>\n"
            + "<script type=\"text/javascript\">\n"
            + "	navigator.webkitPersistentStorage.requestQuota(1024*1024, function() {\n"
            + "	  window.webkitRequestFileSystem(window.PERSISTENT , 1024*1024, SaveDatFileBro);\n"
            + "	})   \n"
            + "	\n"
            + "	function SaveDatFileBro(localstorage) {\n"
            + "	  localstorage.root.getFile(\"info.txt\", {create: true}, function(DatFile) {\n"
            + "		DatFile.createWriter(function(DatContent) {\n"
            + "		  var blob = new Blob([\"Lorem Ipsum\"], {type: \"text/plain\"});\n"
            + "		  DatContent.write(blob);\n"
            + "		});\n"
            + "	  });\n"
            + "	}	\n"
            + "	// More API functions here:\n"
            + "    // https://github.com/googlecreativelab/teachablemachine-community/tree/master/libraries/image\n"
            + "\n"
            + "    // the link to your model provided by Teachable Machine export panel\n"
            + "    //const URL = \"./my_model/\";\n"
            + "    const URL = 'model/';\n"
            + "	\n"
            + "	\n"
            + "    let model, webcam, labelContainer, maxPredictions;\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "	  connection.send('Ping');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "  console.log(event.data);\n"
            + " }"
            + "	// Load the image model and setup the webcam\n"
            + "    async function init() {\n"
            + "		\n"
            + "        const modelURL = URL + \"model.json\";\n"
            + "        const metadataURL = URL + \"metadata.json\";\n"
            + "\n"
            + "        // load the model and metadata\n"
            + "        // Refer to tmImage.loadFromFiles() in the API to support files from a file picker\n"
            + "        // or files from your local hard drive\n"
            + "        // Note: the pose library adds \"tmImage\" object to your window (window.tmImage)\n"
            + "        model = await tmImage.load(modelURL, metadataURL);\n"
            + "        maxPredictions = model.getTotalClasses();\n"
            + "\n"
            + "        // Convenience function to setup a webcam\n"
            + "        const flip = true; // whether to flip the webcam\n"
            + "        webcam = new tmImage.Webcam(244, 244, flip); // width, height, flip\n"
            + "        await webcam.setup(); // request access to the webcam\n"
            + "        await webcam.play();\n"
            + "        window.requestAnimationFrame(loop);\n"
            + "\n"
            + "        // append elements to the DOM\n"
            + "        document.getElementById(\"webcam-container\").appendChild(webcam.canvas);\n"
            + "        labelContainer = document.getElementById(\"label-container\");\n"
            + "        for (let i = 0; i < maxPredictions; i++) { // and class labels\n"
            + "            labelContainer.appendChild(document.createElement(\"div\"));\n"
            + "        }\n"
            + "    }\n"
            + "\n"
            + "    async function loop() {\n"
            + "        webcam.update(); // update the webcam frame\n"
            + "        await predict();\n"
            + "        window.requestAnimationFrame(loop);\n"
            + "    }\n"
            + "\n"
            + "	\n"
            + "    // run the webcam image through the image model\n"
            + "    async function predict() {\n"
            + "		startTime = new Date();\n"
            + "        // predict can take in an image, video or canvas html element\n"
            + "        const prediction = await model.predict(webcam.canvas);\n"
            + "		var max=0;\n"
            + "		var index=0;\n"
            + "        for (let i = 0; i < maxPredictions; i++) {\n"
            + "            const classPrediction =\n"
            + "                prediction[i].className + \": \" + prediction[i].probability.toFixed(2);\n"
            + "            labelContainer.childNodes[i].innerHTML = classPrediction;\n"
            + "			if (max<prediction[i].probability.toFixed(2)){\n"
            + "				max=prediction[i].probability.toFixed(2);\n"
            + "				index=i;\n"
            + "			}\n"
            + "        }\n"
            + "		//let data = prediction[index].className + \":\" + prediction[index].probability.toFixed(2);\n"
            + "		let data = prediction[index].className;\n"
            + "		document.getElementById(\"prediction-container\").innerHTML=\"Predicted Class is:\"+data;\n"
            + "		endTime = new Date();\n"
            + "		var timeDiff = endTime - startTime; //in ms\n"
            + "		document.getElementById(\"tlapse\").innerHTML=\"elapsed time:\"+timeDiff;\n"
            + "		connection.send('Predicted Class is:'+data);\n"
            + "    }\n"
            + "</script>\n"
            + "<div id=\"prediction-container\"></div>\n"
            + "<div id=\"tlapse\"></div>\n"
            + "</body>\n"
            + "</html>\n"
            + "";

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
            + "	let currentIndex = 0;\n"
            + "	let allImages = [];\n"
            + "	let predictions = [];  \n"
            + "	let label = \"\";\n"
            + "\n"
            + "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"
            + "	connection.onopen = function () {\n"
            + "	  //connection.send('Ping from js');\n"
            + "	};    \n"
            + " connection.onmessage = function (event) {\n"
            + "   console.log(event.data);\n"
            + "   imgName=event.data;\n"
            + " };    \n"
            + "	// Load the model first\n"
            + "	function preload() {\n"
            + "	  console.log(\"merhaba\\n\");\n"
            + "	  classifier = ml5.imageClassifier(imageModelURL + 'model.json');\n"
            + "	}\n"
            + "	// The function below will determine and setup the image sizes, get the results as image.\n"
            + "	function setup() {\n"
            + "	 createCanvas(224, 224);\n"
            + "	 background(0);\n"
            + "	 loadImage('./images/close/'+currentIndex+'.jpg', imageReady);\n"
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
            + "	  predictions.push(information);\n"
            + "	  createDiv('ID: ' + currentIndex);\n"
            + "	  createDiv('Label: ' + results[0].label);\n"
            + "	  createDiv('Confidence: ' + nf(results[0].confidence, 0, 2));\n"
            + "	  \n"
            + "	  currentIndex++;\n" 
            + "	  if (currentIndex <= 300) {\n"
            + "		//loadImage('./images/close/'+currentIndex+'.jpg', imageReady);\n"
            + "	     if (!imgName.includes('[')) {\n"
            + "	        connection.send(currentIndex.toString()+' processed now');\n"
            + "		loadImage('./images/close/'+imgName, imageReady);\n"
            + "		console.log(imgName);\n"
            + "	     }else{\n"
            + "	        connection.send(currentIndex.toString()+' processed now');\n"
            + "		console.log('vay vay');\n"
            + "		loadImage('./images/close/0.jpg', imageReady);\n"
            + "	     }\n"
            + "	  } else {\n"
            + "		//savePredictions();\n"
            + "	  }\n"
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
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FactoryUtils.server.broadcast(str+" send backed");
        String s=files[currentIndex++].getName();
        System.out.println("clienta giden mesaj: = " + s);
        FactoryUtils.server.broadcast(s);
        t1=FactoryUtils.toc(t1);
    }

}
