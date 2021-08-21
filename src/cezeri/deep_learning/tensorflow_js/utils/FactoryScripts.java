/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.utils;

import cezeri.enums.EnumBackEnd;
import cezeri.enums.EnumDataSource;
import cezeri.enums.EnumDevelopmentPlatform;
import cezeri.factory.FactoryUtils;
import java.io.File;

/**
 *
 * @author BAP1
 */
public class FactoryScripts {
    
    private final static String jsHeader
="<link rel=\"shortcut icon\" href=\"#\" />\n" +
"<div>Teachable Machine Image Model</div>\n" +
"<button type=\"button\" onclick=\"init()\">Start</button>\n" +
"<div id=\"webcam-container\"></div>\n" +
"<div id=\"label-container\"></div>\n" +
"<script src=\"https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@1.3.1/dist/tf.min.js\"></script>\n" +
"<script src=\"https://cdn.jsdelivr.net/npm/@teachablemachine/image@0.8/dist/teachablemachine-image.min.js\"></script>\n" +
"<script type=\"text/javascript\">\n" +
"	navigator.webkitPersistentStorage.requestQuota(1024*1024, function() {\n" +
"	  window.webkitRequestFileSystem(window.PERSISTENT , 1024*1024, SaveDatFileBro);\n" +
"	})   \n" +
"	\n" +
"	function SaveDatFileBro(localstorage) {\n" +
"	  localstorage.root.getFile(\"info.txt\", {create: true}, function(DatFile) {\n" +
"		DatFile.createWriter(function(DatContent) {\n" +
"		  var blob = new Blob([\"Lorem Ipsum\"], {type: \"text/plain\"});\n" +
"		  DatContent.write(blob);\n" +
"		});\n" +
"	  });\n" +
"	}	\n" +
"	// More API functions here:\n" +
"    // https://github.com/googlecreativelab/teachablemachine-community/tree/master/libraries/image\n" +
"\n" +
"    // the link to your model provided by Teachable Machine export panel\n" +
"    //const URL = \"./my_model/\";\n" +
"    const URL = \"my_model/\";\n" +
"	\n" +
"	\n" +
"    let model, webcam, labelContainer, maxPredictions;\n" +
"\n" +
"	var connection = new WebSocket('ws://127.0.0.1:8887');\n" +
"	connection.onopen = function () {\n" +
"	  connection.send('Ping');\n" +
"	};    \n" +
"	// Load the image model and setup the webcam\n" +
"    async function init() {\n" +
"		\n" +
"        const modelURL = URL + \"model.json\";\n" +
"        const metadataURL = URL + \"metadata.json\";\n" +
"\n" +
"        // load the model and metadata\n" +
"        // Refer to tmImage.loadFromFiles() in the API to support files from a file picker\n" +
"        // or files from your local hard drive\n" +
"        // Note: the pose library adds \"tmImage\" object to your window (window.tmImage)\n" +
"        model = await tmImage.load(modelURL, metadataURL);\n" +
"        maxPredictions = model.getTotalClasses();\n" +
"\n" +
"        // Convenience function to setup a webcam\n" +
"        const flip = true; // whether to flip the webcam\n" +
"        webcam = new tmImage.Webcam(400, 400, flip); // width, height, flip\n" +
"        await webcam.setup(); // request access to the webcam\n" +
"        await webcam.play();\n" +
"        window.requestAnimationFrame(loop);\n" +
"\n" +
"        // append elements to the DOM\n" +
"        document.getElementById(\"webcam-container\").appendChild(webcam.canvas);\n" +
"        labelContainer = document.getElementById(\"label-container\");\n" +
"        for (let i = 0; i < maxPredictions; i++) { // and class labels\n" +
"            labelContainer.appendChild(document.createElement(\"div\"));\n" +
"        }\n" +
"    }\n" +
"\n" +
"    async function loop() {\n" +
"        webcam.update(); // update the webcam frame\n" +
"        await predict();\n" +
"        window.requestAnimationFrame(loop);\n" +
"    }\n" +
"\n" +
"	\n" +
"    // run the webcam image through the image model\n" +
"    async function predict() {\n" +
"		startTime = new Date();\n" +
"        // predict can take in an image, video or canvas html element\n" +
"        const prediction = await model.predict(webcam.canvas);\n" +
"		var max=0;\n" +
"		var index=0;\n" +
"        for (let i = 0; i < maxPredictions; i++) {\n" +
"            const classPrediction =\n" +
"                prediction[i].className + \": \" + prediction[i].probability.toFixed(2);\n" +
"            labelContainer.childNodes[i].innerHTML = classPrediction;\n" +
"			if (max<prediction[i].probability.toFixed(2)){\n" +
"				max=prediction[i].probability.toFixed(2);\n" +
"				index=i;\n" +
"			}\n" +
"        }\n" +
"		//let data = prediction[index].className + \":\" + prediction[index].probability.toFixed(2);\n" +
"		let data = prediction[index].className;\n" +
"		document.getElementById(\"prediction-container\").innerHTML=\"Predicted Class is:\"+data;\n" +
"		endTime = new Date();\n" +
"		var timeDiff = endTime - startTime; //in ms\n" +
"		document.getElementById(\"tlapse\").innerHTML=\"elapsed time:\"+timeDiff;\n" +
"		connection.send('Predicted Class is:'+data);\n" +
"    }\n" +
"</script>\n" +
"<html>\n" +
"<body >\n" +
"<div id=\"prediction-container\"></div>\n" +
"<div id=\"tlapse\"></div>\n" +
"</div>\n" +
"</body>\n" +
"</html>\n" +
"";

    private final static String strPythonLibrary
            = "#Temporary Script generated by Jazari Deep Learning Framework\n"
            + "import tensorflow.keras\n"
            + "from PIL import Image\n"
            + "import numpy as np\n"
            + "import os\n"
            + "import matplotlib.pyplot as plt\n"
            + "from keras.optimizers import Adam,Adadelta\n"
            + "import timeit\n"
            + "import time\n"
            + "import websocket\n"
            + "import cv2\n"
            + "from websocket_server import WebsocketServer\n"
            + "os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\n"
            + "ws=websocket.WebSocket();\n"
            + "ws.connect('ws://127.0.0.1:8887')\n";
    private final static String strPythonWebCam=""
                            + "def open_camera():\n"
                            + "    global nTotal;\n"
                            + "    global nOpenSuccess;\n"
                            + "    global nClosedSuccess;\n"
                            + "    global acc;\n"
                            + "    x='open'\n"
                            + "    capture=cv2.VideoCapture(0)\n"
                            + "    while(True):\n"
                            + "        start = timeit.default_timer()\n"
                            + "        ret, frame = capture.read()\n"
                            + "        cv2.imshow('video', frame)\n"
                            + "        image = np.resize(frame,(224, 224,3))\n"
                            + "        image_array = np.asarray(image)\n"
                            + "        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "        # Load the image into the array\n"
                            + "        data[0] = normalized_image_array\n"
                            + "        # run the inference\n"
                            + "        prediction = model.predict(data)\n"
                            + "        ws.send(''+class_names[np.argmax(prediction)])\n"
                            + "        stop = timeit.default_timer()\n"
                            + "        if cv2.waitKey(2) == 27:\n"
                            + "            ws.send('stop')\n"
                            + "            break\n"
                            + "    capture.release()\n"
                            + "    cv2.destroyAllWindows()\n"
                            + "open_camera()\n";
    
    private static String strPythonOfflineTestFileImages=""
                            + "nTotal=0;\n"
//                          + "nOpenSuccess=0;\n"
//                          + "nClosedSuccess=0;\n"
                            + "index=-1\n"
                            + "acc=0;\n"
                            + "for x in class_names:\n"
                            + "    index=index+1\n"
                            + "    path=os.path.join(test_path,x)\n"
                            + "    for imge in os.listdir(path):\n"
                            + "        image = Image.open(os.path.join(path,imge))\n"
                            + "        # Make sure to resize all images to 224, 224 otherwise they won't fit in the array\n"
                            + "        image = image.resize((224, 224))\n"
                            + "        image_array = np.asarray(image)\n"
                            + "        # Normalize the image\n"
                            + "        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "#        normalized_image_array = (image_array.astype(np.float32) / 255.0)\n"
                            + "        # Load the image into the array\n"
                            + "        data[0] = normalized_image_array \n"
                            + "        # run the inference\n"
                            + "        start = timeit.default_timer()\n"
                            + "        prediction = model.predict(data)\n"
                            + "        stop = timeit.default_timer()\n"
                            + "        nTotal+=1\n"
                            + "        if x==class_names[index] and np.argmax(prediction)==index:\n"
                            + "          hist_hit[index]=hist_hit[index]+1\n"
//                            + "        if x=='closed' and np.argmax(prediction)==0:\n"
                            + "        else:\n"
                            + "          hist_error[index]=hist_error[index]+1\n"
//                            + "        acc=(nOpenSuccess+nClosedSuccess)*1.0/nTotal\n"
                            + "#        print('acc',acc,' class:',class_names[np.argmax(prediction)],' elapsed time:', (stop-start))\n"
                            + "        ws.send(''+class_names[np.argmax(prediction)])\n"
                            + "acc=sum(hist_hit)*1.0/(sum(hist_hit)+sum(hist_error))\n"
                            + "ws.send('offline batch prediction accuracy:'+str(acc))\n"
                            + "ws.send('stop')\n"
                        ;

    public static String generateScriptFilePythonWebCamTestTransferLearning(EnumDevelopmentPlatform programming_language, EnumBackEnd back_end, EnumDataSource data_source, String model_path, String[] classLabels) {
        String str = "";
        String ek ="class_names = [";
        for (int i = 0; i < classLabels.length; i++) {
            ek+="'"+classLabels[i]+"',";
        }
        ek=ek.substring(0,ek.length()-1);
        ek=ek+"]\n";
        if (back_end == EnumBackEnd.CPU) {
            if (data_source == EnumDataSource.CAMERA) {
                if (programming_language == EnumDevelopmentPlatform.PYTHON) {
                    str = strPythonLibrary
                            + ek+
                            "# Load the model\n"
                            //+ "model = tensorflow.keras.models.load_model(r'C:\\python_data\\models\\keras_model_pistachio.h5')\n"
                            + "model = tensorflow.keras.models.load_model(r'"+model_path+"')\n"
                            + "data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)\n"
                            +strPythonWebCam;
                    
                    FactoryUtils.writeToFile(FactoryUtils.currDir + "\\tmp.py", str);
                    return FactoryUtils.currDir + "\\tmp.py";
                }
            }
        }
        return null;
    }
    
    public static String generateScriptFileJavaScriptWebCamTestTransferLearning(EnumDevelopmentPlatform programming_language, EnumBackEnd back_end, EnumDataSource data_source, String model_path) {
        if (back_end == EnumBackEnd.CPU) {
            if (data_source == EnumDataSource.CAMERA) {
                if (programming_language == EnumDevelopmentPlatform.JAVA_SCRIPT) {
                    FactoryUtils.writeToFile(model_path+"\\index.html", jsHeader);
                    return model_path+"\\index.html";
                }
            }
        }
        return null;
    }
    
    public static String generateScriptFilePythonOfflineFileTestTransferLearning(EnumDevelopmentPlatform programming_language, EnumBackEnd back_end, EnumDataSource data_source,String test_path, String model_path) {
        File[] dirs=FactoryUtils.getDirectories(test_path);        
        String str = "";
        String ek ="class_names = [";
        for (int i = 0; i < dirs.length; i++) {
            ek+="'"+dirs[i].getName()+"',";
        }
        ek=ek.substring(0,ek.length()-1);
        ek=ek+"]\n";
        String testPath=FactoryUtils.currDir+"\\"+test_path;
        if (back_end == EnumBackEnd.CPU) {
            if (data_source == EnumDataSource.IMAGE_FILE) {
                if (programming_language == EnumDevelopmentPlatform.PYTHON) {
                    str = strPythonLibrary
                            + ek
                            + "test_path=r'"+testPath+"'\n"
                            + "# Load the model\n"
                            //+ "model = tensorflow.keras.models.load_model(r'C:\\python_data\\models\\keras_model_pistachio.h5')\n"
                            + "model = tensorflow.keras.models.load_model(r'"+model_path+"')\n"
                            + "data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)\n"
                            + "hist_hit=np.zeros(len(class_names))\n"
                            + "hist_error=np.zeros(len(class_names))\n"
                            +strPythonOfflineTestFileImages;
                    
                    FactoryUtils.writeToFile(FactoryUtils.currDir + "\\tmp.py", str);
                    return FactoryUtils.currDir + "\\tmp.py";
                }
            }
        }
        return null;
    }
    
    public static String generateScriptFilePythonOnlineFileTestTransferLearning(EnumDevelopmentPlatform programming_language, EnumBackEnd back_end, EnumDataSource data_source,String model_path) {
        String str="";
        if (back_end == EnumBackEnd.CPU) {
            if (data_source == EnumDataSource.IMAGE_FILE) {
                if (programming_language == EnumDevelopmentPlatform.PYTHON) {
                    str = strPythonLibrary
                            + "# Load the model\n"
                            + "model = tensorflow.keras.models.load_model(r'"+model_path+"')\n"
                            + "ws.send('python client is ready for constructing python server')\n"
                            + "def predictSingleImage(client,server,path):\n"
                            + "     data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)\n"
                            + "     image = Image.open(path)\n"
                            + "     # Make sure to resize all images to 224, 224 otherwise they won't fit in the array\n"
                            + "     image = image.resize((224, 224))\n"
                            + "     image_array = np.asarray(image)\n"
                            + "     # Normalize the image\n"
                            + "     normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "     #normalized_image_array = (image_array.astype(np.float32) / 255.0)\n"
                            + "     # Load the image into the array\n"
                            + "     data[0] = normalized_image_array \n"
                            + "     # run the inference\n"
                            + "     start = timeit.default_timer()\n"
                            + "     prediction = model.predict(data)\n"
                            + "     stop = timeit.default_timer()\n"
                            + "     server.send_message(client,'predicted class index:'+str(np.argmax(prediction)))\n"
                            + "#     ws.send('predicted class index:'+str(np.argmax(prediction)))\n"
                            + "#     ws.send(np.argmax(prediction))\n"
                            + "# Called for every client connecting (after handshake)\n"
                            + "def new_client(client, server):\n"
                            + "        print('New client connected and was given id %d' % client['id'])\n"
                            + "        server.send_message_to_all('Hey all, a new client has joined us')\n"
                            + "#        ws.send('22222Hey all, a new client has joined us')\n"
                            + "# Called for every client disconnecting\n"
                            + "def client_left(client, server):\n"
                            + "        print('Client(%d) disconnected' % client['id'])\n"
                            + "# Called when a client sends a message\n"
                            + "def message_received(client, server, message):\n"
                            + "#        server.send_message(client,'python server said java client sent me '+message)\n"
                            + "        if message=='stop':\n"
                            + "            server.shutdown()\n"
                            + "            ws.send('stop')\n"
                            + "        predictSingleImage(client,server,message)\n"
                            + "PORT=8888\n"
                            + "server = WebsocketServer(PORT)\n"
                            + "server.set_fn_new_client(new_client)\n"
                            + "server.set_fn_client_left(client_left)\n"
                            + "server.set_fn_message_received(message_received)\n"
                            + "server.run_forever()\n"
                            
                          ;
                    
                    FactoryUtils.writeToFile(FactoryUtils.currDir + "\\tmp.py", str);
                    return FactoryUtils.currDir + "\\tmp.py";
                }
            }
        }
        return null;
    }

}
/*
                            "# Load the model\n"
                            + "#model = tensorflow.keras.models.load_model('tm_keras_model_big_ds.h5')\n"
                            + "#model = tensorflow.keras.models.load_model('keras_model_epoch200.h5')\n"
                            + "#model = tensorflow.keras.models.load_model('data_augment_model.h5')\n"
                            + "#model = tensorflow.keras.models.load_model('vgg16_model_big_ds.h5')\n"
                            + "#model = tensorflow.keras.models.load_model('vgg16_1000_ds.h5')\n"
                            + "#model = tensorflow.keras.models.load_model('cnn.h5')\n"
                            + "model = tensorflow.keras.models.load_model(r'C:\\python_data\\models\\keras_model_pistachio.h5')\n"
                            + "#model = tensorflow.keras.models.load_model(r'C:\\python_data\\models\\cuneyt_mobilenet.h5')\n"
                            + "#model.summary()\n"
                            + "# Create the array of the right shape to feed into the keras model\n"
                            + "# The 'length' or number of images you can put into the array is\n"
                            + "# determined by the first position in the shape tuple, in this case 1.\n"
                            + "data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)\n"
                            + "# Replace this with the path to your image\n"
                            + "nOpenSuccess=0\n"
                            + "nClosedSuccess=0\n"
                            + "nTotal=0;\n"
                            + "acc=0.0\n"
                            + "#path=r'C:\\python_data\\dataset\\pistachio\\snapshots'\n"
                            + "#for imge in os.listdir(path):\n"
                            + "#        image = Image.open(os.path.join(path,imge))\n"
                            + "#        # Make sure to resize all images to 224, 224 otherwise they won't fit in the array\n"
                            + "#        image = image.resize((224, 224))\n"
                            + "#        plt.imshow(image)\n"
                            + "#        plt.show()\n"
                            + "#        image_array = np.asarray(image)\n"
                            + "#        # Normalize the image\n"
                            + "#        normalized_image_array = (image_array.astype(np.float32) / 255.0)\n"
                            + "##        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "#        # Load the image into the array\n"
                            + "#        data[0] = normalized_image_array\n"
                            + "#        # run the inference\n"
                            + "#        prediction = model.predict(data)\n"
                            + "#        nTotal+=1\n"
                            + "#        print('acc',acc,' class:',class_names[np.argmax(prediction)])\n"
                            + "#for x in class_names:\n"
                            + "#    path=os.path.join(r'C:\\python_data\\dataset\\pistachio\\teachable_machine\\test',x)\n"
                            + "#    for imge in os.listdir(path):\n"
                            + "#        image = Image.open(os.path.join(path,imge))\n"
                            + "#        # Make sure to resize all images to 224, 224 otherwise they won't fit in the array\n"
                            + "#        image = image.resize((224, 224))\n"
                            + "##        plt.imshow(image)\n"
                            + "##        plt.show()\n"
                            + "#        image_array = np.asarray(image)\n"
                            + "#        # Normalize the image\n"
                            + "#        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "##        normalized_image_array = (image_array.astype(np.float32) / 255.0)\n"
                            + "#        # Load the image into the array\n"
                            + "#        data[0] = normalized_image_array \n"
                            + "#        # run the inference\n"
                            + "#        start = timeit.default_timer()\n"
                            + "#        prediction = model.predict(data)\n"
                            + "#        stop = timeit.default_timer()\n"
                            + "#        nTotal+=1\n"
                            + "#        if x=='open' and np.argmax(prediction)==1:\n"
                            + "#            nOpenSuccess+=1\n"
                            + "#        if x=='closed' and np.argmax(prediction)==0:\n"
                            + "#            nClosedSuccess+=1\n"
                            + "#        acc=(nOpenSuccess+nClosedSuccess)*1.0/nTotal\n"
                            + "#        print('acc',acc,' class:',class_names[np.argmax(prediction)],' elapsed time:', (stop-start))\n"
                            + "#        ws.send('class:'+class_names[np.argmax(prediction)])\n"
                            + "def open_camera():\n"
                            + "    global nTotal;\n"
                            + "    global nOpenSuccess;\n"
                            + "    global nClosedSuccess;\n"
                            + "    global acc;\n"
                            + "    x='open'\n"
                            + "    capture=cv2.VideoCapture(0)\n"
                            + "    while(True):\n"
                            + "        start = timeit.default_timer()\n"
                            + "        ret, frame = capture.read()\n"
                            + "        cv2.imshow('video', frame)\n"
                            + "        image = np.resize(frame,(224, 224,3))\n"
                            + "        image_array = np.asarray(image)\n"
                            + "        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1\n"
                            + "        # Load the image into the array\n"
                            + "        data[0] = normalized_image_array\n"
                            + "        # run the inference\n"
                            + "        prediction = model.predict(data)\n"
                            + "        nTotal+=1\n"
                            + "        if x=='open' and np.argmax(prediction)==1:\n"
                            + "            nOpenSuccess+=1\n"
                            + "        if x=='closed' and np.argmax(prediction)==0:\n"
                            + "            nClosedSuccess+=1\n"
                            + "        acc=(nOpenSuccess+nClosedSuccess)*1.0/nTotal\n"
                            + "        ws.send('class:'+class_names[np.argmax(prediction)])\n"
                            + "        stop = timeit.default_timer()\n"
                            + "        #print('acc',acc,' class:',class_names[np.argmax(prediction)],' elapsed time:', (stop-start)*1000)\n"
                            + "        if cv2.waitKey(2) == 27:\n"
                            + "            break\n"
                            + "    capture.release()\n"
                            + "    cv2.destroyAllWindows()\n"
                            + "open_camera()\n"
                            + "print('overall accuracy:',acc)\n"
                            + "print('hit open :',nOpenSuccess)\n"
                            + "print('hit closed :',nClosedSuccess)\n"
                            + "print('#samples:',nTotal)";
*/