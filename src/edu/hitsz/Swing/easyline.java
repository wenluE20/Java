package edu.hitsz.Swing;

import edu.hitsz.dao.GameRecord;
import edu.hitsz.dao.GameRecordService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class easyline {
    private JTable scoretable;
    private JPanel easyPanel;
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private JLabel headerLabel;
    private JPanel deletePanel;
    private JButton deletebutton;
    private JLabel modelLabel;
    private GameRecordService gameRecordService;
    private String difficulty = "EASY";
    private DefaultTableModel tableModel;

    public easyline() {
        // 初始化游戏记录服务
        gameRecordService = new GameRecordService();
        
        // 设置表格模型和列名
        tableModel = new DefaultTableModel(new Object[]{"排名", "玩家名称", "得分", "记录时间"}, 0);
        scoretable.setModel(tableModel);
        
        // 加载排行榜数据
        loadLeaderboardData();
        
        // 设置删除按钮事件
        deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecord();
            }
        });
    }
    
    // 加载排行榜数据
    private void loadLeaderboardData() {
        // 清空表格数据
        tableModel.setRowCount(0);
        
        // 获取排行榜数据
        List<GameRecord> records = gameRecordService.getTopRecords(difficulty, 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // 添加数据到表格
        for (int i = 0; i < records.size(); i++) {
            GameRecord record = records.get(i);
            Object[] rowData = {
                (i + 1),
                record.getPlayerName(),
                record.getScore(),
                dateFormat.format(record.getRecordTime())
            };
            tableModel.addRow(rowData);
        }
    }
    
    // 删除选中的记录
    private void deleteSelectedRecord() {
        int selectedRow = scoretable.getSelectedRow();
        if (selectedRow >= 0) {
            // 获取选中行的记录
            List<GameRecord> records = gameRecordService.getTopRecords(difficulty, 10);
            if (selectedRow < records.size()) {
                GameRecord recordToDelete = records.get(selectedRow);
                
                // 显示确认对话框
                int confirm = JOptionPane.showConfirmDialog(
                        easyPanel,
                    "确定要删除这条记录吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // 删除记录
                    gameRecordService.deleteRecord(recordToDelete.getId());
                    // 重新加载数据
                    loadLeaderboardData();
                    JOptionPane.showMessageDialog(easyPanel, "记录已删除");
                }
            }
        } else {
            JOptionPane.showMessageDialog(easyPanel, "请先选择要删除的记录");
        }
    }
    
    // 获取主面板的方法
    public JPanel getNormalPanel() {
        return easyPanel;
    }
}
