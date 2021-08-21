/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.interfaces;

import cezeri.enums.EnumOperatingSystem;
import cezeri.websocket.SocketServer;

/**
 *
 * @author DELL LAB
 */
public interface InterfaceDeepLearning {
    void setConfiguration(InterfaceConfiguration cnf);
    SocketServer execute();
}
