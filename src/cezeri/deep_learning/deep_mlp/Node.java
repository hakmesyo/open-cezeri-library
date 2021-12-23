/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.deep_mlp;

import cezeri.factory.FactoryUtils;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class Node {
    double data;
    double weight;  
    double freezed_weight;  
    double[] weightsToOutputLayer;
    double[] freezed_weightsToOutputLayer;
    Node nextNode;
    //Node[][] prevNodes;
    Channel ch;
    int px;
    int py;
    boolean isBias = false;

    public Node(Channel ch, int px, int py) {
        this.ch = ch;
        this.px = px;
        this.py = py;
        if (px == -1 && py == -1) {
            isBias = true;
            data = 1;
        }
    }

    public String toString() {
        return "pos:" + px + "," + py + "; data:" + data+" ; weight:"+FactoryUtils.formatDouble(weight);
    }

    public Node dump() {
        System.out.println(toString());
        return this;
    }

    public Node traceForward() {
        if (isBias) {
            System.out.println("bias node can't be traced");
            return this;
        }
        System.out.println("");
        Node temp = this;
        while (temp.nextNode != null) {
            temp = temp.dump().nextNode;
        }
        temp = temp.dump().nextNode;
        return this;
    }

}
