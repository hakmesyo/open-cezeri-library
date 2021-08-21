/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.test.pistachio;

import cezeri.interfaces.InterfaceCallBack;
import cezeri.enums.EnumEngine;
import cezeri.enums.EnumOperatingSystem;
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
public class CMatrixApproach {
    int channel=0;
    long t1=FactoryUtils.tic();
    private static String imgPath = FactoryUtils.getDefaultDirectory() + "/models/pistachio_rest/images/test";
    private static File[] files=FactoryUtils.getFileListDataSetForImageClassification(imgPath);;
    
    public static void main(String[] args) {
        files = FactoryUtils.shuffle(files, new Random(123));
//        files = FactoryUtils.shuffle(files, new Random());
        System.out.println("files size:" + files.length);
        int n=files.length/5;
        for (int i = 0; i < 5; i++) {
            new CMatrixApproach().generateInference((i+1),8080+i,8887+i,i*n,i*n+n);
        }
        
//        new CMatrixApproach().generateInference(1,8080,8887,0,files.length-1);
//        new CMatrixApproach().generateInference(2,8081,8888,0,files.length-1);
//        new CMatrixApproach().generateInference(3,8082,8889,0,files.length-1);
//        new CMatrixApproach().generateInference(4,8083,8890,0,files.length-1);
//        new CMatrixApproach().generateInference(5,8084,8891,0,files.length-1);
    }
    
    //01:20:84

    private void generateInference(int ch,int http_server_port,int web_socket_port,int from,int to) {
        channel=ch;
        CMatrix cm = CMatrix.getInstance()
                .setModelForInference(EnumOperatingSystem.WINDOWS, EnumEngine.TENSORFLOW_JS, "models/pistachio_rest", this::invokeMethod, http_server_port, web_socket_port);
        SocketServer server = cm.TENSORFLOW_JS_SERVER;


        String s = "";
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = from; i < to; i++) {
            s = files[i].getAbsolutePath();
            s = "./" + s.replace(FactoryUtils.currDir + "\\models\\pistachio_rest\\", "");
            System.out.println("tensorflowjs'ye giden mesaj: = " + s);
            server.broadcast(s);
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(InferPistachio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void invokeMethod(String msg) {
        long t=System.currentTimeMillis()-t1;
        System.out.println(channel+". kanaldaki gelen mesaj = " + msg+" elapsed time:"+t);
        t1=System.currentTimeMillis();
    }
}
