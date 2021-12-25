/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.interfaces.call_back_interface;

@FunctionalInterface
public interface CallBackWebSocket {
    public abstract void onMessageReceived(String str);
}
