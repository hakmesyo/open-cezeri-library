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
    private T data=null;
    private Node<T> parent=null;
    private List<Node<T>> childNodes=new ArrayList();
    
    Node(Node<T> parent,T data){
        this.data=data;
        this.parent=parent;
        if (parent!=null) {
            parent.childNodes.add(this);
        }
    }
    
    public Node<T> getChildNode(int index){
        try {
            return childNodes.get(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String toString(){
        return data.toString();
    }
    
    public void println(){
        System.out.println(data);
    }
    
    public void printlnWholeTree(){
        System.out.println(data);
        for (Node<T> childNode : childNodes) {
            childNode.println();
        }
    }
    
    public Node<T> getParent(){
        return parent;
    }
    
    public void addChildNode(Node<T> node){
        node.parent=this;
        childNodes.add(node);
    }
    
    public void setData(T data){
        this.data=data;
    }
    
    public T getData(){
        return data;
    }
}
