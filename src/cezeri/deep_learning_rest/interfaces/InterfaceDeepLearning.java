/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_rest.interfaces;

/**
 *
 * @author DELL LAB
 */
public interface InterfaceDeepLearning {
    void setConfiguration(InterfaceConfiguration cnf);
    void build();
    void execute(int port);
}
