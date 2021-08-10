/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_rest.core;

import cezeri.deep_learning_rest.enums.EnumBackEnd;
import cezeri.deep_learning_rest.enums.EnumDataSource;
import cezeri.deep_learning_rest.enums.EnumLearningMode;
import cezeri.deep_learning_rest.interfaces.Configuration;
import cezeri.deep_learning_rest.interfaces.InterfaceCallBack;
import cezeri.deep_learning_rest.utils.Settings;
import cezeri.utils.SerialType;
import cezeri.factory.FactoryUtils;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author BAP1
 */
public class FactoryStrategies {

    private static long t = FactoryUtils.tic();
    //private static FrameExecution frm = new FrameExecution();
    public static int delay_time = 0;

//    public static void execute() {
//        frm.setVisible(true);
//        if (Settings.learning_type == enums.EnumLearningType.CLASSIFICATION) {
//            if (Settings.learning_mode == enums.EnumLearningMode.TEST) {
//                if (Settings.development_platform == enums.EnumDevelopmentPlatform.PYTHON) {
//                    FactoryUtils.browseKerasModel(FactoryUtils.getDefaultDirectory() + "\\models\\python", "Select Keras h5 Model");
//                    if (Settings.back_end == enums.EnumBackEnd.CPU) {
//                        if (Settings.data_source == enums.EnumDataSource.CAMERA) {
//                            executePythonScript(null,null,frm.txt_log, "webcam", false, 0);
//                        } else if (Settings.data_source == enums.EnumDataSource.IMAGE_FOLDER) {
//                            executePythonScript(null,null,frm.txt_log, "folder", false, 0);
//                        } else if (Settings.data_source == enums.EnumDataSource.IMAGE_FILE) {
//                            executePythonScript(null,null,frm.txt_log, "file", false, 0);
//                        }
//                    } else if (Settings.back_end == enums.EnumBackEnd.GPU) {
//
//                    }
//                }
//            }
//        }
//    }
    
    private static String[] classLabels=null;
    
    public static void executePythonScript(SerialType st, JTextField txt_pins, JTextArea log, String mode, boolean isRemoteConnected, int dt) {
        delay_time = dt;
        JDL_Python jdlp = new JDL_Python();
        String[] pins=txt_pins.getText().split(",");        
        Configuration config = new ConfigurationPythonScript();
        if (mode.equals("folder")) {
            config.setDataSource(EnumDataSource.IMAGE_FOLDER);
            File dir = FactoryUtils.browseDirectory(FactoryUtils.getDefaultDirectory());
            if (dir != null) {
                Settings.testFolderPath = dir.getAbsolutePath();
                config.setTestFolderPath(Settings.testFolderPath);
                classLabels=readClassLabels(Settings.modelPath);
            }
        } else if (mode.equals("webcam")) {
            config.setDataSource(EnumDataSource.CAMERA);
        } else if (mode.equals("file")) {
            config.setDataSource(EnumDataSource.IMAGE_FILE);
        }
        
        config = config.setBackEnd(EnumBackEnd.CPU)
                .setLearningMode(EnumLearningMode.TEST)
                .setModelPath(Settings.modelPath)
                .setClassLabels(readClassLabels(Settings.modelPath))
                .setCallBackFunction(new InterfaceCallBack() {
                    long t1 = System.currentTimeMillis();

                    @Override
                    public void onMessageReceived(String str) {
//                        if (System.currentTimeMillis()-t1>delay_time) {
                        invokeMethod(str);
                        t1 = System.currentTimeMillis();
                        
//                        }
                    }

                    private void invokeMethod(String str) {
                        if (mode.equals("file")) {

                        }
                        if (st != null) {
                            for (int i = 0; i < classLabels.length; i++) {
                                if (classLabels[i].equals(str)) {
                                    FactoryUtils.sendDataToSerialPort(st, pins[i].substring(1));
                                }                                
                            }
                            if (str.equals("stop")) {
                                FactoryUtils.sendDataToSerialPort(st, "0");
                            }
                        }
                        log.append(str + " elapsed:" + FactoryUtils.formatDouble((System.nanoTime() - t) / (1000000.0d)) + " ms \n");
                        t = System.nanoTime();
                    }
                });
        jdlp.setConfiguration(config);
        jdlp.build();
        //websocket aşağıdaki icbf üzerinden haberleşebiliyor o nedenle aşağıdaki komut çok önemlidir.
        FactoryUtils.icbf = config.getCall_back();
        jdlp.execute(8080);
    }

    private static String[] readClassLabels(String modelPath) {
        modelPath = modelPath.substring(0, modelPath.lastIndexOf("\\"));
        String str = FactoryUtils.readFile(modelPath + "\\labels.txt");
        String[] s = str.split("\n");
        String[] ret = new String[s.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = s[i].split(" ")[1];
        }
        return ret;
    }
}
