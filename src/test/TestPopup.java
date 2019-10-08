/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class TestPopup extends JFrame {

    private JRadioButtonMenuItem items[];
    private Color colorValues[] = {Color.blue, Color.yellow, Color.red};

    public TestPopup() {
        super("Using JPopupMenus");

        final JPopupMenu popupMenu = new JPopupMenu();
        ItemHandler handler = new ItemHandler();
        String colors[] = {"Blue", "Yellow", "Red"};
        ButtonGroup colorGroup = new ButtonGroup();
        items = new JRadioButtonMenuItem[3];

      // construct each menu item and add to popup menu; also  
        // enable event handling for each menu item  
        for (int i = 0; i < items.length; i++) {
            items[ i] = new JRadioButtonMenuItem(colors[ i]);
            popupMenu.add(items[ i]);
            colorGroup.add(items[ i]);
            items[ i].addActionListener(handler);
        }

        getContentPane().setBackground(Color.white);

      // define a MouseListener for the window that displays  
        // a JPopupMenu when the popup trigger event occurs  
        addMouseListener(
                new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        checkForTriggerEvent(e);
                    }

                    public void mouseReleased(MouseEvent e) {
                        checkForTriggerEvent(e);
                    }

                    private void checkForTriggerEvent(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            popupMenu.show(e.getComponent(),
                                    e.getX(), e.getY());
                        }
                    }
                }
        );

        setSize(300, 200);
        show();
    }

    public static void main(String args[]) {
        TestPopup app = new TestPopup();

        app.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
    }

    private class ItemHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // determine which menu item was selected  
            for (int i = 0; i < items.length; i++) {
                if (e.getSource() == items[ i]) {
                    getContentPane().setBackground(colorValues[ i]);
                    repaint();
                    return;
                }
            }
        }
    }
}
