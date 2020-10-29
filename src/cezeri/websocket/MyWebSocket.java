/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.websocket;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class MyWebSocket {
    private final static HashMap<String, MyWebSocket> sockets = new HashMap<>();
    private Session session;
    private String myUniqueId;

    private String getMyUniqueId() {
        // unique ID from this class' hash code
        return Integer.toHexString(this.hashCode());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        // save session so we can send
        this.session = session;

        // this unique ID
        this.myUniqueId = this.getMyUniqueId();

        // map this unique ID to this connection
        MyWebSocket.sockets.put(this.myUniqueId, this);

        // send its unique ID to the client (JSON)
        this.sendClient(String.format("{\"msg\": \"uniqueId\", \"uniqueId\": \"%s\"}",
                this.myUniqueId));

        // broadcast this new connection (with its unique ID) to all other connected clients
        for (MyWebSocket dstSocket : MyWebSocket.sockets.values()) {
            if (dstSocket == this) {
                // skip me
                continue;
            }
            dstSocket.sendClient(String.format("{\"msg\": \"newClient\", \"newClientId\": \"%s\"}",
                    this.myUniqueId));
        }
    }

    @OnWebSocketMessage
    public void onMsg(String msg) {
        /*
         * process message here with whatever JSON library or protocol you like
         * to get the destination unique ID from the client and the actual message
         * to be sent (not shown). also, make sure to escape the message string
         * for further JSON inclusion. 
         */
        String destUniqueId = "...";
        String escapedMessage = "...";

        // is the destination client connected?
        if (!MyWebSocket.sockets.containsKey(destUniqueId)) {
            this.sendError(String.format("destination client %s does not exist", destUniqueId));
            return;
        }

        // send message to destination client
        this.sendClient(String.format("{\"msg\": \"message\", \"destId\": \"%s\", \"message\": \"%s\"}",
                destUniqueId, escapedMessage));
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        if (MyWebSocket.sockets.containsKey(this.myUniqueId)) {
            // remove connection
            MyWebSocket.sockets.remove(this.myUniqueId);

            // broadcast this lost connection to all other connected clients
            for (MyWebSocket dstSocket : MyWebSocket.sockets.values()) {
                if (dstSocket == this) {
                    // skip me
                    continue;
                }
                dstSocket.sendClient(String.format("{\"msg\": \"lostClient\", \"lostClientId\": \"%s\"}",
                        this.myUniqueId));
            }
        }
    }

    private void sendClient(String str) {
        try {
            this.session.getRemote().sendString(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendError(String err) {
        this.sendClient(String.format("{\"msg\": \"error\", \"error\": \"%s\"}", err));
    }
    
    public static void main(String[] args) {
//        MyWebSocket mws=new MyWebSocket();        
        MyWebSocketClient ws=new MyWebSocketClient("ws://192.168.1.33:8887");
        
    }
}