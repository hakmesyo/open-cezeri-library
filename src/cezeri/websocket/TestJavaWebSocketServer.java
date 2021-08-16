package cezeri.websocket;

import cezeri.deep_learning.tensorflow_js.interfaces.InterfaceCallBack;
import cezeri.factory.FactoryUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DELL LAB
 */
public class TestJavaWebSocketServer {

    private static long t1 = FactoryUtils.tic();

    private static void messageReceived(String str) {
        System.out.println("clienttan gelen str = " + str);
//        System.out.println("str = " + str);
//        if (str.equals("ping start")) {
//            FactoryUtils.server.broadcast("send me");
//            //FactoryUtils.server.broadcast("stop");
//        } else {
//            System.out.println(str);
//            FactoryUtils.server.broadcast(str+" send backed");
//            t1 = FactoryUtils.toc(t1);
//        }

        
    }

    public static void main(String[] args) {
        FactoryUtils.startJavaServer();
        FactoryUtils.icbf=new InterfaceCallBack() {
            @Override
            public void onMessageReceived(String str) {
                messageReceived(str);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestJavaWebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                FactoryUtils.server.broadcast("broadcast mesaj serverdan gitti");
            }
        };
    }
}
