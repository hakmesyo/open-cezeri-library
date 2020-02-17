/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning;

import cezeri.websocket.ChatServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author BAP1
 */
public class CDL_Utils {

    public static boolean stopServer = false;
    public static boolean isConnectPythonServer = false;
    public static ChatServer s;
    public static WebSocketClient client;

    public static void startJavaServer() {
        new Thread(() -> {
            try {
                int port = 8887;
                s = new ChatServer(port);
                s.start();
                System.out.println("Java WebSocket Server started on port: " + s.getPort());
                BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        if (!sysin.ready()) {
                            if (stopServer) {
                                s.stop();
                                System.out.println("Java Server is stopping");
                                break;
                            }
                        } else {

                            String in;
                            try {
                                in = sysin.readLine();
                                s.broadcast(in);
                                if (in.equals("exit")) {
                                    try {
                                        s.stop(1000);
                                        System.out.println("Java Server is stopping");
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    break;
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (stopServer) {
                                System.out.println("Java WebSocket Server was terminated successfully");
                                break;
                            }
                        }
                        Thread.sleep(1);
                    } catch (IOException ex) {
                        Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public static void stopWebsocketServer() {
        System.out.println("stop server command stop server flag is true");
        stopServer = true;
    }

    public static boolean connectPythonServer() {
        isConnectPythonServer = true;
        try {
            client = new WebSocketClient(new URI("ws://localhost:8888"), new Draft_6455()) {

                @Override
                public void onMessage(String message) {
                    System.out.println("incoming message = " + message);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("You are connected to Remote Python Server: " + getURI() + "\n");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason + "\n");
                }

                @Override
                public void onError(Exception ex) {
                    new Thread(() -> {
                        System.out.println("Remote Python Server is starting now");
                        executeCommand("python " + CDL.currDir + "\\scripts\\python\\server.py");
                    }).start();
                    System.out.println("Exception occured but it was fixed...\n" + ex + "\n");
                    isConnectPythonServer = false;
                    System.out.println("düzeltmek için bir daha deniyor");
                    connectPythonServer();
                    isConnectPythonServer = false;
                }
            };
            if (isConnectPythonServer) {
                client.connect();
            }

        } catch (URISyntaxException ex) {
            System.out.println("ws://localhost:8888" + " is not a valid WebSocket URI\n");
            isConnectPythonServer = false;
        }
        return isConnectPythonServer;
    }

    public static void delay(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            Logger.getLogger(CDL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void executeCommand(String cmd) {

        ProcessBuilder processBuilder = new ProcessBuilder();
        // Run this on Windows, cmd, /c = terminate after this run
//        processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com");
        //processBuilder.command("cmd.exe","","cd D:\Anaconda3\envs\cpu");
//        processBuilder.command("cmd.exe", "", "cd D:\\Anaconda3\\envs\\cpu");
//        processBuilder.command("cmd.exe", "/c", "python "+cmd);
//        processBuilder.directory(new File("C:\\Users\\DELL LAB\\Envs\\dl4c_cpu"));
//        processBuilder.command("cmd.exe", "workon dl4c_cpu");
//        processBuilder.command("cmd.exe","/c","workon dl4c_cpu"," & ", cmd);
//        processBuilder.command("cmd.exe", "/c", "python --version");
//        processBuilder.command("cmd.exe", "/c", cmd);
        processBuilder.command("cmd.exe", "/c", cmd);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
            if (exitCode == 1) {
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorString = error.readLine();
                while (errorString != null) {
                    System.out.println("errorString = " + errorString);
                    errorString = error.readLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
