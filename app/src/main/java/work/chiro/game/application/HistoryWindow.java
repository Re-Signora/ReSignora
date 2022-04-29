package work.chiro.game.application;

import work.chiro.game.dao.HistoryImpl;
import work.chiro.game.dao.HistoryObject;
import work.chiro.game.scene.AbstractSceneClient;
import work.chiro.game.utils.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Chiro
 */
public class HistoryWindow extends AbstractSceneClient {
    private static HistoryWindow historyWindow = null;
    private final JPanel mainPanel = new JPanel();
    private final JLabel difficultyLabel = new JLabel();
    private final JTable historyTable = new JTable();
    private final JComboBox<String> selectDifficultyComboBox = new JComboBox<>();
    private final Object waitObject = new Object();
    private Difficulty selectedDifficulty = null;

    public HistoryWindow(boolean enableRestart) {
        JButton restartButton = new JButton();
        restartButton.addActionListener(e -> nextScene(MainWindow.class));
        restartButton.setText("重新开始");
        JButton deleteButton = new JButton();
        deleteButton.setText("删除项目");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            synchronized public void actionPerformed(ActionEvent e) {
                int[] selectedRows = historyTable.getSelectedRows();
                if (selectedRows.length == 0) {
                    return;
                }
                List<HistoryObject> data = HistoryImpl.getInstance().getByDifficulty(selectedDifficulty);
                LinkedList<HistoryObject> selected = new LinkedList<>();
                int willDelete = JOptionPane.showConfirmDialog(null, "删除" + (selectedRows.length == 1 ? "该" : "这些") + "记录吗?");
                if (willDelete != JOptionPane.YES_OPTION) {
                    return;
                }
                for (int selectedRow : selectedRows) {
                    System.out.println("selectedRow = " + selectedRow + ", data = " + data.get(selectedRow));
                    selected.add(data.get(selectedRow));
                }
                for (HistoryObject selectedHistoryObject : selected) {
                    List<HistoryObject> copy = new ArrayList<>(HistoryImpl.getInstance().getAll());
                    copy.removeIf(historyObject -> historyObject.getTime() == selectedHistoryObject.getTime());
                    HistoryImpl.getInstance().set(copy);
                }
                HistoryWindow.this.syncWithDao();
            }
        });
        JButton quitButton = new JButton();
        quitButton.setText("退出游戏");
        quitButton.addActionListener(e -> System.exit(0));
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
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("难度:"));
        topPanel.add(difficultyLabel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(restartButton);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        JPanel selectPanel = new JPanel();
        selectPanel.add(new JLabel("选择难度"));
        selectPanel.add(selectDifficultyComboBox);
        bottomPanel.add(selectPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        syncWithDao();
    }

    public HistoryWindow() {
        this(true);
    }

    synchronized public void syncWithDao() {
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
}
