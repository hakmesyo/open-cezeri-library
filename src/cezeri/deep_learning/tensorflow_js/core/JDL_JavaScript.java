/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.core;

import cezeri.deep_learning.tensorflow_js.enums.EnumDataSource;
import cezeri.deep_learning.tensorflow_js.enums.EnumLearningMode;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceConfiguration;
import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceDeepLearning;
import cezeri.factory.FactoryUtils;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL LAB
 */
public class JDL_JavaScript implements InterfaceDeepLearning {

    private final static String strJSWebCam_previous
            = "<link rel=\"shortcut icon\" href=\"#\" />\n"
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
            + "<html>\n"
            + "<body >\n"
            + "<div id=\"prediction-container\"></div>\n"
            + "<div id=\"tlapse\"></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>\n"
            + "";
    private final static String strJSWebCam
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
    private final static String strML5ImageClassification=
            "<html>\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>Multiple Image classification using MobileNet and p5.js</title>\n" +
            "\n" +
            "  <!--<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/p5.min.js\"></script>-->\n" +
            "  <!--<script src=\"https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.9.0/addons/p5.dom.min.js\"></script>-->\n" +
            "  <!--<script src=\"https://unpkg.com/ml5@latest/dist/ml5.min.js\"></script>-->\n" +
            "  <script src=\"http://localhost:8080/models/js/classify_pistachio/p5.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"http://localhost:8080/models/js/classify_pistachio/p5.dom.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"http://localhost:8080/models/js/classify_pistachio/ml5.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script type=\"text/javascript\">\n"+
            "	var connection = new WebSocket('ws://127.0.0.1:8887');\n"+
            "	connection.onopen = function () {\n"+
            "	  connection.send('Ping message form tensorflow.js back end');\n"+
            "	};    \n"+
            "  </script>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "  <h1>Multiple Image classification using MobileNet and p5.js</h1>\n" +
            "  <h2>The results will be saved to a JSON file when finished</h2>\n" +
            "  <script src=\"sketch.js\"></script>\n" +
            "  <div id='comment'> <p> </p> </div>\n"+
            "</body>\n" +
            "\n" +
            "</html>"
            ;
    
    private final static String strTensorRegression = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "\n"
            + "<head>\n"
            + "    <meta charset=\"utf-8\">\n"
            + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
            + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "    <title>TensorFlow.js Tutorial</title>\n"
            + "\n"
            + "    <!-- Import TensorFlow.js -->\n"
            + "    <script src=\"https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@3.0.0/dist/tf.min.js\"></script>\n"
            + "    <script src=\"https://cdn.plot.ly/plotly-latest.min.js\"></script>\n"
            + "    <p> Plot of 'lattitude' feature vs row index<p>\n"
            + "    <div id=\"dataSpaceWith4Lines\" class=\"plots\"></div>\n"
            + "    \n"
            + "    <script src=\"myscript.js\"> </script>\n"
            + "</head>\n"
            + "\n"
            + "<body>\n"
            + "</body>\n"
            + "\n"
            + "</html>";

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
            if (config.getDataSource() == EnumDataSource.CAMERA) {
                str = strJSWebCam;
            } else if (config.getDataSource() == EnumDataSource.IMAGE_FILE) {
                str=strML5ImageClassification;
                /*
                str = strJSImageFile;
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
            } else if (config.getDataSource() == EnumDataSource.SCALAR) {
                str=strTensorRegression;
            }
        }
        String folder = config.getModelPath();
        scriptFilePath = folder + "\\index.html";
        FactoryUtils.writeToFile(scriptFilePath, str);
//        FactoryUtils.executeCmdCommand("dir",true);
//        FactoryUtils.executeCommand("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "localhost:8080", true);
    }

    @Override
    public void execute(int port) {
        try {
            Runtime.getRuntime().exec("cmd /c http-server -p "+port);
        } catch (IOException ex) {
            Logger.getLogger(JDL_JavaScript.class.getName()).log(Level.SEVERE, null, ex);
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
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:"+port+"/models/js/" + folder});
                } catch (IOException ex) {
                    Logger.getLogger(JDL_JavaScript.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    private String getClassNames(String[] classLabels) {
        String ek = "class_names = [";
        for (int i = 0; i < classLabels.length; i++) {
            ek += "'" + classLabels[i] + "',";
        }
        ek = ek.substring(0, ek.length() - 1);
        ek = ek + "]\n";
        return ek;
    }

}
