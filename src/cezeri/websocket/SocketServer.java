/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.websocket;

import static cezeri.factory.FactorySocket.running;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketServer extends WebSocketServer {

    public static boolean isClientConnectedNow = false;
    public static InterfaceCallBack icbf = null;

    public SocketServer(String ip, int port, InterfaceCallBack icb) throws UnknownHostException {
        super(new InetSocketAddress(new InetSocketAddress(ip, port).getAddress(), port));
        icbf=icb;
    }

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
//        broadcast("[new client connection]: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
        broadcast("[new client connection]: " + conn.getRemoteSocketAddress().getAddress().getHostAddress()); //This method sends a message to all clients connected
        System.out.println("New Client " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to Java Server!");
        isClientConnectedNow = true;
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        super.onWebsocketPing(conn, f);
        System.out.println(f.getOpcode() + " from " + conn.getLocalSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
        isClientConnectedNow = false;
    }

    long t = 0;

    @Override
    public void onMessage(WebSocket conn, String message) {
        icbf.onMessageReceived(message);
        if (message.equals("stop")) {
            running.set(false);

        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
//		broadcast( message.array() );
        System.out.println(conn + " bak hele: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
//        System.out.println("Java WebSocket Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}
