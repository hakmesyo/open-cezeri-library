/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.test;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JFrame;

public class TestPopupMenu extends JFrame {

    JMenuItem item1, item2;
    static JPopupMenu pop;

    public TestPopupMenu() {
        this.setSize(200, 150);
        item1 = new JMenuItem("This is Menu Item");
        item2 = new JMenuItem("This is another Menu Item");

        pop = new JPopupMenu();

        MouseListener popListener = new PopupListener();

        pop.add(item1);
        pop.add(item2);

        addMouseListener(popListener);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public static void main(String a[]) {
        TestPopupMenu obj=new TestPopupMenu();
        obj.setSize(200,200);
    }

}

class PopupListener extends MouseAdapter {

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            TestPopupMenu.pop.show(e.getComponent(),e.getX(), e.getY());
        }
    }

}
