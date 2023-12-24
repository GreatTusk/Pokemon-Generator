/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import ImageManagement.ImageIconCellRenderer;
import Interface.IDefaultAttributes;
import java.awt.Dimension;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.Move;
import soundManagement.SoundPlayer;

/**
 *
 * @author F776
 */
public class MovesTable extends javax.swing.JFrame implements IDefaultAttributes , Serializable {

    private final HashMap<Integer, ImageIcon> listTypeImage = MainWindow.getInstance().getTypeImages();
    private final ArrayList<Move> listMove = MainWindow.getInstance().getMoves();
    private boolean isTableFilled = false;

    /**
     * Creates new form MovesTable
     */
    public MovesTable() {
        initComponents();

        if (!isTableFilled) {
            fillMoveTbl();
            isTableFilled = true;
        }
        this.setLocationRelativeTo(null);
    }

    public MovesTable(Move move) {
        initComponents();
        fillMoveTbl(move);
        this.setSize(new Dimension(1300, 150));
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        tblMoves = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Moves Pokedex");
        setIconImage(MainWindow.getPokeballImage().getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tblMoves.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Category", "Type", "Power", "Accuracy", "PP", "Desciption", "Effect Probability"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMoves.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblMoves);
        if (tblMoves.getColumnModel().getColumnCount() > 0) {
            tblMoves.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblMoves.getColumnModel().getColumn(0).setMaxWidth(50);
            tblMoves.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblMoves.getColumnModel().getColumn(2).setMaxWidth(60);
            tblMoves.getColumnModel().getColumn(3).setPreferredWidth(85);
            tblMoves.getColumnModel().getColumn(3).setMaxWidth(85);
            tblMoves.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblMoves.getColumnModel().getColumn(4).setMaxWidth(40);
            tblMoves.getColumnModel().getColumn(5).setPreferredWidth(40);
            tblMoves.getColumnModel().getColumn(5).setMaxWidth(40);
            tblMoves.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblMoves.getColumnModel().getColumn(6).setMaxWidth(40);
            tblMoves.getColumnModel().getColumn(8).setPreferredWidth(40);
            tblMoves.getColumnModel().getColumn(8).setMaxWidth(40);
        }

        getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        SoundPlayer.menuClosed();
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblMoves;
    // End of variables declaration//GEN-END:variables

    /**
     * Este método llena la tabla tblMoves con todos los movimientos de la base
     * de datos. Se ejecuta al inicio del programa para poder ser consultada en
     * cualquier momento.
     */
    private void fillMoveTbl() {
        DefaultTableModel tbl = (DefaultTableModel) this.tblMoves.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tbl);
        this.tblMoves.setRowSorter(sorter);
        tbl.setRowCount(0);

        ImageIconCellRenderer renderer = new ImageIconCellRenderer();
        this.tblMoves.getColumnModel().getColumn(2).setCellRenderer(renderer);
        this.tblMoves.getColumnModel().getColumn(3).setCellRenderer(renderer);
        for (Move mi : listMove) {
            tbl.addRow(new Object[]{
                mi.getId(),
                mi.getName(),
                getMoveCategoryIcon(mi.getIdMoveCat().getId()),
                getTypeImage(mi.getIdType().getId()),
                mi.getPower() == 0 ? "—" : mi.getPower(),
                mi.getAccuracy() == 0 ? "—" : mi.getAccuracy(),
                mi.getPp() == 0 ? "—" : mi.getPp(),
                mi.getEffect(),
                mi.getEffectProb() == 0 ? "—" : mi.getPp()
            });
        }
    }

    private void fillMoveTbl(Move mi) {
        DefaultTableModel tbl = (DefaultTableModel) this.tblMoves.getModel();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tbl);
        this.tblMoves.setRowSorter(sorter);
        tbl.setRowCount(0);

        ImageIconCellRenderer renderer = new ImageIconCellRenderer();
        this.tblMoves.getColumnModel().getColumn(2).setCellRenderer(renderer);
        this.tblMoves.getColumnModel().getColumn(3).setCellRenderer(renderer);

        tbl.addRow(new Object[]{
            mi.getId(),
            mi.getName(),
            getMoveCategoryIcon(mi.getIdMoveCat().getId()),
            getTypeImage(mi.getIdType().getId()),
            mi.getPower() == 0 ? "—" : mi.getPower(),
            mi.getAccuracy() == 0 ? "—" : mi.getAccuracy(),
            mi.getPp() == 0 ? "—" : mi.getPp(),
            mi.getEffect(),
            mi.getEffectProb() == 0 ? "—" : mi.getPp()
        });

    }

    private ImageIcon getMoveCategoryIcon(int id) {
        switch (id) {
            case 1:
                return PHYSICAL_MOVE;
            case 2:
                return SPECIAL_MOVE;
            case 3:
                return STATUS_MOVE;
            default:
                return POKEBALL_IMAGE;
        }
    }

    private ImageIcon getTypeImage(int id) {
        return new ImageIcon(listTypeImage.get(id).getImage().getScaledInstance(75, 15, Image.SCALE_SMOOTH));
    }

}