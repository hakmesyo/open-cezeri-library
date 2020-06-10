/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import static cezeri.factory.FactorySocket.running;
import cezeri.websocket.InterfaceCallBack;
import cezeri.websocket.SocketServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 *
 * @author elcezerilab
 */
public class FactorySocket {

    public static boolean stopServer = false;
    public static boolean isConnectPythonServer = false;
    public static SocketServer server;
    public static WebSocketClient client;
    public static String currDir = System.getProperty("user.dir");
    public static final AtomicBoolean running = new AtomicBoolean(false);
    public static int nAttempts = 0;

    public static void startJavaWebSocketServer(InterfaceCallBack icb) {
        startJavaWebSocketServer("localhost", 8887,icb);
    }

    public static void startJavaWebSocketServer(String ip, int port,InterfaceCallBack icb) {
        new Thread(() -> {
            try {
                server = new SocketServer(ip, port,icb);
            } catch (UnknownHostException ex) {
                Logger.getLogger(FactorySocket.class.getName()).log(Level.SEVERE, null, ex);
            }
            server.start();
            System.out.println("Java WebSocket Server started on port: " + server.getPort());
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            running.set(true);
            while (running.get()) {
                try {
                    if (sysin.ready()) {
                        String in;
                        try {
                            in = sysin.readLine();
//                                s.broadcast(in);
                            if (in.equals("exit")) {
                                server.stop(1000);
                                System.out.println("Java Server is stopping");
                                break;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Thread.sleep(1);
                } catch (IOException ex) {
                    Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Java Server was stopped");
            try {
                server.stop();
            } catch (IOException ex) {
                Logger.getLogger(FactorySocket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FactorySocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    public static void main(String[] args) {
        startJavaWebSocketServer("192.168.1.33",1111,new InterfaceCallBack() {
            @Override
            public void onMessageReceived(String str) {
                System.out.println(str);
            }
        });
    }

}
