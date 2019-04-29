/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.data_structure;

import java.util.Enumeration;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author elcezerilab
 */
public class TestTreeDS {
    public static void main(String[] args) {
        Node root=new Node(null, 0);
        Node n1=new Node(root, 11);
        Node n2=new Node(root, 12);
        Node n3=new Node(root, 13);
        Node n1_1=new Node(n1, 3);
        Node n1_2=new Node(n1, -3);
        Node n2_1=new Node(n2, 32);
        Node n2_2=new Node(n2, -32);
        root.println();
        root.getChildNode(1).println();
        root.getChildNode(1).getParent().printlnWholeTree();
    }
}
