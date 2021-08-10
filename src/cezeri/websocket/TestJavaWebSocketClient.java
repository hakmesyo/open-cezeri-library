/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import processing.data.JSONObject;

/**
 *
 * @author cezerilab
 */
public class TestJavaWebSocketClient {
    WebSocketClient mWs = null;
    public static void main(String[] args) throws URISyntaxException {
        TestJavaWebSocketClient qw=new TestJavaWebSocketClient();
        qw.mWs = new WebSocketClient(new URI("ws://127.0.0.1:8887"), new Draft_6455()) {
            @Override
            public void onMessage(String message) {
                System.out.println("serverdan gelen mesaj:" + message);
                qw.geriGonder(message);
            }

            public void onOpen(ServerHandshake handshake) {
                System.out.println("opened connection");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("closed connection");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

        };
        //open websocket
        qw.mWs.connect();

//        JSONObject obj = new JSONObject();
//        obj.put("event", "addChannel");
//        obj.put("channel", "ok_btccny_ticker");
//        String message = obj.toString();
        //send message
        //mWs.send(message);
    }

    private void geriGonder(String message) {
        mWs.send("server mesajını geri gonderiyorum:" + message);
    }
}
