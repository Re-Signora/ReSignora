package work.chiro.game.application;

import work.chiro.game.dao.HistoryImpl;
import work.chiro.game.dao.HistoryObject;
import work.chiro.game.scene.SceneClient;
import work.chiro.game.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class HistoryWindow implements SceneClient {
    private static HistoryWindow historyWindow = null;
    private JPanel mainPanel;
    private JLabel difficultyLabel;
    private JTable historyTable;
    private JButton deleteButton;
    private JButton restartButton;
    private JScrollPane scrollPane;
    private JLabel selectDifficultyLabel;
    private JComboBox<String> selectDifficultyComboBox;
    private final Object waitObject = new Object();
    private Difficulty selectedDifficulty = null;

    public HistoryWindow(boolean enableRestart) {
        restartButton.addActionListener(e -> nextScene());
        deleteButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
            int[] selectedRows = historyTable.getSelectedRows();
            List<HistoryObject> data = HistoryImpl.getInstance().getAll();
            LinkedList<HistoryObject> selected = new LinkedList<>();
            for (int selectedRow : selectedRows) {
                System.out.println("selectedRow = " + selectedRow + ", data = " + data.get(selectedRow));
                selected.add(data.get(selectedRow));
            }
            for (HistoryObject selectedHistoryObject : selected) {
                List<HistoryObject> copy = new ArrayList<>(HistoryImpl.getInstance().getAll());
                copy.removeIf(historyObject -> historyObject.getTime() == selectedHistoryObject.getTime());
                HistoryImpl.getInstance().set(copy);
            }
            syncWithDao();
        });
        syncWithDao();
        if (!enableRestart) {
            restartButton.setEnabled(false);
        }
        selectDifficultyComboBox.addItem("--未选择--");
        selectDifficultyComboBox.addItem("简单");
        selectDifficultyComboBox.addItem("中等");
        selectDifficultyComboBox.addItem("困难");
        selectDifficultyComboBox.addActionListener(e -> {
            String selectedLabel = (String) selectDifficultyComboBox.getSelectedItem();
            selectedDifficulty = Utils.difficultyFromString(selectedLabel);
            syncWithDao();
            difficultyLabel.setText(selectedLabel);
        });
        difficultyLabel.setText("--未选择--");
        historyTable.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
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
        HistoryImpl.getInstance().getAll().stream()
                .filter(historyObject -> selectedDifficulty == null || (historyObject.getDifficulty() == selectedDifficulty))
                .forEach(historyObject -> model.addRow(historyObject.getDataAsList().toArray(new Object[0])));
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
