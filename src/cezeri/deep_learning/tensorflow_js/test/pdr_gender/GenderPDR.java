/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.test.pdr_gender;

import cezeri.deep_learning.tensorflow_js.test.pistachio.*;
import cezeri.enums.EnumEngine;
import cezeri.enums.EnumOperatingSystem;
import cezeri.enums.EnumRunTime;
import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import cezeri.websocket.SocketServer;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cezerilab
 */
public class GenderPDR {
    //tensorflow launches a new library to train similarity models
    
    private int hit;
    private int err;
    private double acc;
    int channel = 0;
    long t1 = FactoryUtils.tic();
    private static String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pdr_gender/images/test";
    private static File[] files = FactoryUtils.getFileListDataSetForImageClassification(imgPath);

    public static void main(String[] args) {
        files = FactoryUtils.shuffle(files, new Random(123));
        System.out.println("files size:" + files.length);
        int m = 1;
        int n = files.length / m;
        for (int i = 0; i < m; i++) {
            newThread(i, 8080 + i, 8887 + i, 0, files.length - 1);
        }
    }

    private static void newThread(int ch, int http_server_port, int web_socket_port, int from, int to) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GenderPDR().generateInference(ch, 8080 + ch, 8887 + ch, 0, files.length - 1);
            }
        }).start();
    }
    
    private void generateInference(int ch, int http_server_port, int web_socket_port, int from, int to) {
        channel = ch;
        CMatrix cm = CMatrix.getInstance()
                .setModelForInferenceTensorFlowJS(
                        EnumRunTime.RUN, 
                        EnumOperatingSystem.WINDOWS, 
                        EnumEngine.TENSORFLOW_JS, 
                        "models/pdr_gender", 
                        this::invokeMethod, 
                        http_server_port, 
                        web_socket_port);
        SocketServer server = cm.TENSORFLOW_JS_SERVER;

        String s = "";
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestInferenceAsClassicWay.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = from; i < to; i++) {
            s = files[i].getAbsolutePath();
            s = "./" + s.replace(new File(new File(FactoryUtils.currDir), "/models/pdr_gender/").getAbsolutePath(), "");
            System.out.println("tensorflowjs'ye giden mesaj: = " + s);
            server.broadcast(s);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestInferenceAsClassicWay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void invokeMethod(String msg) {
        long t = System.currentTimeMillis() - t1;
        System.out.println(channel + ". kanaldaki gelen mesaj = " + msg + " elapsed time:" + t);
        if (msg.split(":").length > 1) {
            String path = msg.split(":")[0];
            String label = msg.split(":")[1];
            path=FactoryUtils.getItemPath(path,-2);
            if (path.equals(label)) {
                hit++;
            } else {
                err++;
            }
            acc = FactoryUtils.formatDouble(1.0 * hit / (hit + err) * 100);
            System.out.println("acc:" + acc + "% hit:" + hit + " err:" + err + " gelen mesaj:" + msg);

        }
        t1 = System.currentTimeMillis();
    }
}
