package edu.hitsz.application;

import edu.hitsz.dao.HistoryImpl;
import edu.hitsz.dao.HistoryObject;
import edu.hitsz.scene.SceneClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Chiro
 */
public class HistoryWindow implements SceneClient {
    private static HistoryWindow historyWindow = null;
    private JPanel mainPanel;
    private JLabel difficulty;
    private JTable historyTable;
    private JButton deleteButton;
    private JButton modifyButton;
    private JButton restartButton;
    private JScrollPane scrollPane;
    private final Object waitObject = new Object();

    public HistoryWindow(boolean enableRestart) {
        restartButton.addActionListener(e -> nextScene());
        deleteButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
            int[] selectedRows = historyTable.getSelectedRows();
            List<HistoryObject> data = HistoryImpl.getInstance().getAll();
            for (int selectedRow : selectedRows) {
                System.out.println("selectedRow = " + selectedRow + ", data = " + data.get(selectedRow));
                HistoryImpl.getInstance().deleteByTime(data.get(selectedRow).getTime());
            }
            syncWithDao();
        });
        syncWithDao();
        if (!enableRestart) {
            restartButton.setEnabled(false);
        }
    }

    public HistoryWindow() {
        this(true);
    }

    public void syncWithDao() {
        HistoryImpl.getInstance().sort();
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        // 清除数据
        model.setRowCount(0);
        // 设置表头
        model.setColumnIdentifiers(HistoryObject.getLabels().toArray(new String[0]));
        // 增加列
        HistoryImpl.getInstance().getAll().forEach(historyObject -> model.addRow(historyObject.getDataAsList().toArray(new Object[0])));
        historyTable.setRowHeight(30);
        historyTable.setModel(model);
    }

    public static HistoryWindow getInstance() {
        if (historyWindow == null) {
            synchronized (HistoryWindow.class) {
                historyWindow = new HistoryWindow();
            }
        }
        historyWindow.syncWithDao();
        return historyWindow;
    }


    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Object getWaitObject() {
        return waitObject;
    }

    @Override
    public void nextScene() {
        synchronized (waitObject) {
            waitObject.notify();
        }
    }
}
