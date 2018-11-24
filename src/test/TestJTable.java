/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author BAP1
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;

public class TestJTable {

    public static void main(String[] args) {
        new TestJTable();
    }

    public TestJTable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JTable table = new JTable(new MyModel());
                final JPopupMenu pm = new JPopupMenu();
                pm.add(new CopyAction(table));
                pm.add(new PasteAction(table));

                table.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            highlightRow(e);
                            doPopup(e);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            highlightRow(e);
                            doPopup(e);
                        }
                    }

                    protected void doPopup(MouseEvent e) {
                        pm.show(e.getComponent(), e.getX(), e.getY());
                    }

                    protected void highlightRow(MouseEvent e) {
                        JTable table = (JTable) e.getSource();
                        Point point = e.getPoint();
                        int row = table.rowAtPoint(point);
                        int col = table.columnAtPoint(point);

                        table.setRowSelectionInterval(row, row);
                        table.setColumnSelectionInterval(col, col);
                    }

                });

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(table);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }

    public class MyModel extends AbstractTableModel {

        private String[] names = {"1", "2", "3", "4", "5"};
        private String[][] values = new String[5][5];

        public MyModel() {
            values = new String[10][names.length];
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < names.length; col++) {
                    values[row][col] = String.valueOf((char) ((row * names.length) + col + 65));
                }
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (value instanceof Double || value instanceof Integer) {
                values[row][col] = value.toString();
            } else {
                values[row][col] = (String) value;
            }

            fireTableCellUpdated(row, col);
        }

        @Override
        public int getRowCount() {
            return values.length;
        }

        @Override
        public int getColumnCount() {
            return names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return values[rowIndex][columnIndex];
        }

    }

    class CopyAction extends AbstractAction {

        private JTable table;

        public CopyAction(JTable table) {
            this.table = table;
            putValue(NAME, "Copy");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new CellTransferable(table.getValueAt(row, col)), null);

        }

    }

    class CutAction extends AbstractAction {

        private JTable table;

        public CutAction(JTable table) {
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new CellTransferable(table.getValueAt(row, col)), null);

        }

    }

    class PasteAction extends AbstractAction {

        private JTable table;

        public PasteAction(JTable tbl) {

            putValue(NAME, "Paste");

            table = tbl;

            final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

            cb.addFlavorListener(new FlavorListener() {
                @Override
                public void flavorsChanged(FlavorEvent e) {
                    setEnabled(cb.isDataFlavorAvailable(CellTransferable.CELL_DATA_FLAVOR));
                }
            });
            setEnabled(cb.isDataFlavorAvailable(CellTransferable.CELL_DATA_FLAVOR));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (cb.isDataFlavorAvailable(CellTransferable.CELL_DATA_FLAVOR)) {
                try {
                    Object value = cb.getData(CellTransferable.CELL_DATA_FLAVOR);
                    System.out.println(value);
                    table.setValueAt(value, row, col);

                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static class CellTransferable implements Transferable {

        public static final DataFlavor CELL_DATA_FLAVOR = new DataFlavor(Object.class, "application/x-cell-value");

        private Object cellValue;

        public CellTransferable(Object cellValue) {
            this.cellValue = cellValue;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{CELL_DATA_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return CELL_DATA_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return cellValue;
        }

    }

}
