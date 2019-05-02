/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elcezerilab
 */
public class Node<T> {
    
    private T data = null;
    private Node<T> parent = null;
    private List<Node<T>> childNodes = new ArrayList();
    
    public Node(Node<T> parent, T data) {
        this.data = data;
        this.parent = parent;
        if (parent != null) {
            parent.childNodes.add(this);
        }
    }
    
    public Node<T> getChildNode(int index) {
        try {
            return childNodes.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString() {
//        return data.toString();
        if (parent == null) {
            return data.toString();
        }else{
            return data.toString()+parent;
        }
//        if (parent != null) {
//            return "[PARENT ="+ parent.toString()+"]" + data.toString();
//
//        } else {
//            return "[ROOT="+data.toString()+"]";
//
//        }
    }
    
    public void println() {
        System.out.println(this);
    }
    
    public void printlnWholeTree() {
        System.out.println(this);
        for (Node<T> childNode : childNodes) {
            childNode.printlnWholeTree();
        }
    }
    
    public Node<T> getParent() {
        return parent;
    }
    
    public void addChildNode(Node<T> node) {
        node.parent = this;
        childNodes.add(node);
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
}
